package com.unistrong.api.services.cloud;


import android.os.Parcel;
import android.os.Parcelable.Creator;

class CloudItemCreator implements Creator<CloudItem>{
    @Override
    public CloudItem createFromParcel(Parcel source) {
        return new CloudItem(source);
    }

    @Override
    public CloudItem[] newArray(int size) {
        return new CloudItem[size];
    }
}
