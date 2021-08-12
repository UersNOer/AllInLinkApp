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
import com.example.android_supervisor.ui.model.YqcsDayRes;
import com.squareup.picasso.Picasso;
import com.example.android_supervisor.R;
import com.example.android_supervisor.common.UserSession;
//import com.example.android_supervisor.yqmodular.ui.model.YqcsDayRes;

import java.util.List;

/**
 * 历史记录 适配器
 */
public class YqcsEventItemAdapter extends ObjectAdapter<YqcsDayRes.WhDetailsListBean> {
    private final Context mContext;
    private String searchKey;
    private int adapterTyle;

    public YqcsEventItemAdapter(Context context, int adapterTyle) {
        super(R.layout.yq_csitem_event_list);
        this.mContext = context;
        this.adapterTyle = adapterTyle;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, YqcsDayRes.WhDetailsListBean object, int position) {
        TextView tvIndex = holder.getView(R.id.tv_event_index, TextView.class);
        tvIndex.setText(String.valueOf(position + 1));

        TextView tvTitle = holder.getView(R.id.tv_event_title, TextView.class);
        tvTitle.setText(object.getTypeName());


        TextView tvTime = holder.getView(R.id.tv_event_time, TextView.class);
//        tvTime.setText(DateUtils.format(object.getReportTime()));
        tvTime.setText(object.getOrdate());

        TextView tvAddress = holder.getView(R.id.tv_event_address, TextView.class);
        tvAddress.setText(object.getObjpos());

        TextView tvDesc = holder.getView(R.id.tv_event_desc, TextView.class);
        tvDesc.setText(object.getObjname());

        ImageView ivCover = holder.getView(R.id.iv_event_cover, ImageView.class);
        Uri coverUri = null;
        List<YqcsDayRes.WhDetailsListBean.Img> attaches = object.getImgPathList();
        if (attaches != null && attaches.size() > 0) {
            String coverUrl = attaches.get(0).getFilePath();
            if (!TextUtils.isEmpty(coverUrl)) {
                if (!(coverUrl.contains("http"))) {
//                    String fileServer = SaveObjectUtils.getInstance(mContext).getObject("fileServer", null);
                    String fileServer = UserSession.getFileServer(mContext);
                    if (!TextUtils.isEmpty(fileServer)) {
                        coverUrl = new StringBuilder(fileServer).append(attaches.get(0).getFilePath()).toString();
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


    private boolean isShowCaseType;

    public void setShowCaseType(boolean isShow) {
        isShowCaseType = isShow;
    }
}
