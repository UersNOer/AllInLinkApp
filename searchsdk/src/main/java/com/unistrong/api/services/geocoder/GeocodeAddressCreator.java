package com.unistrong.api.services.geocoder;

import android.os.Parcel;
import android.os.Parcelable;

class GeocodeAddressCreator implements Parcelable.Creator<GeocodeAddress> {

	@Override
	public GeocodeAddress createFromParcel(Parcel paramParcel) {
		return new GeocodeAddress(paramParcel);
	}

	@Override
	public GeocodeAddress[] newArray(int arg0) {
		return new GeocodeAddress[arg0];
	}

}
