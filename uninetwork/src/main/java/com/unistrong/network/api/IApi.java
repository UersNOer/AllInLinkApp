package com.unistrong.network.api;


import com.unistrong.network.provider.INetProvider;

import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * Description:定义api的接口 IApi
 * Created by ltd ON 2020/5/25
 * Phone:18600920091
 * Email:td.liu@unistrong.com
 */
public interface IApi {

    class CommonProviderHolder {
        private INetProvider mProvider;

        public void setProvider(INetProvider provider) {
            mProvider = provider;
        }

        public INetProvider getProvider() {
            return mProvider;
        }
    }

    Map<String, INetProvider> PROVIDER_MAP = new HashMap<>();
    Map<String, Retrofit> RETROFIT_MAP = new HashMap<>();
    Map<String, OkHttpClient> CLIENT_MAP = new HashMap<>();

    /**
     * 返回Retrofit map
     *
     * @return
     */
    Map<String, Retrofit> getRetrofitMap();

    /**
     * 返回Client map
     *
     * @return
     */
    Map<String, OkHttpClient> getClientMap();

    /**
     * 返回Provider map
     *
     * @return
     */
    Map<String, INetProvider> getProviderMap();

    /**
     * 清除缓存
     */
    void clearCache();

    /**
     * 注册公共的Provider
     *
     * @param provider
     */
    void registerProvider(INetProvider provider);

    /**
     * 注册指定的Provider
     *
     * @param baseUrl
     * @param provider
     */
    void registerProvider(String baseUrl, INetProvider provider);

    /**
     * 利用Retrofit创建Service
     *
     * @param baseUrl
     * @param service
     * @param useRx
     * @param <S>
     * @return
     */
    <S> S getService(String baseUrl, Class<S> service, boolean useRx);

    /**
     * 利用Retrofit创建Service
     *
     * @param baseUrl
     * @param service
     * @param useRx
     * @param connectTimeout
     * @param readTimeout
     * @param <S>
     * @return
     */
    <S> S getService(String baseUrl, Class<S> service, boolean useRx, long connectTimeout, long readTimeout);

    /**
     * 获取公用的Provider
     *
     * @return
     */
    INetProvider getCommonProvider();

}
