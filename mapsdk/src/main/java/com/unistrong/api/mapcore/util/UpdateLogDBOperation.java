package com.unistrong.api.mapcore.util;

import android.content.Context;
import java.util.List;
//日志数据库更新
public class UpdateLogDBOperation//da
{
  private DBOperation dbOperation;
  private Context context;
  
  public UpdateLogDBOperation(Context context)
  {
    this.context = context;
    this.dbOperation = getDB(this.context);
  }
  
  private DBOperation getDB(Context context)
  {
    DBOperation dbOperation = null;
    try
    {
      dbOperation = new DBOperation(context, LogDBCreator.getInstance());
    }
    catch (Throwable throwable)
    {
      SDKLogHandler.exception(throwable, "UpdateLogDB", "getDB");
      throwable.printStackTrace();
    }
    return dbOperation;
  }
  
  public UpdateLogInfo getUpdateLog()
  {
    UpdateLogInfo localdc = null;
    try
    {
      if (this.dbOperation == null) {
        this.dbOperation = getDB(this.context);
      }
      String str = "1=1";
      List localList = this.dbOperation.searchListData(str, new UpdateLogEntity());
      if (localList.size() > 0) {
        localdc = (UpdateLogInfo)localList.get(0);
      }
    }
    catch (Throwable throwable)
    {
      SDKLogHandler.exception(throwable, "UpdateLogDB", "getUpdateLog");
      throwable.printStackTrace();
    }
    return localdc;
  }
  
  public void updateLog(UpdateLogInfo updateLogInfo)
  {
    try
    {
      if (updateLogInfo == null) {
        return;
      }
      if (this.dbOperation == null) {
        this.dbOperation = getDB(this.context);
      }
      UpdateLogEntity localdb = new UpdateLogEntity();
      localdb.setLogInfo(updateLogInfo);
      
      String str = "1=1";
      List localList = this.dbOperation.searchListData(str, new UpdateLogEntity());
      if ((localList == null) || (localList.size() == 0)) {
        this.dbOperation.insertDB(localdb);
      } else {
        this.dbOperation.updataDB(str, localdb);
      }
    }
    catch (Throwable throwable)
    {
      SDKLogHandler.exception(throwable, "UpdateLogDB", "updateLog");
      throwable.printStackTrace();
    }
  }
}
