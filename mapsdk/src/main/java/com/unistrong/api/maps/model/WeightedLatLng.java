package com.unistrong.api.maps.model;

import com.unistrong.api.mapcore.util.Util;
import com.leador.mapcore.DPoint;

/**
 * 带权值的经纬度位置点。
 */
public class WeightedLatLng
{
    /**
     * 默认权值1.0
     */
  public static final double DEFAULT_INTENSITY = 1.0D;
  private DPoint point;
    /**
     *权值。
     */
  public final double intensity;
    /**
     *经纬度。
     */
  public final LatLng latLng;

    /**
     *构造函数。
     * @param latLng - 地理位置。
     * @param intensity  - 权值，大于零；两个权值等于一的位置点等同于一个全职等于二的点。
     */
  public WeightedLatLng(LatLng latLng, double intensity)
  {
    if (latLng == null) {
      throw new IllegalArgumentException("latLng can not null");
    }
    this.latLng = latLng;
    this.point = Util.a(latLng);
    if (intensity >= 0.0D) {
      this.intensity = intensity;
    } else {
      this.intensity = DEFAULT_INTENSITY;
    }
  }

    /**
     *构造函数，使用设置的权值。
     * @param latLng - 地理位置坐标。
     */
  public WeightedLatLng(LatLng latLng)
  {
    this(latLng, DEFAULT_INTENSITY);
  }

  protected DPoint getPoint()
  {
    return this.point;
  }
}
