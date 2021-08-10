package com.unistrong.api.mapcore.util;

import android.database.sqlite.SQLiteDatabase;

public class LogDBCreator  implements DBCreator
{
  static final String a = "a";
  static final String CrashLogTable = "b";
  static final String ExceptionLogTable = "c";
  static final String AnrLogTable = "d";
  static final String NormalLogTable = "f";
  static final String e = "e";
  static final String f = "a1";
  static final String g = "a2";
  static final String h = "a3";
  static final String i = "a4";
  static final String j = "a5";
  static final String k = "a6";
  static final String l = "b1";
  static final String m = "b2";
  static final String n = "b3";
  static final String o = "c1";
  static final String p = "c2";
  static final String q = "c3";
  private static final String r = "CREATE TABLE IF NOT EXISTS " + a + " (_id integer primary key autoincrement, " + f + "  varchar(20), " + g + " varchar(10)," + h + " varchar(50)," + i + " varchar(100)," + j + " varchar(20)," + k + " integer);";
  private static final String s = "CREATE TABLE IF NOT EXISTS %s (_id integer primary key autoincrement," + l + " varchar(40), " + m + " integer," + n + "  integer," + f + "  varchar(20));";
  private static final String t = "CREATE TABLE IF NOT EXISTS " + e + " (_id integer primary key autoincrement," + o + " integer," + p + " integer," + q + " integer);";
  private static LogDBCreator instance;
  
  public static synchronized LogDBCreator getInstance()
  {
    if (instance == null) {
      instance = new LogDBCreator();
    }
    return instance;
  }
  
  public void onCreate(SQLiteDatabase paramSQLiteDatabase)
  {
    try
    {
      paramSQLiteDatabase.execSQL(r);
      paramSQLiteDatabase.execSQL(String.format(s, new Object[] {CrashLogTable}));
      paramSQLiteDatabase.execSQL(String.format(s, new Object[] {ExceptionLogTable}));
      paramSQLiteDatabase.execSQL(String.format(s, new Object[] {AnrLogTable}));
      paramSQLiteDatabase.execSQL(String.format(s, new Object[] {NormalLogTable}));
      paramSQLiteDatabase.execSQL(t);
    }
    catch (Throwable localThrowable)
    {
      SDKLogHandler.exception(localThrowable, "DB", "onCreate");
      localThrowable.printStackTrace();
    }
  }
  
  public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2) {}
  
  public String getDBFileName()
  {
    return "logdb.db";
  }
  
  public int c()
  {
    return 1;
  }
}
