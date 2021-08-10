package com.allinlink.platformapp.video_project.presenter.activity;

import com.allinlink.platformapp.R;
import com.unistrong.api.maps.model.LatLng;
import com.unistrong.utils.RxBus;
import com.unistrong.utils.StringsUtils;
import com.allinlink.platformapp.R;
import com.allinlink.platformapp.video_project.bean.BaseBean;
import com.allinlink.platformapp.video_project.bean.ChannelBean;
import com.allinlink.platformapp.video_project.bean.UpChannelBus;
import com.allinlink.platformapp.video_project.contract.activity.AddCameraInfoContract;
import com.allinlink.platformapp.video_project.model.activity.AddCameraInfoModel;
import com.unistrong.view.base.BasePresenter;

import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class AddCameraInfoPresenter extends BasePresenter<AddCameraInfoContract.View, AddCameraInfoModel> {
    public AddCameraInfoPresenter(AddCameraInfoContract.View view) {
        super(view);


        Subscription subscription = RxBus.getInstance().toObservable(UpChannelBus.class)
                .subscribe(new Action1<UpChannelBus>() {
                    @Override
                    public void call(UpChannelBus eventBean) {
                        mView.UpChannelData(eventBean);
                    }
                });
        mSubscription.add(subscription);

    }

    @Override
    public AddCameraInfoModel initModel() {
        return new AddCameraInfoModel(this);
    }

    public void getAllChannel(String gid) {
        Subscription subscribe = mModel.queryChannelById(gid)
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
                            getView().dismissLoadingDialog();
                            getView().onError(StringsUtils.getResourceString(R.string.control_fail));
                            return;
                        }
                        getView().dismissLoadingDialog();
                        mView.setChannelData(listBaseOutput.getResult());

                    }
                });
        mSubscription.add(subscribe);
    }

    public void upChannelInfo(ChannelBean bus) {
        Subscription subscribe = mModel.upChannelInfo(bus)
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
                            getView().dismissLoadingDialog();
                            getView().onError(StringsUtils.getResourceString(R.string.control_fail));
                            return;
                        }
                        getView().dismissLoadingDialog();


                    }
                });
        mSubscription.add(subscribe);
    }
}
