package com.pdftron.pdf.tools;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringDef;

import com.pdftron.common.PDFNetException;
import com.pdftron.fdf.FDFDoc;
import com.pdftron.pdf.Annot;
import com.pdftron.pdf.ExternalAnnotManager;
import com.pdftron.pdf.PDFDoc;
import com.pdftron.pdf.PDFViewCtrl;
import com.pdftron.pdf.utils.AnalyticsHandlerAdapter;
import com.pdftron.pdf.utils.Utils;
import com.pdftron.sdf.SDFDoc;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class is responsible for generating local annotation changes XFDF string and
 * merging remote annotation changes XFDF string
 */
public class AnnotManager {

    private static final String TAG = AnnotManager.class.getName();
    private static boolean sDebug = true;

    private final Lock mDataLock = new ReentrantLock();

    /**
     * This interface can be used to monitor local annotation changes,
     * which can then be forwarded to a remote server.
     */
    public interface AnnotationSyncingListener {
        void onLocalChange(String action, String xfdfCommand, String xfdfJSON);
    }

    /**
     * @hide
     */
    @StringDef({
            AnnotationAction.ADD,
            AnnotationAction.MODIFY,
            AnnotationAction.DELETE,
            AnnotationAction.UNDO,
            AnnotationAction.REDO
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface AnnotAction {
    }

    /**
     * Annotation actions
     */
    public interface AnnotationAction {
        /**
         * Add annotation
         */
        String ADD = "add";
        /**
         * Modify annotation
         */
        String MODIFY = "modify";
        /**
         * Delete annotation
         */
        String DELETE = "delete";
        /**
         * Undo annotation action
         */
        String UNDO = "undo";
        /**
         * Redo annotation action
         */
        String REDO = "redo";
    }

    /**
     * Annotation custom properties
     */
    public interface AnnotItem {
        String MSG_COUNT = "msgCount";
    }

    public enum EditPermissionMode {
        /**
         * Can edit yours and other's changes
         */
        EDIT_OTHERS,
        /**
         * Can edit only your changes
         */
        EDIT_OWN,
    }

    private final ExternalAnnotManager mExternalAnnotManager;
    private final PDFViewCtrl mPdfViewCtrl;
    private final ToolManager mToolManager;

    private Bundle mInitialAnnot;
    private Bundle mAnnots;

    private final PDFViewCtrl.AnnotationManagerMode mUndoMode;
    private final EditPermissionMode mEditMode;

    private AnnotationSyncingListener mListener;

    /**
     * Initializes annotation manager for annotation syncing
     *
     * @param toolManager the {@link ToolManager}
     * @param userId      the unique identifier of the current user
     * @param listener    the {@link AnnotationSyncingListener}
     * @throws PDFNetException
     */
    AnnotManager(@NonNull final ToolManager toolManager,
            @NonNull final String userId,
            @Nullable final String userName,
            AnnotationSyncingListener listener) throws PDFNetException {
        this(toolManager, userId, userName, null,
                PDFViewCtrl.AnnotationManagerMode.ADMIN_UNDO_OWN,
                EditPermissionMode.EDIT_OWN,
                listener);
    }

    /**
     * Initializes annotation manager for annotation syncing
     *
     * @param toolManager  the {@link ToolManager}
     * @param userId       the unique identifier of the current user
     * @param initialAnnot if set, viewer will jump to the set annotation automatically
     * @param undoMode     the undo mode
     * @param editMode     the edit mode
     * @param listener     the {@link AnnotationSyncingListener}
     * @throws PDFNetException
     */
    AnnotManager(@NonNull final ToolManager toolManager,
            @NonNull final String userId,
            @Nullable final String userName,
            @Nullable final Bundle initialAnnot,
            @NonNull final PDFViewCtrl.AnnotationManagerMode undoMode,
            @NonNull final EditPermissionMode editMode,
            AnnotationSyncingListener listener) throws PDFNetException {
        if (toolManager.getPDFViewCtrl() == null) {
            throw new NullPointerException("PDFfViewCtrl can't be null");
        }
        mToolManager = toolManager;
        mPdfViewCtrl = toolManager.getPDFViewCtrl();

        mUndoMode = undoMode;
        mEditMode = editMode;
        mExternalAnnotManager = toolManager.getPDFViewCtrl().enableExternalAnnotManager(userId, undoMode);
        if (mExternalAnnotManager == null) {
            throw new NullPointerException("ExternalAnnotManager can't be null");
        }
        toolManager.setAuthorId(userId);
        toolManager.setAuthorName(userName);
        mInitialAnnot = initialAnnot;
        mListener = listener;
    }

    /**
     * Sets AnnotationSyncingListener
     *
     * @param listener the listener
     */
    public void setAnnotationSyncingListener(AnnotationSyncingListener listener) {
        mListener = listener;
    }

    @NonNull
    public PDFViewCtrl.AnnotationManagerMode getUndoMode() {
        return mUndoMode;
    }

    @NonNull
    public EditPermissionMode getEditMode() {
        return mEditMode;
    }

    /**
     * @param action the action
     * @hide Used internally to raise local annotation change event
     */
    public void onLocalChange(@AnnotAction String action) {
        try {
            String lastChanges = mExternalAnnotManager.getLastXFDF();
            if (sDebug) {
                Log.d(TAG, "onLocalChange: [" + action + "] " + lastChanges);
            }
            String lastJSON = mExternalAnnotManager.getLastJSON();
            if (sDebug) {
                Log.d(TAG, "onLocalChange json: [" + action + "] " + lastJSON);
            }

            if (mListener != null && !Utils.isNullOrEmpty(lastChanges) && !Utils.isNullOrEmpty(lastJSON)) {
                mListener.onLocalChange(action, lastChanges, lastJSON);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Used when annotation change event is received from remote service
     *
     * @param incoming the XFDF command string
     */
    public void onRemoteChange(String incoming) {
        if (sDebug) {
            Log.d(TAG, "onRemoteChange: " + incoming);
        }
        boolean shouldUnlock = false;
        try {
            mPdfViewCtrl.docLock(true);
            shouldUnlock = true;
            mExternalAnnotManager.mergeXFDF(incoming);
        } catch (Exception e) {
            AnalyticsHandlerAdapter.getInstance().sendException(e);
        } finally {
            if (shouldUnlock) {
                mPdfViewCtrl.docUnlock();
            }
        }
        if (mInitialAnnot != null) {
            String annotId = mInitialAnnot.getString("chatId");
            try {
                String page = mInitialAnnot.getString("page");
                int pageNum = Integer.parseInt(page);
                jumpToAnnot(annotId, pageNum);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            mInitialAnnot = null;
        }
        mToolManager.resetIndicator();
    }

    /**
     * Jump to an annotation by id
     *
     * @param annotId the annotation id
     */
    public void jumpToAnnot(String annotId) {
        mPdfViewCtrl.jumpToAnnotWithID(annotId);
    }

    /**
     * Jump to an annotation by id and selects it
     *
     * @param annotId annotation id
     * @param page    xfdf zero-indexed page number
     */
    public void jumpToAnnot(String annotId, int page) {
        mPdfViewCtrl.jumpToAnnotWithID(annotId);
        page = page + 1;
        if (page > 0) {
            mToolManager.selectAnnot(annotId, page);
        } else {
            mToolManager.deselectAll();
        }
    }

    /**
     * Export all annotations to a file
     *
     * @param targetFile the target file
     */
    public void exportToFile(File targetFile) {
        boolean shouldUnlockRead = false;
        PDFDoc exportedDoc = null;
        try {
            // Locks the document first as accessing annotation/doc information isn't thread safe.
            mPdfViewCtrl.docLockRead();
            shouldUnlockRead = true;

            PDFDoc mainDoc = mPdfViewCtrl.getDoc();
            exportedDoc = new PDFDoc(targetFile.getAbsolutePath());

            // Obtain XFDF
            FDFDoc mainFDFDoc = mainDoc.fdfExtract(PDFDoc.e_both);
            String xfdf = mainFDFDoc.saveAsXFDF();
            // Create new FDFDoc to merge in duplicated file
            FDFDoc newFDFDoc = FDFDoc.createFromXFDF(xfdf);
            exportedDoc.fdfUpdate(newFDFDoc);

            exportedDoc.save(targetFile.getAbsolutePath(), SDFDoc.SaveMode.LINEARIZED, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (shouldUnlockRead) {
                mPdfViewCtrl.docUnlockRead();
            }
            if (exportedDoc != null) {
                Utils.closeQuietly(exportedDoc);
                exportedDoc = null;
            }
        }
    }

    /**
     * @hide
     */
    public void onAnnotationItemsChange(Bundle annots) {
        if (mDataLock.tryLock()) {
            try {
                mAnnots = annots;
            } finally {
                mDataLock.unlock();
            }
        }
    }

    /**
     * @hide
     */
    public boolean shouldShowIndicator(Annot annot) {
        try {
            if (null == mAnnots ||
                    null == annot ||
                    null == annot.getUniqueID()) {
                return false;
            }
            String annotId = annot.getUniqueID().getAsPDFText();
            if (null == annotId) {
                return false;
            }
            Bundle annotItem = null;
            if (mDataLock.tryLock()) {
                try {
                    annotItem = mAnnots.getBundle(annotId);
                } finally {
                    mDataLock.unlock();
                }
            }
            if (null != annotItem) {
                double msgCount = annotItem.getDouble(AnnotItem.MSG_COUNT, 0);
                if (msgCount > 0) {
                    return true;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
