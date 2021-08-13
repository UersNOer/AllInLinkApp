package com.example.android_supervisor.ui.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.cncoderx.recyclerviewhelper.adapter.ObjectAdapter;
import com.example.android_supervisor.R;
import com.example.android_supervisor.entities.CheckInRes;

/**
 * @author wujie
 */
public class CheckInAdapter extends ObjectAdapter<CheckInRes> {

    public CheckInAdapter() {
        super(R.layout.item_check_in);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, CheckInRes object, int position) {
        TextView tvName = holder.getView(R.id.tv_check_in_name, TextView.class);
        tvName.setText(object.getName());

        TextView tvOnTime = holder.getView(R.id.tv_check_in_on_time, TextView.class);
        tvOnTime.setText(object.getOnTime());

        TextView tvOffTime = holder.getView(R.id.tv_check_in_off_time, TextView.class);
        tvOffTime.setText(object.getOffTime());

        TextView tvOffAddress = holder.getView(R.id.tvOffAddress, TextView.class);
        tvOffAddress.setText("下班地址："+object.getOffAddress());

        TextView tvOnAddress = holder.getView(R.id.tvOnAddress, TextView.class);
        tvOnAddress.setText("上班地址："+object.getOnAddress());

        TextView tvOnStatus = holder.getView(R.id.tv_check_in_on_status, TextView.class);
        if (TextUtils.isEmpty(object.getOnStatus())) {
            tvOnStatus.setVisibility(View.GONE);
            tvOnStatus.setText(null);
        } else {
            tvOnStatus.setVisibility(View.VISIBLE);
            tvOnStatus.setText("(" + object.getOnStatus() + ")");
        }

        TextView tvOffStatus = holder.getView(R.id.tv_check_in_off_status, TextView.class);
        if (TextUtils.isEmpty(object.getOffStatus())) {
            tvOffStatus.setVisibility(View.GONE);
            tvOffStatus.setText(null);
        } else {
            tvOffStatus.setVisibility(View.VISIBLE);
            tvOffStatus.setText("(" + object.getOffStatus() + ")");
        }
    }
}
