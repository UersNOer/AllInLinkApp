package com.unistrong.api.mapcore.util;

import android.content.Context;

import com.unistrong.api.mapcore.ConfigableConstDecode;
import com.unistrong.api.maps.UnistrongException;
import com.unistrong.api.maps.MapsInitializer;

import java.util.HashMap;
import java.util.Map;

public abstract class ProtocalHandler<T, V>//aj
  extends Request
{
  protected T task;
  protected int maxTry = 1;
  protected String c = "";
  protected Context cnt;
  private int j = 1;
  protected final int e = 5000;
  protected final int f = 50000;
  
  public ProtocalHandler(Context context, T paramT)
  {
    a(context, paramT);
  }
  
  private void a(Context context, T task)
  {
    this.cnt = context;
    this.task = task;
  }
  
  protected abstract V parseJson(String paramString)
    throws UnistrongException;
  
  public V getData()
    throws UnistrongException
  {
    if (null != this.task) {
      return (V) getDataMayThrow();
    }
    return null;
  }
  
  private V getDataMayThrow()
    throws UnistrongException
  {
    Object localObject = null;
    int trytime = 0;
    while (trytime < this.maxTry) {
      try
      {
        NetManger netManger = NetManger.a(false);
//        setProxy(ProxyUtil.getProxy(this.cnt));
        byte[] arrayOfByte = null;
//        arrayOfByte = netManger.d(this);
        arrayOfByte = netManger.makeSyncPostRequest(this);
        localObject = a(arrayOfByte);
        trytime = this.maxTry;
      }
      catch (UnistrongException localMapException)
      {
        SDKLogHandler.exception(localMapException, "ProtocalHandler", "getDataMayThrow MapException");
        
        localMapException.printStackTrace();
        trytime++;
        if (trytime >= this.maxTry)
        {
          localObject = null;
          throw new UnistrongException(localMapException.getErrorMessage());
        }
      }
      catch (IMMapCoreException localbl)
      {
        SDKLogHandler.exception(localbl, "ProtocalHandler", "getDataMayThrow MapCoreException");
        
        localbl.printStackTrace();
        trytime++;
        if (trytime < this.maxTry)
        {
          try
          {
            Thread.sleep(this.j * 1000);
          }
          catch (InterruptedException localInterruptedException)
          {
            SDKLogHandler.exception(localbl, "ProtocalHandler", "getDataMayThrow InterruptedException");
            
            localbl.printStackTrace();
            throw new UnistrongException(localbl.getMessage());
          }
        }
        else
        {
          localObject = e();
          throw new UnistrongException(localbl.getErrorMessage());
        }
      }
    }
    return (V)localObject;
  }
  
  protected V getObject(byte[] bytes)
    throws UnistrongException
  {
    String jsonStr = null;
    try
    {
      jsonStr = new String(bytes, "utf-8");
    }
    catch (Exception localException)
    {
      SDKLogHandler.exception(localException, "ProtocalHandler", "loadData Exception");
      
      localException.printStackTrace();
    }
    if ((jsonStr == null) || (jsonStr.equals(""))) {
      return null;
    }
    Util.paseAuthFailurJson(jsonStr);
    return (V) parseJson(jsonStr);
  }
  
  public Map<String, String> getHeadMaps()
  {
    SDKInfo sdkInfo = null;
    try {
      sdkInfo = new SDKInfo.createSDKInfo(ConfigableConstDecode.product,
              MapsInitializer.getVersion(),
              ConfigableConstDecode.userAgent, "test")
              .setPackageName(new String[]{"com.leador.api.maps", "com.leador.api.mapcore", "com.leador.mapcore"}).a();
    } catch (IMMapCoreException e1) {
      e1.printStackTrace();
    }
    Map<String,String> localHashMap = new HashMap<String,String>();
    localHashMap.put("Content-Type", "application/x-www-form-urlencoded");
    localHashMap.put("Accept-Encoding", "gzip");
    localHashMap.put("Connection", "Keep-Alive");
    localHashMap.put("User-Agent", ConfigableConstDecode.userAgent);
    localHashMap.put("X-INFO", 
      ClientInfo.initXInfo(this.cnt, Util.getSDKInfo(), null, false));
    localHashMap.put("logversion", "1.2");
    localHashMap.put("platinfo", String.format("platform=Android&sdkversion=%s&product=%s",
            new Object[]{sdkInfo.version, sdkInfo.product}));

    return localHashMap;
  }
  
  private V a(byte[] bytes)
    throws UnistrongException
  {
    return (V) getObject(bytes);
  }
  
  protected V e()
  {
    return null;
  }
}
