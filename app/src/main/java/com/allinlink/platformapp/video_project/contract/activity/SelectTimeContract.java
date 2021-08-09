package com.allinlink.platformapp.video_project.contract.activity;

import com.unistrong.common.contract.IBaseExtraView;
import com.allinlink.platformapp.video_project.bean.BaseBean;
import com.allinlink.platformapp.video_project.bean.PlayBackBean;

import rx.Observable;

public interface SelectTimeContract {
    public interface View extends IBaseExtraView<SelectTimeContract.Presenter> {
        void querySuccess(PlayBackBean backBean);
    }

    public interface Model {
        Observable<BaseBean<PlayBackBean>> queryChannelById(String gid, String startTime, String endTime);


    }

    interface Presenter {


    }
}
