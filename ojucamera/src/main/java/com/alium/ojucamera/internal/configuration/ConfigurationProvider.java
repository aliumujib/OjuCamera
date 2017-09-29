package com.alium.ojucamera.internal.configuration;


public interface ConfigurationProvider {

    int getRequestCode();

    @CameraConfiguration.MediaAction
    int getMediaAction();

    @CameraConfiguration.MediaQuality
    int getMediaQuality();

    int getVideoDuration();

    long getVideoFileSize();

    @CameraConfiguration.SensorPosition
    int getSensorPosition();

    int getDegrees();

    int getMinimumVideoDuration();

    @CameraConfiguration.FlashMode
    int getFlashMode();

}
