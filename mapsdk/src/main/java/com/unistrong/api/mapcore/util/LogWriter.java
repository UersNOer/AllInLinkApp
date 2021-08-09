package com.unistrong.api.mapcore.util;

import android.content.Context;
import android.os.Looper;

import com.unistrong.api.mapcore.ConfigableConstDecode;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

abstract class LogWriter//cl
{
  private SDKInfo sdkInfo;

  
  static LogWriter getLogWriterByType(int paramInt)
  {
    Object localObject = null;
    switch (paramInt)
    {
    case 0: 
      localObject = new CrashLogWriter();
      break;
    case 1: 
      localObject = new ExceptionLogWriter();
      break;
    case 2: 
      localObject = new ANRLogWriter();
      break;
      case 3:
        localObject = new NormalLogWriter();
        break;
    default: 
      return null;
    }
    return (LogWriter)localObject;
  }
  
  void writerLog(Context context, Throwable throwable, String className, String methodName)
  {
    List<SDKInfo> localList = getSDKLog(context);
    if ((localList == null) || (localList.size() == 0)) {
      return;
    }
    String html = throwableHtml(throwable);
    if ((html == null) || ("".equals(html))) {
      return;
    }
    for (SDKInfo sdkinfo : localList)
    {
      String[] packageNames = sdkinfo.getPackageNames();
      if (isContain(packageNames, html))
      {
        setSDKInfo(sdkinfo);
        String time = getTime();
        String info = getClientInfo(context, sdkinfo);
        String appKey = getAppKey(context);
        String throwString = throwableToStrings(throwable);
        if ((throwString == null) || ("".equals(throwString))) {
          return;
        }
        int logType = getType();//错误类型
        StringBuilder stringBuilder = new StringBuilder();
        if (className != null) {
          stringBuilder.append("class:").append(className);
        }
        if (methodName != null) {
          stringBuilder.append(" method:").append(methodName).append("$").append("<br/>");
        }
        stringBuilder.append(html);
        String logMD5 = toMD5Encrypt(html);
        String str7 = appendParam(appKey, info, time, logType, throwString, stringBuilder.toString());
        if ((str7 == null) || ("".equals(str7))) {
          return;
        }
        String logAES = base64AndHex(context, str7);
        String dirName = getDirName();
        synchronized (Looper.getMainLooper())
        {
          LogDBOperation dbOperation = new LogDBOperation(context);
          boolean cacheRes = outToDiskCache(context, logMD5, dirName, logAES, dbOperation);
          
          outputLog(dbOperation, sdkinfo.getProduct(), logMD5, logType, cacheRes);
        }
      }
    }
  }
  void writerLog(Context context,int customid, String content, String className, String methodName)
  {
    SDKInfo sdkinfo =  ConfigableConstDecode.sdkInfo;
    if(sdkinfo!=null) {
        setSDKInfo(sdkinfo);
        String time = getTime();
        String info = getClientInfo(context, sdkinfo);
//        String appKey = getAppKey(context);
        if ((content == null) || ("".equals(content))) {
          return;
        }
        int logType = getType();//错误类型
        StringBuilder stringBuilder = new StringBuilder();
        if (className != null) {
          stringBuilder.append("class:").append(className);
        }
        if (methodName != null) {
          stringBuilder.append(" method:").append(methodName).append("$").append("<br/>");
        }
        stringBuilder.append(content);
        String logMD5 = toMD5Encrypt(content);
        String str7 = appendParam(customid+"", info, time, logType, "", stringBuilder.toString());
        if ((str7 == null) || ("".equals(str7))) {
          return;
        }
        String logAES = base64AndHex(context, str7);
        String dirName = getDirName();
        synchronized (Looper.getMainLooper()) {
          LogDBOperation dbOperation = new LogDBOperation(context);
          boolean cacheRes = outToDiskCache(context, logMD5, dirName, logAES, dbOperation);

          outputLog(dbOperation, sdkinfo.getProduct(), logMD5, logType, cacheRes);
        }

    }
  }
  void writerANRLog(Context paramContext)
  {
    List<SDKInfo> localList = getSDKLog(paramContext);
    if ((localList == null) || (localList.size() == 0)) {
      return;
    }
    String str1 = a(localList);
    if ((str1 == null) || ("".equals(str1))) {
      return;
    }
    String str2 = getTime();
    String str3 = getClientInfo(paramContext, this.sdkInfo);
    String str4 = getAppKey(paramContext);
    String str5 = "ANR";
    int i = getType();
    String str6 = appendParam(str4, str3, str2, i, str5, str1);
    if ((str6 == null) || ("".equals(str6))) {
      return;
    }
    String str7 = toMD5Encrypt(str1);
    String str8 = base64AndHex(paramContext, str6);
    String str9 = getDirName();
    synchronized (Looper.getMainLooper())
    {
      LogDBOperation localcv = new LogDBOperation(paramContext);
      boolean bool = outToDiskCache(paramContext, str7, str9, str8, localcv);
      
      outputLog(localcv, this.sdkInfo.getProduct(), str7, i, bool);
    }
  }
  
