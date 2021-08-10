package com.unistrong.api.maps.model;


import android.os.Parcel;
import android.os.Parcelable;

public class GeoJsonLayerOptionsCreator implements Parcelable.Creator<GeoJsonLayerOptions>{

    @Override
    public GeoJsonLayerOptions createFromParcel(Parcel source) {
        return new GeoJsonLayerOptions(source);
    }

    @Override
    public GeoJsonLayerOptions[] newArray(int size) {
        return new GeoJsonLayerOptions[size];
    }
}
