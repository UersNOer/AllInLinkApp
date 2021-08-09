package com.unistrong.api.mapcore.util;

import android.content.ContentValues;
import android.database.Cursor;
import java.util.HashMap;
import java.util.Map;

public abstract class LogEntity//cw
  implements SQlEntity<LogInfo>
{
  private static final String a = LogDBCreator.l;
  private static final String b = LogDBCreator.m;
  private static final String c = LogDBCreator.n;
  private static final String d = LogDBCreator.f;
  private LogInfo logInfo = null;
  
  public ContentValues a()
  {
    ContentValues localContentValues = null;
    try
    {
      if (this.logInfo == null) {
        return null;
      }
      localContentValues = new ContentValues();
      localContentValues.put(a, this.logInfo.getMD5());
      localContentValues.put(b, Integer.valueOf(this.logInfo.a()));
      localContentValues.put(d, EncryptUtil.a(this.logInfo.getSdkType()));
      localContentValues.put(c, Integer.valueOf(this.logInfo.d()));
      
      return localContentValues;
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
    return localContentValues;
  }
  
  //public cx a(Cursor paramCursor)
  @Override
  public LogInfo select(Cursor cursor)
  {
    LogInfo localcx = null;
    try
    {
      if (cursor == null) {
        return null;
      }
      String str1 = cursor.getString(1);
      int i = cursor.getInt(2);
      String str2 = cursor.getString(4);
      int j = cursor.getInt(3);
      
      localcx = new LogInfo();
      localcx.setMD5(str1);
      localcx.a(i);
      localcx.setSdkType(EncryptUtil.b(str2));
      localcx.b(j);
      return localcx;
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
    return localcx;
  }
  
  public void setLogInfo(LogInfo logInfo)
  {
    this.logInfo = logInfo;
  }
  
  public static String getWhereString(String paramString)
  {
    Map<String, String> localHashMap = new HashMap<String, String>();
    localHashMap.put(a, paramString);
    return DBOperation.whereStr(localHashMap);
  }
  
  public static String a(int paramInt)
  {
    StringBuilder stringBuilder = new StringBuilder();
    try
    {
      stringBuilder.append(b).append("=").append(paramInt);
    }
    catch (Throwable throwable)
    {
      throwable.printStackTrace();
    }
    return stringBuilder.toString();
  }
}
