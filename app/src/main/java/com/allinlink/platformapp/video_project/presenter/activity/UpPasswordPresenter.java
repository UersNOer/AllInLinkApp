package com.allinlink.platformapp.video_project.presenter.activity;

import com.unistrong.utils.StringsUtils;
import com.allinlink.platformapp.video_project.bean.BaseBean;
import com.allinlink.platformapp.video_project.contract.activity.UpPasswordContract;
import com.allinlink.platformapp.video_project.model.activity.UpPasswordModel;
import com.unistrong.view.base.BasePresenter;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UpPasswordPresenter extends BasePresenter<UpPasswordContract.View, UpPasswordModel> {
    public UpPasswordPresenter(UpPasswordContract.View view) {
        super(view);
    }

    @Override
    public UpPasswordModel initModel() {
        return new UpPasswordModel(this);
    }

    public void submitPassword(String oldPassword, String newPassword) {
        getView().showLoadingDialog(true);

        Subscription subscribe = mModel.submitPassword(oldPassword, newPassword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<BaseBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().onError("修改密码失败");
                        getView().dismissLoadingDialog();
                    }

                    @Override
                    public void onNext(BaseBean baseBean) {
                        if (!baseBean.isSuccess()) {
                            getView().onError("修改密码失败");
                            getView().dismissLoadingDialog();
                            return;
                        }
                        getView().dismissLoadingDialog();
                        mView.upPasswordSuccess();
                    }
                });
        mSubscription.add(subscribe);
    }

}
