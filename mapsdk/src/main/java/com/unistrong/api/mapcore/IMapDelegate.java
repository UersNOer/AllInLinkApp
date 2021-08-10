package com.unistrong.api.mapcore;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.location.Location;
import android.os.RemoteException;
import android.view.MotionEvent;
import android.view.View;

import com.unistrong.api.maps.CustomRenderer;
import com.unistrong.api.maps.MapController;
import com.unistrong.api.maps.model.ArcOptions;
import com.unistrong.api.maps.model.CameraPosition;
import com.unistrong.api.maps.model.CircleOptions;
import com.unistrong.api.maps.model.GeoJsonLayerOptions;
import com.unistrong.api.maps.model.GroundOverlayOptions;
import com.unistrong.api.maps.model.LatLng;
import com.unistrong.api.maps.model.LatLngBounds;
import com.unistrong.api.maps.model.Marker;
import com.unistrong.api.maps.model.MarkerOptions;
import com.unistrong.api.maps.model.MyLocationStyle;
import com.unistrong.api.maps.model.NavigateArrowOptions;
import com.unistrong.api.maps.model.PolygonOptions;
import com.unistrong.api.maps.model.PolylineOptions;
import com.unistrong.api.maps.model.Text;
import com.unistrong.api.maps.model.TextOptions;
import com.unistrong.api.maps.model.TileOverlay;
import com.unistrong.api.maps.model.TileOverlayOptions;
import com.leador.mapcore.DPoint;
import com.leador.mapcore.FPoint;
import com.leador.mapcore.IPoint;
import com.leador.mapcore.MapProjection;

import java.util.ArrayList;
import java.util.List;

public abstract interface IMapDelegate
{
  public abstract void addCustomRenderer(CustomRenderer renderer) throws RemoteException;

  public abstract void removeCustomRenderer(CustomRenderer renderer) throws RemoteException;

  public abstract void clearCustomRenderer() throws RemoteException;

  public abstract void setCustomRenderer(CustomRenderer renderer) throws RemoteException;

  public void drawCustomRenderTime(int drawTime);

//  public abstract void removeOverlay(GLOverlayDecode paramu); // a
  
  public abstract MapProjection getMapProjection(); //c
  
  public abstract int getMapWidth();
  
  public abstract int getMapHeight();
  
  public abstract int getLineTextureID(); //b
  
  public abstract int getDottedLineTextureID(); //o
  
  public abstract View getView() //C
    throws RemoteException;
  
  public abstract Rect getRect();
  public abstract void set2DMapRotateEnable(boolean isEnable);
  
  public abstract void setZOrderOnTop_(boolean paramBoolean) //f
    throws RemoteException;
  
  public abstract void showMyLocationOverlay(Location paramLocation) // a
    throws RemoteException;
  /**
   * 删除绘制的线面
   *
   * @param id
   * @return
   */
  public abstract boolean removeGLOverlay(String id) // a
    throws RemoteException;
  
  public abstract void M(); //M
  
  public abstract void latlon2Geo(double paramDouble1, double paramDouble2, IPoint paramIPoint); // a
  
  public abstract void geo2Map(int paramInt1, int paramInt2, FPoint paramFPoint); //b

  public abstract void geo2Latlng(int paramInt1, int paramInt2, DPoint paramDPoint); //b
  
  public abstract float getZoomLevel(); //E
  
  public abstract int getTexsureId(); //K
  
  public abstract void deleteTexsureId(int paramInt); //f
  
  public abstract CameraPosition getCameraPosition() //q
    throws RemoteException;

  public abstract void setMaxZoomLevel(float maxZoomLevel);

  public abstract float getMaxZoomLevel(); //r
  
  public abstract float getMinZoomLevel(); //s
  
  public abstract void moveCamera(CameraUpdateFactoryDelegate paramo) // a
    throws RemoteException;
  
  public abstract void animateCamera(CameraUpdateFactoryDelegate paramo) //b
    throws RemoteException;
  
  public abstract void animateCamera(CameraUpdateFactoryDelegate paramo, MapController.CancelableCallback paramCancelableCallback) // a
    throws RemoteException;
  
  public abstract void animateCameraWithDurationAndCallback(CameraUpdateFactoryDelegate paramo, long paramLong, MapController.CancelableCallback paramCancelableCallback) // a
    throws RemoteException;
  
  public abstract void stopAnimation() //t
    throws RemoteException;
  
  public abstract IPolylineDelegateDecode addPolyline(PolylineOptions paramPolylineOptions) // a
    throws RemoteException;
  
