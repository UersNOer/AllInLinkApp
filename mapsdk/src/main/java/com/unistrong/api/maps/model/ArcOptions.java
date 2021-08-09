package com.unistrong.api.maps.model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * 圆形选项类。
 */
public final class ArcOptions
  implements Parcelable
{
  public static final ArcOptionsCreator CREATOR = new ArcOptionsCreator();
  String a;
    /**
     * 弧线的起点坐标。
     */
  private LatLng startpoint;
    /**
     * 弧线的途径点坐标。
     */
  private LatLng passedpoint;
    /**
     * 弧线的终点坐标。
     */
  private LatLng endpoint;
    /**
     *边框宽度，单位：像素。
     */
  private float width = 10.0F;
    /**
     *边框颜色，ARGB格式。
     */
  private int color = -16777216;
    /**
     *Z轴数值。
     */
  private float zIndex = 0.0F;
    /**
     *弧形是否可见，默认可见。
     */
  private boolean visible = true;
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    Bundle localBundle = new Bundle();
    if (this.startpoint != null)
    {
      localBundle.putDouble("startlat", this.startpoint.latitude);
      localBundle.putDouble("startlng", this.startpoint.longitude);
    }
    if (this.passedpoint != null)
    {
      localBundle.putDouble("passedlat", this.passedpoint.latitude);
      localBundle.putDouble("passedlng", this.passedpoint.longitude);
    }
    if (this.endpoint != null)
    {
      localBundle.putDouble("endlat", this.endpoint.latitude);
      localBundle.putDouble("endlng", this.endpoint.longitude);
    }
    paramParcel.writeBundle(localBundle);
    paramParcel.writeFloat(this.width);
    paramParcel.writeInt(this.color);
    paramParcel.writeFloat(this.zIndex);
    paramParcel.writeByte((byte)(this.visible ? 1 : 0));
    paramParcel.writeString(this.a);
  }
  
  public int describeContents()
  {
    return 0;
  }

    /**
     *设置弧线的起终点和途径点。
     * @param startpoint - 弧线的起点坐标。
     * @param passedpoint - 弧线的途经点坐标。
     * @param endpoint - 弧线的终点坐标。
     * @return 弧线的起终点和途径点的 ArcOptions 对象。
     */
  public ArcOptions point(LatLng startpoint, LatLng passedpoint, LatLng endpoint)
  {
    this.startpoint = startpoint;
    this.passedpoint = passedpoint;
    this.endpoint = endpoint;
    return this;
  }

    /**
     *设置边框宽度，单位：像素。参数必须大于等于0，默认10。
     * @param width - 边框宽度，单位：像素。
     * @return 新边框宽度的 ArcOptions 对象。
     */
  public ArcOptions strokeWidth(float width)
  {
    this.width = width;
    return this;
  }

    /**
     *设置边框颜色，ARGB格式。如果设置透明，则边框不会被绘制。默认黑色。
     * @param color - 边框颜色，ARGB格式。
     * @return 设置新边框颜色的 ArcOptions 对象。
     */
  public ArcOptions strokeColor(int color)
  {
    this.color = color;
    return this;
  }

    /**
     *设置Z轴数值，默认为0。
     * @param zIndex - Z轴数值。
     * @return 设置新Z轴数值的 ArcOptions 对象。
     */
  public ArcOptions zIndex(float zIndex)
  {
    this.zIndex = zIndex;
    return this;
  }

    /**
     * 设置是否可见。
     * @param visible - true为可见，false为不可见。
     * @return 设置新可见属性的 ArcOptions 对象。
     */
  public ArcOptions visible(boolean visible)
  {
    this.visible = visible;
    return this;
  }

    /**
     * 返回弧形边框宽度，单位：像素。
     * @return 弧形边框宽度，单位：像素。
     */
  public float getStrokeWidth()
  {
    return this.width;
  }

    /**
     * 返回弧形边框颜色，ARGB格式。
     * @return 弧形边框颜色，ARGB格式。
     */
  public int getStrokeColor()
  {
    return this.color;
  }

    /**
     * 返回弧形Z轴的数值 。
     * @return 弧形Z轴的数值。
     */
  public float getZIndex()
  {
    return this.zIndex;
  }

    /**
     * 返回弧形是否可见。
     * @return true表示可见； false表示不可见。
     */
  public boolean isVisible()
  {
    return this.visible;
  }

    /**
     * 返回圆弧起点。
     * @return 圆弧起点。
     */
  public LatLng getStart()
  {
    return this.startpoint;
  }

    /**
     *返回圆弧中点。
     * @return 圆弧中点。
     */
  public LatLng getPassed()
  {
    return this.passedpoint;
  }

    /**
     *返回圆弧终点。
     * @return 圆弧终点。
     */
  public LatLng getEnd()
  {
    return this.endpoint;
  }
}
