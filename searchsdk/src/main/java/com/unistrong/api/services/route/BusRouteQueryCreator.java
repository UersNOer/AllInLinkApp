package com.unistrong.api.services.route;

import android.os.Parcel;
import android.os.Parcelable.Creator;

import com.unistrong.api.services.route.RouteSearch.BusRouteQuery;

class BusRouteQueryCreator<T> implements Creator<BusRouteQuery> {

	@Override
	public BusRouteQuery createFromParcel(Parcel source) {

        return new BusRouteQuery(source);
	}

	@Override
	public BusRouteQuery[] newArray(int size) {

        return new BusRouteQuery[size];
	}

}
