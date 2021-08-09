package com.unistrong.network.provider;


import com.unistrong.network.IRequestHandler;
import com.unistrong.network.NetError;

import okhttp3.CookieJar;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 * Description: 定义NetProvider的接口类 INetProvider
 * Created by ltd ON 2020/5/25
 * Phone:18600920091
 * Email:td.liu@unistrong.com
 */
public interface INetProvider {
    //    使用框架net部分有一个必要条件:实体需要实现IModel接口
    //    支持多个baseUrl，并可对每个url配置自己的provider
    //    可配置公共provider
    //    对一个网络请求的基本配置，一个baseUrl对应一个provider，多个baseUrl也可对应一个公共的provider.

    /**
     * 配置拦截器
     *
     * @return
     */
    Interceptor[] configInterceptors();

    /**
     * 配置https
     *
     * @param builder
     */
    void configHttps(OkHttpClient.Builder builder);

    /**
     * 配置cookie
     *
     * @return
     */
    CookieJar configCookie();

    /**
     * 配置通用请求handler
     *
     * @return
     */
    IRequestHandler configHandler();

    /**
     * 配置连接超时时间
     *
     * @return
     */
    long configConnectTimeoutMills();

    /**
     * 配置读取超时时间
     *
     * @return
     */
    long configReadTimeoutMills();

    /**
     * 配置是否可以打印log
     *
     * @return
     */
    boolean configLogEnable();

    /**
     * 统一异常处理
     *
     * @param error
     * @return
     */
    boolean handleError(NetError error);

    /**
     * 是否允许分发数据传输进度
     *
     * @return
     */
    boolean dispatchProgressEnable();
}
