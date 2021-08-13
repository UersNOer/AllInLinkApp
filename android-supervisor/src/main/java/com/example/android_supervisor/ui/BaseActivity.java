package com.example.android_supervisor.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.example.android_supervisor.R;
import com.example.android_supervisor.common.AppContext;
import com.example.android_supervisor.service.AppLockThread;
import com.example.android_supervisor.ui.view.TitleBar;
import com.example.android_supervisor.utils.MyDividerItemDecoration;
import com.example.android_supervisor.utils.ToastUtils;
import com.trello.rxlifecycle2.components.support.RxFragmentActivity;

import butterknife.BindView;

public class BaseActivity extends RxFragmentActivity {
    @BindView(R.id.root)
    View mRoot;

    @BindView(R.id.title_bar)
    public TitleBar mTitleBar;

    View mContentView;

    public AppContext appContext;
    public Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.setContentView(R.layout.activity_base);
        mContext = this;
        mRoot = super.findViewById(R.id.root);
        mTitleBar = super.findViewById(R.id.title_bar);
        mTitleBar.setTitle(getTitle());


        appContext = (AppContext) getApplication();
        if (appContext != null) {
            appContext.pushActivity(this);
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        mContentView = layoutInflater.inflate(layoutResID, (ViewGroup) mRoot, true);
//        SupportMultipleScreensUtil.init(BaseActivity.this);
//        SupportMultipleScreensUtil.scale(mContentView);
    }

    @Override
    public void setContentView(View view) {
       super.setContentView(view);
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        mTitleBar.setTitle(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }

    public View addMenu(CharSequence text) {
        return mTitleBar.addMenu(text);
    }

    public View addMenu(CharSequence text, View.OnClickListener listener) {
        return mTitleBar.addMenu(text, listener);
    }

    public void addMenu(View view) {
        mTitleBar.addMenu(view);
    }

    public View getRoot() {
        return mRoot;
    }

    public View getContentView() {
        return mContentView;
    }

    public TitleBar getTitleBar() {
        return mTitleBar;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            AppLockThread appLock = AppLockThread.getInstance();
            if (appLock != null) {
                appLock.setLastTouchEventTime(System.currentTimeMillis());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean dispatchKeyEvent(KeyEvent ev) {
        if (ev.getAction() == KeyEvent.ACTION_DOWN) {
            AppLockThread appLock = AppLockThread.getInstance();
            if (appLock != null) {
                appLock.setLastTouchEventTime(System.currentTimeMillis());
            }
        }
        return super.dispatchKeyEvent(ev);
    }

    protected boolean isEnableAppLock() {
        return true;
    }

    @Override
    protected void onDestroy() {
        AppContext appContext = (AppContext) getApplication();
        if (appContext != null) {
            appContext.removeActivity(this);
        }
        super.onDestroy();
    }

    /**
     * 设置状态栏颜色
     */
    public void setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(0x00000000);//0x00000000
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
        }
    }

    public void setStatusBar() {
        if (!isTaskRoot()) {
            finish();
            return;
        }
        setStatusBarColor();
        getTitleBar().setVisibility(View.GONE);
    }

    public void checkOverLayPermission() {

        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(getApplicationContext())) {
                //启动Activity让用户授权
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 2);
                return;
            }
        } else {
            //执行6.0以下绘制代码、
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (!Settings.canDrawOverlays(this)) {
                    // SYSTEM_ALERT_WINDOW permission not granted...
                    ToastUtils.show(BaseActivity.this, "悬浮窗权限未开启，会导致部分功能无法正常使用",9000);
                }
            }
        }
    }
    public void initRefresh(SmartRefreshLayout mRefreshLayout){
        mRefreshLayout.setRefreshHeader(new ClassicsHeader(mContext).setEnableLastTime(false));
        mRefreshLayout.setRefreshFooter(new ClassicsFooter(mContext));
    }
    public void showToast(String msg){
        ToastUtils.show(this,msg);
    }
    public void initRecycle(RecyclerView...recyclerViews){
        for (int i = 0;i < recyclerViews.length;i++){
            recyclerViews[i].setLayoutManager(new LinearLayoutManager(mContext));
            recyclerViews[i].addItemDecoration(new MyDividerItemDecoration(mContext,MyDividerItemDecoration.VERTICAL_LIST,true));
        }
    }
}
