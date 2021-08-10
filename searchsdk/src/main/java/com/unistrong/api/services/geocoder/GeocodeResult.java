package com.unistrong.api.services.geocoder;

import java.util.ArrayList;
import java.util.List;

/**
 * 地理编码的搜索结果。
 */
public class GeocodeResult {
	/**
	 * 查询条件。
	 */
	private GeocodeQuery query;
	/**
	 * 地理编码搜索的地理结果。
	 */
	private List<GeocodeAddress> addressList = new ArrayList<GeocodeAddress>();

	/**
	 * GeocodeResult 构造函数
	 */
	public GeocodeResult() {
		super();
	}

	/**
	 * 依据给定的参数构造地理编码的搜索结果对象。
	 * 
	 * @param paramGeocodeQuery
	 *            查询条件。
	 * @param paramList
	 *            地理编码搜索的结果。
	 */
	public GeocodeResult(GeocodeQuery paramGeocodeQuery,
			List<GeocodeAddress> paramList) {
		this.query = paramGeocodeQuery;
		this.addressList = paramList;
	}

	/**
	 * 返回该结果对应的查询参数。
	 * 
	 * @return 该结果对应的查询参数。
	 */
	public GeocodeQuery getGeocodeQuery() {
		return this.query;
	}

	/**
	 * 设置查询结果对应的查询参数。
	 * 
	 * @param paramGeocodeQuery
	 *            查询结果对应的查询参数。
	 */
	public void setGeocodeQuery(GeocodeQuery paramGeocodeQuery) {
		this.query = paramGeocodeQuery;
	}

	/**
	 * 返回地理编码搜索的地理结果。
	 * 
	 * @return 地理编码搜索的地理结果。
	 */
	public List<GeocodeAddress> getGeocodeAddressList() {
		return this.addressList;
	}

	/**
	 * 设置地理编码搜索的结果。
	 * 
	 * @param paramList
	 *            地理编码搜索的结果。
	 */
	public void setGeocodeAddressList(List<GeocodeAddress> paramList) {
		this.addressList = paramList;
	}
}
