package com.unistrong.api.maps;

import android.graphics.Point;
import com.unistrong.api.mapcore.CameraUpdateFactoryDelegate;
import com.unistrong.api.maps.model.CameraPosition;
import com.unistrong.api.maps.model.LatLng;
import com.unistrong.api.maps.model.LatLngBounds;
import com.leador.mapcore.IPoint;
import com.leador.mapcore.MapProjection;

/**
 * 创建CameraUpdate 对象,改变一个地图的视图。 调用 animateCamera(CameraUpdate), animateCamera(CameraUpdate, MapController.CancelableCallback) or moveCamera(CameraUpdate), 修改地图的视图位置
 */
public final class CameraUpdateFactory
{
    /**
     * 返回一个包含缩放级别增大的CameraUpdate 对象，调用此方法一次缩放级别加大一级，也就是屏幕距离地面更近一级。
     * @return 一个包含缩放级别改变的CameraUpdate 对象。
     */
  public static CameraUpdate zoomIn()
  {
    return new CameraUpdate(CameraUpdateFactoryDelegate.zoomIn());
  }

    /**
     * 返回一个包含缩放级别减小的CameraUpdate 对象，调用此方法一次缩放级别的减小一级，也就是屏幕距离地面更远一级。
     * @return 一个包含缩放级别改变的CameraUpdate 对象。
     */
  public static CameraUpdate zoomOut()
  {
    return new CameraUpdate(CameraUpdateFactoryDelegate.zoomOut());
  }

    /**
     * 返回一个CameraUpdate 对象，此对象为改变可视区域的中心的值，单位像素。可视区域中心点将会在x 和y 轴方向按参数的数据进行移动。
     * @example
     * 如果传入的xPixel = 5 、 yPixel = 0，则系统将可视区域向右移动，所以地图将显示为向左移动5 个像素。
     * 如果传入xPixel = 0 、 yPixel = 5，则系统将可视区域向下移动，所以地图显示为向上移动了5 个像素。
     * 以上的移动方向和可视区域当前的朝向有关。例如如果可视区域是旋转90 度，东方为上，这时向右移动窗口时，可视区域将向南移动。
     * @param xPixel - 这是水平移动的像素数。正值代表可视区域向右移动，负值代表可视区域向左移动。
     * @param yPixel - 这是垂直移动的像素数。正值代表可视区域向下移动，负值代表可视区域向上移动。
     * @return 一个包含可视区域移动的CameraUpdate 对象。
     */
  public static CameraUpdate scrollBy(float xPixel, float yPixel)
  {
    return new CameraUpdate(CameraUpdateFactoryDelegate.scrollBy(xPixel, yPixel));
  }

    /**
     * 返回一个包含缩放级别改变的CameraUpdate 对象。缩放级别将改变为转入的参数值。
     * @param zoom - 描述了一个缩放级别。地图的缩放级别是在3-19 之间。
     * @return 一个包含缩放级别改变的CameraUpdate 对象
     */
  public static CameraUpdate zoomTo(float zoom)
  {
    return new CameraUpdate(CameraUpdateFactoryDelegate.zoomTo(zoom));
  }

    /**
     * 返回一个CameraUpdate 对象，改变了当前可视区域的zoom 级别。 这个方法不同于zoomTo(float)，它作用于当前可视区域。
     * @param amount  - 修改的缩放级别。数值越大，界面距离地面越远。
     * @return CameraUpdate对象。
     */
  public static CameraUpdate zoomBy(float amount)
  {
    return new CameraUpdate(CameraUpdateFactoryDelegate.b(amount));
  }

    /**
     * 返回一个CameraUpdate 对象改变当前可视区域的缩放级别。
     * <p>Point 类型的参数可以指定缩放级别修改后的焦点，我们建议在缩放级别修改前后的焦点保持不变。 这个方法与zoomTo(float)方法是不同的，这个方法是修改了当前的可视区域。 例如：如果你经纬度(11.11, 22.22)在当前屏幕上的位置是（23，45）。在调用这个方法改变的缩放级别和经纬度后，这个经纬度在屏幕上的位置仍是(23, 45)。</p>
     * @param amount - 修改的缩放级别。数值越大，界面距离地面越远。
     * @param focus - Point 类型的参数可以指定缩放级别修改后的焦点，我们建议在缩放级别修改前后的焦点保持不变。
     * @return 一个包含可视区域移动的CameraUpdate 对象。
     */
  public static CameraUpdate zoomBy(float amount, Point focus)
  {
    return new CameraUpdate(CameraUpdateFactoryDelegate.zoomBy(amount, focus));
  }

    /**
     * 返回一个定义了可视区域移动位置CameraUpdate 对象。
     * @param cameraPosition  - 一个cameraPosition的对象。
     * @return 一个包含位置变换信息的CameraUpdate 对象。
     */
  public static CameraUpdate newCameraPosition(CameraPosition cameraPosition)
  {
    return new CameraUpdate(CameraUpdateFactoryDelegate.newCameraPosition(cameraPosition));
  }

