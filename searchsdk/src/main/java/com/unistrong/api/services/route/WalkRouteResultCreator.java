package com.unistrong.api.services.route;

import android.os.Parcel;
import android.os.Parcelable.Creator;

class WalkRouteResultCreator implements Creator<WalkRouteResult> {

	@Override
	public WalkRouteResult createFromParcel(Parcel source) {
		return new WalkRouteResult(source);
	}

	@Override
	public WalkRouteResult[] newArray(int size) {
		return new WalkRouteResult[size];
	}

}
