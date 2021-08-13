package com.example.android_supervisor.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.tencent.liteav.demo.trtc.TRTCMainActivity;
import com.tencent.liteav.demo.trtc.debug.GenerateTestUserSig;
import com.tencent.trtc.TRTCCloudDef;
import com.example.android_supervisor.Presenter.CheckPresenter;
import com.example.android_supervisor.Presenter.VideoPresenter;
import com.example.android_supervisor.R;
import com.example.android_supervisor.common.UserSession;
import com.example.android_supervisor.entities.CheckUpRes;
import com.example.android_supervisor.entities.EventRes;
import com.example.android_supervisor.entities.Message;
import com.example.android_supervisor.entities.MessageTitle;
import com.example.android_supervisor.entities.VideoRoomPara;
import com.example.android_supervisor.http.ServiceGenerator;
import com.example.android_supervisor.http.response.Response;
import com.example.android_supervisor.http.service.PublicService;
import com.example.android_supervisor.http.service.QuestionService;
import com.example.android_supervisor.notify.Notifies;
import com.example.android_supervisor.notify.NotifyManager;
import com.example.android_supervisor.notify.NotifyReceiver;
import com.example.android_supervisor.socketio.MsgManager;
import com.example.android_supervisor.sqlite.PrimarySqliteHelper;
import com.example.android_supervisor.ui.ActualNewsDetailActivity;
import com.example.android_supervisor.ui.CheckUpActivity;
import com.example.android_supervisor.ui.HistoryActivity;
import com.example.android_supervisor.ui.TaskDetailActivity;
import com.example.android_supervisor.ui.adapter.MessageListAdapter;
import com.example.android_supervisor.utils.DialogUtils;
import com.example.android_supervisor.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static com.example.android_supervisor.entities.Message.CHECK_UP_STATUS;

public class MessageListFragment extends ListFragment {
    private MessageListAdapter adapter;
    private int pageIndex = 1;
    private int pageSize = 50;

    private String msgId = null;
    MessageTitle messageTitle;


    public static MessageListFragment newInstance(MessageTitle messageTitle) {
        Bundle args = new Bundle();
        args.putSerializable("messageTitleInfo", messageTitle);
        MessageListFragment messageListFragment = new MessageListFragment();
        messageListFragment.setArguments(args);
        return messageListFragment;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new MessageListAdapter(getContext());
        setAdapter(adapter);
        mRefreshLayout.setEnabled(false);

        NotifyManager.registerReceiver(getActivity(), mNotifyReceiver);

        if (getArguments() != null) {
            messageTitle = (MessageTitle) getArguments().getSerializable("messageTitleInfo");
            if (messageTitle != null) {
                pageIndex = 1;
                fetchData(0);
            }
        }

    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_message;
    }

    @Override
    public RecyclerView.ItemDecoration getItemDecoration(Context context) {
        return null;
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        super.onRefresh(refreshLayout);
        pageIndex = 1;
        fetchData(0);
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        super.onLoadMore(refreshLayout);
        fetchData(1);
    }

