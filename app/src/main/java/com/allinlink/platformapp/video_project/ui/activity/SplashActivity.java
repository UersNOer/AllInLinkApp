package com.allinlink.platformapp.video_project.ui.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.unistrong.log.TLog;
import com.unistrong.utils.SharedPreferencesUtil;
import com.allinlink.platformapp.R;
import com.allinlink.platformapp.video_project.config.Configs;
import com.allinlink.platformapp.video_project.contract.activity.GuidePageEnterButtonClick;
import com.allinlink.platformapp.video_project.utils.VpnLinkUtils;
import com.allinlink.platformapp.video_project.widget.ShowGuidePage;

import java.net.MalformedURLException;
import java.net.URL;


/**
 * 欢迎页
 **/
public class SplashActivity extends LauncherBasicActivity {

    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.CAMERA",
            "android.permission.READ_PHONE_STATE"};


    private RelativeLayout guideRelativeLayout;

    @Override
    protected void initStart() {
        super.initStart();
        //这行代码一定要在setContentView之前，不然会闪退
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {
        super.initView();
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(this,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                verifyPermissions("android.permission.WRITE_EXTERNAL_STORAGE", PERMISSIONS_STORAGE);
            } else {
                startShow();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        guideRelativeLayout = findViewById(R.id.guide_relativeLayout);

        //设置环境地址
        setDOMAIN();




    }

    private void setDOMAIN() {
        String current_ipport = SharedPreferencesUtil.getInstance().getObject("current_ipport", String.class);
        if (current_ipport != null) {
            String[] ip_port = TextUtils.split(current_ipport, ",");
            Configs.DOMAIN = ip_port[0] + ":" + ip_port[1] + "/";
        } else {
            try {
                URL url = new URL(Configs.DOMAIN);
                boolean isHttps = url.toString().startsWith("https://");
                String ip;
                if (isHttps) {
                    ip = "https://" + url.getHost();
                } else {
                    ip = "http://" + url.getHost();
                }
                String port = url.getPort() + "";

                SharedPreferencesUtil.getInstance().setObject("current_ipport", ip + "," + port);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void permissionsResult(@NonNull String[] permissions, @NonNull int[] grantResults) {
        super.permissionsResult(permissions, grantResults);
        TLog.logD(TAG, "onRequestPermissionsResult permissions:" + permissions
                .toString() + "grantResults:" + grantResults.toString());
        startShow();
    }

    private void startShow() {
        findViewById(R.id.splash).postDelayed(() -> {
            String key = "isShowedGuidePage";
            Boolean isShowed = SharedPreferencesUtil.getInstance().getObject(key, Boolean.class);
            if (isShowed != null && isShowed) {
                toOpenApp();
            } else {
                guideRelativeLayout.setVisibility(View.VISIBLE);
                SharedPreferencesUtil.getInstance().setObject(key, true);
                new ShowGuidePage(guideRelativeLayout, this, new
                        GuidePageEnterButtonClick() {
                            @Override
                            public void guidePageFinish() {
                                SharedPreferencesUtil.getInstance().setObject(key, true);
                                toOpenApp();
                            }
                        });
            }
        }, 500);
    }

    private void toOpenApp() {



//        String token = SharedPreferencesUtil.getInstance().getToken();
//        if (TextUtils.isEmpty(token)) {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
//        } else {
//            startActivity(new Intent(SplashActivity.this, UniMainActivity.class));
//        }
//        finish();
    }

    @Override
    protected void destroyView() {

    }
}