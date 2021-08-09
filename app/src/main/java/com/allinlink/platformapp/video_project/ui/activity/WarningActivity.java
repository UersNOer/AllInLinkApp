package com.allinlink.platformapp.video_project.ui.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.unistrong.utils.StringsUtils;
import com.allinlink.platformapp.R;
import com.allinlink.platformapp.video_project.bean.OrderBean;
import com.allinlink.platformapp.video_project.bean.WarningBean;
import com.allinlink.platformapp.video_project.ui.adapter.FileAdapter;
import com.allinlink.platformapp.video_project.utils.LogUtil;
import com.allinlink.platformapp.video_project.utils.StringUtil;
import com.unistrong.view.base.BaseTitleActivity;
import com.unistrong.view.navigationbar.INavigationBarBackListener;
import com.unistrong.view.navigationbar.NavigationBar;
import com.unistrong.view.navigationbar.NavigationBuilder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;

public class WarningActivity extends BaseTitleActivity {

    OrderBean.DatasBean warningBean;
    RecyclerView ryData;
    FileAdapter adapter;

    @Override
    protected NavigationBuilder onCreateNavigationBarByBuilder(NavigationBuilder builder) {
        NavigationBuilder navigationBuilder = builder.setTitle("工单信息")
                .setType(NavigationBar.BACK_AND_TITLE).setNavigationBarListener(new INavigationBarBackListener() {
                    @Override
                    public void onBackClick() {
                        finish();
                    }
                });
        return navigationBuilder;
    }

    @Override
    public View onCreateContentView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_warning, null);
        init(view);
        return view;
    }

    private void init(View view) {
        Bundle bun = getIntent().getExtras();
        if (bun != null) {
            Set<String> keySet = bun.keySet();
            for (String c : keySet) {
                String value = bun.getString(c);
                warningBean = new Gson().fromJson(value, OrderBean.DatasBean.class);
            }
        }

        TextView tvDeviceType = view.findViewById(R.id.tv_deviceType);
        TextView tvFromType = view.findViewById(R.id.tv_fromtype);
        TextView tvIP = view.findViewById(R.id.tv_ip);
        TextView tvTaskExplain = view.findViewById(R.id.tv_taskExplain);
        TextView tvTime = view.findViewById(R.id.tv_time);
        TextView tvDesc = view.findViewById(R.id.tv_desc);
        TextView tvDoc = view.findViewById(R.id.tv_doc);
        ryData = view.findViewById(R.id.ry_data);
        ryData.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FileAdapter(this);
        ryData.setAdapter(adapter);
        tvDeviceType.setText(StringUtil.isEmpty(warningBean.getDeviceTypeLable()) ? warningBean.getDeviceType() : warningBean.getDeviceTypeLable());
        tvFromType.setText(warningBean.getFormType());
        tvIP.setText(warningBean.getAddress());
        tvTaskExplain.setText(warningBean.getTaskExplain());
        tvTime.setText(warningBean.getCreateDate());
        tvDesc.setText(warningBean.getTaskExplain().length() > 10 ? warningBean.getTaskExplain().substring(0, 10) + "..." : warningBean.getTaskExplain());
        if (!TextUtils.isEmpty(warningBean.getDocFiles())) {
            adapter.setData(new Gson().fromJson(warningBean.getDocFiles(), Map.class), warningBean.getGid());
        } else {
            tvDoc.setVisibility(View.GONE);
            ryData.setVisibility(View.GONE);
        }

    }

    public static String getDateToString(long milSecond, String pattern) {
        Date date = new Date(milSecond);
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

}