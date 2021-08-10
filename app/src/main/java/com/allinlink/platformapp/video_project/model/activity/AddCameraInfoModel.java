package com.allinlink.platformapp.video_project.model.activity;

import com.allinlink.platformapp.video_project.bean.BaseBean;
import com.allinlink.platformapp.video_project.bean.ChannelBean;
import com.allinlink.platformapp.video_project.contract.activity.AddCameraInfoContract;
import com.allinlink.platformapp.video_project.presenter.activity.AddCameraInfoPresenter;
import com.allinlink.platformapp.video_project.service.VideoApi;
import com.unistrong.view.base.BaseModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;

public class AddCameraInfoModel extends BaseModel<AddCameraInfoPresenter> implements AddCameraInfoContract.Model {

    public AddCameraInfoModel(AddCameraInfoPresenter presenter) {
        super(presenter);
    }

    @Override
    public Observable<BaseBean<List<ChannelBean>>> queryChannelById(String gid) {
        Map<String, Object> map = new HashMap();
        map.put("cameraInfo", gid);


        return VideoApi.getVideoService().queryChannelById(map).filter(new Func1<BaseBean<List<ChannelBean>>, Boolean>() {
            @Override
            public Boolean call(BaseBean<List<ChannelBean>> loginOutputBaseOutput) {
                return loginOutputBaseOutput != null;
            }
        });
    }
    @Override
    public Observable<BaseBean> upChannelInfo(ChannelBean gid) {
        Map<String, Object> map = new HashMap();
        map.put("gid", gid.getGid());
        map.put("channelCode", gid.getChannelCode());
        map.put("channelName", gid.getChannelName());
        map.put("cameraType", gid.getCameraType());
        map.put("jd", gid.getJd());
        map.put("wd", gid.getWd());


        return VideoApi.getVideoService().upChannelInfo(map).filter(new Func1<BaseBean, Boolean>() {
            @Override
            public Boolean call(BaseBean loginOutputBaseOutput) {
                return loginOutputBaseOutput != null;
            }
        });
    }
}
