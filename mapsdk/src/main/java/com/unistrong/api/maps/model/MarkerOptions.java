package com.unistrong.api.maps.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
/**
 * 定义了一个marker 的选项。
 *
 */
public final class MarkerOptions
  implements Parcelable
{
  public static final MarkerOptionsCreator CREATOR = new MarkerOptionsCreator();
  private LatLng position;
  private String title;
  private String snippet;
  private float anchorU = 0.5F;
  private float anchorV = 1.0F;
  private float zIndex = 0.0F;//marker覆盖物 zIndex。
  private boolean isDraggable = false;
  private boolean isVisible = true;
  String id;
  private boolean perspective = false;//是否有透视效果//j
  private int offsetX = 0;
  private int offsetY = 0;
  private ArrayList<BitmapDescriptor> icons = new ArrayList();
  private int period = 20;
  private boolean gps = false;
  private boolean flat = false;
  private boolean isAddByAnimation = false;
  /**
   * 设置Marker的动画帧列表，多张图片模拟gif的效果。
   * @param icons - Marker的动画帧列表。
   * @return MarkerOptions 对象。
   */
  public MarkerOptions icons(ArrayList<BitmapDescriptor> icons)
  {
    this.icons = icons;
    return this;
  }

  /**
   * 返回Marker的动画帧列表，动画的描点和大小以第一帧为准，建议图片大小保持一致。
   * @return Marker的动画帧列表。
   */
  public ArrayList<BitmapDescriptor> getIcons()
  {
    return this.icons;
  }

  /**
   * 设置多少帧刷新一次图片资源，Marker动画的间隔时间，值越小动画越快。
   * @param period - 帧数， 刷新周期，值越小速度越快。默认为20，最小为1。
   * @return MarkerOptions 对象。
   */
  public MarkerOptions period(int period)
  {
    if (period <= 1) {
      this.period = 1;
    } else {
      this.period = period;
    }
    return this;
  }

  /**
   * 得到多少帧刷新一次图片资源，值越小动画越快。
   * @return 刷新周期，值越小速度越快。默认为20，最小为1。
   */
  public int getPeriod()
  {
    return this.period;
  }

  /**
   * 是否有透视效果 默认为false
   *
   * @return 是否透视效果
   */
  public boolean isPerspective()
  {
    return this.perspective;
  }

  /**
   * 设置透视效果
   *
   * @param perspective
   * @return 一个设置了透视效果的MarkerOptions 对象
   */
  public MarkerOptions perspective(boolean perspective)
  {
    this.perspective = perspective;
    return this;
  }

  /**
   * 获取是否以撒点动画方式添加Marker。
   * @return true 有动画，false 无动画，默认值为false。
     */
  public boolean isAddByAnimation() {
    return isAddByAnimation;
  }

  /**
   * 设置是否以撒点动画方式添加Marker。
   * @param addByAnimation true 有动画，false 无动画。
     */
  public MarkerOptions setAddByAnimation(boolean addByAnimation) {
    this.isAddByAnimation = addByAnimation;
    return this;
  }

  /**
   * 设置当前MarkerOptions 对象的经纬度。Marker经纬度坐标不能为Null，坐标无默认值。
   * @param position - 当前MarkerOptions对象的经纬度。
   * @return MarkerOptions对象。
   */
  public MarkerOptions position(LatLng position)
  {
    this.position = position;
    return this;
  }

  /**
   * 设置 marker 是否平贴地图。
   * @param flat - 平贴地图设置为 true，面对镜头设置为 false。
   * @return MarkerOptions对象。
   */
  public MarkerOptions setFlat(boolean flat)
  {
    this.flat = flat;
    return this;
  }
  
  private void initBitmapArray()
  {
    if (this.icons == null) {
      this.icons = new ArrayList();
    }
  }

  /**
   * 设置MarkerOptions 对象的自定义图标。
   * @param icon - 设置图标的BitmapDescriptor对象
   * @return MarkerOptions对象。
   */
  public MarkerOptions icon(BitmapDescriptor icon)
  {
    initBitmapArray();
    this.icons.clear();
    this.icons.add(icon);
    return this;
  }
  /**
   * 定义marker 图标的锚点。锚点是marker 图标接触地图平面的点。图标的左顶点为（0,0）点，右底点为（1,1）点。
   *
   * @param u - 锚点水平范围的比例，必须传入0 到1 之间的数值。
   * @param v - 锚点垂直范围的比例，必须传入0 到1 之间的数值。
   * @return 一个设置了锚点的MarkerOptions 对象
   */
  public MarkerOptions anchor(float u, float v)
  {
    this.anchorU = u;
    this.anchorV = v;
    return this;
  }

  /**
   * 设置当前标记的InfoWindow相对marker的偏移。
   * 坐标系原点为marker的中上点，InfoWindow相对此原点的像素偏移，向左和向上上为负，向右和向下为正。InfoWindow的初始位置为marker上边线与InfoWindow下边线重合，并且两者的中线在一条线上。
   * @param offsetX - InfoWindow相对原点的横向像素偏移量，单位：像素。
   * @param offsetY - InfoWindow相对原点的纵向像素偏移量，单位：像素。
   * @return MarkerOptions对象。
   */
  public MarkerOptions setInfoWindowOffset(int offsetX, int offsetY)
  {
    this.offsetX = offsetX;
    this.offsetY = offsetY;
    return this;
  }

  /**
   * 设置 Marker 的标题。
   * @param title - Marker 的标题。
   * @return 一个设置标题的MarkerOptions对象。
   */
  public MarkerOptions title(String title)
  {
    this.title = title;
    return this;
  }

  /**
   * 设置 Marker 上的 snippet。
   * @param snippet - Marker上的文字描述。
   * @return 一个设置文字描述的MarkerOptions对象。
   */
  public MarkerOptions snippet(String snippet)
  {
    this.snippet = snippet;
    return this;
  }

  /**
   * 设置标记是否可拖动。
   * @param draggable - 一个布尔值，表示标记是否可拖动，true表示可拖动，false表示不可拖动。
   * @return 设置新的拖动属性后的对象（方法被调用的对象）。
   */
  public MarkerOptions draggable(boolean draggable)
  {
    this.isDraggable = draggable;
    return this;
  }

  /**
   * 设置Marker是否可见。
   * @param visible  - Marker的可见性。
   * @return 一个设置可见属性的MarkerOptions对象。
   */
  public MarkerOptions visible(boolean visible)
  {
    this.isVisible = visible;
    return this;
  }

  /**
   * 设置marker的坐标是否是Gps，默认为false。
   * @param isGps - true 是Gps，false 不是Gps。
   * @return MarkerOptions 对象。
   */
  public MarkerOptions setGps(boolean isGps)
  {
    this.gps = isGps;
    return this;
  }
  /**
   * 返回当前MarkerOptions 对象所设置的经纬度。
   *
   * @return 当前MarkerOptions 对象所设置的经纬度。
   */
  public LatLng getPosition()
  {
    return this.position;
  }

  /**
   * 返回当前MarkerOptions对象所设置的标题。
   * @return Marker的标题。
   */
  public String getTitle()
  {
    return this.title;
  }
  /**
   * 返回当前MarkerOptions 对象所设置的文字片段。
   *
   * @return 当前MarkerOptions 对象所设置的文字片段。
   */
  public String getSnippet()
  {
    return this.snippet;
  }
  /**
   * 返回MarkerOptions 对象的自定义图标。
   *
   * @return 设置图标的BitmapDescriptor 对象，如果未设置返回null。
   */
  public BitmapDescriptor getIcon()
  {
    if ((this.icons != null) && (this.icons.size() != 0)) {
      return (BitmapDescriptor)this.icons.get(0);
    }
    return null;
  }
  /**
   * 返回锚点在水平范围的比例，是一个0 到1 之间的float 数值。这个比例值从图标的左边开始计算。
   *
   * @return 锚点在水平范围的比例。
   */
  public float getAnchorU()
  {
    return this.anchorU;
  }

  /**
   * 获取marker的水平偏移距离。
   * @return infowindow偏移水平距离。
   */
  public int getInfoWindowOffsetX()
  {
    return this.offsetX;
  }

  /**
   * 获取marker的垂直偏移距离。
   * @return infowindow偏移垂直距离。
   */
  public int getInfoWindowOffsetY()
  {
    return this.offsetY;
  }
  /**
   * 返回锚点垂直范围的比例，是一个0 到1 之间的float 数值。这个比例值从顶端开始计算。
   *
   * @return 锚点在垂直范围的比例。
   */
  public float getAnchorV()
  {
    return this.anchorV;
  }

  /**
   * 获取MarkerOptions对象的拖拽状态。
   * @return true 标记可以拖动；false 标记不可以拖动。
   */
  public boolean isDraggable()
  {
    return this.isDraggable;
  }

  /**
   * 返回当前MarkerOptions的可视属性设置。
   * @return 返回当前MarkerOptions的可视属性设置。
   */
  public boolean isVisible()
  {
    return this.isVisible;
  }

  /**
   * Marker 坐标是否是Gps，默认为false。
   * @return true 是Gps，false 不是Gps。
   */
  public boolean isGps()
  {
    return this.gps;
  }

  /**
   * 返回marker是否平贴地图。
   * @return 若marker平贴在地图上返回 true；若marker面对镜头返回 false。
   */
  public boolean isFlat()
  {
    return this.flat;
  }

  /**
   * 设置marker覆盖物 zIndex。
   * @param zIndex -
   * @return MarkerOptions对象。
   */
  public MarkerOptions zIndex(float zIndex)
  {
    this.zIndex = zIndex;
    return this;
  }

  /**
   * 获取marker覆盖物zIndex。
   * @return zIndex
   */
  public float getZIndex()
  {
    return this.zIndex;
  }
  @Override
  public int describeContents()
  {
    return 0;
  }
  @Override
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeParcelable(this.position, paramInt);
    if ((this.icons != null) && (this.icons.size() != 0)) {
      paramParcel.writeParcelable((Parcelable)this.icons.get(0), paramInt);
    }
    paramParcel.writeString(this.title);
    paramParcel.writeString(this.snippet);
    paramParcel.writeFloat(this.anchorU);
    paramParcel.writeFloat(this.anchorV);
    paramParcel.writeInt(this.offsetX);
    paramParcel.writeInt(this.offsetY);
    paramParcel.writeBooleanArray(new boolean[] { this.isVisible, this.isDraggable, this.gps, this.flat});
    paramParcel.writeString(this.id);
    paramParcel.writeInt(this.period);
    paramParcel.writeList(this.icons);
    paramParcel.writeFloat(this.zIndex);
  }
}
