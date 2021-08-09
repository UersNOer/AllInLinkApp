package com.unistrong.api.maps.model;

import android.os.RemoteException;
import com.unistrong.api.mapcore.IArcDelegateDecode;

/**
 * 定义了在地图上绘制弧形的类。
 * 它有以下属性:
 * 边框宽度
 *     弧形边框的宽度。这个值设置后不会受到可视区域缩放级别的影响。默认为10。
 * 边框颜色
 *     边框颜色是一个ARGB格式，ARGB请参考android.graphics.Color。默认为黑色。
 * Z轴
 *     Z轴是控制覆盖物重复区域的绘制顺序的值。Z轴较大的覆盖物会绘制在Z轴较小的覆盖物上面。如果两个覆盖物的Z轴数值相同，则覆盖情况将随机。默认值为0。
 * 可见性
 *     标示弧形是否可见。如果可见性为否，则不会被绘制。
 * 弧形方法必须在主线程中调用，否则会抛出IllegalStateException。
 */
public final class Arc
{
  private final IArcDelegateDecode a;
  
  public Arc(IArcDelegateDecode iArcDelegateDecode)
  {
    this.a = iArcDelegateDecode;
  }

    /**
     * 删除从地图对象里圆弧。
     */
  public void remove()
  {
    try
    {
      this.a.remove();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 返回弧形的 Id。Id 是覆盖物在所属的地图对象里的唯一标识。
     * @return 弧形的 Id。Id 是覆盖物在所属的地图对象里的唯一标识。
     */
  public String getId()
  {
    try
    {
      return this.a.getId();
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
      this.a.setStrokeWidth(width);
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 返回形边框宽度。
     * @return 形边框宽度。
     */
  public float getStrokeWidth()
  {
    try
    {
      return this.a.getStrokeWidth();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     *设置边框颜色。
     * @param color - 边框颜色。
     */
  public void setStrokeColor(int color)
  {
    try
    {
      this.a.setStrokeColor(color);
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 返回边框颜色。
     * @return 边框颜色，ARGB格式。
     */
  public int getStrokeColor()
  {
    try
    {
      return this.a.getStrokeColor();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     *设置Z轴数值。设置圆弧的Z轴数值。数值更高的多边形将绘制在数值较低的上面。
     * @param zIndex - Z轴数值。设置圆弧的Z轴数值。数值更高的多边形将绘制在数值较低的上面。
     */
  public void setZIndex(float zIndex)
  {
    try
    {
      this.a.setZIndex(zIndex);
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     *返回圆弧Z轴数值。
     * @return 圆弧Z轴数值。
     */
  public float getZIndex()
  {
    try
    {
      return this.a.getZIndex();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     *设置弧形的可见属性。当不可见时，弧形将不会被绘制。默认值为 true。
     * @param visible -  true为可见；false不可见。
     */
  public void setVisible(boolean visible)
  {
    try
    {
      this.a.setVisible(visible);
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     *返回圆弧是否可见。
     * @return 圆弧是否可见。
     */
  public boolean isVisible()
  {
    try
    {
      return this.a.isVisible();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }
  
  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof Arc)) {
      return false;
    }
    try
    {
      return this.a.equals(((Arc) paramObject).a);
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
      return this.a.hashCodeRemote();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }
}
