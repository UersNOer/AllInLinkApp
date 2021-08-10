package com.unistrong.api.mapcore;

import android.os.RemoteException;
import com.unistrong.api.maps.model.BitmapDescriptor;
import com.unistrong.api.maps.model.LatLng;
import com.unistrong.api.maps.model.LatLngBounds;

public abstract interface IGroundOverlayDelegateDecode
  extends IOverlayDelegateDecode
{
  public abstract void setPosition(LatLng paramLatLng)
    throws RemoteException;
  
  public abstract LatLng getPosition()
    throws RemoteException;
  
  public abstract void setDimensions(float paramFloat)
    throws RemoteException;
  
  public abstract void setDimensions(float paramFloat1, float paramFloat2)
    throws RemoteException;
  
  public abstract float getWidth()
    throws RemoteException;
  
  public abstract float getHeight()
    throws RemoteException;
  
  public abstract void setPositionFromBounds(LatLngBounds paramLatLngBounds)
    throws RemoteException;
  
  public abstract LatLngBounds getBounds()
    throws RemoteException;
  
  public abstract void setBearing(float paramFloat)
    throws RemoteException;
  
  public abstract float getBearing()
    throws RemoteException;
  
  public abstract void setTransparency(float paramFloat)
    throws RemoteException;
  
  public abstract float getTransparency()
    throws RemoteException;
  
  public abstract void setImage(BitmapDescriptor paramBitmapDescriptor)
    throws RemoteException;
}
