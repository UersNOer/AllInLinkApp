package com.allinlink.platformapp.video_project.contract.activity;

import com.unistrong.common.contract.IBaseExtraView;
import com.allinlink.platformapp.video_project.bean.BaseBean;
import com.allinlink.platformapp.video_project.bean.CameraBean;

import rx.Observable;

public interface AddCameraContract {
    public interface View extends IBaseExtraView<AddCameraContract.Presenter> {
        void addSuccess(String gid);
    }

    public interface Model {

        Observable<BaseBean<String>> addCamera(String cameraCode, String cameraName, String tenantInfo, String ipAddress, String port, String userName, String userPsw, String standardType, String factory);
        Observable<BaseBean<String>> findChannelInfoChecked();
        Observable<BaseBean<String>> findAllTenanInfoList();
        Observable<BaseBean<String>> findCreamManage();

    }

    interface Presenter {


    }
}