    /**
     * 返回一个移动目的地的屏幕中心点的经纬度的CameraUpdate 对象。
     * @param latLng - 一个移动目的地的屏幕中心点的经纬度的latLng 对象。
     * @return 一个移动目的地的屏幕中心点的经纬度的CameraUpdate 对像。
     */
  public static CameraUpdate newLatLng(LatLng latLng)
  {
    return new CameraUpdate(CameraUpdateFactoryDelegate.a(latLng));
  }

    /**
     * 返回一个CameraUpdate 对象，包括可视区域框移动目标点屏幕中心位置的经纬度以及缩放级别。
     * @param latLng - 可视区域框移动目标点屏幕中心位置的经纬度。
     * @param zoom - 可视区域的缩放级别，地图支持3-19 级的缩放级别。
     * @return 一个CameraUpdate 对象。
     */
  public static CameraUpdate newLatLngZoom(LatLng latLng, float zoom)
  {
    return new CameraUpdate(CameraUpdateFactoryDelegate.a(latLng, zoom));
  }

    /**
     * 返回CameraUpdate对象，这个对象包含一个经纬度限制的区域，并且是最大可能的缩放级别。 你可以设置一个边距数值来控制插入区域与view的边框之间的空白距离。 方法必须在地图初始化完成之后使用。
     * @param bounds - 屏幕上的一个区域。
     * @param padding  - 设置区域和view之间的空白距离，单位像素。这个值适用于区域的四个边。
     * @return 一个包含变化信息的CameraUpdate对象。
     */
  public static CameraUpdate newLatLngBounds(LatLngBounds bounds, int padding)
  {
    return new CameraUpdate(CameraUpdateFactoryDelegate.newLatLngBounds(bounds, padding));
  }

    /**
     * 返回一个CameraUpdate对象，只改变地图可视区域中心点，地图缩放级别不变。
     * @param latLng - 地图可视区域中心点坐标。
     * @return 一个包含地图可视区域中心点坐标改变的CameraUpdate对象。
     */
  public static CameraUpdate changeLatLng(LatLng latLng)
  {
    IPoint localIPoint = new IPoint();
    MapProjection.lonlat2Geo(latLng.longitude, latLng.latitude, localIPoint);
    return new CameraUpdate(CameraUpdateFactoryDelegate.changeGeoCenter(localIPoint));
  }

    /**
     * 返回一个CameraUpdate 对象，只改变旋转角度。
     * @param bearing - 地图旋转角度。以角度为单位，正北方向到地图方向逆时针旋转的角度，范围从0度到360度。
     * @return 一个包含旋转角度改变的CameraUpdate对象。
     */
  public static CameraUpdate changeBearing(float bearing)
  {
    return new CameraUpdate(CameraUpdateFactoryDelegate.changeBearing(bearing % 360.0F));
  }

    /**
     * 修改选择角度和中心坐标。
     * @param bearing  - 选择角度。
     * @param geoPoint - 中心坐标。
     * @return 一个包含旋转角度改变的CameraUpdate对象。
     */
  public static CameraUpdate changeBearingGeoCenter(float bearing, IPoint geoPoint)
  {
    return new CameraUpdate(CameraUpdateFactoryDelegate.changeBearingGeoCenter(bearing % 360.0F, geoPoint));
  }


    /**
     * 返回一个CameraUpdate 对象，只改变地图倾斜度。
     * @param tilt  - 地图倾斜度。以角度为单位，范围（0,45）。
     * @return 一个包含地图倾斜度改变的CameraUpdate对象。
     */
  public static CameraUpdate changeTilt(float tilt)
  {
    return new CameraUpdate(CameraUpdateFactoryDelegate.changeTilt(tilt));
  }

    /**
     * 返回CameraUpdate对象，这个对象包含一个经纬度限制的区域这个区域将会放置在屏幕中间， 并且是最大可能的缩放级别。
     * <p>你可以设置一个边距数值来控制插入区域与view的边框之间的空白距离。 和newLatLngBounds(LatLngBounds, int)不一样的是，这个方法可以根据长、宽参数来决定限制区域的大小。</p>
     * @param bounds  - 限制区域的对象。
     * @param width - 限制区域的宽度，单位像素。
     * @param height - 限制区域的高度，单位像素。
     * @param padding - 限制区域的边框大小，单位像素。
     * @return CameraUpdate对象，它包含了变换信息。
     */
  public static CameraUpdate newLatLngBounds(LatLngBounds bounds, int width, int height, int padding)
  {
    return new CameraUpdate(CameraUpdateFactoryDelegate.newLatLngBoundsWithSize(bounds, width, height, padding));
  }
}
