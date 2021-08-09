package com.unistrong.api.services.core;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

class AppInfo//bm
{
  private static String appName = "";
  private static String packageName = "";
  private static String appVersion = "";
  private static String key = "";
  private static String sha1AndPackage = null;
  
  public static String getAppKey(Context context)
  {
    try
    {
      return getKey1(context);
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
      localNameNotFoundException.printStackTrace();
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
    return key;
  }
  
  public static String getApplicationName(Context context)
  {
    PackageManager localPackageManager = null;
    ApplicationInfo localApplicationInfo = null;
    try
    {
      if (!"".equals(appName)) {
        return appName;
      }
      localPackageManager = context.getPackageManager();
      localApplicationInfo = localPackageManager.getApplicationInfo(context.getPackageName(), 0);
      
      appName = (String)localPackageManager.getApplicationLabel(localApplicationInfo);
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
      BasicLogHandler.a(localNameNotFoundException, "AppInfo", "getApplicationName");
    }
    catch (Throwable localThrowable)
    {
      BasicLogHandler.a(localThrowable, "AppInfo", "getApplicationName");
    }
    return appName;
  }
  
  public static String getPackageName(Context context)
  {
    try
    {
      if ((packageName != null) && (!"".equals(packageName))) {
        return packageName;
      }
      packageName = context.getApplicationContext().getPackageName();
    }
    catch (Throwable localThrowable)
    {
      BasicLogHandler.a(localThrowable, "AppInfo", "getPackageName");
    }
    return packageName;
  }
  
  public static String getApplicationVersion(Context context)
  {
    PackageInfo localPackageInfo = null;
    try
    {
      if (!"".equals(appVersion)) {
        return appVersion;
      }
      PackageManager localPackageManager = context.getPackageManager();
      localPackageInfo = localPackageManager.getPackageInfo(context.getPackageName(), 0);
      
      appVersion = localPackageInfo.versionName;
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
      BasicLogHandler.a(localNameNotFoundException, "AppInfo", "getApplicationVersion");
    }
    catch (Throwable localThrowable)
    {
      BasicLogHandler.a(localThrowable, "AppInfo", "getApplicationVersion");
    }
    return appVersion;
  }
  
  public static String getSHA1AndPackage(Context context)
  {
    try
    {
      if ((sha1AndPackage != null) && (!"".equals(sha1AndPackage))) {
        return sha1AndPackage;
      }
      PackageInfo localPackageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);

      byte[] arrayOfByte1 = localPackageInfo.signatures[0].toByteArray();
      MessageDigest localMessageDigest = MessageDigest.getInstance("SHA1");
      byte[] arrayOfByte2 = localMessageDigest.digest(arrayOfByte1);
      StringBuffer localStringBuffer = new StringBuffer();

      for (int i = 0; i < arrayOfByte2.length; i++)
      {
        String str = Integer.toHexString(0xFF & arrayOfByte2[i]).toUpperCase(Locale.US);
        if (str.length() == 1) {
          localStringBuffer.append("0");
        }
        localStringBuffer.append(str);
        localStringBuffer.append(":");
      }

      localStringBuffer.append(localPackageInfo.packageName);
      sha1AndPackage = localStringBuffer.toString();
      return sha1AndPackage;
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
      BasicLogHandler.a(localNameNotFoundException, "AppInfo", "getSHA1AndPackage");
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
      BasicLogHandler.a(localNoSuchAlgorithmException, "AppInfo", "getSHA1AndPackage");
    }
    catch (Throwable localThrowable)
    {
      BasicLogHandler.a(localThrowable, "AppInfo", "getSHA1AndPackage");
    }
    return sha1AndPackage;
  }
  
  static void a(String paramString)
  {
    key = paramString;
  }
  
  private static String getKey1(Context context)
    throws PackageManager.NameNotFoundException
  {
    if ((key == null) || (key.equals("")))
    {
      ApplicationInfo localApplicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
      if (localApplicationInfo == null||localApplicationInfo.metaData==null) {
        return key;
      }
      key = localApplicationInfo.metaData.getString("com.leador.apikey");
    }
    return key;
  }
  
  public static String getKey(Context context)
  {
    try
    {
      return getKey1(context);
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
      BasicLogHandler.a(localNameNotFoundException, "AppInfo", "getKey");
    }
    catch (Throwable localThrowable)
    {
      BasicLogHandler.a(localThrowable, "AppInfo", "getKey");
    }
    return key;
  }
}
