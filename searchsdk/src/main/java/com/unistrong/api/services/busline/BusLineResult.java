package com.unistrong.api.services.busline;

import java.util.List;

/**
 * 公交线路搜索结果是分页显示的，从第0页开始。
 */
public class BusLineResult {
	/**
	 * 查询条件。
	 */
	private BusLineQuery query;
	/**
	 * 搜索建议的城市集合。
	 */
	private List<java.lang.String> stringList;
	/**
	 * 总页数。
	 */
	private int pageCount;
	/**
	 * 公交线路信息集合。
	 */
	private List<BusLineItem> busLineItem;
	/**
     * 返回结果的总数。
     */
    private int total;


	/**
	 * 返回该结果的总页数。
	 * 
	 * @return 该结果的总页数。
	 */
	public int getPageCount() {
        int i = total / this.query.getPageSize();
         int pageCount =this.total % this.query.getPageSize() > 0 ? i + 1 : i;
        return pageCount;
	}

	/**
	 * 返回当前页对应的结果。
	 * 
	 * @return 一个BusLineItem 的列表，其中每一项代表一个BusLine。
	 */
	public java.util.List<BusLineItem> getBusLines() {
		return busLineItem;

	}

	/**
	 * 返回该结果对应的查询参数。
	 * 
	 * @return 该结果对应的查询参数。
	 */
	public BusLineQuery getQuery() {
		return query;
	}

	/**
	 * 设置查询条件。
	 * 
	 * @param query
	 *            查询条件。
	 */
	public void setQuery(BusLineQuery query) {
		this.query = query;
	}

	/**
	 * 当前页对应的公交线路信息。
	 * 
	 * @param busLineItem
	 *            公交线路信息。
	 */
	public void setBusLines(List<BusLineItem> busLineItem) {
		this.busLineItem = busLineItem;
	}

	/**
	 * 设置该结果的总页数。
	 * 
	 * @param pageCount
	 *            结果的总页数。
	 */
//	void setPageCount(int pageCount) {
//		this.pageCount = pageCount;
//	}

	/**
	 * 返回结果的个数。
	 * 
	 * @return 结果的个数。
	 */
	public int getTotal() {
		return total;
	}

	/**
	 * 设置返回结果的总数。
	 * 
	 * @param total
	 *            返回结果的总数。
	 */
	public void setTotal(int total) {
		this.total = total;
	}

}
