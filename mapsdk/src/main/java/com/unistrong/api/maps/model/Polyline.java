package com.unistrong.api.maps.model;

import android.os.RemoteException;
import com.unistrong.api.mapcore.IPolylineDelegateDecode;
import java.util.List;

/**
 * 线段类。一个线段是多个连贯点的集合线段。
 * 它拥有以下属性：
 * 顶点
 * 线段是由两个顶点之间连贯的点构成的。如果两个顶点相同，则一个线段将闭合。
 * 宽度
 * 宽度是单位是像素。宽度是可视区域的缩放级别无关。默认为10。
 * 颜色
 * 线段的颜色是ARGB格式，颜色格式可以参考android.graphics.Color。默认是黑色(0xff000000)。
 * Z轴
 * Z轴是控制地图覆盖物（overlay）之间的绘制层次的参数。这个参数能够控制Circles、Polygons、Polyline的绘制层次，但不会影响marker。 Z轴数值越大的覆盖物（overlay）将会绘制在更上层。如果两个及两个以上覆盖物（overlay）的Z轴数值相同，则最后的绘制结果是随机的。 覆盖物（overlay）的Z轴默认为0。
 * 可见
 * 这个属性表示了线段是否可以显示在地图上。如果设置为不可见，则绘制地图时不会绘制此线段，但其他属性不受影响。默认为可见。
 */
public class Polyline
{
  private final IPolylineDelegateDecode polyline;
  
  public Polyline(IPolylineDelegateDecode paramak)
  {
    this.polyline = paramak;
  }

    /**
     * 从地图上删除当前线段。线段删除后，被删除的线段的方法均无效。
     */
  public void remove()
  {
    try
    {
      this.polyline.remove();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 返回线段对象的id。线段的id用于区分在同一个地图里面不同的线段对象。
     * @return 线段对象的id。
     */
  public String getId()
  {
    try
    {
      return this.polyline.getId();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }
  /**
   * 在线段顶端追加点，与线段链接在一起形成新的线段。
   * @param point 追加的点。
   */
  public void addPoint(LatLng point){
    try
    {
       this.polyline.addPoint(point);
    }
    catch (RemoteException e)
    {
      throw new RuntimeRemoteException(e);
    }
  }
    /**
     * 设置线段的顶点。如果调用此方法后修改了List的值将不会影响到线段。
     * @param points  - 线段顶点的经纬度集合。
     */
  public void setPoints(List<LatLng> points)
  {
    try
    {
      this.polyline.setPoints(points);
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 返回当前线段的顶点列表。如果调用此方法后，线段的顶点发生了变化，列表的值不会随之改变。
     * 如果调用此方法获得顶点坐标后并且修改了顶点坐标，也不会作用到当前线程。如果需要修改线段的顶点值必须调用setPoints(List)。
     * @return 当前线段的顶点列表。
     */
  public List<LatLng> getPoints()
  {
    try
    {
      return this.polyline.getPoints();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 设置是否画大地曲线，默认false，不画大地曲线。
     * @param isGeodesic - 如果为 true，画大地曲线；如果为false，不画大地曲线。默认为false，不画大地曲线。
     */
  public void setGeodesic(boolean isGeodesic)
  {
    try
    {
      if (this.polyline.isGeodesic() != isGeodesic)
      {
        List localList = getPoints();
        this.polyline.geodesic(isGeodesic);
        setPoints(localList);
      }
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 线段是否为大地曲线，默认false，不画大地曲线。
     * @return true 为大地曲线，flase 不是大地曲线。
     */
  public boolean isGeodesic()
  {
    return this.polyline.isGeodesic();
  }

    /**
     * 设置是否画虚线，默认为false，画实线。
     * @param isDottedLine  - true，画虚线；false，画实线。
     */
  public void setDottedLine(boolean isDottedLine)
  {
    this.polyline.setDottedLine(isDottedLine);
  }

    /**
     * 是否画虚线。
     * @return true，画虚线；false，画实线。
     */
  public boolean isDottedLine()
  {
    return this.polyline.isDottedLine();
  }

    /**
     *设置线段的宽度。
     * @param width  - 线段宽度的像素。
     */
  public void setWidth(float width)
  {
    try
    {
      this.polyline.setWidth(width);
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 返回线段的宽度值。
     * @return 线段宽度的像素值。
     */
  public float getWidth()
  {
    try
    {
      return this.polyline.getWidth();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 设置线段的颜色。
     * @param color - 线段颜色的ARGB格式。
     */
  public void setColor(int color)
  {
    try
    {
      this.polyline.setColor(color);
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 返回线段的颜色。
     * @return 线段颜色的ARGB格式。
     */
  public int getColor()
  {
    try
    {
      return this.polyline.getColor();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 设置线段Z轴的值。
     * @param zIndex - 线段Z轴的值。
     */
  public void setZIndex(float zIndex)
  {
    try
    {
      this.polyline.setZIndex(zIndex);
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 返回当前线段z轴的值。
     * @return 当前线段z轴的值。
     */
  public float getZIndex()
  {
    try
    {
      return this.polyline.getZIndex();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 设置线段的可见属性。当不可见时，线段不会被绘制。
     * @param visible  - 传入true线段可见，false为不可见。
     */
  public void setVisible(boolean visible)
  {
    try
    {
      this.polyline.setVisible(visible);
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 返回线段的可见属性。
     * @return 线段的可见属性。
     */
  public boolean isVisible()
  {
    try
    {
      return this.polyline.isVisible();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }
  
  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof Polyline)) {
      return false;
    }
    try
    {
      return this.polyline.equals(((Polyline) paramObject).polyline);
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
      return this.polyline.hashCodeRemote();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 返回某点距线上最近的点。
     * @param latLng  - 一个经纬度点。
     * @return 某点到线上最近的点。
     */
  public LatLng getNearestLatLng(LatLng latLng)
  {
    return this.polyline.getNearestLatLng(latLng);
  }

    /**
     *设置折线段透明度，需要使用纹理，此方法才有效，如果只设置颜色透明度，使用color即可。
     * @param transparency  - 透明度 范围0~1, 0为不透明。
     */
  public void setTransparency(float transparency)
  {
    this.polyline.setTransparency(transparency);
  }
}
