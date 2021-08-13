package com.example.android_supervisor.service;

import android.content.Context;
import android.media.SoundPool;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.example.android_supervisor.R;
import com.example.android_supervisor.common.UserSession;
import com.example.android_supervisor.entities.Message;
import com.example.android_supervisor.entities.SocketMsg;
import com.example.android_supervisor.http.ServiceGenerator;
import com.example.android_supervisor.http.request.JsonBody;
import com.example.android_supervisor.http.service.PublicService;
import com.example.android_supervisor.notify.Notifies;
import com.example.android_supervisor.notify.NotifyManager;
import com.example.android_supervisor.socketio.MsgEchoClient;
import com.example.android_supervisor.socketio.MsgManager;
import com.example.android_supervisor.sqlite.PrimarySqliteHelper;
import com.example.android_supervisor.utils.DevLocalHostManager;
import com.example.android_supervisor.utils.Environments;
import com.example.android_supervisor.utils.LogUtils;
import com.example.android_supervisor.utils.OauthHostManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.socket.client.Ack;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * @author yangjie
 */
public class PullMsgThread implements ScheduledRunnable, MsgEchoClient.Listener {
    private Context mContext;
    private int mSoundId;
    private SoundPool mSoundPool;
    private Vibrator mVibrator;

    private MsgEchoClient msgEchoClient;
    private final ArrayDeque<SocketMsg> mMsgDeque;

    public PullMsgThread(Context context) {
        mContext = context;
        mMsgDeque = new ArrayDeque<SocketMsg>(16);
        mVibrator = ((Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE));
        mSoundPool = new SoundPool(10, 1, 5);
        mSoundId = this.mSoundPool.load(context, R.raw.msg, 0);

        if (Environments.isDeBug()) {
            Map<String, String> map = DevLocalHostManager.getHashMapData(mContext, DevLocalHostManager.KEY_DEV_LOCALHOST_SERVER_CONFIG);
            String msgUrl = map.get(DevLocalHostManager.KEY_MSG);
            LogUtils.e("msgUrl", msgUrl);
            if (TextUtils.isEmpty(msgUrl)) {
                throw new RuntimeException("消息服务器地址不能为空");
            }
            msgEchoClient = new MsgEchoClient(context, msgUrl);//开发
//            msgEchoClient = new MsgEchoClient(context, msgUrl);

        } else {
            String hostAddress = OauthHostManager.getInstance(context).getMsgConfig();
            //           String url = "https://zhcg.cszhx.top";
            if (!TextUtils.isEmpty(hostAddress)) {

                Log.d("socket-io url is: ", hostAddress);
                msgEchoClient = new MsgEchoClient(context, hostAddress);//测试
            }
        }
    }

    public void putMsgToDeque(SocketMsg socketMsg) {
        synchronized (mMsgDeque) {
            mMsgDeque.offer(socketMsg);
        }
    }

    public SocketMsg getMsgFromDeque() {
        synchronized (mMsgDeque) {
            return mMsgDeque.poll();
        }
    }

    public void run() {
        Log.d("socket thread1", "run: ");
        boolean isPlay = false;
        while (true && mMsgDeque.size() > 0) {

//            if (getMsgFromDeque()!=null){
            try {
                SocketMsg socketMsg = getMsgFromDeque();
                if (socketMsg != null) {
                    Message msg = socketMsg.getMsg();
                    if (msg != null) {

                        Log.d("socket 原始数据", msg.getMsgId() + "--" + msg.getContent() + "--" + msg.getGroupCode());
                        PrimarySqliteHelper sqliteHelper = PrimarySqliteHelper.getInstance(mContext);

                        //加工消息 抽取消息标题主页面数据 并存入本地消息标题表中
                        processMsgTitle(msg);

                        //存入完整的消息
                        List<Message> messages = sqliteHelper.getMessageDao().queryForEq("msgId", msg.getMsgId());
                        if (messages != null && messages.size() > 0 && socketMsg.getAck() != null) {
                            Log.d("socket 原始数据回写", messages.size() + "==" + messages.get(0).getMsgId() + "--" + messages.get(0).getContent() + "--" + messages.get(0).getGroupCode());
                            Ack ack = socketMsg.getAck();
                            ack.call();
                            setMsgRecevie(messages.get(0));
                            LogUtils.d("msgCallback" + "消息存在已经刷新");
                            return;
                        }

                        int isWrite = sqliteHelper.getMessageDao().create(msg);
                        if (isWrite > 0 && socketMsg.getAck() != null) {
                            Ack ack = socketMsg.getAck();
                            ack.call();
                        }
                        isPlay = true;
                        //   content = "";
                        setMsgRecevie(msg);
                        LogUtils.d("msgCallback" + "回写消息完毕");
                    }
                } else break;
            } catch (Exception e) {
                Log.d("socket thread", "run: " + e.getMessage());
            }


        }

        if (isPlay) {
            mSoundPool.play(this.mSoundId, 1.0F, 1.0F, 0, 0, 1.0F);
            mVibrator.vibrate(2000L);
            NotifyManager.notify(mContext, Notifies.NOTIFY_TYPE_MSG, 1, 1);
        }

    }

