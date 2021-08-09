package com.unistrong.api.mapcore.util;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

public class AuthManager//bn
{
  public static int authResult = 0;//0 鉴权成功，2 key错误，其他值鉴权失败
  public static boolean isRequestFinished = false;//是否请求鉴权服务完成；
  public static String b = "";
  private static SDKInfo sdkInfo;
  private static String e = null;
  
  private static boolean getKeyAuth(Context context, SDKInfo sdkInfo, boolean paramBoolean)
  {
    AuthManager.sdkInfo = sdkInfo;
    String url = null;
    boolean bool = true;
    try
    {
//      String key = AppInfo.getKey(context);
//      if(null == key || key.length()==0){
//        authResult = 2;
//        return false;
//      }
//      url = getURL(context);
//      HashMap<String,String> paramsMap = new HashMap<String,String>();
//      paramsMap.put("Content-Type", "application/x-www-form-urlencoded");
//      paramsMap.put("Accept-Encoding", "gzip");
//      paramsMap.put("Connection", "Keep-Alive");
//      paramsMap.put("User-Agent", sdkInfo.useragen);
//      paramsMap.put("X-INFO", ClientInfo.initXInfo(context, sdkInfo, null, paramBoolean));
//      paramsMap.put("logversion", "1.2");
//      paramsMap.put("platinfo", String.format("platform=Android&sdkversion=%s&product=%s", new Object[]{sdkInfo.version, sdkInfo.product}));
//      BaseNetManager netManager = BaseNetManager.getInstance();
//      AuthRequest request = new AuthRequest();
////      request.setProxy(ProxyUtil.getProxy(paramContext));
//      request.setSDKInfo(paramsMap);
//      Map<String,String> localMap = gParamsMap(context);
//      request.setDevInfo(localMap);
//      request.setUrl(url);
//
//      byte[] arrayOfByte = netManager.makeSyncPostRequest(request);
//      bool = parseResult(arrayOfByte);
//      isRequestFinished = true;
    }
    catch (Throwable localThrowable)
    {
      BasicLogHandler.a(localThrowable, "Auth", "getAuth");
    }
    return bool;
  }
  
  public static synchronized boolean a(Context paramContext, SDKInfo parambv)
  {
    return getKeyAuth(paramContext, parambv, true);
  }
  
  public static synchronized boolean getKeyAuth(Context paramContext, SDKInfo parambv)
  {
    return getKeyAuth(paramContext, parambv, false);
  }
  
  public static void a(String paramString)
  {
    AppInfo.a(paramString);
  }
  
  private static String getURL(Context context)
  {
    return Util.getAuthUrl(context);
  }
  
  private static boolean parseResult(byte[] paramArrayOfByte)
  {
//    String str = null;
//    try
//    {
//      if (paramArrayOfByte == null) {
//        return true;
//      }
//      try
//      {
//        str = new String(paramArrayOfByte, "UTF-8");
////        Log.i("auth",str);
//      }
//      catch (UnsupportedEncodingException localUnsupportedEncodingException)
//      {
//        str = new String(paramArrayOfByte);
//      }
//      Log.i("Auth", str);
//      JSONObject localJSONObject = new JSONObject(str);
//      if (localJSONObject.has("status"))
//      {
//        int i = localJSONObject.getInt("status");
////        if (i == 0) {
////          authResult = 0;
////        } else  {
////          authResult = i;
////        }
//      }
//      if (localJSONObject.has("message")) {
//        b = localJSONObject.getString("message");
//      }
//      if (authResult != 0) {
//        Log.i("AuthFailure", b);
//      }
//      if (authResult == 0) {
//
//        return true;
//      }
//      return false;
//    }
//    catch (JSONException localJSONException)
//    {
//      BasicLogHandler.a(localJSONException, "Auth", "lData");
//    }
//    catch (Throwable localThrowable)
//    {
//      BasicLogHandler.a(localThrowable, "Auth", "lData");
//    }
    return false;
  }
  
  private static Map<String, String> gParamsMap(Context context)
  {
    HashMap<String,String> localHashMap = new HashMap<String,String>();
    try
    {
      localHashMap.put("resType", "json");
      localHashMap.put("encode", "UTF-8");
      
      String ts = ClientInfo.getTS();
      localHashMap.put("ts", ts);
      localHashMap.put("ak", AppInfo.getKey(context));
      String str2 = "resType=json&encode=UTF-8&ak=" + AppInfo.getKey(context);
      
      String str3 = Utils.sortParams(str2);
      
      localHashMap.put("scode", ClientInfo.Scode(context, ts, str3));
    }
    catch (Throwable localThrowable)
    {
      BasicLogHandler.a(localThrowable, "Auth", "gParams");
    }
    return localHashMap;
  }
}
