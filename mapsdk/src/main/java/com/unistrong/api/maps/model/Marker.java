package com.unistrong.api.maps.model;

import android.os.RemoteException;

import com.unistrong.api.mapcore.IMarkerDelegate;
import com.leador.mapcore.IPoint;

import java.util.ArrayList;

/**
 * Marker 是在地图上的一个点绘制图标。这个图标和屏幕朝向一致，和地图朝向无关，也不会受地图的旋转、倾斜、缩放影响。
 * 一个marker有如下属性：
 *     锚点：图标摆放在地图上的基准点。默认情况下，锚点是从图片下沿的中间处。
 *     位置：marker是通过经纬度的值来标注在地图上的。
 *     标题：当点击Marker 显示在信息窗口的文字，随时可以更改。
 *     片段：除了标题外其他的文字，随时可以更改。
 *     图标：Marker 显示的图标。如果未设置图标，API 将使用默认的图标，为默认图标提供了10 种颜色备选。默认情况下，Marker 是可见的。你们随时更改marker 的可见性。
 */
public final class Marker
{
  private IMarkerDelegate marker;
  
  public Marker(IMarkerDelegate marker)
  {
    this.marker = marker;
  }

    /**
     * 设置多少帧刷新一次图片资源，Marker动画的间隔时间，值越小动画越快。默认为20，最小为1。
     * @param period - 刷新周期，越小速度越快。默认为20，最小为1。
     */
  public void setPeriod(int period)
  {
    try
    {
      this.marker.setPeriod(period);
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 得到多少帧刷新一次图片资源。默认为20，最小为1。
     * @return 刷新周期，速度越小速度越快。默认为20，最小为1。
     */
  public int getPeriod()
  {
    try
    {
      return this.marker.getPeriod();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     *设置Marker动画的每一帧图片，动画的描点和大小以第一帧为准，建议图片大小保持一致。
     * @param icons - Marker动画帧的列表。
     */
  public void setIcons(ArrayList<BitmapDescriptor> icons)
  {
    try
    {
      this.marker.setIcons(icons);
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 返回Marker动画帧的列表
     * @return Marker动画帧的列表。
     */
  public ArrayList<BitmapDescriptor> getIcons()
  {
    try
    {
      return this.marker.getIcons();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 删除当前marker。在删除当前marker之后，它的所有方法将不被支持。
     */
  public void remove()
  {
    try
    {
      this.marker.remove();
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }

    /**
     * 删除当前marker并销毁Marker的图片等资源。一旦使用此方法，该对象将被彻底释放。
     */
  public void destroy()
  {
    try
    {
      if (this.marker != null) {
        this.marker.destroy();
      }
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }

    /**
     * 返回marker 的id，每个marker 的唯一标识，用来区分不同的marker。
     * @return Marker的ID。
     */
  public String getId()
  {
    try
    {
      return this.marker.getId();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

//    /**
//     * @deprecated
//     * 设置标记的近大远小效果，用在marker初始化之后。当地图倾斜时，远方的标记变小，附近的标记变大。
//     * @param isPerspective - true，设置近大远小效果；false，普通效果。
//     */
//  public void setPerspective(boolean isPerspective)
//  {
//    try
//    {
//      this.marker.setPerspective(isPerspective);
//    }
//    catch (RemoteException localRemoteException)
//    {
//      throw new RuntimeRemoteException(localRemoteException);
//    }
//  }

//    /**
//     *  @deprecated
//     *  返回标记是否设置为近大远小效果。默认为false，不具有此效果。
//     * @return 标记是否设置为近大远小效果。true为近大远小效果，反之没有效果。
//     */
//  public boolean isPerspective()
//  {
//    try
//    {
//      return this.marker.isPerspective();
//    }
//    catch (RemoteException localRemoteException)
//    {
//      throw new RuntimeRemoteException(localRemoteException);
//    }
//  }

    /**
     * 设置 marker 的经纬度位置。
     * <p>若想让标记随地图移动，可以使用此方法改变。若想让 marker 在屏幕固定显示，可以使用 setPositionByPixels(int, int) 方法。</p>
     * @param latlng  - 经纬度坐标。
     */
  public void setPosition(LatLng latlng)
  {
    try
    {
      this.marker.setPosition(latlng);
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 返回当前marker 的经纬度坐标对象。
     * @return 当前marker的经纬度坐标对象。
     */
  public LatLng getPosition()
  {
    try
    {
      return this.marker.getPosition();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 设置marker 的标题。
     * @param title  - 标题信息。
     */
  public void setTitle(String title)
  {
    try
    {
      this.marker.title(title);
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 返回marker 的标题。
     * @return marker 的标题。
     */
  public String getTitle()
  {
    try
    {
      return this.marker.getTitle();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 设置marker的文字片段。
     * @param snippet  - 文字片段。
     */
  public void setSnippet(String snippet)
  {
    try
    {
      this.marker.setSnippet(snippet);
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 返回当前marker 的文字片段。
     * @return 当前marker 的文字片段
     */
  public String getSnippet()
  {
    try
    {
      return this.marker.getSnippet();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 设置当前marker的图标 。
     * @param icon marker的图标 。
     */
  public void setIcon(BitmapDescriptor icon)
  {
    try
    {
      if (icon != null) {
        this.marker.icon(icon);
      }
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

  /**
   * 设置当前marker的锚点。 锚点是定位图标接触地图平面的点。图标的左上顶点为（0,0）点，右下点为（1,1）点。默认情况下，锚点为（0.5,1.0）。
   * @param anchorU - 锚点水平范围的比例。
   * @param anchorV - 锚点垂直范围的比例。
   */
  public void setAnchor(float anchorU, float anchorV)
  {
    try
    {
      this.marker.setAnchor(anchorU, anchorV);
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 设置标记是否可拖动。当标记设置为可拖动，用户可通过长按标记来进行移动标记。
     * @param paramBoolean - 一个布尔值，表示标记是否可拖动，true表示可拖动，false表示不可拖动。
     */
  public void setDraggable(boolean paramBoolean)
  {
    try
    {
      this.marker.a(paramBoolean);
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 获得标记的拖动状态。当标记设置为可拖动，用户可通过长按标记来进行移动标记。
     * @return true 标记可以拖动；false 标记不可以拖动。
     */
  public boolean isDraggable()
  {
    return this.marker.isDraggable();
  }

    /**
     * 显示当前 marker 的信息窗口。
     */
  public void showInfoWindow()
  {
    try
    {
      this.marker.showInfoWindow();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 隐藏marker的InfoWindow信息窗口。如果marker本身是不可以见的，此方法将不起任何作用。
     */
  public void hideInfoWindow()
  {
    try
    {
      this.marker.hideInfoWindow();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 如果marker的信息窗口显示，返回true，否则返回false。此方法返回值时不会参考这个信息窗口是否在当前屏幕上真的可见。
     * @return marker 是否可见。
     */
  public boolean isInfoWindowShown()
  {
    return this.marker.isInfoWindowShow();
  }

    /**
     * 设置marker 的可见属性。如果当前marker的信息窗口处于显示状态，设置marker 的可见属性为false 时，信息窗口也会同时为不可见。
     * @param visible  - 一个布尔值，表示Marker是否可见，true表示可见，false表示不可见。
     */
  public void setVisible(boolean visible)
  {
    try
    {
      this.marker.setVisible(visible);
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 返回marker是否可见。
     * @return marker是否可见。true，可见；false，不可见。
     */
  public boolean isVisible()
  {
    try
    {
      return this.marker.isVisible();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }
  
  public boolean equals(Object paramObject)
  {
    try
    {
      if (!(paramObject instanceof Marker)) {
        return false;
      }
      return this.marker.a(((Marker) paramObject).marker);
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }
  
  public int hashCode()
  {
    return this.marker.hashCode();
  }

    /**
     *设置Marker的附加对象。用户可以自定义Marker的属性。
     * @param object - Marker的附加对象。
     */
  public void setObject(Object object)
  {
    this.marker.setObject(object);
  }

    /**
     * 返回Marker的附加对象，即自定义的Marker的属性。
     * @return Marker的附加对象。
     */
  public Object getObject()
  {
    return this.marker.getObject();
  }

    /**
     * 设置Marker图片旋转的角度，从正北开始，逆时针计算。
     * @param rotate - Marker图片旋转的角度，从正北开始，逆时针计算。
     */
  public void setRotateAngle(float rotate)
  {
    try
    {
      this.marker.setRotateAngle(rotate);
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

  /**
   * 返回 Marker图片旋转的角度，从正北开始，逆时针计算。
   * @return Marker图片旋转的角度。
   */
  public float getRotateAngle()
  {
    return this.marker.getRotateAngle();
  }

  /**
   * 设置当前marker在最上面。
   */
  public void setToTop()
  {
    try
    {
      this.marker.setToTop();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

  /**
   * 设置marker的像素坐标点。
   * @param geoPoint
   *                {@link IPoint}像素坐标。
   */
  public void setGeoPoint(IPoint geoPoint)
  {
    this.marker.setGeoPoint(geoPoint);
  }

  /**
   * 获取marker的像素坐标。
   * @return marker的像素坐标。
   */
  public IPoint getGeoPoint()
  {
    return this.marker.getGeoPoint();
  }

    /**
     * 设置当前marker是否平贴在地图上。
     * @param flat - marker平贴地图设置为 true，面对镜头设置为 false。
     */
  public void setFlat(boolean flat)
  {
    try
    {
      this.marker.setFlat(flat);
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 返回marker是否是平贴在地图上。
     * @return 若marker平贴在地图上返回 true；若marker面对镜头返回 false。
     */
  public boolean isFlat()
  {
    return this.marker.isFlat();
  }

    /**
     * 设置marker在屏幕的像素坐标。若用此方式，marker固定显示在屏幕上；如果想让标记随地图移动，可以使用 setPosition(LatLng) 改变。
     * @param x - 横向像素点。
     * @param y - 纵向像素点。
     */
  public void setPositionByPixels(int x, int y)
  {
    this.marker.setPositionByPixels(x, y);
  }

    /**
     * 设置marker Z轴的值。
     * @param zIndex - z轴，即竖直方向上的值。
     */
  public void setZIndex(float zIndex)
  {
    this.marker.setZIndex(zIndex);
  }

    /**
     * 返回当前线段z轴的值。
     * @return zIndex z轴，即竖直方向上的值。
     */
  public float getZIndex()
  {
    return this.marker.getZIndex();
  }
}
