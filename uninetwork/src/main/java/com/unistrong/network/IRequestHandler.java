package com.unistrong.network;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Description:定义处理Request的Handler接口 IRequestHandler
 * Created by ltd ON 2020/5/25
 * Phone:18600920091
 * Email:td.liu@unistrong.com
 */
public interface IRequestHandler {
    Request onBeforeRequest(Request request, Interceptor.Chain chain);

    Response onAfterRequest(Response response, Interceptor.Chain chain);
}
