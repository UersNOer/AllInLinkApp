	package com.unistrong.api.mapcore;

import android.os.RemoteException;
import android.util.Log;
import com.unistrong.api.mapcore.util.Util;
import com.unistrong.api.mapcore.util.SDKLogHandler;
import com.unistrong.api.maps.MapUtils;
import com.unistrong.api.maps.model.LatLng;
import com.leador.mapcore.FPoint;
import com.leador.mapcore.IPoint;
import com.leador.mapcore.MapProjection;

import java.nio.FloatBuffer;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

class CircleDelegateImp
  implements ICircleDelegate
{
  private static final int INCISION_PRECISION = 360;
  private LatLng center = null;
  private double radius = 0.0D;
  private float strokeWidth = 10.0F;
  private int strokeColor = -16777216;
  private int fillColor = 0;
  private float zIndex = 0.0F;
  private boolean visible = true;
  private String sid;
  private IMapDelegate mapDelegate;
  private FloatBuffer floatBuffer;
  private int pointSize = 0;
  private boolean l = false;

  public CircleDelegateImp(IMapDelegate paramaa)
  {
    this.mapDelegate = paramaa;
    try
    {
      this.sid = getId();
    }
    catch (RemoteException localRemoteException)
    {
      SDKLogHandler.exception(localRemoteException, "CircleDelegateImp", "create");
      localRemoteException.printStackTrace();
    }
  }

  public boolean a()
  {
    return true;
  }

  public void remove()
    throws RemoteException
  {
    this.mapDelegate.removeGLOverlay(getId());
    this.mapDelegate.setRunLowFrame(false);
  }

  public String getId()
    throws RemoteException
  {
    if (this.sid == null) {
      this.sid = GLOverlayLayerDecode.CreateId("Circle");
    }
    return this.sid;
  }

  public void setZIndex(float zIndex)
    throws RemoteException
  {
    this.zIndex = zIndex;
    this.mapDelegate.M();
    this.mapDelegate.setRunLowFrame(false);
  }

  public float getZIndex()
    throws RemoteException
  {
    return this.zIndex;
  }

  public void setVisible(boolean visible)
    throws RemoteException
  {
    this.visible = visible;
    this.mapDelegate.setRunLowFrame(false);
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
    return 0;
  }
  /**
   * 计算位置
   *
   * @throws RemoteException
   */
  public void calMapFPoint()
    throws RemoteException
  {
    this.l = false;
    LatLng xy = this.center;
    if (xy != null)
    {
      FPoint[] linePoints = new FPoint[INCISION_PRECISION];

      float[] lineVertexs = new float[3 * linePoints.length];
      double hypotenuse = MMapPointsPerMeter(this.center.latitude) * this.radius;

      IPoint centerMapPoint = new IPoint();
      MapProjection localMapProjection = this.mapDelegate.getMapProjection();
      MapProjection.lonlat2Geo(xy.longitude, xy.latitude, centerMapPoint);
      for (int i = 0; i < INCISION_PRECISION; i++)
      {
        double radian = i * Math.PI / 180.0D;
        double xOffset = Math.sin(radian) * hypotenuse;
        double yOffset = Math.cos(radian) * hypotenuse;
        int x = (int)(centerMapPoint.x + xOffset);
        int y = (int)(centerMapPoint.y + yOffset);
        FPoint resultPnt = new FPoint();
        localMapProjection.geo2Map(x, y, resultPnt);
        linePoints[i] = resultPnt;

        lineVertexs[(i * 3)] = linePoints[i].x;
        lineVertexs[(i * 3 + 1)] = linePoints[i].y;
        lineVertexs[(i * 3 + 2)] = 0.0F;
      }
      this.pointSize = linePoints.length;
      this.floatBuffer = Util.makeFloatBuffer(lineVertexs);
    }
  }

  public void draw(GL10 gl)
    throws RemoteException
  {
    if ((this.center == null) || (this.radius <= 0.0D) || (!this.visible)) {
      return;
    }
    if ((this.floatBuffer == null) || (this.pointSize == 0)) {
      calMapFPoint();
    }
    if ((this.floatBuffer != null) && (this.pointSize > 0)) {
      GLESUtility.drawCircle(gl, this.fillColor, this.strokeColor, this.floatBuffer, this.strokeWidth, this.pointSize);
    }
    this.l = true;
  }

  void h()
  {
    this.pointSize = 0;
    if (this.floatBuffer != null) {
      this.floatBuffer.clear();
    }
    this.mapDelegate.setRunLowFrame(false);
  }

  public void setCenter(LatLng center)
    throws RemoteException
  {
    this.center = center;
    h();
  }

  public LatLng getCenter()
    throws RemoteException
  {
    return this.center;
  }

  public void setRadius(double radius)
    throws RemoteException
  {
    this.radius = radius;
    h();
  }

  public double getRadius()
    throws RemoteException
  {
    return this.radius;
  }

  public void setStrokeWidth(float width)
    throws RemoteException
  {
    this.strokeWidth = width;
    this.mapDelegate.setRunLowFrame(false);
  }

  public float getStrokeWidth()
    throws RemoteException
  {
    return this.strokeWidth;
  }

  public void setStrokeColor(int color)
    throws RemoteException
  {
    this.strokeColor = color;
  }

  public int getStrokeColor()
    throws RemoteException
  {
    return this.strokeColor;
  }

  public void setFillColor(int color)
    throws RemoteException
  {
    this.fillColor = color;
    this.mapDelegate.setRunLowFrame(false);
  }

  public int getFillColor()
    throws RemoteException
  {
    return this.fillColor;
  }

  private static float m = 4.0075016E7F;
  private static int n = 256;
  private static int o = 20;

  private float b(double paramDouble)
  {
    return (float)(Math.cos(paramDouble * Math.PI / 180.0D) * m / (n << o));
  }

  private double MMapPointsPerMeter(double paramDouble)
  {
    return 1.0D / b(paramDouble);
  }

  public void destroy()
  {
    try
    {
      this.center = null;
      if (this.floatBuffer != null)
      {
        this.floatBuffer.clear();
        this.floatBuffer = null;
      }
    }
    catch (Throwable localThrowable)
    {
      SDKLogHandler.exception(localThrowable, "CircleDelegateImp", "destroy");
      localThrowable.printStackTrace();
      Log.d("destroy erro", "CircleDelegateImp destroy");
    }
  }

  public boolean contains(LatLng paramLatLng)
    throws RemoteException
  {
    if (this.radius >= MapUtils.calculateLineDistance(this.center, paramLatLng)) {
      return true;
    }
    return false;
  }

  public boolean checkInBounds()
  {
    return this.l;
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
