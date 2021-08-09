package com.unistrong.api.services.route;

import android.os.Parcel;
import android.os.Parcelable.Creator;

class DriveStepCreator implements Creator<DriveStep> {

	@Override
	public DriveStep createFromParcel(Parcel source) {
		return new DriveStep(source);
	}

	@Override
	public DriveStep[] newArray(int size) {
		return new DriveStep[size];
	}

}
