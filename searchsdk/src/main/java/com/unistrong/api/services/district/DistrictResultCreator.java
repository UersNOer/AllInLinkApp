package com.unistrong.api.services.district;

import android.os.Parcel;
import android.os.Parcelable;

class DistrictResultCreator implements Parcelable.Creator<DistrictResult> {

	@Override
	public DistrictResult createFromParcel(Parcel paramParcel) {
		return new DistrictResult(paramParcel);
	}

	@Override
	public DistrictResult[] newArray(int paramInt) {
		return new DistrictResult[paramInt];
	}
}
