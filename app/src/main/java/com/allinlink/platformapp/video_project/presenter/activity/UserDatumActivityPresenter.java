package com.allinlink.platformapp.video_project.presenter.activity;

import com.unistrong.utils.SharedPreferencesUtil;
import com.unistrong.utils.StringsUtils;
import com.allinlink.platformapp.R;
import com.allinlink.platformapp.video_project.bean.BaseBean;
import com.allinlink.platformapp.video_project.bean.ChannelBean;
import com.allinlink.platformapp.video_project.bean.LoginOutput;
import com.allinlink.platformapp.video_project.contract.activity.UserDatumActivityContract;
import com.allinlink.platformapp.video_project.model.activity.UserDatumModel;
import com.allinlink.platformapp.video_project.utils.LoginUtils;
import com.allinlink.platformapp.video_project.utils.UserCache;
import com.unistrong.view.base.BasePresenter;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UserDatumActivityPresenter extends BasePresenter<UserDatumActivityContract.View, UserDatumModel> {
    public UserDatumActivityPresenter(UserDatumActivityContract.View view) {
        super(view);
    }

    @Override
    public UserDatumModel initModel() {
        return new UserDatumModel(this);
    }


    public void getUserInfo() {
        Subscription subscribe = mModel.getUserInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseBean<LoginOutput>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().onError(StringsUtils.getResourceString(R.string.control_fail));
                        getView().dismissLoadingDialog();
                    }

                    @Override
                    public void onNext(BaseBean<LoginOutput> loginOutput) {
                        if (!loginOutput.isSuccess()) {
                            getView().onError(StringsUtils.getResourceString(R.string.control_fail));
                            getView().dismissLoadingDialog();
                            return;
                        }
                        UserCache.loginOutput = loginOutput.getResult();
                        getView().dismissLoadingDialog();
//                        mView.setUserInfo(loginOutput.getResult());
                    }
                });
        mSubscription.add(subscribe);
    }



    public void upUserInfo(String text, String phoneText, String emailText) {
        Subscription subscribe = mModel.upUserInfo(text, phoneText, emailText)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().onError(StringsUtils.getResourceString(R.string.control_fail));
                        getView().dismissLoadingDialog();
                    }

                    @Override
                    public void onNext(BaseBean listBaseOutput) {
                        if (!listBaseOutput.isSuccess()) {
                            getView().onError(StringsUtils.getResourceString(R.string.control_fail));
                            return;
                        }
                        getView().dismissLoadingDialog();
                        getUserInfo();
                        mView.upInfoSccess();
                    }
                });
        mSubscription.add(subscribe);
    }
}
