package com.alium.ojucamera.internal.manager;

import android.content.Context;

import com.alium.ojucamera.internal.configuration.ConfigurationProvider;
import com.alium.ojucamera.internal.configuration.CameraConfiguration;
import com.alium.ojucamera.internal.manager.listener.CameraCloseListener;
import com.alium.ojucamera.internal.manager.listener.CameraOpenListener;
import com.alium.ojucamera.internal.manager.listener.CameraPhotoListener;
import com.alium.ojucamera.internal.manager.listener.CameraVideoListener;
import com.alium.ojucamera.internal.utils.Size;

import java.io.File;

public interface CameraManager<CameraId, SurfaceListener> {

    void initializeCameraManager(ConfigurationProvider configurationProvider, Context context);

    void openCamera(CameraId cameraId, CameraOpenListener<CameraId, SurfaceListener> cameraOpenListener);

    void closeCamera(CameraCloseListener<CameraId> cameraCloseListener);

    void takePhoto(File photoFile, CameraPhotoListener cameraPhotoListener);

    void startVideoRecord(File videoFile, CameraVideoListener cameraVideoListener);

    Size getPhotoSizeForQuality(@CameraConfiguration.MediaQuality int mediaQuality);

    void setFlashMode(@CameraConfiguration.FlashMode int flashMode);

    void stopVideoRecord();

    void releaseCameraManager();

    CameraId getCurrentCameraId();

    CameraId getFaceFrontCameraId();

    CameraId getFaceBackCameraId();

    int getNumberOfCameras();

    int getFaceFrontCameraOrientation();

    int getFaceBackCameraOrientation();

    boolean isVideoRecording();
}
