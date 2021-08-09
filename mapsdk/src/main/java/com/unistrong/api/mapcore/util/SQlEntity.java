package com.unistrong.api.mapcore.util;

import android.content.ContentValues;
import android.database.Cursor;

public abstract interface SQlEntity<T>//cp
{
  public abstract ContentValues a();
  
  public abstract T select(Cursor paramCursor);
  
  public abstract void setLogInfo(T paramT);
  
  public abstract String getTableName();
}
