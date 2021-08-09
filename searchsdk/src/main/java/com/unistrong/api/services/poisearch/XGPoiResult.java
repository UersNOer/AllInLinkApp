package com.unistrong.api.services.poisearch;

import java.util.ArrayList;

/**
 * POI（Point Of Interest，兴趣点）搜索结果是分页显示的，从第0页开始，每页最多50个POI。PoiResult
 * 封装了此分页结果，并且会缓存已经检索到的页的搜索结果。此类不可直接构造，只能通过调用类PoiSearch 的searchPOI()方法得到。
 */
public class XGPoiResult {
	private int total;
	private String message;
	private ArrayList<XGPoiItem> features;

	@Override
	public String toString() {
		return "XGPoiResult{" +
				"total=" + total +
				", message='" + message + '\'' +
				", features=" + features +
				'}';
	}

	public ArrayList<XGPoiItem> getFeatures() {
		return features;
	}

	public void setFeatures(ArrayList<XGPoiItem> features) {
		this.features = features;
	}

	protected XGPoiResult(
                          ArrayList<XGPoiItem> results) {
		this.features = results;
	}

	/**
	 * 返回该结果对应的查询参数。
	 *
	 * @return 该结果对应的查询参数。
	 */


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
