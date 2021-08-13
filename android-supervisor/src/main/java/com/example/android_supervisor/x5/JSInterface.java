package com.example.android_supervisor.x5;

import android.content.Context;
import android.webkit.JavascriptInterface;

import com.tencent.smtt.sdk.WebView;
import com.example.android_supervisor.common.UserSession;
import com.example.android_supervisor.entities.UserBase;
import com.example.android_supervisor.http.oauth.AccessToken;
import com.example.android_supervisor.utils.JsonUtils;
import com.example.android_supervisor.utils.OauthHostManager;
import com.example.android_supervisor.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class JSInterface {

    public static final String JS_INTERFACE_NAME = "JSInterface";//JS调用类名
    private Context mContext;
    private WebView webView;

    public JSInterface(Context context, WebView webView) {
        this.mContext = context;
        this.webView = webView;
    }

    @JavascriptInterface
    public String getUserInfo() {


        UserBase data = JsonUtils.fromJson(UserSession.getLoginInfo(mContext),UserBase.class);

        return UserSession.getLoginInfo(mContext);

    }

    @JavascriptInterface
    public String getUserId() {

//        ToastUtils.show(mContext,"getUserId进来了");
        UserBase data = JsonUtils.fromJson(UserSession.getLoginInfo(mContext),UserBase.class);
        if (data!=null){
            ToastUtils.show(mContext,data.getId());
            return data.getId();
        }

        return "";

    }


    @JavascriptInterface
    public String token() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", AccessToken.getInstance(mContext.getApplicationContext()).get());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ToastUtils.show(mContext,jsonObject.toString());
        //return jsonObject.toString();

        return AccessToken.getInstance(mContext.getApplicationContext()).get();
    }

    @JavascriptInterface
    public String getOauthUrl(){

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("apiUrl", OauthHostManager.getInstance(mContext).getApiConfig());
            jsonObject.put("messageUrl", OauthHostManager.getInstance(mContext).getMsgConfig());
            jsonObject.put("gpsUrl", OauthHostManager.getInstance(mContext).getGpsConfig());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();

    }

    @JavascriptInterface
    public void closeApp(){
        System.exit(0);
    }


    @JavascriptInterface
    public String userInfo(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("loginName", UserSession.getUserName(mContext));
            jsonObject.put("loginPwd", UserSession.getUserPwd(mContext));
        } catch (JSONException e) {
            e.printStackTrace();
        }
//        ToastUtils.show(mContext,jsonObject.toString());
        return jsonObject.toString();

    }



}
