package com.unistrong.network.interceptor;


import com.unistrong.network.IRequestHandler;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Description:XInterceptor
 * Created by ltd ON 2020/5/25
 * Phone:18600920091
 * Email:td.liu@unistrong.com
 */
public class XInterceptor implements Interceptor {

    private IRequestHandler mHandler;

    public XInterceptor(IRequestHandler handler) {
        this.mHandler = handler;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (mHandler != null) {
            request = mHandler.onBeforeRequest(request, chain);
        }
        Response response = chain.proceed(request);
        if (mHandler != null) {
            Response tmp = mHandler.onAfterRequest(response, chain);
            if (tmp != null) {
                return tmp;
            }

        }
        return response;
    }
}
