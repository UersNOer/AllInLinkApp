package com.leador.mapcore;

public class MapSourceGridData
{
  public String gridName;
  public int sourceType;
  public Object obj = null;
  public String keyGridName;
  public int mIndoorIndex;
  public int mIndoorVersion;
  
  public MapSourceGridData(String gridName, int sourceType)
  {
    this.gridName = gridName;
    this.sourceType = sourceType;
    this.keyGridName = (sourceType + "-" + this.gridName);
  }
  
  public MapSourceGridData(String gridName, int sourceType, int mIndoorIndex, int mIndoorVersion)
  {
    this.gridName = gridName;
    this.mIndoorIndex = mIndoorIndex;
    this.mIndoorVersion = mIndoorVersion;
    this.sourceType = sourceType;
    this.keyGridName = (sourceType + "-" + this.gridName + "-" + mIndoorIndex);
  }
  
  public String getKeyGridName()
  {
    return this.keyGridName;
  }
  
  public int getSourceType()
  {
    return this.sourceType;
  }
  
  public String getGridName()
  {
    return this.gridName;
  }
}
