package com.unistrong.api.services.busline;

import android.os.Parcel;

import com.unistrong.api.services.core.LatLonPoint;

import java.util.ArrayList;
import java.util.List;

/**
 * 此类定义了公交站点信息。
 */
public class BusStationItem implements android.os.Parcelable {
	/**
	 * 站点名称。
	 */
	private String busStationName;
	/**
	 * 站点经纬度。
	 */
	private LatLonPoint latLonPoint;
	/**
	 * 站点id。
	 */
	private String busStationId;
	/**
	 * 行政区划编码。
	 */
	private String adCode;
	/**
	 * 站点类型 0:公交站,1:地铁站
	 */
	private String type;
	/**
	 * 城市名称。
	 */
	private String city;
	/**
	 * 站点顺序。
	 */
	private String order;

	/**
	 * 是否是换成站 ;1：是,0：不是。
	 */
	private String isTransfer;

	/**
	 * 公交路线信息。
	 */
	private List<BusLineItem> busLineItems = new ArrayList<BusLineItem>();
	/**
	 * 城市编码。
	 */
	private String cityCode;
	/**
	 * 数据源名称。
	 */
	private String datasource;
	public static final android.os.Parcelable.Creator<BusStationItem> CREATOR = new BusStationItemCreator();

	/**
	 * BusStationItem构造函数。
	 */
	public BusStationItem() {

	}

