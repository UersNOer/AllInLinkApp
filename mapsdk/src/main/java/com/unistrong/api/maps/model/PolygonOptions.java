package com.unistrong.api.maps.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * 多边形选项类。
 */
public final class PolygonOptions
  implements Parcelable
{
  public static final PolygonOptionsCreator CREATOR = new PolygonOptionsCreator();
  private final List<LatLng> b;
  private float c = 10.0F;
  private int d = -16777216;
  private int e = -16777216;
  private float f = 0.0F;
  private boolean g = true;
  String a;

    /**
     * 创建PolygonOptions对象。
     */
  public PolygonOptions()
  {
    this.b = new ArrayList();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeTypedList(this.b);
    paramParcel.writeFloat(this.c);
    paramParcel.writeInt(this.d);
    paramParcel.writeInt(this.e);
    paramParcel.writeFloat(this.f);
    paramParcel.writeByte((byte)(this.g ? 1 : 0));
    paramParcel.writeString(this.a);
  }

    /**
     * 添加一个多边形边框的顶点。
     * @param paramLatLng - 多边形边框的顶点。
     * @return 加入边形边框顶点的PolygonOptions对象。
     */
  public PolygonOptions add(LatLng paramLatLng)
  {
    this.b.add(paramLatLng);
    return this;
  }

    /**
     * 添加多个多边形边框的顶点。
     * @param paramVarArgs - 多个多边形边框的顶点。
     * @return 加入多个新的多边形边框顶点的PolygonOptions对象
     */
  public PolygonOptions add(LatLng... paramVarArgs)
  {
    this.b.addAll(Arrays.asList(paramVarArgs));
    return this;
  }

    /**
     * 添加多个多边形边框的顶点
     * @param paramIterable - 多个多边形边框的顶点。
     * @return 加入多个新的多边形边框顶点的PolygonOptions对象
     */
  public PolygonOptions addAll(Iterable<LatLng> paramIterable)
  {
    Iterator localIterator = paramIterable.iterator();
    while (localIterator.hasNext())
    {
      LatLng localLatLng = (LatLng)localIterator.next();
      this.b.add(localLatLng);
    }
    return this;
  }

    /**
     * 设置多边形边框宽度，单位：像素。默认为10
     * @param paramFloat - 多边形边框宽度。
     * @return 设置新的边框宽度后PolygonOptions 对象
     */
  public PolygonOptions strokeWidth(float paramFloat)
  {
    this.c = paramFloat;
    return this;
  }

    /**
     * 设置多边形边框颜色，32位 ARGB格式，默认为黑色。
     * @param paramInt - 多边形边框颜色。
     * @return 设置新的边框颜色后PolygonOptions 对象
     */
  public PolygonOptions strokeColor(int paramInt)
  {
    this.d = paramInt;
    return this;
  }

    /**
     *设置多边形的填充颜色，32位ARGB格式。默认黑色。
     * @param paramInt - 多边形的填充颜色。
     * @return 设置新的填充颜色后PolygonOptions对象
     */
  public PolygonOptions fillColor(int paramInt)
  {
    this.e = paramInt;
    return this;
  }

    /**
     *设置多边形Z轴数值。
     * @param paramFloat - 多边形Z轴数值。
     * @return 设置新的Z轴数值后PolygonOptions 对象。
     */
  public PolygonOptions zIndex(float paramFloat)
  {
    this.f = paramFloat;
    return this;
  }

    /**
     *设置多边形的是否可见。默认为可见。
     * @return 设置新的可见属性后PolygonOptions 对象。
     */
  public PolygonOptions visible(boolean paramBoolean)
  {
    this.g = paramBoolean;
    return this;
  }

    /**
     * 返回多边形顶点坐标列表。
     * @return 多边形顶点坐标列表。
     */
  public List<LatLng> getPoints()
  {
    return this.b;
  }

    /**
     *返回多边形边框宽度。
     * @return 多边形边框宽度。
     */
  public float getStrokeWidth()
  {
    return this.c;
  }
    /**
     *返回多边形边框颜色。
     * @return 多边形边框颜色。
     */
  public int getStrokeColor()
  {
    return this.d;
  }
    /**
     *返回多边形填充颜色。
     * @return 多边形填充颜色。
     */
  public int getFillColor()
  {
    return this.e;
  }
    /**
     *返回多边形Z轴数值。
     * @return 多边形Z轴数值。
     */
  public float getZIndex()
  {
    return this.f;
  }
    /**
     *返回多边形是否可见。
     * @return True为可见；false为不可见。
     */
  public boolean isVisible()
  {
    return this.g;
  }
}
