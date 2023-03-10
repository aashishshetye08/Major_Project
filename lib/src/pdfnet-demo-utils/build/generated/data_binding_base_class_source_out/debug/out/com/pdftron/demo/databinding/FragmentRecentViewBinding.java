// Generated by view binder compiler. Do not edit!
package com.pdftron.demo.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.viewbinding.ViewBinding;
import com.google.android.material.appbar.AppBarLayout;
import com.pdftron.demo.R;
import com.pdftron.pdf.widget.ForegroundCoordinatorLayout;
import com.pdftron.pdf.widget.PTFloatingActionMenu;
import com.pdftron.pdf.widget.recyclerview.SimpleRecyclerView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentRecentViewBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final AppBarLayout appbar;

  @NonNull
  public final TextView emptyTextViewForFilter;

  @NonNull
  public final EmptyListViewBinding emptyView;

  @NonNull
  public final PTFloatingActionMenu fabMenu;

  @NonNull
  public final ForegroundCoordinatorLayout fragmentContent;

  @NonNull
  public final Toolbar fragmentToolbar;

  @NonNull
  public final ProgressBar progressBarView;

  @NonNull
  public final SimpleRecyclerView recyclerView;

  @NonNull
  public final FrameLayout upgradeContainer;

  private FragmentRecentViewBinding(@NonNull LinearLayout rootView, @NonNull AppBarLayout appbar,
      @NonNull TextView emptyTextViewForFilter, @NonNull EmptyListViewBinding emptyView,
      @NonNull PTFloatingActionMenu fabMenu, @NonNull ForegroundCoordinatorLayout fragmentContent,
      @NonNull Toolbar fragmentToolbar, @NonNull ProgressBar progressBarView,
      @NonNull SimpleRecyclerView recyclerView, @NonNull FrameLayout upgradeContainer) {
    this.rootView = rootView;
    this.appbar = appbar;
    this.emptyTextViewForFilter = emptyTextViewForFilter;
    this.emptyView = emptyView;
    this.fabMenu = fabMenu;
    this.fragmentContent = fragmentContent;
    this.fragmentToolbar = fragmentToolbar;
    this.progressBarView = progressBarView;
    this.recyclerView = recyclerView;
    this.upgradeContainer = upgradeContainer;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentRecentViewBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentRecentViewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_recent_view, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentRecentViewBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.appbar;
      AppBarLayout appbar = rootView.findViewById(id);
      if (appbar == null) {
        break missingId;
      }

      id = R.id.empty_text_view_for_filter;
      TextView emptyTextViewForFilter = rootView.findViewById(id);
      if (emptyTextViewForFilter == null) {
        break missingId;
      }

      id = R.id.empty_view;
      View emptyView = rootView.findViewById(id);
      if (emptyView == null) {
        break missingId;
      }
      EmptyListViewBinding binding_emptyView = EmptyListViewBinding.bind(emptyView);

      id = R.id.fab_menu;
      PTFloatingActionMenu fabMenu = rootView.findViewById(id);
      if (fabMenu == null) {
        break missingId;
      }

      id = R.id.fragment_content;
      ForegroundCoordinatorLayout fragmentContent = rootView.findViewById(id);
      if (fragmentContent == null) {
        break missingId;
      }

      id = R.id.fragment_toolbar;
      Toolbar fragmentToolbar = rootView.findViewById(id);
      if (fragmentToolbar == null) {
        break missingId;
      }

      id = R.id.progress_bar_view;
      ProgressBar progressBarView = rootView.findViewById(id);
      if (progressBarView == null) {
        break missingId;
      }

      id = R.id.recycler_view;
      SimpleRecyclerView recyclerView = rootView.findViewById(id);
      if (recyclerView == null) {
        break missingId;
      }

      id = R.id.upgrade_container;
      FrameLayout upgradeContainer = rootView.findViewById(id);
      if (upgradeContainer == null) {
        break missingId;
      }

      return new FragmentRecentViewBinding((LinearLayout) rootView, appbar, emptyTextViewForFilter,
          binding_emptyView, fabMenu, fragmentContent, fragmentToolbar, progressBarView,
          recyclerView, upgradeContainer);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
