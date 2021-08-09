package com.unistrong.api.maps.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.unistrong.api.mapcore.util.LMapThrowException;
import com.unistrong.api.mapcore.util.RegionUtil;
import com.unistrong.api.mapcore.util.Util;

/**
 * 相机位置，这个类包含了所有的可视区域的位置参数。
 */
public final class CameraPosition
  implements Parcelable
{
  public static final CameraPositionCreator CREATOR = new CameraPositionCreator();
    /**
     * 目标位置的屏幕中心点经纬度坐标。
     */
  public final LatLng target;
    /**
     * 目标可视区域的缩放级别。
     */
  public final float zoom;
    /**
     * 目标可视区域的倾斜度，以角度为单位。
     */
  public final float tilt;
    /**
     * 可视区域指向的方向，以角度为单位，从正北向顺时针方向计算，从0 度到360 度。
     */
  public final float bearing;
    /**
     * 该位置是否在国内（此属性不是精确计算，不能用于边界区域）
     */
  public final boolean isAbroad;

    /**
     * 构造一个CameraPosition 对象。
     * @param target - 目标位置的屏幕中心点经纬度坐标。
     * @param zoom - 目标可视区域的缩放级别。
     * @param tilt - 目标可视区域的倾斜度，以角度为单位。
     * @param bearing -可视区域指向的方向，以角度为单位，从正北向顺时针方向计算，从0 度到360 度。
     */
  public CameraPosition(LatLng target, float zoom, float tilt, float bearing)
  {
    LMapThrowException.ThrowNullPointerException(target, "CameraPosition 位置不能为null ");
    
    this.target = target;
    this.zoom = Util.checkZoomLevel(zoom);
    this.tilt = Util.checkTilt(tilt, this.zoom);
    this.bearing = ((bearing <= 0.0D ? bearing % 360.0F + 360.0F : bearing) % 360.0F);
    this.isAbroad = (!RegionUtil.a(target.latitude, target.longitude));
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeFloat(this.bearing);
    paramParcel.writeFloat((float)this.target.latitude);
    paramParcel.writeFloat((float)this.target.longitude);
    paramParcel.writeFloat(this.tilt);
    paramParcel.writeFloat(this.zoom);
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int hashCode()
  {
    return super.hashCode();
  }

    /**
     *根据传入的经纬度、缩放级别构造一个CameraPosition 对象。这个CameraPosition 对象指向正北，并且可视区域的视角垂直于地图。
     * @param target  - 目标位置的屏幕中心点坐标经纬度。
     * @param zoom - 目标位置的缩放级别。
     * @return 一个CameraPosition对象。
     */
  public static final CameraPosition fromLatLngZoom(LatLng target, float zoom)
  {
    return new CameraPosition(target, zoom, 0.0F, 0.0F);
  }

    /**
     * 创建一个CameraPosition.Builder 对象。
     * @return  一个CameraPosition.Builder 对象。
     */
  public static Builder builder()
  {
    return new Builder();
  }

    /**
     * 根据传入的CameraPosition 创建一个CameraPosition.Builder 对象。
     * @param camera - 传入的CameraPosition对象。
     * @return 一个CameraPosition.Builder对象。
     */
  public static Builder builder(CameraPosition camera)
  {
    return new Builder(camera);
  }
  
  public boolean equals(Object paramObject)
  {
    if (this == paramObject) {
      return true;
    }
    if (!(paramObject instanceof CameraPosition)) {
      return false;
    }
    CameraPosition localCameraPosition = (CameraPosition)paramObject;
    if (this.target.equals(localCameraPosition.target)) {
      if (Float.floatToIntBits(this.zoom) == Float.floatToIntBits(localCameraPosition.zoom)) {
        if (Float.floatToIntBits(this.tilt) != Float.floatToIntBits(localCameraPosition.tilt)) {}
      }
    }
    return Float.floatToIntBits(this.bearing) == Float.floatToIntBits(localCameraPosition.bearing);
  }
  
  public String toString()
  {
    return Util.a(new String[] { Util.a("target", this.target), 
      Util.a("zoom", Float.valueOf(this.zoom)), 
      Util.a("tilt", Float.valueOf(this.tilt)), 
      Util.a("bearing", Float.valueOf(this.bearing)) });
  }

    /**
     * 创建一个摄像机的位置。
     */
  public static final class Builder
  {
      /**
       * 目标位置的地图中心点经纬度坐标。
       */
    private LatLng location;
      /**
       * 目标可视区域的缩放级别。
       */
    private float zoom;
      /**
       * 目标可视区域的倾斜度，以角度为单位。
       */
    private float tilt;
      /**
       * 可视区域指向的方向，以角度为单位，从正北向顺时针方向计算，从0 度到360 度。
       */
    private float bearing;

      /**
       * 构造一个新的CameraPosition对象。
       */
    public Builder() {}

      /**
       *根据给定的参数构造一个CameraPosition的新对象。
       * @param previous  - 一个CameraPosition对象。
       */
    public Builder(CameraPosition previous)
    {
      CameraPosition localCameraPosition = previous;
      target(localCameraPosition.target)
        .bearing(localCameraPosition.bearing)
        .tilt(localCameraPosition.tilt)
        .zoom(localCameraPosition.zoom);
    }

      /**
       * 设置目标位置的地图中心点经纬度坐标。
       * @param location - 目标位置的地图中心点经纬度坐标。
       * @return Builder对象。
       */
    public Builder target(LatLng location)
    {
      this.location = location;
      return this;
    }

      /**
       * 设置目标可视区域的缩放级别。
       * @param zoom - 目标可视区域的缩放级别。
       * @return Builder的对象。
       */
    public Builder zoom(float zoom)
    {
      this.zoom = zoom;
      return this;
    }

      /**
       * 设置目标可视区域的倾斜度。
       * @param tilt - 目标可视区域的倾斜度，以角度为单位。
       * @return Builder对象。
       */
    public Builder tilt(float tilt)
    {
      this.tilt = tilt;
      return this;
    }

      /**
       * 设置可视区域的旋转方向，以角度为单位，正北方向到地图方向逆时针旋转的角度，范围从0度到360度。
       * @param bearing - 可视区域的旋转方向，以角度为单位，正北方向到地图方向逆时针旋转的角度，范围从0度到360度
       * @return Builder对象。
       */
    public Builder bearing(float bearing)
    {
      this.bearing = bearing;
      return this;
    }

      /**
       * 构造一个CameraPosition 对象。
       * @return 一个CameraPosition 对象。
       */
    public CameraPosition build()
    {
      LMapThrowException.ThrowNullPointerException(this.location);
      return new CameraPosition(this.location, this.zoom, this.tilt, this.bearing);
    }
  }
}
