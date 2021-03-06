package com.example.android_supervisor.service;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.SoundPool;
import android.os.Build;
import android.os.IBinder;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.gson.Gson;
import com.example.android_supervisor.R;
import com.example.android_supervisor.entities.CacheEvent;
import com.example.android_supervisor.entities.EventPara;
import com.example.android_supervisor.entities.Message;
import com.example.android_supervisor.http.ServiceGenerator;
import com.example.android_supervisor.http.oauth.AccessToken;
import com.example.android_supervisor.http.request.JsonBody;
import com.example.android_supervisor.http.response.ResponseObserver;
import com.example.android_supervisor.http.service.QuestionService;
import com.example.android_supervisor.notify.Notifies;
import com.example.android_supervisor.notify.NotifyManager;
import com.example.android_supervisor.socketio.MsgManager;
import com.example.android_supervisor.sqlite.PrimarySqliteHelper;
import com.example.android_supervisor.utils.DateUtils;
import com.example.android_supervisor.utils.Environments;
import com.example.android_supervisor.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CaseReportService extends Service {

    private LocalBroadcastManager mLocalBroadcastManager;
    private SoundPool mSoundPool;
    private Vibrator mVibrator;
    private int mSoundId;

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        mVibrator = ((Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE));
        mSoundPool = new SoundPool(10, 1, 5);
        mSoundId = this.mSoundPool.load(this, R.raw.msg, 0);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //ces
        String cachedId = "";
        EventPara mEventPara = null;
        try {
            mEventPara = (EventPara) intent.getSerializableExtra("EventPara");
            cachedId = (String) intent.getStringExtra("cachedId");

            if (mEventPara == null) {
                return super.onStartCommand(intent, flags, startId);
            }
            ToastUtils.show(this, "???????????????????????????...????????????????????????");

            if (TextUtils.isEmpty(Environments.caseCheckUrl) || !Environments.isCaseCheck) {
                //???????????????
                reportEnvent(mEventPara, cachedId);
            } else {
                // reportEnvent(mEventPara, cachedId);
                reportCheckEnvent(mEventPara, cachedId);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return super.onStartCommand(intent, flags, startId);

    }

    private void reportCheckEnvent(EventPara mEventPara, String cachedId) {

        String url = Environments.caseCheckUrl;

        // url = "http://192.168.10.185:15030";
//        if (Environments.isDeBug()){
//            url =  url+"/android/umEvent";
////            url =  Environments.caseCheckUrl+"/questionApi/digital/question/android/umEvent";
//        }else {
//            url =  url+"/questionApi/digital/question/android/umEvent";
//        }
//        url = "http://192.168.20.42:15011/filter/doFilter";

        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        AccessToken accessToken = AccessToken.getInstance(getApplicationContext());
        String token = accessToken.getSafety();
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(mediaType, new Gson().toJson(mEventPara)))
                .addHeader("Authorization", "Bearer " + token)
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
//                reportEnvent(mEventPara,cachedId);
                sendFailMsg(cachedId, mEventPara);
                Log.d("", "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.d("????????????", response.protocol() + " " + response.code() + " " + response.message() + "  " + result);

                if (response.code() == 200 && !TextUtils.isEmpty(result)) {
                    com.example.android_supervisor.http.response.Response<String> res = new Gson().fromJson(result,
                            (com.example.android_supervisor.http.response.Response.class));
                    if (res.isSuccess()) {
                        //??????????????????
                        if (res == null || res.getData() == null) {
                            return;
                        }
                        JSONObject jsonObject = null;

                        try {
                            jsonObject = new JSONObject(String.valueOf(res.getData()));
                            if (jsonObject != null) {
                                String status = jsonObject.getString("status");
                                String message = jsonObject.getString("message");
                                if ("0".equals(status)) {
                                    reportEnvent(mEventPara, cachedId);
                                } else if ("1".equals(status)) {
                                    alert(mEventPara, cachedId, message);
                                }
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        sendSuccessMsg(cachedId, res.getData());
                    } else {
                        sendFailMsg(cachedId, mEventPara);
                    }
                } else {
                    sendFailMsg(cachedId, mEventPara);
                }
            }
        });
    }

    private void reportEnvent(EventPara mEventPara, String cachedId) {
        QuestionService questionService = ServiceGenerator.create(QuestionService.class);

        Observable<com.example.android_supervisor.http.response.Response<String>> observable = null;
        observable = questionService.addEvent(new JsonBody(mEventPara));


        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ResponseObserver<String>(CaseReportService.this) {
                    @Override
                    public void onSuccess(String title) {
                        // ???????????????????????????????????????
                        sendSuccessMsg(cachedId, title);
                    }

                    @Override
                    public void onFailure(int code, String errMsg) {
                        super.onFailure(code, errMsg);

                        sendFailMsg(cachedId, mEventPara);

                    }

                });
    }

    private void sendMsgAndplay(Message message) {

        MsgManager.getInstance(CaseReportService.this).addMsgToDb(message, true);

        mSoundPool.play(mSoundId, 1.0F, 1.0F, 0, 0, 1.0F);
        mVibrator.vibrate(2000L);
        NotifyManager.notify(CaseReportService.this, Notifies.NOTIFY_TYPE_MSG, 1, 1);
    }

    private void sendSuccessMsg(String cachedId, String title) {
        if (cachedId != null) {
            PrimarySqliteHelper sqliteHelper = PrimarySqliteHelper.getInstance(CaseReportService.this);
            sqliteHelper.getCacheEventDao().deleteById(cachedId);
        }

        //String caseTitle = "??????\"" + title + "\"????????????";
        //????????????????????????
        Message message = new Message();
        message.setGroupCode(Message.CASEUPLOAD);
        message.setIsNew(true);
        message.setTitle(title);
        message.setContent("??????????????????????????????:???" + title + "???????????????");
        message.setCreateTime(DateUtils.format(new Date(), 1));
        message.setMsgTime(new Date());

        message.setMsgId(new Date().toString());

        sendMsgAndplay(message);
    }

    private void sendFailMsg(String cachedId, EventPara mEventPara) {
        String cacheId2 = cachedId;
        if (cacheId2 == null) {
            cacheId2 = UUID.randomUUID().toString();
            // mEventPara.setCaseId(cacheId2);
        }
        PrimarySqliteHelper sqliteHelper = PrimarySqliteHelper.getInstance(CaseReportService.this);
        sqliteHelper.getCacheEventDao().createOrUpdate(new CacheEvent(cacheId2, new Date(), mEventPara, mEventPara.getSource(),0));

        Message message = new Message();
        message.setGroupCode(Message.CASEUPLOAD);
        message.setIsNew(true);
        message.setTitle("????????????");
        message.setContent("??????????????????????????????!??????????????????????????????");
        message.setCreateTime(DateUtils.format(new Date(), 1));
        message.setMsgTime(new Date());
        message.setMsgId(new Date().toString());

        sendMsgAndplay(message);
    }


    //    @Override
