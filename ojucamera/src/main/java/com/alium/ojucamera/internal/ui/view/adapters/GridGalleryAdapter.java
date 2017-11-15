package com.alium.ojucamera.internal.ui.view.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.alium.ojucamera.R;

/**
 * Created by abdulmujibaliu on 11/13/17.
 */

public class GridGalleryAdapter extends ImageGalleryAdapter {

    public GridGalleryAdapter(Context context) {
        super(context);
    }

    @Override
    public GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.ojulib_image_item_big, null);
        return new GalleryViewHolder(view);
    }
}
