package com.unistrong.api.maps.model;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * 定位（当前位置）的绘制样式类。
 */
public class MyLocationStyle
  implements Parcelable
{
    /**
     * 使用的位置图标
     */
  private BitmapDescriptor icon;
    /**
     * 锚点水平范围的比例，建议传入0 到1 之间的数值。
     */
  private float u = 0.5F;
    /**
     * 锚点垂直范围的比例，建议传入0 到1 之间的数值。
     */
  private float v = 0.5F;
  private int color = Color.argb(100, 0, 0, 180);
  private int strokeColor = Color.argb(255, 0, 0, 220);
  private float width = 1.0F;

    /**
     * 设置定位（当前位置）的icon图标。
     * @param myLocationIcon - 使用的位置图标。
     * @return 一个设置了定位图标的MyLocationStyle对象。
     */
  public MyLocationStyle myLocationIcon(BitmapDescriptor myLocationIcon)
  {
    this.icon = myLocationIcon;
    return this;
  }

    /**
     * 设置定位图标的锚点。 锚点是定位图标接触地图平面的点。图标的左顶点为（0,0）点，右底点为（1,1）点
     * @param u - 锚点水平范围的比例，建议传入0 到1 之间的数值。
     * @param v - 锚点垂直范围的比例，建议传入0 到1 之间的数值。
     * @return  定位图标的锚点。
     */
  public MyLocationStyle anchor(float u, float v)
  {
    this.u = u;
    this.v = v;
    return this;
  }

    /**
     * 设置圆形区域（以定位位置为圆心，定位半径的圆形区域）的填充颜色。
     * @param color - 圆形区域的填充颜色。
     * @return 一个设置了定位圆形区域填充颜色的MyLocationStyle对象。
     */
  public MyLocationStyle radiusFillColor(int color)
  {
    this.color = color;
    return this;
  }

    /**
     * 设置圆形区域（以定位位置为圆心，定位半径的圆形区域）的边框颜色。
     * @param color - 圆形区域的边框颜色。
     * @return 一个设置了定位圆形区域边框颜色的MyLocationStyle对象。
     */
  public MyLocationStyle strokeColor(int color)
  {
    this.strokeColor = color;
    return this;
  }

    /**
     * 设置圆形区域（以定位位置为圆心，定位半径的圆形区域）的边框宽度。
     * @param width - 圆形区域的边框宽度。
     * @return 一个设置了定位圆形区域边框宽度的MyLocationStyle对象。
     */
  public MyLocationStyle strokeWidth(float width)
  {
    this.width = width;
    return this;
  }

    /**
     * 得到当前位置的图标。
     * @return 当前位置的图标。
     */
  public BitmapDescriptor getMyLocationIcon()
  {
    return this.icon;
  }

    /**
     * 得到锚点横坐标方向的偏移量。
     * @return 锚点横坐标方向的偏移量。
     */
  public float getAnchorU()
  {
    return this.u;
  }

    /**
     * 得到锚点纵坐标方向的偏移量。
     * @return 锚点纵坐标方向的偏移量。
     */
  public float getAnchorV()
  {
    return this.v;
  }

    /**
     * 得到圆形区域（以定位位置为圆心，定位半径的圆形区域）的填充颜色值。
     * @return 圆形区域的填充颜色值。
     */
  public int getRadiusFillColor()
  {
    return this.color;
  }

    /**
     *得到圆形区域（以定位位置为圆心，定位半径的圆形区域）边框的颜色值。
     * @return 圆形区域边框的颜色值。
     */
  public int getStrokeColor()
  {
    return this.strokeColor;
  }

    /**
     *得到圆形区域（以定位位置为圆心，定位半径的圆形区域）边框的宽度。
     * @return 圆形区域边框的宽度。
     */
  public float getStrokeWidth()
  {
    return this.width;
  }

  public int describeContents()
  {
    return 0;
  }

  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeParcelable(this.icon, paramInt);
    paramParcel.writeFloat(this.u);
    paramParcel.writeFloat(this.v);
    paramParcel.writeInt(this.color);
    paramParcel.writeInt(this.strokeColor);
    paramParcel.writeFloat(this.width);
  }
}
