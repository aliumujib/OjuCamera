package com.alium.ojucamera.internal.ui.view.adapters.sectioned_adapter;

import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.alium.ojucamera.R;
import com.alium.ojucamera.internal.ui.model.PickerTile;
import com.alium.ojucamera.internal.ui.view.adapters.GalleryViewHolder;
import com.alium.ojucamera.internal.ui.view.adapters.ImageGalleryAdapter;
import com.bumptech.glide.Glide;

/**
 * Created by abdulmujibaliu on 11/15/17.
 */

public class GridViewHolder extends GalleryViewHolder {
    public GridViewHolder(View view) {
        super(view);
    }

    @Override
    public void bindGalleryItem(PickerTile pickerTile, ImageGalleryAdapter.OnItemClickListener onItemClickListener, int position) {
        Uri uri = pickerTile.getImageUri();
        Glide.with(itemView.getContext())
                .load(uri)
                //.thumbnail(0.1f)
                .dontAnimate()
                //.centerCrop()
                .placeholder(ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_gallery))
                .error(ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_error))
                .into(iv_thumbnail);
    }
}
