package com.unistrong.api.services.localsearch;

import com.unistrong.api.services.localsearch.model.SearchResultInfo;

import java.util.List;

/**
 * 本地搜索结果。
 */
public class LocalPoiResult {
    private int total;
    private String message ="";
    private LocalPoiSearch.Query query;
    private List<SearchResultInfo> results;

    /**
     * 根据给定的参数构造一个PoiItem 的新对象。
     *
     * @param query
     *            查询参数。
     * @param results
     *            结果集合。
     */
    protected LocalPoiResult(LocalPoiSearch.Query query,List<SearchResultInfo> results) {
        this.query = query;
        this.results = results;
    }

    /**
     * 返回该结果对应的查询参数。
     *
     * @return 该结果对应的查询参数。
     */
    public LocalPoiSearch.Query getQuery() {
        return this.query;
    }


    /**
     * 返回当前页所有POI结果。
     *
     * @return 当前页所有POI结果。
     */
    public List<SearchResultInfo> getPois() {

        return this.results;
    }

    /**
     * 返回查询结果总数。
     *
     * @return 查询结果总数。
     */
    public int getTotal() {
        return this.total;
    }

    /**
     * 设置查询结果总数。
     *
     * @param total
     *            查询结果总数。
     */
    public void setTotal(int total) {
        this.total = total;
    }

    /**
     * 返回该结果的总页数。
     *
     * @return 该结果的总页数。
     */
    public int getPageCount() {
        int i = total / this.query.getPageSize();
        return this.total % this.query.getPageSize() > 0 ? i + 1 : i;
    }

    /**
     * 返回查询结果状态描述。
     *
     * @return 查询结果状态描述。
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * 设置查询结果状态描述。
     *
     * @param message
     *            查询结果状态描述。
     */
    public void setMessage(String message) {
        this.message = message;
    }

}
