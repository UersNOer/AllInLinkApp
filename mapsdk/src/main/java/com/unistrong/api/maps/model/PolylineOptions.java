package com.unistrong.api.maps.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * 线段的选项类。
 */
public final class PolylineOptions
  implements Parcelable
{
  public static final PolylineOptionsCreator CREATOR = new PolylineOptionsCreator();
  private final List<LatLng> points;
  private float width = 10.0F;
  private int color = -16777216;
  private float zIndex = 0.0F;
  private boolean isVisible = true;
  String a;
  private BitmapDescriptor bitmap;
  private List<BitmapDescriptor> bitmapList;
  private List<Integer> colors;
  private List<Integer> customTextureList;
  private boolean useTexture = true;
  private boolean geodesic = false;
  private boolean isDottedLine = false;
  private boolean isUseGradient = false;

    /**
     * 构造函数。
     */
  public PolylineOptions()
  {
    this.points = new ArrayList();
  }

    /**
     * 设置是否使用纹理贴图画线。
     * @param useTexture - true，使用纹理贴图；false，不使用。默认为使用纹理贴图画线。
     * @return PolylineOptions对象。
     */
  public PolylineOptions setUseTexture(boolean useTexture)
  {
    this.useTexture = useTexture;
    return this;
  }

    /**
     * 设置线段的纹理图，图片为2的n次方。如果不是，会自动放大至2的n次方。图片最好不大于128*128。
     * @param customTexture  - 用户设置线段的纹理。
     * @return PolylineOptions 对象。
     */
  public PolylineOptions setCustomTexture(BitmapDescriptor customTexture)
  {
    this.bitmap = customTexture;
    return this;
  }

    /**
     * 返回线段的纹理图。
     * @return 线段的纹理图。
     */
  public BitmapDescriptor getCustomTexture()
  {
    return this.bitmap;
  }

    /**
     * 设置分段纹理list。
     * @param customTextureList  - 纹理列表(绘制线总共使用了几种纹理的集合)。
     * @return 分段纹理list。
     */
  public PolylineOptions setCustomTextureList(List<BitmapDescriptor> customTextureList)
  {
    this.bitmapList = customTextureList;
    return this;
  }

    /**
     * 获取分段纹理列表。
     * @return 分段纹理列表。
     */
  public List<BitmapDescriptor> getCustomTextureList()
  {
    return this.bitmapList;
  }

    /**
     * 设置分段纹理index。
     * @param customTextureList - 分段纹理index集合(这条线总共N个点，相邻两个点为一段，总共有N-1个分段，
     *                          每个分段一个纹理,customTextureList总共包含N-1个对应的纹理index元素，
     *                          要确保通过index可以从setCustomTextureList()设置的纹理集合里面查找到对应的纹理对象，
     *                          ps:一定要按顺序从起点到终点添加每个分段的颜色，customTextureList下标必须从0开始)。
     * @return  分段纹理index列表。
     */
  public PolylineOptions setCustomTextureIndex(List<Integer> customTextureList)
  {
    this.customTextureList = customTextureList;
    return this;
  }

    /**
     * 获取分段纹理index列表。
     * @return 分段纹理index列表。
     */
  public List<Integer> getCustomTextureIndex()
  {
    return this.customTextureList;
  }

    /**
     * 设置分段颜色。
     * @param colors - 颜色列表(这条线总共N个点，相邻两个点为一段，总共有N-1个分段，每个分段一个颜色,
     *               colors总共包含N-1个颜色值元素，ps:一定要按顺序从起点到终点添加每个分段的颜色，
     *               color下标必须从0开始)。
     * @return PolylineOptions对象。
     */
  public PolylineOptions colorValues(List<Integer> colors)
  {
    this.colors = colors;
    return this;
  }

    /**
     * 获取颜色列表。
     * @return 颜色列表。
     */
  public List<Integer> getColorValues()
  {
    return this.colors;
  }

  /**
   * 设置线段是否使用渐变色。
   * @param paramBoolean
   * @return PolylineOptions对象。
   */
  public PolylineOptions useGradient(boolean paramBoolean)
  {
    this.isUseGradient = paramBoolean;
    return this;
  }

  /**
   * 获取线段是否使用渐变色
   * @return true,使用;false,不使用
     */
  public boolean isUseGradient()
  {
    return this.isUseGradient;
  }

  /**
   * 获取线段是否使用纹理贴图
   * @return true,使用纹理贴图;false,未使用
     */
  public boolean isUseTexture()
  {
    return this.useTexture;
  }

  /**
   * 获取线段是否为大地曲线,默认为false,不画大地曲线
   * @return true,为大地曲线;false 不为大地曲线。
     */
  public boolean isGeodesic()
  {
    return this.geodesic;
  }

  /**
   * 添加单个坐标点到线段的坐标集合
   * @param point 坐标
     */
  public PolylineOptions add(LatLng point)
  {
    this.points.add(point);
    return this;
  }
  /**
   * 添加多个坐标点到线段的坐标集合
   * @param points 坐标
   */
  public PolylineOptions add(LatLng... points)
  {
    this.points.addAll(Arrays.asList(points));
    return this;
  }
  /**
   * 添加一批坐标点到线段的坐标集合
   * @param points 坐标集合
   */
  public PolylineOptions addAll(Iterable<LatLng> points)
  {
    Iterator localIterator = points.iterator();
    while (localIterator.hasNext())
    {
      LatLng localLatLng = (LatLng)localIterator.next();
      this.points.add(localLatLng);
    }
    return this;
  }

  /**
   * 设置线段的宽度
   * @param width 宽度
     */
  public PolylineOptions width(float width)
  {
    this.width = width;
    return this;
  }
  /**
   * 设置线段的颜色
   * @param color 颜色
   */
  public PolylineOptions color(int color)
  {
    this.color = color;
    return this;
  }
  /**
   * 设置线段的Z轴值
   * @param zIndex Z轴值
   */
  public PolylineOptions zIndex(float zIndex)
  {
    this.zIndex = zIndex;
    return this;
  }

  /**
   * 设置线段的可见性
   * @param isVisible  true表示可见,false 表示不可见
     */
  public PolylineOptions visible(boolean isVisible)
  {
    this.isVisible = isVisible;
    return this;
  }
  /**
   * 设置线段是否为大地曲线 默认为false
   * @param isGeodesic  true表示是大地曲线,false 表示不是大地曲线
   */
  public PolylineOptions geodesic(boolean isGeodesic)
  {
    this.geodesic = isGeodesic;
    return this;
  }

  /**
   * 设置是否画虚线，默认为false，画实线。
   * @param isDottedLine
   */
  public PolylineOptions setDottedLine(boolean isDottedLine)
  {
    this.isDottedLine = isDottedLine;
    return this;
  }

  /**
   * 获取线段是否为画虚线,默认false
   * @return true 画虚线;false 画实线
     */
  public boolean isDottedLine()
  {
    return this.isDottedLine;
  }
  /**
   * 获取线段的坐标点集合
   * @return 线段的坐标点集合
   */
  public List<LatLng> getPoints()
  {
    return this.points;
  }
  /**
   * 获取线段的宽度
   * @return 线段的宽度
   */
  public float getWidth()
  {
    return this.width;
  }
  /**
   * 获取线段的颜色
   * @return 线段的颜色
   */
  public int getColor()
  {
    return this.color;
  }
  /**
   * 获取线段的Z轴值
   * @return 线段的Z轴值
   */
  public float getZIndex()
  {
    return this.zIndex;
  }
  /**
   * 获取线段是否可见
   * @return true 可见;false 不可见
   */
  public boolean isVisible()
  {
    return this.isVisible;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeTypedList(this.points);
    
    paramParcel.writeFloat(this.width);
    paramParcel.writeInt(this.color);
    paramParcel.writeFloat(this.zIndex);
    paramParcel.writeString(this.a);
    paramParcel.writeBooleanArray(new boolean[] { this.isVisible, this.isDottedLine, this.geodesic, this.isUseGradient });
    if (this.bitmap != null) {
      paramParcel.writeParcelable(this.bitmap, paramInt);
    }
    if (this.bitmapList != null) {
      paramParcel.writeList(this.bitmapList);
    }
    if (this.customTextureList != null) {
      paramParcel.writeList(this.customTextureList);
    }
    if (this.colors != null) {
      paramParcel.writeList(this.colors);
    }
  }
}
