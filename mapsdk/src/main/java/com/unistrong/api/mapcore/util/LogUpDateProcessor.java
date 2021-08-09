package com.unistrong.api.mapcore.util;

import android.content.Context;
import android.os.Looper;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

abstract class LogUpDateProcessor
{
  private DiskLruCache cache;
  
  public static LogUpDateProcessor getLogProcessor(Context context, int paramInt)
  {
    Object localObject = null;
    switch (paramInt)
    {
    case 0: 
      localObject = new CrashLogUpDateProcessor(context);
      break;
    case 1: 
      localObject = new ExceptionLogUpDateProcessor(context);
      break;
    case 2: 
      localObject = new ANRLogUpDateProcessor(context);
      break;
      case 3:
        localObject = new NormalLogUpDateProcessor(context);
        break;
    default: 
      return null;
    }
    return (LogUpDateProcessor)localObject;
  }
  
  protected LogUpDateProcessor(Context context)
  {
    try
    {
      String str = getDir();
      this.cache = initDiskLru(context, str);
    }
    catch (Throwable localThrowable)
    {

      localThrowable.printStackTrace();
    }
  }
  
  void processUpdateLog(Context context)
  {
    try
    {
      if (!dbIsRight(context)) {
        return;
      }
      synchronized (Looper.getMainLooper())
      {
        LogDBOperation dbOperation = new LogDBOperation(context);
        
        processDelete(dbOperation, getLogType());
        
        List<LogInfo> localList = dbOperation.a(0, getLogType());
        if ((localList == null) || (localList.size() == 0)) {
          return;
        }
        String logStr = a(localList, context);
        android.util.Log.e("loginfo","_____log:"+logStr);
        if (logStr == null) {
          return;
        }
        int resStatus = processUpdate(context,logStr);// 上传LOG
        if (resStatus == 0) {
          deleteLog(localList, dbOperation, getLogType());
        }
      }
    }
    catch (Throwable throwable)
    {

      throwable.printStackTrace();
    }
  }
  
