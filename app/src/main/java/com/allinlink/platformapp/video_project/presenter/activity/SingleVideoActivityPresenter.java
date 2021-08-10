package com.allinlink.platformapp.video_project.presenter.activity;

import com.unistrong.utils.StringsUtils;
import com.allinlink.platformapp.R;
import com.allinlink.platformapp.video_project.bean.BaseBean;
import com.allinlink.platformapp.video_project.bean.ChannelBean;
import com.allinlink.platformapp.video_project.contract.activity.SingleVideoContract;
import com.allinlink.platformapp.video_project.model.activity.SingleVideoModel;
import com.unistrong.view.base.BasePresenter;

import java.util.HashMap;
import java.util.Map;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SingleVideoActivityPresenter extends BasePresenter<SingleVideoContract.View, SingleVideoModel> {
    public SingleVideoActivityPresenter(SingleVideoContract.View view) {
        super(view);
    }

    @Override
    public SingleVideoModel initModel() {
        return new SingleVideoModel(this);
    }

    public void cloundControl(String gid, String cmd) {


        Map<String, Object> map = new HashMap();
        map.put("channelId", gid);
        map.put("ptzCmd", cmd);
        map.put("speed", 400);
//            getView().showLoadingDialog(true);
        Subscription subscribe = mModel.cloundControl(map)
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
                            getView().dismissLoadingDialog();
                            return;
                        }
                        getView().dismissLoadingDialog();
                    }
                });
        mSubscription.add(subscribe);

    }

    public void stop(String gid) {
        Map<String, Object> map = new HashMap();
        map.put("channelId", gid);
        map.put("ptzCmd", 0);
        map.put("speed", 100);
//            getView().showLoadingDialog(true);
        Subscription subscribe = mModel.cloundControl(map)
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
                            getView().dismissLoadingDialog();
                            return;
                        }
                        getView().dismissLoadingDialog();
                    }
                });
        mSubscription.add(subscribe);
    }
}
