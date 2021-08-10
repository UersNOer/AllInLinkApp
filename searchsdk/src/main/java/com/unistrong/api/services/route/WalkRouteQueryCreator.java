package com.unistrong.api.services.route;

import android.os.Parcel;
import android.os.Parcelable.Creator;

import com.unistrong.api.services.route.RouteSearch.WalkRouteQuery;

class WalkRouteQueryCreator<T> implements Creator<WalkRouteQuery> {

	@Override
	public WalkRouteQuery createFromParcel(Parcel source) {
		return new WalkRouteQuery(source);
	}

	@Override
	public WalkRouteQuery[] newArray(int size) {
		return new WalkRouteQuery[size];
	}

}
