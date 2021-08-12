package com.example.android_supervisor.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;

import com.example.android_supervisor.R;
import com.google.android.material.tabs.TabLayout;

public class AnimationTabLayout extends TabLayout {

    public AnimationTabLayout(Context context) {
        this(context,null);
    }

    public AnimationTabLayout(Context context, AttributeSet attrs) {
        this(context,null,0);
    }

    public AnimationTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        addAnimation();
    }

    public void addAnimation() {
        if(getTabCount()>0){
            for (int i = 0;i < getTabCount();i++){
                TabLayout.Tab tab =this.getTabAt(i);
                View view = tab.getCustomView();
                //增加动画效果
                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_to_right);
                LinearInterpolator lin = new LinearInterpolator();
                animation.setInterpolator(lin);
                view.startAnimation(animation);
            }
        }

    }
}
