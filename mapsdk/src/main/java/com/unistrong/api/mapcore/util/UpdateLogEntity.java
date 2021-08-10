package com.unistrong.api.mapcore.util;

import android.content.ContentValues;
import android.database.Cursor;

public class UpdateLogEntity//db
  implements SQlEntity<UpdateLogInfo>
{
  private UpdateLogInfo a = null;
  private static final String b = LogDBCreator.o;
  private static final String c = LogDBCreator.p;
  private static final String d = LogDBCreator.q;
  
  public ContentValues a()
  {
    ContentValues localContentValues = null;
    try
    {
      if (this.a == null) {
        return null;
      }
      localContentValues = new ContentValues();
      localContentValues.put(b, Boolean.valueOf(this.a.a()));
      
      localContentValues.put(c, Boolean.valueOf(this.a.b()));
      
      localContentValues.put(d, Boolean.valueOf(this.a.c()));
      
      return localContentValues;
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
    return localContentValues;
  }
  
  public UpdateLogInfo select(Cursor paramCursor)
  {
    UpdateLogInfo localdc = null;
    try
    {
      int i = paramCursor.getInt(1);
      int j = paramCursor.getInt(2);
      int k = paramCursor.getInt(3);
      boolean bool1 = false;
      boolean bool2 = false;
      boolean bool3 = false;
      if (i == 0) {
        bool1 = false;
      } else {
        bool1 = true;
      }
      if (j == 0) {
        bool2 = false;
      } else {
        bool2 = true;
      }
      if (k == 0) {
        bool3 = false;
      } else {
        bool3 = true;
      }
      localdc = new UpdateLogInfo();
      localdc.a(bool1);
      localdc.c(bool3);
      localdc.b(bool2);
      return localdc;
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
    return localdc;
  }
  
  public void setLogInfo(UpdateLogInfo paramdc)
  {
    this.a = paramdc;
  }
  
  public String getTableName()
  {
    return LogDBCreator.e;
  }
}
