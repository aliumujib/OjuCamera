package com.alium.ojucamera.internal.configuration;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class CameraConfiguration {

    public static final int MEDIA_QUALITY_AUTO = 10;
    public static final int MEDIA_QUALITY_LOWEST = 15;
    public static final int MEDIA_QUALITY_LOW = 11;
    public static final int MEDIA_QUALITY_MEDIUM = 12;
    public static final int MEDIA_QUALITY_HIGH = 13;
    public static final int MEDIA_QUALITY_HIGHEST = 14;

    public static final int MEDIA_ACTION_VIDEO = 100;
    public static final int MEDIA_ACTION_PHOTO = 101;
    public static final int MEDIA_ACTION_BOTH = 102;

    public static final int CAMERA_FACE_FRONT = 0x6;
    public static final int CAMERA_FACE_REAR = 0x7;

    public static final int SENSOR_POSITION_UP = 90;
    public static final int SENSOR_POSITION_UP_SIDE_DOWN = 270;
    public static final int SENSOR_POSITION_LEFT = 0;
    public static final int SENSOR_POSITION_RIGHT = 180;
    public static final int SENSOR_POSITION_UNSPECIFIED = -1;

    public static final int DISPLAY_ROTATION_0 = 0;
    public static final int DISPLAY_ROTATION_90 = 90;
    public static final int DISPLAY_ROTATION_180 = 180;
    public static final int DISPLAY_ROTATION_270 = 270;

    public static final int ORIENTATION_PORTRAIT = 0x111;
    public static final int ORIENTATION_LANDSCAPE = 0x222;

    public static final int FLASH_MODE_ON = 1;
    public static final int FLASH_MODE_OFF = 2;
    public static final int FLASH_MODE_AUTO = 3;

    public static final int VIDEO = 501;
    public static final int PHOTO = 502;
    public static final int PHOTO_AND_VIDEO = 503;

    public interface Arguments {
        String REQUEST_CODE = "com.alium.ojucamera.request_code";
        String MEDIA_ACTION = "com.alium.ojucamera.media_action";
        String MEDIA_QUALITY = "com.alium.ojucamera.camera_media_quality";
        String VIDEO_DURATION = "com.alium.ojucamera.video_duration";
        String MINIMUM_VIDEO_DURATION = "com.alium.ojucamera.minimum.video_duration";
        String VIDEO_FILE_SIZE = "com.alium.ojucamera.camera_video_file_size";
        String FILE_PATH = "com.alium.ojucamera.camera_video_file_path";
        String FLASH_MODE = "com.alium.ojucamera.camera_flash_mode";
        String SHOW_PICKER = "com.alium.ojucamera.show_picker";
        String PICKER_TYPE = "com.alium.ojucamera.picker_type";
        String ENABLE_CROP = "com.alium.ojucamera.enable_crop";
    }

    @IntDef({MEDIA_QUALITY_AUTO, MEDIA_QUALITY_LOWEST, MEDIA_QUALITY_LOW, MEDIA_QUALITY_MEDIUM, MEDIA_QUALITY_HIGH, MEDIA_QUALITY_HIGHEST})
    @Retention(RetentionPolicy.SOURCE)
    public @interface MediaQuality {
    }

    @IntDef({MEDIA_ACTION_VIDEO, MEDIA_ACTION_PHOTO, MEDIA_ACTION_BOTH})
    @Retention(RetentionPolicy.SOURCE)
    public @interface MediaAction {
    }

    @IntDef({CAMERA_FACE_FRONT, CAMERA_FACE_REAR})
    @Retention(RetentionPolicy.SOURCE)
    public @interface CameraFace {
    }

    @IntDef({SENSOR_POSITION_UP, SENSOR_POSITION_UP_SIDE_DOWN, SENSOR_POSITION_LEFT, SENSOR_POSITION_RIGHT, SENSOR_POSITION_UNSPECIFIED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SensorPosition {
    }

    @IntDef({DISPLAY_ROTATION_0, DISPLAY_ROTATION_90, DISPLAY_ROTATION_180, DISPLAY_ROTATION_270})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DisplayRotation {
    }

    @IntDef({ORIENTATION_PORTRAIT, ORIENTATION_LANDSCAPE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DeviceDefaultOrientation {
    }

    @IntDef({FLASH_MODE_ON, FLASH_MODE_OFF, FLASH_MODE_AUTO})
    @Retention(RetentionPolicy.SOURCE)
    public @interface FlashMode {
    }
}
