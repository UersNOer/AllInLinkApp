package com.allinlink.platformapp.video_project.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.allinlink.platformapp.R;
import com.allinlink.platformapp.video_project.utils.CacheUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.ViewHolder> {
    Context context;
    Map<String, String> fileList = new HashMap<>();
    ArrayList<String> nameList = new ArrayList<>();
    String gid;

    public FileAdapter(Context ct) {
        this.context = ct;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_file, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvName.setText(nameList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    CacheUtil.downLoadFile(gid, nameList.get(position), fileList.get(nameList.get(position)), context);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    public void setData(Map<String, String> list, String gid) {
        this.gid = gid;
        this.nameList.clear();
        for (String q : list.keySet()) {
            this.nameList.add(q);
        }
        this.fileList.clear();
        this.fileList.putAll(list);
        this.notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        LinearLayout linBack;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            linBack = itemView.findViewById(R.id.lin_back);
            tvName = itemView.findViewById(R.id.tv_name);
        }
    }
}
