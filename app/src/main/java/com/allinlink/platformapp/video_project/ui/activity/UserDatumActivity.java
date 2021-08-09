package com.allinlink.platformapp.video_project.ui.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.unistrong.utils.RxBus;
import com.unistrong.utils.StringsUtils;
import com.allinlink.platformapp.R;
import com.allinlink.platformapp.video_project.bean.UserInfoUp;
import com.allinlink.platformapp.video_project.contract.activity.UserDatumActivityContract;
import com.allinlink.platformapp.video_project.presenter.activity.UserDatumActivityPresenter;
import com.allinlink.platformapp.video_project.utils.UserCache;
import com.allinlink.platformapp.video_project.widget.EditLinearLayout;
import com.unistrong.view.EditTextWithDel;
import com.unistrong.view.base.BaseActivity;
import com.unistrong.view.base.BaseTitleActivity;
import com.unistrong.view.navigationbar.INavigationBarBackListener;
import com.unistrong.view.navigationbar.NavigationBar;
import com.unistrong.view.navigationbar.NavigationBuilder;
import com.unistrong.view.utils.ToastUtil;

public class UserDatumActivity extends BaseTitleActivity<UserDatumActivityPresenter> implements View.OnClickListener, UserDatumActivityContract.View {
    EditLinearLayout nickname, phone, email;

    @Override
    protected NavigationBuilder onCreateNavigationBarByBuilder(NavigationBuilder builder) {
        NavigationBuilder navigationBuilder = builder.setTitle(StringsUtils.getResourceString(R.string.acticity_datum))
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
        View view = LayoutInflater.from(this).inflate(R.layout.activity_user_datum, null);
        initView(view);

        return view;
    }

    private void initView(View view) {
        mPresenter = new UserDatumActivityPresenter(this);
        TextView tvName = view.findViewById(R.id.tv_username);
        tvName.setText(UserCache.loginOutput.getUserName() == null ? "--" : UserCache.loginOutput.getUserName());
        TextView tvNepname = view.findViewById(R.id.tv_depname);
        tvNepname.setText(UserCache.loginOutput.getDepName());
        view.findViewById(R.id.bt_submit).setOnClickListener(this);
        nickname = view.findViewById(R.id.lin_nickname);
        nickname.setText(UserCache.loginOutput.getRealName());
        nickname.initEdit();
        phone = view.findViewById(R.id.lin_phone);
        phone.setText(UserCache.loginOutput.getTelephone());
        phone.initEdit();
        email = view.findViewById(R.id.lin_email);
        email.setText(UserCache.loginOutput.getEmail());
        email.initEdit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_submit:
                mPresenter.upUserInfo(nickname.getText(), phone.getText(), email.getText());
                break;
        }
    }

    @Override
    public void upInfoSccess() {
        RxBus.getInstance().send(UserInfoUp.class);
        ToastUtil.showErrorToast("修改成功");
        finish();
    }

    @Override
    public void onError(String msg) {

    }

    @Override
    public Context getContext() {
        return null;
    }
}