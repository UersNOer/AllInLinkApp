package com.allinlink.platformapp.video_project.contract.activity;


import com.unistrong.common.contract.IBaseExtraView;
import com.unistrong.network.BaseOutput;
import com.allinlink.platformapp.video_project.bean.BaseBean;
import com.allinlink.platformapp.video_project.bean.LoginOutput;

import rx.Observable;

/**
 * Description:密码登录
 * Created by ltd ON 2020/4/21
 * Phone:18600920091
 * Email:td.liu@unistrong.com
 */
public interface LoginByPasswordContract {

    interface View extends IBaseExtraView<Presenter> {

        /**
         * 登录成功
         */
        void onLoginSuccess(BaseBean<LoginOutput> loginOutput);

    }

    interface Model {


        /**
         * rx 密码登录
         *
         * @param username 用户名
         * @param password 密码
         */
        Observable<BaseBean<LoginOutput>> login(String username, String password);


    }

    interface Presenter {


        /**
         * 登录
         *
         * @param username 用户名
         * @param password 密码
         */
        void login(String username, String password);

    }
}