package com.unistrong.api.services.geocoder;

import android.os.Parcel;
import android.os.Parcelable;

import com.unistrong.api.services.core.LatLonPoint;
import com.unistrong.api.services.poisearch.PoiItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 逆地理编码返回的结果。
 */
public class RegeocodeAddress implements Parcelable {

	/**
	 * 经纬度坐标。
	 */
	private LatLonPoint location;
	/**
	 * 逆地理编码返回的格式化地址。
	 */
	private String formatAddress;

	/**
	 * 逆地理编码返回的POI(兴趣点)列表。
	 */
	private List<PoiItem> poiList = new ArrayList<PoiItem>();
	/**
	 * 所在省名称、直辖市的名称。
	 */
	private String province;
	/**
	 * 所在城市名称。
	 */
	private String city;

	/**
	 * 所在区（县）名称。
	 */
	private String district;

	/**
	 * 所在城市编码。
	 */
	private String cityCode;

	/**
	 * 所在区（县）的编码。
	 */
	private String adCode;
	/**
	 * 街道门牌号。
	 */
	private String streetNumber;

	/**
	 * 街道名称。
	 */
	private String streetName;
	/**
	 * 距离。
	 */
	private float distance;

	public static final Parcelable.Creator<RegeocodeAddress> CREATOR = new RegeocodeAddressCreator();

	/**
	 * 构造RegeocodeAddress对象
	 */
	public RegeocodeAddress() {
		super();
	}

	public RegeocodeAddress(Parcel paramParcel) {

		this.formatAddress = paramParcel.readString();
		this.location = ((LatLonPoint) paramParcel.readValue(LatLonPoint.class
				.getClassLoader()));
		this.poiList = paramParcel.createTypedArrayList(PoiItem.CREATOR);
		this.province = paramParcel.readString();
		this.city = paramParcel.readString();
		this.district = paramParcel.readString();
		this.cityCode = paramParcel.readString();
		this.adCode = paramParcel.readString();
		this.streetNumber = paramParcel.readString();
		this.streetName = paramParcel.readString();
		this.distance = paramParcel.readFloat();

	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		arg0.writeString(this.formatAddress);
		arg0.writeValue(this.location);
		arg0.writeTypedList(this.poiList);
		arg0.writeString(this.province);
		arg0.writeString(this.city);
		arg0.writeString(this.district);
		arg0.writeString(this.cityCode);
		arg0.writeString(this.adCode);
		arg0.writeString(this.streetNumber);
		arg0.writeString(this.streetName);
		arg0.writeFloat(this.distance);

	}

	/**
	 * 返回逆地理编码返回的所在省名称、直辖市的名称。
	 * 
	 * @return 逆地理编码返回的所在省名称、直辖市的名称。
	 */
	public java.lang.String getProvince() {
		return province;
	}

	/**
	 * 设置逆地理编码返回的所在省名称、直辖市的名称。
	 * 
	 * @param province
	 *            逆地理编码返回的所在省名称、直辖市的名称。
	 */
	public void setProvince(java.lang.String province) {
        this.province = province;

	}

	/**
	 * 逆地理编码返回的所在城市名称。
	 * 
	 * @return 逆地理编码返回的所在城市名称。
	 */
	public java.lang.String getCity() {
		return city;

	}

	/**
	 * 设置逆地理编码返回的所在城市名称。
	 * 
	 * @param city
	 *            逆地理编码返回的所在城市名称。
	 */
	public void setCity(java.lang.String city) {
		this.city = city;
	}

	/**
	 * 设置逆地理编码结果所在城市编码。
	 * 
	 * @param cityCode
	 *            逆地理编码结果所在城市编码。
	 */
	public void setCityCode(java.lang.String cityCode) {
		this.cityCode = cityCode;
	}

	/**
	 * 返回逆地理编码结果所在城市编码。
	 * 
	 * @return 逆地理编码结果所在城市编码。
	 */
	public java.lang.String getCityCode() {
		return cityCode;

	}

	/**
	 * 返回逆地理编码结果所在区（县）的编码。
	 * 
	 * @return 逆地理编码结果所在区（县）的编码。
	 */
	public java.lang.String getAdCode() {
		return adCode;

	}

	/**
	 * 设置逆地理编码结果所在区（县）的编码。
	 * 
	 * @param adCode
	 *            逆地理编码结果所在区（县）的编码。
	 */
	public void setAdCode(java.lang.String adCode) {
		this.adCode = adCode;

	}

	/**
	 * 返回逆地理编码返回的所在区（县）名称。
	 * 
	 * @return 逆地理编码返回的所在区（县）名称。
	 */
	public java.lang.String getDistrict() {
		return district;

	}

	/**
	 * 设置逆地理编码返回的所在区（县）名称。
	 * 
	 * @param district
	 *            逆地理编码返回的所在区（县）名称。
	 */
	public void setDistrict(java.lang.String district) {
		this.district = district;
	}

	/**
	 * 返回街道门牌号。
	 * 
	 * @return 街道门牌号。
	 */
	public String getStreetNumber() {
		return streetNumber;

	}

	/**
	 * 设置街道门牌号。
	 * 
	 * @param streetNumber
	 *            街道门牌号。
	 */
	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}

	/**
	 * 返回街道名称。
	 * 
	 * @return 街道名称。
	 */
	public String getStreetName() {
		return streetName;

	}

	/**
	 * 设置街道名称。
	 * 
	 * @param streetName
	 *            街道名称。
	 */
	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	/**
	 * 返回经纬度坐标。
	 * 
	 * @return 经纬度坐标。
	 */
	public LatLonPoint getLocation() {
		return location;
	}

	/**
	 * 设置经纬度坐标。
	 * 
	 * @param location
	 *            经纬度坐标。
	 */
	public void setLocation(LatLonPoint location) {
		this.location = location;
	}

	/**
	 * 返回逆地理编码返回的格式化地址。
	 * 
	 * @return 逆地理编码返回的格式化地址。
	 */
	public String getFormatAddress() {
		return formatAddress;
	}

	/**
	 * 设置逆地理编码返回的格式化地址。
	 * 
	 * @param formatAddress
	 *            逆地理编码返回的格式化地址。
	 */
	public void setFormatAddress(String formatAddress) {
		this.formatAddress = formatAddress;
	}

	/**
	 * 返回距离。
	 * 
	 * @return 距离。
	 */
	public float getDistance() {
		return distance;
	}

	/**
	 * 设置距离。
	 * 
	 * @param distance
	 *            距离。
	 */
	public void setDistance(float distance) {
		this.distance = distance;
	}

	/**
	 * 设置逆地理编码返回的POI(兴趣点)列表。
	 * 
	 * @param pois
	 *            逆地理编码返回的POI(兴趣点)列表。
	 */
	public void setPois(java.util.List<PoiItem> pois) {
		this.poiList = pois;
	}

	/**
	 * 逆地理编码返回的POI(兴趣点)列表。
	 * 
	 * @return 结果的POI(兴趣点)列表。
	 */
	public java.util.List<PoiItem> getPois() {
		return poiList;

	}


}
