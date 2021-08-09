package com.unistrong.api.mapcore;

import android.os.RemoteException;
import com.unistrong.api.maps.model.LatLng;

public abstract interface ICircleDelegate
  extends IOverlayDelegateDecode
{
  public abstract void setCenter(LatLng center)
    throws RemoteException;
  
  public abstract LatLng getCenter()
    throws RemoteException;
  
  public abstract void setRadius(double radius)
    throws RemoteException;
  
  public abstract double getRadius()
    throws RemoteException;
  
  public abstract void setStrokeWidth(float width)
    throws RemoteException;
  
  public abstract float getStrokeWidth()
    throws RemoteException;
  
  public abstract void setStrokeColor(int color)
    throws RemoteException;
  
  public abstract int getStrokeColor()
    throws RemoteException;
  
  public abstract void setFillColor(int color)
    throws RemoteException;
  
  public abstract int getFillColor()
    throws RemoteException;
  
  public abstract boolean contains(LatLng point)
    throws RemoteException;
}
