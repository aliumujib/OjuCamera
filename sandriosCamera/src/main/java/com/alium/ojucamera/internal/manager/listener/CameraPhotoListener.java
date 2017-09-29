package com.alium.ojucamera.internal.manager.listener;

import java.io.File;

public interface CameraPhotoListener {
    void onPhotoTaken(File photoFile);

    void onPhotoTakeError();
}
