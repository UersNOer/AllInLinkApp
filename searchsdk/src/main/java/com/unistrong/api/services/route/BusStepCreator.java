package com.unistrong.api.services.route;

import android.os.Parcel;
import android.os.Parcelable.Creator;

class BusStepCreator implements Creator<BusStep> {

	@Override
	public BusStep createFromParcel(Parcel source) {
		return new BusStep(source);
	}

	@Override
	public BusStep[] newArray(int size) {
		return new BusStep[size];
	}

}
