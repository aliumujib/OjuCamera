package com.alium.ojucamera.internal.ui.view.adapters;

import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.alium.ojucamera.R;
import com.alium.ojucamera.internal.ui.model.PickerTile;

/**
 * Created by abdulmujibaliu on 9/29/17.
 */
public class GalleryViewHolder extends RecyclerView.ViewHolder {

    public ImageView iv_thumbnail;

    private String TAG = getClass().getSimpleName();

    public GalleryViewHolder(View view) {
        super(view);
        iv_thumbnail = (ImageView) view.findViewById(R.id.image);
    }

    public void bindGalleryItem(PickerTile pickerTile, final ImageGalleryAdapter.OnItemClickListener onItemClickListener, final int position){
        Uri uri = pickerTile.getImageUri();
        Glide.with(itemView.getContext())
                .load(uri)
                //.thumbnail(0.1f)
                .dontAnimate()
                .centerCrop()
                .placeholder(ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_gallery))
                .error(ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_error))
                .into(iv_thumbnail);

        if (onItemClickListener != null) {
            this.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(itemView, position);
                }
            });
        }
    }
}
