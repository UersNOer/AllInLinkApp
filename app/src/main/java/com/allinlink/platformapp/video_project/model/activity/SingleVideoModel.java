package com.allinlink.platformapp.video_project.model.activity;

import com.allinlink.platformapp.video_project.bean.BaseBean;
import com.allinlink.platformapp.video_project.contract.activity.SingleVideoContract;
import com.allinlink.platformapp.video_project.presenter.activity.SingleVideoActivityPresenter;
import com.allinlink.platformapp.video_project.service.VideoApi;
import com.unistrong.view.base.BaseModel;

import java.util.Map;

import rx.Observable;
import rx.functions.Func1;

public class SingleVideoModel extends BaseModel<SingleVideoActivityPresenter> implements SingleVideoContract.Model {

    public SingleVideoModel(SingleVideoActivityPresenter presenter) {
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
