package com.unistrong.api.services.route;

import android.os.Parcel;

import com.unistrong.api.services.core.LatLonPoint;

/**
 * 路径规划的一个方案
 */
public class Path implements android.os.Parcelable {
	private float distance; // 总路线距离
	private long duration;// 总路线耗时
	private LatLonPoint originLocation;// 起点坐标
	private LatLonPoint destinationLocation;// 终点坐标
	public static final Creator<Path> CREATOR = new PathCreator();

	/**
	 * Path的构造方法。
	 */
	public Path() {
		super();
	}

	/**
	 * 序列化实现。
	 */
	public Path(Parcel source) {
		this.originLocation = ((LatLonPoint) source
				.readParcelable(LatLonPoint.class.getClassLoader()));
		this.destinationLocation = ((LatLonPoint) source
				.readParcelable(LatLonPoint.class.getClassLoader()));
		this.distance = source.readFloat();
		this.duration = source.readLong();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(this.originLocation, flags);
		dest.writeParcelable(this.destinationLocation, flags);
		dest.writeFloat(this.distance);
		dest.writeLong(this.duration);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	/**
	 * 返回此规划方案的距离，单位米。
	 * 
	 * @return 规划方案的距离，单位米。
	 */
	public float getDistance() {
		return this.distance;
	}

	/**
	 * 返回方案的预计消耗时间，单位秒
	 * 
	 * @return 方案的预计消耗时间，单位秒。
	 */
	public long getDuration() {
		return this.duration;
	}

	/**
	 * 设置路径规划方案的大约距离，单位米。
	 * 
	 * @param mDistance
	 *            路径距离，单位米。
	 */
	public void setDistance(float mDistance) {
		this.distance = mDistance;
	}

	/**
	 * 设置方案的预计消耗时间，单位秒。
	 * 
	 * @param mDuration
	 *            方案消耗时间，单位秒。
	 */
	public void setDuration(long mDuration) {
		this.duration = mDuration;
	}

	/**
	 * 返回起点坐标
	 * 
	 * @return 起点坐标
	 */
	public LatLonPoint getOriginLocation() {
		return this.originLocation;
	}

	/**
	 * 设置起点坐标
	 * 
	 * @param originLocation
	 *            起点坐标
	 */
	public void setOriginLocation(LatLonPoint originLocation) {
		this.originLocation = originLocation;
	}

	/**
	 * 返回终点坐标
	 * 
	 * @return 终点坐标
	 */
	public LatLonPoint getDestinationLocation() {
		return this.destinationLocation;
	}

	/**
	 * 设置终点坐标
	 * 
	 * @param destinationLocation
	 *            终点坐标
	 */
	public void setDestinationLocation(LatLonPoint destinationLocation) {
		this.destinationLocation = destinationLocation;
	}
}
