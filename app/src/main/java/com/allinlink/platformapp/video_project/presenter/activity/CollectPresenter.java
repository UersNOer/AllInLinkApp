package com.allinlink.platformapp.video_project.presenter.activity;

import com.unistrong.utils.StringsUtils;
import com.allinlink.platformapp.R;
import com.allinlink.platformapp.video_project.bean.BaseBean;
import com.allinlink.platformapp.video_project.bean.ChannelBean;
import com.allinlink.platformapp.video_project.contract.activity.CollectContract;
import com.allinlink.platformapp.video_project.model.activity.CollectModel;
import com.unistrong.view.base.BasePresenter;

import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CollectPresenter extends BasePresenter<CollectContract.View, CollectModel> {
    public CollectPresenter(CollectContract.View view) {
        super(view);
    }

    @Override
    public CollectModel initModel() {
        return new CollectModel(this);
    }

    public void queryCollectChannel() {
        Subscription subscribe = mModel.queryCollect()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseBean<List<ChannelBean>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().onError(StringsUtils.getResourceString(R.string.control_fail));
                        getView().dismissLoadingDialog();
                    }

                    @Override
                    public void onNext(BaseBean<List<ChannelBean>> listBaseOutput) {
                        if (!listBaseOutput.isSuccess()) {
                            getView().onError(StringsUtils.getResourceString(R.string.control_fail));
                            return;
                        }
                        getView().dismissLoadingDialog();
                        mView.setChannelData(listBaseOutput.getResult());
                    }
                });
        mSubscription.add(subscribe);
    }
    public void addChannelUserfavorites(ChannelBean data){
        Subscription subscribe = mModel.addChannelUserfavorites(data)
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
                    }
                });
        mSubscription.add(subscribe);


    }
    public void removeChannelUserfavoritesById(ChannelBean data){
        Subscription subscribe = mModel.removeChannelUserfavoritesById(data)
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
                    }
                });
        mSubscription.add(subscribe);
    }
}
