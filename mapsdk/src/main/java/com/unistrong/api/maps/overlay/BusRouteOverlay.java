package com.unistrong.api.maps.overlay;

import android.content.Context;
import com.unistrong.api.maps.MapController;
import com.unistrong.api.maps.model.LatLng;
import com.unistrong.api.maps.model.MarkerOptions;
import com.unistrong.api.maps.model.PolylineOptions;
import com.unistrong.api.services.busline.BusStationItem;
import com.unistrong.api.services.core.LatLonPoint;
import com.unistrong.api.services.route.BusPath;
import com.unistrong.api.services.route.BusStep;
import com.unistrong.api.services.route.RouteBusLineItem;
import com.unistrong.api.services.route.RouteBusWalkItem;
import com.unistrong.api.services.route.WalkStep;

import java.util.List;

/**
 * 公交路线图层类。在地图API里，如果需要显示公交路线，可以用此类来创建公交路线图层。如不满足需求，也可以自己创建自定义的公交路线图层。
 */
public class BusRouteOverlay
  extends RouteOverlay
{
  private BusPath busPath;
  private LatLng latLng;

    /**
     * 通过此构造函数创建公交路线图层。
     * @param context - 当前activity。
     * @param map  - 地图对象。
     * @param path  - 公交路径规划的一个路段。详见搜索服务模块的路径查询包（com.leador.api.services.route）中的类 {@link BusPath}。
     * @param start - 起点坐标。详见搜索服务模块的核心基础包（com.leador.api.services.core）中的类 {@link LatLonPoint}。
     * @param end - 终点坐标。详见搜索服务模块的核心基础包（com.leador.api.services.core）中的类 {@link LatLonPoint}。
     */
  public BusRouteOverlay(Context context, MapController map, BusPath path, LatLonPoint start, LatLonPoint end)
  {
    super(context);
    this.busPath = path;
    this.startPoint = MapServicesUtil.toLatLng(start);
    this.endPoint = MapServicesUtil.toLatLng(end);
    this.map = map;
  }

    /**
     * 添加公交路线到地图上。
     */
  public void addToMap()
  {
    try
    {
      List<BusStep> stepList = this.busPath.getSteps();
      for (int i = 0; i < stepList.size(); i++)
      {
        BusStep busStep = stepList.get(i);
        if (i < stepList.size() - 1)
        {
          BusStep busStep1 = stepList.get(i + 1);
          if ((busStep.getBusLines() != null) && (busStep.getWalk() != null)) {
            //连接步行与公交
            walkLineToBusLine(busStep);
          }
          if ((busStep.getBusLines() != null) && (busStep1.getWalk() != null)) {
            //连接步行与公交
            busLineToWalkLine(busStep, busStep1);
          }
          if ((busStep.getBusLines() != null) && (busStep1.getWalk() == null) && (busStep1.getBusLines() != null)) {
            busLineToBusLine(busStep, busStep1);
          }
          if ((busStep.getBusLines() != null) && (busStep1.getWalk() == null) && (busStep1.getBusLines() != null)) {
            drawLineArrow(busStep, busStep1);
          }
        }
        if ((busStep.getWalk() != null) &&
          (busStep.getWalk().getSteps().size() > 0)) {
          a(busStep);
        } else if (busStep.getBusLines() == null) {
          addPolyLine(this.latLng, this.endPoint);
        }
        if (busStep.getBusLines() != null)
        {
          java.util.List<RouteBusLineItem> busLineItemList = busStep.getBusLines();
          for(RouteBusLineItem item:busLineItemList) {
            addPoints(item);
            b(item);
          }
        }
      }
      addStartAndEndMarker();
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }
  
  private void a(BusStep paramBusStep)
  {
    RouteBusWalkItem localRouteBusWalkItem = paramBusStep.getWalk();
    List localList = localRouteBusWalkItem.getSteps();
    for (int i = 0; i < localList.size(); i++)
    {
      WalkStep localWalkStep = (WalkStep)localList.get(i);
      Object localObject2;
      Object localObject3;
      if (i == 0)
      {
        LatLng localObject1 = MapServicesUtil.toLatLng(
                (LatLonPoint) localWalkStep.getPolyline().get(0));
        localObject2 = localWalkStep.getRoad();
        localObject3 = c(localList);
        a((LatLng)localObject1, (String)localObject2, (String)localObject3);
      }
      Object localObject1 = MapServicesUtil.clone(localWalkStep.getPolyline());
      this.latLng = ((LatLng)((List)localObject1).get(((List)localObject1).size() - 1));
      
      b((List)localObject1);
      if (i < localList.size() - 1)
      {
        localObject2 = (LatLng)((List)localObject1).get(((List)localObject1)
          .size() - 1);
        
        localObject3 = MapServicesUtil.toLatLng(
                (LatLonPoint) ((WalkStep) localList.get(i + 1)).getPolyline().get(0));
        if (!((LatLng)localObject2).equals(localObject3)) {
          addPolyLine((LatLng) localObject2, (LatLng) localObject3);
        }
      }
    }
  }
  
  private void addPoints(RouteBusLineItem paramRouteBusLineItem)
  {
    addPointList(paramRouteBusLineItem.getPolyline());
  }
  
  private void addPointList(List<LatLonPoint> pointList)
  {
    if (pointList.size() < 1) {
      return;
    }
    addPolyLine(new PolylineOptions().width(getRouteWidth())
            .color(getBusColor())
            .addAll(MapServicesUtil.clone(pointList)));
  }
  
  private void a(LatLng paramLatLng, String paramString1, String paramString2)
  {
    addStationMarker(new MarkerOptions().position(paramLatLng).title(paramString1)
      .snippet(paramString2).anchor(0.5F, 0.5F).visible(this.nodeIconVisible)
      .icon(getWalkBitmapDescriptor()));
  }
  
  private void b(RouteBusLineItem paramRouteBusLineItem)
  {
    BusStationItem localBusStationItem = paramRouteBusLineItem.getDepartureBusStation();
    LatLng localLatLng = MapServicesUtil.toLatLng(localBusStationItem
            .getLatLonPoint());
    String str1 = paramRouteBusLineItem.getBusLineName();
    String str2 = c(paramRouteBusLineItem);
    
    addStationMarker(new MarkerOptions().position(localLatLng).title(str1)
      .snippet(str2).anchor(0.5F, 0.5F).visible(this.nodeIconVisible)
      .icon(getBusBitmapDescriptor()));
  }
  
  private void drawLineArrow(BusStep paramBusStep1, BusStep paramBusStep2)
  {
    LatLng localLatLng1 = MapServicesUtil.toLatLng(getLastBusPoint(paramBusStep1));
    
    LatLng localLatLng2 = MapServicesUtil.toLatLng(getFirstBusPoint(paramBusStep2));
    if ((localLatLng2.latitude - localLatLng1.latitude > 1.0E-4D) || (localLatLng2.longitude - localLatLng1.longitude > 1.0E-4D)) {
      drawLineArrow(localLatLng1, localLatLng2);
    }
  }
  
  private void busLineToBusLine(BusStep paramBusStep1, BusStep paramBusStep2)
  {
    LatLonPoint lastBusPoint = getLastBusPoint(paramBusStep1);
    LatLng lastPoint = MapServicesUtil.toLatLng(lastBusPoint);
    LatLonPoint firstBusPoint = getFirstBusPoint(paramBusStep2);
    LatLng firstPoint = MapServicesUtil.toLatLng(firstBusPoint);
    if (!lastPoint.equals(firstPoint)) {
      drawLineArrow(lastPoint, firstPoint);
    }
  }
  
  private void busLineToWalkLine(BusStep paramBusStep1, BusStep paramBusStep2)
  {
    LatLonPoint lastBusPoint = getLastBusPoint(paramBusStep1);
    LatLonPoint firstWalkPoint = getFirstWalkPoint(paramBusStep2);
    if (!lastBusPoint.equals(firstWalkPoint)) {
      addPolyLine(lastBusPoint, firstWalkPoint);
    }
  }
  
  private void walkLineToBusLine(BusStep step)
  {
    LatLonPoint lastWalkPoint = getLastWalkPoint(step);
    LatLonPoint firstPoint = getFirstBusPoint(step);
    if (!lastWalkPoint.equals(firstPoint)) {
      addPolyLine(lastWalkPoint, firstPoint);
    }
  }
  
  private LatLonPoint getFirstWalkPoint(BusStep paramBusStep)
  {
    return (LatLonPoint)((WalkStep)paramBusStep.getWalk().getSteps().get(0)).getPolyline().get(0);
  }
  
  private void addPolyLine(LatLonPoint startPoint, LatLonPoint endPoint)
  {
    LatLng localLatLng1 = MapServicesUtil.toLatLng(startPoint);
    LatLng localLatLng2 = MapServicesUtil.toLatLng(endPoint);
    
    addPolyLine(localLatLng1, localLatLng2);
  }
  
  private void addPolyLine(LatLng startPoint, LatLng endPoint)
  {
    addPolyLine(new PolylineOptions().add(new LatLng[]{startPoint, endPoint})
      .width(getRouteWidth()).color(getWalkColor()));
  }
  
  private void b(List<LatLng> paramList)
  {
    addPolyLine(new PolylineOptions().addAll(paramList)
      .color(getWalkColor()).width(getRouteWidth()));
  }
  
  private String c(List<WalkStep> paramList)
  {
    float f = 0.0F;
    for (WalkStep localWalkStep : paramList) {
      f += localWalkStep.getDistance();
    }
    return "步行" + f + "米";
  }
  
  public void drawLineArrow(LatLng paramLatLng1, LatLng paramLatLng2)
  {
    addPolyLine(new PolylineOptions().add(new LatLng[] { paramLatLng1, paramLatLng2 }).width(3.0F)
      .color(getBusColor()).width(getRouteWidth()));
  }
  
  private String c(RouteBusLineItem paramRouteBusLineItem)
  {
    return "(" + paramRouteBusLineItem.getDepartureBusStation().getBusStationName() + "-->" + paramRouteBusLineItem.getArrivalBusStation().getBusStationName() + ") 经过" + (paramRouteBusLineItem.getPassStationNum() + 1) + "站";
  }
  
  private LatLonPoint getLastWalkPoint(BusStep busStep)
  {
    List localList1 = busStep.getWalk().getSteps();
    WalkStep walkStep = (WalkStep)localList1.get(localList1.size() - 1);
    List points = walkStep.getPolyline();
    return (LatLonPoint)points.get(points.size() - 1);
  }
  
  private LatLonPoint getLastBusPoint(BusStep busStep)
  {
    int size = busStep.getBusLines().size();
    List localList = busStep.getBusLines().get(size-1).getPolyline();
    return (LatLonPoint)localList.get(localList.size() - 1);
  }
  
  private LatLonPoint getFirstBusPoint(BusStep busStep)
  {
    List localList = busStep.getBusLines().get(0).getPolyline();
    return (LatLonPoint)localList.get(0);
  }
}
