package com.example.android_supervisor.ui;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.appcompat.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.example.android_supervisor.R;

public class SuperImageView extends AppCompatImageView {


    public SuperImageView(Context context) {
        this(context,null);
    }

    public SuperImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SuperImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs,R.styleable.AppCompatImageView);


        typedArray.recycle();
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                setBackgroundResource(R.drawable.add);
                 break;
            case MotionEvent.ACTION_UP:

                 break;

        }



        return super.onTouchEvent(event);
    }
}
