package com.unistrong.api.services.district;

import android.os.Parcel;
import android.os.Parcelable;

import com.unistrong.api.services.core.LatLonPoint;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 行政区信息类。
 * 
 */
public class DistrictItem implements Parcelable {
	/**
	 * 城市编码。
	 */
	private String citycode;
	/**
	 * 区域编码。
	 */
	private String adcode;
	/**
	 * 行政区名称。
	 */
	private String name;
	/**
	 * 行政区划边界字符串数组。
	 */
	private String[] disStr;
	/**
	 * 行政区中心点位置。
	 */
	private LatLonPoint center;
	/**
	 * 行政区划级别。
	 */
	private String level;
	/**
	 * 矩形区域的顶点坐标，取左下、右上坐标点。
	 */
	private String bounds;
	/**
	 * 城市名称。
	 */
	private String city;
	/**
	 * 数据源名称。
	 */
	private String datasource;
	/**
	 * 行政区划边界，格式为 经度,纬度;经度,纬度;经度,纬度;…|经度,纬度;经度,纬度;…二维list。
	 */
	private ArrayList<ArrayList<LatLonPoint>> polyline = new ArrayList<ArrayList<LatLonPoint>>();
	/**
	 * 矩形区域的顶点坐标，取左下、右上坐标点。
	 */
	private ArrayList<LatLonPoint> boundsList = new ArrayList<LatLonPoint>();
	public static final Parcelable.Creator<DistrictItem> CREATOR = new DistrictItemCreator();

	/**
	 * 行政区划类的构造函数。
	 */
	public DistrictItem() {
	}

	/**
	 * 行政区划类的构造函数。
	 * 
	 * @param citycode
	 *            城市编码。
	 * @param adcode
	 *            区域编码。
	 * @param name
	 *            行政区名称 。
	 * @param center
	 *            行政区中心点位置。
	 * @param level
	 *            行政区划级别。
	 */
	public DistrictItem(String citycode, String adcode, String name,
			LatLonPoint center, String level) {
		this.citycode = citycode;
		this.adcode = adcode;
		this.name = name;
		this.center = center;
		this.level = level;
	}

	public DistrictItem(Parcel source) {
		this.citycode = source.readString();
		this.adcode = source.readString();
		this.name = source.readString();
		int length = source.readInt();
		// 如果数组长度大于0，那么就读数组， 所有数组的操作都可以这样。
		if (length > 0) {
			disStr = new String[length];
			source.readStringArray(disStr);
		}
		this.level = source.readString();
		this.center = ((LatLonPoint) source.readValue(LatLonPoint.class
				.getClassLoader()));
		this.bounds = source.readString();
		this.city = source.readString();
		this.datasource = source.readString();
		String polyline_start = source.readString();
		this.polyline = new ArrayList<>();
		while (source.readString().contains("tag")){
			ArrayList<LatLonPoint> pointList = new ArrayList<>();
			source.readTypedList(pointList, LatLonPoint.CREATOR);
			this.polyline.add(pointList);
		}

		source.readTypedList(boundsList, LatLonPoint.CREATOR);

	}

	/**
	 * 设置行政区划边界值。
	 * 
	 * @param disStr
	 *            行政区划边界值。
	 */
	public void setDistrictBoundary(java.lang.String[] disStr) {
		this.disStr = disStr;
	}

	/**
	 * 以字符串数组形式返回行政区划边界值。
	 * 
	 * @return 行政区划边界值。
	 */
	public java.lang.String[] districtBoundary() {
		return disStr;

	}

	/**
	 * 返回城市编码。
	 * 
	 * @return 城市编码。
	 */
	public String getCitycode() {
		return citycode;
	}

	/**
	 * 设置城市编码。
	 * 
	 * @param citycode
	 *            城市编码。
	 */
	public void setCitycode(String citycode) {
		this.citycode = citycode;
	}

	/**
	 * 返回区域编码。
	 * 
	 * @return 区域编码。
	 */
	public String getAdcode() {
		return adcode;
	}

	/**
	 * 设置区域编码。
	 * 
	 * @param adcode
	 *            区域编码。
	 */
	public void setAdcode(String adcode) {
		this.adcode = adcode;
	}

	/**
	 * 返回行政区域的名称。
	 * 
	 * @return 行政区域的名称。
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置行政区域的名称。
	 * 
	 * @param name
	 *            行政区域的名称。
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 返回行政区域规划中心点的经纬度坐标。
	 * 
	 * @return 行政区域规划中心点的经纬度坐标。
	 */
	public LatLonPoint getCenter() {
		return center;
	}

