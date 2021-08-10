package com.unistrong.api.maps.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.unistrong.api.mapcore.util.LMapThrowException;
import com.unistrong.api.mapcore.util.Util;

/**
 * 代表了经纬度划分的一个矩形区域。
 */
public final class LatLngBounds
  implements Parcelable
{
  public static final LatLngBoundsCreator CREATOR = new LatLngBoundsCreator();
  private final int a;
  public final LatLng southwest;
  public final LatLng northeast;
  
  LatLngBounds(int paramInt, LatLng southwest, LatLng northeast)
  {
    LMapThrowException.ThrowNullPointerException(southwest, "null southwest");
    
    LMapThrowException.ThrowNullPointerException(northeast, "null northeast");
    
    LMapThrowException.ThrowIllegalArgumentException(northeast.latitude >= southwest.latitude, "southern latitude exceeds northern latitude (%s > %s)", new Object[]{

            Double.valueOf(southwest.latitude),
            Double.valueOf(northeast.latitude)});
    this.a = paramInt;
    this.southwest = southwest;
    this.northeast = northeast;
  }
  
  public LatLngBounds(LatLng southwest, LatLng northeast)
  {
    this(1, southwest, northeast);
  }
  
  int a()
  {
    return this.a;
  }

    /**
     * 创建一个LatLngBounds构造器。
     * @return 新创建的Builder对象。
     */
  
  public static Builder builder()
  {
    return new Builder();
  }

    /**
     * 判断矩形区域是否包含传入的经纬度点。
     * @param point - 经纬度点。
     * @return true 矩形包含这个点;false 矩形未包含这个点。
     */
  public boolean contains(LatLng point)
  {
    LatLng localLatLng = point;
    
    return (a(localLatLng.latitude)) && (b(localLatLng.longitude));
  }

    /**
     * 判断矩形区域是否包含传入的矩形区域。
     * @param curBounds  - 矩形区域。
     * @return true 矩形包含这个区域;false 矩形未包含这个区域。
     */
  public boolean contains(LatLngBounds curBounds)
  {
    boolean bool = false;
    if (curBounds == null) {
      return bool;
    }
    if ((contains(curBounds.southwest)) &&
      (contains(curBounds.northeast))) {
      bool = true;
    }
    return bool;
  }

    /**
     * 判断两个矩形区域是否相交。
     * @param bounds  - 需要比较的矩形区域。
     * @return true 两个矩形相交;false 两个矩形不想交。
     */
  public boolean intersects(LatLngBounds bounds)
  {
    if (bounds == null) {
      return false;
    }
    return (a(bounds)) || (bounds.a(this));
  }
  
  private boolean a(LatLngBounds paramLatLngBounds)
  {
    if ((paramLatLngBounds == null) || (paramLatLngBounds.northeast == null) || (paramLatLngBounds.southwest == null) || (this.northeast == null) || (this.southwest == null)) {
      return false;
    }
    double d1 = paramLatLngBounds.northeast.longitude + paramLatLngBounds.southwest.longitude - this.northeast.longitude - this.southwest.longitude;
    
    double d2 = this.northeast.longitude - this.southwest.longitude + paramLatLngBounds.northeast.longitude - this.southwest.longitude;
    
    double d3 = paramLatLngBounds.northeast.latitude + paramLatLngBounds.southwest.latitude - this.northeast.latitude - this.southwest.latitude;
    
    double d4 = this.northeast.latitude - this.southwest.latitude + paramLatLngBounds.northeast.latitude - paramLatLngBounds.southwest.latitude;
    
    return (Math.abs(d1) < d2) && (Math.abs(d3) < d4);
  }

    /**
     * 返回一个新的矩形区域。新区域是根据传入的经纬度对原有区域进行最小的扩展。 返回矩形区域，需要传入至少两个扩展点的经纬度对象。多次调用此方法即可。 这个方法将选择向东或向西方向扩展，期间扩展面积相对较小一个区域。如果相同，则优先向东方向扩展。
     * @param point - 扩展点的经纬度对象
     * @return 一个根据传入的经纬度对原有区域进行最小的扩展的新矩形区域。
     */
  public LatLngBounds including(LatLng point)
  {
    double d1 = Math.min(this.southwest.latitude, point.latitude);
    double d2 = Math.max(this.northeast.latitude, point.latitude);
    double d3 = this.northeast.longitude;
    double d4 = this.southwest.longitude;
    double d5 = point.longitude;
    if (!b(d5)) {
      if (c(d4, d5) < d(d3, d5)) {
        d4 = d5;
      } else {
        d3 = d5;
      }
    }
    return new LatLngBounds(new LatLng(d1, d4, false), new LatLng(d2, d3, false));
  }
  
  private static double c(double paramDouble1, double paramDouble2)
  {
    return (paramDouble1 - paramDouble2 + 360.0D) % 360.0D;
  }
  
  private static double d(double paramDouble1, double paramDouble2)
  {
    return (paramDouble2 - paramDouble1 + 360.0D) % 360.0D;
  }
  
  private boolean a(double paramDouble)
  {
    return (this.southwest.latitude <= paramDouble) && (paramDouble <= this.northeast.latitude);
  }
  
  private boolean b(double paramDouble)
  {
    if (this.southwest.longitude <= this.northeast.longitude) {
      return (this.southwest.longitude <= paramDouble) && (paramDouble <= this.northeast.longitude);
    }
    return (this.southwest.longitude <= paramDouble) || (paramDouble <= this.northeast.longitude);
  }
  
  public int hashCode()
  {
    return Util.a(new Object[] { this.southwest, this.northeast });
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {
      return true;
    }
    if (!(paramObject instanceof LatLngBounds)) {
      return false;
    }
    LatLngBounds localLatLngBounds = (LatLngBounds)paramObject;
    
    return (this.southwest.equals(localLatLngBounds.southwest)) && (this.northeast.equals(localLatLngBounds.northeast));
  }
  
  public String toString()
  {
    return Util.a(new String[] {
      Util.a("southwest", this.southwest), 
      Util.a("northeast", this.northeast) });
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    LatLngBoundsCreator.a(this, paramParcel, paramInt);
  }

    /**
     * 经纬度坐标矩形区域的生成器。
     */
  public static final class Builder
  {
    private double a = Double.POSITIVE_INFINITY;
    private double b = Double.NEGATIVE_INFINITY;
    private double c = Double.NaN;
    private double d = Double.NaN;

      /**
       *区域包含传入的坐标。区域将进行小范围延伸包含传入的坐标。 更准确来说，它会考虑向东或向西方向扩展（哪一种方法可能环绕地图），并选择最小扩展的方法。 如果两种方向得到的矩形区域大小相同，则会选择向东方向扩展。 例如：添加坐标（0，-179）和（1,179）将创建一个横穿180经线的区域。
       * @param point - 将坐标点包含在区域中。
       * @return 加入新坐标的builder对象。
       */
    public Builder include(LatLng point)
    {
      this.a = Math.min(this.a, point.latitude);
      this.b = Math.max(this.b, point.latitude);
      double d1 = point.longitude;
      if (Double.isNaN(this.c))
      {
        this.c = d1;
        this.d = d1;
      }
      else if (!a(d1))
      {
        //if (LatLngBounds.a(this.c, d1) < LatLngBounds.b(this.changeBearing, d1)) {
        if (LatLngBounds.c(this.c, d1) < LatLngBounds.d(this.d, d1)) {
          this.c = d1;
        } else {
          this.d = d1;
        }
      }
      return this;
    }
    
    private boolean a(double paramDouble)
    {
//      if (this.c <= this.d) {
//        return (this.c <= paramDouble) && (paramDouble <= this.d);
//      }
//      return (this.c <= paramDouble) || (paramDouble <= this.d);


      boolean v0 = true;
      boolean v1 = false;
      if(this.c <= this.d) {
        if(this.c <= paramDouble && paramDouble <= this.d) {
          return v0;
        }

        v0 = false;
      }
      else {
        if(this.c <= paramDouble || paramDouble <= this.d) {
          v1 = true;
        }
        v0 = v1;
      }
      return v0;
    }

      /**
       * 创建矩形区域。
       * @return 一个LatLngBounds对象。
       */
    public LatLngBounds build()
    {
      LMapThrowException.ThrowIllegalStateException(
              !Double.isNaN(this.c), "no included points");
      return new LatLngBounds(new LatLng(this.a, this.c, false), new LatLng(this.b, this.d, false));
    }
  }
}
