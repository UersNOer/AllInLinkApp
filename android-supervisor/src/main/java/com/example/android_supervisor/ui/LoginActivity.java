package com.example.android_supervisor.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arcsoft.arcfacedemo.activity.RegisterAndRecognizeActivity2;
import com.arcsoft.arcfacedemo.common.FaceMangaer;
import com.arcsoft.arcfacedemo.model.UserFace;
import com.example.android_supervisor.BuildConfig;
import com.example.android_supervisor.Presenter.LoginPresenter;
import com.example.android_supervisor.Presenter.LoginPresenter.LoginCallBack;
import com.example.android_supervisor.R;
import com.example.android_supervisor.biometriclib.BiometricPromptManager;
import com.example.android_supervisor.common.UserSession;
import com.example.android_supervisor.utils.DevLocalHostManager;
import com.example.android_supervisor.utils.DialogUtils;
import com.example.android_supervisor.utils.LogUtils;
import com.example.android_supervisor.utils.LoginMethodConfig;
import com.example.android_supervisor.utils.SystemUtils;
import com.example.android_supervisor.utils.ToastUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;


public class LoginActivity extends BaseActivity {
    private static final String TAG = "LoginActivity";

    @BindView(R.id.et_login_username)
    EditText etUser;

    @BindView(R.id.et_login_password)
    EditText etPwd;

    @BindView(R.id.tv_login_register)
    TextView tvRegister;

    @BindView(R.id.tv_login_version)
    TextView tvVersion;

//    @BindView(R.id.login_type)
//    TextView tvLoginType;

    @BindView(R.id.time_btn)
    Button btnTime;

    @BindView(R.id.iv_login_show_pwd)
    AppCompatImageView ImgPassword;


    @BindView(R.id.lin_container)
    LinearLayout lin_container;

    @BindView(R.id.iv_login_fingerprint)
    AppCompatImageView iv_login_fingerprint;

    @BindView(R.id.iv_login_faceprint)
    AppCompatImageView iv_login_faceprint;

    @BindView(R.id.iv_login_identifyingCode)
    AppCompatImageView iv_login_identifyingCode;


    @BindView(R.id.tv_login_other)
    TextView tv_login_other;



    private String username;
    private String password;
    private String userId;
    private String fileServer;

