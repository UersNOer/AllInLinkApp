package com.unistrong.api.services.route;

import com.unistrong.api.services.core.LatLonPoint;

public class RoutePoint implements android.os.Parcelable {
	private String id;// id
	private String cityName;// 城市名称
	private String cityCode;// 城市代码
	private String searchKey;// 搜索关键字
	private LatLonPoint location;// 坐标

    public static final Creator<RoutePoint> CREATOR = new RoutePointCreator();

	/**
	 * RoutePoint的构造。
	 */
	public RoutePoint() {
		super();
	}

	/**
	 * 返回ID。
	 * 
	 * @return id。
	 */
    private String getId() {
		return this.id;
	}

	/**
	 * 设置ID。
	 * 
	 * @param id
	 *            。
	 */
    private void setId(String id) {
		this.id = id;
	}

	/**
	 * 返回该点所在城市名称。
	 * 
	 * @return 该点所在城市名称。
	 */
    private String getCityName() {
		return this.cityName;
	}

	/**
	 * 设置该点所在城市名称。
	 * 
	 * @param cityName
	 *            该点所在城市名称。
	 */
    private void setCityName(String cityName) {
		this.cityName = cityName;
	}

	/**
	 * 返回该点所在城市代码，如 北京：131.
	 * 
	 * @return 该点所在城市名称。
	 */
    private String getCityCode() {
		return this.cityCode;
	}

	/**
	 * 设置该点所在城市代码。
	 * 
	 * @param cityCode
	 *            该点所在城市名称。
	 */
    private void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	/**
	 * 返回该点搜索的关键字。
	 * 
	 * @return 该点搜索的关键字。
	 */
    private String getSearchKey() {
		return this.searchKey;
	}

	/**
	 * 设置该点搜索的关键字。
	 * 
	 * @param searchKey
	 *            该点搜索的关键字。
	 */
    private void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}

	/**
	 * 返回坐标
	 * 
	 * @return 坐标
	 */
	public LatLonPoint getLocation() {
		return this.location;
	}

	public void setLocation(LatLonPoint location) {
		this.location = location;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	/**
	 * 序列化的实现。
	 */
	public RoutePoint(android.os.Parcel source) {
		this.id = source.readString();
		this.cityName = source.readString();
		this.cityCode = source.readString();
		this.searchKey = source.readString();
		this.location = ((LatLonPoint) source.readParcelable(LatLonPoint.class
				.getClassLoader()));

	}

	public void writeToParcel(android.os.Parcel dest, int flags) {
		dest.writeString(this.id);
		dest.writeString(this.cityName);
		dest.writeString(this.cityCode);
		dest.writeString(this.searchKey);
		dest.writeParcelable(this.location, flags);
	}

}
