package com.allinlink.platformapp.video_project.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.unistrong.api.maps.location.GpsMyLocationProvider;
import com.allinlink.platformapp.R;
import com.allinlink.platformapp.video_project.bean.ChannelBean;
import com.allinlink.platformapp.video_project.bean.UpChannelBus;
import com.allinlink.platformapp.video_project.ui.activity.MapActivity;
import com.allinlink.platformapp.video_project.widget.EditLinearLayout;

import java.util.ArrayList;
import java.util.List;

public class AddCameraInfoAdapter extends RecyclerView.Adapter<AddCameraInfoAdapter.ViewHolder> {
    Context context;
    Bundle savedInstanceState;
    List<ChannelBean> list = new ArrayList<>();

    public AddCameraInfoAdapter(Context context, Bundle savedInstanceState) {
        this.context = context;
        this.savedInstanceState = savedInstanceState;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_add_camera, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
//        viewHolder.setIsRecyclable(false);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChannelBean channelBean = list.get(position);
        holder.linName.setText(channelBean.getCameraType() == null ? "" : channelBean.getCameraType());
        holder.linName.setText(channelBean.getChannelName());

        if (channelBean.getJd() != null && channelBean.getWd() != null) {
            holder.tvLocation.setText(channelBean.getWd() + "," + channelBean.getJd());

        }
        holder.linCode.setText(channelBean.getChannelCode());
        holder.linLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MapActivity.class);
                if (channelBean.getJd() != null && channelBean.getWd() != null) {
                    intent.putExtra("lat", channelBean.getWd());
                    intent.putExtra("lng", channelBean.getJd());
                }
                intent.putExtra("index", position);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setData(List<ChannelBean> list) {
        this.list.clear();
        this.list.addAll(list);
        this.notifyDataSetChanged();
    }

    public void UpData(UpChannelBus bus) {
        list.get(bus.getIndex()).setJd(bus.getLatLng().longitude + "");
        list.get(bus.getIndex()).setWd(bus.getLatLng().latitude + "");
        this.notifyDataSetChanged();
    }

    public ChannelBean getData(int index) {

        return list.get(index);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView linCode, linName, linType;
        LinearLayout linLocation;
        TextView tvLocation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            linLocation = itemView.findViewById(R.id.lin_location);
            tvLocation = itemView.findViewById(R.id.tv_location);
            linCode = itemView.findViewById(R.id.lin_device_code);
            linName = itemView.findViewById(R.id.lin_device_name);
            linType = itemView.findViewById(R.id.lin_device_type);
        }
    }

}
