package com.unistrong.api.services.route;

import android.os.Parcel;
import android.os.Parcelable.Creator;

class BusRouteResultCreator implements Creator<BusRouteResult> {

	@Override
	public BusRouteResult createFromParcel(Parcel source) {
		return new BusRouteResult(source);
	}

	@Override
	public BusRouteResult[] newArray(int size) {
		return new BusRouteResult[size];
	}

}
