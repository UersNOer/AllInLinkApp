package com.unistrong.api.services.route;

import android.os.Parcel;
import android.os.Parcelable.Creator;

class PathCreator<T> implements Creator<Path> {

	@Override
	public Path createFromParcel(Parcel source) {
		return new Path(source);
	}

	@Override
	public Path[] newArray(int size) {
		return new Path[size];
	}
}
