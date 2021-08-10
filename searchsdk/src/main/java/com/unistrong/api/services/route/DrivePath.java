package com.unistrong.api.services.route;

import android.os.Parcel;

import java.util.ArrayList;
import java.util.List;

public class DrivePath extends Path implements android.os.Parcelable {
	private List<DriveStep> steps = new ArrayList<DriveStep>();
	private float toll;// 过路费
	public static final Creator<DrivePath> CREATOR = new DrivePathCreator();

	/**
	 * DrivePath的构造方法。
	 */
	public DrivePath() {
		super();
	}

	public DrivePath(android.os.Parcel source) {
		super(source);
		this.steps = source.createTypedArrayList(DriveStep.CREATOR);
        this.toll = source.readFloat();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeTypedList(this.steps);
        dest.writeFloat(this.toll);
	}

	/**
	 * 返回驾车规划方案的路段列表。
	 * 
	 * @return
	 */
	public List<DriveStep> getSteps() {
		return this.steps;
	}

	/**
	 * 设置驾车规划方案的路段列表。
	 * 
	 * @param mSteps
	 *            驾车规划方案的路段列表。
	 */
	public void setSteps(List<DriveStep> mSteps) {
		this.steps = mSteps;
	}

	/**
	 * 返回此方案中的收费道路的总费用，单位元。
	 * 
	 * @return 此方案中收费道路的总费用
	 */
	public float getTolls() {
		return this.toll;
	}

	/**
	 * 设置此方案中的收费道路的总费用，单位元。
	 * 
	 * @param toll
	 *            此方案中收费道路的总费用
	 */
	public void setTolls(float toll) {
		this.toll = toll;
	}

}
