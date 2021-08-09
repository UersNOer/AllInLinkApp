package com.unistrong.api.services.poisearch;

import com.unistrong.api.services.core.LatLonPoint;
import com.unistrong.api.services.poisearch.PoiSearch.Query;
import com.unistrong.api.services.poisearch.PoiSearch.SearchBound;

import java.util.ArrayList;

/**
 * POI（Point Of Interest，兴趣点）搜索结果是分页显示的，从第0页开始，每页最多50个POI。PoiResult
 * 封装了此分页结果，并且会缓存已经检索到的页的搜索结果。此类不可直接构造，只能通过调用类PoiSearch 的searchPOI()方法得到。
 */
public class PoiResult {
    private int total;
    private String message;
    private SearchBound bound;
    private Query query;
    private ArrayList<PoiItem> results;

    /**
     * 根据给定的参数构造一个PoiItem 的新对象。
     *
     * @param query   查询参数。
     * @param bound   查询范围。
     * @param results 结果集合。
     */
    protected PoiResult(Query query, SearchBound bound,
                        ArrayList<com.unistrong.api.services.poisearch.PoiItem> results) {
        this.bound = bound;
        this.query = query;
        this.results = results;
    }

    public PoiResult(XGPoiResult xgPoiResult) {
        results = new ArrayList<>();
        for (XGPoiItem item : xgPoiResult.getFeatures()) {
            PoiItem poiItem = new PoiItem(item.getId(), new LatLonPoint(item.getGeometry().getLatitude(), item.getGeometry().getLongitude()), item.getProperties().getPoiname(), item.getProperties().getAddress());
            results.add(poiItem);
        }

    }

    /**
     * 返回该结果对应的查询参数。
     *
     * @return 该结果对应的查询参数。
     */
    public Query getQuery() {
        return this.query;
    }

    /**
     * 返回该结果对应的范围参数。
     *
     * @return 该结果对应的范围参数。
     */
    public SearchBound getBound() {
        return this.bound;
    }

    /**
     * 返回当前页所有POI结果。
     *
     * @return 当前页所有POI结果。
     */
    public ArrayList<PoiItem> getPois() {

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
     * @param total 查询结果总数。
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
     * @param message 查询结果状态描述。
     */
    public void setMessage(String message) {
        this.message = message;
    }

}
