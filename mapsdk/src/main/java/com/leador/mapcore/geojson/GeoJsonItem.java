package com.leador.mapcore.geojson;

import com.unistrong.api.maps.model.LatLng;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class GeoJsonItem implements Cloneable{
    private int type;//1.点；2.线；3.面
    private LatLng[] coords;//坐标
    private Map<String,String> entend;//扩展字段
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public LatLng[] getCoords() {
        return coords;
    }

    public void setCoords(LatLng[] coords) {
        this.coords = coords;
    }

    public Map<String, String> getEntend() {
        return entend;
    }

    public void setEntend(Map<String, String> entend) {
        this.entend = entend;
    }

    public GeoJsonItem clone() {
        GeoJsonItem item = null;
        try {
            item = (GeoJsonItem) super.clone();
            item.setType(this.type);
            item.setId(this.id);
            if (this.getCoords() != null){
                int len = this.coords.length;
                LatLng[] coords_ = new LatLng[len];
                for (int i=0;i<len;i++){
                    coords_[i] = this.coords[i].clone();
                }
                item.setCoords(coords_);
            }
            if (this.getEntend() != null){
                Map<String, String> map = new HashMap<String, String>();
                Iterator<Map.Entry<String, String>> entries = this.getEntend().entrySet().iterator();
                while (entries.hasNext()) {
                    Map.Entry<String, String> entry = entries.next();
                    String key = (String) entry.getKey();
                    String value = (String) entry.getValue();
                    map.put(key, value);
                }
                item.setEntend(map);
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return item;
    }
}
