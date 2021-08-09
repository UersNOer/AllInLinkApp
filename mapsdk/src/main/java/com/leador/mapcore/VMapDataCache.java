package com.leador.mapcore;

import com.unistrong.api.mapcore.util.LogManager;
import java.util.ArrayList;
import java.util.HashMap;

public class VMapDataCache
{
  private static final int MAXSIZE = 400;
  HashMap<String, e> vMapDataHs = new HashMap<String,e>();
  ArrayList<String> vMapDataList = new ArrayList<String>();
  HashMap<String, e> vCancelMapDataHs = new HashMap<String, e>();
  ArrayList<String> vCancelMapDataList = new ArrayList<String>();
  private static VMapDataCache instance;
  
  public static VMapDataCache getInstance()
  {
    if (instance == null) {
      instance = new VMapDataCache();
    }
    return instance;
  }
  
  public synchronized void reset()
  {
    LogManager.writeLog(LogManager.productInfo, hashCode() + " VMapData reset, clear all list", 111);
    this.vMapDataHs.clear();
    this.vMapDataList.clear();
    
    this.vCancelMapDataHs.clear();
    this.vCancelMapDataList.clear();
  }
  
  public int getSize()
  {
    return this.vMapDataHs.size();
  }
  
  static String getKey(String paramString, int paramInt)
  {
    return paramString + "-" + paramInt;
  }
  
  public synchronized e getRecoder(String paramString, int paramInt)
  {
    LogManager.writeLog(LogManager.productInfo, hashCode() + " VMapData GetData " + paramString + "-" + paramInt, 111);
    e locale = (e)this.vMapDataHs.get(getKey(paramString, paramInt));
    if (locale != null) {
      locale.d += 1;
    }
    return locale;
  }
  
  public synchronized e getCancelRecoder(String paramString, int paramInt)
  {
    LogManager.writeLog(LogManager.productInfo, hashCode() + " VMapData GetCancelData " + paramString + "-" + paramInt, 111);
    e locale = (e)this.vCancelMapDataHs.get(getKey(paramString, paramInt));
    if (locale != null) {
      if (System.currentTimeMillis() / 1000L - locale.b > 10L) {
        return null;
      }
    }
    return locale;
  }
  
  public synchronized e putRecoder(byte[] paramArrayOfByte, String paramString, int paramInt)
  {
    e locale = new e(paramString, paramInt);
    if (locale.a == null) {
      return null;
    }
    if (this.vMapDataHs.size() > 400)
    {
      this.vMapDataHs.remove(this.vMapDataList.get(0));
      this.vMapDataList.remove(0);
    }
    this.vMapDataHs.put(getKey(paramString, paramInt), locale);
    this.vMapDataList.add(getKey(paramString, paramInt));
    
    LogManager.writeLog(LogManager.productInfo, hashCode() + " VMapData putData " + locale.a + "-" + paramInt, 111);
    
    return locale;
  }
  
  public synchronized e putCancelRecoder(byte[] paramArrayOfByte, String paramString, int paramInt)
  {
    if (getRecoder(paramString, paramInt) != null) {
      return null;
    }
    e locale = new e(paramString, paramInt);
    if (locale.a == null) {
      return null;
    }
    if (this.vCancelMapDataHs.size() > 400)
    {
      this.vCancelMapDataHs.remove(this.vMapDataList.get(0));
      this.vCancelMapDataList.remove(0);
    }
    this.vCancelMapDataHs.put(getKey(paramString, paramInt), locale);
    this.vCancelMapDataList.add(getKey(paramString, paramInt));
    
    return locale;
  }
}
