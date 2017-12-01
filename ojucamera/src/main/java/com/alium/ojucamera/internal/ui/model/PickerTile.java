package com.alium.ojucamera.internal.ui.model;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import org.joda.time.LocalDateTime;

/**
 * Created by abdulmujibaliu on 9/29/17.
 */
public class PickerTile {

    protected final Uri imageUri;
    private LocalDateTime dateCreated;
    private boolean isVideo, isSelected;
    private String TAG = getClass().getSimpleName();

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean isVideo() {
        return isVideo;
    }

    public PickerTile(@NonNull Uri imageUri, LocalDateTime dateCreated, boolean isVideo) {
        this.imageUri = imageUri;
        this.dateCreated = dateCreated;
        Log.d(TAG, dateCreated.toString());
        this.isVideo = isVideo;
    }

    @Nullable
    public Uri getImageUri() {
        return imageUri;
    }

    @Override
    public String toString() {
        return "ImageTile: " + imageUri;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }
}
