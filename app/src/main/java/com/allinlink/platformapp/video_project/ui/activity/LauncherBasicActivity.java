package com.allinlink.platformapp.video_project.ui.activity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.unistrong.log.TLog;
import com.unistrong.view.view.IssLoadingDialog;


/**
 * Description:launcher 项目基类
 * Created by ltd ON 2020/4/12
 * Phone:18600920091
 * Email:td.liu@unistrong.com
 */
public abstract class LauncherBasicActivity extends AppCompatActivity implements ActivityCompat
        .OnRequestPermissionsResultCallback {

    protected final String TAG = getClass().getSimpleName();
    private static final int REQUEST_PERMISSIONS = 1;

    private IssLoadingDialog issLoadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initStart();
        if (initLayout() != 0) {
            setContentView(initLayout());
            initView();
        } else {
            TLog.logI("initLayout == null , then no view will display");
        }
        initBusiness();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showView();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        destroyView();
    }

    // 前置方法
    protected void initStart() {
    }

    // 初始化业务方法
    protected void initBusiness() {
    }

    // 暂停显示方法
    protected void stopView() {
    }

    // 初始化视图
    protected abstract int initLayout();

    protected void initView() {
    }

    // 显示或刷新视图
    protected void showView() {
    }

    // 销毁视图
    protected abstract void destroyView();

    protected void permissionsResult(@NonNull String[] permissions, @NonNull int[] grantResults) {
    }

    protected void verifyPermissions(@NonNull String permission, @NonNull String[] permissions) {
        try {
            //检测是否有写的权限
            int p = ActivityCompat.checkSelfPermission(this, permission);
            if (p != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS);
            } else {
                // 存在误差
                int[] grantResults = new int[permissions.length];
                for (int i = 0, size = permissions.length; i < size; i++) {
                    grantResults[i] = PackageManager.PERMISSION_GRANTED;
                }
                onRequestPermissionsResult(0, permissions, grantResults);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        TLog.logI("onRequestPermissionsResult requestCode:" + requestCode + "permissions:" + permissions
                .length + "grantResults:" + grantResults.length);
        if (requestCode == REQUEST_PERMISSIONS) {
            permissionsResult(permissions, grantResults);
        }
    }

    public void showLoading(String content) {
        showLoading(content, false);
    }

    public void showLoading(String content, boolean isCancel) {
        if (issLoadingDialog == null) {
            this.issLoadingDialog = new IssLoadingDialog(this);
        }
        this.issLoadingDialog.setCancelable(isCancel);
        if (TextUtils.isEmpty(content)) {
            issLoadingDialog.show();
        } else {
            issLoadingDialog.show(content);
        }
    }

    public void dismissLoading() {
        if (issLoadingDialog != null && issLoadingDialog.isShowing()) {
            issLoadingDialog.dismiss();
        }
    }

}
