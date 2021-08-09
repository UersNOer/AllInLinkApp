package com.unistrong.api.mapcore;

import android.os.RemoteException;

public abstract interface IUiSettingsDelegate//ap
{
  public abstract void setScaleControlsEnabled(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setZoomControlsEnabled(boolean paramBoolean) //b
    throws RemoteException;
  
  public abstract void setCompassEnabled(boolean paramBoolean) //c
    throws RemoteException;
  

  public abstract void setScrollGesturesEnabled(boolean paramBoolean) //e
    throws RemoteException;
  
  public abstract void setZoomGesturesEnabled(boolean paramBoolean) //f
    throws RemoteException;
  
  public abstract void setTiltGesturesEnabled(boolean paramBoolean) //g
    throws RemoteException;
  
  public abstract void setRotateGesturesEnabled(boolean paramBoolean) //h
    throws RemoteException;
  
  public abstract void setAllGesturesEnabled(boolean paramBoolean) //i
    throws RemoteException;
  
  public abstract void setLogoPosition(int paramInt) // a
    throws RemoteException;
  
  public abstract void setZoomPosition(int paramInt) //b
    throws RemoteException;
  
  public abstract boolean isScaleControlsEnabled() // a
    throws RemoteException;
  
  public abstract boolean isZoomControlsEnabled() //b
    throws RemoteException;
  
  public abstract boolean isCompassEnabled() //c
    throws RemoteException;
  
  public abstract boolean isMyLocationButtonEnabled() // d
    throws RemoteException;
  
  public abstract boolean isScrollGesturesEnabled() //e
    throws RemoteException;
  
  public abstract boolean isZoomGesturesEnabled() //f
    throws RemoteException;
  
  public abstract boolean isTiltGesturesEnabled() //g
    throws RemoteException;
  
  public abstract boolean isRotateGesturesEnabled() //h
    throws RemoteException;
  
  public abstract int getLogoPosition() //i
    throws RemoteException;
  
  public abstract int getZoomPosition() //j
          throws RemoteException;
  public abstract void setCompassViewPosition(int xPix,int yPix)throws RemoteException;

}
