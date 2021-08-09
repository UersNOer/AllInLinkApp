package com.unistrong.api.services.busline;

/**
 * 此类定义了公交线路搜索的关键字、类别及城市。
 * 
 */
public class BusLineQuery {
	/**
	 * 公交线路查询名称。
	 */
	private String busQuery;
	/**
	 * 待查询城市。
	 */
	private String city;
	/**
	 * 每页记录数，最大值为50,默认为10。
	 */
	private int pageSize = 10;
	/**
	 * 分页页码，1为第一页，2为第二页,默认值为1。
	 */
	private int pageNumber = 1;
	/**
	 * 公交线路搜索类型,包括线路名称和线路id。
	 */
	private SearchType category;

	/**
	 * 构造函数。
	 */
	public BusLineQuery() {

	}

	/**
	 * 构造函数。
	 * 
	 * @param query
	 *            查询关键字。
	 * @param ctgr
	 *            查询类型:线路名称和线路id,{@link BusLineQuery.SearchType}。
	 * @param city
	 *            支持城市编码与中文名称形式。
	 */
	public BusLineQuery(java.lang.String query, BusLineQuery.SearchType ctgr,
			java.lang.String city) {
		this.busQuery = query;
		this.category = ctgr;
		this.city = city;

	}

	/**
	 * 构造函数。
	 * 
	 * @param query
	 *            查询关键字。
	 * @param ctgr
	 *            查询类型:线路名称和线路id,{@link BusLineQuery.SearchType}。
	 * @param city
	 *            c支持城市编码与中文名称形式。
	 * @param pageSize
	 *            每页记录数，最大值为50,默认是10。
	 * @param pageNumber
	 *            分页页码，1为第一页，2为第二页， 默认值为1。
	 */
	public BusLineQuery(java.lang.String query, BusLineQuery.SearchType ctgr,
			java.lang.String city, int pageSize, int pageNumber) {
		this.busQuery = query;
		this.category = ctgr;
		this.city = city;
		this.pageSize = pageSize;
		this.pageNumber = pageNumber;

	}

	/**
	 * 返回查询类型。
	 * 
	 * @return 查询类型 {@link BusLineQuery.SearchType}
	 */
	public BusLineQuery.SearchType getCategory() {
		return category;

	}

	/**
	 * 设置查询类型。
	 * 
	 * @param category
	 *            - 查询类型。
	 */
	public void setCategory(BusLineQuery.SearchType category) {
		this.category = category;
	}

	/**
	 * 返回查询关键字。
	 * 
	 * @return 该结果的查询关键字。
	 */
	public java.lang.String getQueryString() {
		return busQuery;

	}

	/**
	 * 设置查询关键字。
	 * 
	 * @param queryString
	 *            查询关键字。
	 */
	public void setQueryString(java.lang.String queryString) {
		this.busQuery = queryString;
	}

	/**
	 * 设置查询城市编码。
	 * 
	 * @return 查询城市编码。
	 */
	public java.lang.String getCity() {
		return city;

	}

	/**
	 * 设置查询城市参数。
	 * 
	 * @param city
	 *            查询城市参数。
	 */
	public void setCity(java.lang.String city) {
		this.city = city;
	}

	/**
	 * 返回查询数据的页码数。
	 * 
	 * @return 获得查询第几页的数据。
	 */
	public int getPageNumber() {
		return this.pageNumber;

	}

	/**
	 * 设置查询数据的页码数。
	 * 
	 * @param pageNumber
	 *            查询第几页的数据，从1开始。
	 */
	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
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
	 * 返回查询每页的结果数目。
	 * 
	 * @return 查询每页的结果数目。
	 */
	public int getPageSize() {
		return pageSize;
	}

	@Override
	protected BusLineQuery clone() {
		return new BusLineQuery(this.busQuery, category, this.city,
				this.pageSize, this.pageNumber);
	}

	@Override
	public int hashCode() {
		int i = 31;
		int j = 1;
		j = i * j + (this.category == null ? 0 : this.category.hashCode());
		j = i * j + (this.city == null ? 0 : this.city.hashCode());
		j = i * j + this.pageSize;
		j = i * j + this.pageNumber;
		j = i * j + (this.busQuery == null ? 0 : this.busQuery.hashCode());
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
		BusLineQuery localBusLineQuery = (BusLineQuery) obj;
		if (this.pageNumber != localBusLineQuery.pageNumber)
			return false;
		if (this.city == null) {
			if (localBusLineQuery.city != null)
				return false;
		} else if (!this.city.equals(localBusLineQuery.city))
			return false;
		if (this.category != localBusLineQuery.category)
			return false;
		if (this.pageSize != localBusLineQuery.pageSize)
			return false;
		if (this.busQuery == null) {
			if (localBusLineQuery.busQuery != null)
				return false;
		} else if (!this.busQuery.equals(localBusLineQuery.busQuery))
			return false;
		return true;

	}

	protected boolean weakEquals(BusLineQuery busLineQuery) {
		boolean bool = false;
		if ((busLineQuery.getQueryString().equals(this.busQuery))
				&& (busLineQuery.getCity().equals(this.city))
				&& (busLineQuery.getPageSize() == this.pageSize)
				&& (busLineQuery.getCategory().compareTo(this.category) == 0)) {
			bool = true;
		}
		return bool;

	}

	/**
	 * 定义公交线路搜索类型。
	 * 
	 */
	public static enum SearchType {
		BY_LINE_ID, // 根据线路id搜索
		BY_LINE_NAME;// 根据线路名称搜索

	}

}
