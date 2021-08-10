package com.unistrong.api.maps.model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * 圆形选项类
 */
public final class CircleOptions
  implements Parcelable
{
  public static final CircleOptionsCreator CREATOR = new CircleOptionsCreator();
  String a;
  private LatLng centerPoint = null;
  private double radius = 0.0D;
  private float strokeWidth = 10.0F;
  private int strokeColor = -16777216;
  private int fillColor = 0;
  private float zIndex = 0.0F;
  private boolean visible = true;
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    Bundle localBundle = new Bundle();
    if (this.centerPoint != null)
    {
      localBundle.putDouble("lat", this.centerPoint.latitude);
      localBundle.putDouble("lng", this.centerPoint.longitude);
    }
    paramParcel.writeBundle(localBundle);
    paramParcel.writeDouble(this.radius);
    paramParcel.writeFloat(this.strokeWidth);
    paramParcel.writeInt(this.strokeColor);
    paramParcel.writeInt(this.fillColor);
    paramParcel.writeFloat(this.zIndex);
    paramParcel.writeByte((byte)(this.visible ? 1 : 0));
    paramParcel.writeString(this.a);
  }
  
  public int describeContents()
  {
    return 0;
  }

  /**
   * 设置圆心经纬度坐标,圆心经纬度坐标不能为null,圆心经纬度坐标无默认值。
   * @param point 圆心经纬度坐标。
   * @return 设置圆心经纬度坐标的CircleOptions对象。
     */
  public CircleOptions center(LatLng point)
  {
    this.centerPoint = point;
    return this;
  }

  /**
   * 设置圆的半径,单位为米。半径必须大于等于0。
   * @param radius 半径,单位米
   * @return 返回新半径的CricleOptioins对象。
     */
  public CircleOptions radius(double radius)
  {
    this.radius = radius;
    return this;
  }

  /**
   * 设置圆的边框宽度,单位像素。参数必须大于等于0
   * @param width 边框宽度,单位像素
   * @return 返回新边框宽度的CricleOptioins对象。
   */
  public CircleOptions strokeWidth(float width)
  {
    this.strokeWidth = width;
    return this;
  }

  /**
   * 设置圆的边框颜色,ARGB格式。如果设置透明,则边框不会被绘制。默认为黑色
   * @param color 设置边框颜色,ARGB格式
   * @return 返回新边框颜色的CricleOptioins对象。
   */
  public CircleOptions strokeColor(int color)
  {
    this.strokeColor = color;
    return this;
  }

  /**
   * 设置圆的填充颜色。填充颜色是绘制边框以内部分的颜色,ARGB格式。默认透明
   * @param color 填充颜色ARGB格式
   * @return 返回填充颜色的CricleOptioins对象。
   */
  public CircleOptions fillColor(int color)
  {
    this.fillColor = color;
    return this;
  }

  /**
   * 设置圆的Z轴数值,默认为0
   * @param zIndex z轴数值
   * @return 返回新Z轴数值的CricleOptioins对象。
   */
  public CircleOptions zIndex(float zIndex)
  {
    this.zIndex = zIndex;
    return this;
  }

  /**
   * 设置圆的可见性。
   * @param visible true,可见;false,不可见
   * @return 返回新可见属性的CricleOptioins对象。
   */
  public CircleOptions visible(boolean visible)
  {
    this.visible = visible;
    return this;
  }

  /**
   * 获取圆心经纬度坐标
   * @return 圆心经纬度坐标
     */
  public LatLng getCenter()
  {
    return this.centerPoint;
  }
  /**
  * 获取圆的半径
  * @return 圆的半径
  */
  public double getRadius()
  {
    return this.radius;
  }
  /**
   * 获取圆的边框宽度
   * @return 圆的边框宽度
   */
  public float getStrokeWidth()
  {
    return this.strokeWidth;
  }
  /**
   * 获取圆的边框颜色
   * @return 圆的边框颜色
   */
  public int getStrokeColor()
  {
    return this.strokeColor;
  }
  /**
   * 获取圆的填充颜色
   * @return 圆的填充颜色
   */
  public int getFillColor()
  {
    return this.fillColor;
  }
  /**
   * 获取圆的Z轴数值
   * @return 圆的Z轴数值
   */
  public float getZIndex()
  {
    return this.zIndex;
  }
  /**
   * 获取圆的可见性
   * @return true,可见;false,不可见
   */
  public boolean isVisible()
  {
    return this.visible;
  }
}
