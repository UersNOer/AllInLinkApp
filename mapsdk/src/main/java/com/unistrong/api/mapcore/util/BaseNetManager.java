package com.unistrong.api.mapcore.util;

import java.net.Proxy;

public class BaseNetManager//dk
{
  private static BaseNetManager Instance;
  
  public static BaseNetManager getInstance()
  {
    if (Instance == null) {
      Instance = new BaseNetManager();
    }
    return Instance;
  }
  
  public byte[] a(Request paramdp)
    throws IMMapCoreException
  {
    ResponseEntity localdr = null;
    try
    {
      localdr = a(paramdp, true);
    }
    catch (IMMapCoreException localbl)
    {
      throw localbl;
    }
    catch (Throwable localThrowable)
    {
      throw new IMMapCoreException("未知的错误");
    }
    if (localdr != null) {
      return localdr.resBytes;
    }
    return null;
  }
  
  public byte[] makeSyncPostRequest(Request paramdp)
    throws IMMapCoreException
  {
    ResponseEntity localdr = null;
    try
    {
      if (paramdp.getUrl().startsWith("https")){
        localdr = a(paramdp, true);
      } else {
        localdr = a(paramdp, false);
      }
    }
    catch (IMMapCoreException localbl)
    {
      throw localbl;
    }
    catch (Throwable localThrowable)
    {
      BasicLogHandler.a(localThrowable, "BaseNetManager", "makeSyncPostRequest");
      
      throw new IMMapCoreException("未知的错误");
    }
    if (localdr != null) {
      return localdr.resBytes;
    }
    return null;
  }
  
  protected void checkUrl(Request request)
    throws IMMapCoreException
  {
    if (request == null) {
      throw new IMMapCoreException("requeust is null");
    }
    if ((request.getUrl() == null) || ("".equals(request.getUrl()))) {
      throw new IMMapCoreException("request url is empty");
    }
  }
  
  protected ResponseEntity a(Request request, boolean isSSL)
    throws IMMapCoreException
  {
    ResponseEntity localdr = null;
    try
    {
      checkUrl(request);
      Proxy proxy;
      if (request.proxy == null) {
        proxy = null;
      } else {
        proxy = request.proxy;
      }
      HttpUrlUtil http = new HttpUrlUtil(request.connTimeout, request.readTimeout, proxy, isSSL);
      localdr = http.makePostReqeust(request.getUrlAndParam(), request.getHeadMaps(), request.getConnectionDatas());
    }
    catch (IMMapCoreException localbl)
    {
      throw localbl;
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
      throw new IMMapCoreException("未知的错误");
    }
    return localdr;
  }
}
