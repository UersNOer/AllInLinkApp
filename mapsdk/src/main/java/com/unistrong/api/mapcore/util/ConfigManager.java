package com.unistrong.api.mapcore.util;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class ConfigManager//bp
{
  public static a a(byte[] paramArrayOfByte)
  {
//    a locala = new a();
//    try
//    {
//      if ((paramArrayOfByte == null) || (paramArrayOfByte.length == 0)) {
//        return locala;
//      }
//      String str1 = new String(paramArrayOfByte, "UTF-8");
//
//      JSONObject localJSONObject1 = new JSONObject(str1);
//      String str2 = a(localJSONObject1, "status");
//      if ("1".equals(str2))
//      {
//        if (!localJSONObject1.has("result")) {
//          return locala;
//        }
//        JSONObject localJSONObject2 = localJSONObject1.getJSONObject("result");
//        if (localJSONObject2 != null)
//        {
//          boolean bool1 = false;
//          if (Utils.hasKey(localJSONObject2, "exception"))
//          {
//            JSONObject localJSONObject3 = localJSONObject2.getJSONObject("exception");
//
//            bool1 = parseException(localJSONObject3);
//          }
//          boolean bool2 = false;
//          if (Utils.hasKey(localJSONObject2, "common"))
//          {
//            JSONObject localObject1 = localJSONObject2.getJSONObject("common");
//
//            bool2 = parseCommon((JSONObject) localObject1);
//          }
//          Object localObject1 = new ConfigManager.a.d();
//          ((ConfigManager.a.d)localObject1).a = bool1;
//          ((ConfigManager.a.d)localObject1).b = bool2;
//          locala.d = ((ConfigManager.a.d)localObject1);
//          JSONObject localJSONObject4;
//          Object localObject2;
//          if (localJSONObject2.has("sdkupdate"))
//          {
//            localJSONObject4 = localJSONObject2.getJSONObject("sdkupdate");
//
//            localObject2 = new ConfigManager.a.c();
//            parseSDKUpdate(localJSONObject4, (ConfigManager.a.c) localObject2);
//            locala.e = ((ConfigManager.a.c)localObject2);
//          }
//          if (Utils.hasKey(localJSONObject2, "sdkcoordinate"))
//          {
//            localJSONObject4 = localJSONObject2.getJSONObject("sdkcoordinate");
//
//            localObject2 = new ConfigManager.a.b();
//            parseSDKCoordinate(localJSONObject4, (ConfigManager.a.b) localObject2);
//
//            locala.f = ((ConfigManager.a.b)localObject2);
//          }
//          if (Utils.hasKey(localJSONObject2, "callamap"))
//          {
//            localJSONObject4 = localJSONObject2.getJSONObject("callamap");
//
//            locala.b = localJSONObject4;
//          }
//          if (Utils.hasKey(localJSONObject2, "ca"))
//          {
//            localJSONObject4 = localJSONObject2.getJSONObject("ca");
//            locala.c = localJSONObject4;
//          }
//          if (Utils.hasKey(localJSONObject2, "locate"))
//          {
//            localJSONObject4 = localJSONObject2.getJSONObject("locate");
//            locala.a = localJSONObject4;
//          }
//        }
//      }
//      return locala;
//    }
//    catch (JSONException localJSONException)
//    {
//      BasicLogHandler.a(localJSONException, "ConfigManager", "loadConfig");
//    }
//    catch (UnsupportedEncodingException localUnsupportedEncodingException)
//    {
//      BasicLogHandler.a(localUnsupportedEncodingException, "ConfigManager", "loadConfig");
//    }
//    catch (Throwable localThrowable)
//    {
//      BasicLogHandler.a(localThrowable, "ConfigManager", "loadConfig");
//    }
//    return locala;
    return null;
  }
  
  public static a a(Context paramContext, SDKInfo parambv, String paramString)
  {
    try
    {
      BaseNetManager netManager = new BaseNetManager();
      byte[] arrayOfByte = netManager.a(new b(paramContext, parambv, paramString));
      
      return a(arrayOfByte);
    }
    catch (IMMapCoreException localbl)
    {
      BasicLogHandler.a(localbl, "ConfigManager", "loadConfig");
    }
    catch (Throwable localThrowable)
    {
      BasicLogHandler.a(localThrowable, "ConfigManager", "loadConfig");
    }
    return new a();
  }
  
  static class b
    extends Request
  {
    private Context a;
    private SDKInfo b;
    private String c = "";
    
    b(Context paramContext, SDKInfo parambv, String paramString)
    {
      this.a = paramContext;
      this.b = parambv;
      this.c = paramString;
    }
    
    public Map<String, String> getHeadMaps()
    {
      HashMap<String, String> localHashMap = new HashMap<String,String>();
      localHashMap.put("User-Agent", this.b.getUseragen());
      localHashMap.put("platinfo", String.format("platform=Android&sdkversion=%s&product=%s", new Object[] { this.b.getVersion(), this.b.getProduct() }));
      
      localHashMap.put("logversion", "2.0");
      return localHashMap;
    }
    
    public Map<String, String> getRequestParam()
    {
      String str1 = DeviceInfo.getDeviceID(this.a);
      if (!TextUtils.isEmpty(str1))
      {
        StringBuilder localObject = new StringBuilder(str1);
        str1 = ((StringBuilder)localObject).reverse().toString();
        str1 = MD5.MD5AndHex(str1);
      }
      Map<String,String> localObject = new HashMap<String, String>();
      localObject.put("key", AppInfo.getKey(this.a));
      
      localObject.put("opertype", this.c);
      localObject.put("plattype", "android");
      localObject.put("product", this.b.getProduct());
      localObject.put("version", this.b.getVersion());
      localObject.put("output", "json");
      localObject.put("androidversion", Build.VERSION.SDK_INT + "");
      localObject.put("deviceId", str1);
      localObject.put("abitype", Build.CPU_ABI);
      
      localObject.put("ext", this.b.d());
      String str2 = ClientInfo.getTS();
      String str3 = ClientInfo.Scode(this.a, str2, Utils.appendParams(localObject));
      
      localObject.put("ts", str2);
      localObject.put("scode", str3);
      
      return (Map<String, String>)localObject;
    }
    
    public String getUrl()
    {
      return "";
//      return "https://restapi.leador.com/v3/config/resource?";
    }
  }
  
  private static boolean a(String paramString)
  {
    if (paramString == null) {
      return false;
    }
    if (paramString.equals("1")) {
      return true;
    }
    return false;
  }
  
  public static String a(JSONObject paramJSONObject, String paramString)
    throws JSONException
  {
    if (paramJSONObject == null) {
      return "";
    }
    String str = "";
    if ((paramJSONObject.has(paramString)) && (!paramJSONObject.getString(paramString).equals("[]"))) {
      str = paramJSONObject.optString(paramString);
    }
    return str;
  }
  
  private static void parseSDKCoordinate(JSONObject paramJSONObject, ConfigManager.a.b paramb)
  {
    try
    {
      if (paramJSONObject != null)
      {
        String str1 = a(paramJSONObject, "md5");
        
        String str2 = a(paramJSONObject, "url");
        paramb.b = str1;
        paramb.a = str2;
      }
    }
    catch (JSONException localJSONException)
    {
      BasicLogHandler.a(localJSONException, "ConfigManager", "parseSDKCoordinate");
    }
    catch (Throwable localThrowable)
    {
      BasicLogHandler.a(localThrowable, "ConfigManager", "parseSDKCoordinate");
    }
  }
  
  private static void parseSDKUpdate(JSONObject paramJSONObject, ConfigManager.a.c paramc)
  {
    try
    {
      if (paramJSONObject != null)
      {
        String str1 = a(paramJSONObject, "md5");
        String str2 = a(paramJSONObject, "url");
        String str3 = a(paramJSONObject, "sdkversion");
        if ((TextUtils.isEmpty(str1)) || (TextUtils.isEmpty(str2)) || (TextUtils.isEmpty(str3)))
        {
          paramc = null;
        }
        else
        {
          paramc.a = str2;
          paramc.b = str1;
          paramc.c = str3;
        }
      }
      else
      {
        paramc = null;
      }
    }
    catch (JSONException localJSONException)
    {
      BasicLogHandler.a(localJSONException, "ConfigManager", "parseSDKUpdate");
    }
    catch (Throwable localThrowable)
    {
      BasicLogHandler.a(localThrowable, "ConfigManager", "parseSDKUpdate");
    }
  }
  
  private static boolean parseCommon(JSONObject paramJSONObject)
  {
    try
    {
      if (paramJSONObject == null) {
        return false;
      }
      JSONObject localJSONObject = paramJSONObject.getJSONObject("commoninfo");
      
      String str = a(localJSONObject, "com_isupload");
      
      return a(str);
    }
    catch (JSONException localJSONException)
    {
      BasicLogHandler.a(localJSONException, "ConfigManager", "parseCommon");
    }
    catch (Throwable localThrowable)
    {
      BasicLogHandler.a(localThrowable, "ConfigManager", "parseCommon");
    }
    return false;
  }
  
  private static boolean parseException(JSONObject paramJSONObject)
  {
    try
    {
      if (paramJSONObject == null) {
        return false;
      }
      JSONObject localJSONObject = paramJSONObject.getJSONObject("exceptinfo");
      
      String str = a(localJSONObject, "ex_isupload");
      
      return a(str);
    }
    catch (JSONException localJSONException)
    {
      BasicLogHandler.a(localJSONException, "ConfigManager", "parseException");
    }
    catch (Throwable localThrowable)
    {
      BasicLogHandler.a(localThrowable, "ConfigManager", "parseException");
    }
    return false;
  }
  
  public static class a
  {
    public JSONObject a;
    public JSONObject b;
    public JSONObject c;
    public d d;
    public c e;
    public b f;
    
    public static class b
    {
      public String a;
      public String b;
    }
    
    public static class c
    {
      public String a;
      public String b;
      public String c;
    }
    
    public static class d
    {
      public boolean a;
      public boolean b;
    }
  }
}
