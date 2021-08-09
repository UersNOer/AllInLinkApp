package com.unistrong.api.services.cloud;


import android.os.Parcel;
import android.os.Parcelable;

class CloudSearchResultCreator implements Parcelable.Creator<CloudSearchResult> {
    @Override
    public CloudSearchResult createFromParcel(Parcel source) {
        return new CloudSearchResult(source);
    }

    @Override
    public CloudSearchResult[] newArray(int size) {
        return new CloudSearchResult[size];
    }
}
