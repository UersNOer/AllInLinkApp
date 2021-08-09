package com.unistrong.api.services.cloud;


import android.os.Parcel;
import android.os.Parcelable;

class CloudDatasetItemCreator implements Parcelable.Creator<CloudDatasetItem>{
    @Override
    public CloudDatasetItem createFromParcel(Parcel in) {
        return new CloudDatasetItem(in);
    }

    @Override
    public CloudDatasetItem[] newArray(int size) {
        return new CloudDatasetItem[size];
    }
}
