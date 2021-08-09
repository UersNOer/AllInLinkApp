package com.unistrong.api.mapcore.util;

import android.content.ContentValues;
import android.database.Cursor;
import java.util.HashMap;
import java.util.Map;

public class SDKEntity
  implements SQlEntity<SDKInfo>
{
  private static String a = LogDBCreator.f;
  private static String b = LogDBCreator.g;
  private static String c = LogDBCreator.k;
  private static String d = LogDBCreator.h;
  private static String e = LogDBCreator.i;
  private static String f = LogDBCreator.j;
  private SDKInfo g = null;
  
  public ContentValues a()
  {
    ContentValues localContentValues = null;
    try
    {
      if (this.g == null) {
        return null;
      }
      localContentValues = new ContentValues();
      localContentValues.put(a, EncryptUtil.a(this.g.getProduct()));
      
      localContentValues.put(b, EncryptUtil.a(this.g.getVersion()));
      
      localContentValues.put(c, Boolean.valueOf(this.g.e()));
      localContentValues.put(d, EncryptUtil.a(this.g.getUseragen()));
      
      localContentValues.put(f, EncryptUtil.a(this.g.d()));
      
      localContentValues.put(e, EncryptUtil.a(a(this.g.getPackageNames())));
      
      return localContentValues;
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
    return localContentValues;
  }
  
  //public bv a(Cursor paramCursor)
  public SDKInfo select(Cursor paramCursor)
  {
    SDKInfo localbv = null;
    try
    {
      String str1 = EncryptUtil.b(paramCursor.getString(1));
      String str2 = EncryptUtil.b(paramCursor.getString(2));
      String str3 = EncryptUtil.b(paramCursor.getString(3));
      String str4 = EncryptUtil.b(paramCursor.getString(4));
      String[] arrayOfString = b(str4);
      String str5 = EncryptUtil.b(paramCursor.getString(5));
      int i = paramCursor.getInt(6);
      boolean bool = false;
      if (i == 0) {
        bool = false;
      } else {
        bool = true;
      }
      return new SDKInfo.createSDKInfo(str1, str2, str3,"").a(bool).a(str5).setPackageName(arrayOfString).a();
    }
    catch (IMMapCoreException localbl)
    {
      localbl.printStackTrace();
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
    return localbv;
  }
  
  public void setLogInfo(SDKInfo parambv)
  {
    this.g = parambv;
  }
  
  public String getTableName()
  {
    return LogDBCreator.a;
  }
  
  private String[] b(String paramString)
  {
    try
    {
      return paramString.split(";");
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
    return null;
  }
  
  private String a(String[] paramArrayOfString)
  {
    try
    {
      if (paramArrayOfString == null) {
        return null;
      }
      StringBuilder localStringBuilder = new StringBuilder();
      for (String str : paramArrayOfString) {
        localStringBuilder.append(str).append(";");
      }
      return localStringBuilder.toString();
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
    return null;
  }
  
  public static String a(String paramString)
  {
    Map<String,String> localHashMap = new HashMap<String,String>();
    localHashMap.put(a, EncryptUtil.a(paramString));
    
    return DBOperation.whereStr(localHashMap);
  }
  
  public static String c()
  {
    return c + "=1";
  }
}
