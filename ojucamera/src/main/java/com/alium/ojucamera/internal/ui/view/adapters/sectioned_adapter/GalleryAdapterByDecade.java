package com.alium.ojucamera.internal.ui.view.adapters.sectioned_adapter;

import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;

import com.alium.ojucamera.internal.ui.model.PickerTile;
import com.alium.ojucamera.internal.ui.view.adapters.GalleryViewHolder;
import com.alium.ojucamera.internal.utils.DateTimeUtils;

import org.joda.time.Duration;
import org.joda.time.LocalDateTime;
import org.joda.time.Period;

import java.util.List;

/**
 * @author Vladislav Zhukov (https://github.com/zhukic)
 * @Override public boolean onPlaceSubheaderBetweenItems(int position) {
 * final Movie movie = movieList.get(position);
 * final Movie nextMovie = movieList.get(position + 1);
 * <p>
 * return movie.getYear() / 10 != nextMovie.getYear() / 10;
 * }
 */

public class GalleryAdapterByDecade extends BaseGalleryAdapter {

    private String TAG = getClass().getSimpleName();

    public GalleryAdapterByDecade(List<PickerTile> pickerTiles) {
        super();
        this.pickerTileList = pickerTiles;
    }

    @Override
    public boolean onPlaceSubheaderBetweenItems(int position) {
        final PickerTile movie = pickerTileList.get(position);
        final PickerTile nextMovie = pickerTileList.get(position + 1);

        boolean shouldPlace = movie.getDateCreated().getMonthOfYear() != nextMovie.getDateCreated().getMonthOfYear();

        Log.d(TAG, "Should Place: " + shouldPlace);

        return shouldPlace;
    }


    @Override
    public void onBindItemViewHolder(final GalleryViewHolder holder, final int position) {
        final PickerTile pickerTile = pickerTileList.get(position);
        Log.d(TAG, "Binding" + position + " position " + pickerTile.getImageUri().toString());
        holder.bindGalleryItem(pickerTile, null, position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClicked(pickerTile);
                }
            }
        });
    }

    @Override
    public void onBindSubheaderViewHolder(SubheaderHolder subheaderHolder, int nextItemPosition) {
        super.onBindSubheaderViewHolder(subheaderHolder, nextItemPosition);
        final PickerTile nextMovie = pickerTileList.get(subheaderHolder.getAdapterPosition());
        String date = DateTimeUtils.getMonthFromInt(nextMovie.getDateCreated().getMonthOfYear() + 1) + ", " + nextMovie.getDateCreated().getYear();
        subheaderHolder.mSubheaderText.setText(date);
    }
}
