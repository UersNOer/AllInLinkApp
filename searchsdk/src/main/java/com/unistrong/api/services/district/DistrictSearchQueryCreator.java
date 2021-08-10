package com.unistrong.api.services.district;

import android.os.Parcel;
import android.os.Parcelable;

class DistrictSearchQueryCreator implements
		Parcelable.Creator<DistrictSearchQuery> {

	@Override
	public DistrictSearchQuery createFromParcel(Parcel paramParcel) {
		return new DistrictSearchQuery(paramParcel);
	}

	@Override
	public DistrictSearchQuery[] newArray(int paramInt) {
		return new DistrictSearchQuery[paramInt];
	}
}
