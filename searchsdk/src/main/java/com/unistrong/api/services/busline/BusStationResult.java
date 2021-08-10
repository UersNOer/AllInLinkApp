package com.unistrong.api.services.busline;

import java.util.List;

/**
 * 公交站点搜索结果是分页显示的，从第0页开始。
 * 
 */
public class BusStationResult {
	/**
	 * 查询条件。
	 */
	private BusStationQuery busStationQuery;
	/**
	 * 搜索建议的城市集合。
	 */
	private List<java.lang.String> stringList;
	/**
	 * 总页数。。
	 */
	private int pageCount;
    /**
     * 总结果数。
     */
    private int total;

	/**
	 * 公交站点信息集合。
	 */
	private List<BusStationItem> busStationItem;

	/**
	 * 返回查询参数。
	 * 
	 * @return 该结果对应的查询参数。
	 */
	public BusStationQuery getQuery() {
		return busStationQuery;
	}


	/**
	 * 设置搜索建议结果集合。
	 * 
	 * @param stringList
	 *            搜索建议结果集合。
	 */
	public void setSearchSuggestionKeywordst(List<java.lang.String> stringList) {
		this.stringList = stringList;
	}

	/**
	 * 返回该结果的总页数。
	 * 
	 * @return 该结果的总页数。
	 */
	public int getPageCount() {
        int i = getTotal() / this.busStationQuery.getPageSize();
        int pageCount = this.total % this.busStationQuery.getPageSize() > 0 ? i + 1 : i;

        return pageCount;
	}

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

	/**
	 * 设置查询条件。
	 * 
	 * @param busStationQuery
	 *            查询条件。
	 */
	public void setQuery(BusStationQuery busStationQuery) {
		this.busStationQuery = busStationQuery;
	}

	/**
	 * 返回当前页对应的站点信息结果。
	 * 
	 * @return 一个BusStationItem 的列表，其中每一项代表一个BusStation。
	 */
	public java.util.List<BusStationItem> getBusStations() {
		return busStationItem;

	}

	/**
	 * 设置当前页对应的结果。
	 * 
	 * @param busStationItem
	 *            公交站点信息。
	 */
	public void setBusStations(List<BusStationItem> busStationItem) {
		this.busStationItem = busStationItem;
	}

}
