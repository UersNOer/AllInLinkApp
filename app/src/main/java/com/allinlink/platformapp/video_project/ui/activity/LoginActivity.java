package com.allinlink.platformapp.video_project.ui.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.unistrong.network.BaseOutput;
import com.unistrong.skin.CommonSkinUtils;
import com.unistrong.utils.SharedPreferencesUtil;
import com.unistrong.utils.StringsUtils;
import com.allinlink.platformapp.R;
import com.allinlink.platformapp.video_project.bean.BaseBean;
import com.allinlink.platformapp.video_project.bean.LoginOutput;
import com.allinlink.platformapp.video_project.contract.activity.LoginByPasswordContract;
import com.allinlink.platformapp.video_project.presenter.activity.LoginByPasswordPresenter;
import com.allinlink.platformapp.video_project.utils.LoginUtils;
import com.allinlink.platformapp.video_project.widget.EditLinearLayout;
import com.unistrong.view.EditTextWithDel;
import com.unistrong.view.base.BaseActivity;
import com.unistrong.view.utils.StatusBarUtil;
import com.unistrong.view.utils.SysUtils;
import com.unistrong.view.RippleView;

import net.vpnsdk.vpn.AAAMethod;
import net.vpnsdk.vpn.Common;
import net.vpnsdk.vpn.VPNManager;


/**
 * description: 登录页面
 **/


