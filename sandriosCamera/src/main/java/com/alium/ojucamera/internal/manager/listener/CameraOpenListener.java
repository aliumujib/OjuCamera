package com.alium.ojucamera.internal.manager.listener;

import com.alium.ojucamera.internal.utils.Size;

public interface CameraOpenListener<CameraId, SurfaceListener> {
    void onCameraOpened(CameraId openedCameraId, Size previewSize, SurfaceListener surfaceListener);

    void onCameraOpenError();
}
