package com.allinlink.platformapp.video_project.ui.activity;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.unistrong.network.router.UserModuleRouter;
import com.unistrong.utils.AppContextUtils;
import com.unistrong.utils.StringsUtils;
import com.allinlink.platformapp.R;
import com.allinlink.platformapp.video_project.contract.activity.UpPasswordContract;
import com.allinlink.platformapp.video_project.presenter.activity.UpPasswordPresenter;
import com.allinlink.platformapp.video_project.utils.LoginUtils;
import com.allinlink.platformapp.video_project.utils.UserCache;
import com.allinlink.platformapp.video_project.widget.EditLinearLayout;
import com.unistrong.view.base.BaseTitleActivity;
import com.unistrong.view.navigationbar.INavigationBarBackListener;
import com.unistrong.view.navigationbar.NavigationBar;
import com.unistrong.view.navigationbar.NavigationBuilder;
import com.unistrong.view.utils.ToastUtil;

public class UpPasswordActivity extends BaseTitleActivity<UpPasswordPresenter> implements View.OnClickListener, UpPasswordContract.View {

    private EditLinearLayout edOldPassword;
    private EditLinearLayout edNewPassword;
    private EditLinearLayout edNewCheckPassword;

    @Override
    protected NavigationBuilder onCreateNavigationBarByBuilder(NavigationBuilder builder) {
        NavigationBuilder navigationBuilder = builder.setTitle(StringsUtils.getResourceString(R.string.activity_uppassword))
                .setType(NavigationBar.BACK_AND_TITLE).setNavigationBarListener(new INavigationBarBackListener() {
                    @Override
                    public void onBackClick() {
                        finish();
                    }
                });
        return navigationBuilder;
    }

    @Override
    public View onCreateContentView() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_up_password, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mPresenter = new UpPasswordPresenter(this);
        view.findViewById(R.id.bt_submit).setOnClickListener(this);
        edNewCheckPassword = view.findViewById(R.id.ed_new_checkpassword);
        edNewPassword = view.findViewById(R.id.ed_new_password);
        edOldPassword = view.findViewById(R.id.ed_old_password);
        edOldPassword.initEdit();
        edNewPassword.initEdit();
        edNewCheckPassword.initEdit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_submit:
                if (TextUtils.isEmpty(edOldPassword.getText().toString())) {
                    ToastUtil.showVerboseToast("请输入旧密码");
                    return;
                }
                if (TextUtils.isEmpty(edNewPassword.getText().toString())) {
                    ToastUtil.showVerboseToast("请输入新密码");
                    return;
                }
                if (TextUtils.isEmpty(edNewCheckPassword.getText().toString())) {
                    ToastUtil.showVerboseToast("请输入确认密码");
                    return;
                }
                if (!edNewPassword.getText().toString().trim().equals(edNewCheckPassword.getText().toString().trim())) {
                    ToastUtil.showVerboseToast("两次输入密码不一样");
                    return;
                }

                mPresenter.submitPassword(edOldPassword.getText().toString().trim(), edNewPassword.getText().toString().trim());
                break;
        }
    }

    @Override
    public void upPasswordSuccess() {
        UserModuleRouter userModuleRouter = new UserModuleRouter();
        userModuleRouter.clearUserData();
        userModuleRouter.openLoginActivity(AppContextUtils.getAppContext());
        AppContextUtils.finishAllActivity();
    }

    @Override
    public void onError(String msg) {
        ToastUtil.showVerboseToast(msg);

    }

    @Override
    public Context getContext() {
        return null;
    }
}