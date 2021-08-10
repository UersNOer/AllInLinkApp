package com.unistrong.api.maps.model;

import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;

import com.unistrong.api.mapcore.util.HttpUrlUtil;

import java.net.HttpURLConnection;

/**
 * TileOverlay的初始化选项配置类。
 */
public final class TileOverlayOptions
  implements Parcelable
{
  public static final TileOverlayOptionsCreator CREATOR = new TileOverlayOptionsCreator();
  private final int a;
  private TileProvider tileProvider;
  private boolean visible = true;
  private float zIndex;
  private int kiloBytes = 5242880;
  private int diskCacheSize = 20971520;
  private String diskCacheDir = null;
  private boolean memoryCacheEnabled = true;
  private boolean diskCacheEnabled = true;

    /**
     * 构造一个TileOverlayOptions对象。
     */
  public TileOverlayOptions()
  {
    this.a = 1;
  }
  
  TileOverlayOptions(int paramInt, IBinder binder, boolean paramBoolean, float paramFloat)
  {
    this.a = paramInt;
    
    this.visible = paramBoolean;
    this.zIndex = paramFloat;
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public void writeToParcel(Parcel parcel, int paramInt)
  {
    parcel.writeInt(this.a);
    parcel.writeValue(this.tileProvider);
    parcel.writeByte((byte)(this.visible ? 1 : 0));
    parcel.writeFloat(this.zIndex);
    parcel.writeInt(this.kiloBytes);
    parcel.writeInt(this.diskCacheSize);
    parcel.writeString(this.diskCacheDir);
    parcel.writeByte((byte)(this.memoryCacheEnabled ? 1 : 0));
    parcel.writeByte((byte)(this.diskCacheEnabled ? 1 : 0));

  }

    /**
     * 设置瓦片图层的提供者。
     * @param tileProvider - 瓦片图层的提供者。
     * @return 一个设置完瓦片图提供者的TileOverlayOptions对象。
     */
  public TileOverlayOptions tileProvider(TileProvider tileProvider)
  {
    this.tileProvider = tileProvider;
    
    return this;
  }

    /**
     * 设置瓦片图层的Z轴值，默认为0。Z轴是控制不同瓦片图层重复区域的绘制顺序的值。Z轴较大的瓦片图层会在绘制在Z轴较小的瓦片图层上面。如果两个瓦片图层的Z轴数值相同，则覆盖情况将随机出现。
     * @param zIndex - Z轴数值。
     * @return 一个设置了瓦片图层Z轴值的TileOverlayOptions对象。
     */
  public TileOverlayOptions zIndex(float zIndex)
  {
    this.zIndex = zIndex;
    return this;
  }

    /**
     * 设置瓦片图层的可见属性，默认为可见。
     * @param visible - true为可见；false为不可见。
     * @return 一个设置了可见属性的TileOverlayOptions 对象。
     */
  public TileOverlayOptions visible(boolean visible)
  {
    this.visible = visible;
    return this;
  }

    /**
     * 设置用于瓦片图层的内存缓存大小，默认值5MB。
     * @param kiloBytes - 缓存大小，单位KB。
     * @return 一个设置了用于瓦片图层内存缓存大小的TileOverlayOptions对象。
     */
  public TileOverlayOptions memCacheSize(int kiloBytes)
  {
    this.kiloBytes = kiloBytes;
    return this;
  }

    /**
     * 设置栅格图层的默认磁盘缓存大小，默认值20MB。
     * @param kiloBytes  - 缓存大小，单位KB。
     * @return 一个设置了栅格图层默认磁盘缓存大小的TileOverlayOptions对象。
     */
  public TileOverlayOptions diskCacheSize(int kiloBytes)
  {
    this.diskCacheSize = (kiloBytes * 1024);
    return this;
  }

    /**
     * 设置栅格图层的磁盘缓存目录。如果外部（SD卡）缓存可用则为外部缓存，否则为内部（手机空间）缓存。默认外部缓存目录为“/sdcard/Android/data/package_name/cache”；默认内部缓存目录为“/data/data/package_name/cache”。
     * @param path - 磁盘缓存目录的绝对路径。
     * @return 一个设置了栅格图层的磁盘缓存目录的TileOverlayOptions对象。
     */
  public TileOverlayOptions diskCacheDir(String path)
  {
    this.diskCacheDir = path;
    return this;
  }

    /**
     * 设置是否开启内存缓存。
     * @param enabled - true为开启内存缓存；false为关闭。
     * @return 一个设置是否开启内存缓存的TileOverlayOptions对象。
     */
  public TileOverlayOptions memoryCacheEnabled(boolean enabled)
  {
    this.memoryCacheEnabled = enabled;
    return this;
  }

    /**
     * 设置是否开启磁盘缓存。
     * @param enabled - true为开启磁盘缓存；false为关闭。
     * @return 一个设置是否开启磁盘缓存的TileOverlayOptions对象。
     */
  public TileOverlayOptions diskCacheEnabled(boolean enabled)
  {
    this.diskCacheEnabled = enabled;
    return this;
  }

    /**
     * 返回栅格图层的提供者。
     * @return 栅格图层的提供者。
     */
  public TileProvider getTileProvider()
  {
    return this.tileProvider;
  }

    /**
     *返回Z轴数值。
     * @return Z轴数值。
     */
  public float getZIndex()
  {
    return this.zIndex;
  }

    /**
     *返回栅格图层是否可见。
     * @return true为可见；false为不可见。
     */
  public boolean isVisible()
  {
    return this.visible;
  }

    /**
     *返回用于栅格图层的内存缓存大小。
     * @return  用于栅格图层的内存缓存大小，单位KB。
     */
  public int getMemCacheSize()
  {
    return this.kiloBytes;
  }

    /**
     *返回栅格图层的磁盘缓存大小。
     * @return 栅格图层的磁盘缓存大小，单位KB。
     */
  public int getDiskCacheSize()
  {
    return this.diskCacheSize;
  }

    /**
     *返回栅格图层的磁盘缓存目录。
     * @return 栅格图层的磁盘缓存目录。
     */
  public String getDiskCacheDir()
  {
    return this.diskCacheDir;
  }

    /**
     *获取栅格图层的内存缓存开启状态。
     * @return true为开启内存缓存；false为关闭。
     */
  public boolean getMemoryCacheEnabled()
  {
    return this.memoryCacheEnabled;
  }

    /**
     *获取栅格图层磁盘缓存的开启状态。
     * @return true为开启磁盘缓存；false为关闭。
     */
  public boolean getDiskCacheEnabled()
  {
    return this.diskCacheEnabled;
  }
}
