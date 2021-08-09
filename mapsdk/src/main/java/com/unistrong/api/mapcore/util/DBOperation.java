package com.unistrong.api.mapcore.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DBOperation//co
{
  private DBHelper dbHelper;
  private SQLiteDatabase db;
  private DBCreator dbCreator;
  
  public DBOperation(Context context, DBCreator dbCreator)
  {
    try
    {
      this.dbHelper = new DBHelper(context, dbCreator.getDBFileName(), null, dbCreator.c(), dbCreator);
    }
    catch (Throwable throwable)
    {
      throwable.printStackTrace();
    }
    this.dbCreator = dbCreator;
  }
  
  private SQLiteDatabase getReadAbleDataBase(boolean paramBoolean)
  {
    try
    {
      this.db = this.dbHelper.getReadableDatabase();
    }
    catch (Throwable throwable)
    {
      if (!paramBoolean) {
        BasicLogHandler.a(throwable, "DBOperation", "getReadAbleDataBase");
      } else {
        throwable.printStackTrace();
      }
    }
    return this.db;
  }
  
  private SQLiteDatabase getWritableDatabase(boolean paramBoolean)
  {
    try
    {
      this.db = this.dbHelper.getWritableDatabase();
    }
    catch (Throwable throwable)
    {
      BasicLogHandler.a(throwable, "DBOperation", "getWritableDatabase");
    }
    return this.db;
  }
  
  public static String whereStr(Map<String, String> paramMap)
  {
    if (paramMap == null) {
      return "";
    }
    StringBuilder buf = new StringBuilder();
    int i = 1;
    for (String str : paramMap.keySet()) {
      if (i != 0)
      {
        buf.append(str).append(" = '").append((String)paramMap.get(str)).append("'");
        
        i = 0;
      }
      else
      {
        buf.append(" and ").append(str).append(" = '").append((String)paramMap.get(str)).append("'");
      }
    }
    return buf.toString();
  }
  
  public <T> void deleteData(String paramString, SQlEntity<T> paramcp)
  {
    synchronized (this.dbCreator)
    {
      if ((paramcp.getTableName() == null) || (paramString == null)) {
        return;
      }
      if ((this.db == null) || (this.db.isReadOnly())) {
        this.db = getWritableDatabase(false);
      }
      if (this.db == null) {
        return;
      }
      try
      {
        this.db.delete(paramcp.getTableName(), paramString, null);
      }
      catch (Throwable localThrowable)
      {
        BasicLogHandler.a(localThrowable, "DataBase", "deleteData");
      }
      finally
      {
        if (this.db != null)
        {
          this.db.close();
          this.db = null;
        }
      }
    }
  }
  
  public <T> void updataDB(String paramString, SQlEntity<T> paramcp)
  {
    updateData(paramString, paramcp, false);
  }
  
  public <T> void updateData(String logMD5, SQlEntity<T> logInfo, boolean paramBoolean)
  {
    synchronized (this.dbCreator)
    {
      if ((logInfo == null) || (logMD5 == null) || (logInfo.getTableName() == null)) {
        return;
      }
      ContentValues localContentValues = logInfo.a();
      if (localContentValues == null) {
        return;
      }
      if ((this.db == null) || (this.db.isReadOnly())) {
        this.db = getWritableDatabase(paramBoolean);
      }
      if (this.db == null) {
        return;
      }
      try
      {
        this.db.update(logInfo.getTableName(), localContentValues, logMD5, null);
      }
      catch (Throwable localThrowable)
      {
        if (!paramBoolean) {
          BasicLogHandler.a(localThrowable, "DataBase", "updateData");
        } else {
          localThrowable.printStackTrace();
        }
      }
      finally
      {
        if (this.db != null)
        {
          this.db.close();
          this.db = null;
        }
      }
    }
  }
  
  public <T> void searchListData(SQlEntity<T> pentityramcp, String paramString)
  {
    synchronized (this.dbCreator)
    {
      List<T> localList = searchListData(paramString, pentityramcp);
      if ((localList == null) || (localList.size() == 0)) {
        insertDB(pentityramcp);
      } else {
        updataDB(paramString, pentityramcp);
      }
    }
  }
  
  public <T> void insertDB(SQlEntity<T> entity)
  {
    insertData(entity, false);
  }
  
  public <T> void insertData(SQlEntity<T> entity, boolean paramBoolean)
  {
    synchronized (this.dbCreator)
    {
      if ((this.db == null) || (this.db.isReadOnly())) {
        this.db = getWritableDatabase(paramBoolean);
      }
      if (this.db == null) {
        return;
      }
      try
      {
        a(this.db, entity);
      }
      catch (Throwable throwable)
      {
        BasicLogHandler.a(throwable, "DataBase", "insertData");
      }
      finally
      {
        if (this.db != null)
        {
          this.db.close();
          this.db = null;
        }
      }
    }
  }
  
  private <T> void a(SQLiteDatabase database, SQlEntity<T> entity)
  {
    if ((entity == null) || (database == null)) {
      return;
    }
    ContentValues localContentValues = entity.a();
    if ((localContentValues == null) || (entity.getTableName() == null)) {
      return;
    }
    database.insert(entity.getTableName(), null, localContentValues);
  }
  
  public <T> void insertListData(List<SQlEntity<T>> paramList)
  {
    synchronized (this.dbCreator)
    {
      if ((paramList == null) || (paramList.size() == 0)) {
        return;
      }
      if ((this.db == null) || (this.db.isReadOnly())) {
        this.db = getWritableDatabase(false);
      }
      if (this.db == null) {
        return;
      }
      try
      {
        this.db.beginTransaction();
        for (SQlEntity<T> localcp : paramList) {
          a(this.db, localcp);
        }
        this.db.setTransactionSuccessful();
      }
      catch (Throwable localThrowable)
      {
        BasicLogHandler.a(localThrowable, "DataBase", "insertListData");
      }
      finally
      {
        this.db.endTransaction();
        this.db.close();
        this.db = null;
      }
    }
  }
  
  public <T> List<T> searchListData(String adcodeStr, SQlEntity<T> sQlEntity, boolean paramBoolean)
  {
    synchronized (this.dbCreator)
    {
      ArrayList<T> cityList = new ArrayList<T>();
      Cursor cursor = null;
      if (this.db == null) {
        this.db = getReadAbleDataBase(paramBoolean);
      }
      if ((this.db == null) || (sQlEntity.getTableName() == null) || (adcodeStr == null)) {
        return cityList;
      }
      try
      {
        cursor = this.db.query(sQlEntity.getTableName(), null, adcodeStr, null, null, null, null);
        if (cursor == null)
        {
          this.db.close();
          this.db = null;
          ArrayList<T> localArrayList2 = cityList;
          try
          {
            if (cursor != null) {
              cursor.close();
            }
          }
          catch (Throwable throwable1)
          {
            if (!paramBoolean) {
              BasicLogHandler.a(throwable1, "DataBase", "searchListData");
            }
          }
          try
          {
            if (this.db != null)
            {
              this.db.close();
              this.db = null;
            }
          }
          catch (Throwable throwable)
          {
            if (!paramBoolean) {
              BasicLogHandler.a(throwable, "DataBase", "searchListData");
            }
          }
          return localArrayList2;
        }
        while (cursor.moveToNext()) {
          cityList.add(sQlEntity.select(cursor));
        }
        try
        {
          if (cursor != null) {
            cursor.close();
          }
        }
        catch (Throwable throwable1)
        {
          if (!paramBoolean) {
            BasicLogHandler.a(throwable1, "DataBase", "searchListData");
          }
        }
        try
        {
          if (this.db != null)
          {
            this.db.close();
            this.db = null;
          }
        }
        catch (Throwable throwable)
        {
          if (!paramBoolean) {
            BasicLogHandler.a(throwable, "DataBase", "searchListData");
          }
        }
      }
      catch (Throwable throwable2)
      {
        if (!paramBoolean) {
          BasicLogHandler.a(throwable2, "DataBase", "searchListData");
        }
      }
      finally
      {
        try
        {
          if (cursor != null) {
            cursor.close();
          }
        }
        catch (Throwable throwable)
        {
          if (!paramBoolean) {
            BasicLogHandler.a(throwable, "DataBase", "searchListData");
          }
        }
        try
        {
          if (this.db != null)
          {
            this.db.close();
            this.db = null;
          }
        }
        catch (Throwable throwable)
        {
          if (!paramBoolean) {
            BasicLogHandler.a(throwable, "DataBase", "searchListData");
          }
        }
      }
      return cityList;
    }
  }
  
  public <T> List<T> searchListData(String adcodeStr, SQlEntity<T> paramcp)
  {
    return searchListData(adcodeStr, paramcp, false);
  }
}
