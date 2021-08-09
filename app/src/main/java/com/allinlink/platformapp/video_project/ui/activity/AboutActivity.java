package com.allinlink.platformapp.video_project.ui.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.unistrong.utils.StringsUtils;
import com.allinlink.platformapp.R;
import com.allinlink.platformapp.video_project.contract.activity.AboutContract;
import com.allinlink.platformapp.video_project.presenter.activity.AboutActivityPresenter;
import com.allinlink.platformapp.video_project.utils.LogUtil;
import com.unistrong.view.base.BaseTitleActivity;
import com.unistrong.view.navigationbar.INavigationBarBackListener;
import com.unistrong.view.navigationbar.NavigationBar;
import com.unistrong.view.navigationbar.NavigationBuilder;

public class AboutActivity extends BaseTitleActivity<AboutActivityPresenter> implements AboutContract.View {

    @Override
    protected NavigationBuilder onCreateNavigationBarByBuilder(NavigationBuilder builder) {
        NavigationBuilder navigationBuilder = builder.setTitle(StringsUtils.getResourceString(R.string.activity_about))
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
        View view = LayoutInflater.from(this).inflate(R.layout.activity_about, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mPresenter = new AboutActivityPresenter(this);
        TextView tvVersio = view.findViewById(R.id.tv_version);
        tvVersio.setText("v" + mPresenter.getVersionCode());
    }

    @Override
    public void onError(String msg) {

    }

    @Override
    public Context getContext() {
        return this;
    }
}