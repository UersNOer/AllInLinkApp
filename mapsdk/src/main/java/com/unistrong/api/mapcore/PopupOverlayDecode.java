package com.unistrong.api.mapcore;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.RemoteException;
import android.util.Log;
import com.unistrong.api.mapcore.util.Util;
import com.unistrong.api.mapcore.util.SDKLogHandler;
import com.unistrong.api.maps.model.BitmapDescriptor;
import com.unistrong.api.maps.model.LatLng;
import com.unistrong.api.maps.model.MarkerOptions;
import com.leador.mapcore.FPoint;
import com.leador.mapcore.IPoint;
import com.leador.mapcore.MapProjection;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import javax.microedition.khronos.opengles.GL10;

/**
 *
 */
class PopupOverlayDecode
  implements IMarkerDelegate
{
  private boolean a = false;
  private int b = 0;
  private int c = 0;
  private FloatBuffer d = null;
  private String e;
  private FPoint f;
  private BitmapDescriptor g;
  private boolean h = true;
  private FloatBuffer i;
  private Object j;
  private int k;
  private IMapDelegate l = null;
  private MapProjection m = null;
  private float n = 0.5F;
  private float o = 1.0F;
  private boolean p;
  private boolean q = false;
  private boolean r = true;
  private int s = 20;

  public boolean x()
  {
    return this.a;
  }
  
  public void realdestroy()
  {
    if (this.a) {
      try
      {
        remove();
        if (this.g != null)
        {
          Bitmap localBitmap = this.g.getBitmap();
          if (localBitmap != null)
          {
            localBitmap.recycle();
            localBitmap = null;
            this.g = null;
          }
        }
        if (this.i != null)
        {
          this.i.clear();
          this.i = null;
        }
        if (this.d != null)
        {
          this.d.clear();
          this.d = null;
        }
        this.f = null;
        this.j = null;
        this.k = 0;
      }
      catch (Throwable localThrowable)
      {
        SDKLogHandler.exception(localThrowable, "PopupOverlay", "realDestroy");
        localThrowable.printStackTrace();
        Log.d("destroy erro", "MarkerDelegateImp destroy");
      }
    }
  }

  private void b(BitmapDescriptor paramBitmapDescriptor)
  {
    if (paramBitmapDescriptor != null)
    {
      this.k = 0;
      this.g = paramBitmapDescriptor;
    }
  }
  
  public PopupOverlayDecode(MarkerOptions paramMarkerOptions, IMapDelegate paramaa)
  {
    this.l = paramaa;
    this.m = paramaa.getMapProjection();
    b(paramMarkerOptions.getIcon());
    this.b = paramMarkerOptions.getInfoWindowOffsetX();
    this.c = paramMarkerOptions.getInfoWindowOffsetY();
    this.h = paramMarkerOptions.isVisible();
    this.e = getId();
    calFPoint();
  }
  
  public int J()
  {
    try
    {
      return L().getWidth();
    }
    catch (Throwable localThrowable) {}
    return 0;
  }
  
  public int K()
  {
    try
    {
      return L().getHeight();
    }
    catch (Throwable localThrowable) {}
    return 0;
  }
  
  public Rect getRect()
  {
    return null;
  }
  
  public boolean remove()
  {
    M();
    if (this.k != 0) {
      this.l.deleteTexsureId(this.k);
    }
    return true;
  }
  
  private void M()
  {
    if (this.l != null) {
      this.l.setRunLowFrame(false);
    }
  }
  
  public LatLng getPosition()
  {
    return null;
  }
  
  public String getId()
  {
    if (this.e == null) {
      this.e = "PopupOverlay";
    }
    return this.e;
  }
  
  public void a(FPoint paramFPoint)
  {
    if ((paramFPoint != null) && (paramFPoint.equals(this.f))) {
      return;
    }
    this.f = paramFPoint;
  }
  
  public void setPosition(LatLng paramLatLng) {}
  
  public void title(String paramString) {}
  
  public String getTitle()
  {
    return null;
  }
  
  public void setSnippet(String paramString) {}
  
  public String getSnippet()
  {
    return null;
  }
  
  public void a(boolean paramBoolean) {}
  
  public void setIcons(ArrayList<BitmapDescriptor> paramArrayList) {}
  
  public ArrayList<BitmapDescriptor> getIcons()
  {
    return null;
  }
  
  public void icon(BitmapDescriptor paramBitmapDescriptor)
  {
    if (paramBitmapDescriptor == null) {
      return;
    }
    this.g = paramBitmapDescriptor;
    
    this.q = false;
    if (this.i != null)
    {
      this.i.clear();
      this.i = null;
    }
    M();
  }
  
  public BitmapDescriptor L()
  {
    return this.g;
  }
  
  public boolean isDraggable()
  {
    return false;
  }
  
  public void showInfoWindow() {}
  
  public void hideInfoWindow() {}
  
  public boolean isInfoWindowShow()
  {
    return false;
  }
  
  public void setVisible(boolean visible)
  {
    if ((!this.h) && (visible)) {
      this.p = true;
    }
    this.h = visible;
  }
  
  public boolean isVisible()
  {
    return this.h;
  }
  
  public void setAnchor(float paramFloat1, float paramFloat2)
  {
    if ((this.n == paramFloat1) && (this.o == paramFloat2)) {
      return;
    }
    this.n = paramFloat1;
    this.o = paramFloat2;
  }
  
  public boolean a(IMarkerDelegate paramag)
    throws RemoteException
  {
    if ((equals(paramag)) || 
      (paramag.getId().equals(getId()))) {
      return true;
    }
    return false;
  }
  
  public int hashCode()
  {
    return super.hashCode();
  }
  

  
  public boolean calFPoint()
  {
    if (this.f == null) {
      return false;
    }
    IPoint localIPoint = new IPoint();
    this.l.getMapProjection().map2Win(this.f.x, this.f.y, localIPoint);
    int i1 = J();
    int i2 = K();
    int i3 = (int)(localIPoint.x + this.b - i1 * this.n);
    int i4 = (int)(localIPoint.y + this.c + i2 * (1.0F - this.o));

    if ((i3 - i1 > this.l.getMapWidth()) || (i3 < -i1 * 2) || (i4 < -i2 * 2) || 
      (i4 - i2 > this.l.getMapHeight())) {
      return false;
    }
    int i5 = 0;
    int i6 = 0;
    if (this.g == null) {
      return false;
    }
    i5 = this.g.getWidth();
    i6 = this.g.getHeight();
    
    int i7 = this.g.getBitmap().getHeight();
    int i8 = this.g.getBitmap().getWidth();
    float f1 = (float)i5 / (float)i8;
    float f2 = (float)i6 / (float)i7;
    if (this.i == null) {
      this.i = Util.makeFloatBuffer(new float[] { 0.0F, f2, f1, f2, f1, 0.0F, 0.0F, 0.0F });
    }
    float[] arrayOfFloat = new float[12];
    arrayOfFloat[0] = i3;
    
    arrayOfFloat[1] = (this.l.getMapHeight() - i4);
    arrayOfFloat[2] = 0.0F;
    arrayOfFloat[3] = (i3 + i5);
    arrayOfFloat[4] = (this.l.getMapHeight() - i4);
    arrayOfFloat[5] = 0.0F;
    arrayOfFloat[6] = (i3 + i5);
    arrayOfFloat[7] = (this.l.getMapHeight() - i4 + i6);
    arrayOfFloat[8] = 0.0F;
    arrayOfFloat[9] = i3;
    
    arrayOfFloat[10] = (this.l.getMapHeight() - i4 + i6);
    arrayOfFloat[11] = 0.0F;
    if (this.d == null) {
      this.d = Util.makeFloatBuffer(arrayOfFloat);
    } else {
      this.d = Util.makeFloatBuffer(arrayOfFloat, this.d);
    }
    arrayOfFloat = null;
    return true;
  }
  
  private void a(GL10 paramGL10, int paramInt, FloatBuffer paramFloatBuffer1, FloatBuffer paramFloatBuffer2)
  {
    if ((paramFloatBuffer1 == null) || (paramFloatBuffer2 == null)) {
      return;
    }
    paramGL10.glEnable(3042);
    paramGL10.glBlendFunc(1, 771);
    paramGL10.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    
    paramGL10.glEnable(3553);
    paramGL10.glEnableClientState(32884);
    paramGL10.glEnableClientState(32888);
    paramGL10.glBindTexture(3553, paramInt);
    
    paramGL10.glVertexPointer(3, 5126, 0, paramFloatBuffer1);
    paramGL10.glTexCoordPointer(2, 5126, 0, paramFloatBuffer2);
    paramGL10.glDrawArrays(6, 0, 4);
    
    paramGL10.glDisableClientState(32884);
    paramGL10.glDisableClientState(32888);
    paramGL10.glDisable(3553);
    paramGL10.glDisable(3042);
  }
  
  public void onDrawGL(GL10 gl)
  {
    if ((!this.h) || (this.f == null) || (L() == null)) {
      return;
    }
    if (!this.q) {
      try
      {
        if (this.k != 0)
        {
          gl.glDeleteTextures(1, new int[]{this.k}, 0);
          this.l.deleteTexsureId(this.k);
        }
        this.k = b(gl);
        if (this.g != null)
        {
          Bitmap localBitmap = this.g.getBitmap();
          if ((localBitmap != null) && (!localBitmap.isRecycled())) {
            Util.a(gl, this.k, localBitmap);
          }
          this.q = true;
        }
      }
      catch (Throwable localThrowable)
      {
        SDKLogHandler.exception(localThrowable, "PopupOverlay", "drawMarker");
        localThrowable.printStackTrace();
        return;
      }
    }
    if (calFPoint())
    {
      gl.glLoadIdentity();
      gl.glViewport(0, 0, this.l.getMapWidth(), this.l.getMapHeight());
      gl.glMatrixMode(5889);
      gl.glLoadIdentity();
      gl.glOrthof(0.0F, this.l.getMapWidth(), 0.0F, this.l.getMapHeight(), 1.0F, -1.0F);
      
      a(gl, this.k, this.d, this.i);
      if (this.p)
      {
        a();
        this.p = false;
      }
    }
  }
  
  public void a() {}
  
  private int b(GL10 paramGL10)
  {
    int i1 = this.l.getTexsureId();
    if (i1 == 0)
    {
      int[] arrayOfInt = { 0 };
      paramGL10.glGenTextures(1, arrayOfInt, 0);
      i1 = arrayOfInt[0];
    }
    return i1;
  }
  

  
  public boolean isAnimator()
  {
    return this.r;
  }
  
  public void setPeriod(int paramInt)
  {
    if (paramInt <= 1) {
      this.s = 1;
    } else {
      this.s = paramInt;
    }
  }
  
  public void setObject(Object paramObject)
  {
    this.j = paramObject;
  }
  
  public Object getObject()
  {
    return this.j;
  }
  
  public void setPerspective(boolean paramBoolean) {}
  
  public boolean isPerspective()
  {
    return false;
  }
  
  public int getPeriod()
  {
    return this.s;
  }
  
  public LatLng g()
  {
    return null;
  }
  
  public void setToTop() {}
  
  public void setFlat(boolean paramBoolean)
    throws RemoteException
  {
    M();
  }
  
  public boolean isFlat()
  {
    return false;
  }
  
  public void setRotateAngle(float paramFloat)
    throws RemoteException
  {}
  
  public void destroy() {}
  
  public void drawMarker(GL10 paramGL10, IMapDelegate paramaa) {}
  
  public float getRotateAngle()
  {
    return 0.0F;
  }
  
  public void b(int paramInt1, int paramInt2)
    throws RemoteException
  {
    this.b = paramInt1;
    this.c = paramInt2;
  }
  
  public int B()
  {
    return this.b;
  }
  
  public int C()
  {
    return this.c;
  }
  
  public void setPositionByPixels(int width, int height) {}
  
  public int D()
  {
    return 0;
  }
  
  public int E()
  {
    return 0;
  }
  
  public FPoint anchorUVoff()
  {
    return this.f;
  }
  
  public boolean F()
  {
    return false;
  }
  
  public void setZIndex(float paramFloat) {}
  
  public float getZIndex()
  {
    return 0.0F;
  }
  
  public boolean isInScreen()
  {
    return false;
  }
  
  public void windowShowing(boolean paramBoolean) {}
  
  public void setGeoPoint(IPoint paramIPoint) {}
  
  public IPoint getGeoPoint()
  {
    return null;
  }

  @Override
  public long getTileId() {
    return 0;
  }

  @Override
  public void setTileId(long tileId) {

  }
}
