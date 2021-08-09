package com.unistrong.api.mapcore;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.opengl.GLES10;
import android.os.Build;
import android.os.RemoteException;
import android.util.Log;
import com.unistrong.api.mapcore.util.Util;
import com.unistrong.api.mapcore.util.SDKLogHandler;
import com.unistrong.api.mapcore.util.dx;
import com.unistrong.api.maps.UnistrongException;
import com.unistrong.api.maps.model.BitmapDescriptor;
import com.unistrong.api.maps.model.BitmapDescriptorFactory;
import com.unistrong.api.maps.model.LatLng;
import com.unistrong.api.maps.model.MarkerOptions;
import com.leador.mapcore.DPoint;
import com.leador.mapcore.FPoint;
import com.leador.mapcore.IPoint;
import com.leador.mapcore.MapProjection;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.microedition.khronos.opengles.GL10;

class MarkerDelegateImp
  implements IMarkerDelegate
{
  private static int index = 0;

  private static String CreateId(String type) {
    index++;
    return type + index;
  }
  
  private boolean bitmapBufferInit = false;//纹理顶点坐标缓存是否初始化
  private boolean windowShowing = false;//Tip是否显示
  private boolean d = false;
  private float e = 0.0F;
  private float rotateAngle = 0.0F;
  private boolean flat = false;
  private int offsetX = 0;
  private int offsetY = 0;
  private int j = 0;
  private int k = 0;
  private int x;
  private int y;
  private FPoint glPoint = new FPoint();
  private float[] pointFloatArray;
  private int[] TextureIDs = null;
  private float zIndex = 0.0F;
  private long tileId;
  
  public void setRotateAngle(float rotateAngle)
  {
    this.rotateAngle = rotateAngle;
    this.e = ((-rotateAngle % 360.0F + 360.0F) % 360.0F);
    if (isInfoWindowShow())
    {
      this.mapOverlay.hideInfoWindow(this);
      this.mapOverlay.showInfoWindow(this);
    }
    highFreq();
  }
  
  private boolean r = false;
  
  public boolean x()
  {
    return this.r;
  }
  
  public synchronized void realdestroy()
  {
    if (this.r) {
      try
      {
        remove();
        if (this.icons != null)
        {
          for (BitmapDescriptor localBitmapDescriptor : this.icons) {
            localBitmapDescriptor.recycle();
          }
          this.icons = null;
        }
        if (this.coordBuffer != null)
        {
          this.coordBuffer.clear();
          this.coordBuffer = null;
        }
        if (this.verticesBuffer != null)
        {
          this.verticesBuffer.clear();
          this.verticesBuffer = null;
        }
        this.lonlatPoint = null;
        this.object = null;
        this.TextureIDs = null;
      }
      catch (Throwable localThrowable)
      {
        SDKLogHandler.exception(localThrowable, "MarkerDelegateImp", "realdestroy");
        
        localThrowable.printStackTrace();
        Log.d("destroy erro", "MarkerDelegateImp destroy");
      }
    }
  }
  
  public void destroy()
  {
    try
    {
      this.r = true;
      remove();
      if (this.mapOverlay != null)
      {
        this.mapOverlay.getMapDelegate().N();
        for (int i1 = 0; (this.TextureIDs != null) && (i1 < this.TextureIDs.length); i1++)
        {
          this.mapOverlay.recycleId(Integer.valueOf(this.TextureIDs[i1]));
          this.mapOverlay.removeMarker(this.TextureIDs[i1]);
        }
      }
      for (int i1 = 0; (this.icons != null) && (i1 < this.icons.size()); i1++) {
        ((BitmapDescriptor)this.icons.get(i1)).recycle();
      }
    }
    catch (Throwable localThrowable)
    {
      SDKLogHandler.exception(localThrowable, "MarkerDelegateImp", "destroy");
      localThrowable.printStackTrace();
      Log.d("destroy erro", "MarkerDelegateImp destroy");
    }
  }
  
  private FloatBuffer verticesBuffer = null;//s
  private String sid;//t
  private LatLng lonlatPoint;
  private LatLng v;
  private String title;
  private String snippet;
  private float anchorU = 0.5F;
  private float anchorV = 1.0F;
  private boolean isDraggable = false;
  private boolean isVisible = true;
  private IMapOverlayImageView mapOverlay;
  private FloatBuffer coordBuffer;
  private Object object;
  private boolean perspective = false;
  private CopyOnWriteArrayList<BitmapDescriptor> icons = null;
  private boolean isPerspective = false;
  
  synchronized void clearIcons()
  {
    if (this.icons == null) {
      this.icons = new CopyOnWriteArrayList<BitmapDescriptor>();
    } else {
      this.icons.clear();
    }
  }
  
  public synchronized void icons(ArrayList<BitmapDescriptor> icons)
  {
    clearIcons();
    if (icons != null) {
      for (BitmapDescriptor icon : icons) {
        if (icon != null) {
          this.icons.add(icon);
        }
      }
    }
  }
  
  public MarkerDelegateImp(MarkerOptions options, IMapOverlayImageView mapOverlay)
  {
    this.mapOverlay = mapOverlay;
    this.lonlatPoint = options.getPosition();
    IPoint position = new IPoint();
    this.isPerspective = options.isGps();
    if (options.getPosition() != null) {
      if (this.isPerspective) {
        try
        {
          double[] xy = dx.a(options.getPosition().longitude, options.getPosition().latitude);
          this.v = new LatLng(xy[1], xy[0]);
          MapProjection.lonlat2Geo(xy[0], xy[1], position);
        }
        catch (Throwable throwable)
        {
          SDKLogHandler.exception(throwable, "MarkerDelegateImp", "create");
          
          this.v = options.getPosition();
        }
      } else {
        MapProjection.lonlat2Geo(this.lonlatPoint.longitude, this.lonlatPoint.latitude, position);
      }
    }
    this.x = position.x;
    this.y = position.y;
    this.anchorU = options.getAnchorU();
    this.anchorV = options.getAnchorV();
    this.offsetX = options.getInfoWindowOffsetX();
    this.offsetY = options.getInfoWindowOffsetY();
    this.period = options.getPeriod();
    this.zIndex = options.getZIndex();
    calFPoint();
    
    icons(options.getIcons());
    
    this.isVisible = options.isVisible();
    this.snippet = options.getSnippet();
    this.title = options.getTitle();
    this.isDraggable = options.isDraggable();
    this.sid = getId();
    this.perspective = options.isPerspective();
    this.flat = options.isFlat();
  }
  
  public int getIconWidth()
  {
    try
    {
      return getBitmapDescriptor().getWidth();
    }
    catch (Throwable localThrowable) {}
    return 0;
  }
  
  public int getIconHeight()
  {
    try
    {
      return getBitmapDescriptor().getHeight();
    }
    catch (Throwable localThrowable) {}
    return 0;
  }
  
  public Rect getRect()
  {
    if (this.pointFloatArray == null) {
      return new Rect(0, 0, 0, 0);
    }
    try
    {
      MapProjection localMapProjection = this.mapOverlay.getMapDelegate().getMapProjection();
      int i1 = getIconWidth();
      int i2 = getIconHeight();
      IPoint localIPoint1 = new IPoint();
      IPoint localIPoint2 = new IPoint();
      localMapProjection.map2Win(this.glPoint.x, this.glPoint.y, localIPoint1);
      Rect localRect = null;
      if (this.flat)
      {
        localMapProjection.map2Win(this.pointFloatArray[0], this.pointFloatArray[1], localIPoint2);
        localRect = new Rect(localIPoint2.x, localIPoint2.y, localIPoint2.x, localIPoint2.y);
        
        localMapProjection.map2Win(this.pointFloatArray[3], this.pointFloatArray[4], localIPoint2);
        localRect.union(localIPoint2.x, localIPoint2.y);
        localMapProjection.map2Win(this.pointFloatArray[6], this.pointFloatArray[7], localIPoint2);
        localRect.union(localIPoint2.x, localIPoint2.y);
        localMapProjection.map2Win(this.pointFloatArray[9], this.pointFloatArray[10], localIPoint2);
        localRect.union(localIPoint2.x, localIPoint2.y);
      }
      else
      {
        a(-this.anchorU * i1, (this.anchorV - 1.0F) * i2, localIPoint2);
        
        localRect = new Rect(localIPoint1.x + localIPoint2.x, localIPoint1.y - localIPoint2.y, localIPoint1.x + localIPoint2.x, localIPoint1.y - localIPoint2.y);
        
        a(-this.anchorU * i1, this.anchorV * i2, localIPoint2);
        
        localRect.union(localIPoint1.x + localIPoint2.x, localIPoint1.y - localIPoint2.y);
        
        a((1.0F - this.anchorU) * i1, this.anchorV * i2, localIPoint2);
        
        localRect.union(localIPoint1.x + localIPoint2.x, localIPoint1.y - localIPoint2.y);
        
        a((1.0F - this.anchorU) * i1, (this.anchorV - 1.0F) * i2, localIPoint2);
        
        localRect.union(localIPoint1.x + localIPoint2.x, localIPoint1.y - localIPoint2.y);
      }
      this.j = (localRect.centerX() - localIPoint1.x);
      this.k = (localRect.top - localIPoint1.y);
      return localRect;
    }
    catch (Throwable localThrowable)
    {
      SDKLogHandler.exception(localThrowable, "MarkerDelegateImp", "getRect");
      localThrowable.printStackTrace();
    }
    return new Rect(0, 0, 0, 0);
  }
  
  public synchronized boolean remove()
  {
    highFreq();
    this.isVisible = false;
    
    boolean bool = false;
    if (this.mapOverlay != null) {
      bool = this.mapOverlay.removeMarker(this);
    }
    return bool;
  }

  /**
   * 高频刷新
   */
  private void highFreq()
  {
    if (this.mapOverlay.getMapDelegate() != null) {
      this.mapOverlay.getMapDelegate().setRunLowFrame(false);
    }
  }
  
  public LatLng getPosition()
  {
    if ((this.isPixePoint) && (this.glPoint != null))
    {
      DPoint localDPoint = new DPoint();
      IPoint localIPoint = new IPoint();
      calFPoint();
      this.mapOverlay.getMapDelegate().a(this.glPoint.x, this.glPoint.y, localIPoint);
      MapProjection.geo2LonLat(localIPoint.x, localIPoint.y, localDPoint);
      return new LatLng(localDPoint.y, localDPoint.x);
    }
    return this.lonlatPoint;
  }
  
  public String getId()
  {
    if (this.sid == null) {
      this.sid = CreateId("Marker");
    }
    return this.sid;
  }
  
  public void setPosition(LatLng position)
  {
    if (position == null)
    {
      SDKLogHandler.exception(new UnistrongException("非法坐标值 latlng is null"), "setPosition", "Marker");
      return;
    }
    this.lonlatPoint = position;
    IPoint localIPoint = new IPoint();
    if (this.isPerspective) {
      try
      {
        double[] arrayOfDouble = dx.a(position.longitude, position.latitude);
        
        this.v = new LatLng(arrayOfDouble[1], arrayOfDouble[0]);
        MapProjection.lonlat2Geo(arrayOfDouble[0], arrayOfDouble[1], localIPoint);
      }
      catch (Throwable localThrowable)
      {
        this.v = position;
      }
    } else {
      MapProjection.lonlat2Geo(position.longitude, position.latitude, localIPoint);
    }
    this.x = localIPoint.x;
    this.y = localIPoint.y;
    this.isPixePoint = false;
    calFPoint();
    highFreq();
  }
  
  public void title(String title)
  {
    this.title = title;
    highFreq();
  }
  
  public String getTitle()
  {
    return this.title;
  }
  
  public void setSnippet(String snippet)
  {
    this.snippet = snippet;
    highFreq();
  }
  
  public String getSnippet()
  {
    return this.snippet;
  }
  
  public void a(boolean paramBoolean)
  {
    this.isDraggable = paramBoolean;
    highFreq();
  }
  
  public synchronized void setIcons(ArrayList<BitmapDescriptor> paramArrayList)
  {
    try
    {
      if ((paramArrayList == null) || (this.icons == null)) {
        return;
      }
      icons(paramArrayList);
      this.initTexture = false;
      this.bitmapBufferInit = false;
      if (this.coordBuffer != null)
      {
        this.coordBuffer.clear();
        this.coordBuffer = null;
      }
      this.TextureIDs = null;
      if (isInfoWindowShow())
      {
        this.mapOverlay.hideInfoWindow(this);
        this.mapOverlay.showInfoWindow(this);
      }
      highFreq();
    }
    catch (Throwable localThrowable)
    {
      SDKLogHandler.exception(localThrowable, "MarkerDelegateImp", "setIcons");
      localThrowable.printStackTrace();
    }
  }
  
  public synchronized ArrayList<BitmapDescriptor> getIcons()
  {
    if ((this.icons != null) && (this.icons.size() > 0))
    {
      ArrayList<BitmapDescriptor> localArrayList = new ArrayList<BitmapDescriptor>();
      for (BitmapDescriptor localBitmapDescriptor : this.icons) {
        localArrayList.add(localBitmapDescriptor);
      }
      return localArrayList;
    }
    return null;
  }
  
  public synchronized void icon(BitmapDescriptor icon)
  {
    try
    {
      if ((icon == null) || (this.icons == null)) {
        return;
      }
      this.icons.clear();
      this.icons.add(icon);
      
      this.initTexture = false;
      this.bitmapBufferInit = false;
      this.TextureIDs = null;
      if (this.coordBuffer != null)
      {
        this.coordBuffer.clear();
        this.coordBuffer = null;
      }
      if (isInfoWindowShow())
      {
        this.mapOverlay.hideInfoWindow(this);
        this.mapOverlay.showInfoWindow(this);
      }
      highFreq();
    }
    catch (Throwable localThrowable)
    {
      SDKLogHandler.exception(localThrowable, "MarkerDelegateImp", "setIcon");
      localThrowable.printStackTrace();
    }
  }
  
  public synchronized BitmapDescriptor getBitmapDescriptor()
  {
    try
    {
      if ((this.icons == null) || (this.icons.size() == 0))
      {
        clearIcons();
        this.icons.add(BitmapDescriptorFactory.defaultMarker());
      }
      else if (this.icons.get(0) == null)
      {
        this.icons.clear();
        return getBitmapDescriptor();
      }
      return (BitmapDescriptor)this.icons.get(0);
    }
    catch (Throwable localThrowable)
    {
      SDKLogHandler.exception(localThrowable, "MarkerDelegateImp", "getBitmapDescriptor");
      
      localThrowable.printStackTrace();
    }
    return null;
  }

  /**
   * 是否可拖拽
   * @return
   */
  public boolean isDraggable()
  {
    return this.isDraggable;
  }//k
  
  public void showInfoWindow()
  {
    if (!this.isVisible) {
      return;
    }
    this.mapOverlay.showInfoWindow(this);
    highFreq();
  }
  
  public void hideInfoWindow()
  {
    if (isInfoWindowShow())
    {
      this.mapOverlay.hideInfoWindow(this);
      highFreq();
      this.windowShowing = false;
    }
    this.d = false;
  }
  
  public void windowShowing(boolean isShowing)
  {
    this.windowShowing = isShowing;
    if ((this.windowShowing) && (this.isPixePoint)) {
      this.d = true;
    }
  }
  
  public boolean isInfoWindowShow()
  {
    return this.windowShowing;
  }
  
  public void setVisible(boolean visible)
  {
    if (this.isVisible == visible) {
      return;
    }
    this.isVisible = visible;
    if ((!visible) && (isInfoWindowShow())) {
      this.mapOverlay.hideInfoWindow(this);
    }
    highFreq();
  }
  
  public boolean isVisible()
  {
    return this.isVisible;
  }
  /**
   * 设置当前marker的锚点。 锚点是定位图标接触地图平面的点。图标的左上顶点为（0,0）点，右下点为（1,1）点。默认情况下，锚点为（0.5,1.0）。
   * @param anchorU - 锚点水平范围的比例。
   * @param anchorV - 锚点垂直范围的比例。
   */
  public void setAnchor(float anchorU, float anchorV)
  {
    if ((this.anchorU == anchorU) && (this.anchorV == anchorV)) {
      return;
    }
    this.anchorU = anchorU;
    this.anchorV = anchorV;
    if (isInfoWindowShow())
    {
      this.mapOverlay.hideInfoWindow(this);
      this.mapOverlay.showInfoWindow(this);
    }
    highFreq();
  }
  
  public boolean a(IMarkerDelegate marker)
    throws RemoteException
  {
    if ((equals(marker)) ||
      (marker.getId().equals(getId()))) {
      return true;
    }
    return false;
  }
  
  public int hashCode()
  {
    return super.hashCode();
  }
  
  private boolean initTexture = false;//是否初始化纹理
  
  public boolean calFPoint()
  {
    if (this.isPixePoint) {
      this.mapOverlay.getMapDelegate().getMapProjection().win2Map(this.xPixel, this.yPixel, this.glPoint);
    } else {
      this.mapOverlay.getMapDelegate().getMapProjection().geo2Map(this.x, this.y, this.glPoint);
    }
    return true;
  }
  
  private void drawMarker(IMapDelegate map)
    throws RemoteException
  {
    float[] pointFloats = Util.makeFloatArray(map, this.flat ? 1 : 0, this.glPoint, this.e, getIconWidth(), getIconHeight(), this.anchorU, this.anchorV);
    this.pointFloatArray = ((float[])pointFloats.clone());
    if (this.verticesBuffer == null) {
      this.verticesBuffer = Util.makeFloatBuffer(pointFloats);
    } else {
      this.verticesBuffer = Util.makeFloatBuffer(pointFloats, this.verticesBuffer);
    }
    pointFloats = null;
    if ((this.icons != null) && (this.icons.size() > 0))
    {
      this.K += 1;
      int i1 = this.period * this.icons.size();
      if (this.K >= i1) {
        this.K = 0;
      }
      int i2 = this.K / this.period;
      if (!this.isOneIcon) {
        highFreq();
      }
      if ((this.TextureIDs != null) && (this.TextureIDs.length > 0)) {
        a(this.TextureIDs[(i2 % this.icons.size())], this.verticesBuffer, this.coordBuffer);
      }
    }
  }
  
  private void a(float paramFloat1, float paramFloat2, IPoint paramIPoint)
  {
    float f1 = (float)(Math.PI * this.e / 180.0D);
    paramIPoint.x = ((int)(paramFloat1 * Math.cos(f1) + paramFloat2 * Math.sin(f1)));
    paramIPoint.y = ((int)(paramFloat2 * Math.cos(f1) - paramFloat1 * Math.sin(f1)));
  }
  
  private void a(int textureId, FloatBuffer pvertices, FloatBuffer coordBuffer)
  {
    if ((textureId == 0) || (pvertices == null) || (coordBuffer == null)) {
      return;
    }
    GLES10.glEnable(GL10.GL_BLEND);
    GLES10.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
    GLES10.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    
    GLES10.glEnable(GL10.GL_TEXTURE_2D);
    GLES10.glEnableClientState(GL10.GL_VERTEX_ARRAY);
    GLES10.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
    GLES10.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
    
    GLES10.glVertexPointer(3, GL10.GL_FLOAT, 0, pvertices);
    GLES10.glTexCoordPointer(2, GL10.GL_FLOAT, 0, coordBuffer);
    GLES10.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 4);
    
    GLES10.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    GLES10.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
    GLES10.glDisable(GL10.GL_TEXTURE_2D);
    GLES10.glDisable(GL10.GL_BLEND);
  }
  
  public void drawMarker(GL10 gl, IMapDelegate map)
  {
    if ((!this.isVisible) || ((this.lonlatPoint == null) && (!this.isPixePoint)) || ((getBitmapDescriptor() == null) && (this.icons == null))) {
      return;
    }
    int textureid;
    if (!this.initTexture) {
      try
      {
        if (this.icons != null)
        {
          this.TextureIDs = new int[this.icons.size()];
          int i1 = 0;textureid = 0;
          int i4 = Build.VERSION.SDK_INT >= 12 ? 1 : 0;
          for (BitmapDescriptor bitmapDes : this.icons)
          {
            if (i4 != 0) {
              textureid = this.mapOverlay.getTextureID(bitmapDes);
            }
            if (textureid == 0)
            {
              Bitmap bitmap = bitmapDes.getBitmap();
              if ((bitmap != null) && (!bitmap.isRecycled()))
              {
                textureid = getTextureID(gl);
                if (i4 != 0) {
                  this.mapOverlay.addTexture(new OverlayTextureItem(bitmapDes, textureid));
                }
                Util.bindTexture(gl, textureid, bitmap, false);
              }
            }
            this.TextureIDs[i1] = textureid;
            i1++;
          }
          if (this.icons.size() == 1) {
            this.isOneIcon = true;
          } else {
            this.isOneIcon = false;
          }
          this.initTexture = true;
        }
      }
      catch (Throwable throwable)
      {
        SDKLogHandler.exception(throwable, "MarkerDelegateImp", "loadtexture");
        
        return;
      }
    }
    try
    {
      if (!this.bitmapBufferInit)
      {
        if (this.coordBuffer == null)
        {
          int i2 = 0;
          int i3 = 0;
          BitmapDescriptor bitmapDes = getBitmapDescriptor();
          if (bitmapDes == null) {
            return;
          }
          i2 = bitmapDes.getWidth();
          i3 = bitmapDes.getHeight();
          
          int i5 = bitmapDes.getBitmap().getHeight();
          int i6 = bitmapDes.getBitmap().getWidth();
          float f1 = ((float)i2) / ((float)i6);
          float f2 = ((float)i3) / ((float)i5);
          this.coordBuffer = Util.makeFloatBuffer(new float[] { 0.0F, f2, f1, f2, f1, 0.0F, 0.0F, 0.0F });
        }
        calFPoint();
        this.P = System.currentTimeMillis();
        this.bitmapBufferInit = true;
      }
      if (this.isPixePoint) {
        map.win2Map(this.xPixel, this.yPixel, this.glPoint);
      }
      drawMarker(map);
      if ((this.d) && 
        (isInfoWindowShow()))
      {
        this.mapOverlay.postDraw();
        if (System.currentTimeMillis() - this.P > 1000L) {
          this.d = false;
        }
      }
    }
    catch (Throwable e)
    {
      SDKLogHandler.exception(e, "MarkerDelegateImp", "drawMarker");
    }
  }
  
  private int getTextureID(GL10 gl)
  {
    int texsureId = this.mapOverlay.getMapDelegate().getTexsureId();
    if (texsureId == 0)
    {
      int[] arrayOfInt = { 0 };
      gl.glGenTextures(1, arrayOfInt, 0);
      texsureId = arrayOfInt[0];
    }
    return texsureId;
  }
  
  private boolean isOneIcon = true;//动画效果
  private int K = 0;
  private int period = 20;
  private boolean isPixePoint = false;//是否屏幕坐标
  private int xPixel;
  private int yPixel;
  private long P = 0L;
  
  public boolean isAnimator()
  {
    return this.isOneIcon;
  }
  
  public void setPeriod(int paramInt)
  {
    if (paramInt <= 1) {
      this.period = 1;
    } else {
      this.period = paramInt;
    }
  }
  
  public void setObject(Object paramObject)
  {
    this.object = paramObject;
  }
  
  public Object getObject()
  {
    return this.object;
  }
  
  public void setPerspective(boolean paramBoolean)
  {
    this.perspective = paramBoolean;
  }
  
  public boolean isPerspective()
  {
    return this.perspective;
  }
  
  public int getPeriod()
  {
    return this.period;
  }
  
  public LatLng g()
  {
    if (this.isPixePoint)
    {
      this.mapOverlay.getMapDelegate().getMapProjection().win2Map(this.xPixel, this.yPixel, this.glPoint);
      
      DPoint localDPoint = new DPoint();
      this.mapOverlay.getMapDelegate().getPixel2LatLng(this.xPixel, this.yPixel, localDPoint);
      return new LatLng(localDPoint.y, localDPoint.y);
    }
    return this.isPerspective ? this.v : this.lonlatPoint;
  }
  
  public void setToTop()
  {
    this.mapOverlay.set2Top(this);
  }
  
  public void setFlat(boolean paramBoolean)
    throws RemoteException
  {
    this.flat = paramBoolean;
    highFreq();
  }
  
  public boolean isFlat()
  {
    return this.flat;
  }
  
  public float getRotateAngle()
  {
    return this.rotateAngle;
  }
  
  public int B()
  {
    return this.offsetX;
  }
  
  public int C()
  {
    return this.offsetY;
  }
  
  public void setPositionByPixels(int x, int y)//a
  {
    this.xPixel = x;
    this.yPixel = y;
    this.isPixePoint = true;
    
    calFPoint();
    try
    {
      this.pointFloatArray = Util.makeFloatArray(this.mapOverlay.getMapDelegate(), this.flat ? 1 : 0, this.glPoint, this.e, getIconWidth(), getIconHeight(), this.anchorU, this.anchorV);
    }
    catch (Throwable localThrowable)
    {
      SDKLogHandler.exception(localThrowable, "MarkerDelegateImp", "setPositionByPixels");
    }
    highFreq();
    if (isInfoWindowShow()) {
      showInfoWindow();
    }
  }
  
  public int D()
  {
    return this.j;
  }
  
  public int E()
  {
    return this.k;
  }
  
  public FPoint anchorUVoff()
  {
    return this.glPoint;
  }
  
  public boolean F()
  {
    return this.isPixePoint;
  }
  
  public void setZIndex(float paramFloat)
  {
    this.zIndex = paramFloat;
    this.mapOverlay.changeIndexs();
  }
  
  public float getZIndex()
  {
    return this.zIndex;
  }
  
  public boolean isInScreen()
  {
    Rect localRect = this.mapOverlay.getMapDelegate().getRect();
    if ((this.isPixePoint) || (localRect == null)) {
      return true;
    }
    IPoint localIPoint = new IPoint();
    if ((this.isPerspective) && (this.v != null)) {
      this.mapOverlay.getMapDelegate().getLatLng2Pixel(this.v.latitude, this.v.longitude, localIPoint);
    } else if (this.lonlatPoint != null) {
      this.mapOverlay.getMapDelegate().getLatLng2Pixel(this.lonlatPoint.latitude, this.lonlatPoint.longitude, localIPoint);
    }
    return localRect.contains(localIPoint.x, localIPoint.y);
  }
  
  public void setGeoPoint(IPoint geoPoint)
  {
    this.isPixePoint = false;
    this.x = geoPoint.x;
    this.y = geoPoint.y;
    DPoint lonlat = new DPoint();
    MapProjection.geo2LonLat(this.x, this.y, lonlat);
    this.lonlatPoint = new LatLng(lonlat.y, lonlat.x, false);
    this.mapOverlay.getMapDelegate().getMapProjection().geo2Map(this.x, this.y, this.glPoint);
  }
  
  public IPoint getGeoPoint()
  {
    IPoint localIPoint = new IPoint();
    if (this.isPixePoint)
    {
      this.mapOverlay.getMapDelegate().getPixel2Geo(this.xPixel, this.yPixel, localIPoint);
      return localIPoint;
    }
    return new IPoint(this.x, this.y);
  }

  @Override
  public long getTileId() {
    return tileId;
  }

  @Override
  public void setTileId(long tileId) {
    this.tileId = tileId;
  }
}
