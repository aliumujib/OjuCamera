package com.alium.ojucamera.internal.ui;

import android.app.Activity;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.alium.ojucamera.R;
import com.alium.ojucamera.internal.configuration.ConfigurationProvider;
import com.alium.ojucamera.internal.configuration.CameraConfiguration;
import com.alium.ojucamera.internal.controller.CameraController;
import com.alium.ojucamera.internal.controller.view.CameraView;
import com.alium.ojucamera.internal.ui.view.AspectFrameLayout;
import com.alium.ojucamera.internal.utils.Size;
import com.alium.ojucamera.internal.utils.Utils;

abstract public class OjuCameraActivity<CameraId> extends Activity
        implements ConfigurationProvider, CameraView, SensorEventListener {

    protected AspectFrameLayout previewContainer;
    protected ViewGroup userContainer;
    @CameraConfiguration.SensorPosition
    protected int sensorPosition = CameraConfiguration.SENSOR_POSITION_UNSPECIFIED;
    @CameraConfiguration.DeviceDefaultOrientation
    protected int deviceDefaultOrientation;
    private SensorManager sensorManager = null;
    private CameraController<CameraId> cameraController;
    private int degrees = -1;

    // This snippet hides the system bars.
    protected void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        View decorView = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        }
    }

    // This snippet shows the system bars. It does this by removing all the flags
// except for the ones that make the content appear under the system bars.
    protected void showSystemUI() {
        View decorView = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    protected int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cameraController = createCameraController(this, this);
        cameraController.onCreate(savedInstanceState);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        int defaultOrientation = Utils.getDeviceDefaultOrientation(this);

        if (defaultOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            deviceDefaultOrientation = CameraConfiguration.ORIENTATION_LANDSCAPE;
        } else if (defaultOrientation == Configuration.ORIENTATION_PORTRAIT) {
            deviceDefaultOrientation = CameraConfiguration.ORIENTATION_PORTRAIT;
        }

        hideSystemUI();

        setContentView(R.layout.generic_camera_layout);

        previewContainer = (AspectFrameLayout) findViewById(R.id.previewContainer);
        userContainer = (ViewGroup) findViewById(R.id.userContainer);

        onProcessBundle(savedInstanceState);
        setUserContent();


    }

    public void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    protected void onProcessBundle(Bundle savedInstanceState) {

    }

    @Override
    protected void onResume() {
        super.onResume();

        cameraController.onResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();

        cameraController.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        cameraController.onDestroy();
    }

    public final CameraController<CameraId> getCameraController() {
        return cameraController;
    }

    public abstract CameraController<CameraId> createCameraController(CameraView cameraView, ConfigurationProvider configurationProvider);

    private void setUserContent() {
        userContainer.removeAllViews();
        userContainer.addView(getUserContentView(LayoutInflater.from(this), userContainer));
    }

    public final void setCameraPreview(View preview, Size previewSize) {
        onCameraControllerReady();

        if (previewContainer == null || preview == null) return;
        previewContainer.removeAllViews();
        previewContainer.addView(preview);

        //REMOVES THE WEIRD PADDING THAT I DON'T LIKE FROM THE PREVIEW EDGES
        //previewContainer.setAspectRatio(previewSize.getHeight() / (double) previewSize.getWidth());
    }

    public final void clearCameraPreview() {
        if (previewContainer != null)
            previewContainer.removeAllViews();
    }

    abstract View getUserContentView(LayoutInflater layoutInflater, ViewGroup parent);

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        synchronized (this) {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                if (sensorEvent.values[0] < 4 && sensorEvent.values[0] > -4) {
                    if (sensorEvent.values[1] > 0) {
                        // UP
                        sensorPosition = CameraConfiguration.SENSOR_POSITION_UP;
                        degrees = deviceDefaultOrientation == CameraConfiguration.ORIENTATION_PORTRAIT ? 0 : 90;
                    } else if (sensorEvent.values[1] < 0) {
                        // UP SIDE DOWN
                        sensorPosition = CameraConfiguration.SENSOR_POSITION_UP_SIDE_DOWN;
                        degrees = deviceDefaultOrientation == CameraConfiguration.ORIENTATION_PORTRAIT ? 180 : 270;
                    }
                } else if (sensorEvent.values[1] < 4 && sensorEvent.values[1] > -4) {
                    if (sensorEvent.values[0] > 0) {
                        // LEFT
                        sensorPosition = CameraConfiguration.SENSOR_POSITION_LEFT;
                        degrees = deviceDefaultOrientation == CameraConfiguration.ORIENTATION_PORTRAIT ? 90 : 180;
                    } else if (sensorEvent.values[0] < 0) {
                        // RIGHT
                        sensorPosition = CameraConfiguration.SENSOR_POSITION_RIGHT;
                        degrees = deviceDefaultOrientation == CameraConfiguration.ORIENTATION_PORTRAIT ? 270 : 0;
                    }
                }
                onScreenRotation(degrees);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public final int getSensorPosition() {
        return sensorPosition;
    }

    @Override
    public final int getDegrees() {
        return degrees;
    }

    protected abstract void onScreenRotation(int degrees);

    protected void onCameraControllerReady() {
    }
}
