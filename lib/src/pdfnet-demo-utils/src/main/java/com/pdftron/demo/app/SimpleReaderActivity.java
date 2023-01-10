package com.pdftron.demo.app;
//---------------------------------------------------------------------------------------
// Copyright (c) 2001-2019 by PDFTron Systems Inc. All Rights Reserved.
// Consult legal.txt regarding legal and license information.
//---------------------------------------------------------------------------------------

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.pdftron.demo.R;
import com.pdftron.demo.dialog.DialogOpenUrl;
import com.pdftron.demo.dialog.FilePickerDialogFragment;
import com.pdftron.demo.utils.AppUtils;
import com.pdftron.pdf.config.PDFNetConfig;
import com.pdftron.pdf.config.ViewerBuilder2;
import com.pdftron.pdf.config.ViewerConfig;
import com.pdftron.pdf.controls.DocumentActivity;
import com.pdftron.pdf.model.BaseFileInfo;
import com.pdftron.pdf.model.FileInfo;
import com.pdftron.pdf.utils.RequestCode;
import com.pdftron.pdf.utils.StampManager;
import com.pdftron.pdf.utils.Utils;

import java.io.File;

/**
 * SimpleReaderActivity is derived from {@link DocumentActivity}.
 * and is an all-in-one document reader and PDF editor. UI can be configured via {@link ViewerConfig class}.
 */
public class SimpleReaderActivity extends DocumentActivity {

    private static final String TAG = SimpleReaderActivity.class.getName();

    MenuItem mMenuOpenFile;
    MenuItem mMenuOpenUrl;

    /**
     * Opens a built-in sample document with default configuration.
     *
     * @param packageContext the context
     */
    public static void open(Context packageContext) {
        open(packageContext, null);
    }

    /**
     * Opens a built-in sample document with custom configuration.
     *
     * @param packageContext the context
     * @param config         the {@link ViewerConfig}
     */
    public static void open(Context packageContext, @Nullable ViewerConfig config) {
        open(packageContext, config, false);
    }

    public static void open(Context packageContext, @Nullable ViewerConfig config, boolean newUi) {
        Intent intent = new Intent(packageContext, SimpleReaderActivity.class);
        intent.putExtra(EXTRA_CONFIG, config);
        intent.putExtra(EXTRA_NEW_UI, newUi);
        packageContext.startActivity(intent);
    }

    /**
     * Opens a file from Uri with empty password and default configuration.
     *
     * @param packageContext the context
     * @param fileUri        the file Uri
     */
    public static void openDocument(Context packageContext, Uri fileUri) {
        openDocument(packageContext, fileUri, "");
    }

    /**
     * Opens a file from Uri with empty password and custom configuration.
     *
     * @param packageContext the context
     * @param fileUri        the file Uri
     * @param config         the configuration
     */
    public static void openDocument(Context packageContext, Uri fileUri, @Nullable ViewerConfig config) {
        openDocument(packageContext, fileUri, "", config);
    }

    /**
     * Opens a file from resource id with empty password and default configuration.
     *
     * @param packageContext the context
     * @param resId          the resource id
     */
    public static void openDocument(Context packageContext, int resId) {
        openDocument(packageContext, resId, "");
    }

    /**
     * Opens a file from resource id with empty password and custom configuration.
     *
     * @param packageContext the context
     * @param resId          the resource id
     * @param config         the configuration
     */
    public static void openDocument(Context packageContext, int resId, @Nullable ViewerConfig config) {
        openDocument(packageContext, resId, "", config);
    }

    /**
     * Opens a file from Uri with password and default configuration.
     *
     * @param packageContext the context
     * @param fileUri        the file Uri
     * @param password       the password
     */
    public static void openDocument(Context packageContext, Uri fileUri, String password) {
        openDocument(packageContext, fileUri, password, null);
    }

    /**
     * Opens a file from resource id with password and default configuration.
     *
     * @param packageContext the context
     * @param resId          the resource id
     * @param password       the password
     */
    public static void openDocument(Context packageContext, int resId, String password) {
        openDocument(packageContext, resId, password, null);
    }

    /**
     * Opens a file from Uri with password and custom configuration.
     *
     * @param packageContext the context
     * @param fileUri        the file Uri
     * @param password       the password
     * @param config         the configuration
     */
    public static void openDocument(Context packageContext, Uri fileUri, String password, @Nullable ViewerConfig config) {
        openDocument(packageContext, fileUri, password, config, false);
    }

