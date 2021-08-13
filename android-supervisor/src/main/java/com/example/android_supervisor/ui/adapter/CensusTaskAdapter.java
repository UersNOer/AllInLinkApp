package com.example.android_supervisor.ui.adapter;

import android.content.res.Resources;
import android.widget.TextView;

import com.cncoderx.recyclerviewhelper.adapter.ObjectAdapter;
import com.example.android_supervisor.R;
import com.example.android_supervisor.entities.CensusTaskRes;
import com.example.android_supervisor.utils.DateUtils;

import java.util.Date;

/**
 * @author wujie
 */
public class CensusTaskAdapter extends ObjectAdapter<CensusTaskRes> {

    public CensusTaskAdapter() {
        super(R.layout.item_census_task);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, CensusTaskRes object, int position) {
        TextView tvTitle = holder.getView(R.id.tv_census_task_title, TextView.class);
        tvTitle.setText(object.getName());

        TextView tvDuration = holder.getView(R.id.tv_census_task_duration, TextView.class);
//        Date beginTime = object.getBeginTime();
//        Date endTime = object.getEndTime();
//        tvDuration.setText(DateUtils.format(beginTime, 1) + " ~ " + DateUtils.format(endTime, 1));

        tvDuration.setText(object.getBeginTime() + " ~ " + object.getEndTime());

        TextView tvStatus = holder.getView(R.id.tv_census_task_status, TextView.class);

        Resources res = tvStatus.getResources();
        Date now = new Date();
        Date beginTime = DateUtils.parse(object.getBeginTime());
        Date endTime = DateUtils.parse(object.getEndTime());

        if (now.before(beginTime)) {
            tvStatus.setText("未开始");
            tvStatus.setTextColor(res.getColor(R.color.gray_light));
        } else if (now.after(endTime)) {
            tvStatus.setText("已结束");
            tvStatus.setTextColor(res.getColor(R.color.text_red));
        } else {
            tvStatus.setText("进行中");
            tvStatus.setTextColor(res.getColor(R.color.text_green));
        }
    }
}
