package com.unistrong.api.services.route;

import android.os.Parcel;
import android.os.Parcelable.Creator;

class RouteBusLineItemCreator implements Creator<RouteBusLineItem> {

	@Override
	public RouteBusLineItem createFromParcel(Parcel source) {
		return new RouteBusLineItem(source);
	}

	@Override
	public RouteBusLineItem[] newArray(int size) {
		return new RouteBusLineItem[size];
	}

}
