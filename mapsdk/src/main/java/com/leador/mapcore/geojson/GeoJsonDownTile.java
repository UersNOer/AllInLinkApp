package com.leador.mapcore.geojson;

import com.unistrong.api.maps.model.LatLng;


public class GeoJsonDownTile {
    private long dataSetId;
    private long id_tile;
    private LatLng[] coords;
    private boolean isNeedDown;

    public GeoJsonDownTile(long id,LatLng[] latLngs,long dataSetId){
        this.id_tile=id;
        this.coords=latLngs;
        this.dataSetId=dataSetId;
    }

    public long getDataSetId() {
        return dataSetId;
    }


    public long getId_tile() {
        return id_tile;
    }


    public LatLng[] getCoords() {
        return coords;
    }

    public boolean isNeedDown() {
        return isNeedDown;
    }

    public void setNeedDown(boolean needDown) {
        isNeedDown = needDown;
    }
}
