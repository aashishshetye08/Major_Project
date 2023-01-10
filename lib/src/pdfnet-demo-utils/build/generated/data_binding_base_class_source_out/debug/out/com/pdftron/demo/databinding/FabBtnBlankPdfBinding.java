// Generated by view binder compiler. Do not edit!
package com.pdftron.demo.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import com.pdftron.demo.R;
import com.pdftron.pdf.widget.PTFloatingActionButton;
import java.lang.NullPointerException;
import java.lang.Override;

public final class FabBtnBlankPdfBinding implements ViewBinding {
  @NonNull
  private final PTFloatingActionButton rootView;

  @NonNull
  public final PTFloatingActionButton blankPDF;

  private FabBtnBlankPdfBinding(@NonNull PTFloatingActionButton rootView,
      @NonNull PTFloatingActionButton blankPDF) {
    this.rootView = rootView;
    this.blankPDF = blankPDF;
  }

  @Override
  @NonNull
  public PTFloatingActionButton getRoot() {
    return rootView;
  }

  @NonNull
  public static FabBtnBlankPdfBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FabBtnBlankPdfBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fab_btn_blank_pdf, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FabBtnBlankPdfBinding bind(@NonNull View rootView) {
    if (rootView == null) {
      throw new NullPointerException("rootView");
    }

    PTFloatingActionButton blankPDF = (PTFloatingActionButton) rootView;

    return new FabBtnBlankPdfBinding((PTFloatingActionButton) rootView, blankPDF);
  }
}
