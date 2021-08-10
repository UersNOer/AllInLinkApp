package com.unistrong.network.api.impl;

import android.text.TextUtils;
import android.util.Log;


import com.unistrong.network.HttpLoggingInterceptor;
import com.unistrong.network.IRequestHandler;
import com.unistrong.network.api.IApi;
import com.unistrong.network.interceptor.ErrorInterceptor;
import com.unistrong.network.interceptor.LogInterceptor;
import com.unistrong.network.interceptor.XInterceptor;
import com.unistrong.network.progress.ProgressHelper;
import com.unistrong.network.provider.INetProvider;
import com.unistrong.network.provider.NetProviderFactory;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.CookieJar;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Description:默认的Api实现 DefaultApi
 * Created by ltd ON 2020/5/25
 * Phone:18600920091
 * Email:td.liu@unistrong.com
 */
public class DefaultApi implements IApi {

    private CommonProviderHolder mProviderHolder;

    /**
     * 默认超时时间
     */
    private long connectTimeoutMills = 10 * 1000L;
    private long readTimeoutMills = 10 * 1000L;

    public DefaultApi() {
        this.registerProvider(NetProviderFactory.getInstance());
    }

    @Override
    public <S> S getService(String baseUrl, Class<S> service, boolean useRx) {
        return getRetrofit(baseUrl, true).create(service);
    }

    @Override
    public <S> S getService(String baseUrl, Class<S> service, boolean useRx, long connectTimeout, long readTimeout) {
        connectTimeoutMills = connectTimeout;
        readTimeoutMills = readTimeout;
        return getRetrofit(baseUrl, true).create(service);
    }

    @Override
    public void registerProvider(INetProvider provider) {
        if (mProviderHolder == null) {
            mProviderHolder = new CommonProviderHolder();
        }
        mProviderHolder.setProvider(provider);
    }

    @Override
    public void registerProvider(String baseUrl, INetProvider provider) {
        PROVIDER_MAP.put(baseUrl, provider);
    }


    public Retrofit getRetrofit(String baseUrl, boolean useRx) {
        return getRetrofit(baseUrl, null, useRx);
    }


    public Retrofit getRetrofit(String baseUrl, INetProvider provider, boolean useRx) {
        if (TextUtils.isEmpty(baseUrl)) {
            throw new IllegalStateException("baseUrl can not be null");
        }
        if (RETROFIT_MAP.get(baseUrl) != null) {
            return RETROFIT_MAP.get(baseUrl);
        }

        if (provider == null) {
            provider = PROVIDER_MAP.get(baseUrl);
            if (provider == null) {
                provider = mProviderHolder.getProvider();
            }
        }
        checkProvider(provider);

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(getClient(baseUrl, provider))
                .addConverterFactory(GsonConverterFactory.create());
        if (useRx) {
            builder.addCallAdapterFactory(RxJavaCallAdapterFactory.create());
        }

        Retrofit retrofit = builder.build();
        RETROFIT_MAP.put(baseUrl, retrofit);
        PROVIDER_MAP.put(baseUrl, provider);
        //map :http://117.50.31.202:8084 http://117.50.31.202:8085
        return retrofit;
    }

    private OkHttpClient getClient(String baseUrl, INetProvider provider) {
        if (TextUtils.isEmpty(baseUrl)) {
            throw new IllegalStateException("baseUrl can not be null");
        }
        if (CLIENT_MAP.get(baseUrl) != null) {
            return CLIENT_MAP.get(baseUrl);
        }

        checkProvider(provider);

        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.connectTimeout(provider.configConnectTimeoutMills() != 0
                ? provider.configConnectTimeoutMills()
                : connectTimeoutMills, TimeUnit.MILLISECONDS);
        builder.readTimeout(provider.configReadTimeoutMills() != 0
                ? provider.configReadTimeoutMills() : readTimeoutMills, TimeUnit.MILLISECONDS);

        CookieJar cookieJar = provider.configCookie();
        if (cookieJar != null) {
            builder.cookieJar(cookieJar);
        }
        provider.configHttps(builder);

        IRequestHandler handler = provider.configHandler();
        if (handler != null) {
            builder.addInterceptor(new XInterceptor(handler));
        }

        if (provider.dispatchProgressEnable()) {
            builder.addInterceptor(ProgressHelper.get().getInterceptor());
        }

        Interceptor[] interceptors = provider.configInterceptors();
        if (!(interceptors == null || interceptors.length == 0)) {
            for (Interceptor interceptor : interceptors) {
                builder.addInterceptor(interceptor);
            }
        }

        if (provider.configLogEnable()) {
            LogInterceptor logInterceptor = new LogInterceptor();
            builder.addInterceptor(logInterceptor);
        }
        builder.addInterceptor(new ErrorInterceptor());
        OkHttpClient client = builder.build();
        CLIENT_MAP.put(baseUrl, client);
        PROVIDER_MAP.put(baseUrl, provider);

        return client;
    }


    private void checkProvider(INetProvider provider) {
        if (provider == null) {
            throw new IllegalStateException("must register provider first");
        }
    }

    @Override
    public INetProvider getCommonProvider() {
        if (mProviderHolder == null) {
            return null;
        } else {
            return mProviderHolder.getProvider();
        }
    }

    @Override
    public Map<String, Retrofit> getRetrofitMap() {
        return RETROFIT_MAP;
    }

    @Override
    public Map<String, OkHttpClient> getClientMap() {
        return CLIENT_MAP;
    }

    @Override
    public Map<String, INetProvider> getProviderMap() {
        return null;
    }

    @Override
    public void clearCache() {
        PROVIDER_MAP.clear();
        RETROFIT_MAP.clear();
        CLIENT_MAP.clear();
    }

//    /**
//     * 线程切换
//     *
//     * @return
//     */
//    public static <T extends IModel> Sche<T, T> getScheduler() {
//        return new FlowableTransformer<T, T>() {
//            @Override
//            public Publisher<T> apply(Flowable<T> upstream) {
//                return upstream.subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread());
//            }
//        };
//    }

    /**
     * 异常处理变换
     *
     * @return
     */
//    public static <T extends IModel> FlowableTransformer<T, T> getApiTransformer() {
//
//        return new FlowableTransformer<T, T>() {
//            @Override
//            public Publisher<T> apply(Flowable<T> upstream) {
//                return upstream.flatMap(new Function<T, Publisher<T>>() {
//                    @Override
//                    public Publisher<T> apply(T model) throws Exception {
//                        if (model == null || model.isMetaNull()) {
//                            return Flowable.error(new NetError("服务器繁忙，请稍后再试!", NetError.ErrorType.NoDataError));
//                        } else if (model.isError()) {
//                            return Flowable.error(new NetError(model.getErrorMsg(), NetError.ErrorType.ServerError));
//                        } else {
//                            return Flowable.just(model);
//                        }
//                    }
//                });
//            }
//        };
//    }

}
