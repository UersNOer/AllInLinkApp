package com.unistrong.api.services.route;

import android.os.Parcel;
import android.os.Parcelable.Creator;

import com.unistrong.api.services.route.RouteSearch.DriveRouteQuery;

class DriveRouteQueryCreator<T> implements Creator<DriveRouteQuery> {

	@Override
	public DriveRouteQuery createFromParcel(Parcel source) {
		return new DriveRouteQuery(source);
	}

	@Override
	public DriveRouteQuery[] newArray(int size) {
		return new DriveRouteQuery[size];
	}

}
