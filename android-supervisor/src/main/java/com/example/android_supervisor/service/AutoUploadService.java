package com.example.android_supervisor.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.SoundPool;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;

import com.orhanobut.logger.Logger;
import com.example.android_supervisor.R;
import com.example.android_supervisor.entities.CacheEvent;
import com.example.android_supervisor.entities.EventPara;
import com.example.android_supervisor.entities.Message;
import com.example.android_supervisor.http.ServiceGenerator;
import com.example.android_supervisor.http.request.JsonBody;
import com.example.android_supervisor.http.response.Response;
import com.example.android_supervisor.http.response.ResponseObserver;
import com.example.android_supervisor.http.service.QuestionService;
import com.example.android_supervisor.notify.Notifies;
import com.example.android_supervisor.notify.NotifyManager;
import com.example.android_supervisor.socketio.MsgManager;
import com.example.android_supervisor.sqlite.PrimarySqliteHelper;
import com.example.android_supervisor.utils.DateUtils;
import com.example.android_supervisor.utils.LogUtils;
import com.example.android_supervisor.utils.ToastUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class AutoUploadService extends Service {

    private SoundPool mSoundPool;
    private Vibrator mVibrator;
    private int mSoundId;

    ConnectivityManager connectivityManager;

    public AutoUploadService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mVibrator = ((Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE));
        mSoundPool = new SoundPool(10, 1, 5);
        mSoundId = this.mSoundPool.load(this, R.raw.msg, 0);
        connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        executorService = Executors.newFixedThreadPool(5);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            // 请注意这里会有一个版本适配bug，所以请在这里添加非空判断
            if (connectivityManager != null) {
                connectivityManager.requestNetwork(new NetworkRequest.Builder().build(), new ConnectivityManager.NetworkCallback() {

                    @Override
                    public void onAvailable(Network network) {
                        super.onAvailable(network);
//                        ToastUtils.show(AutoUploadService.this,"网络已恢复");
                        Logger.d("upSaveCaseonAvailable");
                        upSaveCase();
                    }

                    /**
                     * 网络丢失的回调
                     * */
                    @Override
                    public void onLost(Network network) {
                        super.onLost(network);
                        Log.e("lzp", "onLost");
                    }

                    /**
                     * 当建立网络连接时，回调连接的属性
                     * */
                    @Override
                    public void onLinkPropertiesChanged(Network network, LinkProperties linkProperties) {
                        super.onLinkPropertiesChanged(network, linkProperties);
                        Log.e("lzp", "onLinkPropertiesChanged");
                    }

                    /**
                     *  按照官方的字面意思是，当我们的网络的某个能力发生了变化回调，那么也就是说可能会回调多次
                     *
                     *  之后在仔细的研究
                     * */
                    @Override
                    public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
                        super.onCapabilitiesChanged(network, networkCapabilities);
                        Log.e("lzp", "onCapabilitiesChanged");
                    }

                    /**
                     * 在网络失去连接的时候回调，但是如果是一个生硬的断开，他可能不回调
                     * */
                    @Override
                    public void onLosing(Network network, int maxMsToLive) {
                        super.onLosing(network, maxMsToLive);
                        Log.e("lzp", "onLosing");
                        if (executorService!=null){
                            executorService.shutdown();
                        }
                    }

                    /**
                     * 按照官方注释的解释，是指如果在超时时间内都没有找到可用的网络时进行回调
                     * */
                    @Override
                    public void onUnavailable() {
                        super.onUnavailable();
                        Log.e("lzp", "onUnavailable");
                        if (executorService!=null){
                            executorService.shutdown();
                        }
                    }

                });
            }
        }
    }

    /**
     * 判断当前网络有没有联网
     * 并且判断是有线还是无线
     *
     * 0:无网络连接
     * 1：有线网络
     * 2：无线网络
     *
     * @return
     */
    private int getNetMode() {
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info == null) {
            return -1;
        }

        boolean iscon = info.isAvailable();
        LogUtils.d("网络连接 =" + iscon + "，连接方式：" + info.getType() + " ," + info.getTypeName());

        if (!iscon) {
            return -1;
        }
        if (info.getType() == ConnectivityManager.TYPE_ETHERNET) {
            return 2;
        } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
            return 1;
        } else if(info.getType() == ConnectivityManager.TYPE_MOBILE) {
            return 0;
        }
        else{
            return -1;
        }
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        LogUtils.d("网络连接 ="+getNetMode());
        if (getNetMode()!= -1){
            upSaveCase();
        }
        return super.onStartCommand(intent, flags, startId);

    }


    ExecutorService executorService;
    private boolean isRuning;
    /**
     * 自动上报保存的案件 重复调用会出现上报多次
     */
    private synchronized void upSaveCase() {

//        if (!executorService.isTerminated()){
//            return;
//        }
        Logger.d("upSaveCase:"+isRuning);
        if (isRuning){
            return;
        }
//        ToastUtils.show(this,"upSaveCase");
        Logger.d("upSaveCase1"+isRuning);

        List<CacheEvent> cacheEvents  = PrimarySqliteHelper.getInstance(this).getCacheEventDao().queryForEq("saveType",1);

        if (cacheEvents!=null && cacheEvents.size()>0){

            isRuning = true;
            taskCount = cacheEvents.size();
            Logger.d("upSaveCase2"+isRuning);
            executorService = Executors.newFixedThreadPool(5);

            ToastUtils.show(AutoUploadService.this,
                    "当前已检测到网络正常,正在自动为您上报保存的【"+cacheEvents.size()+"】条案件,请关注消息提醒",5000);
            for (CacheEvent cacheEvent:cacheEvents){

                if (!executorService.isShutdown()){
                    Logger.d("upSaveCase5isShutdown"+isRuning);
                    executorService.execute(new ReportTask(cacheEvent));
                }
            }

            executorService.shutdown();
            System.out.println("shutdown()：启动一次顺序关闭，执行以前提交的任务，但不接受新任务。");
            while(true){
                if(executorService.isTerminated()){//虽然说任务都结束了，但是此时db数据还没有做完删除，这次又会重复上传
                    Logger.d("upSaveCase3所有的子线程都结束了"+isRuning);
//                    isRuning = false;//不能这样处理
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }


    public class ReportTask implements Runnable{

        CacheEvent cacheEvent;
        public ReportTask(CacheEvent cacheEvent){

            this.cacheEvent = cacheEvent;

        }


        @Override
        public void run() {
            if (cacheEvent ==null){
                return;
            }
            reportEnvent(cacheEvent.getData(),cacheEvent.getId());
        }
    }


    private int taskCount,currentTaskCount;
    private void reportEnvent(EventPara mEventPara, String cachedId) {
        QuestionService questionService = ServiceGenerator.create(QuestionService.class);

        Observable<Response<String>> observable  = questionService.addEvent(new JsonBody(mEventPara));

        observable
                .subscribeOn(Schedulers.io())
                .doOnNext(new Consumer<Response<String>>() {
                    @Override
                    public void accept(Response<String> title) throws Exception {
//                        // 上报成功后需要删除保存案件
//                        sendSuccessMsg(cachedId, title.getData());
//                        Logger.d("upSaveCase onSuccess:"+mEventPara.getDesc());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseObserver<String>(AutoUploadService.this) {
                    @Override
                    public void onSuccess(String title) {
                        // 上报成功后需要删除保存案件
                        sendSuccessMsg(cachedId, title);
                        Logger.d("upSaveCase onSuccess:"+mEventPara.getDesc());
                    }

                    @Override
                    public void onFailure(int code, String errMsg) {
                        super.onFailure(code, errMsg);
                        sendFailMsg(cachedId, mEventPara);
                        Logger.d("upSaveCase onFailure:"+mEventPara.getDesc());
                    }

                });
    }

    private synchronized void addTaskCount(){
        currentTaskCount++;
    }



    private void sendSuccessMsg(String cachedId, String title) {
        if (cachedId != null) {
            PrimarySqliteHelper sqliteHelper = PrimarySqliteHelper.getInstance(AutoUploadService.this);
            sqliteHelper.getCacheEventDao().deleteById(cachedId);
        }

        //String caseTitle = "案件\"" + title + "\"上报成功";
        //增加上报成功消息
        Message message = new Message();
        message.setGroupCode(Message.CASEUPLOAD);
        message.setIsNew(true);
        message.setTitle(title);
        message.setContent("您当前有一条新的案件:【" + title + "】自动上传成功");
        message.setCreateTime(DateUtils.format(new Date(), 1));
        message.setMsgTime(new Date());

        message.setMsgId(new Date().toString());

        sendMsgAndplay(message);

        addTaskCount();
        if (currentTaskCount == taskCount){
            //已经执行完毕
            isRuning = false;
        }
    }

    private void sendFailMsg(String cachedId, EventPara mEventPara) {
        String cacheId2 = cachedId;
        if (cacheId2 == null) {
            cacheId2 = UUID.randomUUID().toString();
            // mEventPara.setCaseId(cacheId2);
        }
        PrimarySqliteHelper sqliteHelper = PrimarySqliteHelper.getInstance(AutoUploadService.this);
        sqliteHelper.getCacheEventDao().createOrUpdate(new CacheEvent(cacheId2, new Date(), mEventPara, mEventPara.getSource(),1));

        Message message = new Message();
        message.setGroupCode(Message.CASEUPLOAD);
        message.setIsNew(true);
        message.setTitle("上报失败");
        message.setContent("您有一条案件上报自动失败!保存在历史记录已保存模块中，等待下次自动上报");
        message.setCreateTime(DateUtils.format(new Date(), 1));
        message.setMsgTime(new Date());
        message.setMsgId(new Date().toString());

        sendMsgAndplay(message);

        addTaskCount();
        if (currentTaskCount == taskCount){
            //已经执行完毕
            isRuning = false;
        }
    }


    //可触发多次响铃 ,可优化
    private void sendMsgAndplay(Message message) {

        MsgManager.getInstance(AutoUploadService.this).addMsgToDb(message, true);

        mSoundPool.play(mSoundId, 1.0F, 1.0F, 0, 0, 1.0F);
        mVibrator.vibrate(2000L);
        NotifyManager.notify(AutoUploadService.this, Notifies.NOTIFY_TYPE_MSG, 1, 1);
    }

    @Override
    public void onDestroy() {
        if (executorService!=null){
            executorService.shutdownNow();
        }
        if (connectivityManager!=null){

        }
        super.onDestroy();
    }
}
