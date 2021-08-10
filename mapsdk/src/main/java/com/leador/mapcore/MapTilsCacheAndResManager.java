package com.leador.mapcore;

import android.content.Context;

public abstract class MapTilsCacheAndResManager
{
  public static final int STYLE_DATA = 1;
  public static final int ICONS_DATA = 2;
  public static final String LEADOR_PATH = "leador";
  public static final String LEADOR_DATA_PATH = "data";
  public static final String MAP_DATA_OFFLINE_PATH_NAME = "vmap";
  public static final String MAP_CACHE_PATH_NAME = "cache";
  public static final String MAP_TILES_PATH_NAME = "vmap4tiles";
  public static final String MAP_RES_EXT_PATH_NAME = "vmap4res";
  public static final String MAP_MAP_ASSETS_NAME = "lmap_assets";

  public static MapTilsCacheAndResManager getInstance(Context paramContext)
  {
    return MapTilsCacheAndResManagerImpl.instance(paramContext);
  }

  public abstract void checkDir();

  public abstract void clearOnlineMapTilsCache();

  public abstract String getBaseMapPath();

  public abstract String getMapCachePath();

  public abstract String getMapOnlineDataPath();

  public abstract String getMapExtResPath();

  public abstract String getMapOfflineDataPath();

  public abstract byte[] getStyleData(String paramString, RetStyleIconsFile paramRetStyleIconsFile);

  public abstract byte[] getIconsData(String paramString, RetStyleIconsFile paramRetStyleIconsFile);

  public abstract byte[] getOtherResData(String paramString);
  public abstract byte[] getStyleDataForPath(String path);

  public abstract void saveFile(String paramString, int paramInt1, int paramInt2, byte[] paramArrayOfByte);

  public abstract boolean canUpate(String paramString);

  public static class RetStyleIconsFile
  {
    public String fullName;
    public String name;
    public int serverVersion;
    public int clientVersion;
    public int type;
  }
}
