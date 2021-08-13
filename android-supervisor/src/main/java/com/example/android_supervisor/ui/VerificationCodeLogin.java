package com.example.android_supervisor.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.topevery.common.splite.Storage;
import com.example.android_supervisor.R;
import com.example.android_supervisor.common.UserSession;
import com.example.android_supervisor.entities.UserBase;
import com.example.android_supervisor.http.ServiceGenerator;
import com.example.android_supervisor.http.common.ProgressTransformer;
import com.example.android_supervisor.http.oauth.AccessToken;
import com.example.android_supervisor.http.response.Response;
import com.example.android_supervisor.http.response.ResponseObserver;
import com.example.android_supervisor.http.service.BasicService;
import com.example.android_supervisor.http.service.OauthService;
import com.example.android_supervisor.ui.view.ProgressText;
import com.example.android_supervisor.utils.Environments;
import com.example.android_supervisor.utils.LogUtils;
import com.example.android_supervisor.utils.ToastUtils;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;


public class VerificationCodeLogin extends BaseActivity {

    @BindView(R.id.et_phone)
    EditText et_phone;

    @BindView(R.id.et_code)
    EditText et_code;

    @BindView(R.id.bt_sent)
    TextView bt_sent;

    @BindView(R.id.btn_login)
    Button btn_login;

    CountDownTimer timer = new CountDownTimer(6000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            bt_sent.setText(millisUntilFinished/1000 + "秒");
        }

        @Override
        public void onFinish() {
            bt_sent.setEnabled(true);
            bt_sent.setText("发送验证码");
            bt_sent.setTextColor(getResources().getColor(R.color.red));
            bt_sent.setClickable(true);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_code_login);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {

        bt_sent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et_phone.getText().toString().trim())){
                    ToastUtils.show(VerificationCodeLogin.this,"请输入手机号码");
                    return;
                }
                bt_sent.setTextColor(getResources().getColor(R.color.text_quaternary));
                bt_sent.setClickable(false);

                timer.start();
                OauthService oauthService = ServiceGenerator.create(OauthService.class);

                oauthService.getPhoneVerifyCode(et_phone.getText().toString().trim())
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(new ProgressTransformer<Response>(VerificationCodeLogin.this, ProgressText.login))
                        .subscribe(new ResponseObserver(VerificationCodeLogin.this) {

                            @Override
                            public void onSuccess(Object data) {
                                ToastUtils.show(VerificationCodeLogin.this,"发送成功");
                            }
                        });

            }
        });
    }


    //     http://192.168.10.131:5000/oauth/token?
    //     username=13187014992
    //     &grant_type=password
    //     &client_id=1666367FA3BA4E319588EA08B6E27279
    //     &client_secret=lBTqrKS0kZixOFXeZ0HRng==
    //     &scope=app&auth_type=phone&verifyCode=3365
    @OnClick(R.id.btn_login)
    public void onLoginClick(){
        OauthService oauthService = ServiceGenerator.create(OauthService.class);

        Observable<ResponseBody> observable = oauthService.getLoginToken(null, null,
                null, null,
                null, null,
                null, true);


        observable.flatMap(new Function<ResponseBody, ObservableSource<Response<UserBase>>>() {
                    @Override
                    public ObservableSource<Response<UserBase>> apply(ResponseBody responseBody) throws Exception {
                        String result = responseBody.string();
                        JSONObject object = new JSONObject(result);
                        String accessToken = object.getString("access_token");
                        String refresh_token = object.getString("refresh_token");
                        LogUtils.d("access_token", accessToken);
                        AccessToken.getInstance(getApplicationContext()).set(accessToken,refresh_token);

                        BasicService platformService = ServiceGenerator.create(BasicService.class);

                        return platformService.getUserInfo();
                    }
                })
                .doOnNext(new Consumer<Response<UserBase>>() {
                    @Override
                    public void accept(Response<UserBase> response) throws Exception {
                        if (!response.isSuccess()) {
                            throw new IllegalAccessException(response.getMessage());
                        }
                        if (response.getData() == null) {
                            throw new IllegalAccessException("无法获取用户信息");
                        }
                        //  userId = response.getData().getId();

                        UserBase userBase = response.getData();
                        if (userBase == null){
                            throw new IllegalAccessException("请使用监督员账号登录");
                        }

                        if (userBase != null) {
//                            if (!"2".equals(userBase.getUserType())){
//                                throw new IllegalAccessException("请使用监督员账号登录");
//                            }
                            UserSession.setUserId(getApplicationContext(), String.valueOf(userBase.getId()));
                            UserSession.setMobile(getApplicationContext(), userBase.getPhone());
                            //   UserSession.setRoleInfoTAG(getApplicationContext(), userBase.getRoleInfo().getName());
                            UserSession.setUserName(getApplicationContext(), userBase.getAccount());

                            UserSession.setUserPwd(getApplicationContext(), userBase.getPwd());//注意后台返回

                            //加载入内存
                            Environments.userBase = userBase;
                            Environments.userBase.setPwd(userBase.getPwd());
                            Storage.setUserId(Environments.userBase.getId());
                        }
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .compose(new ProgressTransformer<Response>(this, ProgressText.login))
                .subscribe(new ResponseObserver(this) {
                    @Override
                    public void onSuccess(Object data) {
                        Intent intent = new Intent(VerificationCodeLogin.this, MainActivity.class);
                         startActivity(intent);
                         finish();
                    }

                    @Override
                    public void onFailure() {

                    }
                });



    }
}
