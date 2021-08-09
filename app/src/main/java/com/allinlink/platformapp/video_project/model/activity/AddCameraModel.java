package com.allinlink.platformapp.video_project.model.activity;

import com.unistrong.utils.SharedPreferencesUtil;
import com.allinlink.platformapp.video_project.bean.BaseBean;
import com.allinlink.platformapp.video_project.contract.activity.AddCameraContract;
import com.allinlink.platformapp.video_project.presenter.activity.AddCameraPresenter;
import com.allinlink.platformapp.video_project.service.VideoApi;
import com.unistrong.view.base.BaseModel;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;

public class AddCameraModel extends BaseModel<AddCameraPresenter> implements AddCameraContract.Model {

    public AddCameraModel(AddCameraPresenter presenter) {
        super(presenter);
    }

    @Override
    public Observable<BaseBean<String>> addCamera(String cameraCode, String cameraName, String tenantInfo, String ipAddress, String port, String userName, String userPsw, String standardType, String factory) {
        Map<String, Object> map = new HashMap();
        map.put("cameraCode", cameraCode);
        map.put("cameraName", cameraName);
        map.put("tenantInfo", tenantInfo);
        map.put("ipAddress", ipAddress);
        map.put("port", port);
        map.put("userName", userName);
        map.put("userPsw", userPsw);
        map.put("standardType", standardType);
        map.put("factory", factory);


        return VideoApi.getVideoService().addCamera(map).filter(new Func1<BaseBean, Boolean>() {
            @Override
            public Boolean call(BaseBean loginOutputBaseOutput) {
                return loginOutputBaseOutput != null;
            }
        });
    }

    //获取所有厂商
    @Override
    public Observable<BaseBean<String>> findChannelInfoChecked() {
        Map<String, Object> map = new HashMap();
        map.put("fzbm", "10004");
        return VideoApi.getVideoService().findChannelInfoChecked(map).filter(new Func1<BaseBean, Boolean>() {
            @Override
            public Boolean call(BaseBean loginOutputBaseOutput) {
                return loginOutputBaseOutput != null;
            }
        });
    }

    @Override
    public Observable<BaseBean<String>> findAllTenanInfoList() {
        Map<String, Object> map = new HashMap();
//        iot/api/iot.tenant.findAllTenantInfoInList
        return VideoApi.getVideoService().findAllTenanInfoList(map).filter(new Func1<BaseBean, Boolean>() {
            @Override
            public Boolean call(BaseBean loginOutputBaseOutput) {
                return loginOutputBaseOutput != null;
            }
        });
    }

    @Override
    public Observable<BaseBean<String>> findCreamManage() {
        Map<String, Object> map = new HashMap();
        map.put("fzbm", "70001");
        return VideoApi.getVideoService().findCreamManage(map).filter(new Func1<BaseBean, Boolean>() {
            @Override
            public Boolean call(BaseBean loginOutputBaseOutput) {
                return loginOutputBaseOutput != null;
            }
        });
    }
}
