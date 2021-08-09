package com.allinlink.platformapp.video_project.contract.activity;

import com.unistrong.common.contract.IBaseExtraView;
import com.allinlink.platformapp.video_project.bean.BaseBean;
import com.allinlink.platformapp.video_project.bean.CameraBean;
import com.allinlink.platformapp.video_project.bean.ChannelBean;

import java.util.List;

import rx.Observable;

public interface SearchCameraContract {
    interface View extends IBaseExtraView<SearchCameraContract.Presenter> {
        void setCameraData(List<ChannelBean> bean);
    }

    public interface Model {
        Observable<BaseBean<List<ChannelBean>>> getAllCamera(String key, int page);
        Observable<BaseBean> addChannelUserfavorites(ChannelBean data);

        Observable<BaseBean> removeChannelUserfavoritesById(ChannelBean data);

    }


    interface Presenter {


    }
}
