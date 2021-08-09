package com.allinlink.platformapp.video_project.bean;

import com.unistrong.api.maps.model.LatLng;

public class UpChannelBus {
    private int index;
    private LatLng latLng;

    public UpChannelBus(int index, LatLng latLng) {
        this.index = index;
        this.latLng = latLng;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }
}
