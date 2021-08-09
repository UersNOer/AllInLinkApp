package com.unistrong.api.mapcore;

import android.graphics.Bitmap;
import android.os.RemoteException;
import android.util.Log;
import com.unistrong.api.mapcore.util.LMapThrowException;
import com.unistrong.api.mapcore.util.Util;
import com.unistrong.api.mapcore.util.SDKLogHandler;
import com.unistrong.api.maps.model.BitmapDescriptor;
import com.unistrong.api.maps.model.LatLng;
import com.unistrong.api.maps.model.LatLngBounds;
import com.leador.mapcore.DPoint;
import com.leador.mapcore.FPoint;

import java.nio.FloatBuffer;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class GroundOverlayDelegateImpDecode
  implements IGroundOverlayDelegateDecode
{
  private IMapDelegate mapDelegate;
  private BitmapDescriptor bitmapDescriptor;//b
  private LatLng c;//c
  private float d;//d
  private float e;//e
  private LatLngBounds bounds;//f
  private float bearing;//g
  private float zIndex;//h
  private boolean visible = true;//i
  private float j = 0.0F;//j
  private float k = 0.5F;//k
  private float l = 0.5F;//l
  private String sid;//m
  private FloatBuffer n = null;//n
  private FloatBuffer o;//o
  private int p;//p
  private boolean q = false;//q
  private boolean r = false;//r
  
  public GroundOverlayDelegateImpDecode(IMapDelegate mapDelegate)
  {
    this.mapDelegate = mapDelegate;
    try
    {
      this.sid = getId();
    }
    catch (RemoteException localRemoteException)
    {
      SDKLogHandler.exception(localRemoteException, "GroundOverlayDelegateImp", "create");
      localRemoteException.printStackTrace();
    }
  }
  
  public void remove()
    throws RemoteException
  {
    this.mapDelegate.deleteTexsureId(this.p);
    this.mapDelegate.removeGLOverlay(getId());
    this.mapDelegate.setRunLowFrame(false);
  }
  
  public String getId()
    throws RemoteException
  {
    if (this.sid == null) {
      this.sid = GLOverlayLayerDecode.CreateId("GroundOverlay");
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
  
  public boolean equals(IOverlayDelegateDecode overlay)
    throws RemoteException
  {
    if ((equals(overlay)) ||
      (overlay.getId().equals(getId()))) {
      return true;
    }
    return false;
  }
  
  public int hashCodeRemote()
    throws RemoteException
  {
    return super.hashCode();
  }
  
  public void calMapFPoint()
    throws RemoteException
  {
    this.r = false;
    if (this.c == null) {
      q();
    } else if (this.bounds == null) {
      p();
    } else {
      r();
    }
  }
  
  private void p()
  {
    if (this.c == null) {
      return;
    }
    double d1 = this.d / (6371000.79D * Math.cos(this.c.latitude * 0.01745329251994329D) * 0.01745329251994329D);
    double d2 = this.e / 111194.94043265979D;
    
    this.bounds = new LatLngBounds(new LatLng(this.c.latitude - (1.0F - this.l) * d2, this.c.longitude - this.k * d1), new LatLng(this.c.latitude + this.l * d2, this.c.longitude + (1.0F - this.k) * d1));
    
    r();
  }
  
  private void q()
  {
    if (this.bounds == null) {
      return;
    }
    LatLng localLatLng1 = this.bounds.southwest;
    LatLng localLatLng2 = this.bounds.northeast;
    
    this.c = new LatLng(localLatLng1.latitude + (1.0F - this.l) * (localLatLng2.latitude - localLatLng1.latitude), localLatLng1.longitude + this.k * (localLatLng2.longitude - localLatLng1.longitude));
    
    this.d = ((float)(6371000.79D * Math.cos(this.c.latitude * 0.01745329251994329D) * (localLatLng2.longitude - localLatLng1.longitude) * 0.01745329251994329D));
    
    this.e = ((float)(6371000.79D * (localLatLng2.latitude - localLatLng1.latitude) * 0.01745329251994329D));
    
    r();
  }
  
  private void r()
  {
    if (this.bounds == null) {
      return;
    }
    float[] arrayOfFloat = new float[12];
    
    FPoint localFPoint1 = new FPoint();
    
    FPoint localFPoint2 = new FPoint();
    
    FPoint localFPoint3 = new FPoint();
    
    FPoint localFPoint4 = new FPoint();
    
    this.mapDelegate.getLatLng2Map(this.bounds.southwest.latitude, this.bounds.southwest.longitude, localFPoint1);
    
    this.mapDelegate.getLatLng2Map(this.bounds.southwest.latitude, this.bounds.northeast.longitude, localFPoint2);
    
    this.mapDelegate.getLatLng2Map(this.bounds.northeast.latitude, this.bounds.northeast.longitude, localFPoint3);
    
    this.mapDelegate.getLatLng2Map(this.bounds.northeast.latitude, this.bounds.southwest.longitude, localFPoint4);
    if (this.bearing != 0.0F)
    {
      double d1 = localFPoint2.x - localFPoint1.x;
      double d2 = localFPoint2.y - localFPoint3.y;
      DPoint localDPoint = new DPoint();
      localDPoint.x = (localFPoint1.x + d1 * this.k);
      localDPoint.y = (localFPoint1.y - d2 * (1.0F - this.l));
      a(localDPoint, 0.0D, 0.0D, d1, d2, localFPoint1);
      a(localDPoint, d1, 0.0D, d1, d2, localFPoint2);
      a(localDPoint, d1, d2, d1, d2, localFPoint3);
      a(localDPoint, 0.0D, d2, d1, d2, localFPoint4);
    }
    arrayOfFloat[0] = localFPoint1.x;
    arrayOfFloat[1] = localFPoint1.y;
    arrayOfFloat[2] = 0.0F;
    
    arrayOfFloat[3] = localFPoint2.x;
    arrayOfFloat[4] = localFPoint2.y;
    arrayOfFloat[5] = 0.0F;
    
    arrayOfFloat[6] = localFPoint3.x;
    arrayOfFloat[7] = localFPoint3.y;
    arrayOfFloat[8] = 0.0F;
    
    arrayOfFloat[9] = localFPoint4.x;
    arrayOfFloat[10] = localFPoint4.y;
    arrayOfFloat[11] = 0.0F;
    if (this.n == null) {
      this.n = Util.makeFloatBuffer(arrayOfFloat);
    } else {
      this.n = Util.makeFloatBuffer(arrayOfFloat, this.n);
    }
    arrayOfFloat = null;
  }
  
  private void a(DPoint paramDPoint, double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, FPoint paramFPoint)
  {
    double d1 = paramDouble1 - paramDouble3 * this.k;
    double d2 = paramDouble4 * (1.0F - this.l) - paramDouble2;
    double d3 = -this.bearing * 0.01745329251994329D;
    
    paramFPoint.x = ((float)(paramDPoint.x + (d1 * Math.cos(d3) + d2 * Math.sin(d3))));
    
    paramFPoint.y = ((float)(paramDPoint.y + (d2 * Math.cos(d3) - d1 * Math.sin(d3))));
  }
  
  private void s()
  {
    if (this.bitmapDescriptor == null) {
      return;
    }
    int width = this.bitmapDescriptor.getWidth();
    int height = this.bitmapDescriptor.getHeight();
    int height1 = this.bitmapDescriptor.getBitmap().getHeight();
    int width1 = this.bitmapDescriptor.getBitmap().getWidth();
    float f1 = ((float)width)/((float)width1);
    float f2 = ((float)height)/((float)height1);
    
    this.o = Util.makeFloatBuffer(new float[] { 0.0F, f2, f1, f2, f1, 0.0F, 0.0F, 0.0F });
  }
  
  public void draw(GL10 paramGL10)
    throws RemoteException
  {
    if ((!this.visible) || ((this.c == null) && (this.bounds == null)) || (this.bitmapDescriptor == null)) {
      return;
    }
    if (!this.q)
    {
      Bitmap bitmap = this.bitmapDescriptor.getBitmap();
      if ((bitmap != null) && (!bitmap.isRecycled()))
      {
        if (this.p == 0)
        {
          this.p = this.mapDelegate.getTexsureId();
          if (this.p == 0)
          {
            int[] arrayOfInt = { 0 };
            paramGL10.glGenTextures(1, arrayOfInt, 0);
            this.p = arrayOfInt[0];
          }
        }
        else
        {
          paramGL10.glDeleteTextures(1, new int[] { this.p }, 0);
        }
        Util.a(paramGL10, this.p, bitmap);
      }
      this.q = true;
    }
    if ((this.d == 0.0F) && (this.e == 0.0F)) {
      return;
    }
    a(paramGL10, this.p, this.n, this.o);
    
    this.r = true;
  }
  
  private void a(GL10 paramGL10, int paramInt, FloatBuffer paramFloatBuffer1, FloatBuffer paramFloatBuffer2)
  {
    if ((paramFloatBuffer1 == null) || (paramFloatBuffer2 == null)) {
      return;
    }
    paramGL10.glEnable(3042);
    paramGL10.glTexEnvf(8960, 8704, 8448.0F);
    
    paramGL10.glBlendFunc(1, 771);
    paramGL10.glColor4f(1.0F, 1.0F, 1.0F, 1.0F - this.j);
    
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
  
  public void destroy()
  {
    try
    {
      remove();
      if (this.bitmapDescriptor != null)
      {
        Bitmap localBitmap = this.bitmapDescriptor.getBitmap();
        if (localBitmap != null)
        {
          localBitmap.recycle();
          localBitmap = null;
          this.bitmapDescriptor = null;
        }
      }
      if (this.o != null)
      {
        this.o.clear();
        this.o = null;
      }
      if (this.n != null)
      {
        this.n.clear();
        this.n = null;
      }
      this.c = null;
      this.bounds = null;
    }
    catch (Throwable localThrowable)
    {
      SDKLogHandler.exception(localThrowable, "GroundOverlayDelegateImp", "destroy");
      localThrowable.printStackTrace();
      Log.d("destroy erro", "GroundOverlayDelegateImp destroy");
    }
  }
  
  public boolean a()
  {
    if (this.bounds == null) {
      return false;
    }
    LatLngBounds localLatLngBounds = this.mapDelegate.getMapBounds();
    if (localLatLngBounds == null) {
      return true;
    }
    return (localLatLngBounds.contains(this.bounds)) || (this.bounds.intersects(localLatLngBounds));
  }
  
  public void setPosition(LatLng paramLatLng)
    throws RemoteException
  {
    this.c = paramLatLng;
    p();
    this.mapDelegate.setRunLowFrame(false);
  }
  
  public LatLng getPosition()
    throws RemoteException
  {
    return this.c;
  }
  
  public void setDimensions(float paramFloat)
    throws RemoteException
  {
    LMapThrowException.bThrowIllegalArgumentException(paramFloat >= 0.0F, "Width must be non-negative");
    if ((this.q) && (this.d != paramFloat))
    {
      this.d = paramFloat;
      this.e = paramFloat;
      p();
    }
    else
    {
      this.d = paramFloat;
      this.e = paramFloat;
    }
    this.mapDelegate.setRunLowFrame(false);
  }
  
  public void setDimensions(float paramFloat1, float paramFloat2)
    throws RemoteException
  {
    LMapThrowException.bThrowIllegalArgumentException(paramFloat1 >= 0.0F, "Width must be non-negative");
    
    LMapThrowException.bThrowIllegalArgumentException(paramFloat2 >= 0.0F, "Height must be non-negative");
    if ((this.q) && (this.d != paramFloat1) && (this.e != paramFloat2))
    {
      this.d = paramFloat1;
      this.e = paramFloat2;
      p();
    }
    else
    {
      this.d = paramFloat1;
      this.e = paramFloat2;
    }
    this.mapDelegate.setRunLowFrame(false);
  }
  
  public float getWidth()
    throws RemoteException
  {
    return this.d;
  }
  
  public float getHeight()
    throws RemoteException
  {
    return this.e;
  }
  
  public void setPositionFromBounds(LatLngBounds paramLatLngBounds)
    throws RemoteException
  {
    this.bounds = paramLatLngBounds;
    q();
    this.mapDelegate.setRunLowFrame(false);
  }
  
  public LatLngBounds getBounds()
    throws RemoteException
  {
    return this.bounds;
  }
  
  public void setBearing(float bearing)
    throws RemoteException
  {
    float mybearing = (bearing % 360.0F + 360.0F) % 360.0F;

    if ((this.q) && (Math.abs(this.bearing - mybearing) >0))
    {
      this.bearing = mybearing;
      r();
    }
    else
    {
      this.bearing = mybearing;
    }
    this.mapDelegate.setRunLowFrame(false);
  }
  
  public float getBearing()
    throws RemoteException
  {
    return this.bearing;
  }
  
  public void setTransparency(float paramFloat)
    throws RemoteException
  {
    LMapThrowException.bThrowIllegalArgumentException((paramFloat >= 0.0F) && (paramFloat <= 1.0F), "Transparency must be in the range [0..1]");
    
    this.j = paramFloat;
    this.mapDelegate.setRunLowFrame(false);
  }
  
  public float getTransparency()
    throws RemoteException
  {
    return this.j;
  }
  
  public void setImage(BitmapDescriptor bitmap)
    throws RemoteException
  {
    this.bitmapDescriptor = bitmap;
    s();
    if (this.q) {
      this.q = false;
    }
    this.mapDelegate.setRunLowFrame(false);
  }
  
  public void setAnchor(float anchorU, float anchorV)
    throws RemoteException
  {
    this.k = anchorU;
    this.l = anchorV;
    this.mapDelegate.setRunLowFrame(false);
  }
  
  public boolean checkInBounds()
  {
    return this.r;
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
