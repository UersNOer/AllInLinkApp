package com.unistrong.api.maps;

import android.graphics.Point;
import android.graphics.PointF;
import android.os.RemoteException;
import com.unistrong.api.mapcore.IProjectionDelegate;
import com.unistrong.api.maps.model.LatLng;
import com.unistrong.api.maps.model.LatLngBounds;
import com.unistrong.api.maps.model.RuntimeRemoteException;
import com.unistrong.api.maps.model.TileProjection;
import com.unistrong.api.maps.model.VisibleRegion;

/**
 * 这个类负责将屏幕位置和地理坐标（经纬度）进行转换。屏幕位置是相对地图的左上角的位置，所以并不一定是从整个屏幕开始计算的。
 */
public class Projection
{
  private final IProjectionDelegate projection;
  
  Projection(IProjectionDelegate projection)
  {
    this.projection = projection;
  }

    /**
     * 根据转入的屏幕位置返回一个地图位置（经纬度）。这个屏幕位置是相对于地图左上角的像素位置（不是相对于屏幕的左上角）。
     * @param position - 屏幕位置的点。
     * @return 根据转入的屏幕位置返回一个地图位置（经纬度）。这个位置是从转入的点垂直射下一条光线映射到地图上的地图位置。如果从这个点射出的光线不能相交的地图上 ，则返回null（可能发生在地图严重倾斜的情况下）。
     */
  public LatLng fromScreenLocation(Point position)
  {
    try
    {
      return this.projection.fromScreenLocation(position);
    }
    catch (RemoteException e)
    {
      throw new RuntimeRemoteException(e);
    }
  }

    /**
     * 返回一个从地图位置转换来的屏幕位置。这个屏幕位置是相对于地图的左上角的，不是相对于整个屏幕的。
     * @param position - 一个从地图上的经纬度转换来的屏幕位置。
     * @return 一个从地图位置转换来的屏幕位置。
     */
  public Point toScreenLocation(LatLng position)
  {
    try
    {
      return this.projection.toScreenLocation(position);
    }
    catch (RemoteException e)
    {
      throw new RuntimeRemoteException(e);
    }
  }
  

//  /**
//   * @deprecated
//   * 返回一个从经纬度坐标转换来的opengles 需要的坐标。 用方法toOpenGLLocation 替换。
//   * @param location - 经纬度坐标。
//   * @return 返回一个opengl坐标。
//   */
//  public PointF toMapLocation(LatLng location)
//  {
//    try
//    {
//      return this.projection.toMapLocation(location);
//    }
//    catch (RemoteException e)
//    {
//      throw new RuntimeRemoteException(e);
//    }
//  }

    /**
     * 返回一个从经纬度坐标转换来的opengles 需要的坐标。
     * @param location - 经纬度坐标。
     * @return 返回一个opengl坐标。
     */
  public PointF toOpenGLLocation(LatLng location)
  {
    try
    {
      return this.projection.toMapLocation(location);
    }
    catch (RemoteException e)
    {
      throw new RuntimeRemoteException(e);
    }
  }

    /**
     * 返回一个屏幕宽度转换来的openGL 需要的宽度。
     * @param screenWidth - 屏幕的宽度（像素）。
     * @return 一个屏幕宽度转换来的openGL 需要的宽度。
     */
  public float toOpenGLWidth(int screenWidth)
  {
    try
    {
      return this.projection.toOpenGLWidth(screenWidth);
    }
    catch (RemoteException e)
    {
      throw new RuntimeRemoteException(e);
    }
  }

    /**
     * 返回一个在屏幕坐标与地理经纬度坐标之间锥形视图的映射。
     * @return VisibleRegion 当前状态的锥形视图映射。
     */
  public VisibleRegion getVisibleRegion()
  {
    try
    {
      return this.projection.getVisibleRegion();
    }
    catch (RemoteException e)
    {
      throw new RuntimeRemoteException(e);
    }
  }

    /**
     * 返回按照墨卡托投影切块后的矩形块信息。
     * @param lb  - 经纬度矩形。
     * @param zoom - 所在级别。
     * @param width  - 切图后每一个正方形图块的宽，单位为像素。要求为2 的 n次方，并且大于128。
     * @return 矩形按照墨卡托投影切块后的信息对象。
     */
  public TileProjection fromBoundsToTile(LatLngBounds lb, int zoom, int width)
  {
    try
    {
      return this.projection.fromBoundsToTile(lb, zoom, width);
    }
    catch (RemoteException e)
    {
      throw new RuntimeRemoteException(e);
    }
  }

    /**
     * 根据中心点和zoom级别获取地图控件对应的目标区域。
     * @param center  - 坐标中心。
     * @param zoom - zoom级别。
     * @return 返回一个经纬度矩形局域。
     */
  public LatLngBounds getMapBounds(LatLng center, float zoom)
  {
    try
    {
      return this.projection.getMapBounds(center, zoom);
    }
    catch (RemoteException e)
    {
      throw new RuntimeRemoteException(e);
    }
  }
}
