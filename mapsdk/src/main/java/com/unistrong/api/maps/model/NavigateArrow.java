package com.unistrong.api.maps.model;

import android.os.RemoteException;
import com.unistrong.api.mapcore.INavigateArrowDelegateDecode;
import java.util.List;

/**
 * 一个导航箭头是多个连贯点的集合，拥有以下属性：
 * 顶点
 * 箭头是由两个顶点之间连贯的点构成的。如果两个顶点相同，则一个箭头将闭合。
 * 宽度
 * 宽度是单位是像素。宽度是可视区域的缩放级别无关。默认为10。
 * 顶颜色
 * 箭头的顶颜色是ARGB格式，颜色格式可以参考android.graphics.Color。默认是黑色。
 * 侧边颜色
 * 箭头的侧边颜色是ARGB格式，颜色格式可以参考android.graphics.Color。默认是黑色。
 * Z轴
 * Z轴是控制地图覆盖物（overlay）之间的绘制层次的参数。这个参数能够控制Circles、Polygons、Polyline的绘制层次，但不会影响marker。Z轴数值越大的覆盖物（overlay）将会绘制在更上层。如果两个及两个以上覆盖物（overlay）的Z轴数值相同，则最后的绘制结果是随机的。覆盖物（overlay）的默认为Z轴为0。
 * 可见
 * 这个属性表示了箭头是否可以显示在地图上。如果设置为不可见，则绘制地图时不会绘制此箭头，但其他属性不受影响。默认为可见
 */
public class NavigateArrow
{
  private final INavigateArrowDelegateDecode arrowDelegate;
  
  public NavigateArrow(INavigateArrowDelegateDecode paramah)
  {
    this.arrowDelegate = paramah;
  }

    /**
     * 从地图上删除当前箭头。箭头删除后，被删除的箭头的方法均无效。
     */
  public void remove()
  {
    try
    {
      this.arrowDelegate.remove();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 返回箭头对象的id。箭头的id用于区分在同一个地图里面不同的箭头对象。
     * @return 箭头对象的id。
     */
  public String getId()
  {
    try
    {
      return this.arrowDelegate.getId();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }
  /**
   * 设置当前箭头的侧边颜色。
   * @param color - 侧边颜色，ARGB格式。
   */
  public void setSideColor(int color)
  {
    try
    {
      this.arrowDelegate.setSideColor(color);
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }
  /**
   * 返回箭头的顶部颜色。
   * @return 线段颜色的ARGB格式。
   */
  public int getSideColor()
  {
    try
    {
      return this.arrowDelegate.getSideColor();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 设置箭头的顶点。如果调用此方法后修改了List的值将不会影响到箭头。
     * @param points - 箭头顶点的经纬度集合。
     */
  public void setPoints(List<LatLng> points)
  {
    try
    {
      this.arrowDelegate.setPoints(points);
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 返回当前箭头的顶点列表。如果调用此方法后，线段的箭头发生了变化，列表的值不会随之改变。 如果调用此方法获得顶点坐标后并且修改了顶点坐标，也不会作用到当前线程。如果需要修改箭头的顶点值必须调用setPoints(List)。
     * @return 返回当前箭头的顶点列表。
     */
  public List<LatLng> getPoints()
  {
    try
    {
      return this.arrowDelegate.getPoints();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 设置箭头的宽度。
     * @param width - 箭头宽度的像素。
     */
  public void setWidth(float width)
  {
    try
    {
      this.arrowDelegate.setWidth(width);
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 返回箭头的宽度值。
     * @return 箭头的宽度值。
     *
     */
  public float getWidth()
  {
    try
    {
      return this.arrowDelegate.getWidth();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 设置箭头的顶部颜色。
     * @param color  - 颜色的ARGB格式。
     */
  public void setTopColor(int color)
  {
    try
    {
      this.arrowDelegate.setTopColor(color);
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 返回当前箭头的顶颜色。
     * @return 当前箭头的顶颜色。
     */
  public int getTopColor() {
    try {
      return this.arrowDelegate.getTopColor();
    } catch (RemoteException localRemoteException) {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 设置箭头Z轴的值。
     * @param zIndex - 箭头Z轴的值。
     */
  public void setZIndex(float zIndex)
  {
    try
    {
      this.arrowDelegate.setZIndex(zIndex);
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 返回当前箭头z轴的值。
     * @return 当前箭头z轴的值。
     */
  public float getZIndex()
  {
    try
    {
      return this.arrowDelegate.getZIndex();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 设置箭头的可见属性。当不可见时，箭头不会被绘制。
     * @param visible - 传入true箭头可见，false为不可见。
     */
  public void setVisible(boolean visible)
  {
    try
    {
      this.arrowDelegate.setVisible(visible);
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 返回箭头的可见属性。
     * @return 箭头的可见属性。
     */
  public boolean isVisible()
  {
    try
    {
      return this.arrowDelegate.isVisible();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }
  
  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof NavigateArrow)) {
      return false;
    }
    try
    {
      return this.arrowDelegate.equals(((NavigateArrow) paramObject).arrowDelegate);
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
      return this.arrowDelegate.hashCodeRemote();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }
}
