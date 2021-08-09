package com.unistrong.api.services.core;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 几何点对象类。 该类为不可变类，表示一对经、纬度值，以双精度形式存储。
 * 
 */
public class LatLonPoint implements Parcelable {
	private double latitude;
	private double longitude;
	public static final Parcelable.Creator<LatLonPoint> CREATOR = new LatLonPointCreator();

	/**
	 * 构造一个LatLonPoint。
	 */
    public LatLonPoint(){}
	/**
	 * 用给定的经度和纬度构造一个LatLonPoint。
	 * 
	 * @param latitude
	 *            该点的纬度。
	 * @param longitude
	 *            该点的经度。
	 */
	public LatLonPoint(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	/**
	 * 获取该点经度。
	 * 
	 * @return 该点经度。
	 */
	public double getLongitude() {
		return this.longitude;
	}

	/**
	 * 设置该点经度。
	 * 
	 * @param lon
	 *            获取该点经度。
	 */
	public void setLongitude(double lon) {
		this.longitude = lon;
	}

	public double getLatitude() {
		return this.latitude;
	}

	/**
	 * 设置该点纬度。
	 * 
	 * @param lat
	 *            该点纬度。
	 */
	public void setLatitude(double lat) {
		this.latitude = lat;
	}

	public LatLonPoint copy() {
		return new LatLonPoint(this.latitude, this.longitude);
	}

	public int hashCode() {
		int i = 31;
		int j = 1;

		long l = Double.doubleToLongBits(this.latitude);
		j = i * j + (int) (l ^ l >>> 32);
		l = Double.doubleToLongBits(this.longitude);
		j = i * j + (int) (l ^ l >>> 32);
		return j;
	}

	public boolean equals(Object paramObject) {
		if (this == paramObject)
			return true;
		if (paramObject == null)
			return false;
		if (getClass() != paramObject.getClass())
			return false;
		LatLonPoint localLatLonPoint = (LatLonPoint) paramObject;
		if (Double.doubleToLongBits(this.latitude) != Double
				.doubleToLongBits(localLatLonPoint.latitude))
			return false;
		if (Double.doubleToLongBits(this.longitude) != Double
				.doubleToLongBits(localLatLonPoint.longitude))
			return false;
		return true;
	}

	public String toString() {
		return "" + this.longitude + "," + this.latitude;
	}

	protected LatLonPoint(Parcel paramParcel) {
		this.latitude = paramParcel.readDouble();
		this.longitude = paramParcel.readDouble();
	}

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel paramParcel, int paramInt) {
		paramParcel.writeDouble(this.latitude);
		paramParcel.writeDouble(this.longitude);
	}
}