package com.example.android_supervisor.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cncoderx.recyclerviewhelper.adapter.ObjectAdapter;
import com.example.android_supervisor.R;
import com.example.android_supervisor.entities.MessageTitle;
import com.example.android_supervisor.socketio.MsgManager;
import com.example.android_supervisor.ui.view.CircleImageView;
import com.example.android_supervisor.utils.DateUtils;

/**
 * @author yj
 */
public class MessageTitleAdapter extends ObjectAdapter<MessageTitle> {

    private Context context;

    public MessageTitleAdapter(Context context) {
        super(R.layout.item_message_group);
        this.context = context;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, final MessageTitle msg, int position) {
        TextView tvTitle = holder.getView(R.id.tv_message_title, TextView.class);
        TextView tv_message_count = holder.getView(R.id.tv_message_count, TextView.class);
        TextView tvDate = holder.getView(R.id.tv_message_date, TextView.class);
//        CircleImageView imgIconBack = (CircleImageView) holder.getView(R.id.icon_back_color, ImageView.class);
        ImageView imgIcon = holder.getView(R.id.iv_message_icon, ImageView.class);
        TextView tvContent = holder.getView(R.id.tv_message_content, TextView.class);
        tvTitle.setText(msg.getTitle());

        tv_message_count.setVisibility(msg.getMsgCount()==0? View.GONE:View.VISIBLE);
        tv_message_count.setText(String.valueOf(msg.getMsgCount()));

        MsgManager.MsgGroup msgGroup = MsgManager.getInstance(context).getMesGroupInfoByCode(msg.getGroupCode());
        imgIcon.setImageResource(msg.getGroupCode().equals("提醒助手") ? R.drawable.msg_mian:msgGroup.imageId);
        tvTitle.setText(!TextUtils.isEmpty(msgGroup.describe) ? msgGroup.describe : "提醒小助手");

        tvDate.setText(DateUtils.smartFormat(msg.getMsgTime()));

        tvContent.setText(msg.getContent());

//        setIconView(imgIconBack, imgIcon, R.drawable.attention, "#e60012");
    }

    private void setIconView(CircleImageView imgIconBack, ImageView imgIcon, int resId, String colorId) {
        imgIcon.setBackgroundResource(resId);
        int tint = Color.parseColor(colorId);
        imgIconBack.getBackground().setColorFilter(tint, PorterDuff.Mode.DARKEN);
    }
}
