package com.unistrong.api.services.route;

import android.os.Parcel;
import android.os.Parcelable.Creator;

class RouteBusWalkItemCreator implements Creator<RouteBusWalkItem> {

	@Override
	public RouteBusWalkItem createFromParcel(Parcel source) {
		return new RouteBusWalkItem(source);
	}

	@Override
	public RouteBusWalkItem[] newArray(int size) {
		return new RouteBusWalkItem[size];
	}

}
