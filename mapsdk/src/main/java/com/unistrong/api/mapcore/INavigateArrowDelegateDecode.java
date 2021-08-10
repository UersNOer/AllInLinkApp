package com.unistrong.api.mapcore;

import android.os.RemoteException;
import com.unistrong.api.maps.model.LatLng;

import java.util.List;

public abstract interface INavigateArrowDelegateDecode
  extends IOverlayDelegateDecode
{
  public abstract void setWidth(float paramFloat)
    throws RemoteException;
  
  public abstract float getWidth()
    throws RemoteException;
  
  public abstract void setTopColor(int paramInt)
    throws RemoteException;
  
  public abstract void setSideColor(int paramInt)
    throws RemoteException;
  
  public abstract int getTopColor()
    throws RemoteException;
  
  public abstract int getSideColor()
    throws RemoteException;
  
  public abstract void setPoints(List<LatLng> paramList)
    throws RemoteException;
  
  public abstract List<LatLng> getPoints()
    throws RemoteException;
}
