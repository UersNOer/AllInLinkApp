package com.example.android_supervisor.service;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import androidx.annotation.Nullable;

import com.example.android_supervisor.common.AppContext;
import com.example.android_supervisor.common.UserSession;
import com.example.android_supervisor.config.AppConfig;
import com.example.android_supervisor.http.oauth.AccessToken;
import com.example.android_supervisor.ui.GestureLoginActivity;
import com.example.android_supervisor.ui.LoginActivity;
import com.example.android_supervisor.utils.LogUtils;
import com.orhanobut.logger.Logger;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author wujie
 */
public class AppLockThread implements ScheduledRunnable {
    private long lastTouchEventTime = 0L;
    private Context context;
    private Handler handler;
    private boolean paused;
    private int lockInterval;
    private int autoExitInterval;

    private static AppLockThread instance;

    @Nullable
    public static AppLockThread getInstance() {
        return instance;
    }

    public AppLockThread(Context context) {
        this.context = context;
        this.handler = new Handler();
        LogUtils.e("App auto." );
        //判断从数据库里面取数据，没有就取本地文件配置默认值  锁屏和退出 时间单位是分钟
        this.lockInterval = AppConfig.getAppAutoLockingInterval(context) * 60 * 1000;
        this.autoExitInterval = AppConfig.getAppAutoExitInterval(context) * 60 * 1000;
 //       this.autoExitInterval = 1 * 60 * 1000;


        LogUtils.e("App auto." + lockInterval+"---"+autoExitInterval);
    }

    @Override
    public void start(ScheduledExecutorService service) {
        instance = this;
        lastTouchEventTime = System.currentTimeMillis();
        service.scheduleWithFixedDelay(this, 1, 1, TimeUnit.MINUTES);
    }

    public void setLastTouchEventTime(long time) {
        lastTouchEventTime = time;
    }

    public void resume() {
        paused = false;
        lastTouchEventTime = System.currentTimeMillis();
    }

    public void pause() {
        lastTouchEventTime = System.currentTimeMillis();
        paused = true;
    }

    @Override
    public void stop(ScheduledExecutorService service) {
        instance = null;
    }

    @Override
    public void run() {
        if (System.currentTimeMillis() - lastTouchEventTime > autoExitInterval) {
            Logger.i("App auto exit!");
            handler.post(new Runnable() {
                @Override
                public void run() {
                    AccessToken.getInstance(context).delete();
                    UserSession.setUserId(context, "");

                    AppServiceManager.stop(context);
//                    EaseUI.getInstance().getNotifier().reset();
//                    EMClient.getInstance().logout(true);

                    AppContext appContext = (AppContext) context.getApplicationContext();
                    appContext.exit();

                    Intent loginIntent = new Intent(context, LoginActivity.class);
                    loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(loginIntent);
                }
            });
        }
        if (!paused) {
            if (System.currentTimeMillis() - lastTouchEventTime > lockInterval) {
                Logger.i("App locked!");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(context, GestureLoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                });
            }
        }
    }
}
