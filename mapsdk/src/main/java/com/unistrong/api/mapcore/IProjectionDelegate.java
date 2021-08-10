package com.unistrong.api.mapcore;

import android.graphics.Point;
import android.graphics.PointF;
import android.os.RemoteException;
import com.unistrong.api.maps.model.LatLng;
import com.unistrong.api.maps.model.LatLngBounds;
import com.unistrong.api.maps.model.TileProjection;
import com.unistrong.api.maps.model.VisibleRegion;

public abstract interface IProjectionDelegate
{
  public abstract LatLng fromScreenLocation(Point point) // a
    throws RemoteException;
  
  public abstract Point toScreenLocation(LatLng latLng) // a
    throws RemoteException;
  
  public abstract PointF toMapLocation(LatLng latLng) //b
    throws RemoteException;
  
  public abstract VisibleRegion getVisibleRegion() // a
    throws RemoteException;
  
  public abstract float toOpenGLWidth(int paramInt) // a
    throws RemoteException;
  
  public abstract TileProjection fromBoundsToTile(LatLngBounds latLngBounds, int paramInt1, int paramInt2) // a
    throws RemoteException;
  
  public abstract LatLngBounds getMapBounds(LatLng latLng, float paramFloat) // a
    throws RemoteException;
}
