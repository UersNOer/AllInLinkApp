package com.unistrong.api.mapcore;

import android.graphics.Point;
import android.graphics.PointF;
import android.os.RemoteException;
import com.unistrong.api.mapcore.util.Util;
import com.unistrong.api.maps.model.LatLng;
import com.unistrong.api.maps.model.LatLngBounds;
import com.unistrong.api.maps.model.TileProjection;
import com.unistrong.api.maps.model.VisibleRegion;
import com.leador.mapcore.DPoint;
import com.leador.mapcore.FPoint;
import com.leador.mapcore.IPoint;

class ProjectionDelegateImpDecode
  implements IProjectionDelegate
{
  private IMapDelegate mapDelegate; // a
  
  public ProjectionDelegateImpDecode(IMapDelegate mapDelegate)
  {
    this.mapDelegate = mapDelegate;
  }
  
  public LatLng fromScreenLocation(Point point)
    throws RemoteException
  {
    if (point == null) {
      return null;
    }
    DPoint localDPoint = new DPoint();
    this.mapDelegate.getPixel2LatLng(point.x, point.y, localDPoint);
    return new LatLng(localDPoint.y, localDPoint.x);
  }
  
  public Point toScreenLocation(LatLng latLng)
    throws RemoteException
  {
    if (latLng == null) {
      return null;
    }
    IPoint localIPoint = new IPoint();
    this.mapDelegate.getLatLng2Pixel(latLng.latitude, latLng.longitude, localIPoint);
    
    return new Point(localIPoint.x, localIPoint.y);
  }
  
  public VisibleRegion getVisibleRegion()
    throws RemoteException
  {
    int width = this.mapDelegate.getMapWidth();
    int height = this.mapDelegate.getMapHeight();
    LatLng farLeft = fromScreenLocation(new Point(0, 0));
    LatLng farRight = fromScreenLocation(new Point(width, 0));
    LatLng nearLeft = fromScreenLocation(new Point(0, height));
    LatLng nearRight = fromScreenLocation(new Point(width, height));
    
    LatLngBounds bounds = LatLngBounds.builder().include(nearLeft).include(nearRight).include(farLeft).include(farRight).build();
    return new VisibleRegion(nearLeft, nearRight, farLeft, farRight, bounds);
  }
  
  public PointF toMapLocation(LatLng latLng)
    throws RemoteException
  {
    if (latLng == null) {
      return null;
    }
    FPoint localFPoint = new FPoint();
    this.mapDelegate.getLatLng2Map(latLng.latitude, latLng.longitude, localFPoint);
    
    return new PointF(localFPoint.x, localFPoint.y);
  }
  
  public float toOpenGLWidth(int paramInt)
  {
    if (paramInt <= 0) {
      return 0.0F;
    }
    return this.mapDelegate.getMapLenWithWin(paramInt);
  }
  
  public TileProjection fromBoundsToTile(LatLngBounds latLngBounds, int paramInt1, int paramInt2)
    throws RemoteException
  {
    if ((latLngBounds == null) || (paramInt1 < 0) || (paramInt1 > 20) || (paramInt2 <= 0)) {
      return null;
    }
    IPoint localIPoint1 = new IPoint();
    IPoint localIPoint2 = new IPoint();
    this.mapDelegate.latlon2Geo(latLngBounds.southwest.latitude, latLngBounds.southwest.longitude, localIPoint1);
    this.mapDelegate.latlon2Geo(latLngBounds.northeast.latitude, latLngBounds.northeast.longitude, localIPoint2);
    
    int i = (localIPoint1.x >> 20 - paramInt1) / paramInt2;
    int j = (localIPoint1.y >> 20 - paramInt1) / paramInt2;
    int k = (localIPoint2.x >> 20 - paramInt1) / paramInt2;
    int m = (localIPoint2.y >> 20 - paramInt1) / paramInt2;
    
    int n = (i << 20 - paramInt1) * paramInt2;
    int i1 = (m << 20 - paramInt1) * paramInt2;
    int i2 = localIPoint1.x - n >> 20 - paramInt1;
    int i3 = localIPoint2.y - i1 >> 20 - paramInt1;
    
    return new TileProjection(i2, i3, i, k, m, j);
  }
  
  public LatLngBounds getMapBounds(LatLng latLng, float paramFloat)
    throws RemoteException
  {
    if ((this.mapDelegate == null) || (latLng == null)) {
      return null;
    }
    return this.mapDelegate.a(latLng, Util.checkZoomLevel(paramFloat));
  }
}