    private void setMsgRecevie(Message msg) throws JSONException {
        if (msg.getExtraInfo() == null) {
            return;
        }
        LogUtils.d("msgCallback" + "回写消息");
        //回馈消息
        JSONObject extraObj = new JSONObject(msg.getExtraInfo());
        String id = extraObj.getString("id");
        HashMap<String, String> params = new HashMap<>();
        params.put("chkId", id);
        params.put("userId", UserSession.getUserId(mContext));
        LogUtils.d("msgCallback" + id);
        PublicService service = ServiceGenerator.create(PublicService.class);
        Call<ResponseBody> call = service.msgCallback(new JsonBody(params));
        try {
            retrofit2.Response response = call.execute();
            if (response.isSuccessful()) {
                LogUtils.d("msgCallback" + response);
            } else {
                LogUtils.d("msgCallback" + "通知失败");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


//        service.msgCallback(new JsonBody(params))
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<Response>() {
//                    @Override
//                    public void accept(Response response) throws Exception {
//                        if (response.isSuccess()) {
//                            LogUtils.d("msgCallback"+response);
//                        } else {
//                        }
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        LogUtils.d("msgCallback"+throwable.getMessage());
//                        throwable.printStackTrace();
//                    }
//                });
    }

    /**
     * 保存消息标题
     */
    private synchronized void processMsgTitle(Message message) {

        MsgManager.getInstance(mContext).addMsgToDb(message, false);

    }

    public void start(ScheduledExecutorService service) {
        Logger.i("Start message thread");

        service.scheduleWithFixedDelay(this, 5L, 10L, TimeUnit.SECONDS);

        msgEchoClient.addListener(this);
        msgEchoClient.connect();
    }

    public void stop(ScheduledExecutorService service) {
        Logger.i("Stop message thread");

        mSoundPool.release();
        mVibrator.cancel();

        msgEchoClient.logout();
        msgEchoClient.disconnect();
        msgEchoClient.removeListener(this);
    }

    @Override
    public void onConnect() {
        Logger.i("Connect message server successful!");
        NotifyManager.notify(mContext, Notifies.NOTIFY_TYPE_GPRS, Notifies.NOTIFY_STATUS_ONLINE, 0);
        msgEchoClient.login();
    }

    @Override
    public void onDisconnect() {
        Logger.i("disconnected message server");
        NotifyManager.notify(mContext, Notifies.NOTIFY_TYPE_GPRS, Notifies.NOTIFY_STATUS_OFFLINE, 0);
    }

    //String content = "";

    @Override
    public void onMessage(JSONObject msgObj, Ack ack) {
        try {
            Log.d(",Receive Msg:", msgObj.toString());
            Message msg = new Gson().fromJson(msgObj.toString(), Message.class);
            msg.parseTime();
            if (!isAdded(msg)) {
                putDeque(msg, ack);
            }
//            if (TextUtils.isEmpty(content)) {
//                content = msg.getContent();
//                putDeque(msg, ack);
//            } else {
//                if (!content.equals(msg.getContent())) {
//                    putDeque(msg, ack);
//                }
//            }
        } catch (JSONException e) {
            Logger.e(e, "");
        }
    }

    private boolean isAdded(Message msg) {

        if (mMsgDeque != null) {

            for (Iterator itr = mMsgDeque.descendingIterator(); itr.hasNext(); ) {
                SocketMsg socketMsg = (SocketMsg) itr.next();
                if (socketMsg.getMsg().getMsgId().equals(msg.getMsgId())) {
                    return true;
                }
            }

        }
        return false;


    }

    private void putDeque(Message msg, Ack ack) throws JSONException {
        SocketMsg socketMsg = new SocketMsg();
        socketMsg.setAck(ack);
        socketMsg.setMsg(msg);
        putMsgToDeque(socketMsg);
    }
}

