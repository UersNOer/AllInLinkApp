package com.unistrong.api.services.geocoder;

import java.util.List;

/**
 * 逆地理编码的搜索结果。
 */
public class RegeocodeResult {
	/**
	 * 查询条件。
	 */
	private RegeocodeQuery regeocodeQuery;

	/**
	 * 逆地理编码搜索的地理结果集合。
	 */
	private List<RegeocodeAddress> regeocodeAddressList;

	/**
	 * RegeocodeResult构造函数
	 */
	public RegeocodeResult() {
		super();
	}


	/**
	 * RegeocodeResult构造函数。
	 * 
	 * @param regeocodeQuery
	 *            逆地理编码查询条件。
	 * @param regeocodeAddressList
	 *            逆地理编码查询结果。
	 */
	public RegeocodeResult(RegeocodeQuery regeocodeQuery,
			List<RegeocodeAddress> regeocodeAddressList) {
		this.regeocodeQuery = regeocodeQuery;
		this.regeocodeAddressList = regeocodeAddressList;
	}

	/**
	 * 返回该结果对应的查询参数。
	 * 
	 * @return 该结果对应的查询参数。
	 */
	public RegeocodeQuery getRegeocodeQuery() {
		return this.regeocodeQuery;
	}

	/**
	 * 设置该结果对应的查询参数。
	 * 
	 * @param regeocodeQuery
	 *            该结果对应的查询参数。
	 */
	public void setRegeocodeQuery(RegeocodeQuery regeocodeQuery) {
		this.regeocodeQuery = regeocodeQuery;
	}


	/**
	 * 返回逆地理编码搜索的地理结果集合。
	 * 
	 * @return 逆地理编码搜索的地理结果集合。
	 */
	public List<RegeocodeAddress> getRegeocodeAddressList() {
		return regeocodeAddressList;
	}

	/**
	 * 设置逆地理编码搜索的结果。
	 * 
	 * @param regeocodeAddressList
	 *            逆地理编码搜索的地理结果集合 。
	 */
	public void setRegeocodeAddressList(
			List<RegeocodeAddress> regeocodeAddressList) {
		this.regeocodeAddressList = regeocodeAddressList;
	}

}
