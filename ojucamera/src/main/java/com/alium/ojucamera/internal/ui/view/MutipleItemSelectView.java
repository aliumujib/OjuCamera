package com.alium.ojucamera.internal.ui.view;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.GridLayoutManager;

import com.alium.ojucamera.R;
import com.alium.ojucamera.internal.configuration.CameraConfiguration;
import com.alium.ojucamera.internal.repository.MediaRepository;
import com.alium.ojucamera.internal.ui.model.PickerTile;
import com.alium.ojucamera.internal.ui.view.adapters.sectioned_adapter.BaseGalleryAdapter;
import com.alium.ojucamera.internal.ui.view.adapters.sectioned_adapter.GalleryAdapterByDecade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by abdulmujibaliu on 9/29/17.
 */

public class MutipleItemSelectView extends LinearLayout {

    private final View mView;
    private final int mediatype = CameraConfiguration.PHOTO_AND_VIDEO;
    private LinearLayout mMultiselectPicker;
    private Toolbar mMultiSelectorToolbar;
    private RecyclerView mExpandedRecyclerView;
    private GalleryAdapterByDecade mSectionedRecyclerAdapter;

    public String TAG = getClass().getSimpleName();

    private Comparator<PickerTile> mediaComparator;
    private List<PickerTile> pickerTileArrayList = new ArrayList<>();
    private CameraControlPanel.PickerItemClickListener pickerItemClickListener;

    public MutipleItemSelectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);


        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mView = inflater.inflate(R.layout.ojulib_multiple_select_view, this, true);
        initView(mView);
        setUpAdapter(CameraConfiguration.PHOTO_AND_VIDEO);
    }

    public RecyclerView getmExpandedRecyclerView() {
        return mExpandedRecyclerView;
    }

    public void setUpAdapter(int mediatype) {


        Consumer<List<PickerTile>> successConsumer = new Consumer<List<PickerTile>>() {
            @Override
            public void accept(@NonNull List<PickerTile> pickerTiles) throws Exception {
                Log.d(TAG, String.valueOf(pickerTiles.size()));
                sortItemsByDate(pickerTiles);
            }
        };
        Consumer<Throwable> failuireConsumer = new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                throwable.printStackTrace();
            }
        };


        if (mediatype == CameraConfiguration.PHOTO_AND_VIDEO) {
            MediaRepository.sharedInstance.getAllMedia(getActivity()).subscribe(successConsumer, failuireConsumer);
        } else if (mediatype == CameraConfiguration.PHOTO) {
            MediaRepository.sharedInstance.getPhotos(getActivity()).subscribe(successConsumer, failuireConsumer);
        } else if (mediatype == CameraConfiguration.VIDEO) {
            MediaRepository.sharedInstance.getVideos(getActivity()).subscribe(successConsumer, failuireConsumer);
        }


    }

    public void setPickerItemClickListener(CameraControlPanel.PickerItemClickListener pickerItemClickListener) {
        this.pickerItemClickListener = pickerItemClickListener;
    }

    private void sortItemsByDate(List<PickerTile> pickerTiles) {
        this.mediaComparator = new Comparator<PickerTile>() {
            @Override
            public int compare(PickerTile o1, PickerTile o2) {
                return o2.getDateCreated().getDayOfYear() - o1.getDateCreated().getDayOfYear();
            }
        };
        Collections.sort(pickerTiles, mediaComparator);
        Log.d(TAG, "AFTER SORT " +String.valueOf(pickerTiles.size()));
        mSectionedRecyclerAdapter = new GalleryAdapterByDecade(pickerTiles);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4);
        mExpandedRecyclerView.setLayoutManager(gridLayoutManager);
        mSectionedRecyclerAdapter.setGridLayoutManager(gridLayoutManager);
        mSectionedRecyclerAdapter.setOnItemClickListener(new BaseGalleryAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(PickerTile tile) {
                pickerItemClickListener.onItemClick(tile.getImageUri());
            }

            @Override
            public void onSubheaderClicked(int position) {

            }
        });
        mExpandedRecyclerView.setAdapter(mSectionedRecyclerAdapter);
        mSectionedRecyclerAdapter.addAll(pickerTiles);
    }

    public Activity getActivity() {
        Context context = getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }


    private void initView(View view) {
        mMultiselectPicker = (LinearLayout) view.findViewById(R.id.multiselect_picker);
        mMultiSelectorToolbar = (Toolbar) view.findViewById(R.id.multi_selector_toolbar);
        mExpandedRecyclerView = (RecyclerView) view.findViewById(R.id.expanded_recycler_view);

        if(getActivity() instanceof AppCompatActivity){
            ((AppCompatActivity)getActivity()).setSupportActionBar(mMultiSelectorToolbar);
            ActionBar actionBar;
            if((actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar())!=null){
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setTitle("Pick Photos");
            }
        }


    }
}
