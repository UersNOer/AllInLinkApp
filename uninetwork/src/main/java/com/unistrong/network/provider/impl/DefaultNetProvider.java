package com.unistrong.network.provider.impl;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;


import com.unistrong.network.DeviceIdUtils;
import com.unistrong.network.IRequestHandler;
import com.unistrong.network.NetError;
import com.unistrong.network.provider.INetProvider;
import com.unistrong.utils.AppContextUtils;
import com.unistrong.utils.SharedPreferencesUtil;
import com.unistrong.utils.UNIMetaData;
import com.unistrong.view.utils.SysUtils;

import okhttp3.CookieJar;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Description: description
 * Created by ltd ON 2020/5/25
 * Phone:18600920091
 * Email:td.liu@unistrong.com
 */
public class DefaultNetProvider implements INetProvider {

    @Override
    public Interceptor[] configInterceptors() {
        return new Interceptor[0];
    }

    @Override
    public void configHttps(OkHttpClient.Builder builder) {

    }

    @Override
    public CookieJar configCookie() {
        return null;
    }

    @Override
    public IRequestHandler configHandler() {
        return new IRequestHandler() {
            @Override
            public Request onBeforeRequest(Request request, Interceptor.Chain chain) {
                //添加统一header头
//            X-Uni-User-Agent:platform:iOS,appVersion:2.3.0,uniId:102,deviceId:A000004F64D3FF,platformVersion:19

                StringBuilder agent = new StringBuilder("platform:android,appVersion:");
                agent.append(getVersionName(AppContextUtils.getAppContext()));
//                agent.append(",platformVersion:" + Build.VERSION.SDK_INT);
                agent.append(",uniId:" + UNIMetaData.UNI_APP_TYPE);
                try {
//                    agent.append(",deviceId:" + DeviceIdUtils.getDeviceId((AppContextUtils.getAppContext())).trim());
//                    agent.append(",deviceId:" + SysUtils.getDeviceId((AppContextUtils.getAppContext())));
                } catch (Exception e) {
                }
                return chain.request().newBuilder()
                        .addHeader("Accept", "application/json;charset=UTF-8")//
                        .addHeader("X-Uni-User-Agent", agent.toString())
//                        .addHeader("uniId", UNIMetaData.UNI_APP_TYPE + "")
                        .addHeader("token", TextUtils.isEmpty(SharedPreferencesUtil.getInstance().getToken()) ? "" : SharedPreferencesUtil.getInstance().getToken())
//                        .addHeader("token","AgU-xV_CFr9fwK9TxvTyiwXcmDA8vWwjzDwYJM5nwjjZ")
//                        .addHeader("X-Uni-User-ID", SPUtils.getInstance().getString(SharedPUtils_USER_ID))
                        .build();
            }

            @Override
            public Response onAfterRequest(Response response, Interceptor.Chain chain) {
                return null;
            }
        };
    }

    @Override
    public long configConnectTimeoutMills() {
        return 0;
    }

    @Override
    public long configReadTimeoutMills() {
        return 0;
    }

    @Override
    public boolean configLogEnable() {
        return true;
    }

    @Override
    public boolean handleError(NetError error) {
        return false;
    }

    @Override
    public boolean dispatchProgressEnable() {
        return false;
    }

    public static final String SharedPUtils_DEVICE_ID = "SharedPUtils_DEVICE_ID";

    /**
     * 获取当前版本
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        PackageManager pManager = context.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = pManager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo.versionName;
    }
}