  private boolean a(String paramString)
  {
    if (this.cache == null) {
      return false;
    }
    boolean bool = false;
    try
    {
      bool = this.cache.remove(paramString);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    return bool;
  }
  
  protected abstract String getDir();
  
  protected abstract int getLogType();
  
  protected abstract boolean dbIsRight(Context context);
  
  private void processDelete(LogDBOperation dbOperation, int paramInt)
  {
    try
    {
      List<LogInfo> localList = dbOperation.a(2, paramInt);
      
      deleteLog(localList, dbOperation, paramInt);
    }
    catch (Throwable localThrowable)
    {

      localThrowable.printStackTrace();
    }
  }
  
  private int processUpdate(Context context,String logStr)
  {
    int resStatus = 1;
    byte[] zipData = null;
    try
    {
      zipData = Utils.gZip(logStr.getBytes("UTF-8"));
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException1)
    {
      zipData = Utils.gZip(logStr.getBytes());
    }
    LogUpdateRequest request = new LogUpdateRequest(context,zipData);
    try
    {
      byte[] resBytes = NetManger.a(false).makeSyncPostRequest(request);
      if (resBytes == null) {
        return resStatus;
      }

      String resStr = null;
      try
      {
        resStr = new String(resBytes, "UTF-8");
      }
      catch (UnsupportedEncodingException e)
      {
        resStr = new String(resBytes);
      }
      try
      {
        JSONObject resJSON = new JSONObject(resStr);
        String status = "status";
        if (resJSON.has(status)) {
          android.util.Log.e("LogUpDateProcessor","res:"+resJSON.toString());
          resStatus = resJSON.getInt(status);
        }
      }
      catch (JSONException e)
      {

        e.printStackTrace();
      }
    }
    catch (IMMapCoreException localbl)
    {
      localbl.printStackTrace();
    }
    return resStatus;
  }
  
  private void deleteLog(List<LogInfo> list, LogDBOperation logDBOperation, int logType)
  {
    if ((list != null) && (list.size() > 0)) {
      for (LogInfo logInfo : list)
      {
        boolean bool = a(logInfo.getMD5());
        if (bool)
        {
          logDBOperation.delLog(logInfo.getMD5(), logType);
        }
        else
        {
          logInfo.a(2);
          logDBOperation.updateLogInfo(logInfo, logInfo.a());
        }
      }
    }
  }
  
  private String getPublicInfo(Context context)
  {
    String str1 = null;
    try
    {
      String str2 = WrapperClientInfo.getPublicInfo(context);
      if ("".equals(str2)) {
        return str1;
      }
      str2 = URLEncoder.encode(str2,"UTF-8");
      byte[] arrayOfByte = str2.getBytes("UTF-8");
      return Encrypt.parseByte2HexStr(Encrypt.base64(arrayOfByte).getBytes("UTF-8"));
    }
    catch (UnsupportedEncodingException e)
    {
      e.printStackTrace();
    }
    catch (Throwable throwable)
    {
      throwable.printStackTrace();
    }
    return str1;
  }
  
  private String a(List<LogInfo> logInfoList, Context context)
  {
    StringBuilder buf = new StringBuilder();
    buf.append("{\"pinfo\":\"").append(getPublicInfo(context)).append("\",\"elogs\":[");
    
    int i = 1;
    for (LogInfo logInfo : logInfoList)
    {
      String loginfo = readLog(logInfo.getMD5());
      if ((loginfo != null) && (!"".equals(loginfo)))
      {
        //TODO WCB logInfo.d() 意义未知，暂时屏蔽
//        loginfo = loginfo + "||" + logInfo.d();
        if (i != 0) {
          i = 0;
        } else {
          buf.append(",");
        }
        buf.append("{\"log\":\"").append(loginfo).append("\"}");
      }
    }
    if (i != 0) {
      return null;
    }
    buf.append("]}");
    return buf.toString();
  }
  
  private String readLog(String md5)
  {
    InputStream inputStream = null;
    String str1 = null;
    ByteArrayOutputStream byteoutputstream = null;
    try
    {
      if (this.cache == null) {
        return str1;
      }
      Object localObject1 = this.cache.get(md5);
      if (localObject1 == null) {
        return str1;
      }
      inputStream = ((DiskLruCache.Snapshot)localObject1).getInputStream(0);
      int i = -1;
      byteoutputstream = new ByteArrayOutputStream();
      byte[] arrayOfByte = new byte['Ѐ'];
      while ((i = inputStream.read(arrayOfByte)) != -1) {
        byteoutputstream.write(arrayOfByte, 0, i);
      }
      return byteoutputstream.toString("utf-8");
    }
    catch (IOException ioex)
    {
      ioex.printStackTrace();
    }
    catch (Throwable throwable)
    {
      throwable.printStackTrace();
    }
    finally
    {
      if (byteoutputstream != null) {
        try
        {
          byteoutputstream.close();
        }
        catch (IOException e)
        {

          e.printStackTrace();
        }
      }
      if (inputStream != null) {
        try
        {
          inputStream.close();
        }
        catch (IOException localIOException13)
        {

          localIOException13.printStackTrace();
        }
      }
    }
    return str1;
  }
  
  void closeDiskLru()
  {
    if ((this.cache != null) && (!this.cache.isClosed())) {
      try
      {
        this.cache.close();
      }
      catch (IOException localIOException)
      {
        localIOException.printStackTrace();
      }
      catch (Throwable localThrowable)
      {
        localThrowable.printStackTrace();
      }
    }
  }
  
  private DiskLruCache initDiskLru(Context context, String paramString)
  {
    DiskLruCache localde = null;
    try
    {
      StringBuilder buf = new StringBuilder();
      String str1 = context.getFilesDir().getAbsolutePath();
      buf.append(str1);
      buf.append(Log.a);
      buf.append(paramString);
      String str2 = buf.toString();
      File localFile = new File(str2);
      if ((!localFile.exists()) && (!localFile.mkdirs())) {
        return null;
      }
      localde = DiskLruCache.open(localFile, 1, 1, 20480L);
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
    return localde;
  }
}
