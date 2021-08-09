package com.unistrong.api.maps.model;

/**
 * 唤起地图路径规划功能的参数类。
 */
 class RoutePara
{
  private int a = 0;
  private int b = 0;
  private LatLng c;
  private LatLng d;
  private String e;
  private String f;

    /**
     * 驾车规划模式。
     * @return 返会驾车规划模式对应的值。
     */
  public int getDrivingRouteStyle()
  {
    return this.a;
  }

    /**
     * 设置驾车路径规划的规划模式。
     * @param drivingRouteStyle  - 模式。
     */
  public void setDrivingRouteStyle(int drivingRouteStyle)
  {
    if ((drivingRouteStyle >= 0) && (drivingRouteStyle < 9)) {
      this.a = drivingRouteStyle;
    }
  }

    /**
     * 公交驾车模式。
     * @return 公交驾车模式对应的值。
     */
  public int getTransitRouteStyle()
  {
    return this.b;
  }

    /**
     * 设置公交路径规划的规划模式。
     * @param transitRouteStyle - 模式。
     */
  public void setTransitRouteStyle(int transitRouteStyle)
  {
    if ((transitRouteStyle >= 0) && (transitRouteStyle < 6)) {
      this.b = transitRouteStyle;
    }
  }

    /**
     * 获取起始点的经纬度。
     * @return 起点坐标的经纬度。
     */
  public LatLng getStartPoint()
  {
    return this.c;
  }

    /**
     * 设置路线检索起点坐标。
     * @param startPoint - 起点坐标。
     */
  public void setStartPoint(LatLng startPoint)
  {
    this.c = startPoint;
  }

    /**
     * 获取路线检索终点坐标。
     * @return 终点坐标。
     */
  public LatLng getEndPoint()
  {
    return this.d;
  }

    /**
     * 设置路线检索终点坐标。
     * @param endPoint - 终点坐标。
     */
  public void setEndPoint(LatLng endPoint)
  {
    this.d = endPoint;
  }

    /**
     * 获取路线检索终点名称。
     * @return 终点名称。
     */
  public String getEndName()
  {
    return this.f;
  }

    /**
     * 设置路线检索终点名称。
     * @param endName - 终点名称。
     */
  public void setEndName(String endName)
  {
    this.f = endName;
  }

    /**
     * 获取路线检索起点地址名称。
     * @return 起点地址名称。
     */
  public String getStartName()
  {
    return this.e;
  }

    /**
     * 设置路线检索起点地址名称
     * @param startName - 起点地址名称。
     */
  public void setStartName(String startName)
  {
    this.e = startName;
}
}
