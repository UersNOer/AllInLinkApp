package com.unistrong.api.mapcore;

import android.graphics.Color;
import android.os.RemoteException;
import android.util.Log;

import com.unistrong.api.mapcore.util.SDKLogHandler;
import com.unistrong.api.maps.model.LatLng;
import com.unistrong.api.maps.model.LatLngBounds;
import com.leador.mapcore.DPoint;
import com.leador.mapcore.IPoint;
import com.leador.mapcore.NativeLineRenderer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.microedition.khronos.opengles.GL10;

class NavigateArrowDelegateImpDecode
  implements INavigateArrowDelegateDecode
{
  private IMapDelegate mMapDelegate;
  private float width = 10.0F;
  private int topColor = -16777216; //箭头主题颜色
  private int sideColor = -16777216; //箭头边界颜色
  private float zIndex = 0.0F;
  private boolean visible = true;
  private String objId;
  private CopyOnWriteArrayList<IPoint> pointList = new CopyOnWriteArrayList<IPoint>();
  private int pointSize = 0;
  private boolean inBounds = false;
  float topColorAlpha;
  float topColorRed;
  float topColorGreen;
  float topColorBlue;
  float sideColorAlpha;
  float sideColorRed;
  float sideColorGreen;
  float sideColorBlue;
  private LatLngBounds arrowBound = null;
  int [] lnglatPoints;
  
  public NavigateArrowDelegateImpDecode(IMapDelegate delegate)
  {
    this.mMapDelegate = delegate;
    try
    {
      this.objId = getId();
    }
    catch (RemoteException localRemoteException)
    {
      SDKLogHandler.exception(localRemoteException, "NavigateArrowDelegateImp", "create");
      localRemoteException.printStackTrace();
    }
  }
  
  void b(List<LatLng> paramList)
    throws RemoteException
  {
    List<LatLng> localList = paramList;
    LatLngBounds.Builder localBuilder = LatLngBounds.builder();
    this.pointList.clear();
    Object localObject;
    if (localList != null)
    {
      localObject = null;
      for (LatLng localLatLng : localList) {
        if ((localLatLng != null) && (!localLatLng.equals(localObject)))
        {
          IPoint localIPoint = new IPoint();
          this.mMapDelegate.latlon2Geo(localLatLng.latitude, localLatLng.longitude, localIPoint);
          this.pointList.add(localIPoint);
          localBuilder.include(localLatLng);
          localObject = localLatLng;
        }
      }
    }
    this.arrowBound = localBuilder.build();
    this.pointSize = 0;
    
    this.mMapDelegate.setRunLowFrame(false);
  }
  
  public void remove()
    throws RemoteException
  {
    this.mMapDelegate.removeGLOverlay(getId());
    this.mMapDelegate.setRunLowFrame(false);
  }
  
  public String getId()
    throws RemoteException
  {
    if (this.objId == null) {
      this.objId = GLOverlayLayerDecode.CreateId("NavigateArrow");
    }
    return this.objId;
  }
  
  public void setPoints(List<LatLng> paramList)
    throws RemoteException
  {
    b(paramList);
  }
  
  public List<LatLng> getPoints()
    throws RemoteException
  {
    return n();
  }
  
  private List<LatLng> n()
    throws RemoteException
  {
    if (this.pointList != null)
    {
      ArrayList<LatLng> localArrayList = new ArrayList<LatLng>();
      for (IPoint localIPoint : this.pointList) {
        if (localIPoint != null)
        {
          DPoint localDPoint = new DPoint();
          this.mMapDelegate.geo2Latlng(localIPoint.x, localIPoint.y, localDPoint);
          localArrayList.add(new LatLng(localDPoint.y, localDPoint.x));
        }
      }
      return localArrayList;
    }
    return null;
  }
  
  public void setWidth(float paramFloat)
    throws RemoteException
  {
    this.width = paramFloat;
    this.mMapDelegate.setRunLowFrame(false);
  }
  
  public float getWidth()
    throws RemoteException
  {
    return this.width;
  }
  
  public void setTopColor(int color)
    throws RemoteException
  {
    this.topColor = color;
    this.topColorAlpha = (Color.alpha(color) / 255.0F);
    this.topColorRed = (Color.red(color) / 255.0F);
    this.topColorGreen = (Color.green(color) / 255.0F);
    this.topColorBlue = (Color.blue(color) / 255.0F);
    this.mMapDelegate.setRunLowFrame(false);
  }
  
  public int getTopColor()
    throws RemoteException
  {
    return this.topColor;
  }
  
  public void setSideColor(int paramInt)
    throws RemoteException
  {
    this.sideColor = paramInt;
    this.sideColorAlpha = (Color.alpha(paramInt) / 255.0F);
    this.sideColorRed = (Color.red(paramInt) / 255.0F);
    this.sideColorGreen = (Color.green(paramInt) / 255.0F);
    this.sideColorBlue = (Color.blue(paramInt) / 255.0F);
    this.mMapDelegate.setRunLowFrame(false);
  }
  
  public int getSideColor()
    throws RemoteException
  {
    return this.sideColor;
  }
  
  public void setZIndex(float paramFloat)
    throws RemoteException
  {
    this.zIndex = paramFloat;
    this.mMapDelegate.M();
    this.mMapDelegate.setRunLowFrame(false);
  }
  
  public float getZIndex()
    throws RemoteException
  {
    return this.zIndex;
  }
  
  public void setVisible(boolean paramBoolean)
    throws RemoteException
  {
    this.visible = paramBoolean;
    this.mMapDelegate.setRunLowFrame(false);
  }
  
  public boolean isVisible()
    throws RemoteException
  {
    return this.visible;
  }
  
  public boolean equals(IOverlayDelegateDecode paramai)
    throws RemoteException
  {
    if ((equals(paramai)) || 
      (paramai.getId().equals(getId()))) {
      return true;
    }
    return false;
  }
  
  public int hashCodeRemote()
    throws RemoteException
  {
    return super.hashCode();
  }
  
  public boolean a()
  {
    if (this.arrowBound == null) {
      return false;
    }
    LatLngBounds localLatLngBounds = this.mMapDelegate.getMapBounds();
    if (localLatLngBounds == null) {
      return true;
    }
    return (localLatLngBounds.contains(this.arrowBound)) || (this.arrowBound.intersects(localLatLngBounds));
  }
  
  public void calMapFPoint()
    throws RemoteException
  {
    this.inBounds = false;

    lnglatPoints = new int[this.pointList.size() * 2]; //坐标数组，后期可以在构造对象时创建
    for(int i = 0; i  < this.pointList.size() ; i ++ ){
      lnglatPoints[i*2] = this.pointList.get(i).x;
      lnglatPoints[i*2+1] = this.pointList.get(i).y;
    }

    this.pointSize = this.pointList.size();
  }
  
  public void draw(GL10 paramGL10)
    throws RemoteException
  {
    if ((this.pointList == null) || (this.pointList.size() == 0) || (this.width <= 0.0F)) {
      return;
    }

    if (this.pointSize == 0) {
      calMapFPoint();
    }

    if ((this.lnglatPoints != null) && (this.pointSize > 0))
    {
      float f1 = this.mMapDelegate.getMapProjection().getMapLenWithWin((int)this.width);

      long stateInstance = this.mMapDelegate.getMapProjection().getInstanceHandle();

      //绘制箭头
      NativeLineRenderer.nativeDrawArrowLine(lnglatPoints, lnglatPoints.length, stateInstance, f1, sideColor, topColor);
    }
    this.inBounds = true;
  }
  
  public void destroy()
  {
    try
    {
      if (this.lnglatPoints != null) {
        this.lnglatPoints = null;
      }
    }
    catch (Throwable localThrowable)
    {
      SDKLogHandler.exception(localThrowable, "NavigateArrowDelegateImp", "destroy");
      localThrowable.printStackTrace();
      Log.d("destroy erro", "NavigateArrowDelegateImp destroy");
    }
  }
  
  public boolean checkInBounds()
  {
    return this.inBounds;
  }

  @Override
  public List<Long> getTileIds() {
    return null;
  }

  @Override
  public void setTileIds(List<Long> tileIds) {

  }

  @Override
  public int getDataId() {
    return 0;
  }

  @Override
  public void setDataId(int dataId) {

  }
}
