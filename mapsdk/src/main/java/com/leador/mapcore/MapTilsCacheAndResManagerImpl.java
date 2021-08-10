package com.leador.mapcore;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MapTilsCacheAndResManagerImpl extends MapTilsCacheAndResManager
{
  private static volatile MapTilsCacheAndResManagerImpl instance = null;
  private Context mContext;
  private String mMapBaseDataPath;
  private String mMapOfflinePath;
  private String mCachePath;
  private String mMapOnlineTilesPath;
  private String mMapExtResPath;
  private static final long Style_Update_Internal_Time = 43200000L;
  private static final int CREATE_DIR_COUNT = 5;

  public static MapTilsCacheAndResManagerImpl instance(Context paramContext)
  {
    if (instance == null) {
      instance = new MapTilsCacheAndResManagerImpl(paramContext);
    }
    return instance;
  }

  private MapTilsCacheAndResManagerImpl(Context paramContext)
  {
    this.mContext = paramContext;
    init();
  }

  public synchronized void reset()
  {
    instance = null;
  }

  public String getBaseMapPath()
  {
    return this.mMapBaseDataPath;
  }

  public String getMapCachePath()
  {
    return this.mCachePath;
  }

  public String getMapOnlineDataPath()
  {
    return this.mMapOnlineTilesPath;
  }

  public String getMapOfflineDataPath()
  {
    return this.mMapOfflinePath;
  }

  public String getMapExtResPath()
  {
    return this.mMapExtResPath;
  }

  public byte[] getStyleData(String paramString, MapTilsCacheAndResManager.RetStyleIconsFile paramRetStyleIconsFile)
  {
    try
    {
      return getStyleIconsData(paramString, 1, paramRetStyleIconsFile);
    }
    catch (IOException localIOException) {}
    return null;
  }

  public byte[] getIconsData(String paramString, MapTilsCacheAndResManager.RetStyleIconsFile paramRetStyleIconsFile)
  {
    try
    {
      return getStyleIconsData(paramString, 2, paramRetStyleIconsFile);
    }
    catch (IOException localIOException) {}
    return null;
  }

  public byte[] getOtherResData(String paramString)
  {
    return ResUtil.decodeAssetResData(this.mContext, MAP_MAP_ASSETS_NAME +"/" + paramString);
  }

  public void init()
  {
    try
    {
      initRelease();
    }
    catch (Throwable localThrowable) {}
  }
  /*
  定义各个缓存目录路径
   */
  private void initRelease()
  {
    int i = 0;
    File localFile1 = new File(FileUtil.getMapBaseStorage(this.mContext));
    if (!localFile1.exists()) {
      localFile1.mkdir();
    }
    File localFile2 = new File(localFile1, "data");
    if (!localFile2.exists()) {
      localFile2.mkdir();
    }
    this.mMapBaseDataPath = (localFile2.toString() + "/");

    File localFile3 = new File(localFile2, "vmap");
    if (!localFile3.exists()) {
      localFile3.mkdir();
    }
    this.mMapOfflinePath = (localFile3.toString() + "/");

    File localFile4 = new File(localFile2, "cache");

    i = 0;
    while ((!localFile4.exists()) && (i++ < 5)) {
      localFile4.mkdir();
    }
    this.mCachePath = (localFile4.toString() + "/");

    i = 0;
    File localFile5 = new File(localFile4, "vmap4tiles");
    while ((!localFile5.exists()) && (i++ < 5)) {
      localFile5.mkdir();
    }
    this.mMapOnlineTilesPath = (localFile5.toString() + "/");

    i = 0;
    File localFile6 = new File(localFile4, "vmap4res");
    while ((!localFile6.exists()) && (i++ < 5)) {
      localFile6.mkdir();
    }
    this.mMapExtResPath = (localFile6.toString() + "/");
  }

  void initDebug() {}

  public void saveFile(String paramString, int paramInt1, int paramInt2, byte[] paramArrayOfByte)
  {
    if ((paramArrayOfByte == null) || (paramArrayOfByte.length <= 0)) {
      return;
    }
    File localFile = new File(this.mMapExtResPath);
    File[] arrayOfFile = localFile.listFiles();
    if (arrayOfFile != null) {
      for (File localObject2 : arrayOfFile) {
        if (((File)localObject2).getName().contains(paramString))
        {
          ((File)localObject2).delete();

          break;
        }
      }
    }

    String ssssssa = paramString + "" + paramInt1 + "" + paramInt2 + ".data";
//    String ssssssa = paramString + "_" + paramInt1 + "_" + paramInt2 + ".data";

    FileUtil.writeDatasToFile(this.mMapExtResPath + (String)ssssssa, paramArrayOfByte);
  }

  private String getFilePreName(String paramString)
  {

    return paramString.substring(0,4);
//    String[] arrayOfString = paramString.split("_");
//    return arrayOfString[0] + "_" + arrayOfString[1] + "_" + arrayOfString[2];
  }

  private void setRetFile(MapTilsCacheAndResManager.RetStyleIconsFile retStyleIconsFile, String paramString)
  {
    if (retStyleIconsFile == null) {
      return;
    }
    retStyleIconsFile.fullName = paramString;
    retStyleIconsFile.name = (paramString.charAt(0) + "" + paramString.charAt(1));
    retStyleIconsFile.clientVersion = Integer.parseInt(String.valueOf(paramString.charAt(2)+""+paramString.charAt(3)));
    retStyleIconsFile.serverVersion = Integer.parseInt(paramString.substring(4,paramString.lastIndexOf(".")));
//TODO ZYC 屏蔽原样式文件读取
//    retStyleIconsFile.fullName = paramString;
//    String[] arrayOfString = paramString.split("_|\\.");
//    retStyleIconsFile.name = (arrayOfString[0] + "_" + arrayOfString[1]);
//    retStyleIconsFile.clientVersion = Integer.parseInt(arrayOfString[2]);
//    retStyleIconsFile.serverVersion = Integer.parseInt(arrayOfString[3]);
  }
  /*
  读取样式资源文件
   */
  private byte[] getStyleIconsData(String styleFileName, int paramInt, MapTilsCacheAndResManager.RetStyleIconsFile paramRetStyleIconsFile)   throws IOException
  {
    String str = getFilePreName(styleFileName);//截取前3位名称

    MapTilsCacheAndResManager.RetStyleIconsFile retStyleIconsFile = new MapTilsCacheAndResManager.RetStyleIconsFile();
    setRetFile(retStyleIconsFile, styleFileName);//解析风格名称并封装信息
    File[] files = new File(this.mMapExtResPath).listFiles();//vmap4res目录
    if (files != null) {
      try
      {
        for (File file : files) {
          if (file.getName().contains(str))
          {
            setRetFile(paramRetStyleIconsFile, file.getName());
            if (retStyleIconsFile.serverVersion < paramRetStyleIconsFile.serverVersion)//目录中文件版本大于默认样式文件版本
            {
              byte[] data = FileUtil.readFileContents(file .getAbsolutePath());
              if ((data != null) && (data.length > 0)) {
                if (paramInt == 1)//style
                {
//                  int index = 0;
//                  int length = Convert.getInt(data, index);
//
//                  index += 4;
                  if (data!=null) {
                    return data;
                  }
                  FileUtil.deleteFile(file);
                }
                else//icon
                {
                  return data;
                }
              }
            }
            else
            {
              FileUtil.deleteFile(file);
            }
          }
        }
      }
      catch (OutOfMemoryError localOutOfMemoryError) {}
    }
    setRetFile(paramRetStyleIconsFile, styleFileName);

    return ResUtil.decodeAssetResData(this.mContext, MAP_MAP_ASSETS_NAME +"/"+ styleFileName);
  }

  public byte[] getStyleDataForPath(String path){

    byte[] data = FileUtil.readFileContents(path);
    if ((data != null) && (data.length > 0)) {
      if (data!=null) {
        return data;
      }
    }return null;
  }

  static void copyAssertToTmp(Context paramContext, String paramString, File paramFile)
  {
    if (paramFile.exists()) {
      return;
    }
    try
    {
      if (paramFile.createNewFile())
      {
        byte[] arrayOfByte = ResUtil.decodeAssetResData(paramContext, paramString);
        if (arrayOfByte != null)
        {
          FileOutputStream localFileOutputStream = new FileOutputStream(paramFile);
          localFileOutputStream.write(arrayOfByte);
          localFileOutputStream.close();
        }
      }
    }
    catch (IOException localIOException) {}
  }

  public void checkDir()
  {
    init();
  }

  public void clearOnlineMapTilsCache()
  {
    File localFile = new File(this.mMapOnlineTilesPath);
    if (localFile.exists())
    {
      FileUtil.deleteFile(localFile);
      init();
    }
  }

  private void addUdateRecorder(String paramString)
  {
    SharedPreferences localSharedPreferences = this.mContext.getSharedPreferences("styles_icons_update_recorder", 0);

    SharedPreferences.Editor localEditor = localSharedPreferences.edit();
    localEditor.putLong(paramString, System.currentTimeMillis());
    localEditor.commit();
  }

  public synchronized boolean canUpate(String paramString)
  {
    boolean bool = true;
    SharedPreferences localSharedPreferences = this.mContext.getSharedPreferences("styles_icons_update_recorder", 0);

    long l = localSharedPreferences.getLong(paramString, -1L);
    if ((l > 0L) &&
            (System.currentTimeMillis() - l < 43200000L)) {
      bool = false;
    }
    if (bool) {
      addUdateRecorder(paramString);
    }
    return bool;
  }
}
