package com.unistrong.api.services.poisearch;


import android.os.Parcel;
import android.os.Parcelable.Creator;

class ComplexSearchResultCreator implements Creator<ComplexSearchResult> {
    @Override
    public ComplexSearchResult createFromParcel(Parcel in) {
        return new ComplexSearchResult(in);
    }

    @Override
    public ComplexSearchResult[] newArray(int size) {
        return new ComplexSearchResult[size];
    }
}
