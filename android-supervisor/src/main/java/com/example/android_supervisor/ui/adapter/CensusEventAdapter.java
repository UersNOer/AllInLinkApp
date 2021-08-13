package com.example.android_supervisor.ui.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import com.cncoderx.recyclerviewhelper.adapter.ObjectAdapter;
import com.squareup.picasso.Picasso;
import com.example.android_supervisor.R;
import com.example.android_supervisor.entities.Attach;
import com.example.android_supervisor.entities.CensusEventRes;
import com.example.android_supervisor.utils.FileServerUtils;

import java.util.List;

/**
 * 专项任务适配器
 */
public class CensusEventAdapter extends ObjectAdapter<CensusEventRes> {
    private String searchKey;
    private Context context;

    public CensusEventAdapter(Context context) {
        super(R.layout.item_event_list);
        this.context = context;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, CensusEventRes object, int position) {
        TextView tvIndex = holder.getView(R.id.tv_event_index, TextView.class);
        tvIndex.setText(String.valueOf(position + 1));

        TextView tvTitle = holder.getView(R.id.tv_event_title, TextView.class);
        tvTitle.setText(getTextWithFlag(object.getTitle()));

        TextView tvType = holder.getView(R.id.tv_event_type, TextView.class);
        tvType.setText(object.getTypeName());

        TextView tvTime = holder.getView(R.id.tv_event_time, TextView.class);
//        tvTime.setText(DateUtils.format(object.getReportTime()));
        tvTime.setText(object.getReportTime());
        TextView tvAddress = holder.getView(R.id.tv_event_address, TextView.class);
        tvAddress.setText(getTextWithFlag(object.getAddress()));

        TextView tvDesc = holder.getView(R.id.tv_event_desc, TextView.class);
        tvDesc.setText(getTextWithFlag(object.getDesc()));


        TableRow tr_reportType = holder.getView(R.id.tr_reportType, TableRow.class);
        tr_reportType.setVisibility(View.GONE);

        ImageView ivCover = holder.getView(R.id.iv_event_cover, ImageView.class);


        Uri coverUri = null;
        List<Attach> attaches = object.getAttaches();
        if (attaches != null && attaches.size() > 0) {

            for (int i = 0; i < attaches.size(); i++) {
                if (attaches.get(i).getType() == 0) {
                    String coverUrl = attaches.get(i).getThumbUrl();
                    if (!TextUtils.isEmpty(coverUrl)){
                        coverUri = FileServerUtils.getUri(context, coverUrl, Uri.parse(coverUrl));
                        Picasso.get().load(coverUri)
                                .resizeDimen(R.dimen.evt_img_width, R.dimen.evt_img_height)
                                .placeholder(R.drawable.noimg)
                                .into(ivCover);
                    }
                    break;
                }

            }
        }


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
