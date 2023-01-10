// Generated by view binder compiler. Do not edit!
package com.pdftron.demo.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.viewbinding.ViewBinding;
import com.pdftron.demo.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentCriticalPermissionDialogBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final LinearLayout buttons;

  @NonNull
  public final TextView permissionBody;

  @NonNull
  public final Button permissionExit;

  @NonNull
  public final AppCompatImageView permissionImage;

  @NonNull
  public final Button permissionSettings;

  private FragmentCriticalPermissionDialogBinding(@NonNull RelativeLayout rootView,
      @NonNull LinearLayout buttons, @NonNull TextView permissionBody,
      @NonNull Button permissionExit, @NonNull AppCompatImageView permissionImage,
      @NonNull Button permissionSettings) {
    this.rootView = rootView;
    this.buttons = buttons;
    this.permissionBody = permissionBody;
    this.permissionExit = permissionExit;
    this.permissionImage = permissionImage;
    this.permissionSettings = permissionSettings;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentCriticalPermissionDialogBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentCriticalPermissionDialogBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_critical_permission_dialog, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentCriticalPermissionDialogBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.buttons;
      LinearLayout buttons = rootView.findViewById(id);
      if (buttons == null) {
        break missingId;
      }

      id = R.id.permission_body;
      TextView permissionBody = rootView.findViewById(id);
      if (permissionBody == null) {
        break missingId;
      }

      id = R.id.permission_exit;
      Button permissionExit = rootView.findViewById(id);
      if (permissionExit == null) {
        break missingId;
      }

      id = R.id.permission_image;
      AppCompatImageView permissionImage = rootView.findViewById(id);
      if (permissionImage == null) {
        break missingId;
      }

      id = R.id.permission_settings;
      Button permissionSettings = rootView.findViewById(id);
      if (permissionSettings == null) {
        break missingId;
      }

      return new FragmentCriticalPermissionDialogBinding((RelativeLayout) rootView, buttons,
          permissionBody, permissionExit, permissionImage, permissionSettings);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
