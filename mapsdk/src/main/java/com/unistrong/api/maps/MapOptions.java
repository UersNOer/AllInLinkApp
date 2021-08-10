package com.unistrong.api.maps;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import com.unistrong.api.maps.model.CameraPosition;

import java.util.HashMap;
import java.util.Map;

/**
 * 定义了一个配置LMap 的选项类。实现了Parcelable 接口。
 */
public class MapOptions
  implements Parcelable
{
  public static final MapOptionsCreator CREATOR = new MapOptionsCreator();
    /**
     * 地图类型。
     */
  private int mapType = 1;
    /**
     * 地图是否可以通过手势进行旋转。默认为true。
     */
  private boolean rotateGesturesEnabled = true;
    /**
     * 地图是否可以通过手势滑动。默认为true。
     */
  private boolean scrollGesturesEnabled = true;
    /**
     * 地图是否可以通过手势倾斜（3D 楼块效果），默认为true。
     */
  private boolean tiltGesturesEnabled = true;
    /**
     * 地图是否可以通过手势进行缩放。默认为true。
     */
  private boolean zoomGesturesEnabled = true;
    /**
     * 地图是否允许缩放。默认为true。
     */
  private boolean zoomEnabled= true;
    /**
     * Z 轴排序是否被允许。
     */
  private boolean zOrderOnTop = false;
    /**
     * 可视范围的位置对象。
     */
  private CameraPosition camera;
    /**
     * 指南针是否可用。默认为启用。
     */
  private boolean compassEnabled = false;
    /**
     * 地图是否显示比例尺。默认为false。
     */
  private boolean enabled = false;
    /**
     * Logo的位置。默认Logo位置（地图左下角）。
     */
  private int position = 0;
    /**
     * Logo位置（地图左下角）。
     */
  public static final int LOGO_POSITION_BOTTOM_LEFT = 0;
    /**
     * Logo位置（地图底部居中）。
     */
  public static final int LOGO_POSITION_BOTTOM_CENTER = 1;
    /**
     * Logo位置（地图右下角））。
     */
  public static final int LOGO_POSITION_BOTTOM_RIGHT = 2;
  /**
   * 缩放按钮位置（地图右边界中部）。
   */
  public static final int ZOOM_POSITION_RIGHT_CENTER = 1;
  /**
   * 缩放按钮位置（地图右下角）。
   */
  public static final int ZOOM_POSITION_RIGHT_BUTTOM = 2;
    /**
     * 调整控件位置（指北针）。
     */
  public static final int POSITION_COMPASS = 1;
  /**
   * 调整控件位置（比例尺）。
   */
  public static final int POSITION_SCALE = 2;
  /**
   * 调整控件位置（LOGO）。
   */
  public static final int POSITION_LOGO = 3;



//  public static Map<Integer,int[]> viewPositionMap=new HashMap<>();
  public static Map<Integer,Bitmap[]> viewBitmapMap=new HashMap<>();

    /**
     * 设置“地图”Logo的位置。
     * @param position  - Logo的位置。 左下：LOGO_POSITION_BOTTOM_LEFT 底部居中：LOGO_POSITION_BOTTOM_CENTER 右下：LOGO_POSITION_BOTTOM_RIGHT。
     * @return LMapOptions对象。
     */
  public MapOptions logoPosition(int position)
  {
    this.position = position;
    return this;
  }

    /**
     * 设置Z 轴排序是否被允许。
     * @param zOrderOnTop - true 允许，false 不允许。
     * @return 一个设置Z轴排序的LMapOptions对象。
     */
  public MapOptions zOrderOnTop(boolean zOrderOnTop)
  {
    this.zOrderOnTop = zOrderOnTop;
    return this;
  }

    /**
     *设置一个地图显示类型以改变初始的类型。
     * @param mapType - 地图类型： MAP_TYPE_NORMAL：普通地图，值为1； MAP_TYPE_NIGHT 黑夜地图，夜间模式，值为3； MAP_TYPE_NAVI 导航模式，值为4。
     * @return MapOptions 设置了一个地图显示类型的选项类。
     */
  public MapOptions mapType(int mapType)
  {
    this.mapType = mapType;
    return this;
  }

    /**
     * 设置了一个可视范围的初始化位置。
     * @param camera - 可视范围的位置对象。
     * @return MapOptions 设置了可视范围的初始化位置的选项类。
     */
  public MapOptions camera(CameraPosition camera)
  {
    this.camera = camera;
    return this;
  }

    /**
     * 设置地图是否显示比例尺。默认为false。
     * @param enabled  - true 支持，false 不支持。
     * @return 一个设置地图是否显示比例尺的LMapOptions对象。
     */
  public MapOptions scaleControlsEnabled(boolean enabled)
  {
    this.enabled = enabled;
    return this;
  }

    /**
     * 设置地图缩放控件是否显示。
     * @param enabled - true 显示，false 不显示。
     * @return 一个设置地图是否允许缩放的LMapOptions对象。
     */
  public MapOptions zoomControlsEnabled(boolean enabled)
  {
    this.zoomEnabled = enabled;
    return this;
  }

    /**
     * 设置指南针是否可用。默认为启用。
     * @param enabled - 一个布尔值，表示指南针是否可用，true表示可用，false表示不可用。
     * @return  LMapOptions对象。
     */
  public MapOptions compassEnabled(boolean enabled)
  {
    this.compassEnabled = enabled;
    return this;
  }

    /**
     * 设置地图是否可以通过手势滑动。默认为true。
     * @param enabled - true 支持，false 不支持。
     * @return 一个设置地图是否手势滑动的LMapOptions对象对象。
     */
  public MapOptions scrollGesturesEnabled(boolean enabled)
  {
    this.scrollGesturesEnabled = enabled;
    return this;
  }

    /**
     * 设置地图是否可以通过手势进行缩放。默认为true。
     * @param enabled  - true 支持，false 不支持。
     * @return 一个LMapOptions对象对象。
     */
  public MapOptions zoomGesturesEnabled(boolean enabled)
  {
    this.zoomGesturesEnabled = enabled;
    return this;
  }

    /**
     * 设置地图是否可以通过手势倾斜（3D 楼块效果），默认为true。
     * @param enabled - true 支持，false 不支持。
     * @return 一个LMapOptions对象。
     */
  public MapOptions tiltGesturesEnabled(boolean enabled)
  {
    this.tiltGesturesEnabled = enabled;
    return this;
  }

    /**
     * 设置地图是否可以通过手势进行旋转。默认为true。
     * @param enabled - true 支持，false 不支持。
     * @return 一个LMapOptions对象。
     */
  public MapOptions rotateGesturesEnabled(boolean enabled)
  {
    this.rotateGesturesEnabled = enabled;
    return this;
  }

    /**
     * 获取“地图”Logo的位置。
     * @return “地图”Logo的位置常量。
     */
  public int getLogoPosition()
  {
    return this.position;
  }


//  /**
//   * 设置Logo,比例尺,指北针相对地图的屏幕位置
//   * @param viewType 类型:MapOption.POSITION_COMPASS,MapOption.POSITION_SCALE,MapOption.POSITION_LOGO
//   * @param pixXY 相对地图左侧的绝对x,y坐标
//     */
//  public void customViewPositionInWindow(int viewType,int[] pixXY){
//        this.viewPositionMap.put(viewType,pixXY);
//  }
//
//  /**
//   * 设置指北针,我的位置控件图标
//   * @param viewType 类型:1.指北针;2.定位按钮 我的位置按钮需要2张图标,状态:正常和按下
//   * @param bitmap 图标
//     */
//  public void customViewBitmap(int viewType,Bitmap[] bitmap){
//        this.viewBitmapMap.put(viewType,bitmap);
//  }

    /**
     *返回Z 轴排序是否被允许。
     * @return true 允许，false 不允许。
     */
  public Boolean getZOrderOnTop()
  {
    return Boolean.valueOf(this.zOrderOnTop);
  }

    /**
     *返回选项类里的地图类型。
     * @return 选项类里的地图类型，如果返回-1，则表示未设置。
     */
  public int getMapType()
  {
    return this.mapType;
  }

    /**
     * 获取可视范围的初始化位置。
     * @return 一个已经设置的可视范围位置，如果为null 则说明还没有设置。
     */
  public CameraPosition getCamera()
  {
    return this.camera;
  }

    /**
     * 返回比例尺功能是否可用。
     * @return 比例尺功能是否可用。
     */
  public Boolean getScaleControlsEnabled()
  {
    return Boolean.valueOf(this.enabled);
  }

    /**
     * 返回地图缩放控件是否显示。
     * @return true 显示 ，false 不显示。
     */
  public Boolean getZoomControlsEnabled()
  {
    return Boolean.valueOf(this.zoomEnabled);
  }

    /**
     * 返回指南针功能是否可用。
     * @return 选项类里的指南针，如果返回false，则表示未设置。
     */
  public Boolean getCompassEnabled()
  {
    return Boolean.valueOf(this.compassEnabled);
  }

    /**
     * 返回拖动手势是否被允许。
     * @return true 允许，false 不允许。
     */
  public Boolean getScrollGesturesEnabled()
  {
    return Boolean.valueOf(this.scrollGesturesEnabled);
  }

    /**
     * 返回缩放手势是否被支持。
     * @return true 支持，false 不支持。
     */
  public Boolean getZoomGesturesEnabled()
  {
    return Boolean.valueOf(this.zoomGesturesEnabled);
  }

    /**
     * 返回地图倾斜手势（显示3D 楼块）是否被允许。
     * @return true 允许，false 不允许。
     */
  public Boolean getTiltGesturesEnabled()
  {
    return Boolean.valueOf(this.tiltGesturesEnabled);
  }

    /**
     * 返回旋转手势是否被允许。
     * @return 旋转手势是否被允许。
     */
  public Boolean getRotateGesturesEnabled()
  {
    return Boolean.valueOf(this.rotateGesturesEnabled);
  }

  public int describeContents()
  {
    return 0;
  }

  public void writeToParcel(Parcel parcel, int paramInt)
  {
    parcel.writeParcelable(this.camera, paramInt);
    parcel.writeInt(this.mapType);
    boolean[] arrayOfBoolean = { this.rotateGesturesEnabled, this.scrollGesturesEnabled, this.tiltGesturesEnabled, this.zoomGesturesEnabled, this.zoomEnabled, this.zOrderOnTop, this.compassEnabled, this.enabled };
    
    parcel.writeBooleanArray(arrayOfBoolean);
  }
}
