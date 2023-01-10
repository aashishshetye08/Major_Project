//---------------------------------------------------------------------------------------
// Copyright (c) 2001-2019 by PDFTron Systems Inc. All Rights Reserved.
// Consult legal.txt regarding legal and license information.
//-------------------------------------------------------------------------

package com.pdftron.pdf.controls;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.DialogFragment;

import com.pdftron.common.Matrix2D;
import com.pdftron.common.PDFNetException;
import com.pdftron.filters.MappedFile;
import com.pdftron.pdf.PDFDoc;
import com.pdftron.pdf.PDFRasterizer;
import com.pdftron.pdf.PDFViewCtrl;
import com.pdftron.pdf.Page;
import com.pdftron.pdf.PageIterator;
import com.pdftron.pdf.Point;
import com.pdftron.pdf.Rect;
import com.pdftron.pdf.tools.R;
import com.pdftron.pdf.tools.ToolManager;
import com.pdftron.pdf.utils.AnalyticsHandlerAdapter;
import com.pdftron.pdf.utils.AnalyticsParam;
import com.pdftron.pdf.utils.CommonToast;
import com.pdftron.pdf.utils.CustomAsyncTask;
import com.pdftron.pdf.utils.ExceptionHandlerCallback;
import com.pdftron.pdf.utils.ImageMemoryCache;
import com.pdftron.pdf.utils.PdfViewCtrlSettingsManager;
import com.pdftron.pdf.utils.Utils;
import com.pdftron.pdf.utils.ViewerUtils;
import com.pdftron.pdf.widget.ContentLoadingRelativeLayout;
import com.pdftron.pdf.widget.PTCropImageView;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.HashSet;

/**
 * The UserCropDialogFragment shows manual crop dialog.
 */
public class UserCropDialogFragment extends DialogFragment {

    private static final String TAG = UserCropDialogFragment.class.getName();

    // Default memory cache size in kilobytes
    private static final int DEFAULT_MEM_CACHE_SIZE = 1024 * 50; // 50MB

    // The number of images to render ahead and behind the current page
    private static final int MAX_PAGES_TO_PRERENDER_PER_DIRECTION = 4;

    private static final long MILLISECONDS_BEFORE_SHOWING_PROGRESS = 500;

    private boolean mIsActive;

    private int mCurrentPage;
    private int mTotalPages;
    private PDFViewCtrl mPdfViewCtrl;

    // layout parameters
    private RelativeLayout mCropPageHost;
    private View mBlankPagePlaceholder;
    private ContentLoadingRelativeLayout mBlankPageSpinnerHost;
    private View mCropImageBorder;
    private PTCropImageView mCropImageView;
    private TextView mPageNumberTextView;
    private ProgressBar mProgressBar;
    private Button mButtonUseOnEvenOdd;
    private Button mButtonApply;

    // Pre-rendering
    private int mPagesToPreRenderPerDirection;

    private OnUserCropDialogDismissListener mOnUserCropDialogDismissListener;

    private int mCropPageDetails = AnalyticsHandlerAdapter.CROP_PAGE_ONE_PAGE;
    private boolean mIsCropped = false;

    // modified page count
    private final HashSet<Integer> mModifiedPages = new HashSet<>();
    private boolean mUseOnAllClicked;
    private boolean mUseOnEvenClicked;
    private boolean mUseOnOddClicked;

    /**
     * Callback interface to be invoked when the user crop dialog is dismissed.
     */
    public interface OnUserCropDialogDismissListener {
        /**
         * Called when the user crop dialog has been dismissed.
         *
         * @param pageNumberAtDismiss The current page number when it was dismissed
         */
        void onUserCropDialogDismiss(int pageNumberAtDismiss);
    }

    // Page Properties /////////////////////////////////////////////////////////

    private static class PageCropProperties {

        Rect mUserCropBox;
        Rect mCropBox;
        int mRotation;

        PageCropProperties() {
        }
    }

    private PageCropProperties[] mPageProperties;

    // Bitmap Handling /////////////////////////////////////////////////////////

    /**
     * Returns a new instance of the class.
     */
    public static UserCropDialogFragment newInstance() {
        return new UserCropDialogFragment();
    }

