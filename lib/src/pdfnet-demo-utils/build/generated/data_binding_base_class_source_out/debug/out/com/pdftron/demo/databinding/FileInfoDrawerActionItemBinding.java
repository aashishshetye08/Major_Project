// Generated by view binder compiler. Do not edit!
package com.pdftron.demo.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import com.pdftron.demo.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FileInfoDrawerActionItemBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ConstraintLayout container;

  @NonNull
  public final CardView iconBackground;

  @NonNull
  public final AppCompatImageButton itemIcon;

  @NonNull
  public final TextView itemText;

  private FileInfoDrawerActionItemBinding(@NonNull ConstraintLayout rootView,
      @NonNull ConstraintLayout container, @NonNull CardView iconBackground,
      @NonNull AppCompatImageButton itemIcon, @NonNull TextView itemText) {
    this.rootView = rootView;
    this.container = container;
    this.iconBackground = iconBackground;
    this.itemIcon = itemIcon;
    this.itemText = itemText;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FileInfoDrawerActionItemBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FileInfoDrawerActionItemBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.file_info_drawer_action_item, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FileInfoDrawerActionItemBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      ConstraintLayout container = (ConstraintLayout) rootView;

      id = R.id.icon_background;
      CardView iconBackground = rootView.findViewById(id);
      if (iconBackground == null) {
        break missingId;
      }

      id = R.id.item_icon;
      AppCompatImageButton itemIcon = rootView.findViewById(id);
      if (itemIcon == null) {
        break missingId;
      }

      id = R.id.item_text;
      TextView itemText = rootView.findViewById(id);
      if (itemText == null) {
        break missingId;
      }

      return new FileInfoDrawerActionItemBinding((ConstraintLayout) rootView, container,
          iconBackground, itemIcon, itemText);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
