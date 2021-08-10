package com.unistrong.api.maps.overlay;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import com.unistrong.api.mapcore.ConfigableConstDecode;
import com.unistrong.api.mapcore.util.ResourcesUtilDecode;
import com.unistrong.api.mapcore.util.Util;
import com.unistrong.api.maps.MapController;
import com.unistrong.api.maps.CameraUpdateFactory;
import com.unistrong.api.maps.model.BitmapDescriptor;
import com.unistrong.api.maps.model.BitmapDescriptorFactory;
import com.unistrong.api.maps.model.LatLng;
import com.unistrong.api.maps.model.LatLngBounds;
import com.unistrong.api.maps.model.Marker;
import com.unistrong.api.maps.model.MarkerOptions;
import com.unistrong.api.maps.model.Polyline;
import com.unistrong.api.maps.model.PolylineOptions;
import com.unistrong.api.services.busline.BusLineItem;
import com.unistrong.api.services.busline.BusStationItem;
import com.unistrong.api.services.core.LatLonPoint;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 公交线路图层类。在地图API里，如果要显示公交线路，可以用此类来创建公交线路图层。如不满足需求，也可以自己创建自定义的公交线路图层。
 */
public class BusLineOverlay
{
    /**
     * 公交线路。
     */
  private BusLineItem buslineItem;
    /**
     * 地图对象。
     */
  private MapController lmap;
    /**
     * 标记集合。
     */
  private ArrayList<Marker> markers = new ArrayList();
    /**
     * 线段对象。
     */
  private Polyline polyline;
  private List<BusStationItem> e;
  private BitmapDescriptor startBitmap;
  private BitmapDescriptor endBitmap;
  private BitmapDescriptor busBitmap;
  private BitmapDescriptor roadBitmap;
  private Context context;

    /**
     * 通过此构造函数创建公交线路图层。
     * @param context  - 当前activity。BusStationItem
     * @param lmap  - 地图对象。
     * @param busLineItem - 公交线路。详见搜索服务模块的公交线路和公交站点包（com.leador.api.services.busline）中的类 {@link BusStationItem}。
     */
  public BusLineOverlay(Context context, MapController lmap, BusLineItem busLineItem)
  {
    this.context = context;
    this.buslineItem = busLineItem;
    this.lmap = lmap;
    this.e = this.buslineItem.getBusStations();
  }

