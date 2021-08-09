package com.unistrong.api.mapcore;

import android.os.RemoteException;
import com.unistrong.api.maps.model.LatLng;

import java.util.List;

public abstract interface IPolygonDelegate
  extends IOverlayDelegateDecode
{
  public abstract void setStrokeWidth(float paramFloat)
    throws RemoteException;
  
  public abstract float getStrokeWidth()
    throws RemoteException;
  
  public abstract void setFillColor(int paramInt)
    throws RemoteException;
  
  public abstract int getFillColor()
    throws RemoteException;
  
  public abstract void setStrokeColor(int paramInt)
    throws RemoteException;
  
  public abstract void a(List<LatLng> paramList)
    throws RemoteException;
  
  public abstract List<LatLng> getPoints()
    throws RemoteException;
  
  public abstract int getStrokeColor()
    throws RemoteException;
  
  public abstract boolean contains(LatLng paramLatLng)
    throws RemoteException;
}
