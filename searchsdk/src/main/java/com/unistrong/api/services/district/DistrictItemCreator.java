package com.unistrong.api.services.district;

import android.os.Parcel;
import android.os.Parcelable;

class DistrictItemCreator implements Parcelable.Creator<DistrictItem> {

	@Override
	public DistrictItem createFromParcel(Parcel paramParcel) {
		return new DistrictItem(paramParcel);
	}

	@Override
	public DistrictItem[] newArray(int paramInt) {
		return new DistrictItem[paramInt];
	}
}
