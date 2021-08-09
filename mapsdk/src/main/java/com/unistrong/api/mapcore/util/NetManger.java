package com.unistrong.api.mapcore.util;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import java.net.Proxy;

public class NetManger//do
  extends BaseNetManager
{
  private static NetManger netManger = null;
  private ThreadPool pool;
  private Handler handler;
  
  public static NetManger a(boolean paramBoolean)
  {
    return a(paramBoolean, 5);
  }
  
  private static synchronized NetManger a(boolean paramBoolean, int nThreads)
  {
    try
    {
      if (netManger == null) {
        netManger = new NetManger(paramBoolean, nThreads);
      } else if ((paramBoolean) && (netManger.pool == null)) {
        netManger.pool = ThreadPool.getInstance(nThreads);
      }
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
    return netManger;
  }
  
  private NetManger(boolean paramBoolean, int nThreads)
  {
    try
    {
      if (paramBoolean) {
        this.pool = ThreadPool.getInstance(nThreads);
      }
      if (Looper.myLooper() == null) {
        //this.c = new a(Looper.getMainLooper(), null);
        this.handler = new a(Looper.getMainLooper());
      } else {
        this.handler = new a();
      }
    }
    catch (Throwable localThrowable)
    {
      SDKLogHandler.exception(localThrowable, "NetManger", "NetManger1");
      localThrowable.printStackTrace();
    }
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
      localThrowable.printStackTrace();
      SDKLogHandler.getLogHandler().b(localThrowable, "NetManager", "makeSyncPostRequest");
      throw new IMMapCoreException("未知的错误");
    }
    if (localdr != null) {
      return localdr.resBytes;
    }
    return null;
  }
  
  public byte[] d(Request paramdp)
    throws IMMapCoreException
  {
    ResponseEntity localdr = null;
    try
    {
      if (paramdp.getUrl().startsWith("https")){
        localdr = b(paramdp, true);
      } else {
        localdr = b(paramdp, false);
      }
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
  
  public ResponseEntity b(Request paramdp, boolean paramBoolean)
    throws IMMapCoreException
  {
    ResponseEntity localdr = null;
    try
    {
      checkUrl(paramdp);
      Proxy localProxy;
      if (paramdp.proxy == null) {
        localProxy = null;
      } else {
        localProxy = paramdp.proxy;
      }
      HttpUrlUtil urlUtil = new HttpUrlUtil(paramdp.connTimeout, paramdp.readTimeout, localProxy, paramBoolean);
      
      localdr = urlUtil.makeGetRequest(paramdp.getUrl(), paramdp.getHeadMaps(), paramdp.getRequestParam());
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
  
  private void a(IMMapCoreException parambl, Response paramdq)
  {
    ResponseMessageEntity localdt = new ResponseMessageEntity();
    localdt.mapCore = parambl;
    localdt.response = paramdq;
    Message localMessage = Message.obtain();
    localMessage.obj = localdt;
    localMessage.what = 1;
    this.handler.sendMessage(localMessage);
  }
  
  private void a(ResponseEntity paramdr, Response paramdq)
  {
    paramdq.a(paramdr.b, paramdr.resBytes);
    ResponseMessageEntity localdt = new ResponseMessageEntity();
    localdt.response = paramdq;
    Message localMessage = Message.obtain();
    localMessage.obj = localdt;
    localMessage.what = 0;
    this.handler.sendMessage(localMessage);
  }
  
  static class a
    extends Handler
  {
    private a(Looper looper)
    {
      super(looper);
    }
    
    public a() {}
    
    public void handleMessage(Message paramMessage)
    {
      try
      {
        int i = paramMessage.what;
        switch (i)
        {
        case 0: 
          ResponseMessageEntity localdt1 = (ResponseMessageEntity)paramMessage.obj;
          Response localdq1 = localdt1.response;
          localdq1.a();
          break;
        case 1: 
          ResponseMessageEntity localdt2 = (ResponseMessageEntity)paramMessage.obj;
          Response localdq2 = localdt2.response;
          localdq2.a(localdt2.mapCore);
          break;
        }
      }
      catch (Throwable throwable)
      {
        throwable.printStackTrace();
      }
    }
  }
}