	public BusStationItem(Parcel source) {
		this.busStationName = source.readString();
		this.latLonPoint = ((LatLonPoint) source.readParcelable(LatLonPoint.class
				.getClassLoader()));
		this.busStationId = source.readString();
		this.adCode = source.readString();
		this.city = source.readString();
		this.isTransfer = source.readString();
		this.cityCode = source.readString();
		this.busLineItems = source.createTypedArrayList(BusLineItem.CREATOR);
		this.order = source.readString();
		this.type=source.readString();
		this.datasource = source.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.busStationName);
		dest.writeParcelable(this.latLonPoint,flags);
		dest.writeString(this.busStationId);
		dest.writeString(this.adCode);
		dest.writeString(this.city);
		dest.writeString(this.isTransfer);
		dest.writeString(this.cityCode);
		dest.writeTypedList(this.busLineItems);
		dest.writeString(this.order);
		dest.writeString(this.type);
		dest.writeString(this.datasource);
	}

	/**
	 * 设置站点类型
	 * @param type
	 *            站点类型 0:公交站,1:地铁站
	 */
	public void setBusStationType(String type){
		this.type=type;
	}

	/**
	 * 返回站点类型
	 * @return 站点类型 0:公交站,1:地铁站
	 */
	public String getBusStationType(){
		return this.type;
	}
	/**
	 * 设置途径此公交站的公交路线。
	 * 
	 * @param busLineItems
	 *            途径此公交站的公交路线。
	 */
	public void setBusLineItems(List<BusLineItem> busLineItems) {
		this.busLineItems = busLineItems;

	}

	/**
	 * 返回途径此公交站的公交路线。
	 * 
	 * @return 途径此公交站的公交路线。
	 */
	public java.util.List<BusLineItem> getBusLineItems() {
		return busLineItems;

	}

    /**
	 * 返回城市编码。
	 * 
	 * @return 城市编码。
	 */
	public String getCitycode() {
		return cityCode;
	}

	/**
	 * 设置城市编码。
	 * 
	 * @param citycode
	 *            城市编码。
	 */
	public void setCitycode(String citycode) {
		this.cityCode = citycode;
	}

	/**
	 * 返回车站名称。
	 * 
	 * @return 车站名称。
	 */
	public String getBusStationName() {
		return busStationName;
	}

	/**
	 * 返回站点顺序。
	 * 
	 * @return 站点顺序。
	 */
	public String getOrder() {
		return order;
	}

	/**
	 * 设置站点顺序。
	 * 
	 * @param order
	 *            站点顺序。
	 */
	public void setOrder(String order) {
		this.order = order;
	}

	/**
	 * 设置车站名称。
	 * 
	 * @param busStationName
	 *            车站名称。
	 */
	public void setBusStationName(String busStationName) {
		this.busStationName = busStationName;
	}

	/**
	 * 返回车站经纬度坐标。
	 * 
	 * @return 车站经纬度坐标。
	 */
	public LatLonPoint getLatLonPoint() {
		return latLonPoint;
	}

	/**
	 * 设置车站经纬度坐标。
	 * 
	 * @param latLonPoint
	 *            返回车站经纬度坐标。
	 */
	public void setLatLonPoint(LatLonPoint latLonPoint) {
		this.latLonPoint = latLonPoint;
	}

	/**
	 * 返回站点id。
	 * 
	 * @return 站点id。
	 */
	public String getBusStationId() {
		return busStationId;
	}

	/**
	 * 设置站点id。
	 * 
	 * @param busStationId
	 *            站点id。
	 */
	public void setBusStationId(String busStationId) {
		this.busStationId = busStationId;
	}

	/**
	 * 返回行政区划编码。
	 * 
	 * @return 行政区划编码。
	 */
	public String getAdCode() {
		return adCode;
	}

	/**
	 * 设置行政区划编码。
	 * 
	 * @param adCode
	 *            行政区划编码。
	 */
	public void setAdCode(String adCode) {
		this.adCode = adCode;
	}

	/**
	 * 返回城市名称。
	 * 
	 * @return 城市名称。
	 */
	public String getCity() {
		return city;
	}

	/**
	 * 设置城市名称。
	 * 
	 * @param city
	 *            城市名称。
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * 返回站点是否是换乘站。
	 * 
	 * @return 站点是否是换乘站。
	 */
	public boolean isTransfer() {
		if ("1".equals(this.isTransfer))
			return true;
		else
			return false;
	}

	/**
	 * 设置站点是否是换乘站。
	 * 
	 * @param isTransfer
	 *            是否是换乘站。
	 */
	public void setIsTransfer(boolean isTransfer) {
		if (isTransfer)
			this.isTransfer = "1";
		else
			this.isTransfer = "0";

	}

	/**
	 * 返回数据源名称。
	 * @return 数据源名称。
	 */
	public String getDatasource() {
		return datasource;
	}

	/**
	 * 设置数据源名称。
	 * @param datasource
	 *                  数据源名称。
	 */
	public void setDatasource(String datasource) {
		this.datasource = datasource;
	}

	/**
	 * 返回公交站名称、经纬度、途径站点、城市编码和车站区域编码的字符串。
	 */
	@Override
	public String toString() {
		return "BusStationItem [busStationName=" + busStationName
				+ ", latLonPoint=" + latLonPoint + ", busStationId="
				+ busStationId + ", adCode=" + adCode + ", city=" + city
				+ ", busLineItems=" + busLineItems + ", citycode=" + cityCode
				+ ", datasource=" + datasource + "]";
	}

	/**
	 * 比较两个公交站点ID是否相同。
	 * 
	 * @param obj
	 *            查询条件。
	 * @return 查询条件是否相同。
	 */
	public boolean equals(java.lang.Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BusStationItem localBusStationItem = (BusStationItem) obj;
		if (this.busStationId == null) {
			if (localBusStationItem.busStationId != null)
				return false;
		} else if (!this.busStationId.equals(localBusStationItem.busStationId))
			return false;

		if (this.busStationName == null) {
			if (localBusStationItem.busStationName != null)
				return false;
		} else if (!this.busStationName
				.equals(localBusStationItem.busStationName))
			return false;

		return true;
	}

	public int hashCode() {
		int i = 31;
		int j = 1;
		j = i
				* j
				+ (this.busStationId == null ? 0 : this.busStationId.hashCode());
		j = i
				* j
				+ (this.busStationName == null ? 0 : this.busStationName
						.hashCode());
		return j;

	}

}
