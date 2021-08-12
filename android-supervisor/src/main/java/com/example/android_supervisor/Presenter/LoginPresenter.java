package com.example.android_supervisor.Presenter;

import android.content.Context;

import com.ihsanbal.logging.Level;
import com.ihsanbal.logging.LoggingInterceptor;
import com.topevery.common.splite.Storage;
import com.example.android_supervisor.BuildConfig;
import com.example.android_supervisor.common.UserSession;
import com.example.android_supervisor.entities.UserBase;
import com.example.android_supervisor.http.ServiceGenerator;
import com.example.android_supervisor.http.common.ProgressTransformer;
import com.example.android_supervisor.http.common.UrlTranslation;
import com.example.android_supervisor.http.oauth.AccessToken;
import com.example.android_supervisor.http.request.JsonBody;
import com.example.android_supervisor.http.response.Response;
import com.example.android_supervisor.http.response.ResponseObserver;
import com.example.android_supervisor.http.service.BasicService;
import com.example.android_supervisor.http.service.OauthService;
import com.example.android_supervisor.ui.view.ProgressText;
import com.example.android_supervisor.utils.AESUtils;
import com.example.android_supervisor.utils.Environments;
import com.example.android_supervisor.utils.JsonUtils;
import com.example.android_supervisor.utils.LogUtils;
import com.example.android_supervisor.utils.OauthHostManager;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.internal.platform.Platform;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class LoginPresenter {

    public static String grant_type = "password";
    public static String client_id = "3EF436DE44754EBE99B2E64A1EBB4591";
    public static String client_secret = "lBTqrKS0kZixOFXeZ0HRng==";
    public static String scope = "app";
    public static String auth_type = "account";
    public static boolean is_encrypt = true;

    public void login(Context context, String username, String password, LoginCallBack callBack) {

        String encodedPassword = AESUtils.encode(password, "1234567887654321", "1234567887654321");

        ServiceGenerator.initialize(context);
        OauthService oauthService = ServiceGenerator.create(OauthService.class);
        Observable<ResponseBody> observable = null;
//        observable = oauthService.getToken(username, encodedPassword,
//                "password", "app-android",
//                "123456", "app",
//                true, false, "password");
//        {"access_token":"50924ac6-9cf9-4883-87fd-0b83843503ad",
//                "token_type":"bearer",
//                "refresh_token":"3298507d-9395-4164-8f8e-4f4a4cb1f47d","expires_in":3152,"scope":"app"}

        observable = oauthService.getLoginToken(username, encodedPassword,
                grant_type, client_id,
                client_secret, scope,
                auth_type, is_encrypt);
//        Map<String,String>map = new HashMap<>();
//        map.put("client_id","topevery-zhcg-platform");
//        map.put("grant_type","password");
//        map.put("password",password);
//        map.put("username",username);
//        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),new Gson().toJson(map));
//        observable = oauthService.getLoginTokenNew(requestBody);

        AccessToken.getInstance(context).delete();

        observable
                // 获取ACCESS_TOKEN
                .flatMap(new Function<ResponseBody, ObservableSource<ResponseBody>>() {
                    @Override
                    public ObservableSource<ResponseBody> apply(ResponseBody responseBody) throws Exception {
                        String result = responseBody.string();
                        JSONObject object = new JSONObject(result);
                        String accessToken = object.getString("access_token");
                        String refresh_token = object.getString("refresh_token");
                        LogUtils.d("access_token", accessToken);
                        AccessToken.getInstance(context.getApplicationContext()).set(accessToken,refresh_token);

                        //绑定acessToken用户信息和业务用户信息 bindCurrentUser
                        BasicService basicService = ServiceGenerator.create(BasicService.class);


                        return basicService.bindCurrentUser();
                    }
                })
                .flatMap(new Function<ResponseBody, ObservableSource<Response<UserBase>>>() {
                    @Override
                    public ObservableSource<Response<UserBase>> apply(ResponseBody responseBody) throws Exception {
                        if (responseBody != null) {
                            LogUtils.d("bindCurrentUser", responseBody.toString());
                        }

                        HashMap<String, String> map = new HashMap<>();
                        map.put("username", UserSession.getUserName(context));
                        JsonBody jsonBody = new JsonBody(map);

                        BasicService basicService = ServiceGenerator.create(BasicService.class);
                        return basicService.getUserInfo();
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

//                        if (EMClient.getInstance().isLoggedInBefore()) {
//                            return;
//                        }
//                        final CountDownLatch countDownLatch = new CountDownLatch(1);
//                        EMClient.getInstance().login(userId, userId, new EMCallBack() {
//                            @Override
//                            public void onSuccess() {
//                                Log.d("EMClient", "onSuccess: ");
//                                EMClient.getInstance().groupManager().loadAllGroups();
//                                EMClient.getInstance().chatManager().loadAllConversations();
//                                countDownLatch.countDown();
//                            }
//
//                            @Override
//                            public void onError(int code, String error) {
//                                Log.d("EMClient", "code: " + code + error);
//                                countDownLatch.countDown();
//                            }
//
//                            @Override
//                            public void onProgress(int progress, String status) {
//                            }
//                        });
//                        countDownLatch.await();
                        UserBase userBase = response.getData();
                        if (userBase == null) {
                            throw new IllegalAccessException("请使用监督员账号登录");
                        }

                        if (userBase != null) {
//                            if (!"2".equals(userBase.getUserType())){
//                                throw new IllegalAccessException("请使用监督员账号登录");
//                            }
                            UserSession.setUserId(context.getApplicationContext(), String.valueOf(userBase.getId()));
                            UserSession.setMobile(context.getApplicationContext(), userBase.getPhone());
//                            UserSession.setRoleInfoTAG(context.getApplicationContext(), userBase.getRoleInfo().getName());
                            UserSession.setUserName(context.getApplicationContext(), username);
                            UserSession.setUserPwd(context.getApplicationContext(), password);
                            UserSession.setLoginInfo(context, JsonUtils.toJson(userBase));

                            //加载入内存
                            Environments.userBase = userBase;
                            Environments.userBase.setPwd(password);


                            Storage.setUserId(Environments.userBase.getId());

                        }
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .compose(new ProgressTransformer<Response<UserBase>>(context, ProgressText.login))
                .subscribe(new ResponseObserver<UserBase>(context) {
                    @Override
                    public void onSuccess(UserBase data) {

                        if (callBack != null) {
                            if (data!=null){
//                                if (!"2".equals(data.getUserType()) && !"3".equals(data.getUserType())) {
//
//                                    ToastUtils.show(context,"请使用监督员账号登录");
//                                    return;
//                                }
                            }
                            callBack.onSuccess();
                        }


                    }

                    @Override
                    public void onFailure() {
                        if (callBack != null) {
                            callBack.onFailure();
                        }
                    }
                });
    }


    public void refreshToken1(Context context) {

        long token_create_time = AccessToken.getInstance(context).getToken_create_time();
        if ((System.currentTimeMillis() - token_create_time) < 1000 * 60 *30 ){
//            LogUtils.d("freshen_access_token"+ "token不重置"+ (System.currentTimeMillis() - token_create_time)/1000 );
            return;
        }

//        LogUtils.d("freshen_access_token"+ "token重置");

        String encodedPassword = AESUtils.encode(UserSession.getUserPwd(context), "1234567887654321", "1234567887654321");

        String hostAddress = OauthHostManager.getInstance(context).getApiConfig();

        Class classs = OauthService.class;
        UrlTranslation urlTranslation = (UrlTranslation) classs.getAnnotation(UrlTranslation.class);
        String prefix = urlTranslation != null ? urlTranslation.value() : "";


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(new StringBuffer(hostAddress).append("/"+prefix+"/").toString())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .client(buildHttpClient())
                .build();

        OauthService oauthService = retrofit.create(OauthService.class);

        Call<ResponseBody> call = oauthService.refreshToken(UserSession.getUserName(context), encodedPassword,
                "refresh_token", client_id,
                client_secret, scope,
                auth_type, is_encrypt,AccessToken.getInstance(context).getRefreshToken());


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try{
                    if (response != null && response.isSuccessful()) {

                        String result = response.body().string();

                        JSONObject object = new JSONObject(result);
                        String accessToken = object.getString("access_token");
                        String refresh_token = object.getString("refresh_token");
                        LogUtils.d("freshen_access_token  "+result);
                        AccessToken.getInstance(context).delete();
                        AccessToken.getInstance(context.getApplicationContext()).set(accessToken,refresh_token);

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }


    public String refreshToken(Context context) {

        String encodedPassword = AESUtils.encode(UserSession.getUserPwd(context), "1234567887654321", "1234567887654321");
        OauthService oauthService = ServiceGenerator.create(OauthService.class);
        Call<ResponseBody> call  = oauthService.getLoginToken2(UserSession.getUserName(context), encodedPassword,
                grant_type, client_id,
                client_secret, scope,
                auth_type, is_encrypt);

          try {
            retrofit2.Response<ResponseBody> response = call.execute();

            if (response != null && response.isSuccessful()) {
                String result = response.body().string();

                JSONObject object = new JSONObject(result);
                String accessToken = object.getString("access_token");
                String refresh_token = object.getString("refresh_token");
                LogUtils.d("freshen_access_token  "+accessToken);
                AccessToken.getInstance(context).delete();
                AccessToken.getInstance(context.getApplicationContext()).set(accessToken,refresh_token);

                BasicService basicService = ServiceGenerator.create(BasicService.class);
                Call<ResponseBody> call1 = basicService.bindCurrentUser1();
                retrofit2.Response<ResponseBody> response1 = call1.execute();

                if (response1 != null && response1.isSuccessful()){
                    LogUtils.d("freshen_access_token  "+response1.body().string());
                    return accessToken;
                }


            }

        } catch (Exception e) {
            e.printStackTrace();
        }


//        String encodedPassword = AESUtils.encode(UserSession.getUserPwd(context), "1234567887654321", "1234567887654321");
//
//        String hostAddress = OauthHostManager.getInstance(context).getApiConfig();
//
//        Class classs = OauthService.class;
//        UrlTranslation urlTranslation = (UrlTranslation) classs.getAnnotation(UrlTranslation.class);
//        String prefix = urlTranslation != null ? urlTranslation.value() : "";
//
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(new StringBuffer(hostAddress).append("/"+prefix+"/").toString())
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
//                .client(buildHttpClient())
//                .build();
//
//        OauthService oauthService = retrofit.create(OauthService.class);
//
//        Call<ResponseBody> call = oauthService.refreshToken(UserSession.getUserName(context), encodedPassword,
//                "refresh_token", client_id,
//                client_secret, scope,
//                auth_type, is_encrypt,AccessToken.getInstance(context).getRefreshToken());
//
//        try {
//            retrofit2.Response<ResponseBody> response = call.execute();
//
//            if (response != null && response.isSuccessful()) {
//                String result = response.body().string();
//
//                JSONObject object = new JSONObject(result);
//                String accessToken = object.getString("access_token");
//                String refresh_token = object.getString("refresh_token");
//                LogUtils.d("freshen_access_token  "+result);
//                AccessToken.getInstance(context).delete();
//                AccessToken.getInstance(context.getApplicationContext()).set(accessToken,refresh_token);
//                return accessToken;
//
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return "";

//        LogUtils.d("freshen_access_token"+ "token已失效");
//
//        String encodedPassword = AESUtils.encode(UserSession.getUserPwd(context), "1234567887654321", "1234567887654321");
//
//        String hostAddress = OauthHostManager.getInstance(context).getApiConfig();
//
//        Class classs = OauthService.class;
//        UrlTranslation urlTranslation = (UrlTranslation) classs.getAnnotation(UrlTranslation.class);
//        String prefix = urlTranslation != null ? urlTranslation.value() : "";
//
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(new StringBuffer(hostAddress).append("/"+prefix+"/").toString())
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
//                .client(buildHttpClient())
//                .build();
//
//        OauthService oauthService = retrofit.create(OauthService.class);
//
//        Call<ResponseBody> call = oauthService.executeLoginToken(UserSession.getUserName(context), encodedPassword,
//                grant_type, client_id,
//                client_secret, scope,
//                auth_type, is_encrypt);
//
//        try {
//            retrofit2.Response<ResponseBody> response = call.execute();
//            if (response != null) {
//                String result = response.body().string();
//                JSONObject object = new JSONObject(result);
//                String accessToken = object.getString("access_token");
//                LogUtils.d("freshen_access_token"+accessToken);
//                AccessToken.getInstance(context).delete();
//                AccessToken.getInstance(context.getApplicationContext()).set(accessToken);
//                return accessToken;
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        return "";


    }


    public interface LoginCallBack {


        void onSuccess();

        void onFailure();

    }


    private OkHttpClient buildHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(18000, TimeUnit.SECONDS).
                        readTimeout(18000, TimeUnit.SECONDS).
                        writeTimeout(18000, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true);

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();

        if (BuildConfig.DEBUG) {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        }
        builder.addInterceptor(loggingInterceptor);

        builder.addInterceptor(new LoggingInterceptor.Builder()
                .loggable(BuildConfig.DEBUG)
                .setLevel(Level.BASIC)
                .log(Platform.INFO)
                .request("Request")
                .response("Response")
                .build());


        return builder.build();
    }

}