    public static void openDocument(Context packageContext, Uri fileUri, String password, @Nullable ViewerConfig config, boolean newUi) {
        Intent intent = new Intent(packageContext, SimpleReaderActivity.class);
        if (null != fileUri) {
            intent.putExtra(EXTRA_FILE_URI, fileUri);
        }
        if (null != password) {
            intent.putExtra(EXTRA_FILE_PASSWORD, password);
        }
        intent.putExtra(EXTRA_CONFIG, config);
        intent.putExtra(EXTRA_NEW_UI, newUi);
        packageContext.startActivity(intent);
    }

    /**
     * Opens a file from resource id with password and custom configuration.
     *
     * @param packageContext the context
     * @param resId          the resource id
     * @param password       the password
     * @param config         the configuration
     */
    public static void openDocument(Context packageContext, int resId, String password, @Nullable ViewerConfig config) {
        Intent intent = new Intent(packageContext, SimpleReaderActivity.class);
        intent.putExtra(EXTRA_FILE_RES_ID, resId);
        if (null != password) {
            intent.putExtra(EXTRA_FILE_PASSWORD, password);
        }
        intent.putExtra(EXTRA_CONFIG, config);
        packageContext.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        try {
            AppUtils.initializePDFNetApplication(getApplicationContext(),
                    PDFNetConfig.loadFromXML(getApplicationContext(), R.xml.pdfnet_config));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        mSampleRes = R.raw.getting_started;
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int[] getToolbarMenuResArray() {
        if (mUseNewUi) {
            return new int[]{R.menu.fragment_viewer_addon, R.menu.fragment_viewer_new};
        } else {
            return new int[]{R.menu.fragment_viewer_addon, R.menu.fragment_viewer};
        }
    }

    /**
     * @hide
     */
    @Override
    public boolean onToolbarCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        mMenuOpenFile = menu.findItem(R.id.action_open_file);
        mMenuOpenUrl = menu.findItem(R.id.action_open_url);
        return false;
    }

    /**
     * @hide
     */
    @Override
    public boolean onToolbarOptionsItemSelected(MenuItem item) {
        final int id = item.getItemId();
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (id == R.id.action_open_file) {
            FilePickerDialogFragment dialogFragment = FilePickerDialogFragment.newInstance(RequestCode.SELECT_FILE,
                    Environment.getExternalStorageDirectory());
            dialogFragment.setSingleFileListener(new FilePickerDialogFragment.SingleFileListener() {
                @Override
                public void onSingleFileSelected(int requestCode, FileInfo fileInfo) {
                    Uri fileUri;
                    if (fileInfo.getType() == BaseFileInfo.FILE_TYPE_FILE) {
                        fileUri = Uri.fromFile(new File(fileInfo.getAbsolutePath()));
                    } else {
                        fileUri = Uri.parse(fileInfo.getAbsolutePath());
                    }

                    if (mUseNewUi) {
                        mPdfViewCtrlTabHostFragment2.onOpenAddNewTab(
                                ViewerBuilder2.withUri(fileUri, "")
                                        .usingConfig(mViewerConfig)
                                        .createBundle(SimpleReaderActivity.this)
                        );
                    } else {
                        mPdfViewCtrlTabHostFragment.onOpenAddNewTab(
                                ViewerBuilder2.withUri(fileUri, "")
                                        .usingConfig(mViewerConfig)
                                        .createBundle(SimpleReaderActivity.this)
                        );
                    }
                }
            });
            dialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomAppTheme);
            dialogFragment.show(fragmentManager, "file_picker_dialog_fragment");
        } else if (id == R.id.action_open_url) {
            DialogOpenUrl dialogOpenUrl = new DialogOpenUrl(this, new DialogOpenUrl.DialogOpenUrlListener() {
                @Override
                public void onSubmit(String url) {
                    if (mUseNewUi) {
                        mPdfViewCtrlTabHostFragment2.onOpenAddNewTab(
                                ViewerBuilder2.withUri(Uri.parse(url), "")
                                        .usingConfig(mViewerConfig)
                                        .createBundle(SimpleReaderActivity.this)
                        );
                    } else {
                        mPdfViewCtrlTabHostFragment.onOpenAddNewTab(
                                ViewerBuilder2.withUri(Uri.parse(url), "")
                                        .usingConfig(mViewerConfig)
                                        .createBundle(SimpleReaderActivity.this)
                        );
                    }
                }
            });
            dialogOpenUrl.show();
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!Utils.hasStoragePermission(this)) {
            Utils.requestStoragePermissions(this, null, RequestCode.STORAGE_1);
        }
    }
}
