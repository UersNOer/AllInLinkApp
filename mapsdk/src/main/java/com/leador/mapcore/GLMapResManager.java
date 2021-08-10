package com.leador.mapcore;

import android.content.Context;

import com.unistrong.api.mapcore.MapDelegateImp;
import com.unistrong.api.mapcore.util.StylesIconsUpdate;
import com.unistrong.api.maps.MapController;

public class GLMapResManager
{
  public static final int TEXTURE_ICON = 0;
  public static final int TEXTURE_BACKGROUND = 1;
  public static final int TEXTURE_TOP_COVER = 41;
  public static final int TEXTURE_BIG_ICON = 20;
  public static final int TEXTURE_ROAD_BOARD = 51;
  public static final int TEXTURE_TMC = 52;
  public static final int TEXTURE_ROAD = 53;


  public static enum MapViewMode
  {
    NORAML,  SATELLITE,  BUS,STREETVIEW,EAGLE_EYE;
    
    private MapViewMode() {}
  }
  
  public static enum MapViewTime
  {
    DAY,  NIGHT;
    
    private MapViewTime() {}
  }
  
  public static enum MapViewModeState
  {
    NORMAL,  PREVIEW_CAR,  PREVIEW_BUS,  PREVIEW_FOOT,  NAVI_CAR,  NAVI_BUS,  NAVI_FOOT;
    
    private MapViewModeState() {}
  }
  
  public boolean isBigIcon = true;
  private MapDelegateImp mapDelegateImp = null;
  private Context mContext = null;
  private MapCore mMapCore = null;


  private static final String texture_background_day = "bktile.data";
  private static final String texture_abckground_night = "bktile_n.data";
  private static final String texture_roadboard_day = "rbt_1.data"; //道路路牌白天资源
  private static final String texture_roadboard_night = "rbt_2.data"; //道路路牌黑夜资源
  private static final String texture_tmc_day = "tt_1.data"; //TMC白天资源
  private static final String texture_tmc_night = "tt_2.data";  //TMC黑夜资源
  private static final String texture_road = "rdt.data";  //其他资源 - 道路、铁路、过街天桥
  private static final String texture_top_cover_day = "3dAir_light.data";
  private static final String texture_top_cover_night = "3dAir_night.data";
  private static final String style_day_default = "s1051466495472.data";
  private static final String style_night_default = "s2051469094516.data";
  private static final String style_satelite_day_night = "s3051450262818.data";
  private static final String style_navi_car_day = "s4051469178172.data";
  private static final String style_navi_car_night = "s5051469088454.data";
  private static final String styel_map_yingyan ="yingyan20170515.data";
//  private static final String style_day_default = "style_1_5_1466495472.data";
//  private static final String style_night_default = "style_2_5_1469094516.data";
//  private static final String style_satelite_day_night = "style_3_5_1450262818.data";
//  private static final String style_navi_car_day = "style_4_5_1469178172.data";
//  private static final String style_navi_car_night = "style_5_5_1469088454.data";
  private static final String texture_icon_day = "i1051466998222.data";
  private static final String texture_icon_night = "i2051466998222.data";
//  private static final String texture_icon_day = "icons_1_5_1466998222.data";
//  private static final String texture_icon_night = "icons_2_5_1466998222.data";


  public GLMapResManager(MapDelegateImp paramMapDelegateImp, Context context)
  {
    this.mapDelegateImp = paramMapDelegateImp;
    this.mContext = context;
    this.mMapCore = this.mapDelegateImp.getMapCore();
  }
  private void updataStyleIcons(MapTilsCacheAndResManager.RetStyleIconsFile  flie,int styleIconMode){
    new StylesIconsUpdate(mContext, flie,styleIconMode).start();//开启线程更新样式
  }

  public void setStyleDataForPath(String stylePath,String iconPaht){


    if(stylePath!=null&&!"".equals(stylePath)){
      byte[] data=MapTilsCacheAndResManager.getInstance(this.mContext).getStyleDataForPath(stylePath);
      if(data!=null&&data.length>0){
        this.mMapCore.setStyleData(data, 0, 1);
      }
    }
    if(iconPaht!=null&&!"".equals(iconPaht)){
      byte[] iconData=MapTilsCacheAndResManager.getInstance(this.mContext).getStyleDataForPath(iconPaht);
      if(iconData!=null&&iconData.length>0)
        this.mMapCore.setTexture(iconData, TEXTURE_ICON);
    }

  }

