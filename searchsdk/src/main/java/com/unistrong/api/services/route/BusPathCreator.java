package com.unistrong.api.services.route;

import android.os.Parcel;
import android.os.Parcelable.Creator;

class BusPathCreator implements Creator<BusPath> {

	@Override
	public BusPath createFromParcel(Parcel source) {
		return new BusPath(source);
	}

	@Override
	public BusPath[] newArray(int size) {
		return new BusPath[size];
	}

}
