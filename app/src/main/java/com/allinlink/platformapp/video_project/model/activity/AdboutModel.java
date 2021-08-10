package com.allinlink.platformapp.video_project.model.activity;

import com.allinlink.platformapp.video_project.contract.activity.AboutContract;
import com.allinlink.platformapp.video_project.presenter.activity.AboutActivityPresenter;
import com.unistrong.view.base.BaseModel;

public class AdboutModel extends BaseModel<AboutActivityPresenter> implements AboutContract.Model {

    public AdboutModel(AboutActivityPresenter presenter) {
        super(presenter);
    }
}
