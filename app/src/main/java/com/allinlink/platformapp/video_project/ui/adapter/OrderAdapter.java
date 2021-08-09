package com.allinlink.platformapp.video_project.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.allinlink.platformapp.R;
import com.allinlink.platformapp.video_project.bean.OrderBean;
import com.allinlink.platformapp.video_project.ui.activity.WarningActivity;
import com.unistrong.view.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private Context context;
    private ArrayList<OrderBean.DatasBean> list = new ArrayList<>();
    private ItemOnClick onClick;

    private int type = 0;

    public void setOnClick(ItemOnClick onClick) {

        this.onClick = onClick;
    }

    public OrderAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderBean.DatasBean bean = list.get(position);
        holder.tvDeviceType.setText(bean.getDeviceTypeLable());
        holder.tvFormType.setText(bean.getFormType() == null ? "维护" : bean.getFormTypeLable());
        holder.tvHardwareCode.setText(bean.getHardwareCode());
        holder.tvSn.setText(bean.getSn());
        holder.tvTaskExplain.setText(bean.getTaskExplain());
        holder.tvTenantInfo.setText(bean.getTenantName());

        holder.tvTime.setText(bean.getCreateDate());


        if (type == 1) {
            holder.ivState.setImageResource(R.drawable.img_order_0);
            holder.btClose.setVisibility(View.GONE);
        } else {
            holder.ivState.setImageResource(R.drawable.img_order_1);
            holder.btClose.setVisibility(View.VISIBLE);
        }
        holder.btClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onItemClick(bean);
            }
        });
        holder.linOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WarningActivity.class);
                intent.putExtra("data", new Gson().toJson(bean));
                context.startActivity(intent);
            }
        });
        holder.btDownLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(bean.getDocFiles())) {
                    ToastUtil.showErrorToast("当前工单没有附件");
                    return;
                }
                onClick.onItemClickdownLoad(bean.getDocFiles(), bean.getGid());
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addData(List<OrderBean.DatasBean> datas) {
        this.list.addAll(datas);
        this.notifyDataSetChanged();
    }

    public void setData(List<OrderBean.DatasBean> datas) {
        this.list.clear();
        if (null != datas) {
            this.list.addAll(datas);
        }
        this.notifyDataSetChanged();
    }

    public void setType(int type) {
        this.type = type;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSn, tvFormType, tvDeviceType, tvTenantInfo, tvHardwareCode, tvTaskExplain, tvTime;
        Button btClose, btDownLoad;
        ImageView ivState;
        LinearLayout linOrder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            linOrder = itemView.findViewById(R.id.lin_back);
            ivState = itemView.findViewById(R.id.iv_state);
            btDownLoad = itemView.findViewById(R.id.bt_download);
            btClose = itemView.findViewById(R.id.bt_close);
            tvSn = itemView.findViewById(R.id.tv_sn);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvFormType = itemView.findViewById(R.id.tv_formtype);
            tvDeviceType = itemView.findViewById(R.id.tv_devicetype);
            tvTenantInfo = itemView.findViewById(R.id.tv_tenantInfo);
            tvHardwareCode = itemView.findViewById(R.id.tv_hardwareCode);
            tvTaskExplain = itemView.findViewById(R.id.tv_taskExplain);
        }
    }

    public interface ItemOnClick {
        void onItemClick(OrderBean.DatasBean bean);

        void onItemClickdownLoad(String docFiles, String filedoc);
    }
}
