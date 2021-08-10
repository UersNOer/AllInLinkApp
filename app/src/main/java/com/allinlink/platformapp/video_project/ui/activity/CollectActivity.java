package com.allinlink.platformapp.video_project.ui.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.unistrong.utils.StringsUtils;
import com.allinlink.platformapp.R;
import com.allinlink.platformapp.video_project.adapter.SearchCameraAdapter;
import com.allinlink.platformapp.video_project.bean.ChannelBean;
import com.allinlink.platformapp.video_project.contract.activity.CollectContract;
import com.allinlink.platformapp.video_project.presenter.activity.CollectPresenter;
import com.unistrong.view.base.BaseTitleActivity;
import com.unistrong.view.navigationbar.INavigationBarBackListener;
import com.unistrong.view.navigationbar.NavigationBar;
import com.unistrong.view.navigationbar.NavigationBuilder;

import java.util.List;


public class CollectActivity extends BaseTitleActivity<CollectPresenter> implements CollectContract.View, View.OnClickListener, SearchCameraAdapter.CollectOnClickListance {

    private SearchCameraAdapter adapter;
    RecyclerView xRecyclerView;
    TextView tvMode1, tvMode2, tvSure;
    RelativeLayout rlBack;

    @Override
    protected NavigationBuilder onCreateNavigationBarByBuilder(NavigationBuilder builder) {
        NavigationBuilder navigationBuilder = builder.setTitle("收藏列表")
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
        View view = LayoutInflater.from(this).inflate(R.layout.activity_collect, null);
        mPresenter = new CollectPresenter(this);
        mPresenter.queryCollectChannel();
        initView(view);
        return view;
    }

    private void initView(View view) {
        xRecyclerView = view.findViewById(R.id.ry_data);
        xRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SearchCameraAdapter(this);
        adapter.setCollectOnClickListance(this);
        adapter.setMax(getIntent().getIntExtra("max", 0));
        xRecyclerView.setAdapter(adapter);
        rlBack = view.findViewById(R.id.rl_back);
        tvMode1 = view.findViewById(R.id.tv_mode_1);
        tvMode2 = view.findViewById(R.id.tv_mode_2);
        tvSure = view.findViewById(R.id.tv_sure);
        tvMode1.setOnClickListener(this);
        tvMode2.setOnClickListener(this);
        tvSure.setOnClickListener(this);

    }

    @Override
    public void onError(String msg) {

    }

    @Override
    public Context getContext() {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_mode_1:
                tvMode1.setVisibility(View.GONE);
                tvMode2.setVisibility(View.VISIBLE);
                tvSure.setVisibility(View.VISIBLE);
                adapter.setMode(1);
                break;
            case R.id.tv_mode_2:
                tvMode1.setVisibility(View.VISIBLE);
                tvMode2.setVisibility(View.GONE);
                tvSure.setVisibility(View.GONE);
                adapter.setMode(0);
                break;
            case R.id.tv_sure:
                adapter.getData();
                break;
        }
    }

    @Override
    public void setChannelData(List<ChannelBean> result) {
        if (result.size() > 0) {
            rlBack.setVisibility(View.VISIBLE);
        }
        adapter.setData(result);
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