  /*
  设置风格样式
   */
  public void setStyleData()
  {
    if (this.mapDelegateImp == null) {
      return;
    }
    try{
      byte[] arrayOfByte1 = null;
      String str = getStyleName();//获取当前模式名称
      if(mapDelegateImp.getMapType() == MapController.MAP_TYPE_EAGLE_EYE){
        byte[] data=ResUtil.decodeAssetResData(this.mContext,MapTilsCacheAndResManager.MAP_MAP_ASSETS_NAME+"/"+ str);
        if(data!=null&&data.length>0){
          this.mMapCore.setStyleData(data, 0, 1);
        }
      }else {
        MapTilsCacheAndResManager.RetStyleIconsFile styleFile = new MapTilsCacheAndResManager.RetStyleIconsFile();
        arrayOfByte1 = MapTilsCacheAndResManager.getInstance(this.mContext).getStyleData(str, styleFile);//读取样式文件
        this.mMapCore.setStyleData(arrayOfByte1, 0, 1);
        updataStyleIcons(styleFile, 0);//开启线程更新样式
      }
    }catch (Exception ex){
      ex.printStackTrace();
    }

  }
  /*
  设置图标风格
   */
  public void setIconsData(boolean isGLThread)
  {
    if (this.mapDelegateImp == null) {
      return;
    }
    byte[] arrayOfByte1 = null;
    byte[] arrayOfByte2 = null;
    MapTilsCacheAndResManager.RetStyleIconsFile iconsFile = new MapTilsCacheAndResManager.RetStyleIconsFile();
    String str1 = getIconName();
    String str2 = getBigIconName(str1);
    arrayOfByte1 = MapTilsCacheAndResManager.getInstance(this.mContext).getIconsData(str1, iconsFile);

    if (this.isBigIcon)
    {
    	MapTilsCacheAndResManager.RetStyleIconsFile localObject = new MapTilsCacheAndResManager.RetStyleIconsFile();
      arrayOfByte2 = MapTilsCacheAndResManager.getInstance(this.mContext).getIconsData(str2, (MapTilsCacheAndResManager.RetStyleIconsFile)localObject);
    }
    updataStyleIcons(iconsFile,1);//开启线程更新图标
    if (isGLThread)
    {
      this.mMapCore.setTexture(arrayOfByte1, TEXTURE_ICON); //0
      if (this.isBigIcon) {
        this.mMapCore.setTexture(arrayOfByte2, TEXTURE_BIG_ICON); //20
      }
    }
    else
    {
      final byte[] arrayOfByte3 = arrayOfByte1;
      final byte[] arrayOfByte4 = arrayOfByte2;
      this.mapDelegateImp.a(new Runnable()
      {
        public void run()
        {
          mMapCore.setTexture(arrayOfByte3, TEXTURE_ICON); //0
          if (GLMapResManager.this.isBigIcon) {
            mMapCore.setTexture(arrayOfByte4, TEXTURE_BIG_ICON); //
          }
        }
      });
    }
  }
  
  private String getBigIconName(String paramString)
  {
    this.isBigIcon = false;
    return null;
  }
  /*
  风格名称定义
   */
  public String getStyleName()
  {
    String str = "";
    if (this.mapDelegateImp == null) {
      return str;
    }
    MapViewTime localMapViewTime = this.mapDelegateImp.getMapViewTime();//时间
    MapViewMode localMapViewMode = this.mapDelegateImp.getMapViewMode();//卫星/正常
    MapViewModeState localMapViewModeState = this.mapDelegateImp.getMapViewModeState();//驾车/正常
    if (MapViewTime.DAY == localMapViewTime) { //白天
      if (MapViewMode.NORAML == localMapViewMode) {//地图模式
        if (MapViewModeState.NAVI_CAR == localMapViewModeState) {
          str = style_navi_car_day;//驾车模式
        }else{
          str = style_day_default;//正常模式
        }

      } else if (MapViewMode.SATELLITE == localMapViewMode) { //卫星模式
        if (MapViewModeState.NAVI_CAR == localMapViewModeState) {
          str = style_navi_car_day;//驾车模式
        }else{
          str = style_satelite_day_night;//晚上卫星模式
        }
      } else if(MapViewMode.EAGLE_EYE == localMapViewMode){//鹰眼地图
        str = styel_map_yingyan;
      }  else {
        str = style_day_default;//正常地图模式
      }
    }else {//晚上
      if (MapViewMode.NORAML == localMapViewMode) {//地图模式
        if (MapViewModeState.NAVI_CAR == localMapViewModeState) {
          str = style_navi_car_night;//晚上加车模式
        }else{
          str = style_night_default;//晚上地图模式
        }

      } else if (MapViewMode.SATELLITE == localMapViewMode) {//卫星模式
        if (MapViewModeState.NAVI_CAR == localMapViewModeState) {
          str = style_navi_car_night;//晚上驾车模式
        }else{
          str = style_satelite_day_night;//晚上卫星模式
        }
      } else if(MapViewMode.EAGLE_EYE == localMapViewMode){//鹰眼地图
        str = styel_map_yingyan;
      } else {
        str = style_night_default;//正常晚上模式
      }
    }
    return str;
  }
  
