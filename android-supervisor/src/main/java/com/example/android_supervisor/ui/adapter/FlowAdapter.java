package com.example.android_supervisor.ui.adapter;

//import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.android_supervisor.R;
import com.example.android_supervisor.entities.EventFlowRes;

import java.util.List;

/**
 * Created by dw on 2019/8/5.
 */
public class FlowAdapter extends BaseQuickAdapter<EventFlowRes, BaseViewHolder> {
    public FlowAdapter(int layoutResId, @Nullable List<EventFlowRes> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EventFlowRes object) {

        TextView tvAction = helper.getView(R.id.tv_task_flow_action);
        tvAction.setText(object.getCurrentLink());
        if (object.getArrivalTime() != null) {
            tvAction.append("  ");
            tvAction.append(object.getArrivalTime());
        }

        helper.setText(R.id.tv_task_flow_role, object.getAssignName());

        TextView tvComment = helper.getView(R.id.tv_task_flow_comment);
        tvComment.setText("处理意见：");
        if (!TextUtils.isEmpty(object.getComments())) {
            tvComment.append(object.getComments());
        }
    }
}
