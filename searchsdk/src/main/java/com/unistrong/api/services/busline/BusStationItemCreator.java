package com.unistrong.api.services.busline;

import android.os.Parcel;
import android.os.Parcelable;

class BusStationItemCreator implements Parcelable.Creator<BusStationItem> {

	@Override
	public BusStationItem createFromParcel(Parcel paramParcel) {
		return new BusStationItem(paramParcel);
	}

	@Override
	public BusStationItem[] newArray(int paramInt) {
		return new BusStationItem[paramInt];
	}
}
