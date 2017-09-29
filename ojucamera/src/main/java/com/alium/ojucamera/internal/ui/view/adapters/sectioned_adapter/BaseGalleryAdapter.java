package com.alium.ojucamera.internal.ui.view.adapters.sectioned_adapter;

import android.graphics.Typeface;
import android.support.annotation.CallSuper;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alium.ojucamera.R;
import com.alium.ojucamera.internal.ui.model.PickerTile;
import com.alium.ojucamera.internal.ui.view.adapters.GalleryViewHolder;
import com.zhukic.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.util.List;


/**
 * @author Vladislav Zhukov (https://github.com/zhukic)
 */

public abstract class BaseGalleryAdapter extends SectionedRecyclerViewAdapter<BaseGalleryAdapter.SubheaderHolder, GalleryViewHolder> {

    public interface OnItemClickListener {
        void onItemClicked(PickerTile movie);

        void onSubheaderClicked(int position);
    }

    List<PickerTile> movieList;

    OnItemClickListener onItemClickListener;

    static class SubheaderHolder extends RecyclerView.ViewHolder {

        private static Typeface meduiumTypeface = null;

        TextView mSubheaderText;
        ImageView mArrow;

        SubheaderHolder(View itemView) {
            super(itemView);
            this.mSubheaderText = (TextView) itemView.findViewById(R.id.subheaderText);
            this.mArrow = (ImageView) itemView.findViewById(R.id.arrow);

            if (meduiumTypeface == null) {
                meduiumTypeface = Typeface.createFromAsset(itemView.getContext().getAssets(), "Roboto-Medium.ttf");
            }
            this.mSubheaderText.setTypeface(meduiumTypeface);
        }

    }

    BaseGalleryAdapter(List<PickerTile> itemList) {
        super();
        this.movieList = itemList;
    }

    @Override
    public GalleryViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return new GalleryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false));
    }

    @Override
    public SubheaderHolder onCreateSubheaderViewHolder(ViewGroup parent, int viewType) {
        return new SubheaderHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.oju_lib_item_header, parent, false));
    }

    @Override
    @CallSuper
    public void onBindSubheaderViewHolder(final SubheaderHolder subheaderHolder, int nextItemPosition) {

        boolean isSectionExpanded = isSectionExpanded(getSectionIndex(subheaderHolder.getAdapterPosition()));

        if (isSectionExpanded) {
            subheaderHolder.mArrow.setImageDrawable(ContextCompat.getDrawable(subheaderHolder.itemView.getContext(), R.drawable.ic_keyboard_arrow_up_black_24dp));
        } else {
            subheaderHolder.mArrow.setImageDrawable(ContextCompat.getDrawable(subheaderHolder.itemView.getContext(), R.drawable.ic_keyboard_arrow_down_black_24dp));
        }

        subheaderHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onSubheaderClicked(subheaderHolder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemSize() {
        return movieList.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}
