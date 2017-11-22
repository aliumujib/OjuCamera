package com.alium.ojucamera.internal.ui.view.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.alium.ojucamera.R;
import com.alium.ojucamera.internal.ui.model.PickerTile;

import java.util.ArrayList;
import java.util.Collection;

public class ImageGalleryAdapter extends RecyclerView.Adapter<GalleryViewHolder> {

    ArrayList<PickerTile> pickerTiles = new ArrayList<>();
    Context context;
    OnItemClickListener onItemClickListener;
    private String TAG = getClass().getSimpleName();

    public ImageGalleryAdapter(Context context) {
        this.context = context;
    }

    public void addAll(Collection<PickerTile> pickerTiles){
        this.pickerTiles.addAll(pickerTiles);
        notifyDataSetChanged();
    }

    @Override
    public GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.oju_lib_image_item, null);
        return new GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GalleryViewHolder holder, final int position) {

        PickerTile pickerTile = getItem(position);
        holder.bindGalleryItem(pickerTile, onItemClickListener, position);
    }

    @Override
    public int getItemCount() {
        return pickerTiles.size();
    }

    public PickerTile getItem(int position) {
        return pickerTiles.get(position);
    }

    public void setOnItemClickListener(
            OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


}