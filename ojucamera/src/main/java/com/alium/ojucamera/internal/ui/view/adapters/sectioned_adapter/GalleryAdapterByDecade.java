package com.alium.ojucamera.internal.ui.view.adapters.sectioned_adapter;

import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;

import com.alium.ojucamera.internal.ui.model.PickerTile;
import com.alium.ojucamera.internal.ui.view.adapters.GalleryViewHolder;

/**
 * @author Vladislav Zhukov (https://github.com/zhukic)
 */

public class GalleryAdapterByDecade extends BaseGalleryAdapter {

    private String TAG = getClass().getSimpleName();

    public GalleryAdapterByDecade() {
        super();
    }

    @Override
    public boolean onPlaceSubheaderBetweenItems(int position) {
        final PickerTile movie = pickerTileList.get(position);
        final PickerTile nextMovie = pickerTileList.get(position + 1);

        return movie.getDateCreated().getDayOfYear() == nextMovie.getDateCreated().getDayOfYear();
    }

    @Override
    public void onBindItemViewHolder(final GalleryViewHolder holder, final int position) {
        final PickerTile pickerTile = pickerTileList.get(position);
        Log.d(TAG, "Binding" + position + " position " + pickerTile.getImageUri().toString());
        holder.bindGalleryItem(pickerTile, null, position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClicked(pickerTile);
            }
        });
    }

    @Override
    public void onBindSubheaderViewHolder(SubheaderHolder subheaderHolder, int nextItemPosition) {
        super.onBindSubheaderViewHolder(subheaderHolder, nextItemPosition);
        final PickerTile nextMovie = pickerTileList.get(nextItemPosition);
        String decade = DateUtils.getRelativeTimeSpanString(nextMovie.getDateCreated().getMillisOfDay()).toString();
        subheaderHolder.mSubheaderText.setText(decade);
    }
}
