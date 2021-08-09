package com.allinlink.platformapp.video_project.presenter.activity;

import com.unistrong.utils.SharedPreferencesUtil;
import com.unistrong.utils.StringsUtils;
import com.allinlink.platformapp.R;
import com.allinlink.platformapp.video_project.bean.BaseBean;
import com.allinlink.platformapp.video_project.bean.LoginOutput;
import com.allinlink.platformapp.video_project.contract.activity.LoginByPasswordContract;
import com.allinlink.platformapp.video_project.model.activity.LoginModel;
import com.allinlink.platformapp.video_project.utils.LoginUtils;
import com.allinlink.platformapp.video_project.utils.VpnLinkUtils;
import com.unistrong.view.base.BasePresenter;

import android.os.Handler;
import android.util.Log;


import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * description: 密码登录
 *
 * @author ltd
 * @date 2020-06-3
 **/
public class LoginByPasswordPresenter extends BasePresenter<LoginByPasswordContract.View, LoginModel> implements LoginByPasswordContract.Presenter {

    VpnLinkUtils vpnLinkUtils;


    public LoginByPasswordPresenter(LoginByPasswordContract.View view, Handler handler) {
        super(view);
        vpnLinkUtils = new VpnLinkUtils();
        vpnLinkUtils.initUtils(mView.getContext(), handler);
    }
    /**
     * VPN 连接
     */
    public void vpnLink(String name, String pwd) {
        Log.i("TAG", "VPN开始连接");
        vpnLinkUtils.startLogin(name, pwd);
    }

    /**
     * @return 获取VPN当前状态
     */
    public int getVpnState() {
        return vpnLinkUtils.getVpnState();
    }


    @Override
    public void login(final String username, final String password) {

        getView().showLoadingDialog(true);
        Subscription subscribe = mModel.login(username, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseBean<LoginOutput>>() {
                    @Override
                    public void onCompleted() {
                        getView().dismissLoadingDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("TAG",e.toString());
                        getView().onError(StringsUtils.getResourceString(R.string.login_fail));
                        getView().dismissLoadingDialog();
                    }

                    @Override
                    public void onNext(BaseBean<LoginOutput> loginOutput) {
                        if (!loginOutput.isSuccess()) {
                            getView().onError(StringsUtils.getResourceString(R.string.login_fail));
                            getView().dismissLoadingDialog();
                            return;
                        }
                        SharedPreferencesUtil.getInstance().putToken(loginOutput.getToken());
                        //本地保存用户信息
                        LoginUtils.setUserLoginData(loginOutput.getResult());
                        //保存未加密的密码
                        LoginUtils.setLoginPwd(password);

                        getView().onLoginSuccess(loginOutput);

                    }
                });
        mSubscription.add(subscribe);
    }


    @Override
    public LoginModel initModel() {
        return new LoginModel(this);
    }


}