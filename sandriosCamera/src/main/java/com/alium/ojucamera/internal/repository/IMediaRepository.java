package com.alium.ojucamera.internal.repository;

import android.content.Context;

import com.alium.ojucamera.internal.ui.model.PickerTile;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by abdulmujibaliu on 9/29/17.
 */

public interface IMediaRepository {

    Observable<List<PickerTile>> getPhotos(Context context);

    Observable<List<PickerTile>> getVideos(Context context);

    Observable<List<PickerTile>> getAllMedia(Context context);
}