    public UserCropDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null && mPdfViewCtrl == null) {
            this.dismiss();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_user_crop_dialog, null);
        int width = 0;
        int height = 0;
        WindowManager wm = (WindowManager) view.getContext().getSystemService(Context.WINDOW_SERVICE);
        if (wm != null) {
            Display display = wm.getDefaultDisplay();
            android.graphics.Point size = new android.graphics.Point();
            display.getSize(size);
            width = size.x - 10;
            height = size.y - 10;
        }

        int maxImageSize = width * height * 4;
        if (maxImageSize > 0) {
            int maxImages = (DEFAULT_MEM_CACHE_SIZE * 1000) / maxImageSize;
            if (maxImages > 0) {
                mPagesToPreRenderPerDirection = Math.min(MAX_PAGES_TO_PRERENDER_PER_DIRECTION, (maxImages - 1) / 2);
            }
        }
        initUI(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adjustForOrientation(getResources().getConfiguration().orientation);
    }

    /**
     * Sets the {@link PDFViewCtrl}
     *
     * @param pdfViewCtrl The {@link PDFViewCtrl}
     * @return This class
     */
    public UserCropDialogFragment setPdfViewCtrl(@NonNull PDFViewCtrl pdfViewCtrl) {
        mIsActive = true;
        mPdfViewCtrl = pdfViewCtrl;
        mCurrentPage = pdfViewCtrl.getCurrentPage();
        boolean shouldUnlockRead = false;
        try {
            pdfViewCtrl.docLockRead();
            shouldUnlockRead = true;
            mTotalPages = pdfViewCtrl.getDoc().getPageCount();
            int totalItems = mTotalPages + 1;
            mPageProperties = new PageCropProperties[totalItems];
            resetModifiedPage();
        } catch (Exception ex) {
            AnalyticsHandlerAdapter.getInstance().sendException(ex);
        } finally {
            if (shouldUnlockRead) {
                pdfViewCtrl.docUnlockRead();
            }
        }

        mPagesToPreRenderPerDirection = 0;

        ImageMemoryCache.getInstance().setMemCacheSize(DEFAULT_MEM_CACHE_SIZE);

        return this;
    }

    private void initUI(final View view) {
        mPageNumberTextView = view.findViewById(R.id.page_num_text_view);

        mCropPageHost = view.findViewById(R.id.page_crop_host);

        mCropImageBorder = view.findViewById(R.id.image_crop_border);

        mCropImageView = view.findViewById(R.id.image_crop_view);
        mCropImageView.setGuidelines(0); // off
        mCropImageView.setPTCropImageViewListener(new PTCropImageView.PTCropImageViewListener() {
            @Override
            public void cropWindowChanged() {
                mModifiedPages.add(mCurrentPage);
                updateCTA();
            }
        });

        mCropImageBorder.setVisibility(View.GONE);

        mBlankPagePlaceholder = view.findViewById(R.id.blank_page_placeholder);
        mBlankPageSpinnerHost = view.findViewById(R.id.blank_page_progress_bar_host);

        mProgressBar = view.findViewById(R.id.progress_bar);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.pref_viewmode_user_crop);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserCropDialogFragment.this.dismiss();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_reset) {
                    resetModifiedPage();
                    mPageProperties = new PageCropProperties[mTotalPages + 1];
                    setPage(mCurrentPage);
                    updateCTA();
                    return true;
                }
                return false;
            }
        });

        mCropPageHost.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mIsActive) {
                    mCropImageBorder.setVisibility(View.VISIBLE);
                    setPage(mCurrentPage);
                }
            }
        }, 200); // Extra delay to ensure that view has time to settle. post alone doesn't work.

        getButtons(view);
        updateCTA();
    }

    private void getButtons(final View view) {
        final ImageButton nextButton = view.findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                switchPage(true);
            }
        });
        final ImageButton prevButton = view.findViewById(R.id.prev_button);
        prevButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                switchPage(false);
            }
        });

        if (Utils.isRtlLayout(getContext())) {
            nextButton.setImageResource(R.drawable.ic_chevron_left_black_24dp);
            prevButton.setImageResource(R.drawable.ic_chevron_right_black_24dp);
        }

        Button buttonUseOnAll = view.findViewById(R.id.all_pages_button);
        mButtonUseOnEvenOdd = view.findViewById(R.id.odd_even_pages_button);

        buttonUseOnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUseOnAllClicked = true;
                applyCropToPagesAndFlash(MultiApplyMode.All);
                mCropPageDetails = AnalyticsHandlerAdapter.CROP_PAGE_ALL_PAGES;
            }
        });
        mButtonUseOnEvenOdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentPage % 2 == 0) {
                    mUseOnEvenClicked = true;
                } else {
                    mUseOnOddClicked = true;
                }
                applyCropToPagesAndFlash(mCurrentPage % 2 == 0 ? MultiApplyMode.Even : MultiApplyMode.Odd);
                mCropPageDetails = AnalyticsHandlerAdapter.CROP_PAGE_EVEN_ODD_PAGES;
            }
        });
        mButtonUseOnEvenOdd.setText(mCurrentPage % 2 == 0 ? R.string.user_crop_manual_crop_even_pages : R.string.user_crop_manual_crop_odd_pages);

        mButtonApply = view.findViewById(R.id.apply_button);
        if (!Utils.isLollipop()) {
            mButtonApply.setBackgroundColor(Utils.getAccentColor(mButtonApply.getContext()));
        }
        mButtonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commitCrop();
            }
        });
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        adjustForOrientation(newConfig.orientation);

        if (mCurrentJob != null && !mCurrentJob.isDone()) {
            mCurrentJob.cancelRasterizing();
        }

        mCurrentJob = null;

        if (mCropImageView.hasBitmap()) {
            updatePageCropFromImageView(mPageProperties[mCurrentPage], mCropImageView.getCropRectPercentageMargins());
            mCropImageView.setImageBitmap(null);
        }

        ImageMemoryCache.getInstance().clearCache();

        View view = getView();
        if (view != null) {
            initUI(view);
        }
    }

    private void adjustForOrientation(int orientation) {
        View root = getView();
        if (root == null) {
            return;
        }
        ConstraintLayout view = root.findViewById(R.id.root_view);
        View cropRegion = root.findViewById(R.id.crop_region_layout);
        View applyButton = root.findViewById(R.id.apply_button);
        int cropRegionMargin = cropRegion.getContext().getResources().getDimensionPixelSize(R.dimen.user_crop_image_view_margin);
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // general layout
            ViewGroup.LayoutParams lp = applyButton.getLayoutParams();
            lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
            applyButton.setLayoutParams(lp);

            int applyButtonMargin = applyButton.getContext().getResources().getDimensionPixelSize(R.dimen.user_crop_apply_button_margin);

            // constraints
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(view);

            constraintSet.connect(R.id.crop_region_layout, ConstraintSet.START, ConstraintLayout.LayoutParams.PARENT_ID, ConstraintSet.START, 0);
            constraintSet.connect(R.id.crop_region_layout, ConstraintSet.END, R.id.apply_button, ConstraintSet.START, 0);
            constraintSet.connect(R.id.crop_region_layout, ConstraintSet.TOP, R.id.app_bar_layout, ConstraintSet.BOTTOM, cropRegionMargin);
            constraintSet.connect(R.id.crop_region_layout, ConstraintSet.BOTTOM, ConstraintLayout.LayoutParams.PARENT_ID, ConstraintSet.BOTTOM, cropRegionMargin);

            constraintSet.connect(R.id.card_view, ConstraintSet.TOP, R.id.app_bar_layout, ConstraintSet.BOTTOM);

            constraintSet.connect(R.id.apply_button, ConstraintSet.START, R.id.crop_region_layout, ConstraintSet.END, applyButtonMargin);
            constraintSet.connect(R.id.apply_button, ConstraintSet.END, ConstraintLayout.LayoutParams.PARENT_ID, ConstraintSet.END, applyButtonMargin);
            constraintSet.connect(R.id.apply_button, ConstraintSet.TOP, R.id.odd_even_pages_button, ConstraintSet.BOTTOM, applyButtonMargin);
            constraintSet.connect(R.id.apply_button, ConstraintSet.BOTTOM, ConstraintLayout.LayoutParams.PARENT_ID, ConstraintSet.BOTTOM, applyButtonMargin);

            constraintSet.applyTo(view);
        } else {
            // general layout
            ViewGroup.LayoutParams lp = applyButton.getLayoutParams();
            lp.width = 0;
            applyButton.setLayoutParams(lp);

            int applyButtonMargin = applyButton.getContext().getResources().getDimensionPixelSize(R.dimen.user_crop_apply_button_vertical_margin);

            // constraints
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(view);

            constraintSet.connect(R.id.crop_region_layout, ConstraintSet.START, ConstraintLayout.LayoutParams.PARENT_ID, ConstraintSet.START, cropRegionMargin);
            constraintSet.connect(R.id.crop_region_layout, ConstraintSet.END, ConstraintLayout.LayoutParams.PARENT_ID, ConstraintSet.END, cropRegionMargin);
            constraintSet.connect(R.id.crop_region_layout, ConstraintSet.TOP, R.id.app_bar_layout, ConstraintSet.BOTTOM, 0);
            constraintSet.connect(R.id.crop_region_layout, ConstraintSet.BOTTOM, R.id.card_view, ConstraintSet.TOP, 0);

            constraintSet.connect(R.id.card_view, ConstraintSet.TOP, R.id.crop_region_layout, ConstraintSet.BOTTOM);

            constraintSet.connect(R.id.apply_button, ConstraintSet.START, R.id.crop_region_layout, ConstraintSet.START, 0);
            constraintSet.connect(R.id.apply_button, ConstraintSet.END, R.id.crop_region_layout, ConstraintSet.END, 0);
            constraintSet.connect(R.id.apply_button, ConstraintSet.TOP, R.id.odd_even_pages_button, ConstraintSet.BOTTOM, 0);
            constraintSet.connect(R.id.apply_button, ConstraintSet.BOTTOM, ConstraintLayout.LayoutParams.PARENT_ID, ConstraintSet.BOTTOM, applyButtonMargin);

            constraintSet.applyTo(view);
        }
    }

    private void resetModifiedPage() {
        mModifiedPages.clear();
        mUseOnAllClicked = false;
        mUseOnEvenClicked = false;
        mUseOnOddClicked = false;
    }

    private void updateCTA() {
        String ctaString = getString(R.string.tools_qm_crop);
        int pageCount = mModifiedPages.size();
        if (mUseOnAllClicked || (mUseOnEvenClicked && mUseOnOddClicked)) {
            pageCount = mTotalPages;
        } else {
            if (mUseOnEvenClicked) {
                pageCount = (int) Math.floor(mTotalPages / 2f);
            } else if (mUseOnOddClicked) {
                pageCount = (int) Math.ceil(mTotalPages / 2f);
            }
        }
        ctaString = ctaString + "(" + pageCount + ")";
        mButtonApply.setText(ctaString);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mCurrentJob != null && !mCurrentJob.isDone()) {
            mCurrentJob.cancelRasterizing();
        }

        if (mOnUserCropDialogDismissListener != null) {
            mOnUserCropDialogDismissListener.onUserCropDialogDismiss(mCurrentPage);
        }

        if (!mIsCropped) {
            AnalyticsHandlerAdapter.getInstance().sendEvent(AnalyticsHandlerAdapter.EVENT_CROP_PAGES,
                    AnalyticsParam.cropPageParam(AnalyticsHandlerAdapter.CROP_PAGE_CANCEL_MANUAL));
        }
    }

    @Override
    public void onPause() {
        if (mCurrentJob != null && !mCurrentJob.isDone()) {
            mCurrentJob.cancelRasterizing();
        }

        super.onPause();
    }

    @Override
    public void onDestroy() {
        mIsActive = false;
        ImageMemoryCache.getInstance().clearCache();
        super.onDestroy();
    }

    /**
     * Sets the OnUserCropDialogDismissListener listener
     *
     * @param listener The listener
     */
    public void setOnUserCropDialogDismissListener(OnUserCropDialogDismissListener listener) {
        mOnUserCropDialogDismissListener = listener;
    }

    private void commitCrop() {
        mIsCropped = true;
        setCropBoxAndClose();
        AnalyticsHandlerAdapter.getInstance().sendEvent(AnalyticsHandlerAdapter.EVENT_CROP_PAGES,
                AnalyticsParam.cropPageParam(AnalyticsHandlerAdapter.CROP_PAGE_MANUAL, mCropPageDetails));
    }

    private void switchPage(boolean next) {
        if (!next && mCurrentPage > 1) {
            setPage(mCurrentPage - 1);
        } else if (next) {
            boolean shouldUnlockRead = false;
            try {
                mPdfViewCtrl.docLockRead();
                shouldUnlockRead = true;
                int pageCount = mPdfViewCtrl.getDoc().getPageCount();
                if (mCurrentPage < pageCount) {
                    setPage(mCurrentPage + 1);
                }
            } catch (Exception ex) {
                AnalyticsHandlerAdapter.getInstance().sendException(ex);
            } finally {
                if (shouldUnlockRead) {
                    mPdfViewCtrl.docUnlockRead();
                }
            }
        }
    }

    // Bitmap Acquisition //////////////////////////////////////////////////////

    private DrawImageTask mCurrentJob;

    private class DrawImageTask extends CustomAsyncTask<Void, Void, Bitmap> {

        private final int mPageNumber;
        private PDFRasterizer mRasterizer;
        private final WeakReference<PDFViewCtrl> mPDFViewCtrlRef;
        private final Point mViewDimensions;
        private final PDFDoc mPdfDoc;
        private boolean mGotBitmap;

        private boolean mIsCancelled;

        public int getPageNumber() {
            return mPageNumber;
        }

        public boolean cancelRasterizing() {
            if (mRasterizer != null) {
                try {
                    mRasterizer.setCancel(true);
                } catch (Exception e) {
                    AnalyticsHandlerAdapter.getInstance().sendException(e);
                }
            }
            return this.cancel(false);
        }

        /**
         * Returns true if the task is finished or cancelled
         */
        public boolean isDone() {
            return mGotBitmap || isCancelled();
        }

        public DrawImageTask(Context context, PDFViewCtrl pdfViewCtrl, int pageNumber, Point viewDimensions) {
            super(context);
            mPageNumber = pageNumber;
            mViewDimensions = viewDimensions;
            mPdfDoc = pdfViewCtrl.getDoc();
            mGotBitmap = false;
            mPDFViewCtrlRef = new WeakReference<>(pdfViewCtrl);

            mIsCancelled = false;

            if (viewDimensions.x <= 0 || viewDimensions.y <= 0) {
                Log.e(TAG, "Dimensions are 0 or less");
            }

            try {
                mRasterizer = new PDFRasterizer();
                mRasterizer.setDrawAnnotations(true);

                int colorMode = PdfViewCtrlSettingsManager.getColorMode(context);
                int mode = PDFRasterizer.e_postprocess_none;
                switch (colorMode) {
                    case PdfViewCtrlSettingsManager.KEY_PREF_COLOR_MODE_CUSTOM:
                        mode = PDFRasterizer.e_postprocess_gradient_map;
                        int customBGColor = PdfViewCtrlSettingsManager.getCustomColorModeBGColor(context);
                        int customTxtColor = PdfViewCtrlSettingsManager.getCustomColorModeTextColor(context);
                        mRasterizer.setColorPostProcessColors(customBGColor, customTxtColor);
                        break;
                    case PdfViewCtrlSettingsManager.KEY_PREF_COLOR_MODE_SEPIA:
                        mode = PDFRasterizer.e_postprocess_gradient_map;
                        InputStream is = null;
                        OutputStream os = null;
                        try {
                            File filterFile = new File(context.getCacheDir(), "sepia_mode_filter.png");
                            if (!filterFile.exists() || !filterFile.isFile()) {
                                is = context.getResources().openRawResource(R.raw.sepia_mode_filter);
                                os = new FileOutputStream(filterFile);
                                IOUtils.copy(is, os);
                            }
                            mRasterizer.setColorPostProcessMapFile(new MappedFile(filterFile.getAbsolutePath()));
                        } catch (Exception e) {
                            AnalyticsHandlerAdapter.getInstance().sendException(e);
                        } finally {
                            Utils.closeQuietly(is);
                            Utils.closeQuietly(os);
                        }
                        break;
                    case PdfViewCtrlSettingsManager.KEY_PREF_COLOR_MODE_NIGHT:
                        mode = PDFRasterizer.e_postprocess_night_mode;
                        break;
                }
                mRasterizer.setColorPostProcessMode(mode);
            } catch (Exception e) {
                AnalyticsHandlerAdapter.getInstance().sendException(e);
            }
        }

        int widthCopy = 0;
        int heightCopy = 0;

        @Override
        protected Bitmap doInBackground(Void... jobParam) {
            if (mPageNumber > 0) {
                boolean shouldUnlockRead = false;
                try {
                    if (isCancelled() || mRasterizer == null) {
                        return null;
                    }
                    mPdfDoc.lockRead();
                    shouldUnlockRead = true;
                    Page page = mPdfDoc.getPage(mPageNumber);

                    double scaleFactor = 1;

                    Rect bbox = page.getCropBox();
                    bbox.normalize();

                    double pageWidth = page.getPageWidth();
                    double pageHeight = page.getPageHeight();

                    double pageScaleFactorX = mViewDimensions.x / pageWidth;
                    double pageScaleFactorY = mViewDimensions.y / pageHeight;

                    scaleFactor *= Math.max(pageScaleFactorX, pageScaleFactorY);

                    if (scaleFactor <= 0) {
                        return null;
                    }

                    // Will make the picture right side up, at the correct scale.
                    Matrix2D mtx = new Matrix2D(scaleFactor, 0, 0, scaleFactor, 0, 0);
                    mtx = mtx.multiply(page.getDefaultMatrix(true, Page.e_crop, Page.e_0));

                    int width = (int) (pageWidth * scaleFactor);
                    int height = (int) (pageHeight * scaleFactor);

                    int comps = 4; // BGRA
                    int stride = ((width * comps + 3) / 4) * 4;

                    if (isCancelled()) {
                        return null;
                    }

                    int[] img = new int[stride * height];

                    // PDFRasterizer changes the values of width and height on some Android 8.1.0 devices.
                    // Temporary solution is to copy the value and use the copied width and height after calling rasterize.
                    widthCopy = width;
                    heightCopy = height;
                    mRasterizer.rasterize(page, img, width, height, false, mtx, bbox);
                    width = widthCopy;
                    height = heightCopy;

                    if (isCancelled()) {
                        return null;
                    }

                    ImageMemoryCache imageMemoryCache = ImageMemoryCache.getInstance();
                    Bitmap bitmap = imageMemoryCache.getBitmapFromReusableSet(width, height, Bitmap.Config.ARGB_8888);
                    if (bitmap == null) {
                        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                    }
                    bitmap.setPixels(img, 0, width, 0, 0, width, height);
                    return bitmap;
                } catch (Exception e) {
                    AnalyticsHandlerAdapter.getInstance().sendException(e);
                } catch (OutOfMemoryError oom) {
                    Utils.manageOOM(getContext(), mPDFViewCtrlRef.get());
                } finally {
                    if (shouldUnlockRead) {
                        Utils.unlockReadQuietly(mPdfDoc);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onCancelled(Bitmap result) {
            super.onCancelled(result);

            mIsCancelled = true;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            final Context context = getContext();
            if (context == null) {
                return;
            }
            // TODO I seem to have failed this check once even though I cancelled this task
            // is this a matter of perfect timing (cancel called after this has been posted, but before it's been executed?
            // Added the mIsCancelled and !mIsActive checks since, let's keep monitoring.
            if (isCancelled() || result == null || mIsCancelled || !mIsActive) {
                return;
            }
            mGotBitmap = true;
            if (mCurrentPage == mPageNumber) {
                ImageMemoryCache.getInstance().addBitmapToCache("UserCrop" + mPageNumber, new BitmapDrawable(context.getResources(), result));
                readyNextPage(mPageNumber, result);
            } else {
                ImageMemoryCache.getInstance().addBitmapToCache("UserCrop" + mPageNumber, new BitmapDrawable(context.getResources(), result));
                preRenderIfNecessary();
            }
        }
    }

    // Implementation Details //////////////////////////////////////////////////

    private void setPage(int pageNumber) {
        if (pageNumber != mCurrentPage) {
            if (mPageProperties[mCurrentPage] != null && mCropImageView.hasBitmap()) {
                updatePageCropFromImageView(mPageProperties[mCurrentPage], mCropImageView.getCropRectPercentageMargins());
                mCropImageView.setImageBitmap(null);
            }
            mCurrentPage = pageNumber;
            mButtonUseOnEvenOdd.setText(mCurrentPage % 2 == 0 ? R.string.user_crop_manual_crop_even_pages : R.string.user_crop_manual_crop_odd_pages);
        }

        mBlankPageSpinnerHost.hide(true, false);
        mBlankPageSpinnerHost.show();
        mPageNumberTextView.setText(String.format("%s / %s", pageNumber, mPdfViewCtrl.getPageCount()));

        PDFDoc doc = mPdfViewCtrl.getDoc();
        boolean shouldUnlockRead = false;
        try {
            doc.lockRead();
            shouldUnlockRead = true;
            Page page = doc.getPage(pageNumber);
            setAndGetCropPropertiesForPage(pageNumber);
            Point newPageSize = calculateBlankPageSize(page.getPageWidth(), page.getPageHeight());
            resizePageProperties((int) newPageSize.x, (int) newPageSize.y);
            BitmapDrawable drawable = ImageMemoryCache.getInstance().getBitmapFromCache("UserCrop" + pageNumber);
            if (drawable != null) {
                readyNextPage(pageNumber, drawable.getBitmap());
            } else {
                if (mCurrentJob == null || mCurrentJob.getPageNumber() != pageNumber) {
                    if (mCurrentJob != null && !mCurrentJob.isDone()) {
                        mCurrentJob.cancelRasterizing();
                    }

                    mCurrentJob = new DrawImageTask(getActivity(), mPdfViewCtrl, pageNumber, newPageSize);
                    mCurrentJob.execute();
                }
            }
        } catch (Exception e) {
            AnalyticsHandlerAdapter.getInstance().sendException(e);
        } finally {
            if (shouldUnlockRead) {
                Utils.unlockReadQuietly(doc);
            }
        }
    }

    private PageCropProperties setAndGetCropPropertiesForPage(int pageNumber) {
        PDFDoc doc = mPdfViewCtrl.getDoc();
        boolean shouldUnlockRead = false;

        try {
            doc.lockRead();
            shouldUnlockRead = true;
            Page page = doc.getPage(pageNumber);
            PageCropProperties currentProperties = mPageProperties[pageNumber];
            if (currentProperties == null) {
                currentProperties = new PageCropProperties();
                mPageProperties[pageNumber] = currentProperties;
            }
            if (currentProperties.mCropBox == null) {
                currentProperties.mCropBox = page.getCropBox();
                currentProperties.mUserCropBox = page.getBox(Page.e_user_crop);
                currentProperties.mRotation = page.getRotation();
            }
            return currentProperties;
        } catch (PDFNetException e) {
            AnalyticsHandlerAdapter.getInstance().sendException(e);
        } finally {
            if (shouldUnlockRead) {
                Utils.unlockReadQuietly(doc);
            }
        }
        return null;
    }

    private void readyNextPage(int pageNumber, Bitmap newBitmap) {
        if (pageNumber == mCurrentPage && newBitmap != null) {
            mCropImageView.setImageBitmap(newBitmap);
            // setting the iamge bitmap above seems to auto-resize the mCropImageView to
            // the old size, so we need to set it again.
            resizePageProperties(newBitmap.getWidth(), newBitmap.getHeight());

            SetCropRectOnView();
        }
        preRenderIfNecessary();
    }

    private void preRenderIfNecessary() {
        if (mCurrentJob != null && !mCurrentJob.isDone()) {
            return;
        }
        int pageToRender = getNextPageToPreRender(mCurrentPage);
        while (pageToRender > 0 && pageToRender <= mTotalPages && Math.abs(pageToRender - mCurrentPage) <= mPagesToPreRenderPerDirection) {
            BitmapDrawable drawable = ImageMemoryCache.getInstance().getBitmapFromCache("UserCrop" + pageToRender);
            if (drawable == null) {
                PDFDoc doc = mPdfViewCtrl.getDoc();
                if (doc == null) {
                    return;
                }
                boolean shouldUnlockRead = false;

                try {
                    doc.lockRead();
                    shouldUnlockRead = true;
                    Page page = doc.getPage(pageToRender);
                    Point newPageSize = calculateBlankPageSize(page.getPageWidth(), page.getPageHeight());
                    mCurrentJob = new DrawImageTask(getActivity(), mPdfViewCtrl, pageToRender, newPageSize);
                    mCurrentJob.execute();
                } catch (PDFNetException e) {
                    AnalyticsHandlerAdapter.getInstance().sendException(e);
                } finally {
                    if (shouldUnlockRead) {
                        Utils.unlockReadQuietly(doc);
                    }
                }
                return;
            }
            pageToRender = getNextPageToPreRender(pageToRender);
        }
    }

    private int getNextPageToPreRender(int lastRenderedPage) {
        if (lastRenderedPage < 1 || lastRenderedPage > mTotalPages) {
            return -1;
        }
        if (lastRenderedPage > mCurrentPage) {
            int pageDiff = Math.abs(mCurrentPage - lastRenderedPage);
            int nextPage = lastRenderedPage - pageDiff - pageDiff;
            if (nextPage < 1) {
                return lastRenderedPage + 1;
            }
            return nextPage;
        } else {
            int pageDiff = Math.abs(mCurrentPage - lastRenderedPage);
            int nextPage = lastRenderedPage + pageDiff + pageDiff + 1;
            if (nextPage > mTotalPages) {
                return lastRenderedPage - 1;
            }
            return nextPage;
        }
    }

    private void SetCropRectOnView() {
        if (mPageProperties[mCurrentPage] != null && mPageProperties[mCurrentPage].mUserCropBox != null) {
            try {
                Rect cropBox = mPageProperties[mCurrentPage].mCropBox;
                if (cropBox.getWidth() > 0 && cropBox.getHeight() > 0) {
                    Rect userCropBox = mPageProperties[mCurrentPage].mUserCropBox;
                    RectF cropPercentageRect = new RectF();
                    cropPercentageRect.left = (float) ((userCropBox.getX1() - cropBox.getX1()) / cropBox.getWidth());
                    cropPercentageRect.right = (float) ((cropBox.getX2() - userCropBox.getX2()) / cropBox.getWidth());
                    cropPercentageRect.bottom = (float) ((userCropBox.getY1() - cropBox.getY1()) / cropBox.getHeight());
                    cropPercentageRect.top = (float) ((cropBox.getY2() - userCropBox.getY2()) / cropBox.getHeight());

                    rotateRectangle(cropPercentageRect, mPageProperties[mCurrentPage].mRotation);

                    mCropImageView.setCropRectPercentageMargins(cropPercentageRect);
                }
            } catch (Exception e) {
                AnalyticsHandlerAdapter.getInstance().sendException(e);
            }
        }
    }

    private void updatePageCropFromImageView(PageCropProperties properties, RectF percentageRect) {
        if (properties != null) {
            try {
                int rot = Page.subtractRotations(Page.e_0, properties.mRotation);
                rotateRectangle(percentageRect, rot);

                Rect cropBox = properties.mCropBox;
                if (cropBox != null && cropBox.getWidth() > 0 && cropBox.getHeight() > 0) {
                    if (properties.mUserCropBox == null) {
                        properties.mUserCropBox = new Rect();
                    }
                    properties.mUserCropBox.setX1(cropBox.getX1() + (percentageRect.left * cropBox.getWidth()));
                    properties.mUserCropBox.setX2(cropBox.getX2() - (percentageRect.right * cropBox.getWidth()));
                    properties.mUserCropBox.setY1(cropBox.getY1() + (percentageRect.bottom * cropBox.getHeight()));
                    properties.mUserCropBox.setY2(cropBox.getY2() - (percentageRect.top * cropBox.getHeight()));
                }
            } catch (Exception e) {
                AnalyticsHandlerAdapter.getInstance().sendException(e);
            }
        }
    }

    private Point calculateBlankPageSize(double pageWidth, double pageHeight) {
        double hostWidth = mCropPageHost.getMeasuredWidth();
        double hostHeight = mCropPageHost.getMeasuredHeight();

        // calculate size of page
        double widthScale = hostWidth / pageWidth;
        double heightScale = hostHeight / pageHeight;

        if (widthScale < heightScale) {
            return new Point((int) hostWidth, (int) (pageHeight * widthScale));
        } else {
            return new Point((int) (pageWidth * heightScale), (int) hostHeight);
        }
    }

    private void resizePageProperties(int pageWidth, int pageHeight) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mCropImageView.getLayoutParams();
        layoutParams.width = pageWidth;
        layoutParams.height = pageHeight;
        mCropImageView.setLayoutParams(layoutParams);

        layoutParams = (FrameLayout.LayoutParams) mBlankPagePlaceholder.getLayoutParams();
        layoutParams.width = pageWidth;
        layoutParams.height = pageHeight;
        mBlankPagePlaceholder.setLayoutParams(layoutParams);
    }

    // Cropping ////////////////////////////////////////////////////////////////

    private void setCropBoxAndClose() {
        if (mCurrentJob != null && !mCurrentJob.isDone()) {
            mCurrentJob.cancelRasterizing();
        }
        mIsActive = true;
        if (mPageProperties[mCurrentPage] != null) {
            if (mCropImageView.hasBitmap()) {
                updatePageCropFromImageView(mPageProperties[mCurrentPage], mCropImageView.getCropRectPercentageMargins());
            }
        }

        Activity activity = getActivity();
        if (activity != null) {
            PDFDoc doc = mPdfViewCtrl.getDoc();
            mPdfViewCtrl.cancelRendering();
            SetCropBoxTask mCropBoxTask = new SetCropBoxTask(activity, doc);
            mCropBoxTask.execute();
        }
    }

    private void applyCropToPagesAndFlash(MultiApplyMode applyMode) {
        if (mCropImageView.hasBitmap() && mPageProperties[mCurrentPage] != null) {
            RectF percentageRect = mCropImageView.getCropRectPercentageMargins();

            // Ensure the current page has the correct value now.
            updatePageCropFromImageView(mPageProperties[mCurrentPage], percentageRect);

            try {
                int rot = Page.subtractRotations(Page.e_0, mPageProperties[mCurrentPage].mRotation);
                rotateRectangle(percentageRect, rot);

                Rect cropBox = mPageProperties[mCurrentPage].mCropBox;
                if (cropBox != null && cropBox.getWidth() > 0 && cropBox.getHeight() > 0) {
                    Rect cropMargins = new Rect(percentageRect.left * cropBox.getWidth(),
                            percentageRect.bottom * cropBox.getHeight(),
                            percentageRect.right * cropBox.getWidth(),
                            percentageRect.top * cropBox.getHeight());

                    ApplyCropToPagesTask applyCropTask = new ApplyCropToPagesTask(getActivity(), cropMargins, mPdfViewCtrl.getDoc(), applyMode);
                    applyCropTask.execute();
                    CommonToast.showText(getActivity(), getCropInfoString(applyMode));
                }
            } catch (PDFNetException e) {
                AnalyticsHandlerAdapter.getInstance().sendException(e);
            }
            updateCTA();
        }
    }

    /**
     * Used to modify the user crop box to all the pages in a background thread.
     */
    private class SetCropBoxTask extends CustomAsyncTask<Void, Integer, Boolean> {

        private final PDFDoc mPdfDoc;
        private long mTaskStartTime;
        boolean mHasChange;

        public SetCropBoxTask(@NonNull Context context, @NonNull PDFDoc doc) {
            super(context);

            mPdfDoc = doc;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mTaskStartTime = System.nanoTime() / 1000000;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            if (mPdfViewCtrl == null || mPdfDoc == null) {
                return false;
            }

            boolean success = false;
            boolean shouldUnlock = false;
            try {
                mPdfDoc.lock();
                shouldUnlock = true;

                cropPagesHelper();
                mHasChange = mPdfDoc.hasChangesSinceSnapshot();
                success = true;
            } catch (PDFNetException e) {
                AnalyticsHandlerAdapter.getInstance().sendException(e, "USER_CROP");
            } finally {
                if (shouldUnlock) {
                    Utils.unlockQuietly(mPdfDoc);
                }
            }
            return success;
        }

        private void cropPagesHelper() throws PDFNetException {
            int currPageNum = 1;
            PageIterator itr = mPdfDoc.getPageIterator();
            while (itr.hasNext()) {
                Page page = itr.next();
                PageCropProperties pageProperties = mPageProperties[currPageNum];
                if (pageProperties != null && pageProperties.mUserCropBox != null) {
                    try {
                        if (pageProperties.mUserCropBox.getX1() - pageProperties.mCropBox.getX1() <= 0.1 &&
                                pageProperties.mCropBox.getX2() - pageProperties.mUserCropBox.getX2() <= 0.1 &&
                                pageProperties.mUserCropBox.getY1() - pageProperties.mCropBox.getY1() <= 0.1 &&
                                pageProperties.mCropBox.getY2() - pageProperties.mUserCropBox.getY2() <= 0.1) {
                            com.pdftron.pdf.utils.UserCropUtilities.removeUserCropFromPage(page);
                        } else {
                            page.setBox(Page.e_user_crop, pageProperties.mUserCropBox);
                        }
                    } catch (PDFNetException e) {
                        AnalyticsHandlerAdapter.getInstance().sendException(e);
                    }
                }

                if (currPageNum % 100 == 99) {
                    publishProgress(currPageNum);
                }

                currPageNum++;
            }
        }

        @Override
        protected void onCancelled(Boolean succeeded) {
            super.onCancelled(succeeded);

            removeSpinner();
        }

        @Override
        protected void onPostExecute(Boolean succeeded) {
            super.onPostExecute(succeeded);

            removeSpinner();
            ViewerUtils.safeUpdatePageLayout(mPdfViewCtrl, new ExceptionHandlerCallback() {
                @Override
                public void onException(Exception e) {
                    AnalyticsHandlerAdapter.getInstance().sendException(e);
                }
            });

            if (succeeded) {
                if (mHasChange) {
                    ToolManager toolManager = (ToolManager) mPdfViewCtrl.getToolManager();
                    if (toolManager != null) {
                        toolManager.raisePagesCropped();
                    }
                }
                UserCropDialogFragment.this.dismiss();
            }

            removeSpinner();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            if (getContext() == null) {
                return;
            }

            long now = System.nanoTime() / 1000000;

            if (now - mTaskStartTime > MILLISECONDS_BEFORE_SHOWING_PROGRESS) {
                showSpinner();
            }
        }
    }

    /**
     * Used to apply the crop box to each page's properties
     */
    private class ApplyCropToPagesTask extends CustomAsyncTask<Void, Integer, Boolean> {

        private Rect mMarginRect;
        private PDFDoc mDoc;
        private long mTaskStartTime;

        private MultiApplyMode mMode;

        public ApplyCropToPagesTask(Context context, Rect marginRect, PDFDoc doc, MultiApplyMode mode) {
            super(context);

            mMarginRect = marginRect;
            mDoc = doc;
            mMode = mode;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mTaskStartTime = System.nanoTime() / 1000000;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return applyCropToAllPages(mMarginRect, mDoc);
        }

        boolean applyCropToAllPages(Rect marginRect, PDFDoc doc) {
            boolean success = false;
            // Getting a page (e.g. PDFDoc.getPage(n)) is O(n). Therefore, we create an iterator since
            // we might have to look at every page
            boolean shouldUnlockRead = false;
            try {
                doc.lockRead();
                shouldUnlockRead = true;

                PageIterator itr = doc.getPageIterator();
                int pageNumber = 1;
                while (itr.hasNext() && !isCancelled()) {
                    Page page = itr.next();

                    if (mMode == MultiApplyMode.Even && (pageNumber % 2) != 0) {
                        pageNumber++;
                        continue;
                    } else if (mMode == MultiApplyMode.Odd && (pageNumber % 2) == 0) {
                        pageNumber++;
                        continue;
                    }

                    if (mPageProperties[pageNumber] == null) {
                        PageCropProperties cropProperties = new PageCropProperties();
                        cropProperties.mCropBox = page.getCropBox();
                        cropProperties.mRotation = page.getRotation();
                        cropProperties.mUserCropBox = new Rect();
                        mPageProperties[pageNumber] = cropProperties;
                    }
                    Rect userCrop = mPageProperties[pageNumber].mUserCropBox;
                    Rect crop = mPageProperties[pageNumber].mCropBox;

                    if (marginRect.getX1() + marginRect.getX2() + 10 > crop.getWidth()) {
                        userCrop.setX1(crop.getX1());
                        userCrop.setX2(crop.getX2());
                    } else {
                        userCrop.setX1(crop.getX1() + marginRect.getX1());
                        userCrop.setX2(crop.getX2() - marginRect.getX2());
                    }

                    if (marginRect.getY1() + marginRect.getY2() + 10 > crop.getHeight()) {
                        userCrop.setY1(crop.getY1());
                        userCrop.setY2(crop.getY2());
                    } else {
                        userCrop.setY1(crop.getY1() + marginRect.getY1());
                        userCrop.setY2(crop.getY2() - marginRect.getY2());
                    }

                    pageNumber++;

                    if (pageNumber % 100 == 99) {
                        publishProgress(pageNumber);
                    }
                }
                success = true;
            } catch (PDFNetException e) {
                AnalyticsHandlerAdapter.getInstance().sendException(e);
            } finally {
                if (shouldUnlockRead) {
                    Utils.unlockReadQuietly(doc);
                }
            }
            return success;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            if (getContext() == null) {
                return;
            }

            long now = System.nanoTime() / 1000000;

            if (now - mTaskStartTime > MILLISECONDS_BEFORE_SHOWING_PROGRESS) {
                showSpinner();
            }
        }

        @Override
        protected void onCancelled(Boolean aBoolean) {
            super.onCancelled(aBoolean);

            removeSpinner();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            removeSpinner();
        }
    }

    private enum MultiApplyMode {
        All,
        Even,
        Odd,
    }

    private void showSpinner() {
        this.setCancelable(false);

        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void removeSpinner() {
        this.setCancelable(true);

        mProgressBar.setVisibility(View.GONE);
    }

    // Utility Functions ///////////////////////////////////////////////////////

    @SuppressWarnings("SuspiciousNameCombination")
    void rotateRectangle(RectF rect, int rotation) {
        float temp;
        switch (rotation) {
            case Page.e_0:
                return;
            case Page.e_90:
                temp = rect.left;
                rect.left = rect.bottom;
                rect.bottom = rect.right;
                rect.right = rect.top;
                rect.top = temp;
                return;
            case Page.e_180:
                temp = rect.left;
                rect.left = rect.right;
                rect.right = temp;
                temp = rect.bottom;
                rect.bottom = rect.top;
                rect.top = temp;
                return;
            case Page.e_270:
                temp = rect.left;
                rect.left = rect.top;
                rect.top = rect.right;
                rect.right = rect.bottom;
                rect.bottom = temp;
        }
    }

    private String getCropInfoString(MultiApplyMode mode) {
        int res = R.string.user_crop_manual_crop_crop_all_toast;
        if (mode == MultiApplyMode.Even) {
            res = R.string.user_crop_manual_crop_crop_even_toast;
        } else if (mode == MultiApplyMode.Odd) {
            res = R.string.user_crop_manual_crop_crop_odd_toast;
        }
        return String.format(getString(res),
                getString(R.string.tools_qm_crop));
    }
}
