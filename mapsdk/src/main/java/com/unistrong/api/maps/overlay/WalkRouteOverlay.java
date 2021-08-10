package com.unistrong.api.maps.overlay;

import android.content.Context;
import com.unistrong.api.maps.MapController;
import com.unistrong.api.maps.model.LatLng;
import com.unistrong.api.maps.model.MarkerOptions;
import com.unistrong.api.maps.model.PolylineOptions;
import com.unistrong.api.services.core.LatLonPoint;
import com.unistrong.api.services.route.WalkPath;
import com.unistrong.api.services.route.WalkStep;

import java.util.List;

/**
 * 步行路线图层类。在地图API里，如果要显示步行路线规划，可以用此类来创建步行路线图层。如不满足需求，也可以自己创建自定义的步行路线图层。
 */
public class WalkRouteOverlay
  extends RouteOverlay
{
  private WalkPath walkPath;

    /**
     * 通过此构造函数创建步行路线图层。
     * @param context  - 当前activity。
     * @param map - 地图对象。
     * @param path - 步行路线规划的一个方案。详见搜索服务模块的路径查询包（com.leador.api.services.route）中的类 {@link WalkStep}。
     * @param start - 起点。详见搜索服务模块的核心基础包（com.leador.api.services.core）中的类 {@link LatLonPoint}。
     * @param end - 终点。详见搜索服务模块的核心基础包（com.leador.api.services.core）中的类 {@link LatLonPoint}。
     */
  public WalkRouteOverlay(Context context, MapController map, WalkPath path, LatLonPoint start, LatLonPoint end)
  {
    super(context);
    this.map = map;
    this.walkPath = path;
    this.startPoint = MapServicesUtil.toLatLng(start);
    this.endPoint = MapServicesUtil.toLatLng(end);
  }

    /**
     * 添加步行路线到地图中。
     */
  public void addToMap()
  {
    try
    {
      List steps = this.walkPath.getSteps();
      for (int i = 0; i < steps.size(); i++)
      {
        WalkStep walkStep = (WalkStep)steps.get(i);
        LatLng firstPoint = MapServicesUtil.toLatLng((LatLonPoint) walkStep.getPolyline().get(0));
        if (i == 0) {
          addPolyLine(this.startPoint, firstPoint);
        }
        if (i < steps.size() - 1)
        {
          a(walkStep, (WalkStep)steps.get(i + 1));
        }
        else
        {
          LatLng lastPoint = MapServicesUtil.toLatLng(a(walkStep));
          addPolyLine(lastPoint, this.endPoint);
        }
        a(walkStep, firstPoint);
        c(walkStep);
      }
      addStartAndEndMarker();
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }
  
  private void a(WalkStep paramWalkStep1, WalkStep paramWalkStep2)
  {
    LatLonPoint localLatLonPoint1 = a(paramWalkStep1);
    LatLonPoint localLatLonPoint2 = b(paramWalkStep2);
    if (!localLatLonPoint1.equals(localLatLonPoint2)) {
      a(localLatLonPoint1, localLatLonPoint2);
    }
  }
  
  private LatLonPoint a(WalkStep paramWalkStep)
  {
    return (LatLonPoint)paramWalkStep.getPolyline().get(paramWalkStep.getPolyline().size() - 1);
  }
  
  private LatLonPoint b(WalkStep paramWalkStep)
  {
    return (LatLonPoint)paramWalkStep.getPolyline().get(0);
  }
  
  private void a(LatLonPoint paramLatLonPoint1, LatLonPoint paramLatLonPoint2)
  {
    addPolyLine(MapServicesUtil.toLatLng(paramLatLonPoint1), MapServicesUtil.toLatLng(paramLatLonPoint2));
  }
  
  private void addPolyLine(LatLng startPoint, LatLng endPoint)
  {
    addPolyLine(new PolylineOptions()
      .add(new LatLng[] { startPoint, endPoint }).color(getWalkColor())
      .width(getRouteWidth()));
  }
  
  private void c(WalkStep paramWalkStep)
  {
    addPolyLine(new PolylineOptions()
      .addAll(MapServicesUtil.clone(paramWalkStep
              .getPolyline())).color(getWalkColor())
      .width(getRouteWidth()));
  }
  
  private void a(WalkStep walkStep, LatLng paramLatLng)
  {
    addStationMarker(new MarkerOptions()
            .position(paramLatLng)
            .title("方向:" + walkStep.getInstruction() + "\n道路:" + walkStep
                    .getRoad())
            .snippet(walkStep.getInstruction()).visible(this.nodeIconVisible)
            .anchor(0.5F, 0.5F).icon(getWalkBitmapDescriptor()));
  }
}
