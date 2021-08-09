package com.allinlink.platformapp.video_project.contract.fragment;

import com.unistrong.common.contract.IBaseExtraView;
import com.allinlink.platformapp.video_project.bean.BaseBean;
import com.allinlink.platformapp.video_project.bean.CameraBean;
import com.allinlink.platformapp.video_project.bean.ChannelBean;
import com.allinlink.platformapp.video_project.bean.MapMarkerBean;

import java.util.Map;

import rx.Observable;

public class DeviceFragmentContract {

    public interface View extends IBaseExtraView<Presenter> {
        void selctCameraData(ChannelBean datasBean);
    }

    public interface Model {

        Observable<BaseBean<String>> cloundControl(Map<String, Object> map);

    }

    interface Presenter {


    }
}
