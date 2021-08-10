package com.allinlink.platformapp.video_project.presenter.fragment;

import com.unistrong.utils.RxBus;
import com.unistrong.utils.StringsUtils;
import com.allinlink.platformapp.R;
import com.allinlink.platformapp.video_project.bean.BaseBean;
import com.allinlink.platformapp.video_project.bean.CameraBean;
import com.allinlink.platformapp.video_project.bean.ChannelBean;
import com.allinlink.platformapp.video_project.bean.ControlBean;
import com.allinlink.platformapp.video_project.bean.MapMarkerBean;
import com.allinlink.platformapp.video_project.contract.fragment.DeviceFragmentContract;
import com.allinlink.platformapp.video_project.model.fragment.DeviceFragmentModel;
import com.allinlink.platformapp.video_project.utils.LogUtil;
import com.unistrong.view.base.BasePresenter;

import java.util.HashMap;
import java.util.Map;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class DeviceFragmentPresenter extends BasePresenter<DeviceFragmentContract.View, DeviceFragmentModel> {
    public DeviceFragmentPresenter(DeviceFragmentContract.View view) {
        super(view);
        Subscription subscription = RxBus.getInstance().toObservable(ChannelBean.class)
                .subscribe(new Action1<ChannelBean>() {
                    @Override
                    public void call(ChannelBean eventBean) {
                        mView.selctCameraData(eventBean);
                    }
                });
        mSubscription.add(subscription);

        Subscription subscription2 = RxBus.getInstance().toObservable(ControlBean.class)
                .subscribe(new Action1<ControlBean>() {
                    @Override
                    public void call(ControlBean eventBean) {
                        sendControl(eventBean.getGid(), eventBean.getCmd());
                    }
                });
        mSubscription.add(subscription2);

    }

    @Override
    public DeviceFragmentModel initModel() {
        return new DeviceFragmentModel(this);
    }

    public void cloundControl(HashMap<String, ChannelBean> selectedCameraMap, String cmd) {

        if (selectedCameraMap.size() == 0) {
            return;
        }

        for (String q : selectedCameraMap.keySet()) {
            ChannelBean channelBean = selectedCameraMap.get(q);
            sendControl(channelBean.getGid(), cmd);
        }
    }

    public void sendControl(String gid, String cmd) {
        Map<String, Object> map = new HashMap();
        map.put("channelId", gid);
        map.put("ptzCmd", cmd);
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

    public void stop(HashMap<String, ChannelBean> selectedCameraMap) {
        for (String q : selectedCameraMap.keySet()) {
            ChannelBean channelBean = selectedCameraMap.get(q);
            sendControl(channelBean.getGid(), "0");
//            Map<String, Object> map = new HashMap();
//            map.put("channelId", channelBean.getGid());
//            map.put("ptzCmd", 0);
//            map.put("speed", 100);
////            getView().showLoadingDialog(true);
//            Subscription subscribe = mModel.cloundControl(map)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Observer<BaseBean>() {
//                        @Override
//                        public void onCompleted() {
//
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                            getView().onError(StringsUtils.getResourceString(R.string.control_fail));
//                            getView().dismissLoadingDialog();
//                        }
//
//                        @Override
//                        public void onNext(BaseBean listBaseOutput) {
//                            if (!listBaseOutput.isSuccess()) {
//                                getView().onError(StringsUtils.getResourceString(R.string.control_fail));
//                                getView().dismissLoadingDialog();
//                                return;
//                            }
//                            getView().dismissLoadingDialog();
//                        }
//                    });
//            mSubscription.add(subscribe);
        }
    }
}
