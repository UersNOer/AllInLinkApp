package com.leador.mapcore;

import android.content.Context;

import com.unistrong.api.mapcore.util.LogManager;
import com.unistrong.api.maps.MapsInitializer;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

public class MapCore
{
  public static final int TEXTURE_ICON = 0;
  public static final int TEXTURE_BACKGROUND = 1;
  public static final int TEXTURE_ROADARROW = 2;
  public static final int TEXTURE_ROADROUND = 3;
  public static final int TEXTURE_TMC_RED = 4;
  public static final int TEXTURE_TMC_YELLOW = 5;
  public static final int TEXTURE_TMC_GREEN = 6;
  public static final int TEXTURE_TMC_BLACK = 7;
  public static final int TEXTURE_RAILWAY = 8;
  public static final int ME_DATA_ROADMAP = 0;
  public static final int ME_DATA_GEO_BUILDING = 1;
  public static final int ME_DATA_BMP_BASEMAP = 2;
  public static final int ME_DATA_SATELLITE = 3;
  public static final int ME_DATA_VEC_TMC = 4;
  public static final int ME_DATA_SCREEN = 5;
  public static final int ME_DATA_MODEL = 6;
  public static final int ME_DATA_STANDARD = 7;
  public static final int ME_DATA_POI = 8;
  public static final int ME_DATA_VERSION = 9;
  public static final int ME_DATA_INDOOR = 10;
  public static final int ME_DATA_PANORAMA = 11;
  public static final int ME_DATA_GUIDE = 11;
  public static final int ME_DATA_TILE_SOURCE = 13;       //栅格数据定义的数据源（3857，900913）
  public static final int ME_DATA_SCENIC_WIDGET = 101;
  public static final int MAPRENDER_ENTER = 0;
  public static final int MAPRENDER_BASEMAPBEGIN = 1;
  public static final int MAPRENDER_BUILDINGBEGIN = 2;
  public static final int MAPRENDER_LABELSBEGIN = 3;
  public static final int MAPRENDER_RENDEROVER = 4;
  public static final int MAPRENDER_NOMORENEEDRENDER = 5;
  Context mContext;
  private IMapCallback mMapcallback = null;
  private TextTextureGenerator textTextureGenerator = null;
  long native_instance = 0L;
  GL10 mGL = null;
  byte[] tmp_3072bytes_data;
  private static String deviceID = "";
  private int coodSystem = MapsInitializer.COOD_SYSTEM_900913;
  static
  {
    try
    {
      System.loadLibrary("ldmapengv20");
    }
    catch (Exception localException) {}

  }

  public MapCore(Context paramContext)
  {
    this.mContext = paramContext;
    this.coodSystem = MapsInitializer.getCoodSystem();
    this.deviceID = DeviceIdManager.getDeviceID(mContext);
    MapTilsCacheAndResManager.getInstance(paramContext).checkDir();
    this.textTextureGenerator = new TextTextureGenerator();
    this.tmp_3072bytes_data = ByteBuffer.allocate(3072).array();
  }

//  public void newMap()
//  {
//    MapTilsCacheAndResManager.getInstance(this.mContext).checkDir();
//
//    String baseMapDataPath = MapTilsCacheAndResManager.getInstance(this.mContext).getBaseMapPath();
//    String fontVersion = this.textTextureGenerator.getFontVersion();
//    String rootPath = FileUtil.getMapBaseStorage(this.mContext);
//    byte [] sndata = DeviceIdManager.getProductSN(rootPath);
//    if(sndata == null){
//      DeviceIdManager.saveDeviceID(rootPath,deviceID);
//    }
//    Log.e("MapCore","___MapCore__newMap___");
//    this.native_instance = nativeNewInstance(baseMapDataPath, fontVersion, coodSystem, sndata);
//    LogManager.writeLog(LogManager.productInfo, hashCode() + " new Map instance: " + this.native_instance, 111);
//
//    //TODO 新增图层管理，可以按照需求进行优化
//    int retvalue = nativeAddLayer(this.native_instance, ME_DATA_SATELLITE);
//    retvalue = nativeAddLayer(this.native_instance, ME_DATA_VEC_TMC);
//    retvalue = nativeAddLayer(this.native_instance, ME_DATA_PANORAMA);
//    retvalue = nativeAddLayer(this.native_instance, ME_DATA_TILE_SOURCE);
//
//  }