  public abstract ICircleDelegate addCircle(CircleOptions paramCircleOptions) // a
    throws RemoteException;
  
  public abstract IPolygonDelegate addPolygon(PolygonOptions paramPolygonOptions) // a
    throws RemoteException;
  
  public abstract IGroundOverlayDelegateDecode addGroundOverlay(GroundOverlayOptions paramGroundOverlayOptions) // a
    throws RemoteException;
  
  public abstract Marker addMarker(MarkerOptions paramMarkerOptions) // a
    throws RemoteException;
  
  public abstract Text addText(TextOptions paramTextOptions) // a
    throws RemoteException;
  
  public abstract TileOverlay addTileOverlay(TileOverlayOptions tileOverlayOptions) // a
    throws RemoteException;
  
  public abstract void clear() //u
    throws RemoteException;
  
  public abstract void clear(boolean paramBoolean) //g
    throws RemoteException;
  
  public abstract int getMapType() //v
    throws RemoteException;
  

  public abstract boolean isTrafficEnabled() //w
    throws RemoteException;
  

//  public abstract void j(boolean paramBoolean) //j
//    throws RemoteException;
  
  public abstract boolean isMyLocationEnabled() 
    throws RemoteException;
  

  public abstract void setLoadOfflineData(boolean paramBoolean) //n
    throws RemoteException;
  
  public abstract void setMyLocationStyle(MyLocationStyle paramMyLocationStyle) // a
    throws RemoteException;
  

    public abstract int getMyLocationType()
            throws RemoteException;


    public abstract boolean isLocationRotateEnabled()
            throws RemoteException;
  
  public abstract Location getMyLocation()
    throws RemoteException;
  
  public abstract IUiSettingsDelegate getUiSettings()
    throws RemoteException;
  
  public abstract IProjectionDelegate getProjection() //A
    throws RemoteException;
  
  public abstract void setOnCameraChangeListener(MapController.OnCameraChangeListener paramOnCameraChangeListener) // a
    throws RemoteException;
  public abstract void setOnMapLevelChangeListener(MapController.OnMapLevelChangeListener paramOnCameraChangeListener) // a
    throws RemoteException;
  
  public abstract void setOnMapClickListener(MapController.OnMapClickListener paramOnMapClickListener)  // a
    throws RemoteException;
  
  public abstract void setOnMapTouchListener(MapController.OnMapTouchListener paramOnMapTouchListener) // a
    throws RemoteException;
  
  public abstract void setOnMapLongClickListener(MapController.OnMapLongClickListener paramOnMapLongClickListener) // a
    throws RemoteException;
  
  public abstract void setOnMarkerClickListener(MapController.OnMarkerClickListener paramOnMarkerClickListener) // a
    throws RemoteException;
  
  public abstract void setOnPolylineClickListener(MapController.OnPolylineClickListener paramOnPolylineClickListener) // a
    throws RemoteException; 
  
  public abstract void setOnMarkerDragListener(MapController.OnMarkerDragListener paramOnMarkerDragListener) // a
    throws RemoteException;
  
  public abstract void setOnMapLoadedListener(MapController.OnMapLoadedListener paramOnMapLoadedListener) // a
    throws RemoteException;
  
  public abstract void setOnInfoWindowClickListener(MapController.OnInfoWindowClickListener paramOnInfoWindowClickListener) // a
    throws RemoteException;
  
  public abstract void setInfoWindowAdapter(MapController.InfoWindowAdapter paramInfoWindowAdapter) // a
    throws RemoteException;
  
  public abstract void showInfoWindow(IMarkerDelegate markerDelegate) // a
    throws RemoteException;
  
  public abstract void hiddenInfoWindowShown();  //D
  
  public abstract void getLatLng2Map(double x, double y, FPoint fPoint); // a
  
  public abstract void getPixel2LatLng(int xpixe, int ypixe, DPoint dPoint); // a
  
  public abstract void getLatLng2Pixel(double x, double y, IPoint iPoint);  //b
  
  public abstract void getPixel2Geo(int paramInt1, int paramInt2, IPoint paramIPoint); // a
  
  public abstract void redrawInfoWindow(); //p
  
  public abstract void setOnMyLocationChangeListener(MapController.OnMyLocationChangeListener paramOnMyLocationChangeListener) // a
    throws RemoteException;
  
  public abstract void showZoomControlsEnabled(boolean paramBoolean);
  
