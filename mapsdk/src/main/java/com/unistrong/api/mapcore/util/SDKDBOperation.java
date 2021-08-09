package com.unistrong.api.mapcore.util;

import android.content.Context;
import java.util.List;

public class SDKDBOperation
{
  private DBOperation a;
  private Context b;
  
  public SDKDBOperation(Context paramContext, boolean paramBoolean)
  {
    this.b = paramContext;
    this.a = a(this.b, paramBoolean);
  }
  
  private DBOperation a(Context paramContext, boolean paramBoolean)
  {
    DBOperation localco = null;
    try
    {
      localco = new DBOperation(paramContext, LogDBCreator.getInstance());
    }
    catch (Throwable localThrowable)
    {
      if (!paramBoolean) {
        SDKLogHandler.exception(localThrowable, "SDKDB", "getDB");
      } else {
        localThrowable.printStackTrace();
      }
    }
    return localco;
  }
  
  public void a(SDKInfo parambv)
  {
    try
    {
      if (parambv == null) {
        return;
      }
      if (this.a == null) {
        this.a = a(this.b, false);
      }
      SDKEntity localcz = new SDKEntity();
      localcz.setLogInfo(parambv);
      
      String str = SDKEntity.a(parambv.getProduct());
      List<SDKInfo> localList = this.a.searchListData(str, new SDKEntity());
      if ((localList == null) || (localList.size() == 0)) {
        this.a.insertDB(localcz);
      } else {
        this.a.updataDB(str, localcz);
      }
    }
    catch (Throwable localThrowable)
    {
      SDKLogHandler.exception(localThrowable, "SDKDB", "insert");
      localThrowable.printStackTrace();
    }
  }
  
  public List<SDKInfo> a()
  {
    List<SDKInfo> localList = null;
    try
    {
      SDKEntity sdkEntity = new SDKEntity();
      String str = SDKEntity.c();
      localList = this.a.searchListData(str, sdkEntity, true);
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
    return localList;
  }
}
