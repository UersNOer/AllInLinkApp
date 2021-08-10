package com.unistrong.network.interceptor;


import com.unistrong.network.log.ILog;
import com.unistrong.network.log.LogFactory;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * Description:LogInterceptor
 * Created by ltd ON 2020/5/25
 * Phone:18600920091
 * Email:td.liu@unistrong.com
 */
public class LogInterceptor implements Interceptor {
    public static final String TAG = "net_log";

    private ILog mILog;

    public LogInterceptor() {
        mILog = LogFactory.getInstance();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
//        logRequest(request);
        Response response = chain.proceed(request);
        return logResponse(response);
    }


    private void logRequest(Request request) {
        try {
            mILog.e(TAG, "--------------------start------------------------");
            String url = request.url().toString();
            Headers headers = request.headers();
            mILog.e(TAG, "url : " + url);
            if (headers != null && headers.size() > 0) {
                mILog.e(TAG, "headers : " + headers.toString());
            }
            RequestBody requestBody = request.body();
            if (requestBody != null) {
                MediaType mediaType = requestBody.contentType();
                if (mediaType != null) {
                    if (isText(mediaType)) {
                        mILog.e(TAG, "params : " + bodyToString(request));
                    } else {
                        mILog.e(TAG, "params : " + " maybe [file part] , too large too print , ignored!");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Response logResponse(Response response) {
        try {
            logRequest(response.request());

            Response.Builder builder = response.newBuilder();
            Response clone = builder.build();
            ResponseBody body = clone.body();
            if (body != null) {
                MediaType mediaType = body.contentType();
                if (mediaType != null) {
                    if (isText(mediaType)) {
                        String resp = body.string();
                        mILog.e(TAG, "response : " + resp);
                        mILog.e(TAG, "--------------------end------------------------");
                        //.json(Log.ERROR, TAG, resp);
                        body = ResponseBody.create(mediaType, resp);
                        return response.newBuilder().body(body).build();
                    } else {
                        mILog.d(TAG, "data : " + " maybe [file part] , too large too print , ignored!");
                        mILog.e(TAG, "--------------------end------------------------");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }


    private boolean isText(MediaType mediaType) {
        if (mediaType == null) return false;

        return ("text".equals(mediaType.subtype())
                || "json".equals(mediaType.subtype())
                || "xml".equals(mediaType.subtype())
                || "html".equals(mediaType.subtype())
                || "plain".equals(mediaType.subtype())//视频融合项目新增
                || "webviewhtml".equals(mediaType.subtype())
                || "x-www-form-urlencoded".equals(mediaType.subtype()));
    }

    private String bodyToString(final Request request) {
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "something error when show requestBody.";
        }
    }
}
