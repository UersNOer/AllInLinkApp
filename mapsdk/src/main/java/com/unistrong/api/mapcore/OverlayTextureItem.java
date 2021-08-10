package com.unistrong.api.mapcore;

import com.unistrong.api.maps.model.BitmapDescriptor;

public class OverlayTextureItem
{
  private BitmapDescriptor bitmapDescriptor;
  private int textureid;
  
  public OverlayTextureItem(BitmapDescriptor paramBitmapDescriptor, int paramInt)
  {
    this.bitmapDescriptor = paramBitmapDescriptor;
    this.textureid = paramInt;
  }
  
  public BitmapDescriptor getBitmapDes()
  {
    return this.bitmapDescriptor;
  }
  
  public int getTextureId()
  {
    return this.textureid;
  }
}
