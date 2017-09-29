package com.alium.ojucamera.internal;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.support.annotation.IntRange;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.alium.ojucamera.internal.configuration.CameraConfiguration;
import com.alium.ojucamera.internal.ui.camera.Camera1Activity;
import com.alium.ojucamera.internal.ui.camera2.Camera2Activity;
import com.alium.ojucamera.internal.utils.CameraHelper;

import java.util.ArrayList;


public class OjuCamera {

    private OjuCamera mInstance = null;
    private Activity mActivity;
    private int requestCode;
    private int mediaAction = CameraConfiguration.MEDIA_ACTION_BOTH;
    private int type = 501;
    private boolean enableImageCrop = false;
    private long videoSize = -1;

    public OjuCamera(Activity activity, @IntRange(from = 0) int code) {
        mInstance = this;
        mActivity = activity;
        requestCode = code;
    }

    public OjuCamera setShowPickerType(int type) {
        this.type = type;
        return mInstance;
    }


    public OjuCamera setMediaAction(int mediaAction) {
        this.mediaAction = mediaAction;
        return mInstance;
    }

    public OjuCamera enableImageCropping(boolean enableImageCrop) {
        this.enableImageCrop = enableImageCrop;
        return mInstance;
    }

    public OjuCamera setVideoFileSize(int fileSize) {
        this.videoSize = fileSize;
        return mInstance;
    }

    public void launchCamera() {
        new TedPermission(mActivity)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        launchIntent();
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {

                    }
                })
                .setPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO)
                .check();
    }

    private void launchIntent() {
        if (CameraHelper.hasCamera(mActivity)) {
            Intent cameraIntent;
            if (CameraHelper.hasCamera2(mActivity)) {
                cameraIntent = new Intent(mActivity, Camera2Activity.class);
            } else {
                cameraIntent = new Intent(mActivity, Camera1Activity.class);
            }
            cameraIntent.putExtra(CameraConfiguration.Arguments.REQUEST_CODE, requestCode);
            cameraIntent.putExtra(CameraConfiguration.Arguments.PICKER_TYPE, type);
            cameraIntent.putExtra(CameraConfiguration.Arguments.MEDIA_ACTION, mediaAction);
            cameraIntent.putExtra(CameraConfiguration.Arguments.ENABLE_CROP, enableImageCrop);
            if (videoSize > 0) {
                cameraIntent.putExtra(CameraConfiguration.Arguments.VIDEO_FILE_SIZE, videoSize * 1024 * 1024);
            }
            mActivity.startActivityForResult(cameraIntent, requestCode);
        }
    }
}
