package com.allinlink.platformapp.video_project.presenter.fragment;

import com.unistrong.utils.RxBus;
import com.allinlink.platformapp.video_project.bean.HistoryVideoBean;
import com.allinlink.platformapp.video_project.bean.PlayBackBean;
import com.allinlink.platformapp.video_project.bean.UserInfoUp;
import com.allinlink.platformapp.video_project.contract.fragment.DataFragmentContract;
import com.allinlink.platformapp.video_project.model.fragment.DataFragmentModel;
import com.unistrong.view.base.BasePresenter;

import rx.Subscription;
import rx.functions.Action1;

public class DataFragmentPresenter extends BasePresenter<DataFragmentContract.View, DataFragmentModel> {
    public DataFragmentPresenter(DataFragmentContract.View view) {
        super(view);
        Subscription subscription = RxBus.getInstance().toObservable(PlayBackBean.class)
                .subscribe(new Action1<PlayBackBean>() {
                    @Override
                    public void call(PlayBackBean eventBean) {
                        mView.setHistotyVideo(eventBean);
                    }
                });
        mSubscription.add(subscription);

    }


    @Override
    public DataFragmentModel initModel() {
        return new DataFragmentModel(this);
    }
}
