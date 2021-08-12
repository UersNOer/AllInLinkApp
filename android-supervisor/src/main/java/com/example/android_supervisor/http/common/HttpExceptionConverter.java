package com.example.android_supervisor.http.common;
import com.example.android_supervisor.http.response.ErrorEntity;
import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.text.ParseException;

import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Response;

/**
 * @author wujie
 */
public class HttpExceptionConverter {

    public ErrorEntity convert(Throwable e){
        String errMsg;
        int code=-1;
        if (e instanceof HttpException){
            HttpException httpException=(HttpException)e;
            code=httpException.code();
            switch (httpException.code()){
                case 403:
                    errMsg="网络服务禁止访问";
                    break;
                case 404:
                    errMsg="网络服务不存在";
                    break;
                case 408:
                case 504:
                    errMsg="网络请求超时";
                    break;
                case 500:
                    errMsg="网络服务出现了一点小问题";
                    break;
                default:
                    errMsg = convert(httpException);
                    break;
            }
        }else if(e instanceof ConnectException){
            errMsg="网络连接失败，请稍后重试";
        }else if(e instanceof javax.net.ssl.SSLHandshakeException){
            errMsg="网络证书验证失败";
        }else if (e instanceof SocketTimeoutException) {
            errMsg = "请求网络超时，请稍后重试";
        }  else if (e instanceof ParseException
                || e instanceof JSONException
                || e instanceof JsonParseException) {
            errMsg = "网络数据解析错误";
        } else {
            errMsg = "连接异常:请检查网络连接或者网络请求配置";
        }
        ErrorEntity errorEntity=new ErrorEntity();
        errorEntity.setCode(code);
        errorEntity.setMessage(errMsg);
        return errorEntity;
    }

    private String convert(HttpException httpException) {
        String msg = httpException.message();
        Response response = httpException.response();
        if (response != null) {
            ResponseBody errorBody = response.errorBody();
            if (errorBody != null) {
                try {
                    msg = errorBody.string();
                } catch (IOException e) {
                }
            }
        }
        return msg;
    }
}
