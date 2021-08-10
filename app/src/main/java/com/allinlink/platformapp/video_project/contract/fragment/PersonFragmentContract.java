package com.allinlink.platformapp.video_project.contract.fragment;

import com.unistrong.common.contract.IBaseExtraView;
import com.allinlink.platformapp.video_project.bean.BaseBean;
import com.allinlink.platformapp.video_project.bean.LoginOutput;

import rx.Observable;

public class PersonFragmentContract {

    public interface View extends IBaseExtraView<Presenter> {
        void setUserInfo(LoginOutput loginOutput);
    }

    public interface Model {
        Observable<BaseBean<LoginOutput>> getUserInfo();


    }

    interface Presenter {


    }
}
