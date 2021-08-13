package com.example.android_supervisor.ui.adapter;

import android.widget.TextView;

import com.cncoderx.recyclerviewhelper.adapter.ObjectAdapter;
import com.example.android_supervisor.R;
import com.example.android_supervisor.entities.CheckHfRes;

/**
 * @author yj
 */
public class CheckHfItemAdapter extends ObjectAdapter<CheckHfRes> {

    public CheckHfItemAdapter() {
        super(R.layout.item_my_check_up);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, CheckHfRes object, int position) {
        TextView tvIndex = holder.getView(R.id.tv_check_up_index, TextView.class);
        tvIndex.setText(String.valueOf(position + 1));

        TextView tvName = holder.getView(R.id.tv_check_up_name, TextView.class);
        tvName.setText(object.getId());

        TextView tvContent = holder.getView(R.id.tv_check_up_content, TextView.class);
        tvContent.setText(object.getReplyContent());

        TextView tvTime = holder.getView(R.id.tv_check_up_time, TextView.class);
//        tvTime.setText(DateUtils.format(object.getBeginDate()));

        String format = String.format("开始时间:" + "%s", object.getCreateTime());
        tvTime.setText(format);

        TextView tv_check_up_endtime = holder.getView(R.id.tv_check_up_endtime, TextView.class);
        String format1 = String.format("结束时间:" + "%s", object.getUpdateTime());
        tv_check_up_endtime.setText(format1);

        TextView tv_hfStatus = holder.getView(R.id.tv_hfStatus, TextView.class);
        tv_hfStatus.setText("回复状态:"+ (object.getDbStatus().equals("1")?"按时回复":"未按时回复"));

//        SpannableString spanText = new SpannableString(object.getDbStatus().equals("1")?"按时回复":"未按时回复");
//        spanText.setSpan(new ForegroundColorSpan(0xFF0000), 0, spanText.toString().length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//        tvContent.setText(object.getReplyContent() +"  "+spanText.toString());
    }
}
