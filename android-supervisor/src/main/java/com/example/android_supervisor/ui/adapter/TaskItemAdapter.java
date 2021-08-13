package com.example.android_supervisor.ui.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cncoderx.recyclerviewhelper.adapter.ObjectAdapter;
import com.squareup.picasso.Picasso;
import com.example.android_supervisor.R;
import com.example.android_supervisor.common.UserSession;
import com.example.android_supervisor.entities.Attach;
import com.example.android_supervisor.entities.EventRes;

import java.util.Date;
import java.util.List;

/**
 * @author 个人任务核实核查适配器
 */
public class TaskItemAdapter extends ObjectAdapter<EventRes> {
    private final Context context;
    private String searchKey;

    public TaskItemAdapter(Context context) {
        super(R.layout.item_task_list);
        this.context = context;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, EventRes object, int position) {
        TextView tvIndex = holder.getView(R.id.tv_event_index, TextView.class);
        tvIndex.setText(String.valueOf(position + 1));

        TextView tvTitle = holder.getView(R.id.tv_event_title, TextView.class);
//        tvTitle.setText(object.getTitle());
        if (!TextUtils.isEmpty(object.getAcceptCode())) {
            tvTitle.setText("受理号：");
            if (TextUtils.isEmpty(object.getTitle()))
                tvTitle.append(getTextWithFlag(object.getAcceptCode()));
            else
                tvTitle.append(getTextWithFlag(object.getTitle()));
        } else if (!TextUtils.isEmpty(object.getEventCode())) {
            tvTitle.setText("案件号：");
            if (TextUtils.isEmpty(object.getTitle()))
                tvTitle.append(getTextWithFlag(object.getAcceptCode()));
            else
                tvTitle.append(getTextWithFlag(object.getTitle()));
        }

        TextView tvDB = holder.getView(R.id.tv_event_db, TextView.class);
        if (object.isDB()) {
            tvDB.setVisibility(View.VISIBLE);
        } else {
            tvDB.setVisibility(View.GONE);
        }

        TextView tvCB = holder.getView(R.id.tv_event_cb, TextView.class);
        if (object.isCB()) {
            tvCB.setVisibility(View.VISIBLE);
        } else {
            tvCB.setVisibility(View.GONE);
        }

        TextView tvType = holder.getView(R.id.tv_event_type, TextView.class);
//        tvType.setText(DateUtils.format(object.getArrivalTime(), 0));
        tvType.setText(object.getArrivalTime());
        TextView tvTime = holder.getView(R.id.tv_event_time, TextView.class);
        TextView tvTimeMs = holder.getView(R.id.tv_event_time_ms, TextView.class);
        Log.d("onBindViewHolder", object.getSurplusType() + "\n---getSurplusTime:"
                + object.getSurplusTime() + "\n---isCheckSurplusTime:" + object.isCheckSurplusTime());
        tvTimeMs.setText("剩余时间：");
        if (object.getSurplusType().equals("-1")) {
            tvTimeMs.setText("超时时间：");
            tvTime.setText(new StringBuilder("超时").append(object.getSurplusTime()).toString());
            tvTime.setTextColor(0xffd8000c);
        } else if (object.getSurplusType().equals("0")) {
            tvTime.setText(new StringBuilder("剩余").append(object.getSurplusTime()).toString());
            tvTime.setTextColor(0xff9f6000);
        } else {
            tvTime.setText(new StringBuilder("剩余").append(object.getSurplusTime()).toString());
            tvTime.setTextColor(0xff8FD4AD);
        }

        TextView tvAddress = holder.getView(R.id.tv_event_address, TextView.class);
        tvAddress.setText(getTextWithFlag(object.getAddress()));

        TextView tvDesc = holder.getView(R.id.tv_event_desc, TextView.class);
        tvDesc.setText(getTextWithFlag(object.getDesc()));

        ImageView ivCover = holder.getView(R.id.iv_event_cover, ImageView.class);
        Uri coverUri = null;
        List<Attach> attaches = object.getAttaches();
        if (attaches != null && attaches.size() > 0) {
            String coverUrl = attaches.get(0).getThumbUrl();
            String coverUrl2 = attaches.get(0).getUrl();
            if (!TextUtils.isEmpty(coverUrl)) {
                if (!coverUrl.contains("http")) {
                    String fileServer = UserSession.getFileServer(context);
                    if (!TextUtils.isEmpty(fileServer)){
                        String path = new StringBuilder(fileServer).append(coverUrl).toString();
                        coverUri = Uri.parse(path);
                    }
                } else
                    coverUri = Uri.parse(coverUrl);
            }
            else if (!TextUtils.isEmpty(coverUrl2)){
                if (!coverUrl2.contains("http")) {
                    String fileServer = UserSession.getFileServer(context);
                    if (!TextUtils.isEmpty(fileServer)){
                        String path = new StringBuilder(fileServer).append(coverUrl2).toString();
                        coverUri = Uri.parse(path);
                    }
                } else
                    coverUri = Uri.parse(coverUrl);
            }
        }

        Picasso.get().load(coverUri)
                .resizeDimen(R.dimen.evt_img_width, R.dimen.evt_img_height)
                .placeholder(R.drawable.noimg)
                .into(ivCover);
    }

    private String getElapsedTime(Date expireTime) {
        StringBuilder elapsedTimeBuilder = new StringBuilder();

        long elapsedTime = expireTime.getTime() - System.currentTimeMillis();
        if (elapsedTime > 30 * 60 * 1000) {
            elapsedTimeBuilder.append("剩余");
        } else if (elapsedTime > 0) {
            elapsedTimeBuilder.append("剩余");
        } else {
            elapsedTimeBuilder.append("已超时");
        }

        elapsedTime = Math.abs(elapsedTime);
        final long secInMilli = 1000;
        final long minInMilli = secInMilli * 60;
        final long hourInMilli = minInMilli * 60;
        final long dayInMilli = hourInMilli * 24;

        long elapsedDays = elapsedTime / dayInMilli;
        if (elapsedDays > 0) {
            elapsedTimeBuilder.append(elapsedDays).append("天");
        }
        elapsedTime = elapsedTime % dayInMilli;

        long elapsedHours = elapsedTime / hourInMilli;
        if (elapsedHours > 0) {
            elapsedTimeBuilder.append(elapsedHours).append("时");
        }
        elapsedTime = elapsedTime % hourInMilli;

        long elapsedMinutes = elapsedTime / minInMilli;
        if (elapsedMinutes > 0) {
            elapsedTimeBuilder.append(elapsedMinutes).append("分钟");
        }

        return elapsedTimeBuilder.toString();
    }

    private int getElapsedTimeColor(Date expireTime) {
        int color;
        long elapsedTime = expireTime.getTime() - System.currentTimeMillis();
        if (elapsedTime > 30 * 60 * 1000) {
            color = 0xff00529b;
        } else {
            color = 0xff9f6000;
        }
        return color;
    }

    private CharSequence getTextWithFlag(CharSequence text) {
        if (TextUtils.isEmpty(searchKey)) {
            return text;
        }

        int start = TextUtils.indexOf(text, searchKey);
        if (start == -1) {
            return text;
        }

        int end = start + searchKey.length();
        SpannableString spanText = new SpannableString(text);
        spanText.setSpan(new ForegroundColorSpan(0xffff5555), start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return spanText;
    }
}