    private int typeStatu = 0;//0账号密码登录，1短信验证码登录

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor();
       // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        getTitleBar().setVisibility(View.GONE);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initData();
        initLoginMethod();

    }

    private void initLoginMethod() {
        iv_login_fingerprint.setVisibility(LoginMethodConfig.getLoginFigger(this)?View.VISIBLE:View.GONE);
        iv_login_faceprint.setVisibility(LoginMethodConfig.getLoginFace(this)?View.VISIBLE:View.GONE);
        iv_login_identifyingCode.setVisibility(LoginMethodConfig.getLoginPhone(this)?View.VISIBLE:View.GONE);
        if (!LoginMethodConfig.getLoginFigger(this) && !LoginMethodConfig.getLoginFace(this) && !LoginMethodConfig.getLoginPhone(this)){
            tv_login_other.setVisibility(View.GONE);
        }else {
            tv_login_other.setVisibility(View.VISIBLE);
        }

    }

    private void initData() {
        tvVersion.setText("版本：" + BuildConfig.VERSION_NAME);
//        tvLoginType.setText("短信验证码登录");
        tvRegister.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvRegister.getPaint().setAntiAlias(true);
        etUser.setText(UserSession.getUserName(this));
        etPwd.setText(UserSession.getUserPwd(this));
        loadLocalHostServerConfig();
        //初始化人脸数据到内存
    }

    /**
     * 加载本地 LocalHost 服务器的配置信息
     */
    private void loadLocalHostServerConfig() {
        try {
            Map<String, String> configMap = DevLocalHostManager.loadDevLocalHostServerConfig(this);
            DevLocalHostManager.putHashMapData(this, DevLocalHostManager.KEY_DEV_LOCALHOST_SERVER_CONFIG, configMap);
        } catch (IOException e) {
            LogUtils.e(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    protected boolean isEnableAppLock() {
        return false;
    }


    private int mClickCount = 0;//累计计数变量
    private long mClickTime = 0L;
    public static final int CLICKED_NUM = 6;//点击次数

    @OnClick(R.id.iv_login_logo)
    public void onLogoClick(View v) {
        if (mClickTime == 0) {
            mClickTime = System.currentTimeMillis();
        }
        if (System.currentTimeMillis() - mClickTime > 500) {
            mClickCount = 0;
        } else {
            mClickCount++;
        }
        mClickTime = System.currentTimeMillis();
        if (mClickCount == CLICKED_NUM) { //避免重复启动多个 Activity
            Intent intent = new Intent(this, ServiceSettingActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//避免重复启动多个 Activity
            startActivity(intent);
        }
    }

//    @OnClick(R.id.login_type)
//    public void onChangeLoginType(View v) {
//        if (typeStatu == 0) {
//            tvLoginType.setText("账号密码登录");
//            typeStatu = 1;
//            btnTime.setVisibility(View.VISIBLE);
//            ImgPassword.setVisibility(View.GONE);
//        } else {
//            tvLoginType.setText("短信验证码登录");
//            typeStatu = 0;
//            ImgPassword.setVisibility(View.VISIBLE);
//            btnTime.setVisibility(View.GONE);
//        }
//    }

    @OnClick(R.id.tv_login_register)
    public void onRegister(View v) {
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);//避免重复启动多个 Activity
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @OnClick(R.id.iv_login_clear_name)
    public void onClearName(View v) {
        etUser.setText("");
    }

    @OnClick(R.id.time_btn)
    public void onTime(View v) {
        final int count = 60;//倒计时10秒
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(count + 1)
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(Long aLong) throws Exception {
                        return count - aLong;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        btnTime.setEnabled(false);

                    }
                }).subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Long num) {
                btnTime.setText("剩余" + num + "秒");
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {
                //回复原来初始状态
                btnTime.setEnabled(true);
                btnTime.setText("获取验证码");
            }
        });
    }

    @OnClick(R.id.iv_login_show_pwd)
    public void onShowPwd(View v) {
        if (v.isSelected()) {
            v.setSelected(false);
            ((ImageView) v).setImageResource(R.drawable.ic_pwd_show);
            etPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else {
            v.setSelected(true);
            ((ImageView) v).setImageResource(R.drawable.ic_pwd_hide);
            etPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }
    }

    @OnClick(R.id.btn_login)
    public void onLogin(View v) {
        SystemUtils.hideSoftInput(this, v);
        username = etUser.getText().toString().trim();
        password = etPwd.getText().toString().trim();

        if (validateParams()) {
            login();
        }
    }

    private boolean validateParams() {
        if (TextUtils.isEmpty(username)) {
            ToastUtils.show(this, "请输入用户名");
            etUser.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            ToastUtils.show(this, "请输入密码");
            etPwd.requestFocus();
            return false;
        }
        return true;
    }


    @OnClick(R.id.iv_login_identifyingCode)
    public void loginCode(View v) {
        Intent intent = new Intent(LoginActivity.this, VerificationCodeLogin.class);
        startActivity(intent);
    }

    @OnClick(R.id.iv_login_faceprint)
    public void login_faceprint(View v) {
        if (!TextUtils.isEmpty(UserSession.getUserName(this)) && !TextUtils.isEmpty(UserSession.getUserPwd(this))) {
            //启动人脸登录 获取服务器注册人脸特征值
            List<UserFace> userFaces = FaceMangaer.getInstance(this).getfaceFeature(UserSession.getUserName(this));
            if (userFaces.size() == 0) {
                ToastUtils.show(this, "当前未绑定过人脸登录");
            } else {
                FaceMangaer.getInstance(this).initfaceFeature(UserSession.getUserName(this));
                Intent intent = new Intent(LoginActivity.this, RegisterAndRecognizeActivity2.class);
                intent.putExtra("isShowRegister", false);
                intent.putExtra("userName", UserSession.getUserName(this));
                startActivityForResult(intent, 0);
            }
        } else {
            ToastUtils.show(this, "当前无账号，请先登录后操作");
        }

    }


    @OnClick(R.id.iv_login_fingerprint)
    public void onFingerprint(View v) {
        final String account_userName = UserSession.getUserName(this);
        final String account_pwd = UserSession.getUserPwd(this);
        if (TextUtils.isEmpty(account_userName) || TextUtils.isEmpty(account_pwd)) {
            DialogUtils.show(this, "首次使用请先通过账号密码进行登录");
            return;
        }

        BiometricPromptManager mManager = new BiometricPromptManager(this);
        if (!mManager.isHardwareDetected()) {
            ToastUtils.show(this, "当前设备不支持指纹登录");
            return;
        }
        if (!mManager.hasEnrolledFingerprints()) {
            ToastUtils.show(this, "当前设备没有录入指纹，请进入系统设置开启并添加一个指纹");
            return;
        }
        if (!mManager.isKeyguardSecure()) {
            ToastUtils.show(this, "当前设备没有开启锁屏，请进入系统设置开启锁屏");
            return;
        }
        mManager.authenticate(new BiometricPromptManager.OnBiometricIdentifyCallback() {
            @Override
            public void onUsePassword() {
            }

            @Override
            public void onSucceeded() {
                username = account_userName;
                password = account_pwd;
                login();
            }

            @Override
            public void onFailed() {
                ToastUtils.show(LoginActivity.this, "指纹验证失败");
            }

            @Override
            public void onError(int code, String reason) {
                ToastUtils.show(LoginActivity.this, "指纹验证出错");
            }

            @Override
            public void onCancel() {
            }
        });
    }

    LoginPresenter presenter = new LoginPresenter();
    @SuppressWarnings("unchecked")
    private void login() {


        presenter.login(LoginActivity.this,username, password, new LoginCallBack() {

            @Override
            public void onSuccess() {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure() {
                Log.i(TAG, "onFailure: +++++++++++++");
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode ==Activity.RESULT_OK){
//            lin_container.removeAllViews();
//            ImageView imageView  = new ImageView(this);
//            imageView.setBackgroundResource(R.drawable.splash_theme);
//            R.layout.activity_splash
            setContentView(View.inflate(LoginActivity.this,R.layout.activity_splash,null));
//            lin_container.addView(imageView);
//            lin_container.setGravity(Gravity.CENTER);

            presenter.login(LoginActivity.this,UserSession.getUserName(this),
                    UserSession.getUserPwd(this), new LoginCallBack() {

                        @Override
                        public void onSuccess() {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onFailure() {
                            setContentView(mRoot);
                        }


                    });
        }
    }



}
