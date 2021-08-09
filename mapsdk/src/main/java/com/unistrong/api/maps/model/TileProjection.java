package com.unistrong.api.maps.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 包含矩形按照墨卡托投影切块后的信息类。工具类Projection中调用fromBitmapToTile()方法返回TileProjection对象。
 */
public class TileProjection
  implements Parcelable
{
  public static final TileProjection CREATOR = new TileProjection(0, 0, 0, 0, 0, 0);
    /**
     * 从视图左上角（0,0）点计算传入矩形横向偏移像素。
     */
  public final int offsetX;
    /**
     * 从视图左上角（0,0）点计算传入矩形纵向偏移像素。
     */
  public final int offsetY;
    /**
     * 切块后的矩形最小X。
     */
  public final int minX;
    /**
     * 切块后的矩形最大X。
     */
  public final int maxX;
    /**
     * 切块后的矩形最小Y。
     */
  public final int minY;
    /**
     * 切块后的矩形最大Y。
     */
  public final int maxY;

    /**
     * 根据给定的参数构造一个 TileProjection 新对象。
     * <p>用墨卡托投影方式切图后的第一张图片名称为215811_99326.png，最后一张图片名称为215812_99327.png。</p>
     * @param offsetX - 从视图左上角（0,0）点计算传入矩形横向偏移像素，单位像素。
     * @param offsetY  - 从视图左上角（0,0）点计算传入矩形纵向偏移像素，单位像素。
     * @param minX  - 切块后的矩形最小X，如例中的215811。
     * @param maxX  - 切块后的矩形最大X，如例中的215812。
     * @param minY - 切块后的矩形最小Y，如例中的99326。
     * @param maxY - 切块后的矩形最大Y，如例中的99327。
     */
  public TileProjection(int offsetX, int offsetY, int minX, int maxX, int minY, int maxY)
  {
    this.offsetX = offsetX;
    this.offsetY = offsetY;
    this.minX = minX;
    this.maxX = maxX;
    this.minY = minY;
    this.maxY = maxY;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(this.offsetX);
    paramParcel.writeInt(this.offsetY);
    paramParcel.writeInt(this.minX);
    paramParcel.writeInt(this.maxX);
    paramParcel.writeInt(this.maxX);
    paramParcel.writeInt(this.maxY);
  }
}