//    protected void onHandleIntent(Intent intent) {
//
//        try {
//
//            //??????????????????????????????
//
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * ??????????????????
//     */
//    private void sendThreadStatus(String status, int progress) {
//        Intent intent = new Intent(IntentServiceActivity.ACTION_TYPE_THREAD);
//        intent.putExtra("status", status);
//        intent.putExtra("progress", progress);
//        mLocalBroadcastManager.sendBroadcast(intent);
//    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public void alert(EventPara mEventPara, String cachedId, String message) {


//        {"success":true,"code":200,"message":"????????????","errorType":"INFO","data":{"status":"0","message":null}}
        try {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("??????");
            builder.setMessage(message != null ? message : "??????????????????????????????");
            builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    reportEnvent(mEventPara, cachedId);
                    dialogInterface.dismiss();
                }
            });
            builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            AlertDialog alertDialog = builder.create();//???create()?????????????????????builder??????????????????????????????????????????????????????
            if (Build.VERSION.SDK_INT >= 26){
                alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
            }else {
                alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            }
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


//    Window window = mAlertDialog.getWindow();
//   if(window !=null)
//
//    {
//        WindowManager.LayoutParams attributes = window.getAttributes();
//        if (attributes != null) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                attributes.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
//            } else {
//                attributes.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
//            }
//        }
//        window.setAttributes(attributes);
//    }
//    mAlertDialog.show();

}