	/**
	 * 设置行政区域规划中心点的经纬度坐标。
	 * 
	 * @param center
	 *            行政区域规划中心点的经纬度坐标。
	 */
	public void setCenter(LatLonPoint center) {
		this.center = center;
	}

	/**
	 * 返回当前行政区划的级别。
	 * 
	 * @return 当前行政区划的级别。
	 */
	public String getLevel() {
		return level;
	}

	/**
	 * 设置当前行政区划的级别。
	 * 
	 * @param level
	 *            当前行政区划的级别。
	 */
	public void setLevel(String level) {
		this.level = level;
	}

	/**
	 * 返回下一级行政区划的结果，如果无下级行政区划，返回null。
	 * 
	 * @return 下一级行政区划的结果，如果无下级行政区划，返回null。
	 */
	private java.util.List<DistrictItem> getSubDistrict() {
		return null;

	}

	/**
	 * 设置下一级行政区划的结果。
	 * 
	 * @param districts
	 *            下一级行政区划。
	 */
    private void setSubDistrict(java.util.ArrayList<DistrictItem> districts) {

	}

	/**
	 * 返回矩形区域的顶点坐标，取左下、右上坐标点。
	 * @return  矩形区域的顶点坐标，取左下、右上坐标点。
	 */
	public String getBounds() {
		return bounds;
	}

	/**
	 * 设置 矩形区域的顶点坐标，取左下、右上坐标点。
	 * @param bounds
	 *               矩形区域的顶点坐标，取左下、右上坐标点。
	 */
	public void setBounds(String bounds) {
		this.bounds = bounds;
	}

	/**
	 * 返回城市名称。
	 * @return  城市名称。
	 */
	public String getCity() {
		return city;
	}

	/**
	 * 设置城市名称。
	 * @param city
	 *            城市名称。
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * 返回数据源名称。
	 * @return  数据源名称。
	 */
	public String getDatasource() {
		return datasource;
	}

	/**
	 * 设置数据源名称。
	 * @param datasource
	 *                   数据源名称。
	 */
	public void setDatasource(String datasource) {
		this.datasource = datasource;
	}

	/**
	 * 返回行政区划边界。
	 * @return 行政区划边界，格式为 经度,纬度;经度,纬度;经度,纬度;…|经度,纬度;经度,纬度;…二维list。
	 */
	public ArrayList<ArrayList<LatLonPoint>> getPolyline() {
		return polyline;
	}

	/**
	 * 设置行政区划边界。
	 * @param polyline
	 *                行政区划边界，格式为 经度,纬度;经度,纬度;经度,纬度;…|经度,纬度;经度,纬度;…二维list。
	 */
	public void setPolyline(ArrayList<ArrayList<LatLonPoint>> polyline) {
		this.polyline = polyline;
	}

	/**
	 * 返回矩形区域的顶点坐标，取左下、右上坐标点。
	 * @return  矩形区域的顶点坐标，取左下、右上坐标点。
	 */
	public ArrayList<LatLonPoint> getBoundsList() {
		return boundsList;
	}

	/**
	 * 设置矩形区域的顶点坐标，取左下、右上坐标点。
	 * @param boundsList
	 *                   矩形区域的顶点坐标，取左下、右上坐标点。
	 */
	public void setBoundsList(ArrayList<LatLonPoint> boundsList) {
		this.boundsList = boundsList;
	}

	/**
	 * 将行政区信息转换成字符串输出 返回行政区的城市编码、区域编码、行政区名称、中心点、行政区划级别。
	 */
	@Override
	public String toString() {
		return "DistrictItem [citycode=" + citycode + ", adcode=" + adcode
				+ ", name=" + name + ", disStr=" + Arrays.toString(disStr)
				+ ", center=" + center + ", level=" + level +", bounds="+bounds
				+ ", city=" + city + ", datasource=" + datasource
				+ ", polyline=" + polyline + ", boundsList=" + boundsList + "]";
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(citycode);
		dest.writeString(adcode);
		dest.writeString(name);
		if (disStr == null) {
			dest.writeInt(0);
		} else {
			dest.writeInt(disStr.length);
		}
		// 如果数组为空，就可以不写
		if (disStr != null) {
			dest.writeStringArray(disStr);
		}
		dest.writeString(level);
		dest.writeValue(center);
		dest.writeString(this.bounds);
		dest.writeString(this.city);
		dest.writeString(this.datasource);
		dest.writeString("polyline_start");
		int len = this.polyline.size();
		if (len > 0){
			for (int i=0;i<len;i++){
				ArrayList<LatLonPoint> pointList = this.polyline.get(i);
				dest.writeString("tag"+i);
				dest.writeTypedList(pointList);
			}
		}
		dest.writeString("polyline_end");
		dest.writeTypedList(boundsList);
	}

}