    /**
     * 添加公交线路到地图中。
     */
  public void addToMap()
  {
    try
    {
      List<LatLonPoint> localList = this.buslineItem.getDirectionsCoordinates();
      ArrayList<LatLng> localArrayList = MapServicesUtil.clone(localList);
      PolylineOptions options = new PolylineOptions().addAll(localArrayList).width(getBuslineWidth());
      if(isUserTexture()){
        options.setUseTexture(true);
        options.setCustomTexture(getRoadBitmapDescriptor());
        options.width(50);
      }else{
        options.color(this.getBusColor());
      }
      this.polyline = this.lmap.addPolyline(options);
      if (this.e.size() < 1) {
        return;
      }
      for (int j = 1; j < this.e.size() - 1; j++)
      {
        Marker localMarker2 = this.lmap.addMarker(a(j));
        this.markers.add(localMarker2);
      }
      Marker localMarker1 = this.lmap.addMarker(a(0));
      this.markers.add(localMarker1);
      
      Marker localMarker2 = this.lmap.addMarker(a(this.e.size() - 1));
      this.markers.add(localMarker2);
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }

    /**
     * 去掉BusLineOverlay上所有的Marker。
     */
  public void removeFromMap()
  {
    if (this.polyline != null) {
      this.polyline.remove();
    }
    try
    {
      for (Marker localMarker : this.markers) {
        localMarker.remove();
      }
      a();
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }
  
  private void a()
  {
    if (this.startBitmap != null)
    {
      this.startBitmap.recycle();
      this.startBitmap = null;
    }
    if (this.endBitmap != null)
    {
      this.endBitmap.recycle();
      this.endBitmap = null;
    }
    if (this.busBitmap != null)
    {
      this.busBitmap.recycle();
      this.busBitmap = null;
    }
    if(this.roadBitmap!=null){
      this.roadBitmap.recycle();
      this.roadBitmap = null;
    }
  }

    /**
     * 移动镜头到当前的视角。
     */
  public void zoomToSpan()
  {
    if (this.lmap == null) {
      return;
    }
    try
    {
      List<LatLonPoint> localList = this.buslineItem.getDirectionsCoordinates();
      if ((localList != null) && (localList.size() > 0))
      {
        LatLngBounds localLatLngBounds = a(localList);
        this.lmap.moveCamera(CameraUpdateFactory.newLatLngBounds(localLatLngBounds, 5));
      }
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }
  
  private LatLngBounds a(List<LatLonPoint> paramList)
  {
    LatLngBounds.Builder localBuilder = LatLngBounds.builder();
    for (int j = 0; j < paramList.size(); j++) {
      localBuilder.include(new LatLng(((LatLonPoint)paramList.get(j)).getLatitude(), ((LatLonPoint)paramList.get(j))
        .getLongitude()));
    }
    return localBuilder.build();
  }
  
  private MarkerOptions a(int paramInt)
  {
    MarkerOptions localMarkerOptions = new MarkerOptions().position(new LatLng(((BusStationItem)this.e.get(paramInt)).getLatLonPoint().getLatitude(), ((BusStationItem)this.e.get(paramInt)).getLatLonPoint().getLongitude())).title(getTitle(paramInt)).snippet(getSnippet(paramInt));
    if (paramInt == 0)
    {
      localMarkerOptions.icon(getStartBitmapDescriptor());
    }
    else if (paramInt == this.e.size() - 1)
    {
      localMarkerOptions.icon(getEndBitmapDescriptor());
    }
    else
    {
      localMarkerOptions.anchor(0.5F, 0.5F);
      localMarkerOptions.icon(getBusBitmapDescriptor());
    }
    return localMarkerOptions;
  }
    /**
     * 给起点Marker设置图标，并返回更换图标的图片。如不用默认图片，需要重写此方法。
     * @return 更换的Marker图片。
     */
  protected BitmapDescriptor getStartBitmapDescriptor()
  {
    if(startBitmap==null){
      this.startBitmap = getBitmap("route_start.png");
    }
    return this.startBitmap;
  }
    /**
     * 给终点Marker设置图标，并返回更换图标的图片。如不用默认图片，需要重写此方法。
     * @return 更换的Marker图片。
     */
  protected BitmapDescriptor getEndBitmapDescriptor()
  {
    if(this.endBitmap==null){
      this.endBitmap = getBitmap("route_end.png");
    }
    return this.endBitmap;
  }
    /**
     * 给公交Marker设置图标，并返回更换图标的图片。如不用默认图片，需要重写此方法。
     * @return 更换的Marker图片。
     */
  protected BitmapDescriptor getBusBitmapDescriptor()
  {
    if(this.busBitmap==null){
      this.busBitmap = getBitmap("route_bus.png");
    }
    return this.busBitmap;
  }

  /**
   * 是否使用纹理
   * @return true 使用，false 不使用。
     */
  protected boolean isUserTexture(){
    return true;
  }

  protected BitmapDescriptor getRoadBitmapDescriptor()
  {
    if(this.roadBitmap == null){
      this.roadBitmap = BitmapDescriptorFactory.fromAsset("route_line.png");
    }
    return this.roadBitmap;
  }
    /**
     * 返回第index的Marker的标题。
     * @param index  - 第几个Marker。
     * @return marker的标题。
     */
  protected String getTitle(int index)
  {
    return ((BusStationItem)this.e.get(index)).getBusStationName();
  }

    /**
     * 返回第index的Marker的详情。
     * @param index  - 第几个Marker。
     * @return marker的详情。
     */
  protected String getSnippet(int index)
  {
    return "";
  }

    /**
     * 从marker中得到公交站点在list的位置。
     * @param marker  - 一个标记的对象。
     * @return 返回该marker对应的公交站点在list的位置。
     */
  public int getBusStationIndex(Marker marker)
  {
    for (int j = 0; j < this.markers.size(); j++) {
      if (((Marker)this.markers.get(j)).equals(marker)) {
        return j;
      }
    }
    return -1;
  }

    /**
     * 返回第index的公交站点的信息。
     * @param index  - 第几个公交站点。
     * @return 公交站点的信息。详见搜索服务模块的公交线路和公交站点包（com.leador.api.services.busline）中的类 {@link import BusStationItem}。
     */
  public BusStationItem getBusStationItem(int index)
  {
    if ((index < 0) || (index >= this.e.size())) {
      return null;
    }
    return (BusStationItem)this.e.get(index);
  }
  
  protected int getBusColor()
  {
    return Color.parseColor("#537edc");
  }
  
  protected float getBuslineWidth()
  {
    return 18.0F;
  }
  
  private BitmapDescriptor getBitmap(String paramString)
  {
    InputStream localInputStream = null;
    Bitmap localBitmap = null;
    BitmapDescriptor localBitmapDescriptor = null;
    try
    {
      localInputStream = ResourcesUtilDecode.getSelfAssets(this.context).open(paramString);
      localBitmap = BitmapFactory.decodeStream(localInputStream);
      localBitmap = Util.zoomBitmap(localBitmap, ConfigableConstDecode.Resolution);
      if (localInputStream != null) {
        try
        {
          localInputStream.close();
        }
        catch (IOException localIOException1)
        {
          localIOException1.printStackTrace();
        }
      }
      localBitmapDescriptor = BitmapDescriptorFactory.fromBitmap(localBitmap);
    }
    catch (IOException localIOException2)
    {
      localIOException2.printStackTrace();
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
    finally
    {
      if (localInputStream != null) {
        try
        {
          localInputStream.close();
        }
        catch (IOException localIOException5)
        {
          localIOException5.printStackTrace();
        }
      }
    }
    
    localBitmap.recycle();
    return localBitmapDescriptor;
  }
}
