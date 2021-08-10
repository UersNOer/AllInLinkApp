package com.unistrong.api.mapcore;

import android.graphics.Color;
import android.graphics.Rect;
import android.os.RemoteException;
import android.util.Log;

import com.unistrong.api.mapcore.util.LogManager;
import com.unistrong.api.mapcore.util.Util;
import com.unistrong.api.maps.MapUtils;
import com.unistrong.api.maps.model.BitmapDescriptor;
import com.unistrong.api.maps.model.LatLng;
import com.unistrong.api.maps.model.LatLngBounds;
import com.leador.mapcore.FPoint;
import com.leador.mapcore.IPoint;
import com.leador.mapcore.NativeLineRenderer;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

class PolylineDelegateImpDecode implements IPolylineDelegateDecode {
  private IGLOverlayLayer layer;
  private String sid;
  private List<IPoint> geoPoints = new ArrayList<IPoint>();
  // 原始点
  private List<IPoint> srcGeoPoints = new ArrayList<IPoint>();
  private List<LatLng> points = new ArrayList<LatLng>();
  private List<BitmapDescriptor> bitmapList = new ArrayList<BitmapDescriptor>();
  private List<Integer> g = new ArrayList<Integer>();
  // 所有分段的颜色
  private List<Integer> h = new ArrayList<Integer>();
  private int [] pointArray;
  private FloatBuffer i;
  private BitmapDescriptor bitmapDescriptor = null;
  private LatLngBounds bounds = null;
  private Object synObj = new Object();
  private boolean visible = true;
  private boolean n = true;
  private boolean isGeodesic = false;
  private boolean isDottedLine = false;
  private boolean isGetTexsureId = false;
  private boolean r = false;
  private boolean useTexture = true;
  private boolean t = false;
  private int drawType = 0;//绘制类型
  private int texsureId = 0;
  private int color = -16777216;
  private int geoPointsSize = 0;
  private float width = 10.0F;
  private float zIndex = 0.0F;
  private float A = 0.0F;
  private float alpha;
  private float red_color;
  private float green_color;
  private float blue_color;
  private float transparency = 0.0F;//透明度
  private float G = 0.0F;
  //  private float[] H;
  private int[] I;
  private int[] J;
  private double K = 5.0D;
  private List<Long> tileIds = new ArrayList<>();
  private int dataId;
  // 分段信息
  private List<PolylinePath> paths = new ArrayList<>();
  // 存储多个纹理id信息
  private HashMap<BitmapDescriptor,Integer> textureMap = new HashMap<>();
  
  public void setUseTexture(boolean useTexture)
  {
    if(!useTexture){
      this.drawType = 0;
    }
    this.useTexture = useTexture;
    this.layer.getOverlayMapDelegate().setRunLowFrame(false);
  }
  
  public void geodesic(boolean isGeodesic)
    throws RemoteException
  {
    this.isGeodesic = isGeodesic;
    this.layer.getOverlayMapDelegate().setRunLowFrame(false);
  }
  
  public boolean isGeodesic()
  {
    return this.isGeodesic;
  }
  
  public void setDottedLine(boolean isDottedLine)
  {
    if ((this.drawType == 2) || (this.drawType == 0))
    {
      this.isDottedLine = isDottedLine;
      if ((isDottedLine) && (this.n)) {
        this.drawType = 2;
      }
      this.layer.getOverlayMapDelegate().setRunLowFrame(false);
    }
  }
  
  public boolean isDottedLine()
  {
    return this.isDottedLine;
  }
  
  public PolylineDelegateImpDecode(IGLOverlayLayer paramv)
  {
    this.layer = paramv;
    try
    {
      this.sid = getId();
    }
    catch (RemoteException localRemoteException)
    {
      localRemoteException.printStackTrace();
    }
  }
  
  void b(List<LatLng> srcPoints)
    throws RemoteException
  {
    ArrayList<IPoint> desPoints = new ArrayList<IPoint>();
    LatLngBounds.Builder boundsBuilder = LatLngBounds.builder();
    LatLng lastPoint;
    if (srcPoints != null)
    {
      lastPoint = null;
      if (this.srcGeoPoints != null && !this.srcGeoPoints.isEmpty()){
        this.srcGeoPoints.clear();
      }
      for (LatLng currentPoint : srcPoints) {
        IPoint tempGPoint = new IPoint();
        this.layer.getOverlayMapDelegate().latlon2Geo(currentPoint.latitude, currentPoint.longitude, tempGPoint);
        this.srcGeoPoints.add(tempGPoint);

        if ((currentPoint != null) && (!currentPoint.equals(lastPoint)))
        {
          IPoint curGeoPoint;//坐标
          if (!this.isGeodesic)//非大地线
          {
            if(lastPoint ==null){
              curGeoPoint = new IPoint();
              this.layer.getOverlayMapDelegate().latlon2Geo(currentPoint.latitude, currentPoint.longitude, curGeoPoint);
              desPoints.add(curGeoPoint);
              boundsBuilder.include(currentPoint);
            }else{
              //判断是否插值
              insert1(lastPoint,currentPoint,desPoints,boundsBuilder);
            }
          }
          else if (lastPoint != null)
          {
            if (Math.abs(currentPoint.longitude - ((LatLng)lastPoint).longitude) < 0.01D)
            {
              curGeoPoint = new IPoint();
              this.layer.getOverlayMapDelegate().latlon2Geo(((LatLng)lastPoint).latitude, ((LatLng)lastPoint).longitude, curGeoPoint);
              
              desPoints.add(curGeoPoint);
              boundsBuilder.include((LatLng) lastPoint);
              IPoint localIPoint2 = new IPoint();
              this.layer.getOverlayMapDelegate().latlon2Geo(currentPoint.latitude, currentPoint.longitude, localIPoint2);
              
              desPoints.add(localIPoint2);
              boundsBuilder.include(currentPoint);
            }
            else
            {
              insertPoint((LatLng) lastPoint, currentPoint, desPoints, boundsBuilder);
            }
          }
          lastPoint = currentPoint;
        }
      }
    }
    this.geoPoints = desPoints;
    this.geoPointsSize = geoPoints.size();
    if (this.geoPoints.size() > 0) {
      this.bounds = boundsBuilder.build();
    }
    this.layer.getOverlayMapDelegate().setRunLowFrame(false);
  }

