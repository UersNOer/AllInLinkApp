package com.unistrong.api.mapcore;

import android.os.RemoteException;

public abstract interface IArcDelegateDecode
  extends IOverlayDelegateDecode
{
  public abstract void setStrokeWidth(float paramFloat)
    throws RemoteException;
  
  public abstract float getStrokeWidth()
    throws RemoteException;
  
  public abstract void setStrokeColor(int color)
    throws RemoteException;
  
  public abstract int getStrokeColor()
    throws RemoteException;
}
