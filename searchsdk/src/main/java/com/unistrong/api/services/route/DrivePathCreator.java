package com.unistrong.api.services.route;

import android.os.Parcel;
import android.os.Parcelable.Creator;

class DrivePathCreator implements Creator<DrivePath> {

	@Override
	public DrivePath createFromParcel(Parcel source) {
		return new DrivePath(source);
	}

	@Override
	public DrivePath[] newArray(int size) {
		return new DrivePath[size];
	}

}
