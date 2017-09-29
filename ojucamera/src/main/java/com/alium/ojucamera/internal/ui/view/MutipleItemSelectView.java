package com.alium.ojucamera.internal.ui.view;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
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
import com.alium.ojucamera.internal.ui.view.adapters.ImageGalleryAdapter;
import com.alium.ojucamera.internal.ui.view.adapters.sectioned_adapter.GalleryAdapterByDecade;

import java.util.AbstractCollection;
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


    private Comparator<PickerTile> movieComparator;
    private List<PickerTile> imageGalleryAdapter = new ArrayList<>();

    public MutipleItemSelectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);


        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mView = inflater.inflate(R.layout.ojulib_multiple_select_view, this, true);
        initView(mView);
        setUpAdapter(CameraConfiguration.PHOTO_AND_VIDEO);
    }


    private void setUpAdapter(int mediatype) {


        Consumer<List<PickerTile>> successConsumer = new Consumer<List<PickerTile>>() {
            @Override
            public void accept(@NonNull List<PickerTile> pickerTiles) throws Exception {
                //Log.d(TAG, String.valueOf(pickerTiles.size()));
                setAdapterByDecade(imageGalleryAdapter);
                mSectionedRecyclerAdapter.addAll(pickerTiles);
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

    private void setAdapterByDecade(List<PickerTile> pickerTiles) {
        this.movieComparator = new Comparator<PickerTile>() {
            @Override
            public int compare(PickerTile o1, PickerTile o2) {
                return o1.getDateCreated().getDayOfYear() - o2.getDateCreated().getDayOfYear();
            }
        };
        Collections.sort(pickerTiles, movieComparator);
        mSectionedRecyclerAdapter = new GalleryAdapterByDecade();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4);
        mExpandedRecyclerView.setLayoutManager(gridLayoutManager);
        mSectionedRecyclerAdapter.setGridLayoutManager(gridLayoutManager);
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

        setAdapterByDecade(new ArrayList<PickerTile>());
    }
}
