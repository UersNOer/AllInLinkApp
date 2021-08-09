package com.unistrong.api.mapcore.util;

import java.util.List;

class ExceptionLogWriter//ch
  extends LogWriter
{
  private a a;
  
  protected int getType()
  {
    return 1;
  }
  
  protected String toMD5Encrypt(String paramString)
  {
    String str = MD5.encryptString(paramString);
    
    return str;
  }
  
  protected String getDirName()
  {
    return Log.exceptionlogdir;
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
        this.b.b(paramString, ExceptionLogWriter.this.getType());
      }
      catch (Throwable localThrowable)
      {
        localThrowable.printStackTrace();
      }
    }
  }
  
  protected String a(List<SDKInfo> paramList)
  {
    return null;
  }
}
