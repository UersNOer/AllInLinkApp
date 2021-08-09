package com.allinlink.platformapp.video_project.contract.activity;

import com.unistrong.common.contract.IBaseExtraView;
import com.allinlink.platformapp.video_project.bean.BaseBean;
import com.allinlink.platformapp.video_project.bean.ChannelBean;

import java.util.List;

import rx.Observable;

public interface SerachChannel {
    public interface View extends IBaseExtraView<SerachChannel.Presenter> {

    }

    public interface Model {
        Observable<BaseBean<List<ChannelBean>>> queryChannelByKey(String key);
        Observable<BaseBean> addChannelUserfavorites(ChannelBean data);

        Observable<BaseBean> removeChannelUserfavoritesById(ChannelBean data);


    }

    interface Presenter {


    }
}
