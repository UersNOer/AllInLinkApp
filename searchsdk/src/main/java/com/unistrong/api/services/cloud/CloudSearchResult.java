package com.unistrong.api.services.cloud;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * 此类定义了一个云图数据的检索结果对象。
 */
public class CloudSearchResult implements Parcelable{

    /**
     * 查询条件。
     */
    private CloudSearch.Query query;
    /**
     * 返回的状态信息。
     */
    private String message;
    /**
     * 页码。
     */
    private int pageNum;
    /**
     * 每页数据大小。
     */
    private int pageSize;
    /**
     * 开始行。
     */
    private int startRow;
    /**
     * 结束行。
     */
    private int endRow;
    /**
     * 结果的总数(不是当前页)。
     */
    private long total;
    /**
     * 页数。
     */
    private int pages;
    /**
     * 查询到的当前页云图数据的集合（如果是id检索则只有一个元素，其他检索至少有一个结果）。
     */
    private List<CloudItem> results = new ArrayList<>();
    /**
     * 返回的状态码。
     */
    private int status;

    public static final Creator<CloudSearchResult> CREATOR = new CloudSearchResultCreator();

    /**
     * 构造一个云图数据的检索结果对象。
     */
    public CloudSearchResult() {
    }

    /**
     *  构造一个云图数据的检索结果对象。
     * @param query
     *             查询条件。
     * @param results
     *               包含云图数据的list。
     */
    public CloudSearchResult(CloudSearch.Query query, List<CloudItem> results) {
        this.query = query;
        this.results = results;
    }

    protected CloudSearchResult(Parcel in) {
        message = in.readString();
        pageNum = in.readInt();
        pageSize = in.readInt();
        startRow = in.readInt();
        endRow = in.readInt();
        total = in.readLong();
        pages = in.readInt();
        results = in.createTypedArrayList(CloudItem.CREATOR);
        status = in.readInt();
    }

    /**
     * 返回数据检索对应的查询对象。
     * @return 数据检索对应的查询对象。
     */
    public CloudSearch.Query getQuery() {
        return query;
    }

    /**
     * 返回请求的状态信息。
     * @return 请求的状态信息。
     */
    public String getMessage() {
        return message;
    }

    /**
     * 返回当前页码。
     * @return 当前页码。
     */
    public int getPageNum() {
        return pageNum;
    }

    /**
     * 返回每页大小。
     * @return 每页大小。
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * 返回开始行。
     * @return 开始行。
     */
    public int getStartRow() {
        return startRow;
    }

    /**
     * 返回结束行。
     * @return 结束行。
     */
    public int getEndRow() {
        return endRow;
    }

    /**
     * 返回结果的总数。
     * @return 结果的总数。
     */
    public long getTotal() {
        return total;
    }

    /**
     * 返回总页数。
     * @return 总页数。
     */
    public int getPages() {
        return pages;
    }

    /**
     * 返回检索到的CloudItem集合。
     * @return 检索到的CloudItem集合。
     */
    public List<CloudItem> getResults() {
        return results;
    }

    /**
     * 设置请求的状态信息。
     * @param message
     *               状态信息。
     */
    void setMessage(String message) {
        this.message = message;
    }

    /**
     * 设置当前页码。
     * @param pageNum
     *               页码。
     */
    void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    /**
     * 设置每页数据大小。
     * @param pageSize
     *                每页数据大小。
     */
    void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 设置开始行。
     * @param startRow
     *                开始行。
     */
    void setStartRow(int startRow) {
        this.startRow = startRow;
    }

    /**
     * 设置结束行。
     * @param endRow
     *              结束行。
     */
    void setEndRow(int endRow) {
        this.endRow = endRow;
    }

    /**
     * 设置数据总数。
     * @param total
     *             数据总数。
     */
    void setTotal(long total) {
        this.total = total;
    }

    /**
     * 设置总页数。
     * @param pages
     *             总页数。
     */
    void setPages(int pages) {
        this.pages = pages;
    }

    /**
     * 返回状态码。
     * @return 状态码。
     */
    public int getStatus() {
        return status;
    }

    void setStatus(int status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(message);
        dest.writeInt(pageNum);
        dest.writeInt(pageSize);
        dest.writeInt(startRow);
        dest.writeInt(endRow);
        dest.writeLong(total);
        dest.writeInt(pages);
        dest.writeTypedList(results);
        dest.writeInt(status);
    }
}
