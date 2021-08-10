package com.unistrong.api.services.busline;

import android.os.Parcel;
import android.os.Parcelable;

class BusLineItemCreator implements Parcelable.Creator<BusLineItem> {

	@Override
	public BusLineItem createFromParcel(Parcel paramParcel) {
		return new BusLineItem(paramParcel);
	}

	@Override
	public BusLineItem[] newArray(int paramInt) {
		return new BusLineItem[paramInt];
	}
}
