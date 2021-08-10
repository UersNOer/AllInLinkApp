package com.unistrong.api.services.route;

import android.os.Parcel;
import android.os.Parcelable.Creator;

class WalkStepCreator implements Creator<WalkStep> {

	@Override
	public WalkStep createFromParcel(Parcel source) {
		return new WalkStep(source);
	}

	@Override
	public WalkStep[] newArray(int size) {
		return new WalkStep[size];
	}

}
