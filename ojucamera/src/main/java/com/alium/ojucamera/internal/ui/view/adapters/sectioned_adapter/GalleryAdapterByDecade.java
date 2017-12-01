package com.alium.ojucamera.internal.ui.view.adapters.sectioned_adapter;

import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

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

public class GalleryAdapterByDecade extends BaseGalleryAdapter implements GalleryViewHolder.MultiSelectAdapterInteractor {

    private String TAG = getClass().getSimpleName();

    private View.OnLongClickListener longClickListener;
    private View.OnClickListener clickListener;

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

    public void setLongClickListener(View.OnLongClickListener clickListener) {
        this.longClickListener = clickListener;
    }

    public void setClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void selectRangeChange(int start, int end, boolean isSelected) {
        if (start < 0 || end >= pickerTileList.size()) {
            return;
        }
        if (isSelected) {
            dataSelect(start, end);
        } else {
            dataUnselect(start, end);
        }
    }

    public PickerTile getItem(int i) {
        return pickerTileList.get(i);
    }

    public int getSelectedSize() {
        if (pickerTileList.isEmpty()) {
            return 0;
        }
        int result = 0;
        for (PickerTile item : pickerTileList) {
            if (item.isSelected()) {
                result++;
            }
        }
        return result;
    }



    private void dataSelect(int start, int end) {
        for (int i = start; i <= end; i++) {
            PickerTile pickerTile = getItem(i);
            if (!pickerTile.isSelected()) {
                pickerTile.setSelected(true);
                notifyItemChanged(i);
            }
        }
    }

    private void dataUnselect(int start, int end) {
        for (int i = start; i <= end; i++) {
            PickerTile pickerTile = getItem(i);
            if (pickerTile.isSelected()) {
                pickerTile.setSelected(false);
                notifyItemChangedAtPosition(i);
            }
        }
    }

    public void setSelected(int position, boolean selected) {
        if (pickerTileList.get(position).isSelected() != selected) {
            pickerTileList.get(position).setSelected(selected);
            notifyItemChangedAtPosition(position);
        }
    }

    @Override
    public void onBindItemViewHolder(final GalleryViewHolder holder, final int position) {
        final PickerTile pickerTile = pickerTileList.get(position);
        //Log.d(TAG, "Binding" + position + " position " + pickerTile.getImageUri().toString());
        holder.bindMultiSelectItem(pickerTile, position,this);
    }

    @Override
    public void onBindSubheaderViewHolder(SubheaderHolder subheaderHolder, int nextItemPosition) {
        super.onBindSubheaderViewHolder(subheaderHolder, nextItemPosition);
        final PickerTile nextMovie = pickerTileList.get(subheaderHolder.getAdapterPosition());
        String date = DateTimeUtils.getMonthFromInt(nextMovie.getDateCreated().getMonthOfYear() + 1) + ", " + nextMovie.getDateCreated().getYear();
        subheaderHolder.mSubheaderText.setText(date);
    }
}
