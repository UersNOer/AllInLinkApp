package com.example.android_supervisor.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android_supervisor.R;
import com.example.android_supervisor.entities.LeaveRecordModel;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LeaveRecordAdapter extends RecyclerView.Adapter<LeaveRecordAdapter.VH> {


    private Context mContext;
    private List<LeaveRecordModel>list;
    private final SimpleDateFormat dateFormat;

    public LeaveRecordAdapter(Context mContext, List<LeaveRecordModel> list) {
        this.mContext = mContext;
        this.list = list;
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new VH(LayoutInflater.from(mContext).inflate(R.layout.item_leaverecord,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH vh, int position) {
        LeaveRecordModel model = list.get(position);
        vh.tvStartTime.setText(dateFormat.format(model.getLeaveStartTime()));
        vh.tvEndTime.setText(dateFormat.format(model.getLeaveEndTime()));
//        0:待审批；1:审批通过；2:审批未通过)
        vh.tvStatus.setTextColor(Color.parseColor("#333333"));
        if ("0".equals(model.getLeaveState())){
            vh.tvStatus.setText("待审批");
        }else if ("1".equals(model.getLeaveState())){
            vh.tvStatus.setText("审批通过");
        }else {
            vh.tvStatus.setText("审批未通过");
            vh.tvStatus.setTextColor(Color.parseColor("#ff0000"));
        }

        if ("0".equals(model.getLeaveType())){
            vh.tvType.setText("事假");
        }else if ("1".equals(model.getLeaveType())){
            vh.tvType.setText("病假");
        }else if ("2".equals(model.getLeaveType())){
            vh.tvType.setText("年假");
        }else {
//            、、3
            vh.tvType.setText("其他");

        }
        vh.itemView.setTag(position);
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null){
                    onItemClickListener.onItemClick(v,(int)v.getTag());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null?0:list.size();
    }

    public class VH extends RecyclerView.ViewHolder{

        @BindView(R.id.tvStatus)
        TextView tvStatus;
        @BindView(R.id.tvType)
        TextView tvType;
        @BindView(R.id.tvStartTime)
        TextView tvStartTime;
        @BindView(R.id.tvEndTime)
        TextView tvEndTime;

        public VH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }
}
