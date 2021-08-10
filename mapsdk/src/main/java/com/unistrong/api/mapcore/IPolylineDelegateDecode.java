package com.unistrong.api.mapcore;

import android.os.RemoteException;
import com.unistrong.api.maps.model.LatLng;

import java.util.List;

public abstract interface IPolylineDelegateDecode
  extends IOverlayDelegateDecode
{
  public abstract void setWidth(float width)
    throws RemoteException;
  
  public abstract float getWidth()
    throws RemoteException;
  
  public abstract void setColor(int color)
    throws RemoteException;
  
  public abstract int getColor()
    throws RemoteException;
  public void addPoint(LatLng point)throws RemoteException;

  public abstract void setPoints(List<LatLng> points)
    throws RemoteException;
  
  public abstract List<LatLng> getPoints()
    throws RemoteException;
  
  public abstract boolean isGeodesic();
  
  public abstract void geodesic(boolean paramBoolean)
    throws RemoteException;
  
  public abstract void setDottedLine(boolean isDottedLine);
  
  public abstract boolean isDottedLine();
  
  public abstract LatLng getNearestLatLng(LatLng paramLatLng);
  
  public abstract boolean b(LatLng paramLatLng);
  
  public abstract void setTransparency(float paramFloat);
}