  public void newMap()
  {
    MapTilsCacheAndResManager.getInstance(this.mContext).checkDir();

    String baseMapDataPath = MapTilsCacheAndResManager.getInstance(this.mContext).getBaseMapPath();
    String fontVersion = this.textTextureGenerator.getFontVersion();
    String rootPath = FileUtil.getMapBaseStorage(this.mContext);
    byte [] sndata = DeviceIdManager.getProductSN(rootPath);
    if(sndata == null){
      DeviceIdManager.saveDeviceID(rootPath,deviceID);
    }
    this.native_instance = nativeNewInstance(baseMapDataPath, fontVersion,coodSystem,sndata);
    LogManager.writeLog(LogManager.productInfo, hashCode() + " new Map instance: " + this.native_instance, 111);
  }

  public Context getContext()
  {
    return this.mContext;
  }

  public void setGL(GL10 paramGL10)
  {
    this.mGL = paramGL10;
  }

  public void setMapCallback(IMapCallback paramIMapCallback)
  {
    this.mMapcallback = paramIMapCallback;
  }

  public boolean isMapEngineValid()
  {
    return this.native_instance != 0L;
  }

  public void surfaceCreate(GL10 gl10)
  {
    if (this.native_instance != 0L) {
      nativeSurfaceCreate(this.native_instance, this);
    }
  }

  public void surfaceChange(GL10 gl10, int width, int height)
  {
    if (this.native_instance != 0L) {
      nativeSurfaceChange(this.native_instance, this, width, height);
    }
  }

  public void drawFrame(GL10 gl10)
  {
    if (this.native_instance != 0L) {
      nativeSurfaceRender(this.native_instance);
    }
  }

  public void setStyleData(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    if ((this.native_instance != 0L) &&
            (paramArrayOfByte != null) && (paramArrayOfByte.length > 0)) {
      nativeSetStyleData(this.native_instance, paramArrayOfByte, paramInt1, paramInt2);
    }
  }

  public void setTexture(byte[] paramArrayOfByte, int paramInt)
  {
    if (this.native_instance != 0L) {
      nativeSetTextureData(this.native_instance, paramArrayOfByte, paramInt);
    }
  }

  public long getInstanceHandle()
  {
    return this.native_instance;
  }

  public MapProjection getMapstate()
  {
    if (this.native_instance != 0L)
    {
      long l = nativeGetMapState(this.native_instance);
      return new MapProjection(l,this.coodSystem);
    }
    return null;
  }

  public void setMapstate(MapProjection paramMapProjection)
  {
    if (this.native_instance != 0L) {
      nativeSetMapState(this.native_instance, paramMapProjection.getInstanceHandle());
    }
  }

  public void setParameter(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    if (this.native_instance != 0L) {
      nativeSetParameter(this.native_instance, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5);
    }
  }


  public boolean putMapData(byte[] bufferdata, int off, int len, int datatype, int ptime)
  {
    boolean bool = false;
    if (this.native_instance != 0L)
    {
      LogManager.writeLog(LogManager.productInfo, hashCode() + " putMapData start", 111);
      if ((bufferdata.length == len) && (off == 0))
      {
        bool = nativePutMapData(this.native_instance, datatype, bufferdata) > 0;
      }
      else
      {
        byte[] arrayOfByte = new byte[len];
        System.arraycopy(bufferdata, off, arrayOfByte, 0, len);
        bool = nativePutMapData(this.native_instance, datatype, arrayOfByte) > 0;
      }
      LogManager.writeLog(LogManager.productInfo, hashCode() + " putMapData finish state: " + bool + " ", 111);
      if (this.mMapcallback != null) {
        this.mMapcallback.requestRender();
      }
    }
    return bool;
  }

  public MapPoi GetSelectedMapPoi(int winx, int winy, int radius)
  {
    if (this.native_instance != 0L)
    {
      MapPoi poi = new MapPoi();
      int selcount = nativeGetSelectedMapPoi(this.native_instance, winx, winy, radius, poi);
      if (selcount == 0) {
        return null;
      }

      return poi;
    }

    return null;
  }

