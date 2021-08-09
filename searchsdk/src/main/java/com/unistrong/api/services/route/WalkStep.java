package com.unistrong.api.services.route;

import android.os.Parcel;

/**
 * 定义了步行路径规划的一个路段。
 */
public class WalkStep extends Step implements android.os.Parcelable {
	public static final Creator<WalkStep> CREATOR = new WalkStepCreator();

	/**
	 * WalkStep的构造方法。
	 */
	WalkStep() {
		super();
	}

	/**
	 * 序列化实现。
	 */
	WalkStep(android.os.Parcel source) {
		super(source);
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
	}

}
