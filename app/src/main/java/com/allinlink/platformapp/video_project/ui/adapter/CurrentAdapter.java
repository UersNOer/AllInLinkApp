package com.allinlink.platformapp.video_project.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.allinlink.platformapp.R;
import com.allinlink.platformapp.video_project.bean.CurrentBean;
import com.unistrong.view.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class CurrentAdapter extends RecyclerView.Adapter<CurrentAdapter.ViewHolder> {
    private int mode = 0;
    private Context context;
    private ArrayList<CurrentBean> data = new ArrayList<>();
    private Set checkList = new HashSet();

    public CurrentAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_current_video, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (mode == 1) {
            holder.cbCheck.setVisibility(View.VISIBLE);
            holder.cbCheck.setChecked(checkList.contains(position));
            holder.cbCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!holder.cbCheck.isChecked()) {
                        holder.cbCheck.setChecked(false);
                        checkList.remove(position);
                    } else {
                        if (checkList.size() == 4) {
                            holder.cbCheck.setChecked(!holder.cbCheck.isChecked());
                            ToastUtil.showTextViewPrompt("最多选择4个");
                            return;
                        } else {
                            checkList.add(position);
                        }
                    }
                }
            });

        } else
            holder.cbCheck.setVisibility(View.GONE);
        holder.tvName.setText(data.get(position).name);
        holder.linBack.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                checkList.add(position);
                mode = 1;
                CurrentAdapter.this.notifyDataSetChanged();
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(ArrayList<CurrentBean> list) {
        this.data.clear();
        this.data.addAll(list);
        this.notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private CheckBox cbCheck;
        private LinearLayout linBack;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            linBack = itemView.findViewById(R.id.lin_back);
            cbCheck = itemView.findViewById(R.id.cb_check);
            tvName = itemView.findViewById(R.id.tv_name);
        }
    }
}
