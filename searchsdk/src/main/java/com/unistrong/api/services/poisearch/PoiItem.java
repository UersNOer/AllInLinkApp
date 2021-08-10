package com.unistrong.api.services.poisearch;

import android.os.Parcel;

import com.unistrong.api.services.core.LatLonPoint;

/**
 * 定义一个POI（Point Of Interest，兴趣点）。一个POI 由如下成员组成: POI 的唯一标识，即id。这个标识在不同的数据版本中延续。
 * POI 的名称。 POI 的经纬度。 POI 的地址。 POI 的类型代码。 POI 的类型描述。 POI 的电话号码。 POI距离周边搜索中心点的距离。
 */
public class PoiItem implements android.os.Parcelable {
	/**
	 * POI 的唯一标识，即id。
	 */
	private String poiId;
	/**
	 * POI地址。
	 */
	private String snippet;
	/**
	 * POI 的名称。
	 */
	private String title;
	/**
	 * POI电话。
	 */
	private String tel;
	/**
	 * POI位置。
	 */
	private LatLonPoint location;
	/**
	 * poi到中心点的距离。
	 */
	private double distance;
	/**
	 * poi类型。
	 */
	private String typeDes;
	/**
	 * 行政区划编码。
	 */
	private String adcode;
	/**
	 * 城市名称。
	 */
	private String city;
	/**
	 * 城市编码。
	 */
	private String city_code;
	/**
	 * 数据源名称。
	 */
	private String datasource;
	public static final Creator<PoiItem> CREATOR = new PoiItemCreator<PoiItem>();

	/**
	 * 构造一个PoiItem的新对象。
	 */
	public PoiItem() {
		super();
	}

	/**
	 * 根据给定的参数构造一个PoiItem 的新对象。
	 *
	 * @param id
	 *            poi的ID.
	 * @param location
	 *            POI的经纬度。
	 * @param title
	 *            POI的名称。
	 * @param snippet
	 *            POI地址。
	 */
	public PoiItem(java.lang.String id, LatLonPoint location,
			java.lang.String title, java.lang.String snippet) {
		this.poiId = id;
		this.location = location;
		this.title = title;
		this.snippet = snippet;
	}

	/**
	 * 序列化实现。
	 */
	public PoiItem(Parcel source) {
		this.poiId = source.readString();
		this.snippet = source.readString();
		this.title = source.readString();
		this.tel = source.readString();
		this.location = ((LatLonPoint) source.readParcelable(LatLonPoint.class
				.getClassLoader()));
		this.distance = source.readDouble();
		this.typeDes = source.readString();
		this.adcode = source.readString();
		this.city = source.readString();
		this.city_code = source.readString();
		this.datasource = source.readString();
	}

	/**
	 * 返回POI的地址。
	 *
	 * @return POI的地址。
	 */
	public String getSnippet() {
		return this.snippet;
	}

	/**
	 * 返回POI的名称
	 *
	 * @return POI的名称
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * 设置POI 的id，即其唯一标识。
	 *
	 * @param poiId
	 *            POI 的id，即其唯一标识。
	 */
	public void setPoiId(String poiId) {
		this.poiId = poiId;
	}


    /**
     * 设置POI的地址。
     *
     * @param snippet
     *            POI的地址。
     */
    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

	/**
	 * 设置POI的名称。
	 *
	 * @param title
	 *            POI的名称。
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 返回POI的经纬度坐标。
	 *
	 * @return POI的经纬度坐标。
	 */
	public LatLonPoint getLatLonPoint() {
		return this.location;
	}

    /**
     * 设置POI的经纬度坐标。
     * @param location -  POI的经纬度坐标。
     */
    public void setLatLonPoint(LatLonPoint location){
        this.location = location;
    }

	/**
	 * 返回POI 的id，即其唯一标识。
	 *
	 * @return POI 的id，即其唯一标识。
	 */
	public String getPoiId() {
		return this.poiId;
	}

	/**
	 * 返回POI的电话号码。
	 *
	 * @return POI的电话号码。
	 */
	public String getTel() {
		return this.tel;
	}

	/**
	 * 设置POI的电话号码。
	 *
	 * @param tel
	 *            POI的电话号码。
	 */
	public void setTel(String tel) {
		this.tel = tel;
	}

	/**
	 * 获取 POI 距离中心点的距离。
	 *
	 * @return POI 距离中心点的距离。
	 */
	public double getDistance() {
		return this.distance;
	}

	/**
	 * 设置 POI 距离中心点的距离。
	 *
	 * @param distance
	 *            POI 距离中心点的距离。
	 */
	public void setDistance(double distance) {
		this.distance = distance;
	}

	/**
	 * 返回POI 的类型描述。
	 *
	 * @return POI 的类型描述。
	 */
	public String getTypeDes() {
		return this.typeDes;
	}

	/**
	 * 设置POI 的类型描述。
	 *
	 * @param typeDes
	 *            的类型描述
	 */
	public void setTypeDes(String typeDes) {
		this.typeDes = typeDes;
	}

	/**
	 * 返回poi所在地的行政区划编码。
	 * @return poi所在地的行政区划编码。
	 */
	public String getAdcode() {
		return adcode;
	}

	/**
	 * 设置poi所在地的行政区划编码。
	 * @param adcode
	 *              poi所在地的行政区划编码。
	 */
	public void setAdcode(String adcode) {
		this.adcode = adcode;
	}

	/**
	 * 返回poi所在的城市名称。
	 * @return poi所在的城市名称。
	 */
	public String getCity() {
		return city;
	}

	/**
	 * 设置poi所在的城市名称。
	 * @param city
	 *            poi所在的城市名称。
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * 返回poi所在地的城市编码。
	 * @return poi所在地的城市编码。
	 */
	public String getCity_code() {
		return city_code;
	}

	/**
	 * 设置poi所在地的城市编码。
	 * @param city_code
	 *                 poi所在地的城市编码。
	 */
	public void setCity_code(String city_code) {
		this.city_code = city_code;
	}

	/**
	 * 返回数据源名称。
	 * @return
	 *        数据源名称。
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

	public int hashCode() {
		int i1 = 31;
		int i2 = 1;
		i2 = i1 * i2 + (this.poiId == null ? 0 : this.poiId.hashCode());
		return i2;
	}

	/**
	 * 比较两个POI 的id 是否相等。
	 */
	public boolean equals(java.lang.Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PoiItem localPoiItem = (PoiItem) obj;
		if (this.poiId == null) {
			if (localPoiItem.poiId != null)
				return false;
		} else if (!this.poiId.equals(localPoiItem.poiId))
			return false;
		return true;
	}

	/**
	 * 继承自Object，返回POI 的名称（name）。
	 */
	@Override
	public String toString() {
		return "PoiItem{" +
				"poiId='" + poiId + '\'' +
				", snippet='" + snippet + '\'' +
				", title='" + title + '\'' +
				", tel='" + tel + '\'' +
				", location=" + location +
				", distance=" + distance +
				", typeDes='" + typeDes + '\'' +
				", adcode='" + adcode + '\'' +
				", city='" + city + '\'' +
				", city_code='" + city_code + '\'' +
				", datasource='" + datasource + '\'' +
				'}';
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.poiId);
		dest.writeString(this.snippet);
		dest.writeString(this.title);
		dest.writeString(this.tel);
		dest.writeParcelable(this.location, flags);
		dest.writeDouble(this.distance);
		dest.writeString(this.typeDes);
		dest.writeString(this.adcode);
		dest.writeString(this.city);
		dest.writeString(this.city_code);
		dest.writeString(this.datasource);
	}
}
