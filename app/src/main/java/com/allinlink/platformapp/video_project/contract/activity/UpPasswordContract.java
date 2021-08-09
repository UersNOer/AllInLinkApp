package com.allinlink.platformapp.video_project.contract.activity;

import com.unistrong.common.contract.IBaseExtraView;
import com.allinlink.platformapp.video_project.bean.BaseBean;

import rx.Observable;

public interface UpPasswordContract {
    public interface View extends IBaseExtraView<UpPasswordContract.Presenter> {
        void upPasswordSuccess();
    }

    public interface Model {
        Observable<BaseBean> submitPassword(String key, String page);


    }

    interface Presenter {


    }
}
