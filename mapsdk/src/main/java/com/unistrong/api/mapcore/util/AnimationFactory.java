package com.unistrong.api.mapcore.util;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

/**
 * 动画工厂类，获取Animation和Interpolator.
 */
public class AnimationFactory {
    /**
     * 开始动画
     *
     * @param view         视图对象
     * @param fromXValue   动画开始横坐标
     * @param toXValue     动画结束横坐标
     * @param fromYValue   动画开始纵坐标
     * @param toYValue     动画结束纵坐标
     * @param duration     动画执行时间
     * @param listener     动画监听
     * @param isFillAfter  动画终止时停留在最后一帧
     * @param delaytedTime 延时时间
     */
    public static void startShowAnimationFromPoint(View view, float fromXValue,
                                                   float toXValue, float fromYValue, float toYValue, int duration,
                                                   Animation.AnimationListener listener, boolean isFillAfter, int delaytedTime) {
        if (view == null) {
            return;
        }
        TranslateAnimation translateAnimation = new TranslateAnimation(
                Animation.ABSOLUTE, fromXValue, Animation.ABSOLUTE, toXValue,
                Animation.ABSOLUTE, fromYValue, Animation.ABSOLUTE, toYValue);
        translateAnimation.setDuration(duration);

        if (listener != null)
            translateAnimation.setAnimationListener(listener);

        translateAnimation.setFillAfter(isFillAfter);
        translateAnimation.setStartOffset(delaytedTime);
//        translateAnimation.setRepeatCount(1);
        view.setDrawingCacheEnabled(true);
        view.startAnimation(translateAnimation);
    }
}
