package com.example.android_supervisor.ui.adapter;

import android.content.Context;
import android.net.Uri;
import androidx.annotation.NonNull;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.example.android_supervisor.R;
import com.example.android_supervisor.common.UserSession;
import com.example.android_supervisor.entities.Attach;
import com.example.android_supervisor.entities.CacheEvent;
import com.example.android_supervisor.entities.ConstantEntity;
import com.example.android_supervisor.entities.EventPara;
import com.example.android_supervisor.sqlite.PrimarySqliteHelper;
import com.example.android_supervisor.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 暂存案件
 */
public class HistoryEventAdapter extends SelectableAdapter<CacheEvent> {
    private final Context mContext;
    private String searchKey;

    public HistoryEventAdapter(Context context) {
        super(R.layout.item_history_event);
        this.mContext = context;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, CacheEvent object, int position) {
        super.onBindViewHolder(holder, object, position);
        TextView tvIndex = holder.getView(R.id.tv_event_index, TextView.class);
        tvIndex.setText(String.valueOf(position + 1));


        EventPara evtPara = object.getData();
        TextView tvType = holder.getView(R.id.tv_event_type, TextView.class);
        tvType.setText(evtPara.getTypeName());
        TextView tvTitle = holder.getView(R.id.tv_event_title, TextView.class);
        switch (evtPara.getSource()) {
            case ConstantEntity.CASE_WEB:
                tvTitle.setText(mContext.getResources().getString(R.string.case_web));
                break;
            case ConstantEntity.CASE_PHONE:
                tvTitle.setText(mContext.getResources().getString(R.string.case_phone));
                break;
            case ConstantEntity.CASE_WECHAT:
                tvTitle.setText(mContext.getResources().getString(R.string.case_wechat));
                break;
            case ConstantEntity.CASE_NORMAL:
                tvTitle.setText(mContext.getResources().getString(R.string.case_normal));
                break;
            case ConstantEntity.CASE_FAST:
                tvTitle.setText(mContext.getResources().getString(R.string.case_fast));
                break;
            case ConstantEntity.CASE_LEADER:
                tvTitle.setText(mContext.getResources().getString(R.string.case_leader));
                break;
            case ConstantEntity.CASE_CZT:
                tvTitle.setText(mContext.getResources().getString(R.string.case_czt));
                break;
            case ConstantEntity.CASE_MASTER:
                tvTitle.setText(mContext.getResources().getString(R.string.case_master));
                break;
            case ConstantEntity.CASE_PUBLIC:
                tvTitle.setText(mContext.getResources().getString(R.string.case_public));
                break;
            default:
                tvTitle.setText(mContext.getResources().getString(R.string.case_normal));
                break;
        }

        TextView tvTime = holder.getView(R.id.tv_event_time, TextView.class);
        tvTime.setText(DateUtils.format(object.getCreateTime()));

        TextView tvAddress = holder.getView(R.id.tv_event_address, TextView.class);
        tvAddress.setText(getTextWithFlag(evtPara.getAddress()));

        TextView tvDesc = holder.getView(R.id.tv_event_desc, TextView.class);
        tvDesc.setText(getTextWithFlag(evtPara.getDesc()));

        ImageView ivCover = holder.getView(R.id.iv_event_cover, ImageView.class);
        Uri coverUri = null;
        List<Attach> attaches = evtPara.getAttaches();
        ArrayList<Attach> laterAttaches = evtPara.getLaterAttaches();
        if (laterAttaches != null && laterAttaches.size() > 0) {
            String coverUrl = laterAttaches.get(0).getThumbUrl();
            Log.d("coverUrl", "onBindViewHolder: " + coverUrl);
            if (!coverUrl.contains("http")) {
                String fileServer = UserSession.getFileServer(mContext);
                if (!TextUtils.isEmpty(fileServer)){
                    coverUrl = new StringBuilder(fileServer).append(laterAttaches.get(0).getThumbUrl()).toString();
                }
            }
            if (!TextUtils.isEmpty(coverUrl)) {
                coverUri = Uri.parse(coverUrl);
            }
        }

        if (attaches != null && attaches.size() > 0) {
            String coverUrl = attaches.get(0).getThumbUrl();
            if (!TextUtils.isEmpty(coverUrl)) {
                if (!coverUrl.contains("http")) {
                    String fileServer =  UserSession.getFileServer(mContext);
                    if (!TextUtils.isEmpty(fileServer)) {
                       // coverUri = Uri.parse(coverUrl);
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
    }

    @Override
    public void remove(@NonNull CacheEvent cache) {
        super.remove(cache);
        PrimarySqliteHelper sqliteHelper = PrimarySqliteHelper.getInstance(mContext);
        sqliteHelper.getCacheEventDao().delete(cache);
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
