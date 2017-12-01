package com.alium.ojucamera.internal.ui.view.adapters;

import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
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

    public void loadGalleryItem(PickerTile pickerTile) {
        Uri uri = pickerTile.getImageUri();

        Glide.with(itemView.getContext())
                .load(uri)
                //.thumbnail(0.1f)
                .dontAnimate()
                .centerCrop()
                .placeholder(ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_gallery))
                .error(ContextCompat.getDrawable(itemView.getContext(), R.drawable.ic_error))
                .into(iv_thumbnail);
    }


    public void bindGalleryItem(PickerTile pickerTile, final ImageGalleryAdapter.OnItemClickListener onItemClickListener, final int position) {
        loadGalleryItem(pickerTile);

        if (onItemClickListener != null) {
            this.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(itemView, position);
                }
            });
        }
    }

    public void bindMultiSelectItem(final PickerTile pickerTile, final int position, final MultiSelectAdapterInteractor interactor) {
        loadGalleryItem(pickerTile);

        if (pickerTile.isSelected()) {
            ViewCompat.setScaleX(this.itemView, 0.8f);
            ViewCompat.setScaleY(this.itemView, 0.8f);
            ViewCompat.animate(this.itemView);
            //holder.textView.setTextColor(colorSelect);
        } else {
            ViewCompat.setScaleX(this.itemView, 1f);
            ViewCompat.setScaleY(this.itemView, 1f);
            ViewCompat.animate(this.itemView);
            //holder.textView.setTextColor(colorNormal);
        }

        this.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Clicked" + position + "th position which is " + pickerTile.isSelected());
                if (pickerTile.isSelected()) {
                    interactor.setSelected(position, false);
                } else {
                    interactor.setSelected(position, true);

                }

//                if (onItemClickListener != null) {
//                    onItemClickListener.onItemClicked(pickerTile, position);
//                }
            }
        });


        this.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d(TAG, "Clicked" + position + "th position which is " + pickerTile.isSelected());
                if (pickerTile.isSelected()) {
                    interactor.setSelected(position, false);
                } else {
                    interactor.setSelected(position, true);

                }
                return false;
            }
        });
    }

    public interface MultiSelectAdapterInteractor {
        void setSelected(int position, boolean selected);

    }

}

