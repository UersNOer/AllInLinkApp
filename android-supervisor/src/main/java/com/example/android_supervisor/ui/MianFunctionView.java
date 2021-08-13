package com.example.android_supervisor.ui;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * @author yj
 */
public class MianFunctionView extends FrameLayout {

    public MianFunctionView(@NonNull Context context) {
        this(context,null);
    }

    public MianFunctionView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context,null,0);
    }

    public MianFunctionView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }
}
