package com.unistrong.api.mapcore.util;

import android.database.sqlite.SQLiteDatabase;

public abstract interface DBCreator
{
  public abstract void onCreate(SQLiteDatabase db);
  
  public abstract void onUpgrade(SQLiteDatabase db, int paramInt1, int paramInt2);
  
  public abstract String getDBFileName();
  
  public abstract int c();
}
