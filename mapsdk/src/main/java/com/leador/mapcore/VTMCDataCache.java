package com.leador.mapcore;

import java.util.ArrayList;
import java.util.Hashtable;

public class VTMCDataCache
{
  public static final int MAXSIZE = 500;
  public static final int MAX_EXPIREDTIME = 300;
  static Hashtable<String, CacheData> vtmcHs = new Hashtable();
  static ArrayList<String> vtmcList = new ArrayList();
  private static VTMCDataCache instance;
  public int mNewestTimeStamp = 0;
  
  public static VTMCDataCache getInstance()
  {
    if (instance == null) {
      instance = new VTMCDataCache();
    }
    return instance;
  }
  
  public void reset()
  {
    vtmcList.clear();
    vtmcHs.clear();
  }
  
  public int getSize()
  {
    return vtmcList.size();
  }
  
  private void deleteData(String paramString)
  {
    vtmcHs.remove(paramString);
    for (int i = 0; i < vtmcList.size(); i++) {
      if (((String)vtmcList.get(i)).equals(paramString))
      {
        vtmcList.remove(i);
        
        break;
      }
    }
  }
  
  public synchronized CacheData getData(String paramString, boolean paramBoolean)
  {
    CacheData localf = (CacheData)vtmcHs.get(paramString);
    if (paramBoolean) {
      return localf;
    }
    if (localf != null)
    {
      if ((int)(System.currentTimeMillis() / 1000L) - localf.createTime > MAX_EXPIREDTIME) {
        return null;
      }
      if (this.mNewestTimeStamp > localf.e) {
        return null;
      }
      return localf;
    }
    return null;
  }
  
  public synchronized CacheData putData(byte[] paramArrayOfByte)
  {
    CacheData localf1 = new CacheData(paramArrayOfByte);
    if (this.mNewestTimeStamp < localf1.e) {
      this.mNewestTimeStamp = localf1.e;
    }
    CacheData localf2 = (CacheData)vtmcHs.get(localf1.b);
    if (localf2 != null)
    {
      if (localf2.d.equals(localf1.d))
      {
        localf2.a(this.mNewestTimeStamp);
        return localf2;
      }
      deleteData(localf1.b);
    }
    if (vtmcList.size() > MAXSIZE)
    {
      vtmcHs.remove(vtmcList.get(0));
      vtmcList.remove(0);
    }
    vtmcHs.put(localf1.b, localf1);
    vtmcList.add(localf1.b);
    
    return localf1;
  }
}
