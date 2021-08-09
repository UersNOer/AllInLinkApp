package com.unistrong.api.maps;

import android.os.RemoteException;
import com.unistrong.api.mapcore.IUiSettingsDelegate;
import com.unistrong.api.maps.model.RuntimeRemoteException;

/**
 * 设置用户界面的一个LMap。调用LMap 的getUiSettings() 方法可以获得类的实例。
 */
public final class UiSettings
{
  private  IUiSettingsDelegate uiSettingsDelegate;
   
  UiSettings(IUiSettingsDelegate uiSettingsDelegate)
  {
    this.uiSettingsDelegate = uiSettingsDelegate;
  }

    /**
     * 设置比例尺功能是否可用。
     * @param enabled  - 一个布尔值，表示比例尺功能是否可用，true表示可用，false表示不可用。
     */
  public void setScaleControlsEnabled(boolean enabled)
  {
    try
    {
      this.uiSettingsDelegate.setScaleControlsEnabled(enabled);
    }
    catch (RemoteException e)
    {
      throw new RuntimeRemoteException(e);
    }
  }

    /**
     * 这个方法设置了地图是否允许显示缩放按钮。如果允许，则在地图上显示。默认缩放按钮为显示。
     * @param enabled - true为显示缩放按钮； false 为禁止。
     */
  public void setZoomControlsEnabled(boolean enabled)
  {
    try
    {
      this.uiSettingsDelegate.setZoomControlsEnabled(enabled);
    }
    catch (RemoteException e)
    {
      throw new RuntimeRemoteException(e);
    }
  }

    /**
     * 指南针可用不可用。指南针是地图上的图标，指示向北方向。 如果可用，指南针将出现在界面的左上方 。随着地图移动，指南针始终指向地图的正北方向。 当用户点击指南针，可视区域返回默认方向。 如果不可用，指南针不会显示。
     * @param enabled - true，指南针可用；false，指南针不可用。
     */
  public void setCompassEnabled(boolean enabled)
  {
    try
    {
      this.uiSettingsDelegate.setCompassEnabled(enabled);
    }
    catch (RemoteException e)
    {
      throw new RuntimeRemoteException(e);
    }
  }


    /**
     * 这个方法设置了地图是否允许通过手势来移动。如果允许，则用户可以通过按住地图移动来改变可视区域。如果禁止，则不支持此功能。 这个设置不会影响用户在程序里对地图的移动。 默认移动手势为可用。
     * @param enabled - true为允许通过手势来移动地图； false 为禁止。
     */
  public void setScrollGesturesEnabled(boolean enabled)
  {
    try
    {
      this.uiSettingsDelegate.setScrollGesturesEnabled(enabled);
    }
    catch (RemoteException e)
    {
      throw new RuntimeRemoteException(e);
    }
  }

    /**
     * 这个方法设置了地图是否允许通过手势来缩放。如果允许，则用户可以通过双击地图或双指在地图上捏合来绽放地图。这个设置不会影响缩放按钮的功能， 也不会影响程序对地图的操控。 默认允许通过手势缩放地图。
     * @param enabled - true 为允许通过手势来缩放地图； false 为禁止。
     */
  public void setZoomGesturesEnabled(boolean enabled)
  {
    try
    {
      this.uiSettingsDelegate.setZoomGesturesEnabled(enabled);
    }
    catch (RemoteException e)
    {
      throw new RuntimeRemoteException(e);
    }
  }

    /**
     * 这个方法设置了地图是否允许通过手势来倾斜。如果允许，则用户可以通过两个手指按住地图垂直向下移动来让地图倾斜。如果禁止，则用户不能通过手势来倾斜地图 。 默认倾斜手势为可用。
     * @param enabled  - true 为允许通过手势来倾斜地图； false 为禁止。
     */
  public void setTiltGesturesEnabled(boolean enabled)
  {
    try
    {
      this.uiSettingsDelegate.setTiltGesturesEnabled(enabled);
    }
    catch (RemoteException e)
    {
      throw new RuntimeRemoteException(e);
    }
  }

    /**
     * 这个方法设置了地图是否允许通过手势来旋转。如果允许，则用户可以通过两个手指的旋转来旋转地图。如果禁止，则用户不能通过手势来旋转地图。 这个设置不会影响用户在程序里对地图的旋转。默认旋转手势为可用。
     * @param enabled - true 为允许通过手势来旋转地图； false 为禁止通过手势来旋转地图。
     */
  public void setRotateGesturesEnabled(boolean enabled)
  {
    try
    {
      this.uiSettingsDelegate.setRotateGesturesEnabled(enabled);
    }
    catch (RemoteException e)
    {
      throw new RuntimeRemoteException(e);
    }
  }

    /**
     * 设置当前地图是否支持所有手势。这个设置不影响用户在点击屏幕上的按钮（如缩放按钮）的效果，也不影响用户在程序里对地图的操作。
     * @param enabled - True 为允许所有手势功能；false 为禁止所有的手势功能。
     */
  public void setAllGesturesEnabled(boolean enabled)
  {
    try
    {
      this.uiSettingsDelegate.setAllGesturesEnabled(enabled);
    }
    catch (RemoteException e)
    {
      throw new RuntimeRemoteException(e);
    }
  }

