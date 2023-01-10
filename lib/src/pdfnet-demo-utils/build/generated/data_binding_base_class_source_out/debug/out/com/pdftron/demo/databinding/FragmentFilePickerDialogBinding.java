// Generated by view binder compiler. Do not edit!
package com.pdftron.demo.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.viewbinding.ViewBinding;
import com.pdftron.demo.R;
import com.pdftron.pdf.widget.ContentLoadingRelativeLayout;
import com.pdftron.pdf.widget.PTFloatingActionButton;
import com.pdftron.pdf.widget.recyclerview.SimpleRecyclerView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentFilePickerDialogBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final TextView empty;

  @NonNull
  public final PTFloatingActionButton fragmentFilePickerDialogFab;

  @NonNull
  public final SimpleRecyclerView fragmentFilePickerDialogFolderList;

  @NonNull
  public final ContentLoadingRelativeLayout fragmentFilePickerDialogProgressBar;

  @NonNull
  public final Toolbar fragmentFilePickerDialogToolbar;

  @NonNull
  public final ProgressBar progressBar;

  private FragmentFilePickerDialogBinding(@NonNull RelativeLayout rootView, @NonNull TextView empty,
      @NonNull PTFloatingActionButton fragmentFilePickerDialogFab,
      @NonNull SimpleRecyclerView fragmentFilePickerDialogFolderList,
      @NonNull ContentLoadingRelativeLayout fragmentFilePickerDialogProgressBar,
      @NonNull Toolbar fragmentFilePickerDialogToolbar, @NonNull ProgressBar progressBar) {
    this.rootView = rootView;
    this.empty = empty;
    this.fragmentFilePickerDialogFab = fragmentFilePickerDialogFab;
    this.fragmentFilePickerDialogFolderList = fragmentFilePickerDialogFolderList;
    this.fragmentFilePickerDialogProgressBar = fragmentFilePickerDialogProgressBar;
    this.fragmentFilePickerDialogToolbar = fragmentFilePickerDialogToolbar;
    this.progressBar = progressBar;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentFilePickerDialogBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentFilePickerDialogBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_file_picker_dialog, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentFilePickerDialogBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = android.R.id.empty;
      TextView empty = rootView.findViewById(id);
      if (empty == null) {
        break missingId;
      }

      id = R.id.fragment_file_picker_dialog_fab;
      PTFloatingActionButton fragmentFilePickerDialogFab = rootView.findViewById(id);
      if (fragmentFilePickerDialogFab == null) {
        break missingId;
      }

      id = R.id.fragment_file_picker_dialog_folder_list;
      SimpleRecyclerView fragmentFilePickerDialogFolderList = rootView.findViewById(id);
      if (fragmentFilePickerDialogFolderList == null) {
        break missingId;
      }

      id = R.id.fragment_file_picker_dialog_progress_bar;
      ContentLoadingRelativeLayout fragmentFilePickerDialogProgressBar = rootView.findViewById(id);
      if (fragmentFilePickerDialogProgressBar == null) {
        break missingId;
      }

      id = R.id.fragment_file_picker_dialog_toolbar;
      Toolbar fragmentFilePickerDialogToolbar = rootView.findViewById(id);
      if (fragmentFilePickerDialogToolbar == null) {
        break missingId;
      }

      id = R.id.progress_bar;
      ProgressBar progressBar = rootView.findViewById(id);
      if (progressBar == null) {
        break missingId;
      }

      return new FragmentFilePickerDialogBinding((RelativeLayout) rootView, empty,
          fragmentFilePickerDialogFab, fragmentFilePickerDialogFolderList,
          fragmentFilePickerDialogProgressBar, fragmentFilePickerDialogToolbar, progressBar);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
