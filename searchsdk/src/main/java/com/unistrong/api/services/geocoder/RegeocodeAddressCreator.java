package com.unistrong.api.services.geocoder;

import android.os.Parcel;
import android.os.Parcelable;

class RegeocodeAddressCreator implements Parcelable.Creator<RegeocodeAddress> {

	@Override
	public RegeocodeAddress createFromParcel(Parcel arg0) {
		return new RegeocodeAddress(arg0);
	}

	@Override
	public RegeocodeAddress[] newArray(int arg0) {
		return new RegeocodeAddress[arg0];
	}

}
