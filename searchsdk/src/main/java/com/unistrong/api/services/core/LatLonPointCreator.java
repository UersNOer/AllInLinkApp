package com.unistrong.api.services.core;

import android.os.Parcel;
import android.os.Parcelable;

 class LatLonPointCreator implements Parcelable.Creator<LatLonPoint> {

	@Override
	public LatLonPoint createFromParcel(Parcel paramParcel) {
		return new LatLonPoint(paramParcel);
	}

	@Override
	public LatLonPoint[] newArray(int paramInt) {
		return new LatLonPoint[paramInt];
	}
}
