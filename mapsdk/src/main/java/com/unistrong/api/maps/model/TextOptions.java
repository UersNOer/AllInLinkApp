package com.unistrong.api.maps.model;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * 创建文字覆盖物选项。
 */
public final class TextOptions
  implements Parcelable
{
  public static final TextOptionsCreator CREATOR = new TextOptionsCreator();
  String a;
  private LatLng position;
  private String text;
  private Typeface typeface = Typeface.DEFAULT;
  private float rotate;
  private int alignX = 4;
  private int alignY = 32;
  private int backgroundColor = -1;
  private int fontColor = -16777216;
  private Object object;
  private int fontSize = 20;
  private float zIndex = 0.0F;
  private boolean visible = true;
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeString(this.a);
    Bundle localBundle1 = new Bundle();
    if (this.position != null)
    {
      localBundle1.putDouble("lat", this.position.latitude);
      localBundle1.putDouble("lng", this.position.longitude);
    }
    paramParcel.writeBundle(localBundle1);
    paramParcel.writeString(this.text);
    paramParcel.writeInt(this.typeface.getStyle());
    paramParcel.writeFloat(this.rotate);
    paramParcel.writeInt(this.alignX);
    paramParcel.writeInt(this.alignY);
    paramParcel.writeInt(this.backgroundColor);
    paramParcel.writeInt(this.fontColor);
    paramParcel.writeInt(this.fontSize);
    paramParcel.writeFloat(this.zIndex);
    paramParcel.writeByte((byte)(this.visible ? 1 : 0));
    if ((this.object instanceof Parcelable))
    {
      Bundle localBundle2 = new Bundle();
      localBundle2.putParcelable("obj", (Parcelable)this.object);
      paramParcel.writeBundle(localBundle2);
    }
  }
  
  public int describeContents()
  {
    return 0;
  }

  /**
   * 设置文字覆盖物地理坐标。
   * @param position 地理位置
   * @return 一个TextOptions对象
     */
  public TextOptions position(LatLng position)
  {
    this.position = position;
    return this;
  }

  /**
   * 设置文字覆盖物的文字内容。
   * @param text 文字内容
   * @return 一个TextOptions对象
     */
  public TextOptions text(String text)
  {
    this.text = text;
    return this;
  }

  /**
   * 设置文字覆盖物字体。
   * @param typeface 字体
   * @return 一个TextOptions对象
   */
  public TextOptions typeface(Typeface typeface)
  {
    if (typeface != null) {
      this.typeface = typeface;
    }
    return this;
  }

  /**
   * 设置文字覆盖物可见性。
   * @param visible 是否可见 true：为可见，false：不可见
   * @return 一个TextOptions对象
     */
  public TextOptions visible(boolean visible)
  {
    this.visible = visible;
    return this;
  }

  /**
   * 设置文字覆盖物 zIndex。
   * @param zIndex z轴数值
   * @return 一个TextOptions对象
     */
  public TextOptions zIndex(float zIndex)
  {
    this.zIndex = zIndex;
    return this;
  }

  /**
   * 设置文字覆盖物旋转角度,逆时针。
   * @param rotate 角度
   * @return 一个TextOptions对象
     */
  public TextOptions rotate(float rotate)
  {
    this.rotate = rotate;
    return this;
  }

  /**
   * 设置文字覆盖物对齐方式,默认居中对齐。
   * @param alignX 水平对齐方式。
   * @param alignY 垂直对齐方式。
   * @return 一个TextOptions对象
     */
  public TextOptions align(int alignX, int alignY)
  {
    this.alignX = alignX;
    this.alignY = alignY;
    return this;
  }

  /**
   * 设置文字覆盖物背景颜色。
   * @param backgroundColor 背景颜色
   * @return 一个TextOptions对象
     */
  public TextOptions backgroundColor(int backgroundColor)
  {
    this.backgroundColor = backgroundColor;
    return this;
  }

  /**
   * 设置文字覆盖物附带对象。
   * @param object 附带对象。
   * @return 一个TextOptions对象
     */
  public TextOptions setObject(Object object)
  {
    this.object = object;
    return this;
  }

  /**
   * 设置文字覆盖物字体颜色,默认黑色。
   * @param fontColor 字体颜色
   * @return 一个TextOptions对象
     */
  public TextOptions fontColor(int fontColor)
  {
    this.fontColor = fontColor;
    return this;
  }

  /**
   * 设置文字覆盖物字体大小。
   * @param fontSize 字体大小
   * @return 一个TextOptions对象
     */
  public TextOptions fontSize(int fontSize)
  {
    this.fontSize = fontSize;
    return this;
  }
  /**
   * 获取文字覆盖物的地理坐标。
   * @return 地理坐标。
   */
  public LatLng getPosition()
  {
    return this.position;
  }

  /**
   * 获取文字覆盖物文字内容。
   * @return 文字内容
     */
  public String getText()
  {
    return this.text;
  }

  /**
   * 获取文字覆盖物的字体。
   * @return 字体
     */
  public Typeface getTypeface()
  {
    return this.typeface;
  }

  /**
   * 获取文字覆盖物旋转角度。
   * @return 角度
     */
  public float getRotate()
  {
    return this.rotate;
  }

  /**
   * 获取文字覆盖物水平对齐方式。
   * @return 水平对齐方式
     */
  public int getAlignX()
  {
    return this.alignX;
  }

  /**
   * 获取文字覆盖物垂直对齐方式。
   * @return 垂直对齐方式
     */
  public int getAlignY()
  {
    return this.alignY;
  }

  /**
   * 获取文字覆盖物的背景颜色。
   * @return 背景颜色
     */
  public int getBackgroundColor()
  {
    return this.backgroundColor;
  }

  /**
   * 获取文字覆盖物的字体颜色。
   * @return 字体颜色
     */
  public int getFontColor()
  {
    return this.fontColor;
  }

  /**
   * 获取文字覆盖物附带对象。
   * @return 对象
     */
  public Object getObject()
  {
    return this.object;
  }

  /**
   * 获取文字覆盖物的字体大小。
   * @return 字体大小
     */
  public int getFontSize()
  {
    return this.fontSize;
  }

  /**
   * 获取文字覆盖物z轴数值。
   * @return zIndex
     */
  public float getZIndex()
  {
    return this.zIndex;
  }

  /**
   * 获取文字覆盖物可见性。
   * @return true/false，可见/不可见
     */
  public boolean isVisible()
  {
    return this.visible;
  }
}
