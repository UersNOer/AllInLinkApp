package com.unistrong.api.services.route;

import android.os.Parcel;
import android.os.Parcelable.Creator;

class DoorwayCreator implements Creator<Doorway> {

	@Override
	public Doorway createFromParcel(Parcel source) {
		return new Doorway(source);
	}

	@Override
	public Doorway[] newArray(int size) {
		return new Doorway[size];
	}

}
