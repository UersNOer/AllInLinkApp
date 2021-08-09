package com.unistrong.api.services.busline;

/**
 * 此类定义了公交站点搜索的关键字和城市。
 */
public class BusStationQuery {
	/**
	 * 公交站点查询名称。
	 */
	private String stationQuery;
	/**
	 * 待查询城市。
	 */
	private String city;

	/**
	 * 每页记录数，最大值为50,默认为10。
	 */
	private int pageSize = 10;
	/**
	 * 
	 * 分页页码，1为第一页，2为第二页,默认值为1。
	 */
	private int pageNumber = 1;
	/**
	 * 公交站点搜索类型,包括站点名称和站点id。
	 */
	private StopSearchType category;

	/**
	 * BusStationQuery构造函数。
	 */

	public BusStationQuery() {

	}

	/**
	 * BusStationQuery 构造函数。
	 * 
	 * @param query
	 *            查询关键字。
	 * @param ctgr
	 *            查询类型:站点名称和站点id,{@link BusStationQuery.StopSearchType}。
	 * @param city
	 *            城市编码。
	 */
	public BusStationQuery(String query, BusStationQuery.StopSearchType ctgr,
			java.lang.String city) {
		this.city = city;
		this.stationQuery = query;
		this.category = ctgr;
	}

	/**
	 * 返回查询关键字。
	 * 
	 * @return 该结果的查询关键字。
	 */
	public java.lang.String getQueryString() {
		return stationQuery;

	}

	/**
	 * 返回查询城市编码。
	 * 
	 * @return 返回查询城市编码。
	 */
	public java.lang.String getCity() {
		return city;

	}

	/**
	 * 设置查询城市参数
	 * 
	 * @param city
	 *            查询城市参数。
	 */
	public void setCity(java.lang.String city) {
		this.city = city;
	}

	/**
	 * 返回查询数据属于第几页。
	 * 
	 * @return 查询第几页。
	 */
	public int getPageNumber() {
		return pageNumber;

	}

	/**
	 * 返回查询每页的结果数目。
	 * 
	 * @return 查询每页的结果数目。
	 */
	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 设置查询关键字。
	 * 
	 * @param queryString
	 *            查询关键字。
	 */
	public void setQueryString(java.lang.String queryString) {
		this.stationQuery = queryString;
	}

	/**
	 * 设置查询每页的结果数目。
	 * 
	 * @param pageSize
	 *            新的查询条件。默认每页显示10条结果。
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;

	}

	/**
	 * 返回查询类型 。
	 * 
	 * @return 查询类型 。
	 */
	public StopSearchType getCategory() {
		return category;
	}

	/**
	 * 设置查询类型。
	 * 
	 * @param category
	 *            - 查询类型{@link BusStationQuery.StopSearchType}
	 */
	public void setCategory(StopSearchType category) {
		this.category = category;
	}

	/**
	 * 设置查询第几页。
	 * 
	 * @param pageNumber
	 *            查询第几页。
	 */
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	protected BusStationQuery clone() {
		return new BusStationQuery(this.stationQuery, category, this.city);

	}

	public int hashCode() {
		int i = 31;
		int j = 1;
		j = i * j + (this.category == null ? 0 : this.category.hashCode());
		j = i * j + (this.city == null ? 0 : this.city.hashCode());
		j = i * j + this.pageSize;
		j = i * j + this.pageNumber;
		j = i
				* j
				+ (this.stationQuery == null ? 0 : this.stationQuery.hashCode());
		return j;

	}

	/**
	 * 比较两个查询条件是否相同。
	 * 
	 * @param obj
	 *            查询条件。
	 * @return 查询条件是否相同。
	 */
	@Override
	public boolean equals(java.lang.Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BusStationQuery localBusStationQuery = (BusStationQuery) obj;
		if (this.pageNumber != localBusStationQuery.pageNumber)
			return false;
		if (this.city == null) {
			if (localBusStationQuery.city != null)
				return false;
		} else if (!this.city.equals(localBusStationQuery.city))
			return false;
		if (this.category != localBusStationQuery.category)
			return false;
		if (this.pageSize != localBusStationQuery.pageSize)
			return false;
		if (this.stationQuery == null) {
			if (localBusStationQuery.stationQuery != null)
				return false;
		} else if (!this.stationQuery.equals(localBusStationQuery.stationQuery))
			return false;
		return true;

	}

	protected boolean weakEquals(BusStationQuery busStationQuery) {
		boolean bool = false;
		if ((busStationQuery.getQueryString().equals(this.stationQuery))
				&& (busStationQuery.getCity().equals(this.city))
				&& (busStationQuery.getPageSize() == this.pageSize)
				&& (busStationQuery.getCategory().compareTo(this.category) == 0)) {
			bool = true;
		}
		return bool;

	}

	/**
	 * 定义公交站点搜索类型。
	 */
	public static enum StopSearchType {
		BY_STOP_ID, // 根据站点id搜索
		BY_STOP_NAME;// 根据站点名称搜索

	}

}
