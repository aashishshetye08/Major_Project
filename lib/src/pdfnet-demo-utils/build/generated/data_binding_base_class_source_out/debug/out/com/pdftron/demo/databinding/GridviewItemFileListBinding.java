// Generated by view binder compiler. Do not edit!
package com.pdftron.demo.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.viewbinding.ViewBinding;
import com.pdftron.demo.R;
import com.pdftron.demo.widget.ForegroundRelativeLayout;
import com.pdftron.demo.widget.ImageViewTopCrop;
import com.pdftron.demo.widget.SquareRelativeLayout;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class GridviewItemFileListBinding implements ViewBinding {
  @NonNull
  private final ForegroundRelativeLayout rootView;

  @NonNull
  public final AppCompatImageView divider;

  @NonNull
  public final TextView docTextPlaceHolder;

  @NonNull
  public final ImageViewTopCrop fileIcon;

  @NonNull
  public final SquareRelativeLayout fileIconLayout;

  @NonNull
  public final TextView fileInfo;

  @NonNull
  public final AppCompatImageView fileLockIcon;

  @NonNull
  public final TextView fileName;

  @NonNull
  public final View infoButton;

  @NonNull
  public final AppCompatImageView infoIcon;

  @NonNull
  public final View separator;

  @NonNull
  public final LinearLayout textLayout;

  private GridviewItemFileListBinding(@NonNull ForegroundRelativeLayout rootView,
      @NonNull AppCompatImageView divider, @NonNull TextView docTextPlaceHolder,
      @NonNull ImageViewTopCrop fileIcon, @NonNull SquareRelativeLayout fileIconLayout,
      @NonNull TextView fileInfo, @NonNull AppCompatImageView fileLockIcon,
      @NonNull TextView fileName, @NonNull View infoButton, @NonNull AppCompatImageView infoIcon,
      @NonNull View separator, @NonNull LinearLayout textLayout) {
    this.rootView = rootView;
    this.divider = divider;
    this.docTextPlaceHolder = docTextPlaceHolder;
    this.fileIcon = fileIcon;
    this.fileIconLayout = fileIconLayout;
    this.fileInfo = fileInfo;
    this.fileLockIcon = fileLockIcon;
    this.fileName = fileName;
    this.infoButton = infoButton;
    this.infoIcon = infoIcon;
    this.separator = separator;
    this.textLayout = textLayout;
  }

  @Override
  @NonNull
  public ForegroundRelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static GridviewItemFileListBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static GridviewItemFileListBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.gridview_item_file_list, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static GridviewItemFileListBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
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

      id = R.id.file_icon;
      ImageViewTopCrop fileIcon = rootView.findViewById(id);
      if (fileIcon == null) {
        break missingId;
      }

      id = R.id.file_icon_layout;
      SquareRelativeLayout fileIconLayout = rootView.findViewById(id);
      if (fileIconLayout == null) {
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

      id = R.id.info_button;
      View infoButton = rootView.findViewById(id);
      if (infoButton == null) {
        break missingId;
      }

      id = R.id.info_icon;
      AppCompatImageView infoIcon = rootView.findViewById(id);
      if (infoIcon == null) {
        break missingId;
      }

      id = R.id.separator;
      View separator = rootView.findViewById(id);
      if (separator == null) {
        break missingId;
      }

      id = R.id.text_layout;
      LinearLayout textLayout = rootView.findViewById(id);
      if (textLayout == null) {
        break missingId;
      }

      return new GridviewItemFileListBinding((ForegroundRelativeLayout) rootView, divider,
          docTextPlaceHolder, fileIcon, fileIconLayout, fileInfo, fileLockIcon, fileName,
          infoButton, infoIcon, separator, textLayout);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
