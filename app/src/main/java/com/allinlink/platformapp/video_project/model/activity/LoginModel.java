package com.allinlink.platformapp.video_project.model.activity;

import com.unistrong.utils.MD5;
import com.allinlink.platformapp.video_project.bean.BaseBean;
import com.allinlink.platformapp.video_project.bean.LoginOutput;
import com.allinlink.platformapp.video_project.contract.activity.LoginByPasswordContract;
import com.allinlink.platformapp.video_project.presenter.activity.LoginByPasswordPresenter;
import com.allinlink.platformapp.video_project.service.VideoApi;
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
public class LoginModel extends BaseModel<LoginByPasswordPresenter> implements LoginByPasswordContract.Model {

    public LoginModel(LoginByPasswordPresenter presenter) {
        super(presenter);
    }


    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     * @return
     */
    @Override
    public Observable<BaseBean<LoginOutput>> login(String username, String password) {
        Map<String, Object> map = new HashMap();
        map.put("passportName", username);
        map.put("passportPsw", MD5.getMD5Code(password).toUpperCase());

        return VideoApi.getVideoService().login(map).filter(new Func1<BaseBean<LoginOutput>, Boolean>() {
            @Override
            public Boolean call(BaseBean<LoginOutput> loginOutputBaseBean) {
                return loginOutputBaseBean != null;
            }
        });
    }

}