  protected abstract String a(List<SDKInfo> paramList);
  
  protected void setSDKInfo(SDKInfo parambv)
  {
    this.sdkInfo = parambv;
  }
  
  private List<SDKInfo> getSDKLog(Context paramContext)
  {
    List<SDKInfo> list = null;
    try
    {
      synchronized (Looper.getMainLooper())
      {
        SDKDBOperation sdk = new SDKDBOperation(paramContext, false);
        list = sdk.a();
      }
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
    return list;
  }
  
  private void outputLog(LogDBOperation logDB, String paramString1, String md5, int logType, boolean paramBoolean)
  {
    LogInfo info = new LogInfo();
    
    info.a(0);
    info.setSdkType(paramString1);
    info.setMD5(md5);
    logDB.writeToDB(info, logType);
  }
  
  protected abstract int getType();
  
  protected abstract String toMD5Encrypt(String paramString);
  
  protected abstract FileOperationListenerDecode a(LogDBOperation paramcv);
  
  protected abstract String getDirName();
  
  private String appendParam(String customid, String clientinfo, String time, int logType, String paramString4, String paramString5)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append(clientinfo).append(",").append("\"timestamp\":\"");
    localStringBuffer.append(time);
    localStringBuffer.append("\",\"customid\":\"");
    localStringBuffer.append(customid);
    localStringBuffer.append("\",\"errtype\":\"");
    localStringBuffer.append(logType);
    localStringBuffer.append("\",\"classname\":\"");
    localStringBuffer.append(paramString4);
    localStringBuffer.append("\",\"detail\":\"");
    localStringBuffer.append(paramString5);
    localStringBuffer.append("\"");
    
    return localStringBuffer.toString();
  }
  
  private String base64AndHex(Context paramContext, String content)
  {
    String str = null;
    try
    {
      content = URLEncoder.encode(content,"UTF-8");
      byte[] arrayOfByte = content.getBytes("UTF-8");
      str = Encrypt.parseByte2HexStr(Encrypt.base64(arrayOfByte).getBytes("UTF-8"));
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      localUnsupportedEncodingException.printStackTrace();
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
    return str;
  }
  
  private String getTime()
  {
    String str = null;
    Date localDate = new Date();
    str = Format.getDateAndTime(localDate.getTime());
    
    return str;
  }
  
  protected String throwableHtml(Throwable paramThrowable)
  {
    String str = null;
    try
    {
      str = Format.ThrowableToHtml(paramThrowable);
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
    return str;
  }
  
  private String throwableToStrings(Throwable paramThrowable)
  {
    return paramThrowable.toString();
  }
  
  private String getClientInfo(Context context, SDKInfo sdkInfo)
  {
    String str = ClientInfo.getClientInfo(context, sdkInfo);
    return str;
  }
  
  private String getAppKey(Context paramContext)
  {
    String str = AppInfo.getAppKey(paramContext);
    return str;
  }
  
  private boolean outToDiskCache(Context context, String md5, String dirName, String loginfo, LogDBOperation operation)
  {
    OutputStream outputStream = null;
    DiskLruCache localde = null;
    try
    {
      StringBuilder localStringBuilder = new StringBuilder();
      String str1 = context.getFilesDir().getAbsolutePath();
      localStringBuilder.append(str1);
      localStringBuilder.append(Log.a);
      localStringBuilder.append(dirName);
      String str2 = localStringBuilder.toString();
      File file = new File(str2);
      if ((!file.exists()) &&  (!file.mkdirs())) {
        return false;
      }
      File files[] = file.listFiles();
      localde = DiskLruCache.open(file, 1, 1, 20480L);
      
      localde.a(a(operation));
      DiskLruCache.Snapshot localb = localde.get(md5);
      if (localb != null) {
        return false;
      }
      byte[] arrayOfByte = loginfo.getBytes("UTF-8");
      DiskLruCache.Editor locala = localde.edit(md5);
      outputStream = locala.newOutputStream(0);
      outputStream.write(arrayOfByte);
      locala.commit();
      localde.flush();
      
      return true;
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
    catch (Throwable localThrowable1)
    {
      localThrowable1.printStackTrace();
    }
    finally
    {
      if (outputStream != null) {
        try
        {
          outputStream.close();
        }
        catch (Throwable localThrowable2)
        {
          localThrowable2.printStackTrace();
        }
      }
      if ((localde != null) && (!localde.isClosed())) {
        try
        {
          localde.close();
        }
        catch (Throwable localThrowable3)
        {
          localThrowable3.printStackTrace();
        }
      }
    }
    return false;
  }
  
  protected boolean isContain(String[] paramArrayOfString, String paramString)
  {
    boolean bool = false;
    try
    {
      if ((paramArrayOfString == null) || (paramString == null)) {
        return bool;
      }
      for (String str : paramArrayOfString) {
        if (paramString.indexOf(str) != -1) {
          return true;
        }
      }
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
    return bool;
  }
}
