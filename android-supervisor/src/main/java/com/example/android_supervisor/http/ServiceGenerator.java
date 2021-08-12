package com.example.android_supervisor.http;

import android.content.Context;
import android.text.TextUtils;
import com.example.android_supervisor.BuildConfig;
import com.example.android_supervisor.config.AppConfig;
import com.example.android_supervisor.config.ServiceConfig;
import com.example.android_supervisor.http.oauth.AccessToken;
import com.example.android_supervisor.http.oauth.OauthInterceptor;
import com.example.android_supervisor.utils.Environments;
import com.ihsanbal.logging.Level;
import com.ihsanbal.logging.LoggingInterceptor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import io.reactivex.schedulers.Schedulers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.internal.platform.Platform;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author yj
 */
public class ServiceGenerator {
    private static final long TIME_OUT = 18000;
    private static Map<Class<?>, ServiceConfig> serviceConfigMap;
    private static Retrofit retrofit;

    private static Class mCurrentService;
    private static List<ServiceConfig> srvConfigs;//配置库

    static ThreadLocal<Class> threadLocal = new ThreadLocal();

    public static synchronized <T> T create(Class<T> service) {
        if (retrofit == null) {
            throw new NullPointerException("请先initialize()");
        }
        mCurrentService = service;
        threadLocal.set(mCurrentService);
        return retrofit.create(service);
    }

    public static void initialize(Context context) {
        loadUrlConfig();
        createRetrofit(context);
    }

    private static void loadUrlConfig() {
        serviceConfigMap = new HashMap<>();
        srvConfigs = AppConfig.getLocalServiceConfigs();
        for (ServiceConfig srvConfig : srvConfigs) {
            String srvClassName = srvConfig.getClassName();
            try {
                Class<?> srvClass = Class.forName(srvClassName);
                serviceConfigMap.put(srvClass, srvConfig);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private static synchronized void createRetrofit(final Context context) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(TIME_OUT, TimeUnit.MILLISECONDS);
        builder.readTimeout(18000, TimeUnit.MILLISECONDS);
        builder.writeTimeout(18000, TimeUnit.MILLISECONDS);
        //添加应用拦截器
        builder.addInterceptor(new NetInterceptor(context,srvConfigs,serviceConfigMap));//NetUrlInterceptor

//        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
//        if (BuildConfig.DEBUG) {
//            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        } else {
//            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
//        }
//        builder.addInterceptor(loggingInterceptor);

        builder.addInterceptor(new LoggingInterceptor.Builder()
                .loggable(BuildConfig.DEBUG)
                .setLevel(Level.BASIC)
                .log(Platform.INFO)
                .request("Request")
                .response("Response")
                .build());


        AccessToken accessToken = AccessToken.getInstance(context);
        builder.addNetworkInterceptor(new OauthInterceptor(accessToken));
        OkHttpClient okHttpClient = builder.build();

        retrofit = new Retrofit
                .Builder()
                .client(okHttpClient)
                .baseUrl("https://zhcg.cszhx.top")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build();

    }

//    private static boolean checkAuthorization(Interceptor.Chain chain) throws IOException {
//
//        Request request = chain.request();
//        Response response = chain.proceed(request);
//
//        if (response.code() == 401) {
//            // 这里应该调用自己的刷新token的接口
//            // 这里发起的请求是同步的，刷新完成token后再增加到header中
//            // 这里抛出的错误会直接回调 onError
////                String token = refreshToken();
//            String token = Credentials.basic("userName", "password", Charset.forName("UTF-8"));
//            // 创建新的请求，并增加header
//            Request retryRequest = chain.request()
//                    .newBuilder()
//                    .header("Authorization", token)
//                    .build();
//            // 再次发起请求
//            return chain.proceed(retryRequest);
//        }
//    }


    private static void decideCaseCheckUrl(HttpUrl baseUrl, String requestMethod, boolean isAddEvent) {

        if (baseUrl == null || requestMethod == null) {
            return;
        }
        if (Environments.isCaseCheck && !TextUtils.isEmpty(Environments.caseCheckUrl)) {
            if ("POST".equalsIgnoreCase(requestMethod) && isAddEvent) {
                //判断是 案件效验 重新构造案件效验url
                baseUrl = HttpUrl.parse(Environments.caseCheckUrl);
            }
        }

    }


}
