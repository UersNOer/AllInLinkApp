package com.unistong.netword.modules;

import android.util.Log;


import com.unistong.netword.modules.logging.Level;
import com.unistong.netword.modules.logging.LoggingInterceptor;
import com.unistong.netword.queries.HttpLoggingInterceptor;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.platform.Platform;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtils {

    private static final boolean isLog = true;
    private static final Charset UTF8 = Charset.forName("UTF-8");
    public static String baseUrl = "";

    public static ApiService getInstance() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(_OkHttpClick())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(ApiService.class);
    }

    private static OkHttpClient _OkHttpClick() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(60, TimeUnit.SECONDS);//连接 超时时间
        builder.writeTimeout(60, TimeUnit.SECONDS);//写操作 超时时间
        builder.readTimeout(60, TimeUnit.SECONDS);//读操作 超时时间
        builder.retryOnConnectionFailure(true);//错误重连
        Interceptor headerInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request originalRequest = chain.request();
                Request.Builder requestBuilder = originalRequest.newBuilder()
                        .addHeader("DeviceFlag", "Android")
                        .addHeader("Accept-Encoding", "gzip")
                        .addHeader("Accept", "application/json")
                        .addHeader("Content-Type", "application/json; charset=utf-8");
                requestBuilder.method(originalRequest.method(), originalRequest.body());
                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        };
        HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BODY;
        //新建log拦截器r
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.e("TAG", "OkHttp====Message:" + message);

            }

        });
        loggingInterceptor.setLevel(level);
//        builder.addInterceptor(loggingInterceptor);


        LoggingInterceptor loggingInterceptor1 = new LoggingInterceptor
                .Builder()//构建者模式
                .loggable(isLog) //是否开启日志打印
                .setLevel(Level.BASIC) //打印的等级
                .log(Platform.INFO) // 打印类型
                .request("Request") // request的Tag
                .response("Response")// Response的Tag
//                .addHeader("log-header", "I am the log request header.") // 添加打印头, 注意 key 和 value 都不能是中文
                .build();
        builder.addInterceptor(loggingInterceptor1);
        builder.addInterceptor(headerInterceptor);
        return builder.build();
    }

}

