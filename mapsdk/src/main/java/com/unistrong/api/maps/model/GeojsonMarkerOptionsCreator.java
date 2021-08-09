package com.unistrong.api.maps.model;


import android.os.Parcel;
import android.os.Parcelable;

public class GeojsonMarkerOptionsCreator implements Parcelable.Creator<GeojsonMarkerOptions>{

    @Override
    public GeojsonMarkerOptions createFromParcel(Parcel paramParcel) {

        GeojsonMarkerOptions localMarkerOptions = new GeojsonMarkerOptions();
//        LatLng localLatLng = (LatLng)paramParcel.readParcelable(LatLng.class.getClassLoader());
//        localMarkerOptions.position(localLatLng);
        BitmapDescriptor localBitmapDescriptor = (BitmapDescriptor)paramParcel.readParcelable(BitmapDescriptor.class
                .getClassLoader());
        localMarkerOptions.icon(localBitmapDescriptor);
//        localMarkerOptions.title(paramParcel.readString());
//        localMarkerOptions.snippet(paramParcel.readString());
        localMarkerOptions.anchor(paramParcel.readFloat(), paramParcel.readFloat());
//        localMarkerOptions.setInfoWindowOffset(paramParcel.readInt(), paramParcel.readInt());
        boolean[] arrayOfBoolean = new boolean[1];
        paramParcel.readBooleanArray(arrayOfBoolean);
//        localMarkerOptions.visible(arrayOfBoolean[0]);
//        localMarkerOptions.draggable(arrayOfBoolean[1]);
//        localMarkerOptions.setGps(arrayOfBoolean[2]);
        localMarkerOptions.setFlat(arrayOfBoolean[0]);
//        localMarkerOptions.id = paramParcel.readString();
//        localMarkerOptions.period(paramParcel.readInt());
//        ArrayList localArrayList = paramParcel.readArrayList(BitmapDescriptor.class
//                .getClassLoader());
//        localMarkerOptions.icons(localArrayList);
        localMarkerOptions.zIndex(paramParcel.readFloat());
        return localMarkerOptions;
    }

    @Override
    public GeojsonMarkerOptions[] newArray(int size) {
        return new GeojsonMarkerOptions[size];
    }
}
