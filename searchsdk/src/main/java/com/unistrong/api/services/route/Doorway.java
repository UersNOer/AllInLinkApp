package com.unistrong.api.services.route;

import android.os.Parcel;

import com.unistrong.api.services.core.LatLonPoint;

/**
 * 定义了公交换乘路径规划的一个换乘点的出入口信息
 */
public class Doorway implements android.os.Parcelable {

	/**
	 * 出（入）口名称。
	 */
	private String name;

	/**
	 * 公交换乘中换乘点的出（入）口坐标。
	 */
	private LatLonPoint mLatLonPoint;

	public static final Creator<Doorway> CREATOR = new DoorwayCreator();

	public Doorway() {
		super();
	}

	public Doorway(Parcel source) {
		this.name = source.readString();
		this.mLatLonPoint = ((LatLonPoint) source.readParcelable(LatLonPoint.class.getClassLoader()));
	}

	/**
	 * 返回公交换乘中换乘点的出（入）口名称 。
	 * 
	 * @return 公交换乘中换乘点的出（入）口名称。
	 */
	public java.lang.String getName() {
		return this.name;

	}

	/**
	 * 设置公交换乘中换乘点的出（入）口名称。
	 * 
	 * @param mName
	 *            公交换乘中换乘点的出（入）口名称。
	 */
	public void setName(java.lang.String mName) {
		this.name = mName;
	}

	/**
	 * 返回公交换乘中换乘点的出（入）口名称。
	 * 
	 * @return 公交换乘中换乘点的出（入）口名称。
	 */
	public LatLonPoint getLatLonPoint() {
		return this.mLatLonPoint;

	}

	/**
	 * 设置公交换乘中换乘点的出（入）口坐标。
	 * 
	 * @param mLatLonPoint
	 *            公交换乘中换乘点的出（入）口坐标。
	 */
	public void setLatLonPoint(LatLonPoint mLatLonPoint) {
		this.mLatLonPoint = mLatLonPoint;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.name);
		dest.writeParcelable(this.mLatLonPoint,flags);
	}

}
