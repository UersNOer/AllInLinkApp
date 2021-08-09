package com.unistrong.network.interceptor;

import android.util.Log;

import com.unistrong.network.router.UserModuleRouter;
import com.unistrong.utils.AppContextUtils;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 处理错误码的拦截器
 */
public class ErrorInterceptor implements Interceptor {
    public static final String TAG = "handle_error";

    /**
     * token过期
     */
    public static final int TOKEN_EXPIRED = 403;

    public ErrorInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        return handleError(response);
    }


    private Response handleError(Response response) {
        try {
            Response.Builder builder = response.newBuilder();
            Response clone = builder.build();
            ResponseBody body = clone.body();
            if (body != null) {
                MediaType mediaType = body.contentType();
                if (mediaType != null) {

                    if (isText(mediaType)) {
                        String resp = body.string();
                        JSONObject object = new JSONObject(resp);
                        int code = object.getInt("code");
                        if (code != 200) {
                            handleError(code);
                        }
                        body = ResponseBody.create(mediaType, resp);
                        return response.newBuilder().body(body).build();
//                    } else {
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
                || "plain".equals(mediaType.subtype())
                || "text/plain;charset=UTF-8".equals(mediaType.subtype())
                || "webviewhtml".equals(mediaType.subtype())
                || "x-www-form-urlencoded".equals(mediaType.subtype()));
    }

    /**
     * 错误处理
     *
     * @param code 错误码
     */
    private void handleError(int code) {
        switch (code) {
            case TOKEN_EXPIRED:
                //清除用户数据
                UserModuleRouter userModuleRouter = new UserModuleRouter();
                userModuleRouter.clearUserData();
                userModuleRouter.openLoginActivity(AppContextUtils.getAppContext());
                AppContextUtils.finishAllActivity();
                break;
            default:
                break;
        }
    }
}
