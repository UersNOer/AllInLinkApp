package com.unistrong.api.maps.model;

import android.graphics.Typeface;
import android.os.RemoteException;
import com.unistrong.api.mapcore.ITextDelegate;

/**
 * Text 自定义在地图中绘制的文字覆盖物。
 */
public final class Text
{
    /**
     * 左对齐。
     */
  public static final int ALIGN_LEFT = 1;
    /**
     * 右对齐
     */
  public static final int ALIGN_RIGHT = 2;
    /**
     * 水平居中对齐。
     */
  public static final int ALIGN_CENTER_HORIZONTAL = 4;
    /**
     *上对齐。
     */
  public static final int ALIGN_TOP = 8;
    /**
     * 下对齐。
     */
  public static final int ALIGN_BOTTOM = 16;
    /**
     * 垂直居中对齐。
     */
  public static final int ALIGN_CENTER_VERTICAL = 32;
  private ITextDelegate a;
  
  public Text(ITextDelegate paraman)
  {
    this.a = paraman;
  }

    /**
     * 删除当前Text。在删除当前Text 之后，它的所有方法将不被支持。
     */
  public void remove()
  {
    try
    {
      this.a.remove();
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }

    /**
     * 销毁text 的图片等资源。
     */
  public void destroy()
  {
    try
    {
      if (this.a != null) {
        this.a.destroy();
      }
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }

    /**
     * 返回text 的id，每个text 的唯一标识，用来区分不同的text。
     * @return text的ID。
     */
  public String getId()
  {
    try
    {
      return this.a.getId();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 设置text 的经纬度位置。
     * @param latlng - text 的经纬度位置。
     */
  public void setPosition(LatLng latlng)
  {
    try
    {
      this.a.setPosition(latlng);
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 返回当前text 的经纬度坐标对象。
     * @return 当前text的经纬度坐标对象。
     */
  public LatLng getPosition()
  {
    try
    {
      return this.a.getPosition();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 设置text 的内容。
     * @param text - 信息。
     */
  public void setText(String text)
  {
    try
    {
      this.a.c(text);
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 返回text 的标题。
     * @return text 的标题。
     */
  public String getText()
  {
    try
    {
      return this.a.a();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 设置文字覆盖物背景颜色。
     * @param bgColor - 背景颜色。
     */
  public void setBackgroundColor(int bgColor)
  {
    try
    {
      this.a.b(bgColor);
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 获取背景色。
     * @return 获取背景色。
     */
  public int getBackgroundColor()
  {
    try
    {
      return this.a.J();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 设置字体颜色。
     * @param fontColor  - 字体颜色。
     */
  public void setFontColor(int fontColor)
  {
    try
    {
      this.a.c(fontColor);
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 获取字体颜色。
     * @return 字体颜色。
     */
  public int getFontColor()
  {
    try
    {
      return this.a.K();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 设置字体大小。
     * @param fontSize - 字体大小。
     */
  public void setFontSize(int fontSize)
  {
    try
    {
      this.a.d(fontSize);
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 获取字体大小。
     * @return 字体大小。
     */
  public int getFontSize()
  {
    try
    {
      return this.a.L();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 设置字体样式。
     * @param typeface - 字体样式。
     */
  public void setTypeface(Typeface typeface)
  {
    try
    {
      this.a.a(typeface);
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 获取字体样式。
     * @return 字体样式。
     */
  public Typeface getTypeface()
  {
    try
    {
      return this.a.M();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 设置文字覆盖物对齐方式，默认居中对齐。
     * @param alignX -水平对齐。
     * @param alignY -垂直对齐 。
     */
  public void setAlign(int alignX, int alignY)
  {
    try
    {
      this.a.b(alignX, alignY);
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 获取文字覆盖物水平对齐方式。
     * @return  文字覆盖物水平对齐方式。
     */
  public int getAlignX()
  {
    try
    {
      return this.a.N();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 获取文字覆盖物垂直对齐方式。
     * @return  文字覆盖物垂直对齐方式。
     */
  public int getAlignY()
  {
    try
    {
      return this.a.O();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 设置text 的可见属性。如果当前text 的信息窗口处理显示状态，设置text 的可见属性为false 时，信息窗口也会同时为不可见。
     * @param visible - false为不可见，true为可见。
     */
  public void setVisible(boolean visible)
  {
    try
    {
      this.a.setVisible(visible);
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * Text 是否在地图上可见。
     * @return true： 表示可见；false:不可见。
     */
  public boolean isVisible()
  {
    try
    {
      return this.a.isVisible();
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

  public boolean equals(Object paramObject)
  {
    try
    {
      if (!(paramObject instanceof Text)) {
        return false;
      }
      return this.a.a(((Text)paramObject).a);
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }
  
  public int hashCode()
  {
    return this.a.hashCode();
  }
  
  public void setObject(Object paramObject)
  {
    this.a.setObject(paramObject);
  }
  
  public Object getObject()
  {
    return this.a.getObject();
  }

    /**
     * 设置text旋转角度，以锚点旋转。
     * @param rotate  - 角度 0~360。
     */
  public void setRotate(float rotate)
  {
    try
    {
      this.a.setRotateAngle(rotate);
    }
    catch (RemoteException localRemoteException)
    {
      throw new RuntimeRemoteException(localRemoteException);
    }
  }

    /**
     * 获取文字覆盖物旋转角度，逆时针。
     * @return 文字覆盖物旋转角度，逆时针。
     */
  public float getRotate()
  {
    return this.a.getRotateAngle();
  }

    /**
     * 设置文字覆盖物 zIndex。
     * @param zIndex - Z轴数值
     */
  public void setZIndex(float zIndex)
  {
    this.a.setZIndex(zIndex);
  }

    /**
     *获取文字覆盖物zIndex。
     * @return 文字覆盖物zIndex。
     */
  public float getZIndex()
  {
    return this.a.getZIndex();
  }
}
