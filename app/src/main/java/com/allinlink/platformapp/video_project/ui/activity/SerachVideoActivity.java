package com.allinlink.platformapp.video_project.ui.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import com.unistrong.utils.StringsUtils;
import com.allinlink.platformapp.R;
import com.allinlink.platformapp.video_project.adapter.SearchCameraAdapter;
import com.allinlink.platformapp.video_project.bean.ChannelBean;
import com.allinlink.platformapp.video_project.contract.activity.SerachVideoContract;
import com.allinlink.platformapp.video_project.presenter.activity.SerachVidePresenter;
import com.unistrong.view.base.BaseTitleActivity;
import com.unistrong.view.navigationbar.INavigationBarBackListener;
import com.unistrong.view.navigationbar.NavigationBar;
import com.unistrong.view.navigationbar.NavigationBuilder;

import java.util.List;

public class SerachVideoActivity extends BaseTitleActivity<SerachVidePresenter> implements SerachVideoContract.View, View.OnClickListener, SearchCameraAdapter.CollectOnClickListance {

    RecyclerView ryData;
    SearchCameraAdapter adapter;

    @Override
    protected NavigationBuilder onCreateNavigationBarByBuilder(NavigationBuilder builder) {
        NavigationBuilder navigationBuilder = builder.setTitle("通道列表")
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
        View view = LayoutInflater.from(this).inflate(R.layout.activity_serach_video, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        view.findViewById(R.id.rl_serach).setOnClickListener(this);
        mPresenter = new SerachVidePresenter(this);
        mPresenter.getAllChannel();
        ryData = view.findViewById(R.id.ry_data);
        ryData.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SearchCameraAdapter(this);
        adapter.setTpye(1);
        adapter.setCollectOnClickListance(this);
        ryData.setAdapter(adapter);

    }

    @Override
    public void onError(String msg) {
        if (null == msg)
            finish();

    }

    @Override
    public Context getContext() {
        return null;
    }

    @Override
    public void setChannelData(List<ChannelBean> list) {
        adapter.addData(list);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_serach:
                startActivity(new Intent(this, SerachChannelActivity.class));
                break;
        }
    }

    @Override
    public void collectClick(boolean collect, ChannelBean data) {
        if (collect) {
            mPresenter.addChannelUserfavorites(data);
        } else {
            mPresenter.removeChannelUserfavoritesById(data);

        }
    }
}