package com.allinlink.platformapp.video_project.contract.activity;

import com.unistrong.common.contract.IBaseExtraView;
import com.allinlink.platformapp.video_project.bean.BaseBean;
import com.allinlink.platformapp.video_project.bean.LoginOutput;

import rx.Observable;

public interface UserDatumActivityContract {
    public interface View extends IBaseExtraView<Presenter> {
        void upInfoSccess();

    }

    public interface Model {
        Observable<BaseBean> upUserInfo(String nickName, String telephone, String email) ;
        Observable<BaseBean<LoginOutput>> getUserInfo();
    }

    interface Presenter {


    }
}
