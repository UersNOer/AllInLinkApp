package com.unistrong.api.mapcore.util;

import java.util.Date;
import java.util.List;

class CrashLogWriter
  extends LogWriter
{
  private a a;
  
  protected int getType()
  {
    return 0;
  }
  
  protected String toMD5Encrypt(String paramString)
  {
    String str1 = null;
    
    String str2 = Format.getDateAndTime(new Date().getTime());
    String str3 = paramString + str2;
    str1 = MD5.encryptString(str3);
    
    return str1;
  }
  
  protected String getDirName()
  {
    return Log.crashlogdir;
  }
  
  class a
    implements FileOperationListenerDecode
  {
    private LogDBOperation b;
    
    a(LogDBOperation paramcv)
    {
      this.b = paramcv;
    }
    
    public void a(String paramString)
    {
      try
      {
        this.b.b(paramString, CrashLogWriter.this.getType());
      }
      catch (Throwable localThrowable)
      {
        localThrowable.printStackTrace();
      }
    }
  }
  
  protected FileOperationListenerDecode a(LogDBOperation paramcv)
  {
    try
    {
      if (this.a == null) {
        this.a = new a(paramcv);
      }
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
    return this.a;
  }
  
  protected String a(List<SDKInfo> paramList)
  {
    return null;
  }
}
