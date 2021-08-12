package com.example.android_supervisor.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.example.android_supervisor.BuildConfig;
import com.example.android_supervisor.R;
import com.example.android_supervisor.entities.ConstantEntity;
import com.example.android_supervisor.entities.ServicesConfig;
import com.example.android_supervisor.http.common.ProgressTransformer;
import com.example.android_supervisor.http.response.Response;
import com.example.android_supervisor.http.response.ResponseObserver;
import com.example.android_supervisor.http.service.PublicService;
import com.example.android_supervisor.ui.view.ProgressText;
import com.example.android_supervisor.utils.OauthHostManager;
import com.example.android_supervisor.utils.SystemUtils;
import com.example.android_supervisor.utils.ToastUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 授权页面
 */
public class OauthCodeActivity extends BaseActivity {

    public static final int CONNECT_TIMEOUT = 10;
    public static final int READ_TIMEOUT = 10;
    public static final int WRITE_TIMEOUT = 10;

    @BindView(R.id.oauth_page)
    RelativeLayout oauthPage;

    @BindView(R.id.et_oauth_code)
    EditText etCode;

    @BindView(R.id.tv_exp1)
    TextView tvExp1;

    @BindView(R.id.tv_exp2)
    TextView tvExp2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        setContentView(R.layout.activity_oauth_code);
        ButterKnife.bind(this);
        setExpTextView();

    }

    private void setExpTextView() {
        SpannableString msp = new SpannableString("1、您只有通过授权码才能够使用数字城管app");
        SpannableString msp2 = new SpannableString("2、要获取授权码请于管理员联系");
        msp.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.c_3285ff)), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        msp2.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.c_3285ff)), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvExp1.setText(msp);
        tvExp2.setText(msp2);
    }


    @OnClick(R.id.iv_code_clear_name)
    public void onClearCode(View v) {
        etCode.setText("");
    }

    @OnClick(R.id.btn_oauth)
    public void onOauth(View v) {
        SystemUtils.hideSoftInput(this, v);
        String code = etCode.getText().toString().trim();
        if (validateParams(code)) {
            obtainOauthUrlByCode(code);
        }
    }


    //通过code获取配置Url------服务器地址为后台返回，根据code就是邀请码判断，，在Envment里面设置boolean值，false为后台返回地址，ture为assets里面配置的服务路径
    private void obtainOauthUrlByCode(String code) {

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConstantEntity.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .client(buildHttpClient())
                .build();

        PublicService publicService = retrofit.create(PublicService.class);
        publicService.obtainOauthCode(code)
                .compose(this.<Response<List<ServicesConfig>>>bindToLifecycle())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(new ProgressTransformer<Response<List<ServicesConfig>>>(this, ProgressText.load))
                .subscribe(new ResponseObserver<List<ServicesConfig>>(OauthCodeActivity.this) {
                    @Override
                    public void onSuccess(List<ServicesConfig> data) {

                        if (data != null && data.size() < 3) {
                            showConfigError();
                            return;
                        }

                        for (ServicesConfig servicesConfig : data) {

                            if (servicesConfig.getHostAddress().isEmpty()) {
                                showConfigError();
                                return;
                            }
                            OauthHostManager.getInstance(OauthCodeActivity.this).setOauthHost(servicesConfig);
//
                        }
//
                        Intent intent = new Intent(OauthCodeActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
    }

    private OkHttpClient buildHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS).
                        readTimeout(READ_TIMEOUT, TimeUnit.SECONDS).
                        writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();

        if (BuildConfig.DEBUG) {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        }

        builder.addInterceptor(loggingInterceptor);
        return builder.build();
    }

    private boolean validateParams(String code) {
        if (TextUtils.isEmpty(code)) {
            ToastUtils.show(this, "请输入授权码");
            etCode.requestFocus();
            return false;
        }
        return true;
    }

    public void showConfigError() {
        ToastUtils.show(OauthCodeActivity.this, "当前服务器配置出错,请联系管理员");
    }

}
