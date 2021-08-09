package com.unistrong.api.mapcore.util;

import android.content.Context;
import java.util.List;

public class LogDBOperation//cv
{
  private DBOperation dbOperation;
  
  public LogDBOperation(Context paramContext)
  {
    this.dbOperation = new DBOperation(paramContext, LogDBCreator.getInstance());
  }
  
  private LogEntity getLog(int logType)
  {
    Object localObject = null;
    switch (logType)
    {
    case 0: 
      localObject = new CrashLogEntity();
      break;
    case 1: 
      localObject = new ExceptionLogEntity();
      break;
    case 2: 
      localObject = new AnrLogEntity();
      break;
    case 3:
      localObject = new NormalLogEntity();
      break;
    default: 
      return null;
    }
    return (LogEntity)localObject;
  }
  
  public void delLog(String paramString, int paramInt)
  {
    try
    {
      deleteLog(paramString, paramInt);
    }
    catch (Throwable localThrowable)
    {
      SDKLogHandler.exception(localThrowable, "LogDB", "delLog");
      localThrowable.printStackTrace();
    }
  }
  
  public void b(String paramString, int paramInt)
  {
    try
    {
      deleteLog(paramString, paramInt);
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }
  
  private void deleteLog(String paramString, int paramInt)
  {
    String whereString = LogEntity.getWhereString(paramString);
    LogEntity localcw = getLog(paramInt);
    this.dbOperation.deleteData(whereString, localcw);
  }
  
  public void updateLogInfo(LogInfo logInfo, int paramInt)
  {
    try
    {
      LogEntity logEntity = getLog(paramInt);
      logEntity.setLogInfo(logInfo);
      String str = LogEntity.getWhereString(logInfo.getMD5());
      this.dbOperation.updataDB(str, logEntity);
    }
    catch (Throwable localThrowable)
    {
      SDKLogHandler.exception(localThrowable, "LogDB", "updateLogInfo");
      localThrowable.printStackTrace();
    }
  }
  
  public List<LogInfo> a(int paramInt1, int paramInt2)
  {
    List<LogInfo> logInfoList = null;
    try
    {
      LogEntity localcw = getLog(paramInt2);
      String str = LogEntity.a(paramInt1);
      logInfoList = this.dbOperation.searchListData(str, localcw);
    }
    catch (Throwable localThrowable)
    {
      SDKLogHandler.exception(localThrowable, "LogDB", "ByState");
      localThrowable.printStackTrace();
    }
    return logInfoList;
  }
  
  public void writeToDB(LogInfo logInfo, int logType)
  {
    try
    {
      LogEntity localcw = getLog(logType);
      switch (logType)
      {
      case 0: //crash
        insertToDB(logInfo, localcw);
        break;
      case 1: //Exception
        updataDB(logInfo, localcw);
        break;
      case 2: //anr
        updataDB(logInfo, localcw);
        break;
        case 3:
          updataDB(logInfo, localcw);
          break;
      }
    }
    catch (Throwable throwable)
    {
      throwable.printStackTrace();
    }
  }
  
  private void insertToDB(LogInfo paramcx, LogEntity paramcw)
  {
    paramcw.setLogInfo(paramcx);
    this.dbOperation.insertDB(paramcw);
  }
  
  private void updataDB(LogInfo logInfo, LogEntity logEntity)
  {
    String whereString = LogEntity.getWhereString(logInfo.getMD5());
    
    List<LogInfo> logInfoList = this.dbOperation.searchListData(whereString, logEntity, true);
    if ((logInfoList == null) || (logInfoList.size() == 0))
    {
      logEntity.setLogInfo(logInfo);
      this.dbOperation.insertData(logEntity, true);
    }
    else
    {
      LogInfo logInfo1 = (LogInfo)logInfoList.get(0);
      if (logInfo.a() == 0)
      {
        int i = logInfo1.d();
        int j = i + 1;
        logInfo1.b(j);
      }
      else
      {
        logInfo1.b(0);
      }
      logEntity.setLogInfo(logInfo1);
      this.dbOperation.updataDB(whereString, logEntity);
    }
  }
}
