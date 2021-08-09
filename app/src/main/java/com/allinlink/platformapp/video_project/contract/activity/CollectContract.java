package com.allinlink.platformapp.video_project.contract.activity;

import com.unistrong.common.contract.IBaseExtraView;
import com.allinlink.platformapp.video_project.bean.BaseBean;
import com.allinlink.platformapp.video_project.bean.ChannelBean;

import java.util.List;

import rx.Observable;

public interface CollectContract {
    public interface View extends IBaseExtraView<CollectContract.Presenter> {

        void setChannelData(List<ChannelBean> result);
    }

    public interface Model {
        Observable<BaseBean> addChannelUserfavorites(ChannelBean data);

        Observable<BaseBean> removeChannelUserfavoritesById(ChannelBean data);


        Observable<BaseBean<List<ChannelBean>>> queryCollect();
    }

    interface Presenter {


    }
}
