package com.unistrong.api.maps;

import com.unistrong.api.mapcore.CameraUpdateFactoryDelegate;

/**
 * 定义了一个可视区域的移动。这个类对象在用户改变可视区域的时候被调用。如moveCamera(CameraUpdate)。 使用CameraUpdateFactory 类可以构造CameraUpdate 对象。
 */
public final class CameraUpdate
{
  CameraUpdateFactoryDelegate cameraUpdateFactoryDelegate;

    /**
     * 构造函数
     * @param cameraUpdateFactoryDelegateDecode
     */
  CameraUpdate(CameraUpdateFactoryDelegate cameraUpdateFactoryDelegateDecode)
  {
    this.cameraUpdateFactoryDelegate = cameraUpdateFactoryDelegateDecode;
  }
  
  CameraUpdateFactoryDelegate getCameraUpdateFactory()
  {
    return this.cameraUpdateFactoryDelegate;
  }
}
