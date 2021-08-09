package com.unistrong.api.mapcore;

class MapMessageDecode
{
	/**
	 * 标记级别调整
	 */
	public static final int MAPZOOMER = 0;
	public static final int STISET = 1;
	public static final int TRAFFICSET = 2;
	public static final int SCREENCENTRESET = 3;
	public static final int MAPANGLESET = 4;
	public static final int MAPCAMANGLESET = 5;
	public static final int MAPRESET = 6;
	public static final int SCREENTOUCH = 7;
	
  int id;
  boolean enabled;
  
  public boolean getEnabled() // a
  {
    return this.enabled;
  }
  
  public MapMessageDecode setEnabled(boolean paramBoolean) // a
  {
    this.enabled = paramBoolean;
    return this;
  }
  
  public MapMessageDecode(int id)
  {
    this.id = id;
  }
}
