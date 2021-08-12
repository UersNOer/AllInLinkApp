package com.example.android_supervisor.ui.adapter;

import android.widget.TextView;

import com.cncoderx.recyclerviewhelper.adapter.ObjectAdapter;
import com.example.android_supervisor.R;
import com.example.android_supervisor.entities.WorkScheRes;

/**
 * @author wujie
 */
public class WorkScheAdapter extends ObjectAdapter<WorkScheRes> {

    public WorkScheAdapter() {
        super(R.layout.item_commute_plan);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, WorkScheRes object, int position) {
        TextView tvName = holder.getView(R.id.tv_commute_plan_name, TextView.class);
        tvName.setText(object.getName());

        TextView tvTime = holder.getView(R.id.tv_commute_plan_time, TextView.class);
        String beginTime = object.getBeginTime();
        String endTime = object.getEndTime();
        tvTime.setText(beginTime + "~" + endTime);
    }
}
