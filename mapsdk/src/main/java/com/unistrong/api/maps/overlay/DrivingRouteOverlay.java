package com.unistrong.api.maps.overlay;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.unistrong.api.mapcore.ConfigableConstDecode;
import com.unistrong.api.mapcore.util.ResourcesUtilDecode;
import com.unistrong.api.mapcore.util.Util;
import com.unistrong.api.maps.MapController;
import com.unistrong.api.maps.model.BitmapDescriptor;
import com.unistrong.api.maps.model.BitmapDescriptorFactory;
import com.unistrong.api.maps.model.LatLng;
import com.unistrong.api.maps.model.LatLngBounds;
import com.unistrong.api.maps.model.Marker;
import com.unistrong.api.maps.model.MarkerOptions;
import com.unistrong.api.maps.model.PolylineOptions;
import com.unistrong.api.services.core.LatLonPoint;
import com.unistrong.api.services.route.DrivePath;
import com.unistrong.api.services.route.DriveStep;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 驾车路线图层类。在地图API里，如果要显示驾车路线规划，可以用此类来创建驾车路线图层。如不满足需求，也可以自己创建自定义的驾车路线图层。
 */
public class DrivingRouteOverlay
  extends RouteOverlay
{
  private DrivePath drivePath;
  private List<LatLonPoint> pointList;
  private List<Marker> markerList = new ArrayList();
  private boolean d = true;
  private Context context;
  private PolylineOptions busLineOptions;

    /**
     *通过此构造函数创建驾车路线图层,驾车路线使用一条polyline显示，避免长距离出现卡顿的问题。
     * @param context - 当前activity。
     * @param map - 地图对象。
     * @param path - 驾车路线规划的一个方案。详见搜索服务模块的路径查询包（com.leador.api.services.route）中的类 {@link DrivePath}。
     * @param start - 起点。详见搜索服务模块的核心基础包（com.leador.api.services.core）中的类 {@link LatLonPoint}。
     * @param end - 终点。详见搜索服务模块的核心基础包（com.leador.api.services.core）中的类 {@link LatLonPoint}。
     */
  public DrivingRouteOverlay(Context context, MapController map, DrivePath path, LatLonPoint start, LatLonPoint end)
  {
    this(context, map, path, start, end, null);
    this.context = context;
  }

    /**
     *通过此构造函数创建带有途经点的驾车路线图层。
     * @param context - 当前activity。
     * @param map - 地图对象。
     * @param path - 驾车路线规划的一个方案。详见搜索服务模块的路径查询包（com.leador.api.services.route）中的类 {@link DrivePath}。
     * @param start - 起点。详见搜索服务模块的核心基础包（com.leador.api.services.core）中的类 {@link LatLonPoint}。
     * @param end - 终点。详见搜索服务模块的核心基础包（com.leador.api.services.core）中的类 {@link LatLonPoint}。
     * @param throughPointList  - 途经点列表，详见搜索服务模块的核心基础包（com.leador.api.services.core）中的类{@link LatLonPoint}。
     */
  public DrivingRouteOverlay(Context context, MapController map, DrivePath path, LatLonPoint start, LatLonPoint end, List<LatLonPoint> throughPointList)
  {
    super(context);
    this.map = map;
    this.drivePath = path;
    this.startPoint = MapServicesUtil.toLatLng(start);
    this.endPoint = MapServicesUtil.toLatLng(end);
    this.pointList = throughPointList;
    this.context = context;
  }

    /**
     * 添加驾车路线到地图中。
     */
  public void addToMap()
  {
    a();
    try
    {
      List stepList = this.drivePath.getSteps();
      int size = stepList.size();
      for (int i = 0; i < size; i++)
      {
        DriveStep driveStep = (DriveStep)stepList.get(i);
        LatLng localLatLng = MapServicesUtil.toLatLng(a(driveStep));
        if (i == 0) {
          a(this.startPoint, localLatLng);
        }
        c(driveStep);
        if(i == size-1) {
          a(getLastPoint(driveStep), this.endPoint);
        }
      }
      addStartAndEndMarker();
      c();
      
      b();
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }
  
  private void a()
  {
    this.busLineOptions = null;
    
    this.busLineOptions = new PolylineOptions();
    this.busLineOptions.color(getDriveColor()).width(getRouteWidth());
  }
  
  private void b()
  {
    addPolyLine(this.busLineOptions);
  }
  
  private LatLonPoint a(DriveStep paramDriveStep)
  {
    return (LatLonPoint)paramDriveStep.getPolyline().get(0);
  }
  
  private LatLonPoint getLastPoint(DriveStep paramDriveStep)
  {
    return (LatLonPoint)paramDriveStep.getPolyline().get(paramDriveStep
      .getPolyline().size() - 1);
  }
  
  private void a(LatLonPoint paramLatLonPoint, LatLng paramLatLng)
  {
    a(MapServicesUtil.toLatLng(paramLatLonPoint), paramLatLng);
  }
  
  private void a(LatLng paramLatLng1, LatLng paramLatLng2)
  {
    this.busLineOptions.add(new LatLng[]{paramLatLng1, paramLatLng2});
  }
  
  private void c(DriveStep paramDriveStep)
  {
    this.busLineOptions.addAll(MapServicesUtil.clone(paramDriveStep.getPolyline()));
  }
  
  private void c()
  {
    if ((this.pointList != null) && (this.pointList.size() > 0))
    {
      LatLonPoint localLatLonPoint = null;
      for (int i = 0; i < this.pointList.size(); i++)
      {
        localLatLonPoint = (LatLonPoint)this.pointList.get(i);
        if (localLatLonPoint != null) {
          this.markerList.add(this.map
                  .addMarker(new MarkerOptions()
                          .position(new LatLng(localLatLonPoint

                                  .getLatitude(), localLatLonPoint
                                  .getLongitude()))
                          .visible(this.d)
                          .icon(d())
                          .title("途经点")));
        }
      }
    }
  }
  
  protected LatLngBounds getLatLngBounds()
  {
    LatLngBounds.Builder localBuilder = LatLngBounds.builder();
    localBuilder.include(new LatLng(this.startPoint.latitude, this.startPoint.longitude));
    localBuilder.include(new LatLng(this.endPoint.latitude, this.endPoint.longitude));
    if ((this.pointList != null) && (this.pointList.size() > 0)) {
      for (LatLonPoint latLonPoint:pointList) {
        localBuilder.include(new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude()));
      }
    }
    return localBuilder.build();
  }

    /**
     * 设置途径点的图标是否可见。
     * @param paramBoolean - true表示可见，false不可见。
     */
  public void setThroughPointIconVisibility(boolean paramBoolean)
  {
    try
    {
      this.d = paramBoolean;
      if ((this.markerList != null) &&
        (this.markerList.size() > 0)) {
        for (int i = 0; i < this.markerList.size(); i++) {
          ((Marker)this.markerList.get(i)).setVisible(paramBoolean);
        }
      }
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }
  
  private BitmapDescriptor d()
  {
    InputStream localInputStream = null;
    Bitmap localBitmap = null;
    BitmapDescriptor localBitmapDescriptor = null;
    try
    {
      localInputStream = ResourcesUtilDecode.getSelfAssets(this.context).open("throughpoint.png");//TODO   amap_throughpoint.PNG
      
      localBitmap = BitmapFactory.decodeStream(localInputStream);
      localBitmap = Util.zoomBitmap(localBitmap, ConfigableConstDecode.Resolution);
      try
      {
        if (localInputStream != null) {
          localInputStream.close();
        }
      }
      catch (Throwable localThrowable1)
      {
        localThrowable1.printStackTrace();
      }
      localBitmapDescriptor = BitmapDescriptorFactory.fromBitmap(localBitmap);
    }
    catch (Throwable localThrowable2)
    {
      localThrowable2.printStackTrace();
    }
    finally
    {
      try
      {
        if (localInputStream != null) {
          localInputStream.close();
        }
      }
      catch (Throwable localThrowable4)
      {
        localThrowable4.printStackTrace();
      }
    }
    
    localBitmap.recycle();
    return localBitmapDescriptor;
  }

    /**
     * 去掉DriveLineOverlay上所有的Marker。
     */
  public void removeFromMap()
  {
    try
    {
      super.removeFromMap();
      if ((this.markerList != null) &&
        (this.markerList.size() > 0))
      {
        for (int i = 0; i < this.markerList.size(); i++) {
          ((Marker)this.markerList.get(i)).remove();
        }
        this.markerList.clear();
      }
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }
}
