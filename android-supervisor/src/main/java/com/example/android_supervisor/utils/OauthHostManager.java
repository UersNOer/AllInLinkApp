package com.example.android_supervisor.utils;

import android.content.Context;

import com.example.android_supervisor.entities.ServicesConfig;

/**
 * Created by Administrator on 2019/8/31.
 */
public class OauthHostManager {

    public static final String APICONFIG_KEY="apiConfig";
    public static final String MSGCONFIG_KEY="msgConfig";
    public static final String GPSCONFIG_KEY="gpsConfig";
    public static final String OAUTH_HOSTStatus_KEY="oauthHostStatus";


    public static OauthHostManager instance;
    private Context context;

    private OauthHostManager(Context context){
        this.context = context;
    }

    public static OauthHostManager getInstance(Context context) {
        if (instance == null) {
            instance = new OauthHostManager(context);
        }
        return instance;
    }

    private void checkValue(String value) {
        if (context == null || value == null) {
            throw new RuntimeException("请先调用带有context，value参数的构造！");
        }
    }

    public void setApiConfig(String value){
        checkValue(value);
        LocalSaveUtils.saveString(context,APICONFIG_KEY,value);
    }

    public String getApiConfig(){
        return LocalSaveUtils.getString(context,APICONFIG_KEY,"");
    }

    public void setMsgConfig(String value){
        checkValue(value);
        LocalSaveUtils.saveString(context,MSGCONFIG_KEY,value);
    }


    public String getMsgConfig(){
        return LocalSaveUtils.getString(context, MSGCONFIG_KEY, "");
    }


    public void setGpsConfig(String value){
        checkValue(value);
        LocalSaveUtils.saveString(context,GPSCONFIG_KEY,value);
    }

    public String getGpsConfig(){
        return LocalSaveUtils.getString(context,GPSCONFIG_KEY,"");
    }

    public void setConfigStatus(boolean value){
         LocalSaveUtils.saveBoolean(context, OAUTH_HOSTStatus_KEY, value);
    }

    public void cleanConfigStatus(){
        setConfigStatus(false);
        setApiConfig("");
        setMsgConfig("");
        setGpsConfig("");
    }

    public Boolean getConfigStatus(){
        return LocalSaveUtils.getBoolean(context, OAUTH_HOSTStatus_KEY, false);
    }


    /**
     * 保存不同的网络配置域名
     * @param servicesConfig
     */
    public void setOauthHost(ServicesConfig servicesConfig) {
        switch (servicesConfig.getConfigType()){
            case 0:
                setApiConfig(servicesConfig.getHostAddress());
                break;
            case 1:
                setMsgConfig(servicesConfig.getHostAddress());
                break;
            case 2:
                setGpsConfig(servicesConfig.getHostAddress());
                break;
            default:
                break;
        }
        setConfigStatus(true);
    }
}
