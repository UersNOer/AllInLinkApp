package com.allinlink.platformapp.video_project.model.activity;

import com.unistrong.network.BaseOutput;
import com.unistrong.utils.SharedPreferencesUtil;
import com.allinlink.platformapp.video_project.bean.BaseBean;
import com.allinlink.platformapp.video_project.bean.LoginOutput;
import com.allinlink.platformapp.video_project.contract.activity.UserDatumActivityContract;
import com.allinlink.platformapp.video_project.presenter.activity.UserDatumActivityPresenter;
import com.allinlink.platformapp.video_project.service.VideoApi;
import com.allinlink.platformapp.video_project.utils.UserCache;
import com.unistrong.view.base.BaseModel;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author ltd
 * @description: 登录model
 * @date 2020-05-28
 **/
public class UserDatumModel extends BaseModel<UserDatumActivityPresenter> implements UserDatumActivityContract.Model {

    public UserDatumModel(UserDatumActivityPresenter presenter) {
        super(presenter);
    }

    @Override
    public Observable<BaseBean<LoginOutput>> getUserInfo() {
        HashMap<String, Object> map = new HashMap<>();
//        map.put("token", SharedPreferencesUtil.getInstance().getToken());
        return VideoApi.getVideoService().getUserInfo(map).filter(new Func1<BaseBean, Boolean>() {
            @Override
            public Boolean call(BaseBean loginOutputBaseOutput) {
                return loginOutputBaseOutput != null;
            }
        });
    }

    @Override
    public Observable<BaseBean> upUserInfo(String nickName, String telephone, String email) {
        Map<String, Object> map = new HashMap();
        map.put("gid", SharedPreferencesUtil.getInstance().getUserId());
        map.put("passportName", "");
        map.put("passportPsw", "");
        map.put("tenantInfo", "");
        map.put("roleId", "");
        map.put("stationInfo", "");
        map.put("nickName", nickName);
        map.put("telephone", telephone);
        map.put("email", email);


        return VideoApi.getVideoService().upUserInfo(map).filter(new Func1<BaseBean, Boolean>() {
            @Override
            public Boolean call(BaseBean loginOutputBaseOutput) {
                return loginOutputBaseOutput != null;
            }
        });
    }
}
