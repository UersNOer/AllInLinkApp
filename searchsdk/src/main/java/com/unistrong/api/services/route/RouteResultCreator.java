package com.unistrong.api.services.route;

import android.os.Parcel;
import android.os.Parcelable.Creator;

class RouteResultCreator implements Creator<RouteResult> {

	@Override
	public RouteResult createFromParcel(Parcel source) {
		return new RouteResult(source);
	}

	@Override
	public RouteResult[] newArray(int size) {
		return new RouteResult[size];
	}

}
