package com.unistrong.api.maps.model;

public abstract interface TileProvider
{
  public static final Tile NO_TILE = new Tile(-1, -1, null);

  /**
   * 获取指定坐标和缩放级别下的瓦片图层对象
   * @param x tile的横坐标
   * @param y tile的纵坐标
   * @param zoom 瓦片的缩放级别
     * @return 指定坐标和缩放级别下的Tile对象
     */
  public abstract Tile getTile(int x, int y, int zoom);

  /**
   * 获取瓦片图层的图片像素宽度,单位你素pixel.
   * @return 图片你素宽度
     */
  public abstract int getTileWidth();

  /**
   * 获取指定瓦片图层的图片像素高度,单位像素pixel
   * @return 图片像素高度
     */
  public abstract int getTileHeight();
}
