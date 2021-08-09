package com.unistrong.api.maps;

import android.opengl.GLSurfaceView;

/**
 * 定义了一个获取openGL接口回调方法的接口。
 */
public abstract interface CustomRenderer
  extends GLSurfaceView.Renderer
{
    /**
     * 地图坐标系统刷新，需要重新计算坐标。
     */
  public abstract void onMapReferencechanged();
}
