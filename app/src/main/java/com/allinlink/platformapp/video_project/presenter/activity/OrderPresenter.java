package com.allinlink.platformapp.video_project.presenter.activity;

import com.unistrong.utils.StringsUtils;
import com.allinlink.platformapp.R;
import com.allinlink.platformapp.video_project.bean.BaseBean;
import com.allinlink.platformapp.video_project.bean.OrderBean;
import com.allinlink.platformapp.video_project.contract.activity.OrderContract;
import com.allinlink.platformapp.video_project.model.activity.OrderModel;
import com.unistrong.view.base.BasePresenter;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class OrderPresenter extends BasePresenter<OrderContract.View, OrderModel> {
    public OrderPresenter(OrderContract.View view) {
        super(view);
    }

    @Override
    public OrderModel initModel() {
        return new OrderModel(this);
    }

    public void queryAllOrder(int state, int page) {
        Subscription subscribe = mModel.queryOrser(state, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseBean<OrderBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().onError(StringsUtils.getResourceString(R.string.control_fail));
                        getView().dismissLoadingDialog();
                    }

                    @Override
                    public void onNext(BaseBean<OrderBean> listBaseOutput) {
                        if (!listBaseOutput.isSuccess()) {
                            getView().onError(StringsUtils.getResourceString(R.string.control_fail));
                            return;
                        }
                        getView().dismissLoadingDialog();
                        mView.setOrderData(listBaseOutput.getResult().getDatas());
                    }
                });
        mSubscription.add(subscribe);
    }


    public void closeOrder(OrderBean.DatasBean bean, String resaon) {
        Subscription subscribe = mModel.closeOrder(bean, resaon)
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
                        mView.closeSuccess();
                    }
                });
        mSubscription.add(subscribe);
    }
}
