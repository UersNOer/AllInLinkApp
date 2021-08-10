package com.allinlink.platformapp.video_project.ui.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.unistrong.utils.StringsUtils;
import com.allinlink.platformapp.R;
import com.allinlink.platformapp.video_project.bean.ChannelBean;
import com.allinlink.platformapp.video_project.bean.UpChannelBus;
import com.allinlink.platformapp.video_project.contract.activity.AddCameraInfoContract;
import com.allinlink.platformapp.video_project.presenter.activity.AddCameraInfoPresenter;
import com.allinlink.platformapp.video_project.ui.adapter.AddCameraInfoAdapter;
import com.unistrong.view.base.BaseTitleActivity;
import com.unistrong.view.navigationbar.INavigationBarBackListener;
import com.unistrong.view.navigationbar.NavigationBar;
import com.unistrong.view.navigationbar.NavigationBuilder;

import java.util.List;

public class AddCameraInfoActivity extends BaseTitleActivity<AddCameraInfoPresenter> implements AddCameraInfoContract.View {
    RecyclerView ryChannel;
    AddCameraInfoAdapter addCameraInfoAdapter;

    @Override
    protected NavigationBuilder onCreateNavigationBarByBuilder(NavigationBuilder builder) {
        NavigationBuilder navigationBuilder = builder.setTitle(StringsUtils.getResourceString(R.string.activity_addcamearinfo))
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
        View view = LayoutInflater.from(this).inflate(R.layout.activity_add_camera_info, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mPresenter = new AddCameraInfoPresenter(this);
        String gid = getIntent().getStringExtra("gid");
        showLoadingDialog(true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mPresenter.getAllChannel(gid);
            }
        }).start();
        ryChannel = view.findViewById(R.id.ry_channel);
        ryChannel.setLayoutManager(new LinearLayoutManager(this));
        addCameraInfoAdapter = new AddCameraInfoAdapter(this, this.getSavedInstanceState());
        ryChannel.setAdapter(addCameraInfoAdapter);
    }


    @Override
    public void onError(String msg) {

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void setChannelData(List<ChannelBean> list) {
        addCameraInfoAdapter.setData(list);
    }

    @Override
    public void UpChannelData(UpChannelBus bus) {
        addCameraInfoAdapter.UpData(bus);
        mPresenter.upChannelInfo(addCameraInfoAdapter.getData(bus.getIndex()));
    }
}