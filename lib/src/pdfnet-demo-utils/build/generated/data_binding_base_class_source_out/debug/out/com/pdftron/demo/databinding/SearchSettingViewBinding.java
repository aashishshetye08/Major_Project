// Generated by view binder compiler. Do not edit!
package com.pdftron.demo.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import com.pdftron.demo.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class SearchSettingViewBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final ImageView settingSearchIcon;

  private SearchSettingViewBinding(@NonNull LinearLayout rootView,
      @NonNull ImageView settingSearchIcon) {
    this.rootView = rootView;
    this.settingSearchIcon = settingSearchIcon;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static SearchSettingViewBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static SearchSettingViewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.search_setting_view, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static SearchSettingViewBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.setting_search_icon;
      ImageView settingSearchIcon = rootView.findViewById(id);
      if (settingSearchIcon == null) {
        break missingId;
      }

      return new SearchSettingViewBinding((LinearLayout) rootView, settingSearchIcon);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