  public abstract void showMyLocationButtonEnabled(boolean paramBoolean);  //b
  
  public abstract void showCompassEnabled(boolean paramBoolean); //c
  
  public abstract void showScaleEnabled(boolean paramBoolean); // d
  
  public abstract void destroy();
  
//  public abstract void getMapPrintScreen(MapController.onMapPrintScreenListener printScreenListener); // a
  
  public abstract void a(MapController.OnMapScreenShotListener screenShotListener); // a
  
  public abstract void setLogoPosition(int paramInt); // d
  
  public abstract void setZoomPosition(int paramInt); //e
  
  public abstract float getScalePerPixel()  //J
    throws RemoteException;
  
  public abstract LatLngBounds getMapBounds();
  
  public abstract LatLngBounds a(LatLng paramLatLng, float paramFloat);
  
  public abstract void onResume(); // d
  
  public abstract void onPause(); //e
  
  public abstract boolean onTouchEvent(MotionEvent paramMotionEvent); // a
  
  public abstract void setRunLowFrame(boolean isLow);
  
  public abstract void setMyTrafficStyle(int paramInt1, int paramInt2, int paramInt3, int paramInt4) // a
    throws RemoteException;
  
  public abstract void setRotateAngle(float paramFloat) // a
    throws RemoteException;
  
  public abstract List<Marker> getMapScreenMarkers()
    throws RemoteException;
  
  public abstract void win2Map(int paramInt1, int paramInt2, FPoint paramFPoint); // a
  
  public abstract void a(float paramFloat1, float paramFloat2, IPoint paramIPoint);// a
  
  public abstract float getMapLenWithWin(int paramInt); //c
  
  public abstract void N(); //N

  /**
   * 设置屏幕上的某个点为地图旋转中心点
   * @param x - 屏幕上设置为地图中心点的 x 像素坐标，x 的范围为 0<= x <= 手机屏幕的像素宽度。
   * @param y - 屏幕上设置为地图中心点的 y 像素坐标，y 的范围为 0<= y <= 手机屏幕的像素高度。
   */
  public abstract void setPointToCenter(int x, int y) // a
    throws RemoteException;

  public void setCompassViewPosition(int xPix,int yPix)throws RemoteException;

  public void setScaleViewPosition(int xPix,int yPix)throws RemoteException;

  public void setLogoViewPosition(int xPix,int yPix)throws RemoteException;

  public void setCompassViewBitmap(Bitmap bitmap)throws RemoteException;

  public void setLogoBitmap(Bitmap bitmap)throws RemoteException;

  public void setLocationViewBitmap(Bitmap[] bitmaps )throws RemoteException;

  public abstract CameraPosition getCameraPositionPrj_(boolean paramBoolean); //l
  
  public abstract void setMapTextZIndex(int paramInt) //g
    throws RemoteException;
  
  public abstract int getMapTextZIndex() //Q
    throws RemoteException;
  
  public abstract boolean R(); //R
  
  public abstract IArcDelegateDecode addArc(ArcOptions paramArcOptions) // a
    throws RemoteException;
  
  public abstract CameraAnimatorDecode S(); //S
  
  public abstract INavigateArrowDelegateDecode addNavigateArrow(NavigateArrowOptions paramNavigateArrowOptions) // a
    throws RemoteException;
  
  public abstract ArrayList<Marker> addMarkers(ArrayList<MarkerOptions> optionses, boolean moveToCenter) // a
    throws RemoteException;
  
  public abstract void removecache() //T
    throws RemoteException;
  
  public abstract void removecache(MapController.OnCacheRemoveListener paramOnCacheRemoveListener) // a
    throws RemoteException;
  
  public abstract void showMapText(boolean paramBoolean) //i
    throws RemoteException;
  
  public abstract void setVisibility(int visibility); //h
  
  public abstract void setOnPOIClickListener(MapController.OnPOIClickListener paramOnPOIClickListener) // a
    throws RemoteException;
  
  public abstract Point I(); //I
  
  public abstract void reloadMap(); //W

  public abstract void setMapCenter(LatLng latLng);


  public abstract void setGridUrlListener(MapController.GridUrlListener listener);

  public abstract void setGeojsonOptions(GeoJsonLayerOptions options);

  public abstract void clearGeojsonOverlay();

  public abstract void destroyGeojsonOverlay();

  public abstract void setGeoJsonServerListener(MapController.GeoJsonServerListener listener);

  public abstract MapController.GeoJsonServerListener getGeoJsonServerListener();
}