  /**
   * 非大地线是否插值计算。
   * @param firstPoint
   * @param secondPoint
   * @param desPoints
   * @param boundsBuilder
   */
  private void insert1(LatLng firstPoint, LatLng secondPoint, List<IPoint> desPoints, LatLngBounds.Builder boundsBuilder){
    IPoint firstGeoPoint = new IPoint();
    this.layer.getOverlayMapDelegate().latlon2Geo(firstPoint.latitude, firstPoint.longitude, firstGeoPoint);

    IPoint secondGeoPoint = new IPoint();
    this.layer.getOverlayMapDelegate().latlon2Geo(secondPoint.latitude, secondPoint.longitude, secondGeoPoint);

    int count = 0;
    long disSquare = 0;
    if(true) {
      disSquare = getDisSquare(firstGeoPoint.x, firstGeoPoint.y, secondGeoPoint.x, secondGeoPoint.y);
      count = (int) (disSquare / 65000000000l);
    }
//    Log.e("insert1","____count:"+count);
    if(count > 0){
      count += 1;
      int dx = ( secondGeoPoint.x - firstGeoPoint.x )/count;
      int dy = ( secondGeoPoint.y - firstGeoPoint.y )/count;
      for( int i = 1; i < count; i++ ){
        IPoint insertPoint = new IPoint(firstGeoPoint.x + (i * dx),firstGeoPoint.y + (i * dy));
        desPoints.add(insertPoint);
      }
    }
    desPoints.add(secondGeoPoint);
    boundsBuilder.include(secondPoint);
  }
  IPoint a(IPoint firstGeoPoint, IPoint secondGeoPoint, IPoint centerGeoPoint, double paramDouble, int paramInt)
  {
    IPoint localIPoint = new IPoint();
    double d1 = secondGeoPoint.x - firstGeoPoint.x;
    double d2 = secondGeoPoint.y - firstGeoPoint.y;
    double d3 = d2 * d2 / (d1 * d1) + 1.0D;
    localIPoint.y = ((int)(paramInt * paramDouble / Math.sqrt(d3) + centerGeoPoint.y));
    localIPoint.x = ((int)((centerGeoPoint.y - localIPoint.y) * d2 / d1 + centerGeoPoint.x));
    
    return localIPoint;
  }
  void insertPoint(List<IPoint> paramList1, List<IPoint> paramList2, double paramDouble,int count)
  {
    if (paramList1.size() != 3) {
      return;
    }
    float f1 = 1F;
    for (int i1 = 0; i1 <= count; i1 = (int)(i1 + f1))
    {
      float f2 = i1 / (float)count;
      IPoint localIPoint = new IPoint();

      double d1 = (1.0D - f2) * (1.0D - f2) * ((IPoint)paramList1.get(0)).x + 2.0F * f2 * (1.0D - f2) * ((IPoint)paramList1.get(1)).x * paramDouble + f2 * f2 * ((IPoint)paramList1.get(2)).x;

      double d2 = (1.0D - f2) * (1.0D - f2) * ((IPoint)paramList1.get(0)).y + 2.0F * f2 * (1.0D - f2) * ((IPoint)paramList1.get(1)).y * paramDouble + f2 * f2 * ((IPoint)paramList1.get(2)).y;

      double d3 = (1.0D - f2) * (1.0D - f2) + 2.0F * f2 * (1.0D - f2) * paramDouble + f2 * f2;
      double d4 = (1.0D - f2) * (1.0D - f2) + 2.0F * f2 * (1.0D - f2) * paramDouble + f2 * f2;

      localIPoint.x = ((int)(d1 / d3));
      localIPoint.y = ((int)(d2 / d4));

      paramList2.add(localIPoint);
    }
  }
  void insertPoint(List<IPoint> paramList1, List<IPoint> desPoints, double paramDouble)
  {
    if (paramList1.size() != 3) {
      return;
    }
    float f1 = 1F;
    for (int i1 = 0; i1 <= 10; i1 = (int)(i1 + f1))
    {
      float f2 = i1 / 10.0F;
      IPoint localIPoint = new IPoint();
      
      double d1 = (1.0D - f2) * (1.0D - f2) * ((IPoint)paramList1.get(0)).x + 2.0F * f2 * (1.0D - f2) * ((IPoint)paramList1.get(1)).x * paramDouble + f2 * f2 * ((IPoint)paramList1.get(2)).x;
      
      double d2 = (1.0D - f2) * (1.0D - f2) * ((IPoint)paramList1.get(0)).y + 2.0F * f2 * (1.0D - f2) * ((IPoint)paramList1.get(1)).y * paramDouble + f2 * f2 * ((IPoint)paramList1.get(2)).y;
      
      double d3 = (1.0D - f2) * (1.0D - f2) + 2.0F * f2 * (1.0D - f2) * paramDouble + f2 * f2;
      double d4 = (1.0D - f2) * (1.0D - f2) + 2.0F * f2 * (1.0D - f2) * paramDouble + f2 * f2;
      
      localIPoint.x = ((int)(d1 / d3));
      localIPoint.y = ((int)(d2 / d4));
      
      desPoints.add(localIPoint);
    }
  }
  
