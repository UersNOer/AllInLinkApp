package com.example.android_supervisor.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_supervisor.R;
import com.example.android_supervisor.entities.DayNoticeModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DayNoticeAdapter extends RecyclerView.Adapter<DayNoticeAdapter.VH> {

    private List<DayNoticeModel>list;
    private Context mContext;

    public DayNoticeAdapter(List<DayNoticeModel> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new VH(LayoutInflater.from(mContext).inflate(R.layout.item_daynotice_list,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH vh, int position) {
        DayNoticeModel dayNoticeModel = list.get(position);
        if (dayNoticeModel.getFillState() == 1){
            vh.tvStatus.setText("(已提交)");
            vh.tvSubmit.setVisibility(View.GONE);
        }else{
            vh.tvStatus.setText("(已保存)");
            vh.tvSubmit.setVisibility(View.VISIBLE);
        }
        vh.tvContent.setText(dayNoticeModel.getFillContent());
        vh.tvDay.setText(dayNoticeModel.getCreateTime());
        if (onItemClickListener != null){
            vh.itemView.setTag(position);
            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(view, (int) view.getTag());
                }
            });
            vh.tvSubmit.setTag(position);
            vh.tvSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onSubmitClick(view, (int) view.getTag());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class VH extends RecyclerView.ViewHolder{

        @BindView(R.id.tvDay)
        TextView tvDay;
        @BindView(R.id.tvStatus)
        TextView tvStatus;
        @BindView(R.id.tvContent)
        TextView tvContent;
        @BindView(R.id.tvSubmit)
        TextView tvSubmit;

        public VH(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
    public OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(View view,int position);
        void onSubmitClick(View view,int position);
    }
}
