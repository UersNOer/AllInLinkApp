package com.leador.mapcore.geojson;


import android.util.LongSparseArray;

import java.util.List;

public class CacheManager {

    private LongSparseArray<List> cacheMap;
    private List <GeoJsonDownTile> screenTileList;
    private  int CACHE_MAX_SIZE =20;
//    private static CacheManager manager;

    public CacheManager(int MaxSize){
        this.CACHE_MAX_SIZE=MaxSize;
        cacheMap=new LongSparseArray<>();
    }

//     public  synchronized  static CacheManager getInstance(){
//        if(manager==null)
//            manager=new CacheManager();
//        return manager;
//     }

    public  synchronized List<GeoJsonItem>  getGeojsonDataForTile(long tile_id){
        return cacheMap.get(tile_id);
    }

    public  synchronized void saveGeoJsonData(long tile_id,List<GeoJsonItem> datas){

        if(datas!=null){
            if(cacheMap.size()>= CACHE_MAX_SIZE &&cacheMap.get(tile_id)==null) {
                cacheMap.remove(cacheMap.keyAt(getNoScreenTileIndex()));
            }
                cacheMap.put(tile_id,datas);
        }
    }
    public synchronized void setScreenTileList(List<GeoJsonDownTile> list){
        if(screenTileList==null || screenTileList.size()==0)return;
            screenTileList=list;
    }

    private int getNoScreenTileIndex(){
            int index=0;
            boolean flag=true;
            while (flag){
                flag=false;
                if(screenTileList!=null)
            for(GeoJsonDownTile str:screenTileList){
                if(str.getId_tile()==cacheMap.keyAt(index)){
                    index++;
                    flag=true;
                    break;
                }
            }
            }
            return index;
    }
    public int getCACHE_MAX_SIZE() {
        return CACHE_MAX_SIZE;
    }

    public void setCACHE_MAX_SIZE(int CACHE_MAX_SIZE) {
        if(CACHE_MAX_SIZE<=0)return;
        this.CACHE_MAX_SIZE = CACHE_MAX_SIZE;
    }
//    public synchronized static void release(){
//        if(manager!=null)manager=null;
//    }
    public synchronized void clearCache(){
        if (cacheMap != null){
            cacheMap.clear();
        }
        if (screenTileList != null && !screenTileList.isEmpty()){
            screenTileList.clear();
        }
    }
}
