package com.unistrong.api.mapcore;

import android.graphics.Rect;
import android.os.RemoteException;
import com.unistrong.api.maps.model.BitmapDescriptor;
import com.unistrong.api.maps.model.LatLng;
import com.leador.mapcore.FPoint;
import com.leador.mapcore.IPoint;

import java.util.ArrayList;
import javax.microedition.khronos.opengles.GL10;

public abstract interface IMarkerDelegate
{
  public abstract boolean remove()//b
    throws RemoteException;
  
  public abstract boolean isAnimator();
  
  public abstract Rect getRect();
  
  public abstract LatLng getPosition()
    throws RemoteException;
  
  public abstract FPoint anchorUVoff();//f
  
  public abstract LatLng g();
  
  public abstract String getId()//h
    throws RemoteException;
  
  public abstract void setPosition(LatLng paramLatLng)//a
    throws RemoteException;
  
  public abstract void title(String paramString)
    throws RemoteException;
  
  public abstract String getTitle()
    throws RemoteException;
  
  public abstract void icon(BitmapDescriptor paramBitmapDescriptor)
    throws RemoteException;
  /**
   * 设置当前marker的锚点。 锚点是定位图标接触地图平面的点。图标的左上顶点为（0,0）点，右下点为（1,1）点。默认情况下，锚点为（0.5,1.0）。
   * @param anchorU - 锚点水平范围的比例。
   * @param anchorV - 锚点垂直范围的比例。
   */
  public abstract void setAnchor(float anchorU, float anchorV)
    throws RemoteException;
  
  public abstract void setSnippet(String paramString)
    throws RemoteException;
  
  public abstract String getSnippet()
    throws RemoteException;
  
  public abstract void a(boolean paramBoolean)
    throws RemoteException;
  
  public abstract boolean isDraggable();
  
  public abstract void showInfoWindow()
    throws RemoteException;
  
  public abstract void hideInfoWindow()
    throws RemoteException;
  
  public abstract boolean isInfoWindowShow();// TODO: 16/3/25 原来名称为n，根据2.1修改
  
  public abstract void windowShowing(boolean showing);
  
  public abstract void setVisible(boolean paramBoolean)
    throws RemoteException;
  
  public abstract boolean isVisible()
    throws RemoteException;
  
  public abstract void destroy();
  
  public abstract boolean a(IMarkerDelegate paramag)
    throws RemoteException;
  
  public abstract int hashCode();
  
  public abstract void drawMarker(GL10 gl, IMapDelegate iMapDelegate);
  
  public abstract boolean calFPoint();
  
  public abstract void setObject(Object paramObject);
  
  public abstract Object getObject();
  
  public abstract void setPerspective(boolean paramBoolean)
    throws RemoteException;
  
  public abstract boolean isPerspective()
    throws RemoteException;
  
  public abstract void setRotateAngle(float paramFloat)
    throws RemoteException;
  
  public abstract float getRotateAngle();
  
  public abstract void setPeriod(int paramInt)
    throws RemoteException;
  
  public abstract int getPeriod()
    throws RemoteException;
  
  public abstract void setIcons(ArrayList<BitmapDescriptor> paramArrayList)
    throws RemoteException;
  
  public abstract ArrayList<BitmapDescriptor> getIcons()
    throws RemoteException;
  
  public abstract boolean x();
  
  public abstract void realdestroy();
  
  public abstract void setToTop()
    throws RemoteException;
  
  public abstract void setFlat(boolean paramBoolean)
    throws RemoteException;
  
  public abstract boolean isFlat();
  
  public abstract int B();
  
  public abstract int C();
  
  public abstract int D();
  
  public abstract int E();
  
  public abstract void setPositionByPixels(int x, int y);//a
  
  public abstract boolean F();
  
  public abstract void setZIndex(float paramFloat);
  
  public abstract float getZIndex();
  
  public abstract boolean isInScreen();
  
  public abstract void setGeoPoint(IPoint geoPoint);
  
  public abstract IPoint getGeoPoint();

  public abstract long getTileId();

  public abstract void setTileId(long tileId);
}
