package com.unistrong.api.services.route;

import android.os.Parcel;
import android.os.Parcelable.Creator;

class DriveRouteResultCreator implements Creator<DriveRouteResult> {

	@Override
	public DriveRouteResult createFromParcel(Parcel source) {
		return new DriveRouteResult(source);
	}

	@Override
	public DriveRouteResult[] newArray(int size) {
		return new DriveRouteResult[size];
	}

}
