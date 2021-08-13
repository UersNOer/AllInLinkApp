package com.example.android_supervisor.utils;

import android.content.Context;

import com.bumptech.glide.load.HttpException;
import com.google.gson.JsonSyntaxException;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * Created by dw on 2019/8/7.
 */
public class UnifiedErrorUtil {

    public static Throwable unifiedError(Throwable e,Context context){
        Throwable throwable;
        if(e instanceof UnknownHostException || e instanceof HttpException) {
            //无网络的情况，或者主机挂掉了。返回，对应消息  Unable to resolve host "m.app.haosou.com": No address associated with hostname
            if (!NetworkUtil.isNetAvailable(context)) {
                //无网络
                throwable = new Throwable("hello?好像没网络啊！",e.getCause());
            } else {
                //主机挂了，也就是你服务器关了
                throwable = new Throwable("服务器开小差,请稍后重试！", e.getCause());
            }
        } else if(e instanceof ConnectException || e instanceof SocketTimeoutException || e instanceof SocketException){
            //连接超时等
            throwable = new Throwable("网络连接超时，请检查您的网络状态！", e.getCause());
        } else if(e instanceof NumberFormatException || e instanceof  IllegalArgumentException || e instanceof JsonSyntaxException){
            //也就是后台返回的数据，与你本地定义的Gson类，不一致，导致解析异常 (ps:当然这不能跟客户这么说)
            throwable = new Throwable("未能请求到数据，攻城狮正在修复!", e.getCause());
        }else{
            //其他 未知
            throwable = new Throwable("哎呀故障了，攻城狮正在修复！", e.getCause());
        }
        return throwable;
    }
}
