package com.alium.ojucamera.internal.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;

import com.alium.ojucamera.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class GalleryPanelButtonView extends ImageButton {

    public static final int ANCHORED = 0;
    public static final int COLLAPSED = 1;
    public static final int EXPANDED = 2;
    @PanelState
    private int currentMode = COLLAPSED;
    private GalleryPanelModeSwitchListener panelModeSwitchListener;
    private Drawable anchoredDrawable;
    private Drawable collapsedDrawable;
    private Drawable expandedDrawable;

    public GalleryPanelButtonView(@NonNull Context context) {
        this(context, null);
    }

    public GalleryPanelButtonView(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        anchoredDrawable = ContextCompat.getDrawable(context, R.drawable.ic_minus_white_24dp);
        collapsedDrawable = ContextCompat.getDrawable(context, R.drawable.ic_arrow_collapse_up_white_24dp);
        expandedDrawable = ContextCompat.getDrawable(context, R.drawable.ic_arrow_collapse_down_white_24dp);
        init();
    }

    private void init() {
        setBackgroundColor(Color.TRANSPARENT);
        //setOnClickListener(new GalleryPanelButtonClickListener());
        setIcon();
    }

    private void setIcon() {
        if (COLLAPSED == currentMode) {
            setImageDrawable(collapsedDrawable);
        } else if (ANCHORED == currentMode) {
            setImageDrawable(anchoredDrawable);
        } else setImageDrawable(expandedDrawable);

    }

    public void setPanelState(@PanelState int mode) {
        this.currentMode = mode;
        setIcon();
    }

    @PanelState
    public int getCurrentPanelState() {
        return currentMode;
    }

    public void setStateChangeListener(@NonNull GalleryPanelModeSwitchListener switchListener) {
        this.panelModeSwitchListener = switchListener;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (Build.VERSION.SDK_INT > 10) {
            if (enabled) {
                setAlpha(1f);
            } else {
                setAlpha(0.5f);
            }
        }
    }

    @IntDef({ANCHORED, COLLAPSED, EXPANDED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface PanelState {
    }

    public interface GalleryPanelModeSwitchListener {
        void onStateChanged(@PanelState int mode);
    }

    private class GalleryPanelButtonClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            if (EXPANDED == currentMode) {
                currentMode = COLLAPSED;
            } else if (COLLAPSED == currentMode) {
                currentMode = ANCHORED;
            } else if (ANCHORED == currentMode) {
                currentMode = EXPANDED;
            }
            setIcon();
            if (panelModeSwitchListener != null) {
                panelModeSwitchListener.onStateChanged(currentMode);
            }
        }
    }
}
