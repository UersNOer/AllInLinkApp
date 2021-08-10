package com.unistrong.api.maps.model;


import android.os.Parcel;
import android.os.Parcelable;

public class BoundingBoxCreator implements Parcelable.Creator<BoundingBox>{
    @Override
    public BoundingBox createFromParcel(Parcel in) {
        return new BoundingBox(in);
    }

    @Override
    public BoundingBox[] newArray(int size) {
        return new BoundingBox[size];
    }
}
