package com.leador.mapcore;

import java.util.Hashtable;
import java.util.Iterator;

public class MapCoreManager
{
  private Hashtable<Integer, MapCore> mapCoreTable = new Hashtable();
  static MapCoreManager mapCoreManager;
  
  public static MapCoreManager getInstance()
  {
    if (mapCoreManager == null) {
      mapCoreManager = new MapCoreManager();
    }
    return mapCoreManager;
  }
  
  public void putMapCore(int paramInt, MapCore paramMapCore)
  {
    this.mapCoreTable.put(Integer.valueOf(paramInt), paramMapCore);
  }
  
  public MapCore getMapCore(int paramInt)
  {
    return (MapCore)this.mapCoreTable.get(Integer.valueOf(paramInt));
  }
  
  public void removeMapCore(int paramInt)
  {
    this.mapCoreTable.remove(Integer.valueOf(paramInt));
  }
  
  public int getMapCoreSize()
  {
    return this.mapCoreTable.size();
  }
  
  private void OnMapDataRequired(int paramInt1, String[] paramArrayOfString, int paramInt2)
  {
    Object localObject;
    if (paramInt2 != 0)
    {
      localObject = getMapCore(paramInt2);
      if (localObject != null) {
        ((MapCore)localObject).OnMapDataRequired(paramInt1, paramArrayOfString);
      }
    }
    else
    {
      for (localObject = this.mapCoreTable.keySet().iterator(); ((Iterator)localObject).hasNext();)
      {
        Integer localInteger = (Integer)((Iterator)localObject).next();
        MapCore localMapCore = (MapCore)this.mapCoreTable.get(localInteger);
        if (localMapCore != null) {
          localMapCore.OnMapDataRequired(paramInt1, paramArrayOfString);
        }
      }
    }
  }
  
  private void OnMapLabelsRequired(int[] paramArrayOfInt, int paramInt1, int paramInt2)
  {
    Object localObject;
    if (paramInt2 != 0)
    {
      localObject = getMapCore(paramInt2);
      if (localObject != null) {
        ((MapCore)localObject).OnMapLabelsRequired(paramArrayOfInt, paramInt1);
      }
    }
  }
  
//  private void onIndoorBuildingActivity(byte[] paramArrayOfByte, int paramInt)
//  {
//    if (paramInt != 0)
//    {
//      MapCore localMapCore = getMapCore(paramInt);
//      if (localMapCore != null) {
//        localMapCore.onIndoorBuildingActivity(paramArrayOfByte);
//      }
//    }
//  }
//
//  private void onIndoorDataRequired(int paramInt1, String[] paramArrayOfString, int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt2)
//  {
//    if (paramInt2 != 0)
//    {
//      MapCore localMapCore = getMapCore(paramInt2);
//      if (localMapCore != null) {
//        localMapCore.onIndoorDataRequired(paramInt1, paramArrayOfString, paramArrayOfInt1, paramArrayOfInt2);
//      }
//    }
//  }
  
  static
  {
    try
    {
//      System.loadLibrary("gdinamapv4sdk752");
//      System.loadLibrary("gdinamapv4sdk752ex");
    }
    catch (Exception localException) {}
  }
}
