package com.example.android_supervisor.ui.adapter;

import android.text.TextUtils;
import android.widget.TextView;

import com.cncoderx.recyclerviewhelper.adapter.ObjectAdapter;
import com.example.android_supervisor.R;
import com.example.android_supervisor.entities.EventFlowRes;

/**
 * @author wujie
 */
public class MyTaskFlowAdapter extends ObjectAdapter<EventFlowRes> {

    public MyTaskFlowAdapter() {
        super(R.layout.item_my_task_flow);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, EventFlowRes object, int position) {
        TextView tvAction = holder.getView(R.id.tv_task_flow_action, TextView.class);
        tvAction.setText(object.getCurrentLink());
        if (object.getArrivalTime() != null) {
            tvAction.append("  ");
            tvAction.append(object.getArrivalTime());
        }

        TextView tvRole = holder.getView(R.id.tv_task_flow_role, TextView.class);
        tvRole.setText(object.getAssignName());

        TextView tvComment = holder.getView(R.id.tv_task_flow_comment, TextView.class);
        tvComment.setText("处理意见：");
        if (!TextUtils.isEmpty(object.getComments())) {
            tvComment.append(object.getComments());
        }
    }
}
