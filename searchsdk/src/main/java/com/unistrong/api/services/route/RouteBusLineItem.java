package com.unistrong.api.services.route;

import android.os.Parcel;

import com.unistrong.api.services.busline.BusLineItem;
import com.unistrong.api.services.busline.BusStationItem;
import com.unistrong.api.services.core.LatLonPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * 定义了公交换乘路径规划的一个换乘段的公交信息
 */
public class RouteBusLineItem extends BusLineItem implements
		android.os.Parcelable {
	/**
	 * 公交出发站站点信息。
	 */
	private BusStationItem mDepartureBusStation;

	/**
	 * 公交到达站站点信息。
	 */
	private BusStationItem mArrivalBusStation;

	/**
	 * 返回此公交换乘路段经过的站点数目（除出发站、到达站）。
	 */
	private int mPassStationNum;
	/**
	 * 此公交换乘路段经过的站点名称。
	 */
	private List<BusStationItem> mPassStations = new ArrayList<BusStationItem>();

	/**
	 * 此公交换乘路段公交预计行驶时间。
	 */
	private float mDuration;
	/**
	 * 此公交换乘路段（出发站-到达站）的坐标点集合。
	 */
	private List<LatLonPoint> mPolyline = new ArrayList<LatLonPoint>();

	public static final Creator<RouteBusLineItem> CREATOR = new RouteBusLineItemCreator();

	public RouteBusLineItem() {
		super();
	}

	public RouteBusLineItem(Parcel source) {
		super(source);
		this.mDepartureBusStation = ((BusStationItem) source
				.readParcelable(BusStationItem.class.getClassLoader()));
		this.mArrivalBusStation = ((BusStationItem) source
				.readParcelable(BusStationItem.class.getClassLoader()));
		this.mPassStationNum = source.readInt();
		this.mPassStations = source
				.createTypedArrayList(BusStationItem.CREATOR);
		this.mPolyline = source.createTypedArrayList(LatLonPoint.CREATOR);
		this.mDuration = source.readFloat();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		super.writeToParcel(dest, flags);
		dest.writeParcelable(this.mDepartureBusStation,flags);
		dest.writeParcelable(this.mArrivalBusStation,flags);
		dest.writeInt(this.mPassStationNum);
		dest.writeTypedList(this.mPassStations);
		dest.writeTypedList(this.mPolyline);
		dest.writeFloat(this.mDuration);
	}

	/**
	 * 返回此公交换乘路段的出发站。此站有可能不是公交线路的始发车站。
	 * 
	 * @return 此公交换乘路段的出发站。
	 */
	public BusStationItem getDepartureBusStation() {
		return mDepartureBusStation;

	}

	/**
	 * 设置此公交换乘路段的出发站。
	 * 
	 * @param mDepartureBusStation
	 *            设置此公交换乘路段的出发站。
	 */
	public void setDepartureBusStation(BusStationItem mDepartureBusStation) {
		this.mDepartureBusStation = mDepartureBusStation;
	}

	/**
	 * 返回此公交换乘路段的到达站。此站有可能不是公交线路的终点站。
	 * 
	 * @return 此公交换乘路段的到达站。
	 */
	public BusStationItem getArrivalBusStation() {
		return mArrivalBusStation;

	}

	/**
	 * 设置此公交换乘路段的到达站。
	 * 
	 * @param mArrivalBusStation
	 *            此公交换乘路段的到达站。
	 */
	public void setArrivalBusStation(BusStationItem mArrivalBusStation) {
		this.mArrivalBusStation = mArrivalBusStation;

	}

	/**
	 * 返回此公交换乘路段（出发站-到达站）的坐标点集合。
	 * 
	 * @return 此公交换乘路段（出发站-到达站）的坐标点集合。
	 */
	public java.util.List<LatLonPoint> getPolyline() {
		return mPolyline;

	}

	/**
	 * 设置此公交换乘路段（出发站-到达站）的坐标点集合。
	 * 
	 * @param mPolyline
	 *            此公交换乘路段（出发站-到达站）的坐标点集合。
	 */
	public void setPolyline(java.util.List<LatLonPoint> mPolyline) {
		this.mPolyline = mPolyline;
	}

	/**
	 * 返回此公交换乘路段经过的站点数目（除出发站、到达站）。
	 * 
	 * @return 此公交换乘路段经过的站点数目（除出发站、到达站
	 */
	public int getPassStationNum() {
		return mPassStationNum;

	}

	/**
	 * 设置此公交换乘路段经过的站点数目（除出发站）。
	 * 
	 * @param mPassStationNum
	 *            此公交换乘路段经过的站点数目（除出发站）。
	 */
	public void setPassStationNum(int mPassStationNum) {
		this.mPassStationNum = mPassStationNum;

	}

	/**
	 * 返回此公交换乘路段经过的站点名称。
	 * 
	 * @return 此公交换乘路段经过的站点名称。
	 */
	private java.util.List<BusStationItem> getPassStations() {
		return mPassStations;

	}

	/**
	 * 设置此公交换乘路段经过的站点名称 。
	 * 
	 * @param mPassStations
	 *            此公交换乘路段经过的站点名称。
	 */
    private void setPassStations(java.util.List<BusStationItem> mPassStations) {
		this.mPassStations = mPassStations;

	}

	/**
	 * 返回此公交换乘路段公交预计行驶时间 。
	 * 
	 * @return 此公交换乘路段公交预计行驶时间 。
	 */
	public float getDuration() {
		return mDuration;
	}

	/**
	 * 设置此公交换乘路段公交预计行驶时间 。
	 * 
	 * @param mDuration
	 *            此公交换乘路段公交预计行驶时间 。
	 */
	public void setDuration(float mDuration) {
		this.mDuration = mDuration;

	}

	public int hashCode() {
		int i = 31;
		int j = 1;
		j = i
				* j
				+ (this.mDepartureBusStation == null ? 0
						: this.mDepartureBusStation.hashCode());
		j = i
				* j
				+ (this.mArrivalBusStation == null ? 0
						: this.mArrivalBusStation.hashCode());
		j = i * j + this.mPassStationNum;
		j = i
				* j
				+ (this.mPassStations == null ? 0 : this.mPassStations
						.hashCode());
		j = i * j + (this.mPolyline == null ? 0 : this.mPolyline.hashCode());
		return j;
	}

	/**
	 * 比较两个查询条件是否相同。
	 * 
	 * @param
	 *            - 查询条件。
	 * @return 查询条件是否相同。
	 */
	public boolean equals(Object paramObject) {
		if (this == paramObject)
			return true;
		if (!super.equals(paramObject))
			return false;
		if (getClass() != paramObject.getClass())
			return false;
		RouteBusLineItem localRouteBusLineItem = (RouteBusLineItem) paramObject;
		if (this.mArrivalBusStation == null) {
			if (localRouteBusLineItem.mArrivalBusStation != null)
				return false;
		} else if (!this.mArrivalBusStation
				.equals(localRouteBusLineItem.mArrivalBusStation))
			return false;
		if (this.mDepartureBusStation == null) {
			if (localRouteBusLineItem.mDepartureBusStation != null)
				return false;
		} else if (!this.mDepartureBusStation
				.equals(localRouteBusLineItem.mDepartureBusStation))
			return false;

		if (this.mPassStationNum != localRouteBusLineItem.mPassStationNum)
			return false;
		if (this.mPassStations == null) {
			if (localRouteBusLineItem.mPassStations != null)
				return false;
		} else if (!this.mPassStations
				.equals(localRouteBusLineItem.mPassStations))
			return false;
		if (this.mPolyline == null) {
			if (localRouteBusLineItem.mPolyline != null)
				return false;
		} else if (!this.mPolyline.equals(localRouteBusLineItem.mPolyline))
			return false;
		return true;
	}

}
