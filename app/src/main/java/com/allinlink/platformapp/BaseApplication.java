package com.allinlink.platformapp;


import android.content.Context;
import android.text.TextUtils;

import androidx.multidex.MultiDex;

import com.allinlink.platformapp.video_project.utils.LogUtil;
import com.allinlink.platformapp.video_project.utils.UserCache;
import com.shuyu.gsyvideoplayer.cache.CacheFactory;
import com.shuyu.gsyvideoplayer.cache.ProxyCacheManager;
import com.shuyu.gsyvideoplayer.player.IjkPlayerManager;
import com.shuyu.gsyvideoplayer.player.PlayerFactory;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.unistrong.utils.AppContextUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import me.goldze.mvvmhabit.crash.CaocConfig;
import me.goldze.mvvmhabit.utils.KLog;

public class BaseApplication extends  me.goldze.mvvmhabit.base.BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();

        BaseApplication.setApplication(this);
        MultiDex.install(this);
        AppContextUtils.initApp(this);
        //初始化Vpn
//
        // 初始化设备ID
//        if (TextUtils.isEmpty(SharedPreferencesUtil.getInstance().getDeviceId())) {
//            SharedPreferencesUtil.getInstance()
//                    .putDeviceId(DeviceIdUtils.getDeviceId(getApplicationContext()));
//        }
        ProxyCacheManager.DEFAULT_MAX_SIZE= 10  * 1024 * 1024;
        CacheFactory.setCacheManager(ProxyCacheManager.class);
        PlayerFactory.setPlayManager(IjkPlayerManager.class);
        GSYVideoType.setShowType(GSYVideoType.SCREEN_MATCH_FULL);

        //bugly崩溃日志收集
        initCrashReport();
//        UMConfigure.init(this, "5fb8739f1e29ca3d7bdfbd07", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, "7c2d3c2d010680be77fc32408a4067f4");
//        PushAgent mPushAgent = PushAgent.getInstance(this);
//        //注册推送服务，每次调用register方法都会回调该接口
//
//        mPushAgent.setNotificaitonOnForeground(true);
//
//
//        mPushAgent.register(new IUmengRegisterCallback() {
//
//            @Override
//            public void onSuccess(String deviceToken) {
//                //注册成功会返回deviceToken
//                LogUtil.i("UMeng", "注册成功" + deviceToken);
//                UserCache.deviceToken = deviceToken;
//            }
//
//            @Override
//            public void onFailure(String s, String s1) {
//                LogUtil.i("UMeng", "注册失败" + s + s1);
//
//            }
//        });
        KLog.init(true);
        //配置全局异常崩溃操作
        CaocConfig.Builder.create()
                .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //背景模式,开启沉浸式
                .enabled(true) //是否启动全局异常捕获
                .showErrorDetails(true) //是否显示错误详细信息
                .showRestartButton(true) //是否显示重启按钮
                .trackActivities(true) //是否跟踪Activity
                .minTimeBetweenCrashesMs(2000) //崩溃的间隔时间(毫秒)
                .errorDrawable(R.mipmap.ic_launcher) //错误图标
                .restartActivity(MainActivity.class) //重新启动后的activity
                //.errorActivity(YourCustomErrorActivity.class) //崩溃后的错误activity
                //.eventListener(new YourCustomEventListener()) //崩溃后的错误监听
                .apply();
    }

    /**
     * 初始化崩溃日志工具类
     */
    public void initCrashReport() {
        Context context = getApplicationContext();
        // 获取当前包名
        String packageName = context.getPackageName();
        // 获取当前进程名
        String processName = getProcessName(android.os.Process.myPid());
        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);
        strategy.setUploadProcess(processName == null || processName.equals(packageName));
        // 初始化Bugly
        CrashReport.initCrashReport(context, "aa7bef40b5", true, strategy);
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }
}
