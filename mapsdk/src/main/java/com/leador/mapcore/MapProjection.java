package com.leador.mapcore;

public class MapProjection
{
  long native_instance = 0L;
  private boolean m_bNewInstance = false;
  private static int coodSystem =0;
  public MapProjection(MapCore paramMapCore)
  {
    this.native_instance = nativeNewInstance(paramMapCore.getInstanceHandle());
    this.m_bNewInstance = true;
  }
  
  public MapProjection(long instanceID,int coodSystem)
  {
    this.coodSystem = coodSystem;
    this.native_instance = instanceID;
    this.m_bNewInstance = false;
  }
  
  public void recycle()
  {
    if (this.m_bNewInstance) {
      nativeDestroy(this.native_instance);
    }
  }
  
  public long getInstanceHandle() //改为public ， 以便其他模块能够引用
  {
    return this.native_instance;
  }
  
  public void getBound(IPoint paramIPoint)
  {
    nativeGetBound(this.native_instance, paramIPoint);
  }
  
  public void setGeoCenter(int x, int y)
  {
    nativeSetGeoCenter(this.native_instance, x, y);
    //    Log.e("setGeoCenter","x:"+x+",y:"+y);

  }

  public void getGeoCenter(IPoint paramIPoint)
  {
    nativeGetGeoCenter(this.native_instance, paramIPoint);
  }
  
  public void setMapCenter(float paramFloat1, float paramFloat2)
  {
    nativeSetMapCenter(this.native_instance, paramFloat1, paramFloat2);
  }
  
  public void getMapCenter(FPoint paramFPoint)
  {
    nativeGetMapCenter(this.native_instance, paramFPoint);
  }
  
  public void setMapZoomer(float paramFloat)
  {
    nativeSetMapZoomer(this.native_instance, paramFloat);
  }
  
  public float getMapZoomer()
  {
    return nativeGetMapZoomer(this.native_instance);
  }
  
  public void setMapAngle(float paramFloat)
  {
    nativeSetMapAngle(this.native_instance, paramFloat);
  }
  
  public float getMapAngle()
  {
    return nativeGetMapAngle(this.native_instance);
  }
  
  public void setCameraHeaderAngle(float paramFloat)
  {
    nativeSetCameraHeaderAngle(this.native_instance, paramFloat);
  }
  
  public float getCameraHeaderAngle()
  {
    return nativeGetCameraHeaderAngle(this.native_instance);
  }
  
  public void geo2Map(int paramInt1, int paramInt2, FPoint paramFPoint)
  {
    nativeGeo2Map(this.native_instance, paramInt1, paramInt2, paramFPoint);
  }
  
  public void map2Win(float paramFloat1, float paramFloat2, IPoint paramIPoint)
  {
    nativeMap2Win(this.native_instance, paramFloat1, paramFloat2, paramIPoint);
  }
  
  public void win2Map(int xPixel, int yPixel, FPoint fPoint)
  {
    nativeWin2Map(this.native_instance, xPixel, yPixel, fPoint);
  }
  
  public float getMapLenWithWinbyY(int paramInt1, int paramInt2)
  {
    return nativeGetMapLenWithWinbyY(this.native_instance, paramInt1, paramInt2);
  }
  
  public float getMapLenWithWin(int paramInt)
  {
    return nativeGetMapLenWithWin(this.native_instance, paramInt);
  }
  
  public void map2Geo(float paramFloat1, float paramFloat2, IPoint paramIPoint)
  {
    nativeMap2Geo(this.native_instance, paramFloat1, paramFloat2, paramIPoint);
  }
  
  public void setCenterWithMap(float paramFloat1, float paramFloat2)
  {
    nativeSetCenterWithMap(this.native_instance, paramFloat1, paramFloat2);
  }
  
  public void getCenterMap(FPoint paramFPoint)
  {
    nativeGetCenterMap(this.native_instance, paramFPoint);
  }
  
  public static void lonlat2Geo(double longitude, double latitude, IPoint paramIPoint)
  {
    nativeLonLat2Geo(longitude, latitude,coodSystem, paramIPoint);
  }
  
  public static void geo2LonLat(int x, int y, DPoint paramDPoint)
  {
    nativeGeo2LonLat(x, y,coodSystem, paramDPoint);
  }
  
  public float getMapLenWithGeo(int paramInt)
  {
    return nativeGetMapLenWithGeo(this.native_instance, paramInt);
  }
  
  public void recalculate()
  {
    nativeRecalculate(this.native_instance);
  }
  
  private static native long nativeNewInstance(long paramLong);
  
  private static native void nativeDestroy(long paramLong);
  
  private static native void nativeGetBound(long paramLong, IPoint paramIPoint);
  
  private static native void nativeSetGeoCenter(long paramLong, int paramInt1, int paramInt2);
  
  private static native void nativeGetGeoCenter(long paramLong, IPoint paramIPoint);
  
  private static native void nativeSetMapCenter(long paramLong, float paramFloat1, float paramFloat2);
  
  private static native void nativeGetMapCenter(long paramLong, FPoint paramFPoint);
  
  private static native void nativeSetMapZoomer(long paramLong, float paramFloat);
  
  private static native float nativeGetMapZoomer(long paramLong);
  
  private static native void nativeSetMapAngle(long paramLong, float paramFloat);
  
  private static native float nativeGetMapAngle(long paramLong);
  
  private static native void nativeSetCameraHeaderAngle(long paramLong, float paramFloat);
  
  private static native float nativeGetCameraHeaderAngle(long paramLong);
  
  private static native void nativeGeo2Map(long paramLong, int paramInt1, int paramInt2, FPoint paramFPoint);
  
  private static native void nativeMap2Win(long paramLong, float paramFloat1, float paramFloat2, IPoint paramIPoint);
  
  private static native void nativeWin2Map(long paramLong, int paramInt1, int paramInt2, FPoint paramFPoint);
  
  private static native float nativeGetMapLenWithWinbyY(long paramLong, int paramInt1, int paramInt2);
  
  private static native float nativeGetMapLenWithWin(long paramLong, int paramInt);
  
  private static native void nativeMap2Geo(long paramLong, float paramFloat1, float paramFloat2, IPoint paramIPoint);
  
  private static native void nativeSetCenterWithMap(long paramLong, float paramFloat1, float paramFloat2);
  
  private static native void nativeGetCenterMap(long paramLong, FPoint paramFPoint);
  
  private static native void nativeLonLat2Geo(double paramDouble1, double paramDouble2, int projType, IPoint paramIPoint);
  
  private static native void nativeGeo2LonLat(int paramInt1, int paramInt2, int projType, DPoint paramDPoint);
  
  private static native float nativeGetMapLenWithGeo(long paramLong, int paramInt);
  
  private static native void nativeRecalculate(long paramLong);
}
