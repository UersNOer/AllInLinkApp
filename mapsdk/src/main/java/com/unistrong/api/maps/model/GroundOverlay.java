package com.unistrong.api.maps.model;

import android.os.RemoteException;
import com.unistrong.api.mapcore.IGroundOverlayDelegateDecode;

/**
 * 图片层是一张图片以合适的大小贴在地图上。
 *  位置: 可以通过设置中心点或者图片区域来确定图片层的位置。在添加图片层之前，如果不设置中心点或者图片区域，会报IllegalArgumentException异常。
 *  图片： 给图片层的贴图。在添加图片层之前，如果没有设置图片，会报IllegalArgumentException 异常。
 *  角度： 图片从正北开始，顺时针方向旋转，中心点为锚点。
 *  Z轴 ： Z轴是控制地图覆盖物（overlay）之间的绘制层次的参数。这个参数能够控制Circles、Polygons、Polyline的绘制层次，但不会影响marker。Z轴数值越大的覆盖物（overlay）将会绘制在更上层。如果两个及两个以上覆盖物（overlay）的Z轴数值相同，则最后的绘制结果是随机的 。覆盖物（overlay）的默认为Z轴为0。
 *  可见：这个属性表示了线段是否可以显示在地图上。如果设置为不可见，则绘制地图时不会绘制此线段，但其他属性不受影响。默认为可见。
 *  线段属性的修改必须在主线程里调用，否则将会抛出IllegalStateException。
 */
public final class GroundOverlay
{
  private IGroundOverlayDelegateDecode groundoverlay;
  
  public GroundOverlay(IGroundOverlayDelegateDecode paramae)
  {
    this.groundoverlay = paramae;
  }

    /**
     * 从地图中删除此图片层，在图片层删除后，它的所有方法不可用。
     */
  public void remove()
  {
    try
    {
      this.groundoverlay.remove();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 得到图片层的ID，一个地图上的每个图片层都有自己的ID。
     * @return 当前图片层的ID。
     */
  public String getId()
  {
    try
    {
      return this.groundoverlay.getId();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     *设置根据锚点设置图片层的位置，图片层的其他属性不变。
     * @param latLng - 可以设置图片层的位置的锚点。
     */
  public void setPosition(LatLng latLng)
  {
    try
    {
      this.groundoverlay.setPosition(latLng);
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 锚点在地图上的位置。
     * @return 锚点在地图上的位置。
     */
  public LatLng getPosition()
  {
    try
    {
      return this.groundoverlay.getPosition();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 设置图片层的宽度，图片层的高度根据图片的比例自动变化。
     * @param paramFloat  - 图片层的宽度，单位：米。
     */
  public void setDimensions(float paramFloat)
  {
    try
    {
      this.groundoverlay.setDimensions(paramFloat);
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 设置图片层的图片，新图片会使用老图片的矩形区域。
     * @param icon - 新图片。
     */
  public void setImage(BitmapDescriptor icon)
  {
    try
    {
      this.groundoverlay.setImage(icon);
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 设置图片层的宽度和高度，图片会被拉伸，可能不会保留之前的图片比例。
     * @param width - 图片层的宽度。
     * @param height - 图片层的高度。
     */
  public void setDimensions(float width, float height)
  {
    try
    {
      this.groundoverlay.setDimensions(width, height);
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 得到图片层的宽。
     * @return 图片层的宽。
     */
  public float getWidth()
  {
    try
    {
      return this.groundoverlay.getWidth();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 得到图片层的高。
     * @return 图片层的高。
     */
  public float getHeight()
  {
    try
    {
      return this.groundoverlay.getHeight();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 根据矩形区域设置图片层的位置。当设置时忽略旋转的角度，但是画此图片层时还是会使用之前的旋转角度。
     * @param latLngBounds - 可以设置图片层位置的矩形区域。
     */
  public void setPositionFromBounds(LatLngBounds latLngBounds)
  {
    try
    {
      this.groundoverlay.setPositionFromBounds(latLngBounds);
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 获取图片层位置的矩形区域。
     * @return 图片层位置的矩形区域。
     */
  public LatLngBounds getBounds()
  {
    try
    {
      return this.groundoverlay.getBounds();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 设置图片层从正北开始顺时针旋转的角度，中心点为锚点。
     * @param bearing  - 旋转的角度。
     */
  public void setBearing(float bearing)
  {
    try
    {
      this.groundoverlay.setBearing(bearing);
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 获取图片层旋转的角度。
     * @return 旋转的角度。
     */
  public float getBearing()
  {
    try
    {
      return this.groundoverlay.getBearing();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 设置图片层的z轴指数。
     * @param zIndex - 图片层的z轴指数。
     */
  public void setZIndex(float zIndex)
  {
    try
    {
      this.groundoverlay.setZIndex(zIndex);
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 得到图片层的z轴指数。
     * @return 得到图片层的z轴指数。
     */
  public float getZIndex()
  {
    try
    {
      return this.groundoverlay.getZIndex();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 设置图片层是否可见。
     * @param visible - 当前图片层是否可见，true 可见，false 不可见。
     */
  public void setVisible(boolean visible)
  {
    try
    {
      this.groundoverlay.setVisible(visible);
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 得到当前图片层的可见性。不管是否在屏幕中，只看是否在地图中存在。
     * @return 当前图片层是否可见，true 可见，false 不可见。
     */
  public boolean isVisible()
  {
    try
    {
      return this.groundoverlay.isVisible();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     *设置图片层的透明度。
     * @param transparency - 是0到1之间的数，0为不透明，1为全透明。
     */
  public void setTransparency(float transparency)
  {
    try
    {
      this.groundoverlay.setTransparency(transparency);
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 获取图片层的透明度。
     * @return 图片层的透明度。
     */
  public float getTransparency()
  {
    try
    {
      return this.groundoverlay.getTransparency();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }
  
  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof GroundOverlay)) {
      return false;
    }
    try
    {
      throw new RemoteException();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }
  
  public int hashCode()
  {
    return this.groundoverlay.hashCode();
  }
}
