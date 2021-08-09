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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class RouteOverlay//b
{
  protected List<Marker> stationMarkers = new ArrayList<Marker>();
  protected List<Polyline> allPolyLines = new ArrayList<Polyline>();
  protected Marker startMarker;
  protected Marker endMarker;
  protected LatLng startPoint;
  protected LatLng endPoint;
  protected MapController map;
  private Context context;
  private Bitmap startBitmap;
  private Bitmap endBitmap;
  private Bitmap busBitmap;
  private Bitmap manBitmap;
  private Bitmap carBitmap;
  protected boolean nodeIconVisible = true;
  
  public RouteOverlay(Context paramContext)
  {
    this.context = paramContext;
  }

    /**
     * 去掉RouteOverlay上所有的Marker。
     */
  public void removeFromMap()
  {
    if (this.startMarker != null) {
      this.startMarker.remove();
    }
    if (this.endMarker != null) {
      this.endMarker.remove();
    }
    for (Iterator<Marker> localIterator = this.stationMarkers.iterator(); localIterator.hasNext();)
    {
      Marker localObject = (Marker)localIterator.next();
      ((Marker)localObject).remove();
    }
    for (Iterator<Polyline> localIterator = this.allPolyLines.iterator(); localIterator.hasNext();)
    {
      Polyline localObject2 = (Polyline)localIterator.next();
      ((Polyline)localObject2).remove();
    }
    a();
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
    if (this.manBitmap != null)
    {
      this.manBitmap.recycle();
      this.manBitmap = null;
    }
    if (this.carBitmap != null)
    {
      this.carBitmap.recycle();
      this.carBitmap = null;
    }
  }

    /**
     * 给起点Marker设置图标，并返回更换图标的图片。如不用默认图片，需要重写此方法。
     * @return 更换的Marker图片。
     */
  protected BitmapDescriptor getStartBitmapDescriptor()
  {
    return initBitmap(this.startBitmap, "route_start.png");
  }

    /**
     * 给终点Marker设置图标，并返回更换图标的图片。如不用默认图片，需要重写此方法。
     * @return 更换的Marker图片。
     */
  protected BitmapDescriptor getEndBitmapDescriptor()
  {
    return initBitmap(this.endBitmap, "route_end.png");
  }

    /**
     * 给公交Marker设置图标，并返回更换图标的图片。如不用默认图片，需要重写此方法。
     * @return 更换的Marker图片。
     */
  protected BitmapDescriptor getBusBitmapDescriptor()
  {
    return initBitmap(this.busBitmap, "route_bus.png");
  }

    /**
     * 给步行Marker设置图标，并返回更换图标的图片。如不用默认图片，需要重写此方法。
     * @return 更换的Marker图片。
     */
  protected BitmapDescriptor getWalkBitmapDescriptor()
  {
    return initBitmap(this.manBitmap, "route_way.png");
  }

    /**
     * 给驾车Marker设置图标，并返回更换图标的图片。如不用默认图片，需要重写此方法。
     * @return 更换的Marker图片。
     */
  protected BitmapDescriptor getDriveBitmapDescriptor()
  {
    return initBitmap(this.carBitmap, "route_car.png");
  }
  
  private BitmapDescriptor initBitmap(Bitmap paramBitmap, String bitmapName)
  {
    InputStream localInputStream = null;
    BitmapDescriptor localBitmapDescriptor = null;
    try
    {
      localInputStream = ResourcesUtilDecode.getSelfAssets(this.context).open(bitmapName);
      paramBitmap = BitmapFactory.decodeStream(localInputStream);
      paramBitmap = Util.zoomBitmap(paramBitmap, ConfigableConstDecode.Resolution);
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
      localBitmapDescriptor = BitmapDescriptorFactory.fromBitmap(paramBitmap);
    }
    catch (IOException localIOException2)
    {
      localIOException2.printStackTrace();
    }
    finally
    {
      if (localInputStream != null) {
        try
        {
          localInputStream.close();
        }
        catch (IOException localIOException4)
        {
          localIOException4.printStackTrace();
        }
      }
    }
    
    paramBitmap.recycle();
    return localBitmapDescriptor;
  }
  
  protected void addStartAndEndMarker()
  {
    this.startMarker = this.map.addMarker(new MarkerOptions()
      .position(this.startPoint).icon(getStartBitmapDescriptor())
      .title("起点"));
    
    this.endMarker = this.map.addMarker(new MarkerOptions().position(this.endPoint)
      .icon(getEndBitmapDescriptor()).title("终点"));
  }

    /**
     * 移动镜头到当前的视角。
     */
  public void zoomToSpan()
  {
    if (this.startPoint != null)
    {
      if (this.map == null) {
        return;
      }
      try
      {
        LatLngBounds localLatLngBounds = getLatLngBounds();
        this.map.animateCamera(
          CameraUpdateFactory.newLatLngBounds(localLatLngBounds, 50));
      }
      catch (Throwable localThrowable)
      {
        localThrowable.printStackTrace();
      }
    }
  }
  
  protected LatLngBounds getLatLngBounds()
  {
    LatLngBounds.Builder localBuilder = LatLngBounds.builder();
    localBuilder.include(new LatLng(this.startPoint.latitude, this.startPoint.longitude));
    localBuilder.include(new LatLng(this.endPoint.latitude, this.endPoint.longitude));
    return localBuilder.build();
  }

    /**
     * 路段节点图标控制显示接口。
     * @param paramBoolean - true为显示节点图标，false为不显示。
     */
  public void setNodeIconVisibility(boolean paramBoolean)
  {
    try
    {
      this.nodeIconVisible = paramBoolean;
      if ((this.stationMarkers != null) && (this.stationMarkers.size() > 0)) {
        for (int i = 0; i < this.stationMarkers.size(); i++) {
          ((Marker)this.stationMarkers.get(i)).setVisible(paramBoolean);
        }
      }
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }

    /**
     * 添加站点marker。
     * @param paramMarkerOptions - 一个marker 的选项。
     */
  protected void addStationMarker(MarkerOptions paramMarkerOptions)
  {
    if (paramMarkerOptions == null) {
      return;
    }
    Marker localMarker = this.map.addMarker(paramMarkerOptions);
    if (localMarker != null) {
      this.stationMarkers.add(localMarker);
    }
  }

    /**
     * 添加线段。
     * @param paramPolylineOptions - 线段的选项类。
     */
  protected void addPolyLine(PolylineOptions paramPolylineOptions)
  {
    if (paramPolylineOptions == null) {
      return;
    }
    Polyline localPolyline = this.map.addPolyline(paramPolylineOptions);
    if (localPolyline != null) {
      this.allPolyLines.add(localPolyline);
    }
  }

    /**
     *返回路线线段的宽度值。
     * @return 路线线段的宽度值。
     */
  protected float getRouteWidth()
  {
    return 18.0F;
  }

    /**
     * 返回自定义步行路线颜色。
     * @return   步行路线颜色。
     */
  protected int getWalkColor()
  {
    return Color.parseColor("#6db74d");
  }
    /**
     * 返回自定义公交路线颜色。
     * @return   公交路线颜色。
     */
  protected int getBusColor()
  {
    return Color.parseColor("#537edc");
  }
    /**
     * 返回自定义驾车路线颜色。
     * @return   驾车路线颜色。
     */
  protected int getDriveColor()
  {
    return Color.parseColor("#537edc");
  }
}
