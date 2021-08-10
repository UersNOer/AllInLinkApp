package com.unistrong.api.maps.model;

import android.os.RemoteException;
import com.unistrong.api.mapcore.IPolygonDelegate;
import java.util.List;

/**
 * 这个定义了在地图表面的多边形。一个多边形可以凸面体，也可是凹面体。
 *
 * 它有以下属性:
 *  边框
 *  一个多边开的边框由一系列的顺时针或逆时针顶点组成。边框不需要声明起点和终点一致。如果边框的起点与终点不一致，API会自动将它封闭。
 *  边框宽度
 *  边框的宽度以像素为单位，不会受可视区域缩放级别的影响。默认为10。
 *  边框颜色
 *  边框颜色是一个ARGB格式，ARGB请参考android.graphics.Color。默认为黑色。
 *  填充颜色
 *  填充颜色是一个ARGB格式，ARGB格式可参考android.graphics.Color。默认是透明的。如果多边形的几何图形不正确，则填充颜色不会被绘制。
 *  Z轴
 *  Z轴是控制覆盖物重复区域的绘制顺序的值。Z轴较大的覆盖物会在绘制在Z轴较小的覆盖物上面。如果两个覆盖物的Z轴数值相同，则覆盖情况将随机。默认为0。
 *  可见性
 *  标示多边形是否可见。如果可见性为否，则不会被绘制。 多边形方法必须在主线程中调用，否则会抛出IllegalStateException。
 */
public final class Polygon
{
  private IPolygonDelegate polygon;
  
  public Polygon(IPolygonDelegate paramaj)
  {
    this.polygon = paramaj;
  }

    /**
     * 从地图上删除当前多边形。
     */
  public void remove()
  {
    try
    {
      this.polygon.remove();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 返回多边形的Id。
     * @return 多边形Id。Id是区分一个地图里不同多边形的标识。
     */
  public String getId()
  {
    try
    {
      return this.polygon.getId();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 设置多边形的顶点。设置完成后再修改顶点列表的值，不会影响多边形。
     * @param points  - 多边形顶点列表。
     */
  public void setPoints(List<LatLng> points)
  {
    try
    {
      this.polygon.a(points);
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 返回多边形顶点的列表。如果调用此方法后，多边形的顶点发生了变化，列表的值不会随之改变。如果调用此方法获得顶点坐标后并且修改了顶点坐标， 也不会作用到当前多边形。如果需要修改多边形的顶点值必须调用setPoints(List)。
     * @return 多边形顶点的列表。
     */
  public List<LatLng> getPoints()
  {
    try
    {
      return this.polygon.getPoints();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 设置多边形边框颜色。
     * @param width  - 多边形边框宽度，单位像素。
     */
  public void setStrokeWidth(float width)
  {
    try
    {
      this.polygon.setStrokeWidth(width);
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 返回分框宽度。
     * @return 宽度的像素值。
     */
  public float getStrokeWidth()
  {
    try
    {
      return this.polygon.getStrokeWidth();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 设置多边形边框颜色。
     * @param color - 边框颜色，ARGB格式。
     */
  public void setStrokeColor(int color)
  {
    try
    {
      this.polygon.setStrokeColor(color);
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 返回多边形的边框颜色。
     * @return ARGB格式。
     */
  public int getStrokeColor()
  {
    try
    {
      return this.polygon.getStrokeColor();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 设置多边形填充颜色。
     * @param color  - 填充颜色的ARGB格式。
     */
  public void setFillColor(int color)
  {
    try
    {
      this.polygon.setFillColor(color);
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 返回多边形填充颜色。
     * @return 多边形填充颜色，ARGB格式。
     */
  public int getFillColor()
  {
    try
    {
      return this.polygon.getFillColor();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 设置多边形的Z轴数值。数值更高的多边形将绘制在数值较低的上面。
     * @param zIndex - 多边形Z轴数值。
     */
  public void setZIndex(float zIndex)
  {
    try
    {
      this.polygon.setZIndex(zIndex);
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 返回多边形的Z轴值。
     * @return 多边形的Z轴值。
     */
  public float getZIndex()
  {
    try
    {
      return this.polygon.getZIndex();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 设置多边形的可见属性。当不可见时，多边形将不会被绘制，但是其他属性将会保存。
     * @param visible  - true为可见；false不可见。
     */
  public void setVisible(boolean visible)
  {
    try
    {
      this.polygon.setVisible(visible);
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 返回多边形的可见属性。
     * @return  多边形的可见属性。
     */
  public boolean isVisible()
  {
    try
    {
      return this.polygon.isVisible();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 判断多边形是否包含传入的经纬度点。
     * @param latLng  - 经纬度点。
     * @return True 多边形包含这个点,false 多边形未包含这个点。
     */
  public boolean contains(LatLng latLng)
  {
    try
    {
      return this.polygon.contains(latLng);
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }
  
  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof Polygon)) {
      return false;
    }
    try
    {
      return this.polygon.equals(((Polygon) paramObject).polygon);
    }
    catch (RemoteException localRemoteException)
    {
      localRemoteException.printStackTrace();
    }
    return false;
  }
  
  public int hashCode()
  {
    try
    {
      return this.polygon.hashCodeRemote();
    }
    catch (RemoteException localRemoteException) {}
    return super.hashCode();
  }
}
