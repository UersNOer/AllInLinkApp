package com.unistrong.api.services.route;

import android.os.Parcel;
import android.os.Parcelable.Creator;

import com.unistrong.api.services.route.RouteSearch.FromAndTo;

class FromAndToCreator<T> implements Creator<FromAndTo> {

	@Override
	public FromAndTo createFromParcel(Parcel source) {
		return new FromAndTo(source);
	}

	@Override
	public FromAndTo[] newArray(int size) {
		return new FromAndTo[size];
	}

}
