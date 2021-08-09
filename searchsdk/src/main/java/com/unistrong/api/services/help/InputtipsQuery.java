package com.unistrong.api.services.help;

import com.unistrong.api.services.core.LatLonPoint;

/**
 * 此类定义了输入提示的关键字，类别及城市。
 */
public class InputtipsQuery implements java.lang.Cloneable {
	/**
	 * 输入的关键字,或关键字的首字母、拼音。
	 */
	private String keyword;
	/**
	 * 检索区域名称，可输入城市名、省份名或全国。
	 */
	private String citycode;
	/**
	 * 检索结果的详细程度，1：返回基本信息; 2：返回POI详细信息 ;默认值为1。
	 */
	private int scope = 1;
	/**
	 * 数据源类型，可选值如下：
	 * district：行政区划,poi：兴趣点,bus：公交站点,busline：公交线路。可选择多个，
	 * 不同数据源之间用“,”分隔，格式，如：poi,bus。默认为全部。
	 */
	private String datasource;
	/**
	 * POI类型，多个POI类型之间用“|”分隔，格式如，医院|酒店……
	 */
	private String type;
	/**
	 * 经纬度坐标(格式如：纬度,经度),用于在给出提示信息时优先显示距离该点较近的关键字信息。
	 */
	private LatLonPoint location;

	/**
	 * 根据给定的参数来构造一个InputtipsQuery的对象。
	 * 
	 * @param keyword
	 *            输入的关键字。
	 * @param city
	 *            检索区域名称，可输入城市名、省份名或全国。
	 */
	public InputtipsQuery(String keyword, String city) {
		super();
		this.keyword = keyword;
		this.citycode = city;
	}

	/**
	 * 根据给定的参数来构造一个InputtipsQuery的对象。
	 * 
	 * @param keyword
	 *            输入的关键字。
	 * @param city
	 *            检索区域名称，可输入城市名、省份名或全国。
	 * @param scope
	 *            返回信息的详细程度。
	 */
	public InputtipsQuery(String keyword, String city, int scope) {
		this.keyword = keyword;
		this.citycode = city;
		this.scope = scope;
	}

	/**
	 * 返回查询关键字。
	 * 
	 * @return 查询关键字。
	 */
	public String getKeyword() {
		return keyword;
	}

	/**
	 * 设置查询关键字。
	 * 
	 * @param keyword
	 *            查询关键字。
	 */
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	/**
	 * 返回待查城市（地区）的电话区号。
	 * 
	 * @return 返回待查城市（地区）的电话区号。
	 */
	public String getCity() {
		return citycode;
	}

	/**
	 * 设置待查城市（地区）的电话区号。
	 * 
	 * @param city
	 *            待查城市（地区）的电话区号。
	 */
	public void setCity(String city) {
		this.citycode = city;
	}

	/**
	 * 返回检索结果的详细程度值。
	 * 
	 * @return 检索结果的详细程度值。
	 */
	public int getScope() {
		return scope;
	}

	/**
	 * 设置检索结果的详细程度，1：返回基本信息; 2：返回POI详细信息 ;默认值为1。
	 * 
	 * @param scope
	 *            检索结果的详细程度。
	 */
	public void setScope(int scope) {
		this.scope = scope;
	}

	/**
	 * <p><em>从V3.6.14增加此接口。</em></p>
	 * 返回数据源类型。
	 * @return 数据源类型。
	 * @since 1.10.0
	 */
	public String getDatasource() {
		return datasource;
	}

	/**
	 * <p><em>从V3.6.14增加此接口。</em></p>
	 * 设置数据源类型。
	 * @param datasource
	 *                  数据源类型，可选值如下：district：行政区划,poi：兴趣点,bus：公交站点,
	 *                  busline：公交线路。可选择多个，不同数据源之间用“,”分隔，格式，如：poi,bus。默认为全部。
	 * @since 1.10.0
	 */
	public void setDatasource(String datasource) {
		this.datasource = datasource;
	}

	/**
	 * <p><em>从V3.6.14增加此接口。</em></p>
	 * 返回POI类型。
	 * @return  POI类型，多个POI类型之间用“|”分隔，格式如，医院|酒店……
	 * @since 1.10.0
	 */
	public String getType() {
		return type;
	}

	/**
	 * <p><em>从V3.6.14增加此接口。</em></p>
	 * 设置POI类型。
	 * @param type
	 *            POI类型，多个POI类型之间用“|”分隔，格式如，医院|酒店……
	 * @since 1.10.0
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * <p><em>从V3.6.14增加此接口。</em></p>
	 * 返回中心点坐标。
	 * @return 中心点的经纬度坐标。
	 * @since 1.10.0
	 */
	public LatLonPoint getLocation() {
		return location;
	}

	/**
	 * <p><em>从V3.6.14增加此接口。</em></p>
	 * 设置中心点坐标。
	 * @param location
	 *                 经纬度坐标(格式如：纬度,经度),用于在给出提示信息时优先显示距离该点较近的关键字信息。
	 * @since 1.10.0
	 */
	public void setLocation(LatLonPoint location) {
		this.location = location;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {

		InputtipsQuery inputtipsQuery = new InputtipsQuery(this.keyword, this.citycode, this.scope);
		inputtipsQuery.setDatasource(this.datasource);
		inputtipsQuery.setType(this.type);
		inputtipsQuery.setLocation(this.location);
		return inputtipsQuery;
	}

}
