package com.allinlink.platformapp.video_project.contract.activity;

import com.unistrong.common.contract.IBaseExtraView;
import com.allinlink.platformapp.video_project.bean.BaseBean;

import java.util.Map;

import rx.Observable;

public interface SingleVideoContract {
    public interface View extends IBaseExtraView<SingleVideoContract.Presenter> {

    }

    public interface Model {
        Observable<BaseBean<String>> cloundControl(Map<String, Object> map);


    }

    interface Presenter {


    }
}
