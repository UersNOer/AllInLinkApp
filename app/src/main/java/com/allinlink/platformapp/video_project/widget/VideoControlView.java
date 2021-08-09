package com.allinlink.platformapp.video_project.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.allinlink.platformapp.R;

public class VideoControlView extends LinearLayout {
    public VideoControlView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.video_control, this);
    }

    public VideoControlView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        LayoutInflater.from(context).inflate(R.layout.video_control, this);
    }

    public VideoControlView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.video_control, this);

    }
}
