package com.unistrong.api.services.route;

import android.os.Parcel;
import android.os.Parcelable.Creator;

class StepCreator implements Creator<Step> {

	@Override
	public Step createFromParcel(Parcel arg0) {
		return new Step(arg0);
	}

	@Override
	public Step[] newArray(int arg0) {
		return new Step[arg0];
	}

}
