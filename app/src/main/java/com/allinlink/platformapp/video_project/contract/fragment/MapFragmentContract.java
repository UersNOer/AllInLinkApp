package com.allinlink.platformapp.video_project.contract.fragment;


import com.unistrong.common.contract.IBaseExtraView;
import com.unistrong.network.BaseOutput;
import com.allinlink.platformapp.video_project.bean.BaseBean;
import com.allinlink.platformapp.video_project.bean.CameraBean;
import com.allinlink.platformapp.video_project.bean.ChannelBean;
import com.allinlink.platformapp.video_project.bean.LoginOutput;
import com.allinlink.platformapp.video_project.bean.MapMarkerBean;

import java.util.List;

import rx.Observable;

/**
 * Description:密码登录
 * Created by ltd ON 2020/4/21
 * Phone:18600920091
 * Email:td.liu@unistrong.com
 */
public interface MapFragmentContract {

    interface View extends IBaseExtraView<Presenter> {
        void setMapMarker(MapMarkerBean markers);

        void setCameraData(List<ChannelBean> result);
    }

    interface Model {
        /**
         * rx 密码登录
         */
        Observable<BaseBean> addUMengToken(String token);

        Observable<BaseBean<MapMarkerBean>> getMapMarker();



        Observable<BaseBean<List<ChannelBean>>>getAllCamera();

        Observable<BaseBean> addChannelUserfavorites(ChannelBean data);

        Observable<BaseBean> removeChannelUserfavoritesById(ChannelBean data);
    }

    interface Presenter {

        void getMapMarker();
    }
}