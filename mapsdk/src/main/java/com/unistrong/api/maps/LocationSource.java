package com.unistrong.api.maps;

import android.location.Location;

/**
 * 定义了一个提供位置数据的接口。立得地图的定位API 提供此接口的实现。您也可以根据自己的情况实现此接口，为地图提供定位数据。
 */
public abstract interface LocationSource
{
    /**
     *激活位置接口。定位程序将通过将此接口将主线程广播定位信息，直到用户关闭此通知。
     * @param locationChangedListener - 监听位置改变的接口。
     */
  public abstract void activate(OnLocationChangedListener locationChangedListener);

    /**
     *停止定位。重写该方法时，需移除定位监听，并销毁定位服务对象。
     */
  public abstract void deactivate();

    /**
     *处理定位更新的接口。
     */
  public static abstract interface OnLocationChangedListener
  {
      /**
       * 当获得新位置后，调用此接口。
       * @param location - 新的位置，一定不能为Null。
       */
    public abstract void onLocationChanged(Location location);
  }
}
