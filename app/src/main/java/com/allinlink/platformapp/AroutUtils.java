package com.allinlink.platformapp;

import android.content.Context;
import android.content.Intent;

import com.allinlink.platformapp.video_project.ui.activity.LoginActivity;
import com.allinlink.platformapp.video_project.ui.activity.SplashActivity;
import com.tangxiaolv.annotations.RouterModule;
import com.tangxiaolv.annotations.RouterPath;
import com.tangxiaolv.router.AndroidRouter;

import java.util.Map;

/*
 * 组件直接的界面跳转
 * */
@RouterModule(scheme = "uni", host = "AppProvider")
public class AroutUtils {

    public static void router(String path, Map<String, Object> map) {
        AndroidRouter.open("uni", "AppProvider", "/open" + path, map).call();
    }
    /**
     * @param context
     */
    @RouterPath("/openVideoAppLoginActivity")
    public void openVideoAppLoginActivity(Context context) {
        Intent intent = new Intent(context, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }
}
