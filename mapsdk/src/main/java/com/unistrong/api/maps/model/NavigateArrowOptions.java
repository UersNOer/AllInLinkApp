package com.unistrong.api.maps.model;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * 导航箭头的选项类。
 */
public final class NavigateArrowOptions
  implements Parcelable
{
  public static final NavigateArrowOptionsCreator CREATOR = new NavigateArrowOptionsCreator();
  private final List<LatLng> coordsArray;
  private float width = 10.0F;
  private int topColor = Color.argb(221, 87, 235, 204);
  private int sideColor = Color.argb(170, 0, 172, 146);
  private float zIndex = 0.0F;
  private boolean visible = true;
  String a;

    /**
     * 构造函数。
     */
  public NavigateArrowOptions()
  {
    this.coordsArray = new ArrayList();
  }

    /**
     * 追加一个顶点到箭头的终点。
     * @param point - 要添加的顶点。
     * @return 追加一个顶点到箭头终点的 NavigateArrowOptions 对象。
     */
  public NavigateArrowOptions add(LatLng point)
  {
    this.coordsArray.add(point);
    return this;
  }

    /**
     * 追加一批顶点到箭头终点。
     * @param points - 要添加的顶点集合。
     * @return 追加一批顶点到箭头终点的 NavigateArrowOptions 对象。
     */
  public NavigateArrowOptions add(LatLng... points)
  {
    this.coordsArray.addAll(Arrays.asList(points));
    return this;
  }

    /**
     * 追加一批顶点到箭头终点。
     * @param points  - 要添加的顶点集合。
     * @return 追加一批顶点到箭头终点的 NavigateArrowOptions 对象。
     */
  public NavigateArrowOptions addAll(Iterable<LatLng> points)
  {
    Iterator localIterator = points.iterator();
    while (localIterator.hasNext())
    {
      LatLng localLatLng = (LatLng)localIterator.next();
      this.coordsArray.add(localLatLng);
    }
    return this;
  }

    /**
     *设置箭头的宽度，单位像素。默认为10。
     * @param width  - 要设置的宽度。
     * @return 设置箭头新宽度的 NavigateArrowOptions 对象。
     */
  public NavigateArrowOptions width(float width)
  {
    this.width = width;
    return this;
  }

    /**
     * 设置箭头的顶颜色，需要传入32位的ARGB格式。默认为黑色( 0xff000000)。
     * @param color - 要设置的颜色。
     * @return 设置箭头顶颜色的 NavigateArrowOptions 对象。
     */
  public NavigateArrowOptions topColor(int color)
  {
    this.topColor = color;
    return this;
  }

    /**
     * 设置箭头的侧边颜色，需要传入32位的ARGB格式。默认为黑色( 0xff000000)。
     * @param color  - 要设置的颜色。
     * @return 设置箭头侧边颜色的 NavigateArrowOptions 对象。
     */
  public NavigateArrowOptions sideColor(int color)
  {
    this.sideColor = color;
    return this;
  }

    /**
     * 设置箭头Z轴的值。
     * @param zIndex - 要设置的Z轴的值。
     * @return 设置箭头新Z轴值的PolylineOptions对象。
     */
  public NavigateArrowOptions zIndex(float zIndex)
  {
    this.zIndex = zIndex;
    return this;
  }

    /**
     *设置箭头的可见性。默认为可见。
     * @param isVisible - 一个表示箭头是否可见的布尔值，true表示可见，false表示不可见。
     * @return 设置新可见属性的PolylineOptions对象。
     */
  public NavigateArrowOptions visible(boolean isVisible)
  {
    this.visible = isVisible;
    return this;
  }

    /**
     * 返回 Options对象的顶点坐标列表。
     * @return 线段的顶点坐标列表。
     */
  public List<LatLng> getPoints()
  {
    return this.coordsArray;
  }

    /**
     * 返回Options对象的线段宽度。
     * @return 线段的宽度。
     */
  public float getWidth()
  {
    return this.width;
  }

    /**
     * 返回箭头覆盖物的顶颜色,ARGB格式。
     * @return 线段的颜色ARGB格式。
     */
  public int getTopColor()
  {
    return this.topColor;
  }

  /**
   * 返回箭头覆盖物的侧边颜色,ARGB格式。
   * @return 线段的颜色ARGB格式。
   */
  public int getSideColor()
  {
    return this.sideColor;
  }

    /**
     *返回Options对象的Z轴值。
     * @return Z轴值。
     */
  public float getZIndex()
  {
    return this.zIndex;
  }

    /**
     *返回Options对象的线段可见属性。
     * @return True：为可见; false：不可见。
     */
  public boolean isVisible()
  {
    return this.visible;
  }

  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeTypedList(this.coordsArray);
    
    paramParcel.writeFloat(this.width);
    paramParcel.writeInt(this.topColor);
    paramParcel.writeInt(this.sideColor);
    paramParcel.writeFloat(this.zIndex);
    paramParcel.writeByte((byte)(this.visible ? 1 : 0));
    paramParcel.writeString(this.a);
  }
}