  public void putCharbitmap(int paramInt, byte[] paramArrayOfByte)
  {
    if (this.native_instance != 0L) {
      nativePutCharBitmap(this.native_instance, paramInt, paramArrayOfByte);
    }
  }

  public boolean canStopRenderMap()
  {
    if (this.native_instance != 0L) {
      return nativeCanStopRenderMap(this.native_instance);
    }
    return false;
  }

  private void OnMapSurfaceCreate()
  {
    if (this.mMapcallback != null) {
      this.mMapcallback.OnMapSurfaceCreate(this.mGL, this);
    }
  }

  private void OnMapSufaceChanged(int paramInt1, int paramInt2)
  {
    if (this.mMapcallback != null) {
      this.mMapcallback.OnMapSufaceChanged(this.mGL, this, paramInt1, paramInt2);
    }
  }

  private void OnMapSurfaceRenderer(int paramInt)
  {
    if (this.mMapcallback != null) {
      this.mMapcallback.OnMapSurfaceRenderer(this.mGL, this, paramInt);
    }
  }

  private void OnMapProcessEvent()
  {
    if (this.mMapcallback != null) {
      this.mMapcallback.OnMapProcessEvent(this);
    }
  }

  private void OnMapDestory()
  {
    if (this.mMapcallback != null) {
      this.mMapcallback.OnMapDestory(this.mGL, this);
    }
  }

  private void OnClearCache(){

  }

  public void OnMapDataRequired(int mapType, String[] paramArrayOfString)
  {
    LogManager.writeLog(LogManager.productInfo, hashCode() + " MapDataRequire  datasourceType: " + mapType, 111);
    LogManager.writeLog(LogManager.productInfo, hashCode() + " MapCallback: " + this.mMapcallback, 111);
    if (this.mMapcallback != null) {
      try
      {
        this.mMapcallback.OnMapDataRequired(this, mapType, paramArrayOfString);
      }
      catch (Throwable localThrowable) {}
    }
  }

  public void OnMapLabelsRequired(int[] paramArrayOfInt, int paramInt)
  {
    if (this.mMapcallback != null) {
      this.mMapcallback.OnMapLabelsRequired(this, paramArrayOfInt, paramInt);
    }
  }

  private byte[] OnMapCharsWidthsRequired(int[] paramArrayOfInt, int paramInt1, int paramInt2)
  {
    if (this.mMapcallback != null) {
      return this.mMapcallback.OnMapCharsWidthsRequired(this, paramArrayOfInt, paramInt1, paramInt2);
    }
    return null;
  }

  private void OnMapReferencechanged(String paramString1, String paramString2)
  {
    if (this.mMapcallback != null) {
      this.mMapcallback.OnMapReferencechanged(this, paramString1, paramString2);
    }
  }

  public void onIndoorBuildingActivity(byte[] paramArrayOfByte)
  {
    if (this.mMapcallback != null) {
      try
      {
        this.mMapcallback.onIndoorBuildingActivity(this, paramArrayOfByte);
      }
      catch (Throwable localThrowable) {}
    }
  }

  public void onIndoorDataRequired(int paramInt, String[] paramArrayOfString, int[] paramArrayOfInt1, int[] paramArrayOfInt2)
  {
    if (this.mMapcallback != null) {
      try
      {
        this.mMapcallback.onIndoorDataRequired(this, paramInt, paramArrayOfString, paramArrayOfInt1, paramArrayOfInt2);
      }
      catch (Throwable localThrowable) {}
    }
  }
  public boolean getAuthStatus(){
//      int auth = nativeGetAuthStatus();
//    if(auth==1){
//      return true;
//    }
    return true;
  }

  public void destroy()
          throws Throwable
  {
    if (this.native_instance != 0L)
    {
      nativeDestroy(this.native_instance/*, this*/);
      this.native_instance = 0L;
      this.textTextureGenerator = null;

      this.tmp_3072bytes_data = null;
    }
  }

