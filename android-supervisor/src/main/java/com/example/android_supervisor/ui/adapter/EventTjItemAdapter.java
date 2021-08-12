package com.example.android_supervisor.ui.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.widget.ImageView;
import android.widget.TextView;

import com.cncoderx.recyclerviewhelper.adapter.ObjectAdapter;
import com.squareup.picasso.Picasso;
import com.example.android_supervisor.R;
import com.example.android_supervisor.common.UserSession;
import com.example.android_supervisor.entities.Attach;
import com.example.android_supervisor.entities.EventRes;

import java.util.List;

/**
 * @author wujie
 */
public class EventTjItemAdapter extends ObjectAdapter<EventRes> {
    private final Context mContext;
    private String searchKey;

    public EventTjItemAdapter(Context context) {
        super(R.layout.item_event_list);
        this.mContext = context;
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
        if ((!TextUtils.isEmpty(object.getType()))&&object.getType().equals("0")) {
            tvTitle.setText("受理号：");
            if (!TextUtils.isEmpty(object.getTitle()))
                tvTitle.append(getTextWithFlag(object.getTitle()));
//            else
//                tvTitle.append(getTextWithFlag(object.getAcceptCode()));
        } else if ((!TextUtils.isEmpty(object.getType()))&&object.getType().equals("1")) {
            tvTitle.setText("案件号：");
            if (!TextUtils.isEmpty(object.getTitle()))
                tvTitle.append(getTextWithFlag(object.getTitle()));
//            else
//                tvTitle.append(getTextWithFlag(object.getAcceptCode()));
        }

        TextView tvType = holder.getView(R.id.tv_event_type, TextView.class);
        tvType.setText(object.getTypeName());

        TextView tvTime = holder.getView(R.id.tv_event_time, TextView.class);
//        tvTime.setText(DateUtils.format(object.getReportTime()));
        tvTime.setText(object.getReportTime());
        TextView tvAddress = holder.getView(R.id.tv_event_address, TextView.class);
        tvAddress.setText(getTextWithFlag(object.getAddress()));

        TextView tvDesc = holder.getView(R.id.tv_event_desc, TextView.class);
        tvDesc.setText(getTextWithFlag(object.getDesc()));

        ImageView ivCover = holder.getView(R.id.iv_event_cover, ImageView.class);
        Uri coverUri = null;
        List<Attach> attaches = object.getAttaches();
        if (attaches != null && attaches.size() > 0) {
            String coverUrl = attaches.get(0).getThumbUrl();
            if (!TextUtils.isEmpty(coverUrl)) {
                if (!(coverUrl.contains("http"))) {

//                    String fileServer = SaveObjectUtils.getInstance(mContext).getObject("fileServer", null);
                    String fileServer = UserSession.getFileServer(mContext);
                    if (!TextUtils.isEmpty(fileServer)){
                        coverUrl = new StringBuilder(fileServer).append(attaches.get(0).getThumbUrl()).toString();
                    }
                }
                if (!TextUtils.isEmpty(coverUrl)) {
                    coverUri = Uri.parse(coverUrl);
                }
            }
        }

        Picasso.get().load(coverUri)
                .resizeDimen(R.dimen.evt_img_width, R.dimen.evt_img_height)
                .placeholder(R.drawable.noimg)
                .into(ivCover);
        TextView tv_event_caseTyoeName = holder.getView(R.id.tv_event_caseTyoeName, TextView.class);
        tv_event_caseTyoeName.setText("");
        TextView tv_event_caseTyoe =  holder.getView(R.id.tv_event_caseType, TextView.class);
        tv_event_caseTyoe.setText("");
//        tv_event_caseTyoe.setText(object.getSource() == 4 ? "一般案件" : "快速上报");
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
