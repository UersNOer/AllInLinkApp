package com.unistrong.api.maps.model;

import com.unistrong.api.mapcore.ITileOverlayDelegate;

/**
 * TileOverlay是栅格图层的相关类。栅格图层是显示在基本地图上的一组图像。这些栅格可以是透明的，允许您增加一些新的功能到现有的地图上。栅格图层具有以下属性：
 * 栅格提供者：
 * 提供者提供在栅格图层上使用的图像。您必须在栅格添加到地图上之前指定提供者。一旦栅格添加到地图之后，提供者将不能更改。如果栅格提供者发生变化，您必须使用 cleartilecache() 方法确保以前的栅格图不再出现。
 * Z轴值：
 * Z轴是控制栅格图层重复区域的绘制顺序的值。Z轴较大的栅格图层会在绘制在Z轴较小的栅格图层上面。如果两个栅格图层的Z轴数值相同，则覆盖情况将随机出现。
 * 可见性：
 * 栅格图层是否可见，只是表示是否画在地图上。设置不可见的栅格图层，将不被画在地图上，但会保留它的其他属性。默认栅格图层是可见的。
 * 调用这个类的方法必须在主线程使用，操作失败将会抛出IllegalStateException异常。
 */
public final class TileOverlay
{
  private ITileOverlayDelegate tileOverlayDelegate;
  
  public TileOverlay(ITileOverlayDelegate tileOverlayDelegate)
  {
    this.tileOverlayDelegate = tileOverlayDelegate;
  }

    /**
     * 从地图上删除栅格图层。
     */
  public void remove()
  {
    this.tileOverlayDelegate.a();
  }

    /**
     * 清空栅格图层的缓存。
     */
  public void clearTileCache()
  {
    this.tileOverlayDelegate.clearTileCache();
  }

    /**
     * 获取栅格图层的id。
     * @return 栅格图层的id。
     */
  public String getId()
  {
    return this.tileOverlayDelegate.getId();
  }

    /**
     * 设置栅格图层的Z轴值。
     * @param zIndex - Z轴值。
     */
  public void setZIndex(float zIndex)
  {
    this.tileOverlayDelegate.setZIndex(zIndex);
  }

    /**
     * 返回栅格图层的Z轴值。
     * @return 栅格图层的Z轴值。
     */
  public float getZIndex()
  {
    return this.tileOverlayDelegate.getZIndex();
  }

    /**
     * 设置栅格图层可见属性。
     * @param visible  - true为可见；false为不可见。
     */
  public void setVisible(boolean visible)
  {
    this.tileOverlayDelegate.setVisible(visible);
  }

    /**
     * 返回栅格图层是否可见。
     * @return true为可见；false为不可见。
     */
  public boolean isVisible()
  {
    return this.tileOverlayDelegate.isVisible();
  }
  
  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof TileOverlay)) {
      return false;
    }
    return this.tileOverlayDelegate.equalsRemote(((TileOverlay)paramObject).tileOverlayDelegate);
  }
  
  public int hashCode()
  {
    return this.tileOverlayDelegate.hashCodeRemote();
  }
}
