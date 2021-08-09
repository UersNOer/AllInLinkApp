package com.unistrong.api.services.route;

import android.os.Parcel;
import android.os.Parcelable.Creator;

class WalkPathCreator implements Creator<WalkPath> {

	@Override
	public WalkPath createFromParcel(Parcel source) {
		return new WalkPath(source);
	}

	@Override
	public WalkPath[] newArray(int size) {
		return new WalkPath[size];
	}

}
