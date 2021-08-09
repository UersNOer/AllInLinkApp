package com.unistrong.model.activity.baseui;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;


import androidx.databinding.ViewDataBinding;

import io.reactivex.annotations.Nullable;
import me.goldze.mvvmhabit.base.BaseViewModel;


/**
 * Created by Administrator on 2017/8/4.
 * Activity基类
 * 绑定Presenter
 */

public abstract class BaseActivity<V extends ViewDataBinding, VM extends BaseViewModel> extends BaseActivitySimple {
    protected ViewDataBinding binding;
    protected VM viewModel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.binding=binding;
        super.viewModel=viewModel;
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        try {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {

                // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
                View v = getCurrentFocus();

                if (isShouldHideInput(v, ev)) {
    //                hideSoftInput(v.getWindowToken());
                    closeKeyboard(v);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.dispatchTouchEvent(ev);
    }



    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = { 0, 0 };
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 多种隐藏软件盘方法的其中一种
     *
     * @param token
     */
    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            }
            im.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }









    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
