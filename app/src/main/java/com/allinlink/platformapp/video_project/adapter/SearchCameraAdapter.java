package com.allinlink.platformapp.video_project.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.allinlink.platformapp.R;
import com.unistrong.utils.RxBus;
import com.allinlink.platformapp.video_project.bean.CameraBean;
import com.allinlink.platformapp.video_project.bean.ChannelBean;
import com.allinlink.platformapp.video_project.bean.CurrentBean;
import com.allinlink.platformapp.video_project.ui.activity.SelectTimeActivity;
import com.allinlink.platformapp.video_project.ui.activity.SingleVideoActivity;
import com.unistrong.view.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 搜索通道适配器
 *
 * @Author zk
 */

public class SearchCameraAdapter extends RecyclerView.Adapter<SearchCameraAdapter.ViewHolder> {
    Activity context;
    private int mode = 0;
    ArrayList<ChannelBean> list = new ArrayList<>();
    private Set<Integer> checkList = new HashSet();
    private int max;
    private int type = 0;

    private CollectOnClickListance collectOnClickListance;

    public void setCollectOnClickListance(CollectOnClickListance collectOnClickListance) {
        this.collectOnClickListance = collectOnClickListance;
    }

    public void setTpye(int type) {
        this.type = type;
    }

    public interface CollectOnClickListance {
        void collectClick(boolean collect, ChannelBean data);
    }

    public SearchCameraAdapter(Activity searchCameraActivity) {
        this.context = searchCameraActivity;
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
        if (mode == 1) {
            holder.linBack.setOnClickListener(null);
            holder.cbCheck.setVisibility(View.VISIBLE);
            holder.cbCheck.setChecked(checkList.contains(position));
            holder.cbCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!holder.cbCheck.isChecked()) {
                        holder.cbCheck.setChecked(false);
                        checkList.remove(position);
                    } else {
                        if (type != 1 && TextUtils.isEmpty(list.get(position).getMediaAddress())) {
                            Toast.makeText(context, "该通道没有视频播放", Toast.LENGTH_SHORT).show();
                            holder.cbCheck.setChecked(!holder.cbCheck.isChecked());
                            return;
                        }
                        if (checkList.size() == max) {
                            holder.cbCheck.setChecked(!holder.cbCheck.isChecked());
                            ToastUtil.showTextViewPrompt("最多选择" + max + "个");
                            return;
                        } else {
                            checkList.add(position);
                        }
                    }
                }
            });

        } else {
            holder.cbCheck.setVisibility(View.GONE);
            holder.linBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (type != 1 && TextUtils.isEmpty(list.get(position).getMediaAddress())) {
                        Toast.makeText(context, "该通道没有视频播放", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (type == 1) {
                        Intent intent = new Intent(context, SelectTimeActivity.class);
                        intent.putExtra("data", list.get(position));
                        context.startActivity(intent);
                    } else if (type == 3) {
                        Intent intent = new Intent(context, SingleVideoActivity.class);
                        intent.putExtra("data", list.get(position));
                        intent.putExtra("url", list.get(position).getMediaAddress());
                        context.startActivity(intent);
                    } else {
                        RxBus.getInstance().send(list.get(position));
                    }
                    context.finish();
                }
            });
        }
        holder.tvLocation.setText(list.get(position).getCameraGroup());
        holder.tvName.setText(list.get(position).getChannelName());
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

    public void setData(List<ChannelBean> list) {
        this.list.clear();
        this.list.addAll(list);
        this.notifyDataSetChanged();
    }

    public void addData(List<ChannelBean> datas) {
        this.list.addAll(datas);
        this.notifyDataSetChanged();
    }

    public void setMode(int i) {
        this.mode = i;
        this.checkList.clear();
        this.notifyDataSetChanged();
    }

    public void getData() {
        for (Integer index : checkList)
            RxBus.getInstance().send(list.get(index));
        context.finish();

    }

    public void setMax(int max) {
        this.max = max;
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
