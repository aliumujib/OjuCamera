package com.alium.ojucamera.internal.controller;

import android.os.Bundle;

import com.alium.ojucamera.internal.configuration.CameraConfiguration;
import com.alium.ojucamera.internal.manager.CameraManager;

import java.io.File;

public interface CameraController<CameraId> {

    void onCreate(Bundle savedInstanceState);

    void onResume();

    void onPause();

    void onDestroy();

    void takePhoto();

    void startVideoRecord();

    void stopVideoRecord();

    boolean isVideoRecording();

    void switchCamera(@CameraConfiguration.CameraFace int cameraFace);

    void switchQuality();

    int getNumberOfCameras();

    @CameraConfiguration.MediaAction
    int getMediaAction();

    CameraId getCurrentCameraId();

    File getOutputFile();

    CameraManager getCameraManager();

    void setFlashMode(@CameraConfiguration.FlashMode int flashMode);

}
