package com.allinlink.platformapp.video_project.presenter.fragment;

import com.unistrong.network.BaseOutput;
import com.unistrong.utils.RxBus;
import com.unistrong.utils.StringsUtils;
import com.allinlink.platformapp.R;
import com.allinlink.platformapp.video_project.bean.BaseBean;
import com.allinlink.platformapp.video_project.bean.CameraBean;
import com.allinlink.platformapp.video_project.bean.ChannelBean;
import com.allinlink.platformapp.video_project.bean.LoginOutput;
import com.allinlink.platformapp.video_project.bean.MapMarkerBean;
import com.allinlink.platformapp.video_project.bean.UMToekn;
import com.allinlink.platformapp.video_project.bean.UpChannelBus;
import com.allinlink.platformapp.video_project.contract.fragment.MapFragmentContract;
import com.allinlink.platformapp.video_project.model.fragment.MapFragmentModel;
import com.allinlink.platformapp.video_project.utils.LogUtil;
import com.allinlink.platformapp.video_project.utils.UserCache;
import com.unistrong.view.base.BasePresenter;

import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MapFragmentPresenter extends BasePresenter<MapFragmentContract.View, MapFragmentModel> {
    public MapFragmentPresenter(MapFragmentContract.View view) {
        super(view);
        addUmToken(UserCache.deviceToken);


        Subscription subscription = RxBus.getInstance().toObservable(UpChannelBus.class)
                .subscribe(new Action1<UpChannelBus>() {
                    @Override
                    public void call(UpChannelBus eventBean) {
                        getAllCameraInfo();
                    }
                });
        mSubscription.add(subscription);
    }

    @Override
    public MapFragmentModel initModel() {
        return new MapFragmentModel(this);
    }

    public void getMapMarker() {
        getView().showLoadingDialog(true);
        Subscription subscribe = mModel.getMapMarker()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseBean<MapMarkerBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().onError(StringsUtils.getResourceString(R.string.control_fail));

                        getView().dismissLoadingDialog();
                    }

                    @Override
                    public void onNext(BaseBean<MapMarkerBean> listBaseOutput) {
                        if (!listBaseOutput.isSuccess()) {
                            getView().onError(StringsUtils.getResourceString(R.string.control_fail));
                            getView().dismissLoadingDialog();
                            return;
                        }
                        getView().dismissLoadingDialog();
                        mView.setMapMarker(listBaseOutput.getResult());
                    }
                });
        mSubscription.add(subscribe);
    }

    public void getAllCameraInfo() {
        Subscription subscribe = mModel.getAllCamera()
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

                        mView.setCameraData(listBaseOutput.getResult());
                    }
                });
        mSubscription.add(subscribe);
    }


    public void addUmToken(String token) {
        Subscription subscribe = mModel.addUMengToken(token)
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

    ;


    public void addChannelUserfavorites(ChannelBean data) {
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

    public void removeChannelUserfavoritesById(ChannelBean data) {
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
