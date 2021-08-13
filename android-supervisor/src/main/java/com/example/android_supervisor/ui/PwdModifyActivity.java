package com.example.android_supervisor.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.android_supervisor.R;
import com.example.android_supervisor.common.AppContext;
import com.example.android_supervisor.common.UserSession;
import com.example.android_supervisor.entities.Account;
import com.example.android_supervisor.http.ServiceGenerator;
import com.example.android_supervisor.http.common.ProgressTransformer;
import com.example.android_supervisor.http.oauth.AccessToken;
import com.example.android_supervisor.http.response.Response;
import com.example.android_supervisor.http.response.ResponseObserver;
import com.example.android_supervisor.http.service.BasicService;
import com.example.android_supervisor.service.AppServiceManager;
import com.example.android_supervisor.ui.view.ProgressText;
import com.example.android_supervisor.utils.SystemUtils;
import com.example.android_supervisor.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * 修改密码
 */
public class PwdModifyActivity extends BaseActivity {
    @BindView(R.id.et_pwd_modify_old_pwd)
    EditText etPwdOld;

    @BindView(R.id.et_pwd_modify_new_pwd)
    EditText etPwdNew;

    @BindView(R.id.et_pwd_modify_new_pwd2)
    EditText etPwdNew2;

    private Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pwd_modify);
        ButterKnife.bind(this);
        initUser();
    }

    private void initUser() {
//        String userId = UserSession.getUserId(this);
//        FaceSqliteHelper sqliteHelper = FaceSqliteHelper.getInstance(this);

        String pwd = UserSession.getUserPwd(this);
        if (pwd == null) {
            ToastUtils.show(this, "当前用户信息已清空，请重新登录");
            AccessToken.getInstance(this).delete();
            UserSession.setUserId(this, "");
            UserSession.clear(this);
            // TODO 环信的代码可以屏蔽掉
            AppServiceManager.stop(this);


            AppContext appContext = (AppContext) this.getApplicationContext();
            appContext.exit();

            Intent loginIntent = new Intent(this, LoginActivity.class);
            loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(loginIntent);
        }
    }

    @OnClick(R.id.btn_pwd_modify_submit)
    public void onSubmit(View v) {
        SystemUtils.hideSoftInput(this, v);

        String oldPwd = etPwdOld.getText().toString().trim();
        String newPwd = etPwdNew.getText().toString().trim();
        String newPwd2 = etPwdNew2.getText().toString().trim();

        if (TextUtils.isEmpty(oldPwd)) {
            ToastUtils.show(this, "旧密码不能为空");
            etPwdOld.requestFocus();
            return;
        }

        String pwd = UserSession.getUserPwd(this);

        if (!TextUtils.isEmpty(pwd) && !oldPwd.equals(pwd)) {
            ToastUtils.show(this, "旧密码输入错误");
            etPwdOld.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(newPwd)) {
            ToastUtils.show(this, "新密码不能为空");
            etPwdNew.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(newPwd2)) {
            ToastUtils.show(this, "确认新密码不能为空");
            etPwdNew2.requestFocus();
            return;
        }

        if (pwd.equals(newPwd)) {
            ToastUtils.show(this, "新旧密码不能一样，请重新输入");
            etPwdNew.requestFocus();
            return;
        }

        if (!newPwd.equals(newPwd2)) {
            ToastUtils.show(this, "两次输入的新密码不一致，请重新输入");
            etPwdNew2.requestFocus();
            return;
        }

        changePwd(oldPwd, newPwd);
    }

    @OnClick(R.id.iv_pwd_modify_show_old_pwd)
    public void onShowOldPwd(View v) {
        if (v.isSelected()) {
            v.setSelected(false);
            ((ImageView) v).setImageResource(R.drawable.ic_pwd_show);
            etPwdOld.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else {
            v.setSelected(true);
            ((ImageView) v).setImageResource(R.drawable.ic_pwd_hide);
            etPwdOld.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }
    }

    @OnClick(R.id.iv_pwd_modify_show_new_pwd)
    public void onShowNewPwd(View v) {
        if (v.isSelected()) {
            v.setSelected(false);
            ((ImageView) v).setImageResource(R.drawable.ic_pwd_show);
            etPwdNew.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else {
            v.setSelected(true);
            ((ImageView) v).setImageResource(R.drawable.ic_pwd_hide);
            etPwdNew.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }
    }

    @OnClick(R.id.iv_pwd_modify_show_new_pwd2)
    public void onShowNewPwd2(View v) {
        if (v.isSelected()) {
            v.setSelected(false);
            ((ImageView) v).setImageResource(R.drawable.ic_pwd_show);
            etPwdNew2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else {
            v.setSelected(true);
            ((ImageView) v).setImageResource(R.drawable.ic_pwd_hide);
            etPwdNew2.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }
    }

    @SuppressWarnings("unchecked")
    private void changePwd(String oldPwd, String newPwd) {
        String uid = UserSession.getUserId(this);
        BasicService basicService = ServiceGenerator.create(BasicService.class);
        Observable<Response> observable = basicService.changePwd(uid, oldPwd, newPwd);
        observable.observeOn(AndroidSchedulers.mainThread())
                .compose(this.<Response>bindToLifecycle())
                .compose(new ProgressTransformer<Response>(this, ProgressText.submit))
                .subscribe(new ResponseObserver(this) {
                    @Override
                    public void onSuccess(Object data) {
                        UserSession.setUserPwd(PwdModifyActivity.this, newPwd);
                        setResult(RESULT_OK);
                        finish();
                        ToastUtils.show(PwdModifyActivity.this, "修改密码成功");
                    }
                });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
