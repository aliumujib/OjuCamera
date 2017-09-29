package com.alium.ojucamera.internal.manager.listener;

import com.alium.ojucamera.internal.utils.Size;

import java.io.File;

public interface CameraVideoListener {
    void onVideoRecordStarted(Size videoSize);

    void onVideoRecordStopped(File videoFile);

    void onVideoRecordError();
}
