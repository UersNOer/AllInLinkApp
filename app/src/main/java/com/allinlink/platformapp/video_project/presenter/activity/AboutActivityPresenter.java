package com.allinlink.platformapp.video_project.presenter.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.allinlink.platformapp.video_project.contract.activity.AboutContract;
import com.allinlink.platformapp.video_project.model.activity.AdboutModel;
import com.unistrong.view.base.BasePresenter;

public class AboutActivityPresenter extends BasePresenter<AboutContract.View, AdboutModel> {
    public AboutActivityPresenter(AboutContract.View view) {
        super(view);
    }

    public String getVersionCode() {
        PackageManager packageManager = mView.getContext().getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(
                    mView.getContext().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo.versionName;
    }

    @Override
    public AdboutModel initModel() {
        return null;
    }
}
