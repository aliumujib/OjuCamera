package com.alium.ojucamera.internal.ui.view.adapters.sectioned_adapter;

import android.text.format.DateUtils;
import android.view.View;

import com.alium.ojucamera.internal.ui.model.PickerTile;
import com.alium.ojucamera.internal.ui.view.adapters.GalleryViewHolder;

import java.util.List;

/**
 * @author Vladislav Zhukov (https://github.com/zhukic)
 */

public class GalleryAdapterByDecade extends BaseGalleryAdapter {

    public GalleryAdapterByDecade() {
        super();
    }

    @Override
    public boolean onPlaceSubheaderBetweenItems(int position) {
        final PickerTile movie = movieList.get(position);
        final PickerTile nextMovie = movieList.get(position + 1);

        return movie.getDateCreated().getDayOfYear() == nextMovie.getDateCreated().getDayOfYear();
    }

    @Override
    public void onBindItemViewHolder(final GalleryViewHolder holder, final int position) {
        final PickerTile movie = movieList.get(position);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClicked(movie);
            }
        });
    }

    @Override
    public void onBindSubheaderViewHolder(SubheaderHolder subheaderHolder, int nextItemPosition) {
        super.onBindSubheaderViewHolder(subheaderHolder, nextItemPosition);
        final PickerTile nextMovie = movieList.get(nextItemPosition);
        String decade = DateUtils.getRelativeTimeSpanString(nextMovie.getDateCreated().getMillisOfDay()).toString();
        subheaderHolder.mSubheaderText.setText(decade);
    }
}
