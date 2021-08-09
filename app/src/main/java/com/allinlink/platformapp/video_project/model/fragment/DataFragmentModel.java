package com.allinlink.platformapp.video_project.model.fragment;

import com.allinlink.platformapp.video_project.contract.fragment.DataFragmentContract;
import com.allinlink.platformapp.video_project.presenter.fragment.DataFragmentPresenter;
import com.unistrong.view.base.BaseModel;

/**
 * @author ltd
 * @description: 登录model
 * @date 2020-05-28
 **/
public class DataFragmentModel extends BaseModel<DataFragmentPresenter> implements DataFragmentContract.Model {

    public DataFragmentModel(DataFragmentPresenter presenter) {
        super(presenter);
    }


}
