package com.alium.ojucamera.internal.ui.view.adapters.sectioned_adapter;

import android.graphics.Typeface;
import android.support.annotation.CallSuper;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alium.ojucamera.R;
import com.alium.ojucamera.internal.ui.model.PickerTile;
import com.alium.ojucamera.internal.ui.view.adapters.GalleryViewHolder;
import com.zhukic.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Vladislav Zhukov (https://github.com/zhukic)
 */

public abstract class BaseGalleryAdapter extends SectionedRecyclerViewAdapter<BaseGalleryAdapter.SubheaderHolder, GalleryViewHolder> {

    private String TAG = getClass().getSimpleName();
  protected   List<PickerTile> pickerTileList = new ArrayList<>();

    public void addAll(List<PickerTile> pickerTiles) {
        pickerTileList.addAll(pickerTiles);
        Log.d(TAG, "Added: " + pickerTileList.size());
        this.notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClicked(PickerTile movie);

        void onSubheaderClicked(int position);
    }

    OnItemClickListener onItemClickListener;

    static class SubheaderHolder extends RecyclerView.ViewHolder {

        private static Typeface meduiumTypeface = null;

        TextView mSubheaderText;
        ImageView mArrow;

        SubheaderHolder(View itemView) {
            super(itemView);
            this.mSubheaderText = (TextView) itemView.findViewById(R.id.subheaderText);
            this.mArrow = (ImageView) itemView.findViewById(R.id.arrow);

           /* if (meduiumTypeface == null) {
                meduiumTypeface = Typeface.createFromAsset(itemView.getContext().getAssets(), "Roboto-Medium.ttf");
                            this.mSubheaderText.setTypeface(meduiumTypeface);
            }*/
        }

    }


    @Override
    public GalleryViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return new GalleryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.ojulib_image_item_big, parent, false));
    }

    @Override
    public SubheaderHolder onCreateSubheaderViewHolder(ViewGroup parent, int viewType) {
        return new SubheaderHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.oju_lib_item_header, parent, false));
    }

    @Override
    @CallSuper
    public void onBindSubheaderViewHolder(final SubheaderHolder subheaderHolder, int nextItemPosition) {

        boolean isSectionExpanded = isSectionExpanded(getSectionIndex(subheaderHolder.getAdapterPosition()));
        subheaderHolder.mArrow.setImageDrawable(null);
        subheaderHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener!=null){
                    onItemClickListener.onSubheaderClicked(subheaderHolder.getAdapterPosition());
                }
            }
        });

    }

    @Override
    public int getItemSize() {
        return pickerTileList.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}
