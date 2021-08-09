package com.unistrong.api.services.poisearch;


import android.os.Parcel;
import android.os.Parcelable;

import com.unistrong.api.services.busline.BusLineItem;
import com.unistrong.api.services.busline.BusStationItem;
import com.unistrong.api.services.district.DistrictItem;

import java.util.HashMap;
import java.util.List;

/**
 * 此类封装了综合搜索的结果。
 */
public class ComplexSearchResult implements Parcelable{

    private ComplexSearch.Query query;
    private int status;
    private String message;
    private int total;
    private List<PoiItem> poiItems;
    private List<BusStationItem> busStationItems;
    private List<BusLineItem> busLineItems;
    private List<DistrictItem> districtItems;
    private int pageCount;
    private HashMap<String, Object> extras = new HashMap<>();

    /**
     * 构造一个综合搜索结果的无参构造。
     */
    public ComplexSearchResult() {
    }

    /**
     * 构造一个综合搜索结果的对象。
     * @param query
     *             查询条件。
     * @param poiItems
     *                结果中包含的poi集合List<PoiItem>。
     * @param busStationItems
     *                       结果中包含的BusStationItem集合List<BusStationItem>。
     * @param busLineItems
     *                    结果中包含的BusLineItem集合List<BusLineItem>。
     * @param districtItems
     *                     结果中包含的DistrictItem集合List<DistrictItem>。
     */
    public ComplexSearchResult(ComplexSearch.Query query, List<PoiItem> poiItems,
                               List<BusStationItem> busStationItems,
                               List<BusLineItem> busLineItems, List<DistrictItem> districtItems) {
        this.query = query;
        this.poiItems = poiItems;
        this.busStationItems = busStationItems;
        this.busLineItems = busLineItems;
        this.districtItems = districtItems;
    }

    protected ComplexSearchResult(Parcel in) {
        message = in.readString();
        total = in.readInt();
        poiItems = in.createTypedArrayList(PoiItem.CREATOR);
        busStationItems = in.createTypedArrayList(BusStationItem.CREATOR);
        busLineItems = in.createTypedArrayList(BusLineItem.CREATOR);
        districtItems = in.createTypedArrayList(DistrictItem.CREATOR);
        pageCount = in.readInt();
        extras = in.readHashMap(HashMap.class.getClassLoader());
        status = in.readInt();
    }

    public static final Creator<ComplexSearchResult> CREATOR = new ComplexSearchResultCreator();

    /**
     * 返回当前的综合搜索查询条件对象。
     * @return 当前的综合搜索查询条件对象。
     */
    public ComplexSearch.Query getQuery() {
        return query;
    }

    void setQuery(ComplexSearch.Query query) {
        this.query = query;
    }

    /**
     * 返回请求的状态信息。
     * @return 请求的状态信息。
     */
    public String getMessage() {
        return message;
    }

    void setMessage(String message) {
        this.message = message;
    }

    /**
     * 返回搜索结果的总个数。
     * @return 搜索结果的总个数。
     */
    public int getTotal() {
        return total;
    }

    void setTotal(int total) {
        this.total = total;
    }

    /**
     * 返回结果中包含的poi集合。
     * @return 结果中包含的poi集合。
     */
    public List<PoiItem> getPoiItems() {
        return poiItems;
    }

    void setPoiItems(List<PoiItem> poiItems) {
        this.poiItems = poiItems;
    }

    /**
     * 返回结果中包含的BusStationItem集合。
     * @return 结果中包含的BusStationItem集合。
     */
    public List<BusStationItem> getBusStationItems() {
        return busStationItems;
    }

    void setBusStationItems(List<BusStationItem> busStationItems) {
        this.busStationItems = busStationItems;
    }

    /**
     * 返回结果中包含的BusLineItem集合。
     * @return 结果中包含的BusLineItem集合。
     */
    public List<BusLineItem> getBusLineItems() {
        return busLineItems;
    }

    void setBusLineItems(List<BusLineItem> busLineItems) {
        this.busLineItems = busLineItems;
    }

    /**
     * 返回结果中包含的DistrictItem集合。
     * @return
     */
    public List<DistrictItem> getDistrictItems() {
        return districtItems;
    }

    void setDistrictItems(List<DistrictItem> districtItems) {
        this.districtItems = districtItems;
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
     * 返回搜索结果的扩展信息。
     * @return 搜索结果的扩展信息,存储的value必须是基本类型。
     */
    public HashMap<String, Object> getExtras() {
        return extras;
    }

    /**
     * 设置搜索结果的扩展信息。
     * @param extras
     *              搜索结果的扩展信息,存储的value必须是基本类型。
     */
    public void setExtras(HashMap<String, Object> extras) {
        this.extras = extras;
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
        dest.writeInt(total);
        dest.writeTypedList(poiItems);
        dest.writeTypedList(busStationItems);
        dest.writeTypedList(busLineItems);
        dest.writeTypedList(districtItems);
        dest.writeInt(pageCount);
        dest.writeMap(extras);
        dest.writeInt(status);
    }
}
