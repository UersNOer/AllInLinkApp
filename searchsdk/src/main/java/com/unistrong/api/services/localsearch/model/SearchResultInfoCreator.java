package com.unistrong.api.services.localsearch.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wcb on 16/9/2.
 */
 class SearchResultInfoCreator implements Parcelable.Creator<SearchResultInfo> {

    @Override
    public SearchResultInfo createFromParcel(Parcel source) {
        return new SearchResultInfo(source);
    }

    @Override
    public SearchResultInfo[] newArray(int size) {
        return new SearchResultInfo[size];
    }

}
