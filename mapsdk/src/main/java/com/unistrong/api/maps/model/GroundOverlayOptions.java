package com.unistrong.api.maps.model;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import com.unistrong.api.mapcore.util.LMapThrowException;

public final class GroundOverlayOptions
  implements Parcelable
{
  public static final GroundOverlayOptionsCreator CREATOR = new GroundOverlayOptionsCreator();
  public static final float NO_DIMENSION = -1.0F;
  private final int a;
  private BitmapDescriptor image;
  private LatLng location;
  private float width;
  private float height;
  private LatLngBounds bounds;
  private float bearing;
  private float zIndex = 0.0F;
  private boolean visible = true;
  private float transparency = 0.0F;
  private float anchorU = 0.5F;
  private float anchorV = 0.5F;
  
  GroundOverlayOptions(int paramInt, IBinder paramIBinder, LatLng location, float width, float height, LatLngBounds bounds, float bearing, float zIndex, boolean visible, float transparency, float anchorU, float anchorV)
  {
    this.a = paramInt;
    this.image = BitmapDescriptorFactory.fromBitmap(null);
    this.location = location;
    this.width = width;
    this.height = height;
    this.bounds = bounds;
    this.bearing = bearing;
    this.zIndex = zIndex;
    this.visible = visible;
    this.transparency = transparency;
    this.anchorU = anchorU;
    this.anchorV = anchorV;
  }
  
  public GroundOverlayOptions()
  {
    this.a = 1;
  }

  /**
   * 指定此图片层的图片。
   * @param image - 图片层使用的图片。
   * @return 根据新图片返回的GroundOverlayOptions。
   */
  public GroundOverlayOptions image(BitmapDescriptor image)
  {
    this.image = image;
    return this;
  }

  /**
   * 设置图片的对齐方式，[0,0]是左上角，[1,1]是右下角 。如果不设置，默认为[0.5,0.5]图片的中心点。
   * @param anchorU - 在宽度（水平方向）上的对齐方式，范围为[0,1]。
   * @param anchorV - 在高度（垂直方向）上的对齐方式，范围为[0,1]。
   * @return 根据新的对齐方式返回的GroundOverlayOptions。
   */
  public GroundOverlayOptions anchor(float anchorU, float anchorV)
  {
    this.anchorU = anchorU;
    this.anchorV = anchorV;
    return this;
  }

  /**
   * 根据锚点和宽高设置图片层。在显示时，图片高度根据图片的比例自动匹配。
   * @param location - 图片层的锚点。
   * @param width - 图片层的宽，单位：米。
   * @return 根据锚点设置的GroundOverlayOptions。
   * 抛出:java.lang.IllegalArgumentException - 锚点为null时，IllegalArgumentException 宽度和高度为负数，IllegalStateException 图片层已经用矩形区域设置位置时。
   */
  public GroundOverlayOptions position(LatLng location, float width)
  {
    LMapThrowException.ThrowIllegalStateException(this.bounds == null, "Position has already been set using positionFromBounds");
    LMapThrowException.bThrowIllegalArgumentException(location != null, "Location must be specified");
    LMapThrowException.bThrowIllegalArgumentException(width >= 0.0F, "Width must be non-negative");
    return myposition(location, width, width);
  }

  /**
   * 根据锚点和宽高设置图片层。在显示时，图片会被缩放来适应指定的尺寸。
   * @param location - 图片层的锚点。
   * @param width - 图片层的宽，单位：米。
   * @param height - 图片层的高，单位：米。
   * @return 根据锚点设置的GroundOverlayOptions。
   * 抛出:java.lang.IllegalArgumentException - 锚点为null时，IllegalArgumentException 宽度和高度为负数，IllegalStateException 图片层已经用矩形区域设置位置时。
   */
  public GroundOverlayOptions position(LatLng location, float width, float height)
  {
    LMapThrowException.ThrowIllegalStateException(this.bounds == null, "Position has already been set using positionFromBounds");
    LMapThrowException.bThrowIllegalArgumentException(location != null, "Location must be specified");
    LMapThrowException.bThrowIllegalArgumentException(width >= 0.0F, "Width must be non-negative");
    LMapThrowException.bThrowIllegalArgumentException(height >= 0.0F, "Height must be non-negative");
    return myposition(location, width, height);
  }
  
  private GroundOverlayOptions myposition(LatLng location, float width, float height)
  {
    this.location = location;
    this.width = width;
    this.height = height;
    return this;
  }
  
  public GroundOverlayOptions positionFromBounds(LatLngBounds paramLatLngBounds)
  {
    LMapThrowException.ThrowIllegalStateException(this.location == null, "Position has already been set using position: " + this.location);
    this.bounds = paramLatLngBounds;
    return this;
  }

  /**
   * 图片层从正北顺时针的角度，相对锚点旋转。
   * @param bearing - 图片层从正北顺时针的角度，范围为[0,360)。
   * @return 根据新角度返回的GroundOverlayOptions。
   */
  public GroundOverlayOptions bearing(float bearing)
  {
    this.bearing = bearing;
    return this;
  }

  /**
   * 设置图片层的z轴指数。
   * @param zIndex - z轴指数。
   * @return 新设置z轴指数后的GroundOverlayOptions。
   */
  public GroundOverlayOptions zIndex(float zIndex)
  {
    this.zIndex = zIndex;
    return this;
  }

  /**
   * 设置图片层是否可见。默认为可见 。
   * @param visible - true 可见，false 不可见。
   * @return 新设置后的GroundOverlayOptions。
   */
  public GroundOverlayOptions visible(boolean visible)
  {
    this.visible = visible;
    return this;
  }

  /**
   * 设置图片层的透明度。默认透明度为0，不透明。
   * @param transparency - 图片层的透明度，范围为[0,1]，0为不透明，1为全透明。
   * @return 根据新设置的透明度返回的GroundOverlayOptions。
   */
  public GroundOverlayOptions transparency(float transparency)
  {
    LMapThrowException.bThrowIllegalArgumentException((transparency >= 0.0F) && (transparency <= 1.0F), "Transparency must be in the range [0..1]");
    this.transparency = transparency;
    return this;
  }

  /**
   * 得到GroundOverlayOptions 的图片 。
   * @return GroundOverlayOptions 的图片。
   */
  public BitmapDescriptor getImage()
  {
    return this.image;
  }

  /**
   * GroundOverlayOptions 的锚点位置。
   * @return 此图片层的锚点。如果用矩形区域设置图片层位置，此方法返回null。
   */
  public LatLng getLocation()
  {
    return this.location;
  }

  /**
   * GroundOverlayOptions 的宽 。
   * @return 图片层的宽。
   */
  public float getWidth()
  {
    return this.width;
  }

  /**
   * 得到GroundOverlayOptions 的高。
   * @return 图片层的高。
   */
  public float getHeight()
  {
    return this.height;
  }

  /**
   * 得到GroundOverlayOptions 的矩形区域 。
   * @return GroundOverlayOptions 的矩形区域。如果用锚点设置图片层位置，此方法会返回null。
   */
  public LatLngBounds getBounds()
  {
    return this.bounds;
  }
  
  public float getBearing()
  {
    return this.bearing;
  }
  
  public float getZIndex()
  {
    return this.zIndex;
  }
  
  public float getTransparency()
  {
    return this.transparency;
  }
  
  public float getAnchorU()
  {
    return this.anchorU;
  }
  
  public float getAnchorV()
  {
    return this.anchorV;
  }
  
  public boolean isVisible()
  {
    return this.visible;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(this.a);
    paramParcel.writeParcelable(this.image, paramInt);
    paramParcel.writeParcelable(this.location, paramInt);
    paramParcel.writeFloat(this.width);
    paramParcel.writeFloat(this.height);
    paramParcel.writeParcelable(this.bounds, paramInt);
    paramParcel.writeFloat(this.bearing);
    paramParcel.writeFloat(this.zIndex);
    paramParcel.writeByte((byte)(this.visible ? 1 : 0));
    paramParcel.writeFloat(this.transparency);
    paramParcel.writeFloat(this.anchorU);
    paramParcel.writeFloat(this.anchorV);
  }
}
