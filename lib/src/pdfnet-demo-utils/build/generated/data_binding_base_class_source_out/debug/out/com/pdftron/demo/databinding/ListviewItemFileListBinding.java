// Generated by view binder compiler. Do not edit!
package com.pdftron.demo.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.viewbinding.ViewBinding;
import com.pdftron.demo.R;
import com.pdftron.demo.widget.ImageViewTopCrop;
import com.pdftron.pdf.widget.InertCheckBox;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ListviewItemFileListBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final InertCheckBox checkBox;

  @NonNull
  public final AppCompatImageView divider;

  @NonNull
  public final TextView docTextPlaceHolder;

  @NonNull
  public final AppCompatImageButton dragIcon;

  @NonNull
  public final ImageViewTopCrop fileIcon;

  @NonNull
  public final TextView fileInfo;

  @NonNull
  public final AppCompatImageView fileLockIcon;

  @NonNull
  public final TextView fileName;

  @NonNull
  public final Guideline guidelineImage;

  @NonNull
  public final FrameLayout infoButton;

  @NonNull
  public final AppCompatImageView infoIcon;

  private ListviewItemFileListBinding(@NonNull ConstraintLayout rootView,
      @NonNull InertCheckBox checkBox, @NonNull AppCompatImageView divider,
      @NonNull TextView docTextPlaceHolder, @NonNull AppCompatImageButton dragIcon,
      @NonNull ImageViewTopCrop fileIcon, @NonNull TextView fileInfo,
      @NonNull AppCompatImageView fileLockIcon, @NonNull TextView fileName,
      @NonNull Guideline guidelineImage, @NonNull FrameLayout infoButton,
      @NonNull AppCompatImageView infoIcon) {
    this.rootView = rootView;
    this.checkBox = checkBox;
    this.divider = divider;
    this.docTextPlaceHolder = docTextPlaceHolder;
    this.dragIcon = dragIcon;
    this.fileIcon = fileIcon;
    this.fileInfo = fileInfo;
    this.fileLockIcon = fileLockIcon;
    this.fileName = fileName;
    this.guidelineImage = guidelineImage;
    this.infoButton = infoButton;
    this.infoIcon = infoIcon;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ListviewItemFileListBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ListviewItemFileListBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.listview_item_file_list, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ListviewItemFileListBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.check_box;
      InertCheckBox checkBox = rootView.findViewById(id);
      if (checkBox == null) {
        break missingId;
      }

      id = R.id.divider;
      AppCompatImageView divider = rootView.findViewById(id);
      if (divider == null) {
        break missingId;
      }

      id = R.id.docTextPlaceHolder;
      TextView docTextPlaceHolder = rootView.findViewById(id);
      if (docTextPlaceHolder == null) {
        break missingId;
      }

      id = R.id.drag_icon;
      AppCompatImageButton dragIcon = rootView.findViewById(id);
      if (dragIcon == null) {
        break missingId;
      }

      id = R.id.file_icon;
      ImageViewTopCrop fileIcon = rootView.findViewById(id);
      if (fileIcon == null) {
        break missingId;
      }

      id = R.id.file_info;
      TextView fileInfo = rootView.findViewById(id);
      if (fileInfo == null) {
        break missingId;
      }

      id = R.id.file_lock_icon;
      AppCompatImageView fileLockIcon = rootView.findViewById(id);
      if (fileLockIcon == null) {
        break missingId;
      }

      id = R.id.file_name;
      TextView fileName = rootView.findViewById(id);
      if (fileName == null) {
        break missingId;
      }

      id = R.id.guideline_image;
      Guideline guidelineImage = rootView.findViewById(id);
      if (guidelineImage == null) {
        break missingId;
      }

      id = R.id.info_button;
      FrameLayout infoButton = rootView.findViewById(id);
      if (infoButton == null) {
        break missingId;
      }

      id = R.id.info_icon;
      AppCompatImageView infoIcon = rootView.findViewById(id);
      if (infoIcon == null) {
        break missingId;
      }

      return new ListviewItemFileListBinding((ConstraintLayout) rootView, checkBox, divider,
          docTextPlaceHolder, dragIcon, fileIcon, fileInfo, fileLockIcon, fileName, guidelineImage,
          infoButton, infoIcon);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}