    /**
     * 设置“地图”Logo的位置。
     * @param position - 地图左下角：MapOptions.LOGO_POSITION_BOTTOM_LEFT 地图底部居中：MapOptions.LOGO_POSITION_BOTTOM_CENTER 地图右下角：MapOptions.LOGO_POSITION_BOTTOM_RIGHT 。
     */
  public void setLogoPosition(int position)
  {
    try
    {
      this.uiSettingsDelegate.setLogoPosition(position);
    }
    catch (RemoteException e)
    {
      throw new RuntimeRemoteException(e);
    }
  }

    /**
     * 设置缩放按钮的位置。
     * @param position - 右边界中部：MapOptions.ZOOM_POSITION_RIGHT_CENTER 右下：MapOptions.ZOOM_POSITION_RIGHT_BUTTOM。
     */
  public void setZoomPosition(int position)
  {
    try
    {
      this.uiSettingsDelegate.setZoomPosition(position);
    }
    catch (RemoteException e)
    {
      throw new RuntimeRemoteException(e);
    }
  }

    /**
     * 获取缩放按钮的位置。
     * @return  缩放按钮的位置常量。
     */
  public int getZoomPosition()
  {
    try
    {
      return this.uiSettingsDelegate.getZoomPosition();
    }
    catch (RemoteException e)
    {
      throw new RuntimeRemoteException(e);
    }
  }

    /**
     * 返回比例尺功能是否可用。
     * @return 比例尺功能是否可用。
     */
  public boolean isScaleControlsEnabled()
  {
    try
    {
      return this.uiSettingsDelegate.isScaleControlsEnabled();
    }
    catch (RemoteException e)
    {
      throw new RuntimeRemoteException(e);
    }
  }

    /**
     * 返回当前地图是否显示了缩放按钮。
     * @return true 为显示缩放按钮 ；false 为未显示缩放按钮。
     */
  public boolean isZoomControlsEnabled()
  {
    try
    {
      return this.uiSettingsDelegate.isZoomControlsEnabled();
    }
    catch (RemoteException e)
    {
      throw new RuntimeRemoteException(e);
    }
  }

    /**
     * 获取指南针状态。
     * @return true，如果指南针可用；false，指南针不可用。
     */
  public boolean isCompassEnabled()
  {
    try
    {
      return this.uiSettingsDelegate.isCompassEnabled();
    }
    catch (RemoteException e)
    {
      throw new RuntimeRemoteException(e);
    }
  }

    /**
     * 返回当前地图是否显示了定位按钮。
     * @return true 显示定位按钮 ；false 未显示定位按钮。
     */
  public boolean isMyLocationButtonEnabled()
  {
    try
    {
      return this.uiSettingsDelegate.isMyLocationButtonEnabled();
    }
    catch (RemoteException e)
    {
      throw new RuntimeRemoteException(e);
    }
  }

    /**
     * 返回当前地图是否允许通过手势移动地图。
     * @return 返回当前地图是否允许通过手势移动地图。
     */
  public boolean isScrollGesturesEnabled()
  {
    try
    {
      return this.uiSettingsDelegate.isScrollGesturesEnabled();
    }
    catch (RemoteException e)
    {
      throw new RuntimeRemoteException(e);
    }
  }

    /**
     * 返回当前地图是否允许通过手势缩放地图。
     * @return true 表示允许通过手势缩放地图；false 表示禁止通过手势缩放地图。
     */
  public boolean isZoomGesturesEnabled()
  {
    try
    {
      return this.uiSettingsDelegate.isZoomGesturesEnabled();
    }
    catch (RemoteException e)
    {
      throw new RuntimeRemoteException(e);
    }
  }

    /**
     * 返回当前地图是否允许通过手势倾斜地图。
     * @return 返回当前地图是否允许通过手势倾斜地图。
     */
  public boolean isTiltGesturesEnabled()
  {
    try
    {
      return this.uiSettingsDelegate.isTiltGesturesEnabled();
    }
    catch (RemoteException e)
    {
      throw new RuntimeRemoteException(e);
    }
  }

    /**
     * 返回当前地图是否允许通过手势旋转地图。
     * @return 返回true 表示允许通过手势旋转地图；返回false 表示禁止通过手势旋转地图。
     */
  public boolean isRotateGesturesEnabled()
  {
    try
    {
      return this.uiSettingsDelegate.isRotateGesturesEnabled();
    }
    catch (RemoteException e)
    {
      throw new RuntimeRemoteException(e);
    }
  }

    /**
     *获取“地图”Logo的位置。
     * @return “地图”Logo的位置常量。
     */
  public int getLogoPosition()
  {
    try
    {
      return this.uiSettingsDelegate.getLogoPosition();
    }
    catch (RemoteException e)
    {
      throw new RuntimeRemoteException(e);
    }
  }

  /**
   * 设置指南针相对位置。
   * @param xPix X坐标, x 的范围为 0<= x <= 手机屏幕的像素宽度。
   * @param yPix Y坐标, y 的范围为 0<= y <= 手机屏幕的像素高度。
     */
  public void setCompassViewPosition(int xPix,int yPix){
    try{
        this.uiSettingsDelegate.setCompassViewPosition(xPix,yPix);
    }catch (RemoteException e){
      throw new RuntimeRemoteException(e);
    }
  }
}
