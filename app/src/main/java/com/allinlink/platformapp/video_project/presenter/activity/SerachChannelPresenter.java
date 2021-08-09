package com.allinlink.platformapp.video_project.presenter.activity;

import com.unistrong.utils.RxBus;
import com.unistrong.utils.SharedPreferencesUtil;
import com.unistrong.utils.StringsUtils;
import com.allinlink.platformapp.R;
import com.allinlink.platformapp.video_project.bean.BaseBean;
import com.allinlink.platformapp.video_project.bean.CameraBean;
import com.allinlink.platformapp.video_project.bean.ChannelBean;
import com.allinlink.platformapp.video_project.bean.PlayBackBean;
import com.allinlink.platformapp.video_project.config.Configs;
import com.allinlink.platformapp.video_project.contract.activity.SerachVideoContract;
import com.allinlink.platformapp.video_project.model.activity.SerachChanneModel;
import com.unistrong.view.base.BasePresenter;

import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class SerachChannelPresenter extends BasePresenter<SerachVideoContract.View, SerachChanneModel> {
    public SerachChannelPresenter(SerachVideoContract.View view) {
        super(view);
        Subscription subscription = RxBus.getInstance().toObservable(PlayBackBean.class)
                .subscribe(new Action1<PlayBackBean>() {
                    @Override
                    public void call(PlayBackBean eventBean) {
                        mView.onError(null);
                    }
                });
        mSubscription.add(subscription);
    }

    @Override
    public SerachChanneModel initModel() {
        return new SerachChanneModel(this);
    }


    public void queryChannel(String key) {
//        getView().showLoadingDialog(true);

        Subscription subscribe = mModel.queryChannelByKey(key)
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
                        SharedPreferencesUtil.getInstance().setObject(Configs.SERACH_CHANNEL_HISTORY, listBaseOutput.getResult());
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
