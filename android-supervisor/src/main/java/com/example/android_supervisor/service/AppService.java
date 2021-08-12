package com.example.android_supervisor.service;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;


import com.example.android_supervisor.R;
import com.example.android_supervisor.map.MapLocationAPI;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class AppService extends Service {
    ScheduledExecutorService mThreadPool = Executors.newScheduledThreadPool(1);
    PowerManager.WakeLock mWakeLock;
//    KeepAlivePlayer mKeepAlivePlayer;

    // GPS 工作线程
    private SendGpsThread mSendGpsThread;
    // 消息 工作线程
    private PullMsgThread mPullMsgThread;
    // APP 锁屏工作线程
    private AppLockThread mAppLockThread;

    @Override
    public void onCreate() {
        super.onCreate();
        acquireWakeLock();
        MapLocationAPI.initialize(this);
//        TXLocationApi.start(this);
//        startForeground();
//        startService(new Intent(this, SocketService.class));


        Context context = getApplicationContext();
        mSendGpsThread = new SendGpsThread(context);
        mSendGpsThread.start(mThreadPool);

        mPullMsgThread = new PullMsgThread(context);
        mPullMsgThread.start(mThreadPool);

//        mAppLockThread = new AppLockThread(context);
//        mAppLockThread.start(mThreadPool);


        GetTimer getTimer = new GetTimer(context);
        getTimer.start(mThreadPool);

//        Message msg = new Message();
//        msg.time = "2019-04-23 19:40:00";
//        msg.msgId = (long) (Math.random() * 1000000000000L);
//        msg.content = "您有一条新消息请注意查收";
//        mPullMsgThread.putMessageToDeque(msg);

//        mKeepAlivePlayer = new KeepAlivePlayer(this);

//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
//        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
//        registerReceiver(mScreenStatusReceiver, intentFilter);
    }

    /**
     * 获得唤醒锁
     */
    private void acquireWakeLock() {
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if (powerManager != null) {
            mWakeLock = powerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, this.getClass().getCanonicalName());
            mWakeLock.acquire();
        }
    }

    private void startForeground() {
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.app_icon_mask)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(getString(R.string.app_label))
                .setContentText("正在进行后台定位")
                .getNotification();

        notification.defaults = Notification.DEFAULT_SOUND;
        startForeground(1, notification);
    }

    @Override
    public void onDestroy() {
        releaseWakeLock();
//        TXLocationApi.stop(this);
//        stopService(new Intent(this, SocketService.class));

        mSendGpsThread.stop(mThreadPool);
        mPullMsgThread.stop(mThreadPool);
        if(mAppLockThread !=null){
            mAppLockThread.stop(mThreadPool);
        }

        if (!mThreadPool.isShutdown()) {
            mThreadPool.shutdownNow();
        }
//        unregisterReceiver(mScreenStatusReceiver);
        super.onDestroy();
    }

    /**
     * 释放唤醒锁
     */
    private void releaseWakeLock() {
        if (mWakeLock != null && mWakeLock.isHeld()) {
            mWakeLock.release();
            mWakeLock = null;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return Service.START_STICKY;
    }

//    /**
//     * 锁屏后播放bgm保活进程，开屏后停止播放bgm
//     */
//    private final BroadcastReceiver mScreenStatusReceiver = new BroadcastReceiver() {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            if (action != null) {
//                if (action.equals(Intent.ACTION_SCREEN_ON)) {
//                    mKeepAlivePlayer.stop();
//                } else if (action.equals(Intent.ACTION_SCREEN_OFF)) {
//                    mKeepAlivePlayer.start();
//                }
//            }
//        }
//    };


}
