package com.pdftron.demo.navigation.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;
import androidx.recyclerview.widget.RecyclerView;

import com.pdftron.demo.R;
import com.pdftron.demo.browser.db.trash.TrashEntity;
import com.pdftron.demo.databinding.ListviewItemTrashListBinding;
import com.pdftron.pdf.utils.Utils;
import com.pdftron.pdf.widget.recyclerview.ViewHolderBindListener;

import java.util.List;

public class TrashAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<TrashEntity> mTrashList;
    ListviewItemTrashListBinding mBinding;
    Theme mTheme;
    private ViewHolderBindListener mBindListener;

    public TrashAdapter(List<TrashEntity> trashList, ViewHolderBindListener bindListener) {
        mTrashList = trashList;
        mBindListener = bindListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mBinding = ListviewItemTrashListBinding.inflate(LayoutInflater.from(parent.getContext()));
        mTheme = Theme.fromContext(parent.getContext());
        return new TrashViewHolder(mBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (mBindListener != null) {
            mBindListener.onBindViewHolder(holder, position);
        }
        TrashEntity trash = mTrashList.get(position);
        TrashViewHolder viewHolder = (TrashViewHolder) holder;
        viewHolder.imageViewFileIcon.setImageResource(trash.getIsDirectory() ?
                R.drawable.ic_folder_large : R.drawable.ic_file_blank_24dp);
        viewHolder.textViewFileName.setText(trash.getOriginalName());
        viewHolder.textViewFileInfo.setText(String.format("%s   %s", trash.getTrashDate().toString(),
                trash.getFileSize()));

        viewHolder.divider.setBackgroundColor(mTheme.itemDividerColor);
        viewHolder.imageViewFileIcon.setColorFilter(mTheme.iconColor);
        viewHolder.textViewFileName.setTextColor(mTheme.contentBodyTextColor);
        viewHolder.textViewFileInfo.setTextColor(mTheme.contentSecondaryTextColor);
    }

    @Override
    public int getItemCount() {
        return mTrashList.size();
    }

    public TrashEntity getItem(int position) {
        if (mTrashList != null && position >= 0 && position < mTrashList.size()) {
            return mTrashList.get(position);
        }
        return null;
    }

    class TrashViewHolder extends RecyclerView.ViewHolder {
        ImageView divider;
        ImageView imageViewFileIcon;
        TextView textViewFileName;
        TextView textViewFileInfo;

        public TrashViewHolder(@NonNull View itemView) {
            super(itemView);
            divider = mBinding.divider;
            imageViewFileIcon = mBinding.fileIcon;
            textViewFileName = mBinding.fileName;
            textViewFileInfo = mBinding.fileInfo;
        }
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    public static final class Theme {
        @ColorInt
        public final int contentBodyTextColor;
        @ColorInt
        public final int contentSecondaryTextColor;
        @ColorInt
        public final int iconColor;
        @ColorInt
        public final int itemDividerColor;

        Theme(int contentBodyTextColor, int contentSecondaryTextColor, int iconColor, int itemDividerColor) {
            this.contentBodyTextColor = contentBodyTextColor;
            this.contentSecondaryTextColor = contentSecondaryTextColor;
            this.iconColor = iconColor;
            this.itemDividerColor = itemDividerColor;
        }

        public static Theme fromContext(@NonNull Context context) {

            final TypedArray a = context.obtainStyledAttributes(
                    null, R.styleable.AllDocumentFileBrowserTheme, R.attr.pt_all_document_browser_style, R.style.PTAllDocumentFileBrowserTheme);
            int contentBodyTextColor = a.getColor(R.styleable.AllDocumentFileBrowserTheme_contentBodyTextColor, Utils.getPrimaryTextColor(context));
            int contentSecondaryTextColor = a.getColor(R.styleable.AllDocumentFileBrowserTheme_contentSecondaryTextColor, Utils.getSecondaryTextColor(context));
            int iconColor = a.getColor(R.styleable.AllDocumentFileBrowserTheme_iconColor, Utils.getPrimaryTextColor(context));
            int itemDividerColor = a.getColor(R.styleable.AllDocumentFileBrowserTheme_itemDividerColor, context.getResources().getColor(R.color.browser_divider_color));
            a.recycle();

            return new Theme(
                    contentBodyTextColor,
                    contentSecondaryTextColor,
                    iconColor,
                    itemDividerColor
            );
        }
    }
}
