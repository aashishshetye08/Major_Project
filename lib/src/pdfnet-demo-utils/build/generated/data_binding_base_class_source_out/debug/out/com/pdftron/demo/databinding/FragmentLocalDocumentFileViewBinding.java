// Generated by view binder compiler. Do not edit!
package com.pdftron.demo.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.viewbinding.ViewBinding;
import com.pdftron.demo.R;
import com.pdftron.demo.browser.ui.StickyHeader;
import com.pdftron.demo.browser.ui.StickyRecyclerView;
import com.pdftron.pdf.widget.ForegroundCoordinatorLayout;
import com.pdftron.pdf.widget.PTFloatingActionMenu;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentLocalDocumentFileViewBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final PTFloatingActionMenu fabMenu;

  @NonNull
  public final ForegroundCoordinatorLayout fragmentContent;

  @NonNull
  public final Toolbar fragmentToolbar;

  @NonNull
  public final ProgressBar progressBarView;

  @NonNull
  public final StickyRecyclerView recyclerView;

  @NonNull
  public final StickyHeader stickyHeader;

  private FragmentLocalDocumentFileViewBinding(@NonNull LinearLayout rootView,
      @NonNull PTFloatingActionMenu fabMenu, @NonNull ForegroundCoordinatorLayout fragmentContent,
      @NonNull Toolbar fragmentToolbar, @NonNull ProgressBar progressBarView,
      @NonNull StickyRecyclerView recyclerView, @NonNull StickyHeader stickyHeader) {
    this.rootView = rootView;
    this.fabMenu = fabMenu;
    this.fragmentContent = fragmentContent;
    this.fragmentToolbar = fragmentToolbar;
    this.progressBarView = progressBarView;
    this.recyclerView = recyclerView;
    this.stickyHeader = stickyHeader;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentLocalDocumentFileViewBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentLocalDocumentFileViewBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_local_document_file_view, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentLocalDocumentFileViewBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
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
      StickyRecyclerView recyclerView = rootView.findViewById(id);
      if (recyclerView == null) {
        break missingId;
      }

      id = R.id.sticky_header;
      StickyHeader stickyHeader = rootView.findViewById(id);
      if (stickyHeader == null) {
        break missingId;
      }

      return new FragmentLocalDocumentFileViewBinding((LinearLayout) rootView, fabMenu,
          fragmentContent, fragmentToolbar, progressBarView, recyclerView, stickyHeader);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}