  public void fillCurGridListWithDataType(ArrayList<MapSourceGridData> paramArrayList, int paramInt)
  {
    if (this.native_instance != 0L)
    {
      nativeGetScreenGrids(this.native_instance, this.tmp_3072bytes_data, paramInt);
      int i = 0;
      int j = this.tmp_3072bytes_data[(i++)];
      if ((j <= 0) || (j > 100) || (paramArrayList == null)) {
        return;
      }
      paramArrayList.clear();

      ArrayList<MapSourceGridData> localArrayList = paramArrayList;
      for (int k = 0; (k < j) && (i < 3072); k++)
      {
        int m = this.tmp_3072bytes_data[(i++)];
        if ((m > 0) && (m <= 20) && (i + m <= 3072))
        {
          String str = new String(this.tmp_3072bytes_data, i, m);
          i += m;
          if (paramInt == 10)
          {
            int n = Convert.getShort(this.tmp_3072bytes_data, i);

            i += 2;
            i++;
            localArrayList.add(new MapSourceGridData(str, paramInt, n, 0));
          }
          else
          {
            i++;
            localArrayList.add(new MapSourceGridData(str, paramInt));
          }
        }
      }
    }
  }
  public static String GetDeviceID(){
    return deviceID;
  }

  /**
   * 网格号转换为tile
   * @param quadKey 网格号
   * @param tile 栅格
     */
  public void quadKey2Tile(String quadKey, Tile tile){
    nativeQuadKey2Tile( quadKey,  tile, coodSystem);
  }

  /**
   * tile转换为网格号
   * @param tile
   * @return
     */
  public String tile2QuadKey(Tile tile){
    return nativeTile2QuadKey(tile,coodSystem);
  }
  private static native long nativeNewInstance(String mapbasepath, String fontVersion, int projType, byte [] licenseInfo);
  //0代表未鉴权或鉴权失败， 1代表鉴权通过
  private static native int nativeGetAuthStatus();

  private static native String nativeGetVersion();

  private static native void nativeDestroy(long native_object/*, MapCore paramMapCore*/);

  private static native void nativeSurfaceCreate(long native_object, MapCore paramMapCore);

  private static native void nativeSurfaceChange(long native_object, MapCore paramMapCore, int paramInt1, int paramInt2);

  private static native void nativeSurfaceRender(long native_object);

  private static native void nativeSetMapState(long native_object, long paramLong2);

  private static native long nativeGetMapState(long native_object);

  private static native int nativeGetSelectedMapPoi(long native_object, int winx, int winy, int radius, MapPoi mappoi);

  private static native int nativePutMapData(long native_object, int paramInt, byte[] paramArrayOfByte);

  private static native void nativePutCharBitmap(long native_object, int paramInt, byte[] paramArrayOfByte);

  private static native void nativeGetScreenGrids(long native_object, byte[] paramArrayOfByte, int paramInt);

  private static native void nativeSetTextureData(long native_object, byte[] paramArrayOfByte, int paramInt);

  private static native void nativeSetStyleData(long native_object, byte[] paramArrayOfByte, int paramInt1, int paramInt2);

  private static native boolean nativeCanStopRenderMap(long native_object);

  private static native void nativeSetParameter(long native_object, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5);

  private static native void nativeQuadKey2Tile(String quadKey, Tile tile, int projType);

  private static native String nativeTile2QuadKey(Tile tile, int projType);

  private static native int nativeAddLayer(long native_object, int layerId);

  private static native int nativeRemoveLayer(long native_object, int layerId);


  //如下是暂时无用的接口定义
  //private static native int nativeGetSelectedMapPois(long native_object, int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte);
  //private static native void nativeChangeMapEnv(long native_object, String paramString);
  //private static native int nativeSelectMapPois(long native_object, int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte);
  //private static native int nativeGetMapStateInstance(long native_object);
  //private static native void nativeSetCityBound(long native_object, byte[] paramArrayOfByte);
  //private static native void nativeAddPoiFilter(long native_object, int paramInt1, int paramInt2, int paramInt3, float paramFloat1, float paramFloat2, String paramString);
  //private static native void nativeRemovePoiFilter(long native_object, String paramString);
  //private static native void nativeClearPoiFilter(long native_object);
  //private static native void nativeSetIndoorBuildingToBeActive(long paramLong, String paramString1, int paramInt, String paramString2);
}
