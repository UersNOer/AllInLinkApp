package com.unistrong.api.services.poisearch;

import android.os.Parcel;
import android.os.Parcelable.Creator;

class PoiItemCreator<T> implements Creator<PoiItem> {

	@Override
	public PoiItem createFromParcel(Parcel source) {
		return new PoiItem(source);
	}

	@Override
	public PoiItem[] newArray(int size) {
		return new PoiItem[size];
	}

}
