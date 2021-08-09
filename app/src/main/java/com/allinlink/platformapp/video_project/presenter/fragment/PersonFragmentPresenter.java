package com.allinlink.platformapp.video_project.presenter.fragment;

import com.unistrong.utils.RxBus;
import com.unistrong.utils.SharedPreferencesUtil;
import com.unistrong.utils.StringsUtils;
import com.allinlink.platformapp.R;
import com.allinlink.platformapp.video_project.bean.BaseBean;
import com.allinlink.platformapp.video_project.bean.ChannelBean;
import com.allinlink.platformapp.video_project.bean.LoginOutput;
import com.allinlink.platformapp.video_project.bean.UserInfoUp;
import com.allinlink.platformapp.video_project.contract.fragment.PersonFragmentContract;
import com.allinlink.platformapp.video_project.model.fragment.PersonFragmentModel;
import com.allinlink.platformapp.video_project.utils.LogUtil;
import com.allinlink.platformapp.video_project.utils.LoginUtils;
import com.allinlink.platformapp.video_project.utils.UserCache;
import com.unistrong.view.base.BasePresenter;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class PersonFragmentPresenter extends BasePresenter<PersonFragmentContract.View, PersonFragmentModel> {
    public PersonFragmentPresenter(PersonFragmentContract.View view) {
        super(view);
        Subscription subscription = RxBus.getInstance().toObservable(UserInfoUp.class)
                .subscribe(new Action1<UserInfoUp>() {
                    @Override
                    public void call(UserInfoUp eventBean) {
                       PersonFragmentPresenter.this.getUserInfo();
                    }
                });
        mSubscription.add(subscription);
    }

    @Override
    public PersonFragmentModel initModel() {
        return new PersonFragmentModel(this);
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
                        mView.setUserInfo(loginOutput.getResult());
                    }
                });
        mSubscription.add(subscribe);
    }
}
