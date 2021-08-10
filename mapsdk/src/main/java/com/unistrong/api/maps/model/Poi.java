package com.unistrong.api.maps.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Poi 是指底图上的一个自带Poi点。
 * 一个Poi点有如下属性:
 * 名称：Poi点在底图上显示的名称
 * 坐标：Poi点在底图上的经纬度坐标
 */
public class Poi
  implements Parcelable
{
  public static final PoiCreator CREATOR = new PoiCreator();
  private final String a;
  private final LatLng b;
  private final String c;
  
  public Poi(String name, LatLng coordinate, String poiid)
  {
    this.a = name;
    this.b = coordinate;
    this.c = poiid;
  }

    /**
     * 返回Poi点的名称。
     * @return Poi点的名称。
     */
  public String getName()
  {
    return this.a;
  }

    /**
     *返回Poi点的坐标。
     * @return 返回坐标。
     */
  public LatLng getCoordinate()
  {
    return this.b;
  }

    /**
     *返回Poi的poiid，它是Poi的唯一标识。
     * @return 返回poiid。
     */
  public String getPoiId()
  {
    return this.c;
  }

  public int describeContents()
  {
    return 0;
  }

  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {
      return true;
    }
    if (!(paramObject instanceof Poi)) {
      return false;
    }
    Poi localPoi = (Poi)paramObject;
    
    return (localPoi.getName().equals(this.a)) && (localPoi.getCoordinate().equals(this.b)) && (localPoi.getPoiId().equals(this.c));
  }
  public String toString()
  {
    return "poiid " + this.c + " name:" + this.a + "  coordinate:" + this.b.toString();
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(this.a);
    paramParcel.writeParcelable(this.b, paramInt);
    paramParcel.writeString(this.c);
  }
}
