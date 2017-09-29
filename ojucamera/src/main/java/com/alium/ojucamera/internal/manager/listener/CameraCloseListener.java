package com.alium.ojucamera.internal.manager.listener;

public interface CameraCloseListener<CameraId> {
    void onCameraClosed(CameraId closedCameraId);
}
