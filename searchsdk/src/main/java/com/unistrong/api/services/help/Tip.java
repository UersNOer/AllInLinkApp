package com.unistrong.api.services.help;

import android.os.Parcel;
import android.os.Parcelable;

import com.unistrong.api.services.core.LatLonPoint;

/**
 * 定义Tip的类。
 */
public class Tip implements android.os.Parcelable {
	/**
	 * poi唯一标识。
	 */
	private String poiID;
	/**
	 * poi名称。
	 */
	private String name;
	/**
	 * 坐标。
	 */
	private LatLonPoint point;
	/**
	 * 提示区域编码。
	 */
	private String adcode;
	/**
	 * 提示信息类型。
	 */
	private String type;
	/**
	 * 城市名称。
	 */
	private String city;
	/**
	 * 区（县）名称区（县）名称。
	 */
	private String district;
	/**
	 * 提示信息所在商圈。
	 */
	private String business;
	/**
	 * 城市id。
	 */
	private String cityCode;
	/**
	 * 详细地址信息。
	 */
	private String address;
	/**
	 * 电话号码。
	 */
	private String telephone;



	/**
	 * 数据源名称。
	 */
	private String datasource;

	public static final Parcelable.Creator<Tip> CREATOR = new TipCreator();

	/**
	 * Tip的构造函数。
	 */
	public Tip() {
		super();
	}

	/**
	 * Tip的构造函数。
	 * 
	 * @param paramParcel
	 *            序列化接口的实现实例。
	 */
	public Tip(Parcel paramParcel) {
		this.poiID = paramParcel.readString();
		this.name = paramParcel.readString();
		this.adcode = paramParcel.readString();
		this.type = paramParcel.readString();
		this.city = paramParcel.readString();
		this.district = paramParcel.readString();
		this.business = paramParcel.readString();
		this.cityCode = paramParcel.readString();
		this.point = ((LatLonPoint) paramParcel.readValue(LatLonPoint.class
				.getClassLoader()));
		this.address = paramParcel.readString();
		this.telephone = paramParcel.readString();
		this.datasource = paramParcel.readString();
	}

	@Override
	public void writeToParcel(Parcel paramParcel, int flags) {
		paramParcel.writeString(this.poiID);
		paramParcel.writeString(this.name);
		paramParcel.writeString(this.adcode);
		paramParcel.writeString(this.type);
		paramParcel.writeString(this.city);
		paramParcel.writeString(this.district);
		paramParcel.writeString(this.business);
		paramParcel.writeString(this.cityCode);
		paramParcel.writeValue(this.point);
		paramParcel.writeString(this.address);
		paramParcel.writeString(this.telephone);
        paramParcel.writeString(this.datasource);
	}

	/**
	 * 获取Poi的ID，如果不存在id则为空。
	 * 
	 * @return Poi的ID。
	 */
	public String getPoiID() {
		return poiID;
	}

	/**
	 * 设置Poi的ID。
	 * 
	 * @param poiID
	 *            Poi的ID。
	 */
	public void setPoiID(String poiID) {
		this.poiID = poiID;
	}

	/**
	 * 返回提示名称。
	 * 
	 * @return 提示名称。
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置提示名称 。
	 * 
	 * @param name
	 *            提示名称。
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 返回Poi的经纬度，如果不存在经纬度则为空。
	 * 
	 * @return 获取Poi的经纬度，如果不存在经纬度则为空。
	 */
	public LatLonPoint getPoint() {
		return point;
	}

	/**
	 * 设置经纬度。
	 * 
	 * @param point
	 *            经纬度。
	 */
	public void setPoint(LatLonPoint point) {
		this.point = point;
	}

	/**
	 * 返回提示区域编码。
	 * 
	 * @return 返回提示区域编码。
	 */
	public String getAdcode() {
		return adcode;
	}

	/**
	 * 设置提示区域编码。
	 * 
	 * @param adcode
	 *            提示区域编码。
	 */
	public void setAdcode(String adcode) {
		this.adcode = adcode;
	}

	/**
	 * 返回提示信息类型。
	 * 
	 * @return 提示信息类型。
	 */
	public String getType() {
		return type;
	}

	/**
	 * 设置提示信息类型。
	 * 
	 * @param type
	 *            提示信息类型。
	 */
	public void setType(String type) {
		this.type = type;
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
	 * 返回区（县）名称。
	 * 
	 * @return 区（县）名称。
	 */
	public String getDistrict() {
		return district;
	}

	/**
	 * 设置区（县）名称。
	 * 
	 * @param district
	 *            区（县）名称。
	 */
	public void setDistrict(String district) {
		this.district = district;
	}

	/**
	 * 返回提示信息所在商圈。
	 * 
	 * @return 提示信息所在商圈。
	 */
	public String getBusiness() {
		return business;
	}

	/**
	 * 设置提示信息所在商圈。
	 * 
	 * @param business
	 *            提示信息所在商圈。
	 */
	public void setBusiness(String business) {
		this.business = business;
	}

	/**
	 * 返回城市区号。
	 * 
	 * @return 城市区号。
	 */
	public String getCitycode() {
		return cityCode;
	}

	/**
	 * 设置城市区号。
	 * 
	 * @param citycode
	 *            城市区号。
	 */
	public void setCitycode(String citycode) {
		this.cityCode = citycode;
	}

	/**
	 * 设置详细地址信息。
	 * 
	 * @return 详细地址信息。
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * 设置详细地址信息。
	 * 
	 * @param address
	 *            详细地址信息。
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * 返回电话号码。
	 * 
	 * @return 电话号码。
	 */
	public String getTelephone() {
		return telephone;
	}

	/**
	 * 设置电话号码。
	 * 
	 * @param telephone
	 *            电话号码。
	 */
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	/**
	 * 获取数据源名称。
	 * @return 数据源名称。
	 */
	public String getDatasource() {
		return datasource;
	}

	/**
	 * 设置数据源名称。
	 * @param datasource 数据源名称。
	 */
	public void setDatasource(String datasource) {
		this.datasource = datasource;
	}
	/**
	 * 将提示转换为字符串输出。
	 */
	@Override
	public String toString() {
		return "Tip [poiID=" + poiID + ", name=" + name + ", point=" + point
				+ ", adcode=" + adcode + ", type=" + type + ", city=" + city
				+ ", district=" + district + ", business=" + business
				+ ", cityid=" + cityCode + ", address=" + address
				+ ", telephone=" + telephone + ", datasource="+ datasource +"]";
	}

	/**
	 * describeContents 在接口中 android.os.Parcelable
	 */
	@Override
	public int describeContents() {
		return 0;
	}

}
