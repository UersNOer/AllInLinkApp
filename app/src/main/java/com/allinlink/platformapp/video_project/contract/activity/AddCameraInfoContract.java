package com.allinlink.platformapp.video_project.contract.activity;

import com.unistrong.common.contract.IBaseExtraView;
import com.allinlink.platformapp.video_project.bean.BaseBean;
import com.allinlink.platformapp.video_project.bean.ChannelBean;
import com.allinlink.platformapp.video_project.bean.UpChannelBus;

import java.util.List;

import rx.Observable;

public interface AddCameraInfoContract {
    interface View extends IBaseExtraView<AddCameraInfoContract.Presenter> {
        void setChannelData(List<ChannelBean> list);
        void UpChannelData(UpChannelBus bus);
    }

    public interface Model {
        Observable<BaseBean<List<ChannelBean>>> queryChannelById(String gid);
        Observable<BaseBean> upChannelInfo(ChannelBean gid);

    }

    interface Presenter {


    }
}
