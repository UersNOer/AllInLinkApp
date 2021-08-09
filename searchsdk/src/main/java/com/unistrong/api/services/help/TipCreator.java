package com.unistrong.api.services.help;

import android.os.Parcel;
import android.os.Parcelable;

class TipCreator implements Parcelable.Creator<Tip> {

	@Override
	public Tip createFromParcel(Parcel paramParcel) {
		return new Tip(paramParcel);
	}

	@Override
	public Tip[] newArray(int paramInt) {
		return new Tip[paramInt];
	}
}
