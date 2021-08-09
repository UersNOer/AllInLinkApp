package com.unistrong.model.activity.baseui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;


import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.Disposable;
import me.goldze.mvvmhabit.base.BaseActivity;

public abstract class BaseActivitySimple extends BaseActivity {
    private Unbinder unbinder;
    protected static final int REQUEST_LOGIN = 9008;
    public List<Disposable> disposableList;
    Context context;
    public AlertDialog alertDialog;
//    private AVLoadingIndicatorView aviloading;



    public boolean isBlackStatusBar() {
        return true;
    }



    //系统字体调节时，所有activity都不做更改
//    @Override
//    public Resources getResources() {
//        Resources res = super.getResources();
//        if (res!=null){
//            Configuration config=new Configuration();
//            config.setToDefaults();
//            res.updateConfiguration(config,res.getDisplayMetrics());
//        }
//        return res;
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ScreenUtils.adaptScreen4HorizontalSlide(this, 360);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        unbinder = ButterKnife.bind(this);
        context = this;
//        setStatusBarColor(this);
        initView();
//        initData();
    }

    /**
     * 初始化数据
     */




//    public void setStatusBarColor(Activity activity) {
//
//        //实现沉浸式效果
//        StatusBarCompat.translucentStatusBar(activity,true);
//        //修改状态栏字体图标颜色为黑色
//        if(isBlackStatusBar()){
//            StatusBarCompat.changeToLightStatusBar(activity);
//        }else {
//            StatusBarCompat.cancelLightStatusBar(activity);
//        }
//        //是否设置padding
//        if(isSetFitsSystemWindows()){
//            StatusBarUtil.setRootView(activity,true);
//        }
//
//    }

    public boolean  isSetFitsSystemWindows(){
        return  true;
    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        //TODO 添加功能
    }

    @Override
    protected void onPause() {
        super.onPause();
        //TODO 移除功能


    }

    @Override
    protected void onDestroy() {
//        if (EventBus.getDefault().isRegistered(this))
//            EventBus.getDefault().unregister(this);
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    public void showToast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }


    public boolean isSclback = true;

    /**
     * 初始化控件
     */
    private void initView() {
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        alertDialog = new AlertDialog.Builder(this, R.style.NobackDialog).create();
        View view = LayoutInflater.from(this).inflate(R.layout.base_activity, null);
//        aviloading = (AVLoadingIndicatorView) view.findViewById(R.id.avi);
        alertDialog.setView(view);
    }

    /**
     * 获取DisposableList
     *
     * @return disposableList
     */

    public List<Disposable> getDisposableList() {
        if (disposableList == null) disposableList = new ArrayList<>();
        return disposableList;
    }


    /**
     * @return 默认SharedPreferences
     */

    public SharedPreferences getSharedPreferences() {
        return getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
    }

    /**
     * 关闭软键盘
     *
     * @param view getWindowToken()
     */
    protected void closeKeyboard(@NonNull View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 设置contentView
     *
     * @return
     */

    public void startLogin() {

    }

    public void startrepeatLogin() {

    }

    public void putData(String key, boolean flag) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean(key, flag);
        edit.commit();
    }

    public void putData(String key, String flag) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(key, flag);
        edit.commit();
    }

    public boolean getData(String key) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        return sharedPreferences.getBoolean(key, false);
    }

    public boolean getData(String key, boolean defalue) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        return sharedPreferences.getBoolean(key, defalue);
    }

    public String getStringData(String key) {
        SharedPreferences sharedPreferences = getSharedPreferences();
        return sharedPreferences.getString(key, "");
    }

    /**
     * 显示提示信息
     */
    public void showDialog(@StringRes int title,
                           @StringRes int msg,
                           @StringRes int negative,
                           DialogInterface.OnClickListener negativeListener,
                           @StringRes int positive,
                           DialogInterface.OnClickListener positiveListener,
                           boolean cancelable) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(msg)
//                .setNegativeButton(negative, negativeListener)
//                .setPositiveButton(positive, positiveListener)
                .setPositiveButton(negative, negativeListener)
                .setNegativeButton(positive, positiveListener)
                .setCancelable(cancelable)
                .show();
    }

    public void showLoadingDialog() {
        alertDialog.show();
    }

}
