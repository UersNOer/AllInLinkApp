package com.example.android_supervisor.service;

import android.content.Context;
import android.content.Intent;

import com.example.android_supervisor.utils.LogUtils;

public class AppServiceManager {

    public static void start(Context context) {
        LogUtils.e("Start App Service.");
        context.startService(new Intent(context, AppService.class));
        context.startService(new Intent(context, AutoUploadService.class));
    }

    public static void stop(Context context) {
        LogUtils.e("Stop App Service.");
        context.stopService(new Intent(context, AppService.class));
        context.stopService(new Intent(context, AutoUploadService.class));
    }


    public static void RestartServce(Context context){
        LogUtils.e("RestartServce App Service.");
        stop(context);
        start(context);
    }
}
