// Generated by view binder compiler. Do not edit!
package com.pdftron.demo.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import com.pdftron.demo.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class BreadcrumbBarBinding implements ViewBinding {
  @NonNull
  private final HorizontalScrollView rootView;

  @NonNull
  public final LinearLayout breadcrumbBarLayout;

  @NonNull
  public final HorizontalScrollView breadcrumbBarScrollView;

  private BreadcrumbBarBinding(@NonNull HorizontalScrollView rootView,
      @NonNull LinearLayout breadcrumbBarLayout,
      @NonNull HorizontalScrollView breadcrumbBarScrollView) {
    this.rootView = rootView;
    this.breadcrumbBarLayout = breadcrumbBarLayout;
    this.breadcrumbBarScrollView = breadcrumbBarScrollView;
  }

  @Override
  @NonNull
  public HorizontalScrollView getRoot() {
    return rootView;
  }

  @NonNull
  public static BreadcrumbBarBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static BreadcrumbBarBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.breadcrumb_bar, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static BreadcrumbBarBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.breadcrumb_bar_layout;
      LinearLayout breadcrumbBarLayout = rootView.findViewById(id);
      if (breadcrumbBarLayout == null) {
        break missingId;
      }

      HorizontalScrollView breadcrumbBarScrollView = (HorizontalScrollView) rootView;

      return new BreadcrumbBarBinding((HorizontalScrollView) rootView, breadcrumbBarLayout,
          breadcrumbBarScrollView);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}