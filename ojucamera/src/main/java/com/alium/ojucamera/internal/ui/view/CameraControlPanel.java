package com.alium.ojucamera.internal.ui.view;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.FileObserver;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alium.ojucamera.R;
import com.alium.ojucamera.internal.configuration.CameraConfiguration;
import com.alium.ojucamera.internal.manager.listener.GalleryClickListener;
import com.alium.ojucamera.internal.repository.MediaRepository;
import com.alium.ojucamera.internal.ui.model.PickerTile;
import com.alium.ojucamera.internal.ui.view.adapters.ImageGalleryAdapter;
import com.alium.ojucamera.internal.ui.view.control.BottomSheetBehaviorRecyclerManager;
import com.alium.ojucamera.internal.ui.view.control.BottomSheetBehaviorv2;
import com.alium.ojucamera.internal.utils.DateTimeUtils;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class CameraControlPanel extends RelativeLayout
        implements RecordButton.RecordButtonListener,
        MediaActionSwitchView.OnMediaActionStateChangeListener {

    private Context context;

    private CameraSwitchView cameraSwitchView;
    private RecordButton recordButton;
    private MediaActionSwitchView mediaActionSwitchView;
    private FlashSwitchView flashSwitchView;
    private TextView recordDurationText;
    private TextView recordSizeText;
    private RelativeLayout topControls;
    private ImageButton settingsButton;
    private ImageButton pickFromGalleryButton;
    private RecyclerView anchoredRecyclerView;
    private MutipleItemSelectView multiSelectPicker;

    private ImageGalleryAdapter imageGalleryAdapter;
    private RecordButton.RecordButtonListener recordButtonListener;
    private MediaActionSwitchView.OnMediaActionStateChangeListener onMediaActionStateChangeListener;
    private CameraSwitchView.OnCameraTypeChangeListener onCameraTypeChangeListener;
    private FlashSwitchView.FlashModeSwitchListener flashModeSwitchListener;
    private SettingsClickListener settingsClickListener;
    private PickerItemClickListener pickerItemClickListener;

    private BottomSheetBehaviorv2<View> mBehavior;
    private View mBottomSheet;
    private CoordinatorLayout mCordinatorLayout;

    private TimerTaskBase countDownTimer;
    private long maxVideoFileSize = 0;
    private String mediaFilePath;
    private boolean hasFlash = false;
    private
    @MediaActionSwitchView.MediaActionState
    int mediaActionState;
    private int mediaAction;
    private boolean showImageCrop = false;
    private FileObserver fileObserver;
    private GalleryClickListener galleryClickListener;

    public CameraControlPanel(Context context) {
        this(context, null);
    }

    public CameraControlPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void prepareforAnimation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(this);
        }
    }


    private void init() {
        hasFlash = context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        LayoutInflater.from(context).inflate(R.layout.ojulib_camera_control_panel_layout, this);
        setBackgroundColor(Color.TRANSPARENT);
        settingsButton = (ImageButton) findViewById(R.id.settings_view);
        pickFromGalleryButton = (ImageButton) findViewById(R.id.gallery_pick_button);
        cameraSwitchView = (CameraSwitchView) findViewById(R.id.front_back_camera_switcher);
        mediaActionSwitchView = (MediaActionSwitchView) findViewById(R.id.photo_video_camera_switcher);
        recordButton = (RecordButton) findViewById(R.id.record_button);
        flashSwitchView = (FlashSwitchView) findViewById(R.id.flash_switch_view);
        recordDurationText = (TextView) findViewById(R.id.record_duration_text);
        recordSizeText = (TextView) findViewById(R.id.record_size_mb_text);
        anchoredRecyclerView = (RecyclerView) findViewById(R.id.anchored_recycler_view);
        topControls = (RelativeLayout) findViewById(R.id.top_controls);
        multiSelectPicker = (MutipleItemSelectView) findViewById(R.id.multiselect_picker_view);
        anchoredRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        cameraSwitchView.setOnCameraTypeChangeListener(onCameraTypeChangeListener);
        mediaActionSwitchView.setOnMediaActionStateChangeListener(this);

        setOnCameraTypeChangeListener(onCameraTypeChangeListener);
        setOnMediaActionStateChangeListener(onMediaActionStateChangeListener);
        setFlashModeSwitchListener(flashModeSwitchListener);
        setRecordButtonListener(recordButtonListener);

        settingsButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_settings_white_24dp));
        settingsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (settingsClickListener != null) settingsClickListener.onSettingsClick();
            }
        });

        pickFromGalleryButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryClickListener.lauchGalleryBtnClicked();
            }
        });

        if (hasFlash)
            flashSwitchView.setVisibility(VISIBLE);
        else flashSwitchView.setVisibility(GONE);

        countDownTimer = new TimerTask(recordDurationText);

        mCordinatorLayout = (CoordinatorLayout) this.findViewById(R.id.coordinatorLayout);
        mCordinatorLayout.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);

        mBottomSheet = this.findViewById(R.id.dragView);
        mBehavior = BottomSheetBehaviorv2.from(mBottomSheet);

        //helper to rule scrolls
        BottomSheetBehaviorRecyclerManager manager = new BottomSheetBehaviorRecyclerManager(mCordinatorLayout, mBehavior, mBottomSheet);
        manager.addControl(this.getAnchoredRecyclerView());
        manager.addControl(this.getExpandedRecyclerView());
        manager.create();
    }

    public BottomSheetBehaviorv2<View> getCordinatorBehavior() {
        return mBehavior;
    }

    public void animateTopControls(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(topControls);
        }
    }

    public void hideRecyclerView() {
        anchoredRecyclerView.setVisibility(GONE);
    }

    public void hideMultipleItemSelectView() {
        multiSelectPicker.setVisibility(GONE);
    }

    public void fadeViewsWithOffset(float slideOffset) {
        anchoredRecyclerView.animate().alpha(1.0f - slideOffset);
        if (slideOffset <= 0.2) {
            multiSelectPicker.animate().alpha(0);
        } else {
            multiSelectPicker.animate().alpha(slideOffset);
        }
    }

    public void showRecyclerView() {
        anchoredRecyclerView.setVisibility(VISIBLE);
    }

    public RecyclerView getAnchoredRecyclerView() {
        return anchoredRecyclerView;
    }

    public RecyclerView getExpandedRecyclerView() {
        return multiSelectPicker.getmExpandedRecyclerView();
    }


    public void showMultipleItemSelectView() {
        multiSelectPicker.setVisibility(VISIBLE);
    }

    public void postInit(int mediatype) {
        imageGalleryAdapter = new ImageGalleryAdapter(context);
        anchoredRecyclerView.setAdapter(imageGalleryAdapter);

        Consumer<List<PickerTile>> successConsumer = new Consumer<List<PickerTile>>() {
            @Override
            public void accept(@NonNull List<PickerTile> pickerTiles) throws Exception {
                imageGalleryAdapter.addAll(pickerTiles);
            }
        };
        Consumer<Throwable> failureConsumer = new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                throwable.printStackTrace();
            }
        };

        if (mediatype == CameraConfiguration.PHOTO_AND_VIDEO) {
            MediaRepository.sharedInstance.getAllMedia(context).subscribe(successConsumer, failureConsumer);
        } else if (mediatype == CameraConfiguration.PHOTO) {
            MediaRepository.sharedInstance.getPhotos(context).subscribe(successConsumer, failureConsumer);
        } else if (mediatype == CameraConfiguration.VIDEO) {
            MediaRepository.sharedInstance.getVideos(context).subscribe(successConsumer, failureConsumer);
        }

        imageGalleryAdapter.setOnItemClickListener(new ImageGalleryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                pickerItemClickListener.onItemClick(imageGalleryAdapter.getItem(position).getImageUri());
            }
        });
    }

    public void lockControls() {
        cameraSwitchView.setEnabled(false);
        recordButton.setEnabled(false);
        settingsButton.setEnabled(false);
        flashSwitchView.setEnabled(false);
    }

    public void unLockControls() {
        cameraSwitchView.setEnabled(true);
        recordButton.setEnabled(true);
        settingsButton.setEnabled(true);
        flashSwitchView.setEnabled(true);
    }

    public void setup(int mediaAction) {
        this.mediaAction = mediaAction;
        if (CameraConfiguration.MEDIA_ACTION_VIDEO == mediaAction) {
            recordButton.setup(mediaAction, this);
            flashSwitchView.setVisibility(GONE);
        } else {
            recordButton.setup(CameraConfiguration.MEDIA_ACTION_PHOTO, this);
        }

        if (CameraConfiguration.MEDIA_ACTION_BOTH != mediaAction) {
            mediaActionSwitchView.setVisibility(GONE);
        } else mediaActionSwitchView.setVisibility(VISIBLE);
    }

    public void setMediaFilePath(final File mediaFile) {
        this.mediaFilePath = mediaFile.toString();
    }

    public void setMaxVideoFileSize(long maxVideoFileSize) {
        this.maxVideoFileSize = maxVideoFileSize;
    }

    public void setMaxVideoDuration(int maxVideoDurationInMillis) {
        if (maxVideoDurationInMillis > 0)
            countDownTimer = new CountdownTask(recordDurationText, maxVideoDurationInMillis);
        else countDownTimer = new TimerTask(recordDurationText);
    }

    public void setFlasMode(@FlashSwitchView.FlashMode int flashMode) {
        flashSwitchView.setFlashMode(flashMode);
    }

    public void setMediaActionState(@MediaActionSwitchView.MediaActionState int actionState) {
        if (mediaActionState == actionState) return;
        if (MediaActionSwitchView.ACTION_PHOTO == actionState) {
            recordButton.setMediaAction(CameraConfiguration.MEDIA_ACTION_PHOTO);
            if (hasFlash)
                flashSwitchView.setVisibility(VISIBLE);
        } else {
            recordButton.setMediaAction(CameraConfiguration.MEDIA_ACTION_VIDEO);
            flashSwitchView.setVisibility(GONE);
        }
        mediaActionState = actionState;
        mediaActionSwitchView.setMediaActionState(actionState);
    }

    public void setRecordButtonListener(RecordButton.RecordButtonListener recordButtonListener) {
        this.recordButtonListener = recordButtonListener;
    }

    public void rotateControls(int rotation) {
        if (Build.VERSION.SDK_INT > 10) {
            cameraSwitchView.setRotation(rotation);
            mediaActionSwitchView.setRotation(rotation);
            flashSwitchView.setRotation(rotation);
            recordDurationText.setRotation(rotation);
            recordSizeText.setRotation(rotation);
        }
    }

    public void setOnMediaActionStateChangeListener(MediaActionSwitchView.OnMediaActionStateChangeListener onMediaActionStateChangeListener) {
        this.onMediaActionStateChangeListener = onMediaActionStateChangeListener;
    }

    public void setOnCameraTypeChangeListener(CameraSwitchView.OnCameraTypeChangeListener onCameraTypeChangeListener) {
        this.onCameraTypeChangeListener = onCameraTypeChangeListener;
        if (cameraSwitchView != null)
            cameraSwitchView.setOnCameraTypeChangeListener(this.onCameraTypeChangeListener);
    }

    public void setFlashModeSwitchListener(FlashSwitchView.FlashModeSwitchListener flashModeSwitchListener) {
        this.flashModeSwitchListener = flashModeSwitchListener;
        if (flashSwitchView != null)
            flashSwitchView.setFlashSwitchListener(this.flashModeSwitchListener);
    }

    public void setSettingsClickListener(SettingsClickListener settingsClickListener) {
        this.settingsClickListener = settingsClickListener;
    }

    public void setPickerItemClickListener(PickerItemClickListener pickerItemClickListener) {
        this.pickerItemClickListener = pickerItemClickListener;
        this.multiSelectPicker.setPickerItemClickListener(pickerItemClickListener);
    }

    @Override
    public void onTakePhotoButtonPressed() {
        if (recordButtonListener != null)
            recordButtonListener.onTakePhotoButtonPressed();
    }

    public void onStartVideoRecord(final File mediaFile) {
        setMediaFilePath(mediaFile);
        if (maxVideoFileSize > 0) {
            recordSizeText.setText("1Mb" + " / " + maxVideoFileSize / (1024 * 1024) + "Mb");
            recordSizeText.setVisibility(VISIBLE);
            try {
                fileObserver = new FileObserver(this.mediaFilePath) {
                    private long lastUpdateSize = 0;

                    @Override
                    public void onEvent(int event, String path) {
                        final long fileSize = mediaFile.length() / (1024 * 1024);
                        if ((fileSize - lastUpdateSize) >= 1) {
                            lastUpdateSize = fileSize;
                            recordSizeText.post(new Runnable() {
                                @Override
                                public void run() {
                                    recordSizeText.setText(fileSize + "Mb" + " / " + maxVideoFileSize / (1024 * 1024) + "Mb");
                                }
                            });
                        }
                    }
                };
                fileObserver.startWatching();
            } catch (Exception e) {
                Log.e("FileObserver", "setMediaFilePath: ", e);
            }
        }
        countDownTimer.start();

    }

    public void allowRecord(boolean isAllowed) {
        recordButton.setEnabled(isAllowed);
    }

    public void showPicker(boolean isShown) {
        anchoredRecyclerView.setVisibility(isShown ? VISIBLE : GONE);
    }

    public boolean showCrop() {
        return showImageCrop;
    }

    public void shouldShowCrop(boolean showImageCrop) {
        this.showImageCrop = showImageCrop;
    }

    public void allowCameraSwitching(boolean isAllowed) {
        cameraSwitchView.setVisibility(isAllowed ? VISIBLE : GONE);
    }

    public void onStopVideoRecord() {
        if (fileObserver != null)
            fileObserver.stopWatching();
        countDownTimer.stop();
        anchoredRecyclerView.setVisibility(VISIBLE);
        recordSizeText.setVisibility(GONE);
        cameraSwitchView.setVisibility(View.VISIBLE);
        settingsButton.setVisibility(VISIBLE);

        if (CameraConfiguration.MEDIA_ACTION_BOTH != mediaAction) {
            mediaActionSwitchView.setVisibility(GONE);
        } else mediaActionSwitchView.setVisibility(VISIBLE);
        recordButton.setRecordState(RecordButton.READY_FOR_RECORD_STATE);
    }

    @Override
    public void onStartRecordingButtonPressed() {
        cameraSwitchView.setVisibility(View.GONE);
        mediaActionSwitchView.setVisibility(GONE);
        settingsButton.setVisibility(GONE);
        anchoredRecyclerView.setVisibility(GONE);

        if (recordButtonListener != null)
            recordButtonListener.onStartRecordingButtonPressed();
    }

    @Override
    public void onStopRecordingButtonPressed() {
        onStopVideoRecord();
        if (recordButtonListener != null)
            recordButtonListener.onStopRecordingButtonPressed();
    }

    @Override
    public void onMediaActionChanged(int mediaActionState) {
        setMediaActionState(mediaActionState);
        if (onMediaActionStateChangeListener != null)
            onMediaActionStateChangeListener.onMediaActionChanged(this.mediaActionState);
    }

    public void setMediaType(int type) {
    }

    public void setGalleryClickListener(GalleryClickListener galleryClickListener) {
        this.galleryClickListener = galleryClickListener;
    }

    public interface SettingsClickListener {
        void onSettingsClick();
    }

    public interface PickerItemClickListener {
        void onItemClick(Uri filePath);
    }

    abstract class TimerTaskBase {
        protected Handler handler = new Handler(Looper.getMainLooper());
        protected TextView timerView;
        protected boolean alive = false;
        protected long recordingTimeSeconds = 0;
        protected long recordingTimeMinutes = 0;

        public TimerTaskBase(TextView timerView) {
            this.timerView = timerView;
        }

        abstract void stop();

        abstract void start();
    }

    private class CountdownTask extends TimerTaskBase implements Runnable {

        private int maxDurationMilliseconds = 0;

        public CountdownTask(TextView timerView, int maxDurationMilliseconds) {
            super(timerView);
            this.maxDurationMilliseconds = maxDurationMilliseconds;
        }

        @Override
        public void run() {

            recordingTimeSeconds--;

            int millis = (int) recordingTimeSeconds * 1000;

            timerView.setText(
                    String.format("%02d:%02d",
                            TimeUnit.MILLISECONDS.toMinutes(millis),
                            TimeUnit.MILLISECONDS.toSeconds(millis) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
                    ));

            if (recordingTimeSeconds < 10) {
                timerView.setTextColor(Color.RED);
            }

            if (alive && recordingTimeSeconds > 0) handler.postDelayed(this, DateTimeUtils.SECOND);
        }

        @Override
        void stop() {
            timerView.setVisibility(View.INVISIBLE);
            alive = false;
        }

        @Override
        void start() {
            alive = true;
            recordingTimeSeconds = maxDurationMilliseconds / 1000;
            timerView.setTextColor(Color.WHITE);
            timerView.setText(
                    String.format("%02d:%02d",
                            TimeUnit.MILLISECONDS.toMinutes(maxDurationMilliseconds),
                            TimeUnit.MILLISECONDS.toSeconds(maxDurationMilliseconds) -
                                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(maxDurationMilliseconds))
                    ));
            timerView.setVisibility(View.VISIBLE);
            handler.postDelayed(this, DateTimeUtils.SECOND);
        }
    }

    private class TimerTask extends TimerTaskBase implements Runnable {

        public TimerTask(TextView timerView) {
            super(timerView);
        }

        @Override
        public void run() {
            recordingTimeSeconds++;

            if (recordingTimeSeconds == 60) {
                recordingTimeSeconds = 0;
                recordingTimeMinutes++;
            }
            timerView.setText(
                    String.format("%02d:%02d", recordingTimeMinutes, recordingTimeSeconds));
            if (alive) handler.postDelayed(this, DateTimeUtils.SECOND);
        }

        public void start() {
            alive = true;
            recordingTimeMinutes = 0;
            recordingTimeSeconds = 0;
            timerView.setText(
                    String.format("%02d:%02d", recordingTimeMinutes, recordingTimeSeconds));
            timerView.setVisibility(View.VISIBLE);
            handler.postDelayed(this, DateTimeUtils.SECOND);
        }

        public void stop() {
            timerView.setVisibility(View.INVISIBLE);
            alive = false;
        }
    }

}
