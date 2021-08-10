package com.unistrong.api.mapcore;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.opengl.GLES10;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;
import com.unistrong.api.mapcore.util.SDKLogHandler;
import com.unistrong.api.mapcore.util.Util;
import com.unistrong.api.maps.model.BitmapDescriptor;
import com.unistrong.api.maps.model.LatLng;
import com.unistrong.api.maps.model.LatLngBounds;
import com.unistrong.api.maps.model.TextOptions;
import com.leador.mapcore.FPoint;
import com.leador.mapcore.IPoint;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import javax.microedition.khronos.opengles.GL10;

class TextDelegateImp
  implements ITextDelegate
{
  private static int index = 0;//a
  
  private static String CreateId(String type)
  {
    index += 1;
    return type + index;
  }
  
  private float rotate = 0.0F;
  private float rotateAngle = 0.0F;
  private int d = 4;
  private int e = 32;
  private FPoint glPoint = new FPoint();
  private int textureId;
  private Bitmap bitmap;
  private FloatBuffer floatBuffer = null;
  private String id;
  private LatLng position;
  private float anchorU = 0.5F;
  private float anchorV = 1.0F;//m
  private boolean visible = true;
  private IMapOverlayImageView mapOverlay;
  private FloatBuffer floatBuffer1;
  private Object object;
  private String text;
  private int backgroundColor;
  private int fontColor;
  private int fontSize;
  private Typeface typeface;
  private float zIndex;
  private Rect rect = new Rect();
  private Paint paint = new Paint();
  private Handler handler = new Handler();
  private Runnable runnable = new Runnable(){
	  public void run()
	  {
        initBitmap();
        C = false;
        setRunFrame();
	  }
  };

  public void setRotateAngle(float rotateAngle)
  {
    this.rotateAngle = rotateAngle;
    this.rotate = ((-rotateAngle % 360.0F + 360.0F) % 360.0F);
    setRunFrame();
  }
  
  private boolean B = false;
  
  public boolean x()
  {
    return this.B;
  }
  
  public synchronized void realdestroy()
  {
    if (this.B) {
      try
      {
        remove();
        if (this.bitmap != null)
        {
          this.bitmap.recycle();
          this.bitmap = null;
        }
        if (this.floatBuffer1 != null)
        {
          this.floatBuffer1.clear();
          this.floatBuffer1 = null;
        }
        if (this.floatBuffer != null)
        {
          this.floatBuffer.clear();
          this.floatBuffer = null;
        }
        this.position = null;
        this.object = null;
      }
      catch (Throwable throwable)
      {
        SDKLogHandler.exception(throwable, "TextDelegateImp", "realdestroy");
        
        throwable.printStackTrace();
        Log.d("destroy erro", "TextDelegateImp destroy");
      }
    }
  }
  
  public void destroy()
  {
    try
    {
      this.B = true;
      if ((this.mapOverlay != null) && (this.mapOverlay.getMapDelegate() != null)) {
        this.mapOverlay.getMapDelegate().N();
      }
      this.textureId = 0;
    }
    catch (Throwable throwable)
    {
      SDKLogHandler.exception(throwable, "TextDelegateImp", "destroy");
      throwable.printStackTrace();
      Log.d("destroy erro", "TextDelegateImp destroy");
    }
  }
  
  public TextDelegateImp(TextOptions options, IMapOverlayImageView viewDecode)
    throws RemoteException
  {
    this.mapOverlay = viewDecode;
    if (options.getPosition() != null) {
      this.position = options.getPosition();
    }
    b(options.getAlignX(), options.getAlignY());
    this.visible = options.isVisible();
    this.text = options.getText();
    this.backgroundColor = options.getBackgroundColor();
    this.fontColor = options.getFontColor();
    this.fontSize = options.getFontSize();
    this.object = options.getObject();
    this.zIndex = options.getZIndex();
    this.typeface = options.getTypeface();
    this.id = getId();
    setRotateAngle(options.getRotate());
    initBitmap();
    calFPoint();
  }
  
  private void initBitmap()
  {
    if ((this.text == null) || (this.text.trim().length() <= 0)) {
      return;
    }
    try
    {
      this.paint.setTypeface(this.typeface);
      this.paint.setSubpixelText(true);
      this.paint.setAntiAlias(true);
      this.paint.setStrokeWidth(5.0F);
      this.paint.setStrokeCap(Paint.Cap.ROUND);
      this.paint.setTextSize(this.fontSize);
      this.paint.setTextAlign(Paint.Align.CENTER);
      this.paint.setColor(this.fontColor);
      Paint.FontMetrics fontMetrics = this.paint.getFontMetrics();
      int height = (int)(fontMetrics.descent - fontMetrics.ascent);
      int width = (int)((height - fontMetrics.bottom - fontMetrics.top) / 2.0F);
      this.paint.getTextBounds(this.text, 0, this.text.length(), this.rect);
      Bitmap bitmap = Bitmap.createBitmap(this.rect.width() + 6, height, Bitmap.Config.ARGB_8888);
      
      Canvas canvas = new Canvas(bitmap);
      canvas.drawColor(this.backgroundColor);
      canvas.drawText(this.text, this.rect.centerX() + 3, width, this.paint);
      this.bitmap = bitmap;
      
      this.floatBuffer1 = Util.makeFloatBuffer(new float[] { 0.0F, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 0.0F });
    }
    catch (Throwable throwable)
    {
      SDKLogHandler.exception(throwable, "TextDelegateImp", "initBitmap");
    }
  }
  
  public synchronized boolean remove()
  {
    setRunFrame();
    this.visible = false;
    return this.mapOverlay.removeMarker(this);
  }

  private void setRunFrame()//Q
  {
    if (this.mapOverlay.getMapDelegate() != null) {
      this.mapOverlay.getMapDelegate().setRunLowFrame(false);
    }
  }
  
  public LatLng getPosition()
  {
    return this.position;
  }
  
  public String getId()
  {
    if (this.id == null) {
      this.id = CreateId("Text");
    }
    return this.id;
  }
  
  public void setPosition(LatLng paramLatLng)
  {
    this.position = paramLatLng;
    calFPoint();
    setRunFrame();
  }
  
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
  
  public synchronized void setIcons(ArrayList<BitmapDescriptor> paramArrayList) {}
  
  public synchronized ArrayList<BitmapDescriptor> getIcons()
  {
    return null;
  }
  
  public synchronized void icon(BitmapDescriptor paramBitmapDescriptor) {}
  
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
  
  public void setVisible(boolean paramBoolean)
  {
    if (this.visible == paramBoolean) {
      return;
    }
    this.visible = paramBoolean;
    setRunFrame();
  }
  
  public boolean isVisible()
  {
    return this.visible;
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
  
  public void setAnchor(float paramFloat1, float paramFloat2) {}
  
  public boolean a(IMarkerDelegate paramag)
    throws RemoteException
  {
    if ((equals(paramag)) || 
      (paramag.getId().equals(getId()))) {
      return true;
    }
    return false;
  }
  @Override
  public int hashCode()
  {
    return super.hashCode();
  }
  
  private boolean C = false;
  
  public boolean calFPoint()
  {
    if (this.position == null) {
      return false;
    }
    this.mapOverlay.getMapDelegate().getLatLng2Map(this.position.latitude, this.position.longitude, this.glPoint);
    
    return true;
  }
  
  private void a(IMapDelegate mapDelegate)
    throws RemoteException
  {
    float[] arrayOfFloat = Util.makeFloatArray(mapDelegate, 0, this.glPoint, this.rotate, this.bitmap
      .getWidth(), this.bitmap.getHeight(), this.anchorU, this.anchorV);
    if (this.floatBuffer == null) {
      this.floatBuffer = Util.makeFloatBuffer(arrayOfFloat);
    } else {
      this.floatBuffer = Util.makeFloatBuffer(arrayOfFloat, this.floatBuffer);
    }
    arrayOfFloat = null;
    if (this.textureId != 0) {
      displayTextureLabel(this.textureId, this.floatBuffer, this.floatBuffer1);
    }
  }

  /**
   * 显示贴图
   * @param textureId
   * @param pvertices
   * @param coordBuffer
   */
  private void displayTextureLabel(int textureId, FloatBuffer pvertices, FloatBuffer coordBuffer)
  {
    if ((textureId == 0) || (pvertices == null) || (coordBuffer == null)) {
      return;
    }
    GLES10.glEnable(GL10.GL_BLEND);
    GLES10.glBlendFunc(GL10.GL_ONE,  GL10.GL_ONE_MINUS_SRC_ALPHA);
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
  
  public void drawMarker(GL10 gl, IMapDelegate mapDelegate)
  {
    if ((!this.visible) || (this.position == null) || (this.bitmap == null)) {
      return;
    }
    if (!this.C) {
      try
      {
        if ((this.bitmap != null) && (!this.bitmap.isRecycled()))
        {
          if (this.textureId == 0) {
            this.textureId = getTexsureId(gl);
          }
          Util.bindTexture(gl, this.textureId, this.bitmap, false);
          this.C = true;
          this.bitmap.recycle();
        }
      }
      catch (Throwable localThrowable1)
      {
        SDKLogHandler.exception(localThrowable1, "TextDelegateImp", "loadtexture");
        
        localThrowable1.printStackTrace();
        return;
      }
    }
    try
    {
      a(mapDelegate);
    }
    catch (Throwable localThrowable2)
    {
      SDKLogHandler.exception(localThrowable2, "TextDelegateImp", "drawMarker");
    }
  }
  
  private int getTexsureId(GL10 gl)
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
  
  public boolean isAnimator()
  {
    return true;
  }
  
  public void setPeriod(int paramInt) {}
  
  public void setObject(Object paramObject)
  {
    this.object = paramObject;
  }
  
  public Object getObject()
  {
    return this.object;
  }
  
  public void setPerspective(boolean paramBoolean) {}
  
  public boolean isPerspective()
  {
    return false;
  }
  
  public int getPeriod()
  {
    return 0;
  }
  
  public LatLng g()
  {
    return this.position;
  }
  
  public void setToTop()
  {
    this.mapOverlay.set2Top(this);
  }
  
  public void setFlat(boolean paramBoolean)
    throws RemoteException
  {}
  
  public boolean isFlat()
  {
    return false;
  }
  
  public float getRotateAngle()
  {
    return this.rotateAngle;
  }
  
  public int B()
  {
    return 0;
  }
  
  public int C()
  {
    return 0;
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
    return this.glPoint;
  }
  
  public boolean F()
  {
    return false;
  }
  
  public Rect getRect()
  {
    return null;
  }
  
  public void c(String paramString)
    throws RemoteException
  {
    this.text = paramString;
    R();
  }
  
  public String a()
    throws RemoteException
  {
    return this.text;
  }
  
  public void b(int paramInt)
    throws RemoteException
  {
    this.backgroundColor = paramInt;
    R();
  }
  
  public int J()
    throws RemoteException
  {
    return this.backgroundColor;
  }
  
  public void c(int paramInt)
    throws RemoteException
  {
    this.fontColor = paramInt;
    R();
  }
  
  public int K()
    throws RemoteException
  {
    return this.fontColor;
  }
  
  public void d(int paramInt)
    throws RemoteException
  {
    this.fontSize = paramInt;
    R();
  }
  
  public int L()
    throws RemoteException
  {
    return this.fontSize;
  }
  
  public void a(Typeface paramTypeface)
    throws RemoteException
  {
    this.typeface = paramTypeface;
    R();
  }
  
  public Typeface M()
    throws RemoteException
  {
    return this.typeface;
  }
  
  public void b(int paramInt1, int paramInt2)
    throws RemoteException
  {
    this.d = paramInt1;
    switch (paramInt1)
    {
    case 4: 
      this.anchorU = 0.5F;
      break;
    case 1: 
      this.anchorU = 0.0F;
      break;
    case 2: 
      this.anchorU = 1.0F;
      break;
    case 3: 
    default: 
      this.anchorU = 0.5F;
    }
    this.e = paramInt2;
    switch (paramInt2)
    {
    case 32: 
      this.anchorV = 0.5F;
      break;
    case 8: 
      this.anchorV = 0.0F;
      break;
    case 16: 
      this.anchorV = 1.0F;
      break;
    default: 
      this.anchorV = 0.5F;
    }
    setRunFrame();
  }
  
  public int N()
    throws RemoteException
  {
    return this.d;
  }
  
  public int O()
  {
    return this.e;
  }
  
  private void R()
  {
    this.handler.removeCallbacks(this.runnable);
    this.handler.post(this.runnable);
  }
  
  public boolean isInScreen()
  {
    LatLngBounds localLatLngBounds = this.mapOverlay.getMapDelegate().getMapBounds();
    if ((this.position != null) && (localLatLngBounds != null)) {
      return localLatLngBounds.contains(this.position);
    }
    return true;
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
