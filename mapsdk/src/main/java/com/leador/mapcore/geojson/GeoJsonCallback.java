package com.leador.mapcore.geojson;

import java.util.List;


public interface GeoJsonCallback {
    public void onCallback(long tileId,List<GeoJsonItem> itemList);
    public void onError(int errorCode,String message);
//    public String getGeoJsonSerUrl();
//    public String getGeoJsonSerPath();
//    public String getRequestParmas();

}
