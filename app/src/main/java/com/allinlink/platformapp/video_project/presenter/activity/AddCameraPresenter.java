package com.allinlink.platformapp.video_project.presenter.activity;

import com.unistrong.utils.StringsUtils;
import com.allinlink.platformapp.R;
import com.allinlink.platformapp.video_project.bean.BaseBean;
import com.allinlink.platformapp.video_project.bean.CameraBean;
import com.allinlink.platformapp.video_project.contract.activity.AddCameraContract;
import com.allinlink.platformapp.video_project.model.activity.AddCameraModel;
import com.unistrong.view.base.BasePresenter;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AddCameraPresenter extends BasePresenter<AddCameraContract.View, AddCameraModel> {
    public AddCameraPresenter(AddCameraContract.View view) {
        super(view);
    }

    @Override
    public AddCameraModel initModel() {
        return new AddCameraModel(this);
    }


    public void addCamera(String cameraCode, String cameraName, String tenantInfo, String ipAddress, String port, String userName, String userPsw, String standardType, String factory) {
        Subscription subscribe = mModel.addCamera(cameraCode, cameraName, tenantInfo, ipAddress, port, userName, userPsw, standardType, factory)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseBean<String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().onError(StringsUtils.getResourceString(R.string.control_fail));
                        getView().dismissLoadingDialog();
                    }

                    @Override
                    public void onNext(BaseBean<String> listBaseOutput) {
                        if (!listBaseOutput.isSuccess()) {
                            getView().onError(StringsUtils.getResourceString(R.string.control_fail));
                            return;
                        }
                        getView().dismissLoadingDialog();
                        mView.addSuccess(listBaseOutput.getResult());

                    }
                });
        mSubscription.add(subscribe);
    }

    //查询所有厂商
    public void findChannelInfoChecked() {
        Subscription subscribe = mModel.findChannelInfoChecked()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseBean<String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().onError(StringsUtils.getResourceString(R.string.control_fail));
                        getView().dismissLoadingDialog();
                    }

                    @Override
                    public void onNext(BaseBean<String> listBaseOutput) {
                        if (!listBaseOutput.isSuccess()) {
                            getView().onError(StringsUtils.getResourceString(R.string.control_fail));
                            return;
                        }
                        getView().dismissLoadingDialog();
//                        mView.addSuccess(listBaseOutput.getResult());

                    }
                });
        mSubscription.add(subscribe);
    }


    //获取所有公司
    public void findAllTenanInfoList() {
        Subscription subscribe = mModel.findAllTenanInfoList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseBean<String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().onError(StringsUtils.getResourceString(R.string.control_fail));
                        getView().dismissLoadingDialog();
                    }

                    @Override
                    public void onNext(BaseBean<String> listBaseOutput) {
                        if (!listBaseOutput.isSuccess()) {
                            getView().onError(StringsUtils.getResourceString(R.string.control_fail));
                            return;
                        }
                        getView().dismissLoadingDialog();
//                        mView.addSuccess(listBaseOutput.getResult());

                    }
                });
        mSubscription.add(subscribe);
    }


    //摄像机分组管理
    public void findCreamManage() {
        Subscription subscribe = mModel.findCreamManage()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseBean<String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().onError(StringsUtils.getResourceString(R.string.control_fail));
                        getView().dismissLoadingDialog();
                    }

                    @Override
                    public void onNext(BaseBean<String> listBaseOutput) {
                        if (!listBaseOutput.isSuccess()) {
                            getView().onError(StringsUtils.getResourceString(R.string.control_fail));
                            return;
                        }
                        getView().dismissLoadingDialog();
//                        mView.addSuccess(listBaseOutput.getResult());

                    }
                });
        mSubscription.add(subscribe);
    }
}
