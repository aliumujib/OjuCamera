package com.alium.ojucamera.internal.ui.camera2;

import android.annotation.TargetApi;
import android.media.CamcorderProfile;
import android.os.Build;

import com.alium.ojucamera.internal.configuration.CameraConfiguration;
import com.alium.ojucamera.internal.configuration.ConfigurationProvider;
import com.alium.ojucamera.internal.controller.CameraController;
import com.alium.ojucamera.internal.controller.impl.Camera2Controller;
import com.alium.ojucamera.internal.controller.impl.Camera2ControllerAPI24;
import com.alium.ojucamera.internal.controller.view.CameraView;
import com.alium.ojucamera.internal.ui.BaseOjuCamActivity;
import com.alium.ojucamera.internal.ui.model.PhotoQualityOption;
import com.alium.ojucamera.internal.ui.model.VideoQualityOption;
import com.alium.ojucamera.internal.utils.CameraHelper;

import java.util.ArrayList;
import java.util.List;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class Camera2Activity extends BaseOjuCamActivity<String> {

    @Override
    public CameraController<String> createCameraController(CameraView cameraView, ConfigurationProvider configurationProvider) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            return new Camera2ControllerAPI24(cameraView, configurationProvider);
        else
            return new Camera2Controller(cameraView, configurationProvider);
    }

    @Override
    protected CharSequence[] getVideoQualityOptions() {
        List<CharSequence> videoQualities = new ArrayList<>();

        if (getMinimumVideoDuration() > 0)
            videoQualities.add(new VideoQualityOption(CameraConfiguration.MEDIA_QUALITY_AUTO, CameraHelper.getCamcorderProfile(CameraConfiguration.MEDIA_QUALITY_AUTO, getCameraController().getCurrentCameraId()), getMinimumVideoDuration()));


        CamcorderProfile camcorderProfile = CameraHelper.getCamcorderProfile(CameraConfiguration.MEDIA_QUALITY_HIGH, getCameraController().getCurrentCameraId());
        double videoDuration = CameraHelper.calculateApproximateVideoDuration(camcorderProfile, getVideoFileSize());
        videoQualities.add(new VideoQualityOption(CameraConfiguration.MEDIA_QUALITY_HIGH, camcorderProfile, videoDuration));

        camcorderProfile = CameraHelper.getCamcorderProfile(CameraConfiguration.MEDIA_QUALITY_MEDIUM, getCameraController().getCurrentCameraId());
        videoDuration = CameraHelper.calculateApproximateVideoDuration(camcorderProfile, getVideoFileSize());
        videoQualities.add(new VideoQualityOption(CameraConfiguration.MEDIA_QUALITY_MEDIUM, camcorderProfile, videoDuration));

        camcorderProfile = CameraHelper.getCamcorderProfile(CameraConfiguration.MEDIA_QUALITY_LOW, getCameraController().getCurrentCameraId());
        videoDuration = CameraHelper.calculateApproximateVideoDuration(camcorderProfile, getVideoFileSize());
        videoQualities.add(new VideoQualityOption(CameraConfiguration.MEDIA_QUALITY_LOW, camcorderProfile, videoDuration));

        CharSequence[] array = new CharSequence[videoQualities.size()];
        videoQualities.toArray(array);

        return array;
    }

    @Override
    protected CharSequence[] getPhotoQualityOptions() {
        List<CharSequence> photoQualities = new ArrayList<>();
        photoQualities.add(new PhotoQualityOption(CameraConfiguration.MEDIA_QUALITY_HIGHEST, getCameraController().getCameraManager().getPhotoSizeForQuality(CameraConfiguration.MEDIA_QUALITY_HIGHEST)));
        photoQualities.add(new PhotoQualityOption(CameraConfiguration.MEDIA_QUALITY_HIGH, getCameraController().getCameraManager().getPhotoSizeForQuality(CameraConfiguration.MEDIA_QUALITY_HIGH)));
        photoQualities.add(new PhotoQualityOption(CameraConfiguration.MEDIA_QUALITY_MEDIUM, getCameraController().getCameraManager().getPhotoSizeForQuality(CameraConfiguration.MEDIA_QUALITY_MEDIUM)));
        photoQualities.add(new PhotoQualityOption(CameraConfiguration.MEDIA_QUALITY_LOWEST, getCameraController().getCameraManager().getPhotoSizeForQuality(CameraConfiguration.MEDIA_QUALITY_LOWEST)));

        CharSequence[] array = new CharSequence[photoQualities.size()];
        photoQualities.toArray(array);

        return array;
    }

}
