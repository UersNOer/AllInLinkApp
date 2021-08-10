package com.unistrong.api.maps.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;
import com.unistrong.api.mapcore.util.Util;

/**
 * 如果需要将一张图片绘制为Marker，需要用这个类把图片包装成对象，可以通过BitmapDescriptorFactory 获得一个BitmapDescriptor 对象。
 */
public final class BitmapDescriptor
  implements Parcelable, Cloneable
{
  public static final BitmapDescriptorCreator CREATOR = new BitmapDescriptorCreator();
  int width = 0;
  int height = 0;
  Bitmap icon;
  
  BitmapDescriptor(Bitmap bitmap)
  {
    if (bitmap != null)
    {
      this.width = bitmap.getWidth();
      this.height = bitmap.getHeight();
      this.icon = a(bitmap, Util.pow2(this.width), Util.pow2(this.height));
    }
  }
  
  private BitmapDescriptor(Bitmap bitmap, int width, int height)
  {
    this.width = width;
    this.height = height;
    this.icon = bitmap;
  }
  
  public BitmapDescriptor clone()
  {
    try
    {
      return new BitmapDescriptor(Bitmap.createBitmap(this.icon), this.width, this.height);
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
    return null;
  }

    /**
     * 获取 Bitmap 对象。获取的图片非原有尺寸，图片大小为接近的2^n 。请根据需要使用此方法。
     * @return Bitmap 对象。
     */
  public Bitmap getBitmap()
  {
    return this.icon;
  }

  /**
   * 返回 Bitmap的宽度。
   * @return Bitmap的宽度。
   */
  public int getWidth()
  {
    return this.width;
  }

  /**
   * 返回 Bitmap 的高度。
   * @return Bitmap 的高度。
   */
  public int getHeight()
  {
    return this.height;
  }
  
  private Bitmap a(Bitmap icon, int width, int height)
  {
    if ((icon == null) || (icon.isRecycled())) {
      return null;
    }
    Bitmap localBitmap = Bitmap.createBitmap(width, height, icon
      .hasAlpha() ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
    Canvas localCanvas = new Canvas(localBitmap);
    Paint localPaint = new Paint();
    localPaint.setAntiAlias(true);
    localPaint.setFilterBitmap(true);
    localCanvas.drawBitmap(icon, 0.0F, 0.0F, localPaint);
    return localBitmap;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeParcelable(this.icon, paramInt);
    paramParcel.writeInt(this.width);
    paramParcel.writeInt(this.height);
  }

  /**
   * 销毁方法.
   */
  public void recycle()
  {
    if ((this.icon != null) && (!this.icon.isRecycled()))
    {
      this.icon.recycle();
      this.icon = null;
    }
  }
  
  public boolean equals(Object paramObject)
  {
    if ((this.icon == null) || (this.icon.isRecycled())) {
      return false;
    }
    if (paramObject == null) {
      return false;
    }
    if (this == paramObject) {
      return true;
    }
    if (getClass() != paramObject.getClass()) {
      return false;
    }
    BitmapDescriptor icon = (BitmapDescriptor)paramObject;
    if ((icon.icon == null) || (icon.icon.isRecycled())) {
      return false;
    }
    if ((this.width != icon.getWidth()) || (this.height != icon.getHeight())) {
      return false;
    }
    try
    {
      return this.icon.sameAs(icon.icon);
    }
    catch (Throwable localThrowable) {}
    return false;
  }
}
