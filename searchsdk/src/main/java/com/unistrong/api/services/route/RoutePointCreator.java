package com.unistrong.api.services.route;

import android.os.Parcel;
import android.os.Parcelable.Creator;

class RoutePointCreator implements Creator<RoutePoint> {

	@Override
	public RoutePoint createFromParcel(Parcel source) {
		return new RoutePoint(source);
	}

	@Override
	public RoutePoint[] newArray(int size) {
		return new RoutePoint[size];
	}

}
