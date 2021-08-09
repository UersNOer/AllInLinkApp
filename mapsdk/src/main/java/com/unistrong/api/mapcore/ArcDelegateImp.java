package com.unistrong.api.mapcore;

import android.graphics.Color;
import android.os.RemoteException;
import android.util.Log;

import com.unistrong.api.maps.model.LatLng;
import com.leador.mapcore.DPoint;
import com.leador.mapcore.FPoint;
import com.leador.mapcore.IPoint;
import com.leador.mapcore.MapProjection;
import com.leador.mapcore.NativeLineRenderer;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

class ArcDelegateImp  implements IArcDelegateDecode
{
  private LatLng start;
  private LatLng passed;
  private LatLng end;
  private float width = 10.0F;
  private int strokeColor = -16777216;
  private float zIndex = 0.0F;
  private boolean visible = true;
  private String id;//l
  private IMapDelegate mapDelegate;//m
  private float[] glPointArray; //gl坐标数组
  private int [] pointArray; //20级像素坐标数组
  private int pointArraySize = 0;
  private boolean p = false;
  private double q = 0.0D;
  private double r = 0.0D;
  private double s = 0.0D;
  float alpha;
  float red_color;
  float green_color;
  float blue_color;
  
  public ArcDelegateImp(IMapDelegate mapDelegate)
  {
    this.mapDelegate = mapDelegate;
    try
    {
      this.id = getId();
    }
    catch (RemoteException e)
    {
      e.printStackTrace();
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
    if (this.id == null) {
      this.id = GLOverlayLayerDecode.CreateId("Arc");
    }
    return this.id;
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
    return 0;
  }
  
  public void calMapFPoint()
    throws RemoteException
  {
    if ((this.start == null) || (this.passed == null) || (this.end == null) || (!this.visible)) {
      return;
    }
    try
    {
      this.p = false;
      MapProjection projection = this.mapDelegate.getMapProjection();
      if (!l())
      {
        int i1 = 3;
        FPoint[] arrayOfFPoint = new FPoint[i1];
        this.glPointArray = new float[i1 * arrayOfFPoint.length];
        FPoint localObject = new FPoint();
        this.mapDelegate.getLatLng2Map(this.start.latitude, this.start.longitude, (FPoint) localObject);
        
        arrayOfFPoint[0] = localObject;
        FPoint localFPoint1 = new FPoint();
        this.mapDelegate.getLatLng2Map(this.passed.latitude, this.passed.longitude, localFPoint1);
        
        arrayOfFPoint[1] = localFPoint1;
        FPoint localFPoint2 = new FPoint();
        this.mapDelegate.getLatLng2Map(this.end.latitude, this.end.longitude, localFPoint2);
        
        arrayOfFPoint[2] = localFPoint2;
        for (int i2 = 0; i2 < i1; i2++)
        {
          this.glPointArray[(i2 * 3)] = arrayOfFPoint[i2].x;
          this.glPointArray[(i2 * 3 + 1)] = arrayOfFPoint[i2].y;
          this.glPointArray[(i2 * 3 + 2)] = 0.0F;
        }

        //初始化20级像素坐标
        IPoint[] arrayOfIPoint = new IPoint[i1];
        this.pointArray = new int[2 * arrayOfIPoint.length];
        IPoint pointTemp = new IPoint();
        this.mapDelegate.latlon2Geo(this.start.latitude, this.start.longitude, pointTemp);
        arrayOfIPoint[0] = pointTemp;

        this.mapDelegate.latlon2Geo(this.passed.latitude, this.passed.longitude, pointTemp);
        arrayOfIPoint[1] = pointTemp;

        this.mapDelegate.latlon2Geo(this.end.latitude, this.end.longitude, pointTemp);
        arrayOfIPoint[2] = pointTemp;

        for (int i2 = 0; i2 < i1; i2++)
        {
          this.glPointArray[(i2 * 2)] = arrayOfIPoint[i2].x;
          this.glPointArray[(i2 * 2 + 1)] = arrayOfIPoint[i2].y;
        }

        this.pointArraySize = arrayOfFPoint.length;
      }
      else {

        Object localObject = m();
        int i1 = (int) (Math.abs(this.s - this.r) * 180.0D / Math.PI);
        double d1 = (this.s - this.r) / i1;
        FPoint[] arrayOfFPoint = new FPoint[i1 + 1];
        this.glPointArray = new float[3 * arrayOfFPoint.length];
        this.pointArray = new int[2 * arrayOfFPoint.length];
        for (int i2 = 0; i2 <= i1; i2++) {
          if (i2 == i1) {
            FPoint localFPoint3 = new FPoint();
            this.mapDelegate.getLatLng2Map(this.end.latitude, this.end.longitude, localFPoint3);

            arrayOfFPoint[i2] = localFPoint3;

            IPoint pointTemp = new IPoint();
            this.mapDelegate.latlon2Geo(this.end.latitude, this.end.longitude, pointTemp);
            this.pointArray[i2 * 2] = pointTemp.x;
            this.pointArray[i2 * 2 + 1] = pointTemp.y;
            continue;
          } else {
            arrayOfFPoint[i2] = a(projection, this.r + i2 * d1, ((DPoint) localObject).x, ((DPoint) localObject).y);
          }
          //arrayOfFPoint[i2] = a(localMapProjection, this.r + i2 * d1, ((DPoint)localObject).x, ((DPoint)localObject).y);


          this.glPointArray[(i2 * 3)] = arrayOfFPoint[i2].x;
          this.glPointArray[(i2 * 3 + 1)] = arrayOfFPoint[i2].y;
          this.glPointArray[(i2 * 3 + 2)] = 0.0F;

          IPoint value = aaaaa(projection, this.r + i2 * d1, ((DPoint) localObject).x, ((DPoint) localObject).y);
          this.pointArray[i2 * 2] = value.x;
          this.pointArray[i2 * 2 + 1] = value.y;
        }

        this.pointArraySize = arrayOfFPoint.length;
      }

      //对最终的坐标点进行插值
      ArrayList<IPoint> desPoints = new ArrayList<IPoint>();
      desPoints.add(new IPoint(this.pointArray[0],this.pointArray[1]));
      for( int i = 1 ; i < this.pointArraySize ; i ++)
      {
        insert1(this.pointArray[i*2-2], this.pointArray[i*2-1],this.pointArray[i*2], this.pointArray[i*2+1], desPoints);
      }

      this.pointArray = new int[desPoints.size() * 2];
      for( int i = 0 ; i < desPoints.size() ; i ++ ){
        this.pointArray[i*2] = desPoints.get(i).x;
        this.pointArray[i*2+1] = desPoints.get(i).y;
      }

    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
  }
  
  private FPoint a(MapProjection paramMapProjection, double paramDouble1, double paramDouble2, double paramDouble3)
  {
    double d1 = Math.cos(paramDouble1) * this.q;
    double d2 = -Math.sin(paramDouble1) * this.q;
    
    int i1 = (int)(paramDouble2 + d1);
    int i2 = (int)(paramDouble3 + d2);
    FPoint localFPoint = new FPoint();
    paramMapProjection.geo2Map(i1, i2, localFPoint);
    
    return localFPoint;
  }

  private IPoint aaaaa(MapProjection paramMapProjection, double paramDouble1, double paramDouble2, double paramDouble3)
  {
    double d1 = Math.cos(paramDouble1) * this.q;
    double d2 = -Math.sin(paramDouble1) * this.q;

    int i1 = (int)(paramDouble2 + d1);
    int i2 = (int)(paramDouble3 + d2);
    IPoint localFPoint = new IPoint();
    localFPoint.x = i1;
    localFPoint.y = i2;

    return localFPoint;
  }
  
  private boolean l()
  {
    double d1 = (this.start.latitude - this.passed.latitude) * (this.passed.longitude - this.end.longitude) - (this.start.longitude - this.passed.longitude) * (this.passed.latitude - this.end.latitude);
    if (Math.abs(d1) < 1.0E-6D) {
      return false;
    }
    return true;
  }
  
  private DPoint m()
  {
    IPoint localIPoint1 = new IPoint();
    this.mapDelegate.latlon2Geo(this.start.latitude, this.start.longitude, localIPoint1);
    IPoint localIPoint2 = new IPoint();
    this.mapDelegate.latlon2Geo(this.passed.latitude, this.passed.longitude, localIPoint2);
    
    IPoint localIPoint3 = new IPoint();
    this.mapDelegate.latlon2Geo(this.end.latitude, this.end.longitude, localIPoint3);
    double startx = localIPoint1.x;
    double starty = localIPoint1.y;
    double passx = localIPoint2.x;
    double passy = localIPoint2.y;
    double endx = localIPoint3.x;
    double endy = localIPoint3.y;
    
    double d7 = ((endy - starty) * (passy * passy - starty * starty + passx * passx - startx * startx) + (passy - starty) * (starty * starty - endy * endy + startx * startx - endx * endx)) / (2.0D * (passx - startx) * (endy - starty) - 2.0D * (endx - startx) * (passy - starty));
    
    double d8 = ((endx - startx) * (passx * passx - startx * startx + passy * passy - starty * starty) + (passx - startx) * (startx * startx - endx * endx + starty * starty - endy * endy)) / (2.0D * (passy - starty) * (endx - startx) - 2.0D * (endy - starty) * (passx - startx));
    
    this.q = Math.sqrt((startx - d7) * (startx - d7) + (starty - d8) * (starty - d8));
    
    this.r = a(d7, d8, startx, starty);
    double d9 = a(d7, d8, passx, passy);
    this.s = a(d7, d8, endx, endy);
    if (this.r < this.s)
    {
      if ((d9 <= this.r) || (d9 >= this.s)) {
        this.s -= 6.283185307179586D;
      }
    }
    else if ((d9 <= this.s) || (d9 >= this.r)) {
      this.s += 6.283185307179586D;
    }
    return new DPoint(d7, d8);
  }
  
  private double a(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4)
  {
    double d1 = (paramDouble2 - paramDouble4) / this.q;
    d1 = Math.abs(d1) > 1.0D ? Math.signum(d1) : d1;
    double d2 = Math.asin(d1);
    if (d2 >= 0.0D)
    {
      if (paramDouble3 < paramDouble1) {
        d2 = 3.141592653589793D - Math.abs(d2);
      }
    }
    else if (paramDouble3 < paramDouble1) {
      d2 = 3.141592653589793D - d2;
    } else {
      d2 = 6.283185307179586D + d2;
    }
    return d2;
  }

  private void insert1(int flongitude, int flatitude, int llongitude, int llatitude, List<IPoint> desPoints){

    long  disSquare = getDisSquare(flongitude, flatitude, llongitude, llatitude);
    int  count = (int) (disSquare / 65000000000l);

    if(count > 0){
      count += 1;
      int dx = ( llongitude - flongitude )/count;
      int dy = ( llatitude - flatitude )/count;
      for( int i = 1; i < count; i++ ){
        IPoint insertPoint = new IPoint(flongitude + (i * dx),flatitude + (i * dy));
        desPoints.add(insertPoint);
      }
    }
    desPoints.add(new IPoint(llongitude, llatitude));
  }

  private long getDisSquare(long x1,long y1,long x2,long y2){
    return (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
  }


  
  public void draw(GL10 gl)
    throws RemoteException
  {
    if ((this.start == null) || (this.passed == null) || (this.end == null) || (!this.visible)) {
      return;
    }
    if ((this.glPointArray == null) || (this.pointArraySize == 0)) {
      calMapFPoint();
    }
    if ((this.glPointArray != null) && (this.pointArraySize > 0))
    {

      long stateInstance = this.mapDelegate.getMapProjection().getInstanceHandle();
      //TODO 需要在创建纹理时初始化本信息
      int texOrgPixelLen = 32;
      float texLen = this.mapDelegate.getMapProjection().getMapLenWithWin(texOrgPixelLen); //系统默认的纹理像素宽高都是32像素
      int texid = this.mapDelegate.getLineTextureID(); //获得系统默认的纹理
      float lineWidth = this.mapDelegate.getMapProjection().getMapLenWithWin((int)this.width);

      //虚线的颜色
      int finalcolor = ((int)(this.alpha *255)) << 24 | ((int)(this.red_color * 255)) << 16 | ((int)(this.green_color * 255)) << 8 | ((int)(this.blue_color * 255)) ;
      boolean usecolor = true; //使用颜色替换纹理中的颜色
      boolean complexTex = false; //简单纹理
      NativeLineRenderer.nativeDrawLineByTextureID(pointArray, pointArray.length, stateInstance, texLen, lineWidth, texid, finalcolor, complexTex, usecolor);

    }
    this.p = true;
  }
  
  public void setStrokeWidth(float width)
    throws RemoteException
  {
    this.width = width;
    this.mapDelegate.setRunLowFrame(false);
  }
  
  public float getStrokeWidth()
    throws RemoteException
  {
    return this.width;
  }
  
  public void setStrokeColor(int color)
    throws RemoteException
  {
    this.strokeColor = color;
    this.alpha = (Color.alpha(color) / 255.0F);
    this.red_color = (Color.red(color) / 255.0F);
    this.green_color = (Color.green(color) / 255.0F);
    this.blue_color = (Color.blue(color) / 255.0F);
    this.mapDelegate.setRunLowFrame(false);
  }
  
  public int getStrokeColor()
    throws RemoteException
  {
    return this.strokeColor;
  }
  
  public void destroy()
  {
    try
    {
      this.start = null;
      this.passed = null;
      this.end = null;
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
      Log.d("destroy error", "ArcDelegateImp destroy");
    }
  }
  
  public boolean checkInBounds()
  {
    return this.p;
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

  public void setStart(LatLng paramLatLng)
  {
    this.start = paramLatLng;
  }
  
  public void setPassed(LatLng paramLatLng)
  {
    this.passed = paramLatLng;
  }
  
  public void setEnd(LatLng paramLatLng)
  {
    this.end = paramLatLng;
  }
}
