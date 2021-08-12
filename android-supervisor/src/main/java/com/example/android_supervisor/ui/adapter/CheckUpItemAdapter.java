package com.example.android_supervisor.ui.adapter;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.cncoderx.recyclerviewhelper.adapter.ObjectAdapter;
import com.example.android_supervisor.R;
import com.example.android_supervisor.entities.CheckUpRes;

/**
 * @author wujie
 */
public class CheckUpItemAdapter extends ObjectAdapter<CheckUpRes> {

    public CheckUpItemAdapter() {
        super(R.layout.item_my_check_up);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, CheckUpRes object, int position) {
        TextView tvIndex = holder.getView(R.id.tv_check_up_index, TextView.class);
        tvIndex.setText(String.valueOf(position + 1));

        TextView tvName = holder.getView(R.id.tv_check_up_name, TextView.class);
        tvName.setText(object.getName());

        TextView tvContent = holder.getView(R.id.tv_check_up_content, TextView.class);
        tvContent.setText(object.getContent());

        TextView tvTime = holder.getView(R.id.tv_check_up_time, TextView.class);
//        tvTime.setText(DateUtils.format(object.getBeginDate()));

        String format = String.format("开始时间:" + "%s", object.getBeginDate());
        tvTime.setText(format);

        TextView tv_check_up_endtime = holder.getView(R.id.tv_check_up_endtime, TextView.class);
        String format1 = String.format("结束时间:" + "%s", object.getEndDate());
        tv_check_up_endtime.setText(format1);
        tv_check_up_endtime.setTextColor(Color.RED);


        TextView tv_hfStatus = holder.getView(R.id.tv_hfStatus, TextView.class);
        tv_hfStatus.setVisibility(View.GONE);
    }
}
