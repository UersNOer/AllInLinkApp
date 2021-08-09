package com.allinlink.platformapp.video_project.adapter;

import android.content.Context;
import android.text.TextUtils;

import com.allinlink.platformapp.R;
import com.unistrong.common.adapter.BaseRecyclerAdapter;
import com.unistrong.common.adapter.BaseViewHolder;

/**
 * ip host 列表适配器
 *
 * @Author ltd
 */
public class IpHostAdapter extends BaseRecyclerAdapter<String> {

    public IpHostAdapter(Context context) {
        super(context);
    }

    @Override
    public int onCreateViewLayoutID(int viewType) {
        return R.layout.item_ip_setting;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {

        String iphost = mList.get(position);
        String[] ip_host = TextUtils.split(iphost, ",");
        holder.setText(R.id.tv_ip, "地址：" + ip_host[0]);
        holder.setText(R.id.tv_host, "端口：" + ip_host[1]);

    }


}