  public String getIconName()
  {
    String str = "";
    if (this.mapDelegateImp == null) {
      return str;
    }
    MapViewTime localMapViewTime = this.mapDelegateImp.getMapViewTime();
    MapViewMode localMapViewMode = this.mapDelegateImp.getMapViewMode();
    if (MapViewTime.DAY == localMapViewTime)
    {
      if (MapViewMode.BUS == localMapViewMode) {
        str = texture_icon_day; //有待同正常模式区分
      } else {
        str = texture_icon_day;
      }
    }
    else if (MapViewTime.NIGHT == localMapViewTime) {
      if (MapViewMode.BUS == localMapViewMode) {
        str = texture_icon_night; //有待同正常模式区分
      } else {
        str = texture_icon_night;
      }
    }
    return str;
  }
  
  public void setTrafficTexture(boolean isGLThread)
  {
    String textureName;
    if (this.mapDelegateImp.getMapViewTime() != MapViewTime.NIGHT)
    {
      textureName = texture_tmc_day;
    }
    else
    {
      textureName = texture_tmc_night;
    }

    setTextureUtil(isGLThread, textureName, TEXTURE_TMC);
  }
  
  public void setBkTexture(boolean isGLThread)
  {
    byte[] backgroundBytes = null;
    byte[] topcoverBytes = null;
    if (this.mapDelegateImp.getMapViewTime() != MapViewTime.NIGHT)
    {
      backgroundBytes = MapTilsCacheAndResManager.getInstance(this.mContext).getOtherResData(texture_background_day); //"bktile.data"
      topcoverBytes = MapTilsCacheAndResManager.getInstance(this.mContext).getOtherResData(texture_top_cover_day); //"3dAir_light.data"
    }
    else
    {
      backgroundBytes = MapTilsCacheAndResManager.getInstance(this.mContext).getOtherResData(texture_abckground_night);//"bktile_n.data"
      topcoverBytes = MapTilsCacheAndResManager.getInstance(this.mContext).getOtherResData(texture_top_cover_night); //"3dAir_night.data"
    }

    if (isGLThread)
    {
      this.mMapCore.setTexture(backgroundBytes, TEXTURE_BACKGROUND); //1
      this.mMapCore.setTexture(topcoverBytes, TEXTURE_TOP_COVER); //41
    }
    else
    {
      final byte[] finalbackgroundBytes = backgroundBytes;
      final byte[] finaltopcoverBytes = topcoverBytes;
      this.mapDelegateImp.a(new Runnable()
      {
        public void run()
        {
          GLMapResManager.this.mMapCore.setTexture(finalbackgroundBytes, TEXTURE_BACKGROUND); //1
          GLMapResManager.this.mMapCore.setTexture(finaltopcoverBytes, TEXTURE_TOP_COVER); //41
        }
      });
    }
  }
  
  public void setRoadGuideTexture(boolean isGLThread) {
    String textureName;
    if (this.mapDelegateImp.getMapViewTime() != MapViewTime.NIGHT){
      textureName = texture_roadboard_day;
    }else{
      textureName = texture_roadboard_night;
    }
    setTextureUtil(isGLThread, textureName, TEXTURE_ROAD_BOARD);
  }
  
  public void setOtherMapTexture(boolean isGLThread){
    setTextureUtil(isGLThread, texture_road, TEXTURE_ROAD);
  }

  private void setTextureUtil(boolean isGLThread, String textureName, int textureId)
  {
    byte[] textureBytes = MapTilsCacheAndResManager.getInstance(this.mContext).getOtherResData(textureName);

    if (isGLThread)
    {
      this.mMapCore.setTexture(textureBytes, textureId);
    }
    else
    {
      final byte [] fTextureBytes = textureBytes;
      final int fTextureId = textureId;
      this.mapDelegateImp.a(new Runnable()
      {
        public void run()
        {
          GLMapResManager.this.mMapCore.setTexture(fTextureBytes, fTextureId);
        }
      });
    }
  }
}
