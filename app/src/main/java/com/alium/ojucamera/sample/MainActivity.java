package com.alium.ojucamera.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alium.ojucamera.internal.OjuCamera;
import com.alium.ojucamera.internal.configuration.CameraConfiguration;

/**
 * Created by Arpit Gandhi on 11/8/16.
 */

public class MainActivity extends AppCompatActivity {

    private static final int CAPTURE_MEDIA = 368;

    private Activity activity;
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.withPicker:
                    new OjuCamera(activity, CAPTURE_MEDIA)
                            //.setShowPickerType(CameraConfiguration.VIDEO)
                            .setVideoFileSize(20)
                            .setMediaAction(CameraConfiguration.MEDIA_ACTION_BOTH)
                            .enableImageCropping(true)
                            .launchCamera();
                    break;
                case R.id.withoutPicker:
                    new OjuCamera(activity, CAPTURE_MEDIA)
                            .setMediaAction(CameraConfiguration.MEDIA_ACTION_PHOTO)
                            .enableImageCropping(false)
                            .launchCamera();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);
        activity = this;

        findViewById(R.id.withPicker).setOnClickListener(onClickListener);
        findViewById(R.id.withoutPicker).setOnClickListener(onClickListener);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAPTURE_MEDIA && resultCode == RESULT_OK) {
            Log.e("File", "" + data.getStringExtra(CameraConfiguration.Arguments.FILE_PATH));
            Toast.makeText(this, "Media captured.", Toast.LENGTH_SHORT).show();
        }
    }
}
