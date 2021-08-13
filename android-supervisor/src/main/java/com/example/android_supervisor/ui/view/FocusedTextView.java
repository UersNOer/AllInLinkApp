package com.example.android_supervisor.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;

/**
 * @author wujie
 */
@SuppressLint("AppCompatCustomView")
public class FocusedTextView extends OptionalTextView {

    public FocusedTextView(Context context) {
        super(context);
    }

    public FocusedTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean isFocused() {
        return true;
    }


}
