package com.example.android_supervisor.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cncoderx.recyclerviewhelper.adapter.ObjectAdapter;
import com.example.android_supervisor.R;
import com.example.android_supervisor.entities.Message;
import com.example.android_supervisor.ui.view.CircleImageView;
import com.example.android_supervisor.utils.DateUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * @author yj 消息二级页面
 */
public class MessageListAdapter extends ObjectAdapter<Message> {

    private Context context;

    public MessageListAdapter(Context context) {
        super(R.layout.item_check_status);
        this.context = context;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, final Message msg, int position) {

        LinearLayout ll_msg_item  = holder.getView(R.id.ll_msg_item, LinearLayout.class);

        TextView tvTitle = holder.getView(R.id.tv_message_title, TextView.class);
        TextView tvDate = holder.getView(R.id.tv_message_date, TextView.class);
        CircleImageView imgIconBack = (CircleImageView) holder.getView(R.id.icon_back_color, ImageView.class);
        ImageView imgIcon = holder.getView(R.id.msg_icon, ImageView.class);
        TextView tvContent = holder.getView(R.id.tv_message_content, TextView.class);
        switch (msg.getGroupCode()) {
            case Message.EVENT_TASK_HC://核查
            case Message.EVENT_TASK_HS://核实
                try {
                    if (!TextUtils.isEmpty(msg.getExtraInfo())) {
                        JSONObject extraObj = new JSONObject(msg.getExtraInfo());
                        int isCheck = extraObj.getInt("isCheck");
                        if (isCheck == 1) {
                            tvTitle.setText("核查任务提醒");
                            if (extraObj.has("caseTitle")) {
                                String caseTitle = extraObj.getString("caseTitle");
                                tvContent.setText("您当前有一条核查任务【" + caseTitle + "】,请及时处理");
                            } else
                                tvContent.setText("您当前有一条核查任务" + ",请及时处理");
                            setIconView(imgIconBack, imgIcon, R.drawable.icon_wait_hs, "#f39800");
                        } else {
                            if (extraObj.has("acceptCode")) {
                                String acceptCode = extraObj.getString("acceptCode");
                                tvContent.setText("您当前有一条核实任务【" + acceptCode + "】,请及时处理");
                            } else
                                tvContent.setText("您当前有一条核实任务" + ",请及时处理");
                            tvTitle.setText("核实任务提醒");
                            setIconView(imgIconBack, imgIcon, R.drawable.icon_wait_hs, "#32b16c");
                        }
                    }
                } catch (JSONException e) {
                }
                break;
            case Message.CHECK_UP:
                tvTitle.setText("抽查任务提醒");
                try {
                    if (msg.getExtraInfo() ==null){
                        tvContent.setText(msg.getTitle());
                    }else {
                        JSONObject extraObj = new JSONObject(msg.getExtraInfo());
                        if (extraObj.has("overDate") && !TextUtils.isEmpty(extraObj.getString("overDate"))) {
                            String overDate = extraObj.getString("overDate");
                            tvContent.setText("您当前有一条抽查任务【" + msg.getContent() + "】,有效期至【" + overDate + "】请及时处理");
                        } else {
                            tvContent.setText("您当前有一条抽查任务【" + msg.getContent() + "】,请及时处理");
                        }
                        setIconView(imgIconBack, imgIcon, R.drawable.attention, "#e60012");
                    }
                } catch (JSONException e) {
                }
                break;
            case Message.VIDEO:
                tvTitle.setText("视频会议提醒");
                tvContent.setText(msg.getContent()==null?"您当前有一条新的视频会议查看":
                        "您当前有一条新的视频消息【" +msg.getContent()+ "】" + ",请查看");

                setIconView(imgIconBack, imgIcon, R.drawable.icon_notice, "#0989fe");
                break;
            case Message.CHECK_UP_STATUS:
                tvTitle.setText(msg.getTitle());
                tvContent.setText(msg.getContent());
                setIconView(imgIconBack, imgIcon, R.drawable.icon_notice, "#0989fe");
                break;
            case "publics_mobile_news":
                tvTitle.setText("实时新闻提醒");
                try {
                    JSONObject extraObj = new JSONObject(msg.getExtraInfo());
                    String title = extraObj.getString("title");
                    if (!TextUtils.isEmpty(title))
                        tvContent.setText("您当前有一条新的实时新闻【" + title + "】,请查看");
                    else tvContent.setText("您当前有一条新的实时新闻,请查看");

                } catch (JSONException e) {
                }
                setIconView(imgIconBack, imgIcon, R.drawable.icon_notice, "#0989fe");
                break;
            case "publics_mobile_notice":
                tvTitle.setText("通知公告提醒");
                try {
                    JSONObject extraObj = new JSONObject(msg.getExtraInfo());
                    String title = extraObj.getString("title");
                    if (!TextUtils.isEmpty(title))
                        tvContent.setText("您当前有一条新的通知公告【" + title + "】,请查看");
                    else tvContent.setText("您当前有一条新的通知公告,请查看");
                } catch (JSONException e) {
                }
                setIconView(imgIconBack, imgIcon, R.drawable.icon_notice, "#0989fe");
                break;
            case Message.STAFF_MSG:
                tvTitle.setText("业务提醒");
                if (!TextUtils.isEmpty(msg.getContent())){
                    tvContent.setText("您当前有一条新的业务提醒【" + msg.getContent() + "】,请查看");
                }
                setIconView(imgIconBack, imgIcon, R.drawable.icon_notice, "#0989fe");
                break;
//{"mesType":"TEXT","title":"案件[null]回退",  question_rollback_apply
// "content":"请处理案件：null  当前环节：监督员核实  来件时间：2019-11-20 14:04:05","params":"{\"currentLink\":\"监督员核实\",\"actInstId\":1197032470516600834,\"recipientId\":\"1128110726632734721\",\"comment\":\"  bbbnn\",\"id\":1197029335739928577,\"procInstId\":1197029336918528000}","createTime":"2019-11-20 14:04:06","parentGroupCode":"process_notice","groupCode":"question_rollback_apply","messageId":"1197032848607940609","fromId":"1195218189332516866"}
            default:
                tvTitle.setText(msg.getTitle());
                tvContent.setText(msg.getContent());
                try {
                    if (!TextUtils.isEmpty(msg.getExtraInfo())){
                        JSONObject extraObj = new JSONObject(msg.getExtraInfo());

                        String content = extraObj.getString("content");
                        tvContent.setText(!TextUtils.isEmpty(content)?content:"");

//                        String title = extraObj.getString("title");
//                        tvContent.setText(!TextUtils.isEmpty(title)?"您当前有一条新的消息【" + title + "】,请查看":);

                    }else {
                        tvContent.setText(msg.getContent());
                    }

                } catch (JSONException e) {
                }
                setIconView(imgIconBack, imgIcon, R.drawable.icon_notice, "#0989fe");
                break;

        }
        Date msgTime = msg.getMsgTime();
        tvDate.setText(DateUtils.smartFormat(msgTime));

        ll_msg_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemListener!=null){
                    onItemListener.onitemClick(view,position);
                }
            }
        });

    }

    private void setIconView(CircleImageView imgIconBack, ImageView imgIcon, int resId, String colorId) {
        imgIcon.setBackgroundResource(resId);
        int tint = Color.parseColor(colorId);
        imgIconBack.getBackground().setColorFilter(tint, PorterDuff.Mode.DARKEN);
    }


    private  OnItemListener onItemListener;
    public void setOnItemListener(OnItemListener onItemListener){
        this.onItemListener = onItemListener;
    }


    public interface OnItemListener{
        void onitemClick(View view, int position);

    }
}
