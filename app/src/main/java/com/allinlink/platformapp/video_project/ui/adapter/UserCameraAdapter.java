package com.allinlink.platformapp.video_project.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.allinlink.platformapp.R;
import com.allinlink.platformapp.video_project.adapter.SearchCameraAdapter;
import com.allinlink.platformapp.video_project.bean.ChannelBean;
import com.allinlink.platformapp.video_project.ui.activity.SelectTimeActivity;

import java.util.ArrayList;
import java.util.List;

//用户可看的摄像头列表
public class UserCameraAdapter extends RecyclerView.Adapter<UserCameraAdapter.ViewHolder> {
    Context context;
    private ArrayList<ChannelBean> list = new ArrayList<>();
    private SearchCameraAdapter.CollectOnClickListance collectOnClickListance;

    public void setCollectOnClickListance(SearchCameraAdapter.CollectOnClickListance collectOnClickListance) {
        this.collectOnClickListance = collectOnClickListance;
    }

    public interface CollectOnClickListance {
        void collectClick(boolean collect, ChannelBean data);
    }

    public void setList(List<ChannelBean> list) {
        this.list.clear();
        this.list.addAll(list);
        this.notifyDataSetChanged();
    }

    public UserCameraAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_serarch_camera, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChannelBean channelBean = list.get(position);
        holder.tvName.setText(channelBean.getChannelName());
        holder.tvLocation.setText(TextUtils.isEmpty(channelBean.getCameraGroup()) ? "" : channelBean.getCameraGroup());
        holder.tvLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SelectTimeActivity.class);
                intent.putExtra("data", list.get(position));
                context.startActivity(intent);
            }
        });
        holder.cbCollect.setChecked(list.get(position).getFavoriteFlag() == 1);
        holder.cbCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectOnClickListance.collectClick(holder.cbCollect.isChecked(), list.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void clearData() {
        list.clear();
        this.notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private CheckBox cbCheck;
        private LinearLayout linBack;
        private TextView tvLocation;
        private CheckBox cbCollect;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cbCollect = itemView.findViewById(R.id.cb_collect);
            linBack = itemView.findViewById(R.id.lin_back);
            cbCheck = itemView.findViewById(R.id.cb_check);
            tvName = itemView.findViewById(R.id.tv_name);
            tvLocation = itemView.findViewById(R.id.tv_location);
        }
    }
}
