package com.unistrong.api.maps.model;

import android.os.RemoteException;
import com.unistrong.api.mapcore.ICircleDelegate;

/**
 * 定义了在地图上绘制圆形的类。
 * 圆形对象有以下属性：
 *    圆心：圆形圆心的经纬度。
 *    半径： 圆形半径，单位：米。这个值应该大于等于0。
 *    边框宽度： 圆形边框的宽度。这个值设置后不会受到可视区域缩放级别的影响。默认为10。
 *    边框颜色：圆形边框的颜色，ARGB格式。默认为黑色。
 *    填充颜色：圆形填充的颜色，ARGB格式。默认为透明。
 *    Z轴：Z轴是控制覆盖物重复区域的绘制顺序的值。Z轴较大的覆盖物会绘制在Z轴较小的覆盖物上面。如果两个覆盖物的Z轴数值相同，则覆盖情况将随机。默认值为0。
 *    可见属性：标示多边形是否可见。如果可见性为否，则不会被绘制。 圆形方法必须在主线程中调用，否则会抛出IllegalStateException。
 */
public final class Circle
{
  private final ICircleDelegate circle;
  
  public Circle(ICircleDelegate circle)
  {
    this.circle = circle;
  }

    /**
     * 删除从地图对象里圆形。
     */
  public void remove()
  {
    try
    {
      this.circle.remove();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 返回圆形的id。
     * @return 圆形的id。此id是覆盖物在所属的地图对象里的唯一标识。
     */
  public String getId()
  {
    try
    {
      return this.circle.getId();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 设置圆形的圆心经纬度坐标，参数不能为null，无默认值。
     * @param center - 圆形的圆心经纬度坐标。
     */
  public void setCenter(LatLng center)
  {
    try
    {
      this.circle.setCenter(center);
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 返回圆形的圆心经纬度坐标。
     * @return 圆形的圆心经纬度坐标。
     */
  public LatLng getCenter()
  {
    try
    {
      return this.circle.getCenter();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 圆形半径，单位米。半径必须大于等于0。
     * @param radius - 圆形半径，单位米。
     */
  public void setRadius(double radius)
  {
    try
    {
      this.circle.setRadius(radius);
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     *返回圆形半径，单位米。
     * @return 圆形半径，单位米。
     */
  public double getRadius()
  {
    try
    {
      return this.circle.getRadius();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 设置边框宽度，单位像素。参数必须大于等于0。如果为0则不会绘制边框。默认为10。
     * @param width  - 边框宽度，单位像素。
     */
  public void setStrokeWidth(float width)
  {
    try
    {
      this.circle.setStrokeWidth(width);
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 返回圆形边框宽度。
     * @return 圆形边框宽度，单位像素。
     */
  public float getStrokeWidth()
  {
    try
    {
      return this.circle.getStrokeWidth();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 设置圆形边框颜色。
     * @param color  - 边框颜色，ARGB格式。
     */
  public void setStrokeColor(int color)
  {
    try
    {
      this.circle.setStrokeColor(color);
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 返回圆形边框颜色。
     * @return 圆形边框颜色，ARGB格式。
     */
  public int getStrokeColor()
  {
    try
    {
      return this.circle.getStrokeColor();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 设置填充颜色。填充颜色是绘制边框以内部分的颜色，ARGB格式。
     * @param color  - 填充颜色ARGB格式。
     */
  public void setFillColor(int color)
  {
    try
    {
      this.circle.setFillColor(color);
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 返回圆形填充颜色。
     * @return 圆形填充颜色。
     */
  public int getFillColor()
  {
    try
    {
      return this.circle.getFillColor();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 设置Z轴数值。设置圆形的Z轴数值。数值更高的圆将绘制在数值较低的上面。
     * @param zIndex - Z轴数值。
     */
  public void setZIndex(float zIndex)
  {
    try
    {
      this.circle.setZIndex(zIndex);
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 返回圆形Z轴数值。
     * @return 圆形Z轴数值。
     */
  public float getZIndex()
  {
    try
    {
      return this.circle.getZIndex();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 设置圆形的可见属性。如果圆形不可见属性，则不会被绘制在地图上。默认为true。
     * @param visible  - true为可见；false不可见。
     */
  public void setVisible(boolean visible)
  {
    try
    {
      this.circle.setVisible(visible);
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 返回圆形是否可见。
     * @return True为圆形可见，false为不可见。
     */
  public boolean isVisible()
  {
    try
    {
      return this.circle.isVisible();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }
  
  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof Circle)) {
      return false;
    }
    try
    {
      return this.circle.equals(((Circle) paramObject).circle);
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }
  
  public int hashCode()
  {
    try
    {
      return this.circle.hashCodeRemote();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 判断圆形是否包含传入的经纬度点。
     * @param latLng - 经纬度点。
     * @return true 包含，false 为不包含。
     */
  public boolean contains(LatLng latLng)
  {
    try
    {
      return this.circle.contains(latLng);
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }
}
