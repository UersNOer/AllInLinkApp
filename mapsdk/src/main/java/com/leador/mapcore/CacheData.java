package com.leador.mapcore;

class CacheData
{
  byte[] mapData;
  String b;
  int createTime;
  String d;
  int e;
  
  public CacheData(byte[] mapData)
  {
    try
    {
      this.createTime = ((int)(System.currentTimeMillis() / 1000L));
      int index = 0;
      
      index += 4;
      int j = mapData[(index++)];
      this.b = new String(mapData, index, j);
      index += j;
      
      int k = mapData[(index++)];
      this.d = new String(mapData, index, k);
      index += k;
      
      this.e = Convert.getInt(mapData, index);
      index += 4;
      this.mapData = mapData;
    }
    catch (Exception localException)
    {
      this.mapData = null;
    }
  }
  
  public void a(int paramInt)
  {
    if (this.mapData == null) {
      return;
    }
    this.createTime = ((int)(System.currentTimeMillis() / 1000L));
    
    int i = 4;
    
    int j = this.mapData[(i++)];
    i += j;
    int k = this.mapData[(i++)];
    i += k;
    Convert.writeInt(this.mapData, i, paramInt);
    this.e = paramInt;
  }
}
