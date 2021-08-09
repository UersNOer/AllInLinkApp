package com.unistrong.api.mapcore.util;

import android.text.TextUtils;
import java.io.UnsupportedEncodingException;
import java.net.Proxy;
import java.util.Map;

public abstract class Request//dp
{
  int connTimeout = 20000;
  int readTimeout = 20000;
  Proxy proxy = null;
  
  String getUrlAndParam()
  {
    byte[] arrayOfByte = a_();
    if ((arrayOfByte == null) || (arrayOfByte.length == 0)) {
      return getUrl();
    }
    Map localMap = getRequestParam();
    if (localMap == null) {
      return getUrl();
    }
    String str = HttpUrlUtil.getParaString(localMap);
    
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append(getUrl()).append("?").append(str);
    return localStringBuffer.toString();
  }
  
  byte[] getConnectionDatas()
  {
    byte[] arrayOfByte = null;
    arrayOfByte = a_();
    if ((arrayOfByte == null) || (arrayOfByte.length == 0))
    {
      Map localMap = getRequestParam();
      String str = HttpUrlUtil.getParaString(localMap);
      try
      {
        if (!TextUtils.isEmpty(str)) {
          arrayOfByte = str.getBytes("UTF-8");
        }
      }
      catch (UnsupportedEncodingException exception)
      {
        arrayOfByte = str.getBytes();
        BasicLogHandler.a(exception, "Request", "getConnectionDatas");
      }
    }
    return arrayOfByte;
  }
  
  public abstract Map<String, String> getHeadMaps();
  
  public abstract Map<String, String> getRequestParam();
  
  public abstract String getUrl();
  
  public final void setConnTimeout(int paramInt)
  {
    this.connTimeout = paramInt;
  }
  
  public final void setReadTimeout(int paramInt)
  {
    this.readTimeout = paramInt;
  }
  
  public byte[] a_()
  {
    return null;
  }
  
  public final void setProxy(Proxy paramProxy)
  {
    this.proxy = paramProxy;
  }
}
