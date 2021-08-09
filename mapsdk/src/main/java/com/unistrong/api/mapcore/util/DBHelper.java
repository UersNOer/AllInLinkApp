package com.unistrong.api.mapcore.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper//cs
  extends SQLiteOpenHelper
{
  private DBCreator dbCreator;
  
  public DBHelper(Context paramContext, String paramString, SQLiteDatabase.CursorFactory paramCursorFactory, int paramInt, DBCreator dbCreator)
  {
    super(paramContext, paramString, paramCursorFactory, paramInt);
    this.dbCreator = dbCreator;
  }
  
  public void onCreate(SQLiteDatabase paramSQLiteDatabase)
  {
    this.dbCreator.onCreate(paramSQLiteDatabase);
  }
  
  public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2)
  {
    this.dbCreator.onUpgrade(paramSQLiteDatabase, paramInt1, paramInt2);
  }
}
