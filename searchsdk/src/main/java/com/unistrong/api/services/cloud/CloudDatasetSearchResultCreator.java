package com.unistrong.api.services.cloud;


import android.os.Parcel;
import android.os.Parcelable;

class CloudDatasetSearchResultCreator implements Parcelable.Creator<CloudDatasetSearchResult> {
    @Override
    public CloudDatasetSearchResult createFromParcel(Parcel in) {
        return new CloudDatasetSearchResult(in);
    }

    @Override
    public CloudDatasetSearchResult[] newArray(int size) {
        return new CloudDatasetSearchResult[size];
    }
}