  void insertPoint(LatLng firstPoint, LatLng secondPoint, List<IPoint> desPoints, LatLngBounds.Builder boundsBuilder)
  {
    double d1 = Math.abs(firstPoint.longitude - secondPoint.longitude) * Math.PI / 180.0D;
    
    LatLng centerPoint = new LatLng((secondPoint.latitude + firstPoint.latitude) / 2.0D, (secondPoint.longitude + firstPoint.longitude) / 2.0D, false);
    
    boundsBuilder.include(firstPoint).include(centerPoint).include(secondPoint);
    
    int i1 = centerPoint.latitude > 0.0D ? -1 : 1;
    
    IPoint firstGeoPoint = new IPoint();
    this.layer.getOverlayMapDelegate().latlon2Geo(firstPoint.latitude, firstPoint.longitude, firstGeoPoint);
    
    IPoint secondGeoPoint = new IPoint();
    this.layer.getOverlayMapDelegate().latlon2Geo(secondPoint.latitude, secondPoint.longitude, secondGeoPoint);

    IPoint centerGeoPoint = new IPoint();
    this.layer.getOverlayMapDelegate().latlon2Geo(centerPoint.latitude, centerPoint.longitude, centerGeoPoint);
    
    double d2 = Math.cos(d1 * 0.5D);
    double d3 = Math.hypot(firstGeoPoint.x - secondGeoPoint.x, firstGeoPoint.y - secondGeoPoint.y) * 0.5D * Math.tan(d1 * 0.5D);
    
    IPoint center = a(firstGeoPoint, secondGeoPoint, centerGeoPoint, d3, i1);
    
    ArrayList<IPoint> points = new ArrayList<IPoint>();
    points.add(firstGeoPoint);
    points.add(center);
    points.add(secondGeoPoint);
    int count = 0;
    if(isDottedLine) {
      long disSquare = getDisSquare(firstGeoPoint.x, firstGeoPoint.y, secondGeoPoint.x, secondGeoPoint.y);
      count = (int) (disSquare / 65000000000l);
    }
    if(count>10){
      insertPoint(points, desPoints, d2,count);
    }else{
      insertPoint(points, desPoints, d2);
    }
  }

