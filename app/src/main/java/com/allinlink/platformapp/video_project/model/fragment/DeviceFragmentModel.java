package com.allinlink.platformapp.video_project.model.fragment;

import com.allinlink.platformapp.video_project.bean.BaseBean;
import com.allinlink.platformapp.video_project.bean.MapMarkerBean;
import com.allinlink.platformapp.video_project.contract.fragment.DeviceFragmentContract;
import com.allinlink.platformapp.video_project.presenter.fragment.DeviceFragmentPresenter;
import com.allinlink.platformapp.video_project.service.VideoApi;
import com.unistrong.view.base.BaseModel;

import java.util.Map;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author ltd
 * @description: 登录model
 * @date 2020-05-28
 **/
public class DeviceFragmentModel extends BaseModel<DeviceFragmentPresenter> implements DeviceFragmentContract.Model {

    public DeviceFragmentModel(DeviceFragmentPresenter presenter) {
        super(presenter);
    }


    @Override
    public Observable<BaseBean<String>> cloundControl(Map<String, Object> map) {
        return VideoApi.getVideoService().cloundControl(map).filter(new Func1<BaseBean, Boolean>() {
            @Override
            public Boolean call(BaseBean loginOutputBaseOutput) {
                return loginOutputBaseOutput != null;
            }
        });
    }
}
