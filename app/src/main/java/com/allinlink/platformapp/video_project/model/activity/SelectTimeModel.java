package com.allinlink.platformapp.video_project.model.activity;

import com.allinlink.platformapp.video_project.bean.BaseBean;
import com.allinlink.platformapp.video_project.bean.ChannelBean;
import com.allinlink.platformapp.video_project.bean.PlayBackBean;
import com.allinlink.platformapp.video_project.contract.activity.SelectTimeContract;
import com.allinlink.platformapp.video_project.presenter.activity.SelectTimePresenter;
import com.allinlink.platformapp.video_project.service.VideoApi;
import com.unistrong.view.base.BaseModel;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;

public class SelectTimeModel extends BaseModel<SelectTimePresenter> implements SelectTimeContract.Model {

    public SelectTimeModel(SelectTimePresenter presenter) {
        super(presenter);
    }

    @Override
    public Observable<BaseBean<PlayBackBean>> queryChannelById(String gid, String startTime, String endTime) {

        Map<String, Object> map = new HashMap();
        map.put("ids", "'"+gid+"'");
        map.put("startTime", startTime);
        map.put("endTime", endTime);


        return VideoApi.getVideoService().queryChannelByTime(map).filter(new Func1<BaseBean<PlayBackBean>, Boolean>() {
            @Override
            public Boolean call(BaseBean<PlayBackBean> loginOutputBaseOutput) {
                return loginOutputBaseOutput != null;
            }
        });


    }
}
