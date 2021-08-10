package com.unistrong.api.mapcore.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Locale;

public class ProxyUtil//bt
{
  public static java.net.Proxy getProxy(Context context)
  {
    java.net.Proxy localProxy = null;
    try
    {
      if (Build.VERSION.SDK_INT >= 11)
      {
        URI localURI = new URI("");
        localProxy = a(context, localURI);
      }
      else
      {
        localProxy = getHostProxy(context);
      }
    }
    catch (URISyntaxException uriException)
    {
      BasicLogHandler.a(uriException, "ProxyUtil", "getProxy");
    }
    catch (Throwable localThrowable)
    {
      BasicLogHandler.a(localThrowable, "ProxyUtil", "getProxy");
    }
    return localProxy;
  }
  
  private static java.net.Proxy getHostProxy(Context context)
  {
    Object localObject1 = null;
    int i = -1;
    if (DeviceInfo.getActiveNetWorkType(context) == 0)
    {
      Uri localUri = Uri.parse("content://telephony/carriers/preferapn");
      ContentResolver localContentResolver = context.getContentResolver();
      Cursor localCursor = null;
      try
      {
        localCursor = localContentResolver.query(localUri, null, null, null, null);
        if ((localCursor != null) && (localCursor.moveToFirst()))
        {
          int j = localCursor.getColumnIndex("apn");
          String str1 = localCursor.getString(j);
          if (str1 != null) {
            str1 = str1.toLowerCase(Locale.US);
          }
          if ((str1 != null) && (str1.contains("ctwap")))
          {
            String str2 = a();
            i = b();
            int k = 0;
            if ((!TextUtils.isEmpty(str2)) && 
              (!str2.equals("null")))
            {
              k = 1;
              localObject1 = str2;
            }
            if (k == 0) {
              localObject1 = "10.0.0.200";
            }
            if (i == -1) {
              i = 80;
            }
          }
          else if ((str1 != null) && (str1.contains("wap")))
          {
            String str2 = a();
            i = b();
            
            int k = 0;
            if ((!TextUtils.isEmpty(str2)) && 
              (!str2.equals("null")))
            {
              k = 1;
              localObject1 = str2;
            }
            if (k == 0) {
              localObject1 = "10.0.0.172";
            }
            if (i == -1) {
              i = 80;
            }
          }
        }
      }
      catch (SecurityException exception)
      {
        String str2;
        int k;
        BasicLogHandler.a(exception, "ProxyUtil", "getHostProxy");
        String str1 = DeviceInfo.getNetworkExtraInfo(context);
        if (str1 != null)
        {
          str2 = str1.toLowerCase(Locale.US);
          
          k = 0;
          String str3 = a();
          i = b();
          if (str2.indexOf("ctwap") != -1)
          {
            if ((!TextUtils.isEmpty(str3)) && 
              (!str3.equals("null")))
            {
              k = 1;
              localObject1 = str3;
            }
            if (k == 0) {
              localObject1 = "10.0.0.200";
            }
            if (i == -1) {
              i = 80;
            }
          }
          else if (str2.indexOf("wap") != -1)
          {
            if ((!TextUtils.isEmpty(str3)) && 
              (!str3.equals("null")))
            {
              k = 1;
              localObject1 = str3;
            }
            if (k == 0) {
              localObject1 = "10.0.0.200";
            }
            i = 80;
          }
        }
      }
      catch (Throwable throwable)
      {
        BasicLogHandler.a(throwable, "ProxyUtil", "getHostProxy1");
        
        throwable.printStackTrace();
      }
      finally
      {
        if (localCursor != null) {
          try
          {
            localCursor.close();
          }
          catch (Throwable throwable)
          {
            BasicLogHandler.a(throwable, "ProxyUtil", "getHostProxy2");
            
            throwable.printStackTrace();
          }
        }
      }
      try
      {
        if (a((String)localObject1, i)) {
          return new java.net.Proxy(Proxy.Type.HTTP, InetSocketAddress.createUnresolved((String)localObject1, i));
        }
      }
      catch (Throwable throwable)
      {
        BasicLogHandler.a(throwable, "ProxyUtil", "getHostProxy2");
        
        throwable.printStackTrace();
      }
    }
    return null;
  }
  
  private static boolean a(String paramString, int paramInt)
  {
    return (paramString != null) && (paramString.length() > 0) && (paramInt != -1);
  }
  
  private static String a()
  {
    String str = null;
    try
    {
      str = android.net.Proxy.getDefaultHost();
    }
    catch (Throwable localThrowable)
    {
      BasicLogHandler.a(localThrowable, "ProxyUtil", "getDefHost");
      str = null;
    }
    if (str == null) {
      str = "null";
    }
    return str;
  }
  
  private static java.net.Proxy a(Context paramContext, URI paramURI)
  {
    if (DeviceInfo.getActiveNetWorkType(paramContext) == 0) {
      try
      {
        ProxySelector localProxySelector = ProxySelector.getDefault();
        java.net.Proxy localProxy = null;
        List localList = null;
        localList = localProxySelector.select(paramURI);
        if ((localList != null) && (!localList.isEmpty()))
        {
          localProxy = (java.net.Proxy)localList.get(0);
          if ((localProxy != null) && (localProxy.type() != Proxy.Type.DIRECT)) {}
        }
        return null;
      }
      catch (Throwable localThrowable)
      {
        BasicLogHandler.a(localThrowable, "ProxyUtil", "getProxySelectorCfg");
      }
    }
    return null;
  }
  
  private static int b()
  {
    int i = -1;
    try
    {
      i = android.net.Proxy.getDefaultPort();
    }
    catch (Throwable localThrowable)
    {
      BasicLogHandler.a(localThrowable, "ProxyUtil", "getDefPort");
    }
    return i;
  }
}
