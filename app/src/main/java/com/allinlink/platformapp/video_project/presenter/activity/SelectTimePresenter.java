package com.allinlink.platformapp.video_project.presenter.activity;

import com.unistrong.utils.RxBus;
import com.unistrong.utils.StringsUtils;
import com.allinlink.platformapp.R;
import com.allinlink.platformapp.video_project.bean.BaseBean;
import com.allinlink.platformapp.video_project.bean.ChannelBean;
import com.allinlink.platformapp.video_project.bean.PlayBackBean;
import com.allinlink.platformapp.video_project.contract.activity.SelectTimeContract;
import com.allinlink.platformapp.video_project.model.activity.SelectTimeModel;
import com.unistrong.view.base.BasePresenter;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SelectTimePresenter extends BasePresenter<SelectTimeContract.View, SelectTimeModel> {
    public SelectTimePresenter(SelectTimeContract.View view) {
        super(view);
    }

    @Override
    public SelectTimeModel initModel() {
        return new SelectTimeModel(this);
    }

    public void queryChannelById(String gid, String startTime, String endTime) {
        getView().showLoadingDialog(true);

        Subscription subscribe = mModel.queryChannelById(gid, startTime, endTime)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseBean<PlayBackBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().onError(StringsUtils.getResourceString(R.string.control_fail));
                        getView().dismissLoadingDialog();
                    }

                    @Override
                    public void onNext(BaseBean<PlayBackBean> listBaseOutput) {
                        if (!listBaseOutput.isSuccess()) {
                            getView().onError(StringsUtils.getResourceString(R.string.control_fail));
                            return;
                        }
                        getView().dismissLoadingDialog();

                        if (listBaseOutput.getResult() != null && listBaseOutput.getResult().getItems().size() != 0) {
                            mView.querySuccess(listBaseOutput.getResult());
                        } else {
                            getView().onError("当前所选时间没有录像视频");
                        }

                    }
                });
        mSubscription.add(subscribe);


    }
}
