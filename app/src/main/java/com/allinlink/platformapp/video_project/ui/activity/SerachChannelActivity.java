package com.allinlink.platformapp.video_project.ui.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.unistrong.utils.SharedPreferencesUtil;
import com.allinlink.platformapp.R;
import com.allinlink.platformapp.video_project.adapter.SearchCameraAdapter;
import com.allinlink.platformapp.video_project.bean.ChannelBean;
import com.allinlink.platformapp.video_project.config.Configs;
import com.allinlink.platformapp.video_project.contract.activity.SerachVideoContract;
import com.allinlink.platformapp.video_project.presenter.activity.SerachChannelPresenter;
import com.allinlink.platformapp.video_project.ui.adapter.UserCameraAdapter;
import com.allinlink.platformapp.video_project.utils.LogUtil;
import com.unistrong.view.base.BaseActivity;
import com.unistrong.view.base.BaseTitleActivity;
import com.unistrong.view.navigationbar.INavigationBarBackListener;
import com.unistrong.view.navigationbar.NavigationBar;
import com.unistrong.view.navigationbar.NavigationBuilder;

import java.util.ArrayList;
import java.util.List;

public class SerachChannelActivity extends BaseTitleActivity<SerachChannelPresenter> implements TextWatcher, SerachVideoContract.View, SearchCameraAdapter.CollectOnClickListance {
    LinearLayout linHistory;
    UserCameraAdapter historyAdapter;
    UserCameraAdapter serachAdapter;
    EditText edSerach;

    RecyclerView ryChannelSerach;

    @Override
    protected NavigationBuilder onCreateNavigationBarByBuilder(NavigationBuilder builder) {
        NavigationBuilder navigationBuilder = builder.setTitle("搜索通道")
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
        View view = LayoutInflater.from(this).inflate(R.layout.activity_serach_channel, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mPresenter = new SerachChannelPresenter(this);
        edSerach = view.findViewById(R.id.ed_search);
        edSerach.addTextChangedListener(this);
        ryChannelSerach = view.findViewById(R.id.ry_channel_serach);
        ryChannelSerach.setLayoutManager(new LinearLayoutManager(this));
        serachAdapter = new UserCameraAdapter(this);
        serachAdapter.setCollectOnClickListance(this);
        ryChannelSerach.setAdapter(serachAdapter);
        ArrayList<ChannelBean> historyData = getHistoryData();
        linHistory = view.findViewById(R.id.lin_hostory);
        if (historyData != null && historyData.size() != 0) {

            linHistory.setVisibility(View.VISIBLE);
            RecyclerView recyclerView = view.findViewById(R.id.ry_history);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            historyAdapter = new UserCameraAdapter(this);
            historyAdapter.setCollectOnClickListance(this);

            recyclerView.setAdapter(historyAdapter);
            historyAdapter.setList(historyData);
        }
    }

    private ArrayList<ChannelBean> getHistoryData() {
        ArrayList<ChannelBean> list = (ArrayList<ChannelBean>) SharedPreferencesUtil.getInstance().getObject(Configs.SERACH_CHANNEL_HISTORY, List.class);
        return list;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!TextUtils.isEmpty(s.toString())) {
            linHistory.setVisibility(View.GONE);
            ryChannelSerach.setVisibility(View.VISIBLE);
            mPresenter.queryChannel(s.toString());
        } else {
            linHistory.setVisibility(View.VISIBLE);
            serachAdapter.clearData();
            ryChannelSerach.setVisibility(View.GONE);
        }

    }

    @Override
    public void setChannelData(List<ChannelBean> list) {
        serachAdapter.setList(list);
    }

    @Override
    public void onError(String msg) {
        if (msg == null)
            finish();
    }

    @Override
    public Context getContext() {
        return this;
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