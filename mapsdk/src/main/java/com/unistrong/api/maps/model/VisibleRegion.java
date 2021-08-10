package com.unistrong.api.maps.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.unistrong.api.mapcore.util.Util;

/**
 * 在地图的可视区域中定义多边形四边的四个点坐标包含的区域是可见的。这个多边形是不规则四边形， 而不是矩形，因为可视区域是可以倾斜的。如果可视区域正好在可视区域中心的上方，形状就是矩形； 如果可视区域倾斜，那么形状就是梯形，距离视野最近的边最短。
 */
public final class VisibleRegion
  implements Parcelable
{
  public static final VisibleRegionCreator CREATOR = new VisibleRegionCreator();
  private final int a;
    /**
     * 区域离视野近的左顶点。
     */
  public final LatLng nearLeft;
    /**
     * 区域离视野近的右顶点。
     */
  public final LatLng nearRight;
    /**
     * 区域离视野远的左顶点。
     */
  public final LatLng farLeft;
    /**
     * 区域离视野远的右顶点。
     */
  public final LatLng farRight;
    /**
     * 最小边界框，包含在这个类里定义的可见区域。
     */
  public final LatLngBounds latLngBounds;
  
  VisibleRegion(int paramInt, LatLng paramLatLng1, LatLng paramLatLng2, LatLng paramLatLng3, LatLng paramLatLng4, LatLngBounds paramLatLngBounds)
  {
    this.a = paramInt;
    this.nearLeft = paramLatLng1;
    this.nearRight = paramLatLng2;
    this.farLeft = paramLatLng3;
    this.farRight = paramLatLng4;
    this.latLngBounds = paramLatLngBounds;
  }

    /**
     * 构造函数
     * @param nearLeft - 区域离视野近的左顶点。
     * @param nearRight - 区域离视野近的右顶点。
     * @param farLeft - 区域离视野远的左顶点。
     * @param farRight - 区域离视野远的右顶点。
     * @param latLngBounds - 最小边界框，包含在这个类里定义的可见区域。
     */
  public VisibleRegion(LatLng nearLeft, LatLng nearRight, LatLng farLeft, LatLng farRight, LatLngBounds latLngBounds)
  {
    this(1, nearLeft, nearRight, farLeft, farRight, latLngBounds);
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    VisibleRegionCreator.a(this, paramParcel, paramInt);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int hashCode()
  {
    return Util.a(new Object[] { this.nearLeft, this.nearRight, this.farLeft, this.farRight, this.latLngBounds });
  }
  
  int a()
  {
    return this.a;
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {
      return true;
    }
    if (!(paramObject instanceof VisibleRegion)) {
      return false;
    }
    VisibleRegion localVisibleRegion = (VisibleRegion)paramObject;
    
    return (this.nearLeft.equals(localVisibleRegion.nearLeft)) && (this.nearRight.equals(localVisibleRegion.nearRight)) && (this.farLeft.equals(localVisibleRegion.farLeft)) && (this.farRight.equals(localVisibleRegion.farRight)) && (this.latLngBounds.equals(localVisibleRegion.latLngBounds));
  }
  
  public String toString()
  {
    return Util.a(new String[] {
      Util.a("nearLeft", this.nearLeft), 
      Util.a("nearRight", this.nearRight), 
      Util.a("farLeft", this.farLeft), 
      Util.a("farRight", this.farRight), 
      Util.a("latLngBounds", this.latLngBounds) });
  }
}
