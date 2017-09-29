package com.alium.ojucamera.internal.ui.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.alium.ojucamera.R;

/**
 * Created by abdulmujibaliu on 9/29/17.
 */

public class MutipleItemSelectView extends LinearLayout {

    private final View mView;

    public MutipleItemSelectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);


        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mView = inflater.inflate(R.layout.ojulib_multiple_select_view, this, true);


    }


}
