package com.example.android_supervisor.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.example.android_supervisor.R;
import com.example.android_supervisor.entities.MessageTitle;
import com.example.android_supervisor.notify.Notifies;
import com.example.android_supervisor.notify.NotifyManager;
import com.example.android_supervisor.notify.NotifyReceiver;
import com.example.android_supervisor.socketio.MsgManager;
import com.example.android_supervisor.ui.MainActivity;
import com.example.android_supervisor.ui.MsgListActivity;
import com.example.android_supervisor.ui.TutorialActivity;
import com.example.android_supervisor.ui.adapter.MessageTitleAdapter;

import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MessageTitleFragment extends ListFragment {
    private MessageTitleAdapter adapter;

    private String msgId = null;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

       // mRefreshLayout.setEnabled(false);
        setEnableLoadMore(false);

        adapter = new MessageTitleAdapter(getContext());
        setAdapter(adapter);

        NotifyManager.registerReceiver(getActivity(), mNotifyReceiver);

        fetchData(0);
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

        fetchData(0);
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        super.onLoadMore(refreshLayout);
        //fetchData(1);
    }

    private void fetchData(final int t) {
        if (t == 0) {
            Log.d("ddd","fetchData()");
            adapter.clear();
        }
        Observable.create(new ObservableOnSubscribe<List<MessageTitle>>() {
            @Override
            public void subscribe(ObservableEmitter<List<MessageTitle>> emitter) throws Exception {
                List<MessageTitle> messageTitles = MsgManager.getInstance(getContext()).getAllNewMsgTitle();

                emitter.onNext(messageTitles);
                emitter.onComplete();
            }
        }).compose(this.<List<MessageTitle>>bindToLifecycle())
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
                .subscribe(new Consumer<List<MessageTitle>>() {
                    @Override
                    public void accept(List<MessageTitle> msgs) throws Exception {
                        if (msgs.size() > 0) {
                            Log.d("ddd","fetchData() = "+msgs.size());
                            int count = 0;
                            for (MessageTitle msg : msgs) {
                                Log.d("Message",
                                        ";\"getGroupCode\": " + msg.getGroupCode()
                                                + ";\"getExtraInfo\": " + msg.getExtraInfo()
                                                + ";\"getContent\": " + msg.getContent()
                                                + ";\"getTitle\": " + msg.getTitle()
                                                + ";\"getMsgTime\": " + msg.getMsgTime()
                                                + "}");
                                //重新计算某类型未阅读消息个数
                                msg.setMsgCount(MsgManager.getInstance(getContext()).getNewMsgbyTitle(msg).size());

                                count += msg.getMsgCount();
                                if (msg.getGroupCode().equals("提醒助手")) {
                                    msg.setContent("您好！欢迎使用城管通，点击查看操作指南");
                                } else {
                                    msg.setContent(String.format("您当前有%s条消息待查看", msg.getMsgCount()));
                                }
                            }
                            Collections.reverse(msgs);
                            adapter.addAll(msgs);

                            freshenMsgBadge(MsgManager.getInstance(getContext()).getAllMsgCount());
                        } else {
                            setNoMoreData(true);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }

    MessageTitle msg;
    @Override
    public void onItemClick(RecyclerView parent, View view, int position, long id) {
        msg = adapter.get(position);
        switch (msg.getGroupCode()){
            case "提醒助手":

                startActivity(new Intent(getActivity(), TutorialActivity.class));
                MsgManager.getInstance(getContext()).setReadMsgByTitleId(msg.getGroupCode());
                break;
            default:
                Intent intent = new Intent(getActivity(), MsgListActivity.class);
                intent.putExtra("messageTitleInfo", msg);
                startActivity(intent);
                break;
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        NotifyManager.unregisterReceiver(getActivity(), mNotifyReceiver);
    }


    /**
     *
     */
    final NotifyReceiver mNotifyReceiver = new NotifyReceiver() {
                @Override
                public void onReceive(Context context, int type, int status, int count) {
                    Log.d("mNotifyReceiver",type+"");

                    if (type == Notifies.NOTIFY_TYPE_MSG) {
                        fetchData(0);
                    }
                }

        @Override
        public void onReceive(Context context, int type, int status, int count, String refreshItem) {
            super.onReceive(context, type, status, count, refreshItem);
            if (type ==Notifies.NOTIFY_TYPE_MSG_BADGE){
                //更新item，提高加载速度
                if (msg!=null){
                    msg.setMsgCount(MsgManager.getInstance(getContext()).getNewMsgbyTitle(msg).size());
                    msg.setContent(String.format("您当前有%s条消息待查看", msg.getMsgCount()));
                    adapter.notifyDataSetChanged();
                }
                freshenMsgBadge(MsgManager.getInstance(getContext()).getAllMsgCount());
            }
        }
    };




    private void freshenMsgBadge(int count) {
        //获取并发送角标数据
        MainActivity parentActivity = (MainActivity)getActivity();
        if (parentActivity!=null){
            parentActivity.setMsgBadge(count);
        }
    }



}
