package com.unistrong.api.services.route;

import android.os.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * 定义了公交路径规划的一个路段。
 */
public class BusStep implements android.os.Parcelable {

	/**
	 * 路段类型。
	 */
	private String type;

	/**
	 * 路段的公交导航信息。
	 */
	private List<RouteBusLineItem> mBusLines = new ArrayList<RouteBusLineItem>();
	/**
	 * 路段的入口信息。
	 */
	private Doorway mEntrance;
	/**
	 * 路段的出口信息。
	 */
	private Doorway mExit;
	/**
	 * 路段的步行导航信息。
	 */
	private RouteBusWalkItem mWalk;

	public static final Creator<BusStep> CREATOR = new BusStepCreator();

	/**
	 * BusStep 构造方法
	 */
	BusStep() {
		super();
	}

	/**
	 * 序列化实现
	 * 
	 * @param source
	 */
	public BusStep(Parcel source) {

		this.mEntrance = ((Doorway) source.readParcelable(Doorway.class.getClassLoader()));
		this.mExit = ((Doorway) source.readParcelable(Doorway.class.getClassLoader()));
		this.mWalk = ((RouteBusWalkItem) source.readParcelable(RouteBusWalkItem.class.getClassLoader()));
		this.mBusLines = source.createTypedArrayList(RouteBusLineItem.CREATOR);
		this.type = source.readString();

	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(this.mEntrance,flags);
		dest.writeParcelable(this.mExit,flags);
		dest.writeParcelable(this.mWalk,flags);
		dest.writeTypedList(mBusLines);
		dest.writeString(this.type);
	}

	/**
	 * 设置此路段的步行信息。
	 * 
	 * @param mWalk
	 *            此路段的步行信息。
	 */
	public void setWalk(RouteBusWalkItem mWalk) {
		this.mWalk = mWalk;

	}

	/**
	 * 返回此路段的步行信息。
	 * 
	 * @return 此路段的步行信息。
	 */
	public RouteBusWalkItem getWalk() {
		return mWalk;

	}

	/**
	 * 返回此路段的入口信息。 入口信息指换乘地铁时的进站口。
	 * 
	 * @return 此路段的入口信息。
	 */
	private Doorway getEntrance() {
		return mEntrance;

	}

	/**
	 * 设置此路段的入口信息。 入口信息指换乘地铁时的进站口。
	 * 
	 * @param mEntrance
	 *            此路段的入口信息。
	 */
    private void setEntrance(Doorway mEntrance) {
		this.mEntrance = mEntrance;
	}

	/**
	 * 返回此路段的出口信息。 出口信息指乘地铁之后的出站口信息。
	 * 
	 * @return 此路段的出口信息。
	 */
    private Doorway getExit() {
		return mEntrance;

	}

	/**
	 * 设置此路段的出口信息。
	 * 
	 * @param mExit
	 *            此路段的出口信息。
	 */
    private void setExit(Doorway mExit) {
		this.mExit = mExit;
	}

	/**
	 * 返回此路段的公交导航信息。
	 * 
	 * @return 此路段的公交导航信息。
	 */
	public java.util.List<RouteBusLineItem> getBusLines() {
		return mBusLines;
	}

	/**
	 * 设置此路段的公交导航信息。
	 * 
	 * @param mBusLines
	 *            此路段的公交导航信息。
	 */
	public void setBusLines(java.util.List<RouteBusLineItem> mBusLines) {
		this.mBusLines = mBusLines;

	}

	/**
	 * 返回路段类型。
	 * 
	 * @return 路段类型。
	 */
	private String getType() {
		return type;
	}

	/***
	 * 设置路段类型 。
	 * 
	 * @param type
	 *            路段类型。
	 */
    private void setType(String type) {
		this.type = type;
	}

}