    private void fetchData(final int t) {
        if (t == 0) {
            adapter.clear();
        }
        Observable.create(new ObservableOnSubscribe<List<Message>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Message>> emitter) throws Exception {
                PrimarySqliteHelper sqliteHelper = PrimarySqliteHelper.getInstance(getContext());
                List<Message> msgs = sqliteHelper.getMessageDao().queryForEq("groupCode", messageTitle.getGroupCode());
//
//                long offset = (pageIndex - 1) * pageSize;
//                long limit = pageSize;
//
//                List<Message> msgs1 = sqliteHelper.getMessageDao()
//                        .queryBuilder()
//                        .offset(offset)
//                        .limit(limit)
//                        .query();

                emitter.onNext(msgs);
                emitter.onComplete();
            }
        }).compose(this.<List<Message>>bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate(new Action() {
                    @Override
                    public void run() throws Exception {
                        if (t == 0) {
                            finishRefresh();
                        } else if (t == 1) {
                            finishLoadMore();
                        }
                        setNoData(adapter.size() == 0);
                    }
                })
                .subscribe(new Consumer<List<Message>>() {
                    @Override
                    public void accept(List<Message> msgs) throws Exception {
                        if (msgs.size() > 0) {

                            for (Message msg : msgs) {
                                Log.d("Message", "{\"getMsgType\": " + msg.getMsgType()
                                        + ";\"getGroupCode\": " + msg.getGroupCode()
                                        + ";\"getExtraInfo\": " + msg.getExtraInfo()
                                        + ";\"getContent\": " + msg.getContent()
                                        + ";\"getFromId\": " + msg.getFromId()
                                        + ";\"getTitle\": " + msg.getTitle()
                                        + ";\"getMsgTime\": " + msg.getMsgTime()
                                        + "}");
                            }
                            Collections.reverse(msgs);
                            adapter.addAll(msgs);
                            pageIndex++;
                        } else {
                            setNoMoreData(true);
                            getActivity().finish();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });

       adapter.setOnItemListener(new MessageListAdapter.OnItemListener() {
           @Override
           public void onitemClick(View view, int position) {
               onItemClick(null,view,position,0);
           }
       });
    }

    @Override
    public void onItemClick(RecyclerView parent, View view, int position, long id) {
        Message msg = adapter.get(position);
        String msgId = msg.getMsgId();
        String extraInfo = msg.getExtraInfo();
        String groupCode = msg.getGroupCode();

        if (!TextUtils.isEmpty(groupCode)) {

            if (groupCode.equals(Message.EVENT_TASK_HS) || groupCode.equals(Message.EVENT_TASK_HC)) {
                try {
                    if (TextUtils.isEmpty(extraInfo)){
                        return;
                    }
                    JSONObject extraObj = new JSONObject(extraInfo);
                    String _id = extraObj.getString("id");
                    int isCheck = extraObj.getInt("isCheck");//区分核实还是核查
                    startTaskDetailActivity(msgId, _id, isCheck);

                } catch (JSONException e) {
                }
            } else if (groupCode.equals(Message.CHECK_UP)) {
                try {
                    if (TextUtils.isEmpty(extraInfo)){
                        return;
                    }
                    JSONObject extraObj = new JSONObject(extraInfo);

                    String _id = extraObj.optString("chkId");
                    if(TextUtils.isEmpty(_id)){
                        _id = extraObj.getString("id");
                    }

                    startCheckUpActivity(msgId, _id);
                } catch (JSONException e) {
                }
            } else if (groupCode.equals(Message.VIDEO)) {
                //params:{"title":"xxxx","roomId":"yyyy:}
                try {
                    JSONObject extraObj = new JSONObject(msg.getExtraInfo());
                    if (extraInfo==null){
                        ToastUtils.show(getContext(),"当前视频数据有误");
                        return;
                    }
                    String title = extraObj.getString("title");
                    String roomId = extraObj.getString("roomId");
                    startJoinRoom(UserSession.getUserName(getContext()),roomId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else if (groupCode.equals(Message.CHECK_UP_STATUS)){
                //跳转到抽查审核内容 Ctrl+Shift+U 大小写
                try {
                    if (TextUtils.isEmpty(extraInfo)){
                        return;
                    }
                    JSONObject extraObj = new JSONObject(extraInfo);
                    String _id = extraObj.optString("chkId");
                    if(TextUtils.isEmpty(_id)){
                         _id = extraObj.getString("id");
                    }

                    CheckPresenter checkPresenter  = new CheckPresenter();
                    checkPresenter.getCheckStatusByID(getContext(),_id,null);

                } catch (JSONException e) {
                }
            }
            else if (groupCode.equals("publics_mobile_news") || groupCode.equals("publics_mobile_notice")) {
                try {
                    Intent intent = new Intent(getContext(), ActualNewsDetailActivity.class);
                    JSONObject extraObj = new JSONObject(extraInfo);
                    String _id = extraObj.getString("id");
                    intent.putExtra("id", _id);
                    startActivity(intent);
                } catch (JSONException e) {
                }
            }
            else if (Message.CASEUPLOAD.equals(groupCode)){
                Intent intent = new Intent(getContext(), HistoryActivity.class);
                intent.putExtra("index", 1);
                startActivity(intent);

            }
            //不刷新界面  设置已读和 角标变化
            MsgManager.getInstance(getContext()).setReadMsgByTitleId(groupCode);

        }
    }

    /**
     * 跳转到腾讯云视频
     */
    private void startJoinRoom(String userId,String room) {
        int roomId = 123;
        try {
            roomId = Integer.valueOf(room);

            if (TextUtils.isEmpty(userId)) {
                Toast.makeText(getContext(), "请输入有效的用户名", Toast.LENGTH_SHORT).show();
                return;
            }
            VideoRoomPara para = new VideoRoomPara();
            para.setRoomId(room);
//            para.setUserId(Integer.valueOf(userId));

            int roomIdtemp = roomId;

            startJoinRoomInternal(roomIdtemp, UserSession.getUserId(getContext()));

//            VideoPresenter videoPresenter =  new VideoPresenter();
//            videoPresenter.enterVideoRoom(getContext(), para, new VideoPresenter.RoomCallBack() {
//                @Override
//                public void onSuccess() {
//
//                    startJoinRoomInternal(roomIdtemp, userId);
//                }
//            });


        } catch (Exception e) {
            Toast.makeText(getContext(), "请输入有效的房间号", Toast.LENGTH_SHORT).show();
            return;
        }


    }

    private void startJoinRoomInternal(final int roomId, final String userId) {
        final Intent intent = new Intent(getActivity(), TRTCMainActivity.class);
        // sdkAppId 和 userSig
        // 【*****】目前 Demo 为了方便您接入，使用的是本地签发 sig 的方式，您的项目上线，务必要保证将签发逻辑转移到服务端，否者会出现 key 被盗用，流量盗用的风险。
        int sdkAppId = GenerateTestUserSig.SDKAPPID;//服务端计算
      //  String userSig = GenerateTestUserSig.genTestUserSig(userId);//计算 客户端计算
        if (TextUtils.isEmpty(GenerateTestUserSig.UserSig)){
            Toast.makeText(getContext(), "UserSig未获取到", Toast.LENGTH_SHORT).show();
            return;
        }
        String userSig = GenerateTestUserSig.UserSig;
        intent.putExtra(TRTCMainActivity.KEY_SDK_APP_ID, sdkAppId);
        intent.putExtra(TRTCMainActivity.KEY_USER_SIG, userSig);


        // roomId userId
        intent.putExtra(TRTCMainActivity.KEY_ROOM_ID, roomId);
        intent.putExtra(TRTCMainActivity.KEY_USER_ID, userId);

//        saveUserInfo(String.valueOf(roomId), userId);

        intent.putExtra(TRTCMainActivity.KEY_APP_SCENE, TRTCCloudDef.TRTC_APP_SCENE_VIDEOCALL);
        intent.putExtra(TRTCMainActivity.KEY_CUSTOM_CAPTURE, false);
        intent.putExtra(TRTCMainActivity.KEY_VIDEO_FILE_PATH, "");

        // 是否使用外部采集

        startActivityForResult(intent,2);
    }

    public void startTaskDetailActivity(final String msgId, final String id, final int type) {
        QuestionService questionService = ServiceGenerator.create(QuestionService.class);
        Observable<Response<EventRes>> observable;
        switch (type) {
            case 1:
                observable = questionService.getHcTaskById(id);
                break;
            case 2:
                observable = questionService.getHsTaskById(id);
                break;
            default:
                return;
        }
        observable.compose(this.<Response<EventRes>>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Response<EventRes>>() {
                    @Override
                    public void accept(Response<EventRes> response) throws Exception {
                        if (response.isSuccess()) {
                            MessageListFragment.this.msgId = msgId;
                            Intent intent = new Intent(getContext(), TaskDetailActivity.class);
                            intent.putExtra("id", id);
                            intent.putExtra("status", type);
                            startActivityForResult(intent, 1);
                        } else {
                            throw new IllegalAccessException("加载数据失败");
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        DialogUtils.askYesNo(getContext(), null, "此消息已过期，是否删除？",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        PrimarySqliteHelper sqliteHelper = PrimarySqliteHelper.getInstance(getContext());
                                        sqliteHelper.getMessageDao().deleteById(msgId);

                                        pageIndex = 1;
                                        fetchData(0);
                                    }
                                });
                    }
                });
    }

    public void startCheckUpActivity(final String msgId, final String id) {
        PublicService publicService = ServiceGenerator.create(PublicService.class);
        Observable<Response<CheckUpRes>> observable = publicService.getCheckUpById(id);
        observable.compose(this.<Response<CheckUpRes>>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Response<CheckUpRes>>() {
                    @Override
                    public void accept(Response<CheckUpRes> response) throws Exception {
                        if (response.isSuccess()) {
                            MessageListFragment.this.msgId = msgId;
                            Intent intent = new Intent(getContext(), CheckUpActivity.class);
                            intent.putExtra("id", id);
                            startActivityForResult(intent, 2);
                        } else {
                            throw new IllegalAccessException("加载数据失败");
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        DialogUtils.askYesNo(getContext(), null, "此消息已过期，是否删除？",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        PrimarySqliteHelper sqliteHelper = PrimarySqliteHelper.getInstance(getContext());
                                        sqliteHelper.getMessageDao().deleteById(msgId);

                                        pageIndex = 1;
                                        fetchData(0);
                                    }
                                });
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        NotifyManager.unregisterReceiver(getActivity(), mNotifyReceiver);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (msgId != null) {
                PrimarySqliteHelper sqliteHelper = PrimarySqliteHelper.getInstance(getContext());
                sqliteHelper.getMessageDao().deleteById(msgId);
                //此处还需要加上  主标题 消息个数的减少
                pageIndex = 1;
                fetchData(0);
            }
        }else if(requestCode == 2 && resultCode == Activity.RESULT_OK){
            try{
                if (data.getExtras()!=null){
                    Bundle bundle = data.getExtras();
                    int roomId=bundle.getInt(TRTCMainActivity.KEY_ROOM_ID);
                    String userId=bundle.getString(TRTCMainActivity.KEY_USER_ID);

                    VideoPresenter videoPresenter = new VideoPresenter();
                    VideoRoomPara para = new VideoRoomPara();
                    para.setRoomId(String.valueOf(roomId));
                    para.setUserId(Integer.valueOf(userId));
                    videoPresenter.leaveVideoRoom(getContext(), para, new VideoPresenter.RoomCallBack() {
                        @Override
                        public void onSuccess() {

                        }
                    });
                }
            }catch (Exception e){
                e.printStackTrace();
            }


        }
    }


    final NotifyReceiver mNotifyReceiver = new NotifyReceiver() {

        @Override
        public void onReceive(Context context, int type, int status, int count) {

            if (type == Notifies.NOTIFY_TYPE_MSG) {
                fetchData(0);
            }
        }
    };


}
