package com.allinlink.platformapp.video_project;

import android.content.Context;
import android.text.TextUtils;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.cache.CacheFactory;
import com.shuyu.gsyvideoplayer.cache.ProxyCacheManager;
import com.shuyu.gsyvideoplayer.model.VideoOptionModel;
import com.shuyu.gsyvideoplayer.player.IPlayerManager;
import com.shuyu.gsyvideoplayer.player.IjkPlayerManager;
import com.shuyu.gsyvideoplayer.player.PlayerFactory;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.entity.UMessage;
import com.unistrong.utils.AppContextUtils;
import com.unistrong.utils.RxBus;
import com.unistrong.utils.SharedPreferencesUtil;
import com.allinlink.platformapp.video_project.utils.LogUtil;
import com.allinlink.platformapp.video_project.utils.UserCache;
import com.allinlink.platformapp.video_project.utils.VpnLinkUtils;
import com.unistrong.view.utils.DeviceIdUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import tv.danmaku.ijk.media.exo2.IjkExo2MediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * *
 * ......................我佛慈悲....................
 * ......................_oo0oo_.....................
 * .....................o8888888o....................
 * .....................88" . "88....................
 * .....................(| -_- |)....................
 * .....................0\  =  /0....................
 * ...................___/`---'\___..................
 * ..................' \\|     |// '.................
 * ................./ \\|||  :  |||// \..............
 * .............../ _||||| -卍-|||||- \..............
 * ..............|   | \\\  -  /// |   |.............
 * ..............| \_|  ''\---/''  |_/ |.............
 * ..............\  .-\__  '-'  ___/-. /.............
 * ............___'. .'  /--.--\  `. .'___...........
 * .........."" '<  `.___\_<|>_/___.' >' ""..........
 * ........| | :  `- \`.;`\ _ /`;.`/ - ` : | |.......
 * ........\  \ `_.   \_ __\ /__ _/   .-` /  /.......
 * ....=====`-.____`.___ \_____/___.-`___.-'=====....
 * ......................`=---='.....................
 * ..................佛祖保佑 ,永无BUG................
 * ....................onRequestPermissionsResult
 * ..................南无机械工程佛................
 */

public class UniApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        //dex分包
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
        UMConfigure.init(this, "5fb8739f1e29ca3d7bdfbd07", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, "7c2d3c2d010680be77fc32408a4067f4");
        PushAgent mPushAgent = PushAgent.getInstance(this);
        //注册推送服务，每次调用register方法都会回调该接口

        mPushAgent.setNotificaitonOnForeground(true);


        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回deviceToken
                LogUtil.i("UMeng", "注册成功" + deviceToken);
                UserCache.deviceToken = deviceToken;
            }

            @Override
            public void onFailure(String s, String s1) {
                LogUtil.i("UMeng", "注册失败" + s + s1);

            }
        });

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