public class LoginActivity extends BaseActivity<LoginByPasswordPresenter> implements View.OnClickListener,
        RippleView.OnRippleCompleteListener, LoginByPasswordContract.View {
    private String Tag = "LoginActivity";
    private static AAAMethod[] mMethods = null;
    boolean vpnFlag = false;
    private String vpnName, vpnPwd;
    private LinearLayout llWholeView;

    private EditTextWithDel etPhoneNum, etPassword;

    private RippleView rvEnter, rvSetting;

    SharedPreferences sharedPreferences;

    private Handler mHandler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case Common.VpnMsg.MSG_VPN_CONNECTING:
                    Log.i(Tag, "vpn connecting");
                    break;
                case Common.VpnMsg.MSG_VPN_CONNECTED:
                    vpnFlag = true;
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("VPN_NAME", vpnName);
                    editor.putString("VPN_PWD", vpnPwd);
                    editor.commit();
                    Toast.makeText(LoginActivity.this, "连接成功", Toast.LENGTH_LONG).show();
                    break;
                case Common.VpnMsg.MSG_VPN_DISCONNECTING:
                    Log.i(Tag, "vpn disconnecting ");
                    break;
                case Common.VpnMsg.MSG_VPN_DISCONNECTED:
                    Log.i(Tag, "vpn disconnected");
                case Common.VpnMsg.MSG_VPN_CONNECT_FAILED:

                    Log.i(Tag, "vpn connect failed");
                    break;
                case Common.VpnMsg.MSG_VPN_RECONNECTING:
                    Log.i(Tag, "vpn reconnecting");
                    break;
                case Common.VpnMsg.MSG_VPN_LOGIN:
                    int error = msg.getData().getInt(Common.VpnMsg.MSG_VPN_ERROR_CODE);
                    if (0 == mMethods.length) {
                        VPNManager.getInstance().cancelLogin();
                    }
                    //需要重新输入账户密码
                    break;
                case Common.VpnMsg.MSG_VPN_DEVREG:
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_by_password);
        sharedPreferences = getSharedPreferences("Vpn", MODE_PRIVATE);
        setStatusBar();
        initView();
        setViewListener();

    }

    @Override
    protected void setStatusBar() {
        int statusBarColor = CommonSkinUtils.getColor(this, R.color.white);
        StatusBarUtil.setColorNoTranslucentWithSkin(this, statusBarColor);
    }

    public void initView() {
        findViewById(R.id.tv_vpn).setOnClickListener(this);
        mPresenter = new LoginByPasswordPresenter(this, mHandler);
        llWholeView = findViewById(R.id.ll_phone_num);
        etPhoneNum = findViewById(R.id.et_phone_num);
        etPassword = findViewById(R.id.et_password);
        rvEnter = findViewById(R.id.rv_enter);
        rvSetting = findViewById(R.id.rv_setting);
        rvEnter.setRippleColor(R.color.c12);
        rvEnter.setRippleDuration(100);
        initBeforeUsername();
    }

    private void initBeforeUsername() {
        String loginUsername = LoginUtils.getLoginUsername();
        etPhoneNum.setText(TextUtils.isEmpty(loginUsername) ? "" : loginUsername);
        etPhoneNum.setSelection(TextUtils.isEmpty(loginUsername) ? 0 : loginUsername.length());
        etPhoneNum.requestFocus();


        String loginPassword = LoginUtils.getLoginPwd();
        etPassword.setText(TextUtils.isEmpty(loginPassword) ? "" : loginPassword);
        etPassword.setSelection(TextUtils.isEmpty(loginPassword) ? 0 : loginPassword.length());
        etPassword.requestFocus();


        vpnName = sharedPreferences.getString("VPN_NAME", null);
        vpnPwd = sharedPreferences.getString("VPN_PWD", null);
        if (null != vpnName && null != vpnPwd) {
           new Thread(new Runnable() {
               @Override
               public void run() {
                   try {
                       Thread.sleep(300);
                       mPresenter.vpnLink(vpnName, vpnPwd);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }
               }
           }).start();
        }
    }


    public void setViewListener() {
        llWholeView.setOnClickListener(this);
        rvEnter.setOnRippleCompleteListener(this);
        rvSetting.setOnRippleCompleteListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ll_phone_num) {
            SysUtils.dismissKeyBoard(this);
        } else if (id == R.id.tv_vpn) {
            showVpnDialog();

        }
    }

    private void showVpnDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_vpn, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog alertDialog = builder.create();

        EditLinearLayout edName = view.findViewById(R.id.lin_name);
        EditLinearLayout edPwd = view.findViewById(R.id.lin_pwd);
        Button bt = view.findViewById(R.id.bt_submit);
        edName.setText(vpnName);
        edPwd.setText(vpnPwd);
        edName.initEdit();
        edPwd.initEdit();

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(edName.getText())) {
                    Toast.makeText(LoginActivity.this, "请输入账号", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(edPwd.getText())) {
                    Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_LONG).show();
                } else {
                    vpnName = edName.getText();
                    vpnPwd = edPwd.getText();
                    mPresenter.vpnLink(vpnName, vpnPwd);
                    alertDialog.dismiss();
                }
            }
        });
        alertDialog.setView(view);
        alertDialog.show();
    }

    @Override
    public void onLoginSuccess(BaseBean<LoginOutput> baseOutput) {
        onError(StringsUtils.getResourceString(R.string.login_success));
        SharedPreferencesUtil.getInstance().putIsLoginSuccess(true);
        startActivity(new Intent(this, UniMainActivity.class));
        finish();
    }


    @Override
    public void onError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }


    @Override
    public Context getContext() {
        return LoginActivity.this;
    }

    @Override
    public void onComplete(RippleView rippleView) {

        if (sharedPreferences.getString("VPN_NAME", null) == null) {
            showVpnDialog();
            Toast.makeText(this, "请先连接VPN", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mPresenter.getVpnState() != Common.VpnMsg.MSG_VPN_CONNECTED) {
            mPresenter.vpnLink(vpnName, vpnPwd);
            Toast.makeText(this, "正在连接VPN,请稍后再试", Toast.LENGTH_SHORT).show();
            return;
        }
        if (rippleView.getId() == R.id.rv_enter) {
            SysUtils.dismissKeyBoard(this);
            if (!LoginUtils.isUsername(etPhoneNum.getText().toString())) {
                onError(StringsUtils.getResourceString(R.string.username_error));
                return;
            }
            if (TextUtils.isEmpty(etPassword.getText().toString())) {
                onError(StringsUtils.getResourceString(R.string.password_error));
                return;
            }

            mPresenter.login(etPhoneNum.getText().toString(), etPassword.getText().toString());
        } else if (rippleView.getId() == R.id.rv_setting) {
            startActivity(new Intent(this, IPSettingActivity.class));
        }
    }
}