  private long getDisSquare(long x1,long y1,long x2,long y2){
    return (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
  }
  public void remove()
    throws RemoteException
  {
    this.layer.removeOverlay(getId());
    if ((this.J != null) && (this.J.length > 0))
    {
      for (int i1 = 0; i1 < this.J.length; i1++) {
        this.layer.recycleOverlayId(Integer.valueOf(this.J[i1]));
      }
      this.J = null;
    }
    if (this.texsureId > 0)
    {
      this.layer.recycleOverlayId(Integer.valueOf(this.texsureId));
      this.texsureId = 0;
    }
    this.layer.getOverlayMapDelegate().setRunLowFrame(false);
  }
  
  public String getId()
    throws RemoteException
  {
    if (this.sid == null) {
      //this.b = texsureId.a("Polyline");
      this.sid = GLOverlayLayerDecode.CreateId("Polyline");
    }
    return this.sid;
  }

  public void addPoint(LatLng point)throws RemoteException{
    if(point  == null){
      return;
    }
    int len = points.size();
    if(len>0&&point.equals(points.get(len-1))){
      return;
    }
    this.points.add(point);
    try{
      synchronized (this.synObj) {
        IPoint curGeoPoint = new IPoint();
        this.layer.getOverlayMapDelegate().latlon2Geo(point.latitude, point.longitude, curGeoPoint);
        LatLngBounds.Builder boundsBuilder = LatLngBounds.builder();
        boundsBuilder.include(bounds.northeast);
        boundsBuilder.include(bounds.southwest);
        boundsBuilder.include(point);
        geoPoints.add(curGeoPoint);
        this.geoPointsSize = this.geoPoints.size();
        if ( geoPointsSize > 0) {
          this.bounds = boundsBuilder.build();
        }
        getGeoArray();
      }
      this.layer.getOverlayMapDelegate().setRunLowFrame(false);
    }catch (Throwable throwable)
    {
      this.geoPoints.clear();
      throwable.printStackTrace();
    }
  }
  public void setPoints(List<LatLng> points)
    throws RemoteException
  {
    try
    {
      this.points = points;
      synchronized (this.synObj)
      {
        b(points);
        getGeoArray();
        calMapFPoint();
      }
      this.layer.getOverlayMapDelegate().setRunLowFrame(false);
    }
    catch (Throwable throwable)
    {
      this.geoPoints.clear();
      throwable.printStackTrace();
    }
  }
  private void getGeoArray(){
      int size = this.geoPoints.size();
      int [] pointArray = new int[size * 2];
      for(int i = 0 ; i < size ; i ++){
        pointArray[i*2] = this.geoPoints.get(i).x;
        pointArray[i*2+1] = this.geoPoints.get(i).y;
      }
      this.pointArray = pointArray;
  }
  public List<LatLng> getPoints()
    throws RemoteException
  {
    return this.points;
  }
  
  public void setWidth(float width)
    throws RemoteException
  {
    this.width = width;
    this.layer.getOverlayMapDelegate().setRunLowFrame(false);
  }
  
  public float getWidth()
    throws RemoteException
  {
    return this.width;
  }
  
  public void setColor(int color)
  {
    if ((this.drawType == 0) || (this.drawType == 2))
    {
      this.color = color;
      this.alpha = (Color.alpha(color) / 255.0F);
      this.red_color = (Color.red(color) / 255.0F);
      this.green_color = (Color.green(color) / 255.0F);
      this.blue_color = (Color.blue(color) / 255.0F);
      if (this.n) {
        this.drawType = 0;
      }
      this.layer.getOverlayMapDelegate().setRunLowFrame(false);
    }
  }
  
  public int getColor()
    throws RemoteException
  {
    return this.color;
  }
  
  public void setZIndex(float zIndex)
    throws RemoteException
  {
    this.zIndex = zIndex;
    this.layer.changeOverlayIndexs();
    this.layer.getOverlayMapDelegate().setRunLowFrame(false);
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
    this.layer.getOverlayMapDelegate().setRunLowFrame(false);
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
    if (this.bounds == null) {
      return false;
    }
    LatLngBounds bounds = this.layer.getOverlayMapDelegate().getMapBounds();
    if (bounds == null) {
      return true;
    }
    return (bounds.contains(this.bounds)) || (this.bounds.intersects(bounds));
  }
  
  public void calMapFPoint()
    throws RemoteException
  {


  }
  
  private void o()
  {
    if ((this.geoPointsSize > 5000) && (this.A <= 12.0F))
    {
      float f1 = this.width / 2.0F + this.A / 2.0F;
      f1 = f1 <= 200.0F ? f1 : 200.0F;
      this.G = this.layer.getOverlayMapDelegate().getMapProjection().getMapLenWithWin((int)f1);
    }
    else
    {
      this.G = this.layer.getOverlayMapDelegate().getMapProjection().getMapLenWithWin(10);
    }
  }
  
  private boolean a(FPoint paramFPoint1, FPoint paramFPoint2)
  {
    return (Math.abs(paramFPoint2.x - paramFPoint1.x) >= this.G) || (Math.abs(paramFPoint2.y - paramFPoint1.y) >= this.G);
  }
  
  public void a(BitmapDescriptor bitmapDescriptor)
  {
    this.n = false;
    this.drawType = 1;
    this.bitmapDescriptor = bitmapDescriptor;
    this.layer.getOverlayMapDelegate().setRunLowFrame(false);
  }
  
  public void draw(GL10 gl10)
    throws RemoteException
  {
    if ((this.geoPoints == null) || (this.geoPoints.size() == 0) || (this.width <= 0.0F)) {
      return;
    }
    if (this.geoPointsSize > 0) {
        drawLine(gl10);
    }
    this.r = true;
  }

  private void drawLine(GL10 gl10)
  {
    float f1 = this.layer.getOverlayMapDelegate().getMapProjection().getMapLenWithWin((int)this.width);
    switch (this.drawType)
    {
    case 0: //简单实现
      drawSampleLine(gl10, f1);
      break;
    case 2://绘制虚线
      drawDottedLine(gl10, f1);
      break;
    case 1: //纹理绘制
      drawTextureLine(gl10, f1);
      break;
    case 3: // 绘制彩色线
      drawColorLine(gl10, f1);
      break;
    case 4:
//      b(gl10, f1);
      break;
    case 5: // 绘制分段纹理
      drawTextureList(gl10, f1);
      break;
    }
  }

  private void drawTextureList(GL10 gl10, float lineWidth) {
    try {
      long stateInstance = this.layer.getOverlayMapDelegate().getMapProjection().getInstanceHandle();
      //TODO 需要在创建纹理时初始化本信息
      int texOrgPixelLen = 64;
      float texLen = this.layer.getOverlayMapDelegate().getMapProjection().getMapLenWithWin(texOrgPixelLen);
      int finalcolor = ((int)(this.alpha *255)) << 24 | ((int)(this.red_color * 255)) << 16
              | ((int)(this.green_color * 255)) << 8 | ((int)(this.blue_color * 255)) ; //不需要使用颜色
      boolean usecolor = false; //不使用颜色
      boolean complexTex = false;
      for (PolylinePath path : this.paths){
        BitmapDescriptor bitmapDescriptor = path.bitmapDescriptor;
        int[] pointsArray = path.pathPoints;

        int texsureId = -1;
        if (!this.textureMap.containsKey(bitmapDescriptor)) {
          texsureId = this.layer.getOverlayMapDelegate().getTexsureId();
          if (texsureId == 0) {
            int [] localObject1 = { 0 };
            gl10.glGenTextures(1, localObject1, 0);
            texsureId = localObject1[0];
          }
          if (bitmapDescriptor != null) {
            Util.bindTexture(gl10, texsureId, bitmapDescriptor.getBitmap(), true);
          }
          this.textureMap.put(bitmapDescriptor, texsureId);
          int index = this.bitmapList.indexOf(bitmapDescriptor);
          if (index >= 0) {
            this.J[index] = texsureId;
          }
        } else {
          texsureId = this.textureMap.get(bitmapDescriptor);
        }

        int texid = texsureId; //获取纹理ID

        NativeLineRenderer.nativeDrawLineByTextureID(pointsArray, pointsArray.length, stateInstance,
                texLen, lineWidth, texid, finalcolor, complexTex, usecolor);
      }
    } catch (Throwable localThrowable) {
      LogManager.writeLog(LogManager.productInfo, hashCode() + " drawSingleColorLine exception: "
              + localThrowable.getMessage(), 115);

    }
  }

  private void drawColorLine(GL10 gl10, float lineWidth) {
    try {
      long stateInstance = this.layer.getOverlayMapDelegate().getMapProjection().getInstanceHandle();
      //TODO 需要在创建纹理时初始化本信息
      int texOrgPixelLen = 32;
      float texLen = this.layer.getOverlayMapDelegate().getMapProjection().getMapLenWithWin(texOrgPixelLen); //系统默认的纹理像素宽高都是32像素
      int texid = this.layer.getOverlayMapDelegate().getLineTextureID(); //系统默认的实线纹理ID
      boolean usecolor = true;  //使用颜色替换纹理中的颜色
      boolean complexTex = false; //简单纹理
      for (PolylinePath path : paths){
        int color = path.color;
        int[] pointsArray = path.pathPoints;

        //如下是绘制带有颜色的实线
        float[] colors = getColor(color);
        int finalcolor = ((int)(colors[0] *255)) << 24 | ((int)(colors[1] * 255)) << 16
                | ((int)(colors[2] * 255)) << 8 | ((int)(colors[3] * 255));
        NativeLineRenderer.nativeDrawLineByTextureID(pointsArray, pointsArray.length, stateInstance, texLen, lineWidth, texid, finalcolor, complexTex, usecolor);
      }
    } catch (Throwable localThrowable) {
      LogManager.writeLog(LogManager.productInfo, hashCode() + " drawSingleColorLine exception: " + localThrowable.getMessage(), 115);
    }
  }

  class PolylinePath {
    public int color;
    public BitmapDescriptor bitmapDescriptor;
    public int[] pathPoints;
  }

  private int[] getGeoArray(List<IPoint> geoPoints_){
    int [] pointArray = null;
    if (geoPoints_ != null && !geoPoints_.isEmpty()){
      int size = geoPoints_.size();
      pointArray = new int[size * 2];
      for(int i = 0 ; i < size ; i ++){
        pointArray[i*2] = geoPoints_.get(i).x;
        pointArray[i*2+1] = geoPoints_.get(i).y;
      }
    }
    return pointArray;
  }

  private float[] getColor(int color) {
    float[] colors = new float[4];
    float alpha = (Color.alpha(color) / 255.0F);
    float red_color = (Color.red(color) / 255.0F);
    float green_color = (Color.green(color) / 255.0F);
    float blue_color = (Color.blue(color) / 255.0F);
    colors[0] = alpha;
    colors[1] = red_color;
    colors[2] = green_color;
    colors[3] = blue_color;
    return colors;
  }

  private void drawTextureLine(GL10 gl10, float lineWidth) {
    //原有实现
    if (!this.isGetTexsureId) {
      this.texsureId = this.layer.getOverlayMapDelegate().getTexsureId();
      if (this.texsureId == 0) {
        int [] localObject1 = { 0 };
        gl10.glGenTextures(1, localObject1, 0);
        this.texsureId = localObject1[0];
      }
      if (this.bitmapDescriptor != null) {
        Util.bindTexture(gl10, this.texsureId, this.bitmapDescriptor.getBitmap(), true);
      }
      this.isGetTexsureId = true;
    }

    int texid = this.texsureId; //获取纹理ID

    long stateInstance = this.layer.getOverlayMapDelegate().getMapProjection().getInstanceHandle();
    //TODO 需要在创建纹理时初始化本信息
    int texOrgPixelLen = 64;
    float texLen = this.layer.getOverlayMapDelegate().getMapProjection().getMapLenWithWin(texOrgPixelLen);
    LatLng latLng = points.get(0);
    int finalcolor = ((int)(this.alpha *255)) << 24 | ((int)(this.red_color * 255)) << 16 | ((int)(this.green_color * 255)) << 8 | ((int)(this.blue_color * 255)) ; //不需要使用颜色
    boolean usecolor = false; //不使用颜色
    boolean complexTex = false;
    NativeLineRenderer.nativeDrawLineByTextureID(pointArray, 10, stateInstance, texLen, lineWidth, texid, finalcolor, complexTex, usecolor);
  }
  
  private void drawDottedLine(GL10 gl10, float lineWidth)
  {
    long stateInstance = this.layer.getOverlayMapDelegate().getMapProjection().getInstanceHandle();
    //TODO 需要在创建纹理时初始化本信息
    int texOrgPixelLen = 32;
    float texLen = this.layer.getOverlayMapDelegate().getMapProjection().getMapLenWithWin(texOrgPixelLen); //系统默认的纹理像素宽高都是32像素
    int texid = this.layer.getOverlayMapDelegate().getDottedLineTextureID(); //获得系统默认的虚线纹理


    //虚线的颜色
    int finalcolor = ((int)(this.alpha *255)) << 24 | ((int)(this.red_color * 255)) << 16 | ((int)(this.green_color * 255)) << 8 | ((int)(this.blue_color * 255)) ;
    boolean usecolor = true; //使用颜色替换纹理中的颜色
    boolean complexTex = false; //简单纹理
    NativeLineRenderer.nativeDrawLineByTextureID(pointArray, pointArray.length, stateInstance, texLen, lineWidth, texid, finalcolor, complexTex, usecolor);
  }
  
  private void drawSampleLine(GL10 gl10, float lineWidth)
  {
    try
    {

      //如下是绘制带有颜色的实线
      long stateInstance = this.layer.getOverlayMapDelegate().getMapProjection().getInstanceHandle();
      //TODO 需要在创建纹理时初始化本信息
      int texOrgPixelLen = 32;
      float texLen = this.layer.getOverlayMapDelegate().getMapProjection().getMapLenWithWin(texOrgPixelLen); //系统默认的纹理像素宽高都是32像素
      int texid = this.layer.getOverlayMapDelegate().getLineTextureID(); //系统默认的实线纹理ID
      int finalcolor = ((int)(this.alpha *255)) << 24 | ((int)(this.red_color * 255)) << 16 | ((int)(this.green_color * 255)) << 8 | ((int)(this.blue_color * 255)) ;
      boolean usecolor = true;  //使用颜色替换纹理中的颜色
      boolean complexTex = false; //简单纹理
      NativeLineRenderer.nativeDrawLineByTextureID(pointArray, pointArray.length, stateInstance, texLen, lineWidth, texid, finalcolor, complexTex, usecolor);
    }
    catch (Throwable localThrowable)
    {
      LogManager.writeLog(LogManager.productInfo, hashCode() + " drawSingleColorLine exception: " + localThrowable.getMessage(), 115);
    }
  }
  
  private boolean p()
  {
    boolean boolvalue = false;
    try
    {
      this.A = this.layer.getOverlayMapDelegate().getCameraPosition().zoom;
      o();
      if ((this.A <= 10.0F) || (this.drawType > 2)) {
        return false;
      }
    }
    catch (RemoteException localRemoteException)
    {
      localRemoteException.printStackTrace();
    }
    try
    {
      if (this.layer.getOverlayMapDelegate() != null)
      {
        Rect localRect = new Rect(-100, -100, this.layer.getOverlayMapDelegate().getMapWidth() + 100, this.layer.getOverlayMapDelegate().getMapHeight() + 100);
        LatLng localLatLng1 = this.bounds.northeast;
        LatLng localLatLng2 = this.bounds.southwest;
        IPoint localIPoint1 = new IPoint();
        this.layer.getOverlayMapDelegate().getLatLng2Pixel(localLatLng1.latitude, localLatLng2.longitude, localIPoint1);
        
        IPoint localIPoint2 = new IPoint();
        this.layer.getOverlayMapDelegate().getLatLng2Pixel(localLatLng1.latitude, localLatLng1.longitude, localIPoint2);
        
        IPoint localIPoint3 = new IPoint();
        this.layer.getOverlayMapDelegate().getLatLng2Pixel(localLatLng2.latitude, localLatLng1.longitude, localIPoint3);
        
        IPoint localIPoint4 = new IPoint();
        this.layer.getOverlayMapDelegate().getLatLng2Pixel(localLatLng2.latitude, localLatLng2.longitude, localIPoint4);
        if ((localRect.contains(localIPoint1.x, localIPoint1.y)) && (localRect.contains(localIPoint2.x, localIPoint2.y)) && 
          (localRect.contains(localIPoint3.x, localIPoint3.y)) && 
          (localRect.contains(localIPoint4.x, localIPoint4.y))) {
        	boolvalue = false;
        }
      }
      return true; //???????
    }
    catch (Throwable localThrowable) {}
    return false; //???????
  }
  
  public void destroy()
  {
    try
    {
      remove();
//      if (this.H != null) {
//        this.H = null;
//      }
      if (this.i != null)
      {
        this.i.clear();
        this.i = null;
      }
      if ((this.bitmapList != null) && (this.bitmapList.size() > 0)) {
        for (BitmapDescriptor localBitmapDescriptor : this.bitmapList) {
          localBitmapDescriptor.recycle();
        }
      }
      if (this.bitmapDescriptor != null) {
        this.bitmapDescriptor.recycle();
      }
      if (this.h != null)
      {
        this.h.clear();
        this.h = null;
      }
      if (this.g != null)
      {
        this.g.clear();
        this.g = null;
      }
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
      Log.d("destroy erro", "PolylineDelegateImp destroy");
    }
  }
  
  public boolean checkInBounds()
  {
    return this.r;
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

  public LatLng getNearestLatLng(LatLng paramLatLng)
  {
    if (paramLatLng == null) {
      return null;
    }
    if ((this.points == null) || (this.points.size() == 0)) {
      return null;
    }
    try
    {
      int i1 = 0;
      float f1 = 0.0F;float f2 = 0.0F;
      for (int i2 = 0; i2 < this.points.size(); i2++) {
        if (i2 == 0)
        {
          f1 = MapUtils.calculateLineDistance(paramLatLng,
                  (LatLng) this.points.get(i2));
        }
        else
        {
          f2 = MapUtils.calculateLineDistance(paramLatLng,
                  (LatLng) this.points.get(i2));
          if (f1 > f2)
          {
            f1 = f2;
            i1 = i2;
          }
        }
      }
      return (LatLng)this.points.get(i1);
    }
    catch (Throwable localThrowable)
    {

      localThrowable.printStackTrace();
    }
    return null;
  }
  
  public boolean b(LatLng paramLatLng)
  {
//    float[] arrayOfFloat = new float[this.H.length];
//    System.arraycopy(this.H, 0, arrayOfFloat, 0, this.H.length);
//    int i1 = arrayOfFloat.length / 3;
    int i1 = 0;
    if (i1 < 2) {
      return false;
    }
    ArrayList<FPoint> localArrayList = null;
    try
    {
      localArrayList = q();
      if ((localArrayList == null) || (localArrayList.size() < 1)) {
        return false;
      }
    }
    catch (Exception localException)
    {
      return false;
    }
    double d1 = this.layer.getOverlayMapDelegate().getMapProjection().getMapLenWithWin((int)this.width / 4);
    
    double d2 = this.layer.getOverlayMapDelegate().getMapProjection().getMapLenWithWin((int)this.K);
    
    FPoint localFPoint1 = c(paramLatLng);
    
    Object localObject = null;
    FPoint localFPoint2 = null;
    for (int i2 = 0; i2 < localArrayList.size() - 1; i2++)
    {
      if (i2 == 0) {
        localObject = (FPoint)localArrayList.get(i2);
      } else {
        localObject = localFPoint2;
      }
      localFPoint2 = (FPoint)localArrayList.get(i2 + 1);
      
      double d3 = a(localFPoint1, (FPoint)localObject, localFPoint2);
      if (d2 + d1 - d3 >= 0.0D)
      {
        localArrayList.clear();
        return true;
      }
    }
    localArrayList.clear();
    
    return false;
  }
  
  private ArrayList<FPoint> q()
  {
    ArrayList<FPoint> localArrayList = new ArrayList<FPoint>();
//    for (int i1 = 0; i1 < this.H.length; i1++)
//    {
//      float f1 = this.H[i1];
//      i1++;
//      float f2 = this.H[i1];
//      i1++;
//      FPoint localFPoint = new FPoint(f1, f2);
//
//      localArrayList.add(localFPoint);
//    }
    return localArrayList;
  }
  
  private double a(FPoint paramFPoint1, FPoint paramFPoint2, FPoint paramFPoint3)
  {
    return a(paramFPoint1.x, paramFPoint1.y, paramFPoint2.x, paramFPoint2.y, paramFPoint3.x, paramFPoint3.y);
  }
  
  private FPoint c(LatLng paramLatLng)
  {
    IPoint localIPoint = new IPoint();
    this.layer.getOverlayMapDelegate().latlon2Geo(paramLatLng.latitude, paramLatLng.longitude, localIPoint);
    FPoint localFPoint = new FPoint();
    this.layer.getOverlayMapDelegate().geo2Map(localIPoint.y, localIPoint.x, localFPoint);
    
    return localFPoint;
  }
  
  private double a(double paramDouble1, double paramDouble2, double paramDouble3, double paramDouble4, double paramDouble5, double paramDouble6)
  {
    double d1 = (paramDouble5 - paramDouble3) * (paramDouble1 - paramDouble3) + (paramDouble6 - paramDouble4) * (paramDouble2 - paramDouble4);
    if (d1 <= 0.0D) {
      return Math.sqrt((paramDouble1 - paramDouble3) * (paramDouble1 - paramDouble3) + (paramDouble2 - paramDouble4) * (paramDouble2 - paramDouble4));
    }
    double d2 = (paramDouble5 - paramDouble3) * (paramDouble5 - paramDouble3) + (paramDouble6 - paramDouble4) * (paramDouble6 - paramDouble4);
    if (d1 >= d2) {
      return Math.sqrt((paramDouble1 - paramDouble5) * (paramDouble1 - paramDouble5) + (paramDouble2 - paramDouble6) * (paramDouble2 - paramDouble6));
    }
    double d3 = d1 / d2;
    double d4 = paramDouble3 + (paramDouble5 - paramDouble3) * d3;
    double d5 = paramDouble4 + (paramDouble6 - paramDouble4) * d3;
    return Math.sqrt((paramDouble1 - d4) * (paramDouble1 - d4) + (d5 - paramDouble2) * (d5 - paramDouble2));
  }
  
  public void setTransparency(float transparency)
  {
    this.transparency = transparency;
    this.layer.getOverlayMapDelegate().setRunLowFrame(false);
  }
  
  public void setTextureList(List<BitmapDescriptor> bitmapList) {
    if ((bitmapList == null) || (bitmapList.size() == 0)) {
      return;
    }
    if (bitmapList.size() > 1)
    {
      this.n = false;
      this.drawType = 5;
      this.bitmapList = bitmapList;
      this.J = new int[bitmapList.size()];
      if (!this.textureMap.isEmpty()){
        this.textureMap.clear();
      }
//      this.layer.getOverlayMapDelegate().setRunLowFrame(false);
    }
    else
    {
      a((BitmapDescriptor)bitmapList.get(0));
    }
  }

  public void setTextureIndex(final List<Integer> paramList) {
    if ((paramList == null) || (paramList.size() == 0)) {
      return;
    }

    // 相邻的index排重
//    this.g = g(paramList);
    PolylineDelegateImpDecode.this.g = paramList;

    try {
      // 相同纹理汇聚
      if (PolylineDelegateImpDecode.this.paths == null){
        PolylineDelegateImpDecode.this.paths = new ArrayList<>();
      } else {
        PolylineDelegateImpDecode.this.paths.clear();
      }

      List<IPoint> points = new ArrayList<>();
      points.add(PolylineDelegateImpDecode.this.srcGeoPoints.get(0));
      List<IPoint> betweenList = getSubList(PolylineDelegateImpDecode.this.srcGeoPoints.get(0),
              PolylineDelegateImpDecode.this.srcGeoPoints.get(1));
      if (!isListNull(betweenList)){
        points.addAll(betweenList);
      }
      points.add(PolylineDelegateImpDecode.this.srcGeoPoints.get(1));
      final int colorSize = PolylineDelegateImpDecode.this.g.size();
      final int pointSize = PolylineDelegateImpDecode.this.srcGeoPoints.size();
      PolylinePath polylinePath;
      // 如果只有两个点
      if (pointSize == 2){
        int[] pathArray = getGeoArray(points);
        polylinePath = new PolylinePath();
        polylinePath.bitmapDescriptor = (PolylineDelegateImpDecode.this.bitmapList
                .get(PolylineDelegateImpDecode.this.g.get(0)));
        polylinePath.pathPoints = pathArray;
        PolylineDelegateImpDecode.this.paths.add(polylinePath);
        points.clear();
      } else {
        // n段颜色为n-1
        for (int i = 0; i < (colorSize-1);i++){
          boolean isSame = false;
          if (PolylineDelegateImpDecode.this.g.get(i) == PolylineDelegateImpDecode.this.g.get(i+1)){ // 颜色相同就聚合
            isSame = true;
          } else { // 颜色不同就终止当前聚合
            isSame = false;
            int[] pathArray = getGeoArray(points);
            polylinePath = new PolylinePath();
            polylinePath.bitmapDescriptor = PolylineDelegateImpDecode.this.bitmapList
                    .get(PolylineDelegateImpDecode.this.g.get(i));
            polylinePath.pathPoints = pathArray;
            PolylineDelegateImpDecode.this.paths.add(polylinePath);
            points.clear();
            points.add(PolylineDelegateImpDecode.this.srcGeoPoints.get(i+1));
          }
          // add下一段的终点
          betweenList = getSubList(PolylineDelegateImpDecode.this.srcGeoPoints.get(i+1),
                  PolylineDelegateImpDecode.this.srcGeoPoints.get(i+2));
          if (!isListNull(betweenList)){
            points.addAll(betweenList);
          }

          points.add(PolylineDelegateImpDecode.this.srcGeoPoints.get(i+2));

          if ((i+1) == (colorSize-1) || ((colorSize >= (pointSize-1)) && (i == pointSize-3))){
            int[] pathArray = getGeoArray(points);
            polylinePath = new PolylinePath();
            polylinePath.bitmapDescriptor = (isSame ? PolylineDelegateImpDecode.this.bitmapList
                    .get(PolylineDelegateImpDecode.this.g.get(i))
                    : PolylineDelegateImpDecode.this.bitmapList.get(PolylineDelegateImpDecode.this.g.get(i+1)));
            polylinePath.pathPoints = pathArray;
            PolylineDelegateImpDecode.this.paths.add(polylinePath);
            points.clear();
            break;
          }
        }

        // 如果colorSize != (pointSize-1)
        if (colorSize < (pointSize-1)){
          points.add(PolylineDelegateImpDecode.this.srcGeoPoints.get(colorSize));
          betweenList = getSubList(PolylineDelegateImpDecode.this.srcGeoPoints.get(colorSize),
                  PolylineDelegateImpDecode.this.srcGeoPoints.get(pointSize-1));
          if (!isListNull(betweenList)){
            points.addAll(betweenList);
          }

          points.add(PolylineDelegateImpDecode.this.srcGeoPoints.get(pointSize-1));
          int[] pathArray = getGeoArray(points);
          polylinePath = new PolylinePath();
          polylinePath.bitmapDescriptor = PolylineDelegateImpDecode.this.bitmapList
                  .get(PolylineDelegateImpDecode.this.g.get(colorSize-1));
          polylinePath.pathPoints = pathArray;
          PolylineDelegateImpDecode.this.paths.add(polylinePath);
          points.clear();
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
//          Log.e("@@@", "setTextureIndex Exception \r\n"+e.getMessage());
    } finally {
//          int pathSize = PolylineDelegateImpDecode.this.paths.size();
//          PolylinePath polylinePath = PolylineDelegateImpDecode.this.paths.get(pathSize-1);
//          int len = polylinePath.pathPoints.length;
//          int x = polylinePath.pathPoints[len-2];
//          int y = polylinePath.pathPoints[len-1];
//          DPoint dPoint = new DPoint();
//          PolylineDelegateImpDecode.this.layer.getOverlayMapDelegate().geo2Latlng(x, y, dPoint);
//          Log.e("@@@", "setTextureIndex paths size = "+pathSize+", last point "+dPoint.x+","+dPoint.y);
    }

    PolylineDelegateImpDecode.this.layer.getOverlayMapDelegate().setRunLowFrame(false);
  }

    //多个颜色
    public void e(List<Integer> colors) {
        if ((colors == null) || (colors.size() == 0)) {
            return;
        }
        if (colors.size() > 1) {

            this.n = false;
            // 相邻颜色排重
//            this.h = g(colors);
            this.h = colors;
            this.drawType = 3;

          try {
            // 相同颜色汇聚
            if (PolylineDelegateImpDecode.this.paths == null){
              PolylineDelegateImpDecode.this.paths = new ArrayList<>();
            } else {
              PolylineDelegateImpDecode.this.paths.clear();
            }
            List<IPoint> points = new ArrayList<>();
            points.add(PolylineDelegateImpDecode.this.srcGeoPoints.get(0));
            List<IPoint> betweenList = getSubList(PolylineDelegateImpDecode.this.srcGeoPoints.get(0),
                    PolylineDelegateImpDecode.this.srcGeoPoints.get(1));
            if (!isListNull(betweenList)){
              points.addAll(betweenList);
            }
            points.add(PolylineDelegateImpDecode.this.srcGeoPoints.get(1));
            final int colorSize = PolylineDelegateImpDecode.this.h.size();
            final int pointSize = PolylineDelegateImpDecode.this.srcGeoPoints.size();
            PolylinePath polylinePath;
            if (pointSize == 2){
              int[] pathArray = getGeoArray(points);
              polylinePath = new PolylinePath();
              polylinePath.color = PolylineDelegateImpDecode.this.h.get(0);
              polylinePath.pathPoints = pathArray;
              PolylineDelegateImpDecode.this.paths.add(polylinePath);
              points.clear();
            } else {
              // n段颜色为n-1
              for (int i = 0; i < (colorSize-1);i++){
                boolean isSame = false;
                if (PolylineDelegateImpDecode.this.h.get(i) ==
                        PolylineDelegateImpDecode.this.h.get(i+1)){ // 颜色相同就聚合
                  isSame = true;
                } else { // 颜色不同就终止当前聚合
                  isSame = false;
                  int[] pathArray = getGeoArray(points);
                  polylinePath = new PolylinePath();
                  polylinePath.color = PolylineDelegateImpDecode.this.h.get(i);
                  polylinePath.pathPoints = pathArray;
                  PolylineDelegateImpDecode.this.paths.add(polylinePath);
                  points.clear();
                  points.add(PolylineDelegateImpDecode.this.srcGeoPoints.get(i+1));
                }
                // add下一段的终点
                betweenList = getSubList(PolylineDelegateImpDecode.this.srcGeoPoints.get(i+1),
                        PolylineDelegateImpDecode.this.srcGeoPoints.get(i+2));
                if (!isListNull(betweenList)){
                  points.addAll(betweenList);
                }
                points.add(PolylineDelegateImpDecode.this.srcGeoPoints.get(i+2));

                if ((i+1) == (colorSize-1) || ((colorSize >= (pointSize-1)) && (i == pointSize-3))){
                  int[] pathArray = getGeoArray(points);
                  polylinePath = new PolylinePath();
                  polylinePath.color = (isSame ? PolylineDelegateImpDecode.this.h.get(i) :
                          PolylineDelegateImpDecode.this.h.get(i+1));
                  polylinePath.pathPoints = pathArray;
                  PolylineDelegateImpDecode.this.paths.add(polylinePath);
                  points.clear();
                  break;
                }
              }

              // 如果colorSize != (pointSize-1)
              if (colorSize < (pointSize-1)){
                points.add(PolylineDelegateImpDecode.this.srcGeoPoints.get(colorSize));
                betweenList = getSubList(PolylineDelegateImpDecode.this.srcGeoPoints.get(colorSize),
                        PolylineDelegateImpDecode.this.srcGeoPoints.get(pointSize-1));
                if (!isListNull(betweenList)){
                  points.addAll(betweenList);
                }

                points.add(PolylineDelegateImpDecode.this.srcGeoPoints.get(pointSize-1));
                int[] pathArray = getGeoArray(points);
                polylinePath = new PolylinePath();
                polylinePath.color = PolylineDelegateImpDecode.this.h.get(colorSize-1);
                polylinePath.pathPoints = pathArray;
                PolylineDelegateImpDecode.this.paths.add(polylinePath);
                points.clear();
              }
            }
          } catch (Exception e) {
            e.printStackTrace();
          }

          PolylineDelegateImpDecode.this.layer.getOverlayMapDelegate().setRunLowFrame(false);

        }
        else
        {
            setColor(((Integer)colors.get(0)).intValue());
        }
    }

  private List<IPoint> getSubList(IPoint startP, IPoint endP){
    List<IPoint> subList = new ArrayList<>();
    int indexStart = this.geoPoints.indexOf(startP);
    int endIndex = this.geoPoints.indexOf(endP);
    if (indexStart != endIndex){
      //
//      if (indexStart > 0)--indexStart;
      if (indexStart != endIndex && indexStart < (this.geoPoints.size()-1)){
        ++indexStart;
        if (indexStart <= endIndex){
          subList = this.geoPoints.subList(indexStart, endIndex);
        }
      }
    }
    return subList;
  }

  private boolean isListNull(List list){
    boolean isNull = false;
    if (list == null || list.isEmpty())isNull = true;
    return isNull;
  }

  //是否渐变
  public void e(boolean paramBoolean)
  {
    if ((paramBoolean) && (this.h != null) && (this.h.size() > 1))
    {
      this.t = paramBoolean;
      this.drawType = 4;
      this.layer.getOverlayMapDelegate().setRunLowFrame(false);
    }
  }
  
  private List<Integer> g(List<Integer> colors)
  {
    int[] arrayOfInt = new int[colors.size()];
    ArrayList<Integer> localArrayList = new ArrayList<Integer>();
    int i1 = 0;int i2 = 0;int i3 = 0;
    for (int i4 = 0; i4 < colors.size(); i4++)
    {
      i2 = ((Integer)colors.get(i4)).intValue();
      if (i4 == 0)
      {
        localArrayList.add(Integer.valueOf(i2));
      }
      else
      {
        if (i2 == i1) {
          continue;
        }
        localArrayList.add(Integer.valueOf(i2));
      }
      arrayOfInt[i3] = i4;
      i1 = i2;
      i3++;
    }
    this.I = new int[localArrayList.size()];
    System.arraycopy(arrayOfInt, 0, this.I, 0, this.I.length);
    return localArrayList;
  }
}
