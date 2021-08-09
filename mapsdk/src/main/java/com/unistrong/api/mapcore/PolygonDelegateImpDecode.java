package com.unistrong.api.mapcore;

import android.os.RemoteException;
import android.util.Log;
import com.unistrong.api.mapcore.util.EarClippingTriangulator;
import com.unistrong.api.mapcore.util.ShortArray;
import com.unistrong.api.mapcore.util.Util;
import com.unistrong.api.maps.model.LatLng;
import com.unistrong.api.maps.model.LatLngBounds;
import com.leador.mapcore.FPoint;
import com.leador.mapcore.IPoint;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.microedition.khronos.opengles.GL10;

class PolygonDelegateImpDecode
  implements IPolygonDelegate
{
  private IMapDelegate a;
  private float b = 0.0F;
  private boolean c = true;
  private String d;
  private float e;
  private int f;
  private int g;
  private List<LatLng> h;
  private CopyOnWriteArrayList<IPoint> i = new CopyOnWriteArrayList<IPoint>();
  private FloatBuffer lineVertexBuffer;
  private FloatBuffer trianglesVertexBuffer;
  private int lineSize = 0;
  private int triangleSize = 0;
  private LatLngBounds n = null;
  private boolean o = false;
  private List<Long> tileIds = new ArrayList<>();
  private int dataId;
  
  public PolygonDelegateImpDecode(IMapDelegate paramaa)
  {
    this.a = paramaa;
    try
    {
      this.d = getId();
    }
    catch (RemoteException localRemoteException)
    {
      localRemoteException.printStackTrace();
    }
  }
  
  public void remove()
    throws RemoteException
  {
    this.a.removeGLOverlay(getId());
    this.a.setRunLowFrame(false);
  }
  
  public String getId()
    throws RemoteException
  {
    if (this.d == null) {
      this.d = GLOverlayLayerDecode.CreateId("Polygon");
    }
    return this.d;
  }
  
  public void a(List<LatLng> paramList)
    throws RemoteException
  {
    this.h = paramList;
    b(paramList);
    this.a.setRunLowFrame(false);
  }
  
  public List<LatLng> getPoints()
    throws RemoteException
  {
    return this.h;
  }
  
  public void setZIndex(float paramFloat)
    throws RemoteException
  {
    this.b = paramFloat;
    this.a.M();
    this.a.setRunLowFrame(false);
  }
  
  public float getZIndex()
    throws RemoteException
  {
    return this.b;
  }
  
  public void setVisible(boolean paramBoolean)
    throws RemoteException
  {
    this.c = paramBoolean;
    this.a.setRunLowFrame(false);
  }
  
  public boolean isVisible()
    throws RemoteException
  {
    return this.c;
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
  
  void b(List<LatLng> paramList)
    throws RemoteException
  {
    List<LatLng> localList = paramList;
    LatLngBounds.Builder localBuilder = LatLngBounds.builder();
    this.i.clear();
    if (localList != null)
    {
      Object localObject1 = null;
      for (Iterator<LatLng> localIterator = localList.iterator(); localIterator.hasNext();)
      {
    	  LatLng localObject2 = (LatLng)localIterator.next();
        if (!((LatLng)localObject2).equals(localObject1))
        {
          IPoint localIPoint = new IPoint();
          this.a.latlon2Geo(((LatLng)localObject2).latitude, ((LatLng)localObject2).longitude, localIPoint);
          this.i.add(localIPoint);
          localBuilder.include((LatLng)localObject2);
          localObject1 = localObject2;
        }
      }
      IPoint localObject2;
      IPoint localIPoint;
      int i1 = this.i.size();
      if (i1 > 1)
      {
        localObject2 = (IPoint)this.i.get(0);
        localIPoint = (IPoint)this.i.get(i1 - 1);
        if ((((IPoint)localObject2).x == localIPoint.x) && (((IPoint)localObject2).y == localIPoint.y)) {
          this.i.remove(i1 - 1);
        }
      }
    }
    this.n = localBuilder.build();
    if (this.lineVertexBuffer != null) {
      this.lineVertexBuffer.clear();
    }
    if (this.trianglesVertexBuffer != null) {
      this.trianglesVertexBuffer.clear();
    }
    this.lineSize = 0;
    this.triangleSize = 0;
    this.a.setRunLowFrame(false);
  }
  
  public void calMapFPoint()
    throws RemoteException
  {
    this.o = false;
    FPoint[] polyPoints = new FPoint[this.i.size()];
    float[] lineVertexs = new float[3 * this.i.size()];
    int i = 0;
    for (Iterator<IPoint> localObject1 = this.i.iterator(); localObject1.hasNext();)
    {
      IPoint localObject2 = (IPoint)localObject1.next();
      polyPoints[i] = new FPoint();
      this.a.geo2Map(((IPoint) localObject2).y, ((IPoint) localObject2).x, polyPoints[i]);
      lineVertexs[(i * 3)] = polyPoints[i].x;
      lineVertexs[(i * 3 + 1)] = polyPoints[i].y;
      lineVertexs[(i * 3 + 2)] = 0.0F;
      i++;
    }
    FPoint[] triangles = triangleForPoints(polyPoints);
    if (triangles.length == 0)
    {
      if (ScaleFactor == 1.0E10F) {
        ScaleFactor = 1.0E8F;
      } else {
        ScaleFactor = 1.0E10F;
      }
      triangles = triangleForPoints(polyPoints);
    }
    float [] trianglesVertexs = new float[3 * triangles.length];
    i = 0;
    for (FPoint txy : triangles)
    {
      trianglesVertexs[(i * 3)] = ((FPoint)txy).x;
      trianglesVertexs[(i * 3 + 1)] = ((FPoint)txy).y;
      trianglesVertexs[(i * 3 + 2)] = 0.0F;
      i++;
    }
    this.lineSize = polyPoints.length;
    this.triangleSize = triangles.length;
    
    this.lineVertexBuffer = Util.makeFloatBuffer(lineVertexs);
    this.trianglesVertexBuffer = Util.makeFloatBuffer((float[])trianglesVertexs);
  }
  
  public int hashCodeRemote()
    throws RemoteException
  {
    return super.hashCode();
  }
  
  public boolean a()
  {
    if (this.n == null) {
      return false;
    }
    LatLngBounds localLatLngBounds = this.a.getMapBounds();
    if (localLatLngBounds == null) {
      return true;
    }
    return (this.n.contains(localLatLngBounds)) || (this.n.intersects(localLatLngBounds));
  }
  
  public void draw(GL10 paramGL10)
    throws RemoteException
  {
    if ((this.i == null) || (this.i.size() == 0)) {
      return;
    }
    if ((this.lineVertexBuffer == null) || (this.trianglesVertexBuffer == null) || (this.lineSize == 0) || (this.triangleSize == 0)) {
      calMapFPoint();
    }
    if ((this.lineVertexBuffer != null) && (this.trianglesVertexBuffer != null) && (this.lineSize > 0) && (this.triangleSize > 0)) {
      GLESUtility.a(paramGL10, this.f, this.g, this.lineVertexBuffer, this.e, this.trianglesVertexBuffer, this.lineSize, this.triangleSize);
    }
    this.o = true;
  }
  
  public void setStrokeWidth(float paramFloat)
    throws RemoteException
  {
    this.e = paramFloat;
    this.a.setRunLowFrame(false);
  }
  
  public float getStrokeWidth()
    throws RemoteException
  {
    return this.e;
  }
  
  public void setFillColor(int paramInt)
    throws RemoteException
  {
    this.f = paramInt;
    this.a.setRunLowFrame(false);
  }
  
  public int getFillColor()
    throws RemoteException
  {
    return this.f;
  }
  
  public void setStrokeColor(int paramInt)
    throws RemoteException
  {
    this.g = paramInt;
    this.a.setRunLowFrame(false);
  }
  
  public int getStrokeColor()
    throws RemoteException
  {
    return this.g;
  }
  
  private static float ScaleFactor = 1.0E10F;
  
  static FPoint[] triangleForPoints(FPoint[] paramArrayOfFPoint)
  {
    int i1 = paramArrayOfFPoint.length;
    float[] arrayOfFloat = new float[i1 * 2];
    for (int i2 = 0; i2 < i1; i2++)
    {
      arrayOfFloat[(i2 * 2)] = (paramArrayOfFPoint[i2].x * ScaleFactor);
      arrayOfFloat[(i2 * 2 + 1)] = (paramArrayOfFPoint[i2].y * ScaleFactor);
    }
    EarClippingTriangulator localaz = new EarClippingTriangulator();
    ShortArray localbj = localaz.a(arrayOfFloat);
    int i3 = localbj.b;
    FPoint[] arrayOfFPoint = new FPoint[i3];
    for (int i4 = 0; i4 < i3; i4++)
    {
      arrayOfFPoint[i4] = new FPoint();
      arrayOfFPoint[i4].x = (arrayOfFloat[(localbj.a(i4) * 2)] / ScaleFactor);
      arrayOfFPoint[i4].y = (arrayOfFloat[(localbj.a(i4) * 2 + 1)] / ScaleFactor);
    }
    return arrayOfFPoint;
  }
  
  public void destroy()
  {
    try
    {
      if (this.lineVertexBuffer != null)
      {
        this.lineVertexBuffer.clear();
        this.lineVertexBuffer = null;
      }
      if (this.trianglesVertexBuffer != null) {
        this.trianglesVertexBuffer = null;
      }
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
      Log.d("destroy erro", "PolygonDelegateImp destroy");
    }
  }
  
  public boolean contains(LatLng paramLatLng)
    throws RemoteException
  {
    try
    {
      return Util.a(paramLatLng, getPoints());
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
    return false;
  }
  
  public boolean checkInBounds()
  {
    return this.o;
  }

  @Override
  public List<Long> getTileIds() {
    return this.tileIds;
  }

  @Override
  public void setTileIds(List tileIds) {
    this.tileIds = tileIds;
  }

  @Override
  public int getDataId() {
    return this.dataId;
  }

  @Override
  public void setDataId(int dataId) {
    this.dataId = dataId;
  }
}
