package com.unistrong.api.mapcore;

import android.graphics.Point;
import com.unistrong.api.maps.model.CameraPosition;
import com.unistrong.api.maps.model.LatLng;
import com.unistrong.api.maps.model.LatLngBounds;
import com.leador.mapcore.IPoint;

public class CameraUpdateFactoryDelegate //o
{
  Type nowType = Type.none; //a
  float xPixel; //b
  float yPixel; //c 
  float zoom; // d
  float amount; //e
  float tilt; //f
  float bearing;//g
  CameraPosition h;//h
  LatLngBounds bounds; //i
  int padding;//j
  int width;//k
  int height;//l
  Point focus = null;//m
  boolean n = false; //n
  IPoint geoPoint; //o
  boolean isChangeFinished = false; //p
  
  public static CameraUpdateFactoryDelegate newInstance() //a
  {
    CameraUpdateFactoryDelegate localo = new CameraUpdateFactoryDelegate();
    return localo;
  }
  
  public static CameraUpdateFactoryDelegate zoomIn() //b
  {
    CameraUpdateFactoryDelegate localo = newInstance();
    localo.nowType = Type.zoomIn;
    return localo;
  }
  
  public static CameraUpdateFactoryDelegate zoomOut() //c
  {
    CameraUpdateFactoryDelegate localo = newInstance();
    localo.nowType = Type.zoomOut;
    return localo;
  }
  
  public static CameraUpdateFactoryDelegate scrollBy(float paramFloat1, float paramFloat2) //a
  {
    CameraUpdateFactoryDelegate localo = newInstance();
    localo.nowType = Type.scrollBy;
    localo.xPixel = paramFloat1;
    localo.yPixel = paramFloat2;
    return localo;
  }
  
  public static CameraUpdateFactoryDelegate zoomTo(float paramFloat) //a
  {
    CameraUpdateFactoryDelegate localo = newInstance();
    localo.nowType = Type.zoomTo;
    localo.zoom = paramFloat;
    return localo;
  }
  
  public static CameraUpdateFactoryDelegate b(float paramFloat) //b
  {
    //return a(paramFloat, null);
    return zoomBy(paramFloat, (Point) null); //上一句翻译
  }
  
  public static CameraUpdateFactoryDelegate zoomBy(float amount, Point focus) // a
  {
    CameraUpdateFactoryDelegate localo = newInstance();
    localo.nowType = Type.zoomBy;
    localo.amount = amount;
    localo.focus = focus;
    return localo;
  }
  
  public static CameraUpdateFactoryDelegate newCameraPosition(CameraPosition paramCameraPosition) // a
  {
    CameraUpdateFactoryDelegate localo = newInstance();
    localo.nowType = Type.newCameraPosition;
    localo.h = paramCameraPosition;
    return localo;
  }
  
  public static CameraUpdateFactoryDelegate changeGeoCenter(IPoint paramIPoint)// a
  {
    CameraUpdateFactoryDelegate localo = newInstance();
    localo.nowType = Type.changeCenter;
    localo.geoPoint = paramIPoint;
    return localo;
  }
  
  public static CameraUpdateFactoryDelegate changeTilt(float paramFloat)//c
  {
    CameraUpdateFactoryDelegate localo = newInstance();
    localo.nowType = Type.changeTilt;
    localo.tilt = paramFloat;
    return localo;
  }
  
  public static CameraUpdateFactoryDelegate changeBearing(float paramFloat)//c
  {
    CameraUpdateFactoryDelegate localo = newInstance();
    localo.nowType = Type.changeBearing;
    localo.bearing = paramFloat;
    return localo;
  }
  
  public static CameraUpdateFactoryDelegate changeBearingGeoCenter(float paramFloat, IPoint paramIPoint) //a
  {
    CameraUpdateFactoryDelegate localo = newInstance();
    localo.nowType = Type.changeBearingGeoCenter;
    localo.bearing = paramFloat;
    localo.geoPoint = paramIPoint;
    return localo;
  }

  public static CameraUpdateFactoryDelegate a(LatLng paramLatLng) //a
  {
    return newCameraPosition(CameraPosition.builder().target(paramLatLng)
      .build());
  }
  
  public static CameraUpdateFactoryDelegate a(LatLng paramLatLng, float zoom) //a
  {
    return newCameraPosition(CameraPosition.builder().target(paramLatLng)
      .zoom(zoom).build());
  }
  
  public static CameraUpdateFactoryDelegate a(LatLng paramLatLng, float zoom, float bearing, float tilt) //a
  {
    return newCameraPosition(CameraPosition.builder().target(paramLatLng)
      .zoom(zoom).bearing(bearing).tilt(tilt).build());
  }
  
  static CameraUpdateFactoryDelegate changeGeoCenterZoomTiltBearing(IPoint geoPoint, float zoom, float bearing, float tilt) //a
  {
    CameraUpdateFactoryDelegate cameraUpdate = newInstance();
    cameraUpdate.nowType = Type.changeGeoCenterZoomTiltBearing;
    cameraUpdate.geoPoint = geoPoint;
    cameraUpdate.zoom = zoom;
    cameraUpdate.bearing = bearing;
    cameraUpdate.tilt = tilt;
    return cameraUpdate;
  }
  
  public static CameraUpdateFactoryDelegate newLatLngBounds(LatLngBounds paramLatLngBounds, int paramInt) //a
  {
    CameraUpdateFactoryDelegate localo = newInstance();
    localo.nowType = Type.newLatLngBounds;
    localo.bounds = paramLatLngBounds;
    localo.padding = paramInt;
    return localo;
  }
  
  public static CameraUpdateFactoryDelegate newLatLngBoundsWithSize(LatLngBounds paramLatLngBounds, int paramInt1, int paramInt2, int paramInt3) //a
  {
    CameraUpdateFactoryDelegate localo = newInstance();
    localo.nowType = Type.newLatLngBoundsWithSize;
    localo.bounds = paramLatLngBounds;
    localo.padding = paramInt3;
    localo.width = paramInt1;
    localo.height = paramInt2;
    return localo;
  }
  
  static enum Type
  {
    none,zoomIn,
    /**
     * 只修改中心点 geoPoint有效
     */
    changeCenter,
    /**
     * 只调整仰角
     */
    changeTilt,
    /**
     * 只旋转
     */
    changeBearing,
    changeBearingGeoCenter,
    /**
     * 改变中心点和级别
     */
    changeGeoCenterZoom,zoomOut,zoomTo,zoomBy,scrollBy,
    newCameraPosition,newLatLngBounds,newLatLngBoundsWithSize,changeGeoCenterZoomTiltBearing;
  }
}
