package com.leador.mapcore;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

public abstract class BaseMapCallImplement
        implements IBaseMapCallback, IMapCallback {
    private ArrayList<MapSourceGridData> roadReqMapGrids = new ArrayList();
    private ArrayList<MapSourceGridData> bldReqMapGrids = new ArrayList();
    private ArrayList<MapSourceGridData> regionReqMapGrids = new ArrayList();
    private ArrayList<MapSourceGridData> poiReqMapGrids = new ArrayList();
    private ArrayList<MapSourceGridData> versionMapGrids = new ArrayList();
    private ArrayList<MapSourceGridData> indoorMapGrids = new ArrayList();
    private ArrayList<MapSourceGridData> vectmcReqMapGirds = new ArrayList();
    private ArrayList<MapSourceGridData> stiReqMapGirds = new ArrayList();
    private ArrayList<MapSourceGridData> curRoadMapGrids = new ArrayList();
    private ArrayList<MapSourceGridData> curBldMapGrids = new ArrayList();
    private ArrayList<MapSourceGridData> curRegionMapGrids = new ArrayList();
    private ArrayList<MapSourceGridData> curPoiMapGrids = new ArrayList();
    private ArrayList<MapSourceGridData> curVectmcMapGirds = new ArrayList();
    private ArrayList<MapSourceGridData> curStiMapGirds = new ArrayList();
    private ArrayList<MapSourceGridData> curScreenGirds = new ArrayList();
    private ArrayList<MapSourceGridData> curIndoorMapGirds = new ArrayList();
    private ArrayList<MapSourceGridData> curStreetViewGirds = new ArrayList();
    private ArrayList<MapSourceGridData> curBmpMapGirds = new ArrayList();
    ConnectionManager connectionManager = null;
    d tileProcessCtrl = null;
    TextTextureGenerator textTextureGenerator = null;
    Object mapGridFillLock = new Object();

    public void OnMapDataRequired(MapCore mapCore, int mapType, String[] gridNames) {
        if ((gridNames == null) || (gridNames.length == 0)) {
            return;
        }
        ArrayList<MapSourceGridData> dataList = null;

        dataList = getReqGridList(mapType);
        if (dataList != null && mapType == BaseMapLoader.DATA_SOURCE_TYPE_DATA_BMP_USER_GIRDSMAP) {
            dataList.clear();
            int len = gridNames.length;
            for (int i = 0; i < len; i++) {
                dataList.add(new MapSourceGridData(gridNames[i], mapType));
            }
            for (MapSourceGridData mapSourceGridData : dataList) {
                ArrayList<MapSourceGridData> mapSourceGrids = new ArrayList<MapSourceGridData>();
                mapSourceGrids.add(mapSourceGridData);
                proccessRequiredData(mapCore,mapSourceGrids,mapType);
            }
        } else {
            dataList.clear();
            for (int i = 0; i < gridNames.length; i++) {
                dataList.add(new MapSourceGridData(gridNames[i], mapType));
            }
            if (mapType != BaseMapLoader.DATA_SOURCE_TYPE_DATA_SCREEN) {
                proccessRequiredData(mapCore, dataList, mapType);
            }
        }
    }

    public void OnMapProcessEvent(MapCore paramMapCore) {
    }

    public ArrayList<MapSourceGridData> getReqGridList(int mapType) {
        if (mapType == BaseMapLoader.DATA_SOURCE_TYPE_DATA_ROAD) {
            return this.roadReqMapGrids;
        }
        if (mapType == BaseMapLoader.DATA_SOURCE_TYPE_DATA_GEO_BUILDING) {
            return this.bldReqMapGrids;
        }
        if (mapType == BaseMapLoader.DATA_SOURCE_TYPE_DATA_STANDARD) {
            return this.regionReqMapGrids;
        }
        if (mapType == BaseMapLoader.DATA_SOURCE_TYPE_DATA_POI) {
            return this.poiReqMapGrids;
        }
        if (mapType == BaseMapLoader.DATA_SOURCE_TYPE_DATA_VEC_TMC) {
            return this.vectmcReqMapGirds;
        }
        if (mapType == BaseMapLoader.DATA_SOURCE_TYPE_DATA_SCREEN) {
            return this.curScreenGirds;
        }
        if (mapType == BaseMapLoader.DATA_SOURCE_TYPE_DATA_SATELLITE) {
            return this.stiReqMapGirds;
        }
        if (mapType == BaseMapLoader.DATA_SOURCE_TYPE_DATA_VERSION) {
            return this.versionMapGrids;
        }
        if (mapType == BaseMapLoader.DATA_SOURCE_TYPE_DATA_INDOOR) {
            return this.indoorMapGrids;
        }
        if (mapType == BaseMapLoader.DATA_SOURCE_TYPE_DATA_BMP_USER_GIRDSMAP) {
            return this.curBmpMapGirds;
        }
        if (mapType == BaseMapLoader.DATA_SOURCE_TYPE_DATA_STREETVIEW) {
            return this.curStreetViewGirds;
        }
        return null;
    }

    public void OnMapSurfaceRenderer(GL10 gl10, MapCore mapCore, int paramInt) {
        synchronized (this.mapGridFillLock) {
            mapCore.fillCurGridListWithDataType(this.curPoiMapGrids, BaseMapLoader.DATA_SOURCE_TYPE_DATA_POI);

            mapCore.fillCurGridListWithDataType(this.curRoadMapGrids, BaseMapLoader.DATA_SOURCE_TYPE_DATA_ROAD);

            mapCore.fillCurGridListWithDataType(this.curRegionMapGrids, BaseMapLoader.DATA_SOURCE_TYPE_DATA_STANDARD);

            mapCore.fillCurGridListWithDataType(this.curBldMapGrids, BaseMapLoader.DATA_SOURCE_TYPE_DATA_GEO_BUILDING);

            mapCore.fillCurGridListWithDataType(this.curVectmcMapGirds, BaseMapLoader.DATA_SOURCE_TYPE_DATA_VEC_TMC);

            mapCore.fillCurGridListWithDataType(this.curStiMapGirds, BaseMapLoader.DATA_SOURCE_TYPE_DATA_SATELLITE);

            mapCore.fillCurGridListWithDataType(this.curIndoorMapGirds, BaseMapLoader.DATA_SOURCE_TYPE_DATA_INDOOR);

            mapCore.fillCurGridListWithDataType(this.curStreetViewGirds, BaseMapLoader.DATA_SOURCE_TYPE_DATA_STREETVIEW);

            mapCore.fillCurGridListWithDataType(this.curBmpMapGirds, BaseMapLoader.DATA_SOURCE_TYPE_DATA_BMP_USER_GIRDSMAP);
        }
    }

    public ArrayList<MapSourceGridData> getCurGridList(int paramInt) {
        if (paramInt == BaseMapLoader.DATA_SOURCE_TYPE_DATA_ROAD) {
            return this.curRoadMapGrids;
        }
        if (paramInt == BaseMapLoader.DATA_SOURCE_TYPE_DATA_GEO_BUILDING) {
            return this.curBldMapGrids;
        }
        if (paramInt == BaseMapLoader.DATA_SOURCE_TYPE_DATA_STANDARD) {
            return this.curRegionMapGrids;
        }
        if (paramInt == BaseMapLoader.DATA_SOURCE_TYPE_DATA_POI) {
            return this.curPoiMapGrids;
        }
        if (paramInt == BaseMapLoader.DATA_SOURCE_TYPE_DATA_VEC_TMC) {
            return this.curVectmcMapGirds;
        }
        if (paramInt == BaseMapLoader.DATA_SOURCE_TYPE_DATA_SCREEN) {
            return this.curScreenGirds;
        }
        if (paramInt == BaseMapLoader.DATA_SOURCE_TYPE_DATA_INDOOR) {
            return this.curIndoorMapGirds;
        }
        if (paramInt == BaseMapLoader.DATA_SOURCE_TYPE_DATA_SATELLITE) {
            return this.curStiMapGirds;
        }
        if (paramInt == BaseMapLoader.DATA_SOURCE_TYPE_DATA_BMP_USER_GIRDSMAP){
            return this.curBmpMapGirds;
        }
        return null;
    }

    public boolean isGridsInScreen(ArrayList<MapSourceGridData> paramArrayList, int paramInt) {
        try {
            if (paramArrayList.size() == 0) {
                return false;
            }
            if (!isMapEngineValid()) {
                return false;
            }
            synchronized (this.mapGridFillLock) {
                ArrayList localArrayList = getCurGridList(paramInt);
                if (localArrayList == null) {
                    return true;
                }
                for (int i = 0; i < paramArrayList.size(); i++) {
                    if (isGridInList(((MapSourceGridData) paramArrayList.get(i)).getGridName(), localArrayList)) {
                        return true;
                    }
                }
                return false;
            }

        } catch (Exception localException) {
            localException.printStackTrace();
        }

        return true;
    }

    public boolean isGridInScreen(int paramInt, String paramString) {
        try {
            if (!isMapEngineValid()) {
                return false;
            }
            synchronized (this.mapGridFillLock) {
                ArrayList localArrayList = getCurGridList(paramInt);
                if (isGridInList(paramString, localArrayList)) {
                    return true;
                }
                return false;
            }

        } catch (Exception localException) {
        }

        return true;
    }

    private boolean isGridInList(String paramString, ArrayList<MapSourceGridData> paramArrayList) {
        for (int i = 0; i < paramArrayList.size(); i++) {
            if (((MapSourceGridData) paramArrayList.get(i)).getGridName().equals(paramString)) {
                return true;
            }
        }
        return false;
    }


    protected void proccessRequiredData(MapCore paramMapCore, ArrayList<MapSourceGridData> paramArrayList, int mapType) {
        ArrayList localArrayList = new ArrayList();
        for (int i = 0; i < paramArrayList.size(); i++) {
            MapSourceGridData localMapSourceGridData1 = (MapSourceGridData) paramArrayList.get(i);
            if ((this.tileProcessCtrl == null) || (!this.tileProcessCtrl.b(localMapSourceGridData1.getKeyGridName()))) {
                Object localObject2;
                Object localObject3;
                if (mapType == BaseMapLoader.DATA_SOURCE_TYPE_DATA_VEC_TMC) {//矢量实时交通数据
                    localObject2 = VTMCDataCache.getInstance();
                    localObject3 = ((VTMCDataCache) localObject2).getData(localMapSourceGridData1.getGridName(), true);

                    CacheData cacheData = ((VTMCDataCache) localObject2).getData(localMapSourceGridData1.getGridName(), false);
                    if (localObject3 != null) {
                        localMapSourceGridData1.obj = ((CacheData) localObject3).d;
                    }
                    if ((cacheData != null) && (cacheData.mapData != null) && (cacheData.mapData.length > 0)) {
                        paramMapCore.putMapData(cacheData.mapData, 0, cacheData.mapData.length, mapType, cacheData.createTime);
                    } else {
                        localArrayList.add(localMapSourceGridData1);
                    }
                } else {
                    try {
                        localObject2 = localMapSourceGridData1.gridName;
                        if (mapType == BaseMapLoader.DATA_SOURCE_TYPE_DATA_INDOOR) {
                            localObject2 = localMapSourceGridData1.gridName + "-" + localMapSourceGridData1.mIndoorIndex;
                        }
                        localObject3 = VMapDataCache.getInstance().getRecoder((String) localObject2, mapType);
                        if ((localObject3 != null) && (((e) localObject3).a != null) &&
                                (((e) localObject3).a.length() > 0)) {
                        }
                        localArrayList.add(localMapSourceGridData1);
                    } catch (Exception localException) {
                    }
                }
            }
        }
        if (localArrayList.size() > 0) {
            BaseMapLoader mapLoader = null;
            mapLoader = new NormalMapLoader(paramMapCore, this, mapType);
            for (int j = 0; j < localArrayList.size(); j++) {
                MapSourceGridData localMapSourceGridData2 = (MapSourceGridData) localArrayList.get(j);
                this.tileProcessCtrl.c(localMapSourceGridData2.getKeyGridName());
                mapLoader.addReuqestTiles(localMapSourceGridData2);
            }
            if (this.connectionManager != null) {
                this.connectionManager.insertConntionTask(mapLoader);
            }
        }
    }

    public void OnMapSurfaceCreate(MapCore paramMapCore) {
    }

    public synchronized void onPause() {
        try {
            if (this.connectionManager != null) {
                this.connectionManager.threadFlag = false;
                if (this.connectionManager.isAlive()) {
                    this.connectionManager.interrupt();
                    this.connectionManager.shutDown();
                    this.connectionManager = null;
                }
            }
        } catch (Throwable localThrowable) {
            localThrowable.printStackTrace();
        }
    }

    public synchronized void onResume(MapCore paramMapCore) {
        try {
            this.connectionManager = new ConnectionManager(paramMapCore);
            this.tileProcessCtrl = new d();
            this.connectionManager.start();
        } catch (Throwable localThrowable) {
            localThrowable.printStackTrace();
        }
    }

    public void OnMapDestory(MapCore paramMapCore) {
        try {
            destoryMap(paramMapCore);
        } catch (Throwable localThrowable) {
            localThrowable.printStackTrace();
        }
    }

    public byte[] OnMapCharsWidthsRequired(MapCore mapCore, int[] paramArrayOfInt, int paramInt1, int paramInt2) {
        if (this.textTextureGenerator == null) {
            this.textTextureGenerator = new TextTextureGenerator();
        }
        return this.textTextureGenerator.getCharsWidths(paramArrayOfInt);
    }

    public void OnMapLabelsRequired(MapCore mapCore, int[] paramArrayOfInt, int paramInt) {
        if ((paramArrayOfInt == null) || (paramInt <= 0)) {
            return;
        }
        int[] arrayOfInt = paramArrayOfInt;
        for (int i = 0; i < paramInt; i++) {
            int j = arrayOfInt[i];
            this.textTextureGenerator = new TextTextureGenerator();

            byte[] arrayOfByte = this.textTextureGenerator.getTextPixelBuffer(j);
            if (arrayOfByte != null) {
                mapCore.putCharbitmap(j, arrayOfByte);
            }
            arrayOfByte = null;
        }
    }
//
//  public synchronized void newMap(MapCore paramMapCore)
//  {
//    this.connectionManager = new ConnectionManager(paramMapCore);
//    this.tileProcessCtrl = new d();
//    this.connectionManager.start();
//  }

    public synchronized void destoryMap(MapCore var1) {
        if (this.connectionManager != null) {
            this.connectionManager.threadFlag = false;
            if (this.connectionManager.isAlive()) {
                try {
                    this.connectionManager.interrupt();
                } catch (Throwable var6) {
                    ;
                } finally {
                    this.connectionManager.shutDown();
                    this.connectionManager = null;
                }
            }
        }

        if (this.tileProcessCtrl != null) {
            this.tileProcessCtrl.clearGrids();
        }

    }
}
