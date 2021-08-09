package com.unistrong.api.mapcore;

import android.os.RemoteException;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

/**
 * 覆盖物抽象类
 */
public abstract interface IOverlayDelegateDecode
{
  public abstract void remove()  //b
    throws RemoteException;
  
  public abstract String getId() //c
    throws RemoteException;
  
  public abstract void setZIndex(float zIndex) // a
    throws RemoteException;
  
  public abstract float getZIndex() // d
    throws RemoteException;
  
  public abstract void setVisible(boolean visible) // a
    throws RemoteException;
  
  public abstract boolean isVisible() //e
    throws RemoteException;
  
  public abstract boolean equals(IOverlayDelegateDecode overlay) // a
    throws RemoteException;
  
  public abstract int hashCodeRemote() //f
    throws RemoteException;
  /**
   * 计算位置
   *
   * @throws RemoteException
   */
  public abstract void calMapFPoint() //g
    throws RemoteException;
  
  public abstract void draw(GL10 gl) // a
    throws RemoteException;
  
  public abstract void destroy(); //j
  
  public abstract boolean a(); // a
  
  public abstract boolean checkInBounds(); //k

  public abstract List<Long> getTileIds();

  public abstract void setTileIds(List<Long> tileIds);

  public abstract int getDataId();

  public abstract void setDataId(int dataId);
}
