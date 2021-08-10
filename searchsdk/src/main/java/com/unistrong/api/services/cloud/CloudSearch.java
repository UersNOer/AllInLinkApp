package com.unistrong.api.services.cloud;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.unistrong.api.services.core.CoreUtil;
import com.unistrong.api.services.core.LatLonPoint;
import com.unistrong.api.services.core.UnistrongException;
import com.unistrong.api.services.core.MessageManager;

import java.util.List;

/**
 * 云图数据检索的入口。
 */
public class CloudSearch {
    /**
     * 云图数据检索的查询条件。
     */
    private Query query;
    /**
     * 对应的Context。
     */
    private Context context;
    /**
     * 云图数据检索的回调接口。
     */
    private OnCloudSearchListener searchListener;
    /**
     * 异步通信工具。
     */
    private Handler handler;

    /**
     * 构造一个新的CloudSearch对象。
     * @param context
     *                对应的Context。
     */
    public CloudSearch(Context context) {
        this.context = context;
        handler = MessageManager.getInstance();
    }

    /**
     * 构造一个新的CloudSearch对象。
     * @param context
     *               对应的Context。
     * @param query
     *             查询条件。
     */
    public CloudSearch(Context context, Query query) {
        this.context = context;
        handler = MessageManager.getInstance();
        this.query = query;
    }

    /**
     * 返回查询条件。
     * @return Query对象。
     */
    public Query getQuery() {
        return query;
    }

    /**
     * 设置查询条件。
     * @param query
     *             对应的Query对象。
     */
    public void setQuery(Query query) {
        this.query = query;
    }

    /**
     * 设置云图数据检索的回调监听。
     * @param searchListener
     *                      云图数据检索的回调监听（OnCloudSearchListener对象）。
     */
    public void setSearchListener(OnCloudSearchListener searchListener) {
        this.searchListener = searchListener;
    }

    /**
     * BBox检索、条件检索以及id检索云图数据的异步接口。
     */
    public void searchDataAsyn(){
        new Thread(new Runnable() {
            public void run() {
                Message localMessage = MessageManager.getInstance()
                        .obtainMessage();
                MessageManager.CloudSearchWrapper locale = new MessageManager.CloudSearchWrapper();
                int status = 0;
                try {
                    if (query.isBBoxSearch()){
                        localMessage.arg1 = MessageManager.MESSAGE_TYPE_CLOUDBBOXSEARCH;
                    } else if (query.isConditionSearch()){
                        localMessage.arg1 = MessageManager.MESSAGE_TYPE_CLOUDCONDITIONSEARCH;
                    } else if (query.isIdSearch()){
                        localMessage.arg1 = MessageManager.MESSAGE_TYPE_CLOUDIDSEARCH;
                    }
                    localMessage.arg2 = 0;
                    localMessage.obj = locale;
                    locale.listener = CloudSearch.this.searchListener;
                    locale.result = CloudSearch.this.searchData();
                    if (locale.result != null){
                        status = locale.result.getStatus();
                        localMessage.arg2 = status;
                    }
                } catch (UnistrongException unistrongException) {
                    if (locale.result != null){
                        status = locale.result.getStatus();
                        localMessage.arg2 = status;
                    }
                    if (status != 0){
                        localMessage.arg2 = status;
                    } else {
                        localMessage.arg2 = unistrongException.getErrorCode();
                    }
                } finally {
                    if (null != CloudSearch.this.handler)
                        CloudSearch.this.handler.sendMessage(localMessage);
                }
            }
        }).start();
    }

    /**
     * BBox检索、条件检索以及id检索云图数据的同步接口。
     * @return CloudSearchResult
     *                          包含云图数据的结果封装。
     * @throws UnistrongException
     *                        异常信息。
     */
    public CloudSearchResult searchData() throws UnistrongException {
        CloudSearchServerHandler handler = new CloudSearchServerHandler(
                context, getQuery(), CoreUtil.getProxy(context), null);
        return handler.GetData();
    }

    /**
     * 此类定义了云图数据检索的参数。
     */
    public static class Query {
        /**
         * 数据集的唯一标识。
         */
        private long datasetId;
        /**
         * 过滤条件——sql语句,可用字段为id、adcode以及自定义字段，（ex:ls ='7' order by id desc(ls为自定义字段)）。
         */
        private String filter;
        /**
         * bbox（逗号分隔的一对经纬度代表一个坐标，用分号分割多个坐标）。
         */
        private List<LatLonPoint> bBox;
        /**
         * 当前页数。
         */
        private int pageNumber;
        /**
         * 每页条数。
         */
        private int pageSize;
        /**
         * 数据的唯一标识。
         */
        private long id;
        /**
         * 云图数据的检索类型：0是bbox检索，1是条件检索，2是id检索。
         */
        private SearchType searchType = SearchType.CLOUD_SEARCHTYPE_BBOX;



        /**
         * 构造一个云图数据检索类型为BBox检索的Query对象，仅包含所有的必填参数。
         * @param datasetId
         *                 数据集的唯一标识,必填。
         * @param bBox
         *            bbox（逗号分隔的一对经纬度代表一个坐标，用分号分割多个坐标）,必填。
         */
        public Query(long datasetId, List<LatLonPoint> bBox) {
            this(datasetId, "", bBox);
        }

        /**
         * 构造一个云图数据检索类型为BBox检索的Query对象，包含所有参数（必填和选填）。
         * @param datasetId
         *                 数据集的唯一标识,必填。
         * @param filter
         *               过滤条件——sql语句,可用字段为id、adcode以及自定义字段，
         *               （ex:ls ='7' order by id desc(ls为自定义字段)）。选填。
         * @param bBox
         *            bbox（逗号分隔的一对经纬度代表一个坐标，用分号分割多个坐标）,必填。
         */
        public Query(long datasetId, String filter, List<LatLonPoint> bBox) {
            this.datasetId = datasetId;
            this.filter = filter;
            this.bBox = bBox;
            this.searchType = SearchType.CLOUD_SEARCHTYPE_BBOX;
        }

        /**
         * 构造一个云图数据检索类型为条件检索的Query对象，仅包含所有的必填参数。
         * @param datasetId
         *                 数据集的唯一标识,必填。
         */
        public Query(long datasetId) {
            this(datasetId, "", 1, 10);
        }

        /**
         * 构造一个云图数据检索类型为条件检索的Query对象，包含所有的参数（必填和选填）。
         * @param datasetId
         *                 数据集的唯一标识,必填。
         * @param filter
         *               过滤条件——sql语句,可用字段为id、adcode以及自定义字段，
         *               （ex:ls ='7' order by id desc(ls为自定义字段)）。选填。
         * @param pageNumber
         *                  当前页数,选填(传0即可请求时忽略此参数)。
         * @param pageSize
         *                每页条数,选填(传0即可请求时忽略此参数)。
         */
        public Query(long datasetId, String filter, int pageNumber, int pageSize) {
            this.datasetId = datasetId;
            this.filter = filter;
            this.pageNumber = pageNumber;
            this.pageSize = pageSize;
            this.searchType = SearchType.CLOUD_SEARCHTYPE_CONDITION;
        }

        /**
         * 构造一个云图数据检索类型为id检索的Query对象，包含所有的参数。
         * @param datasetId
         *                 数据集的唯一标识,必填。
         * @param id
         *          数据的唯一标识。
         */
        public Query(long datasetId, long id) {
            this.datasetId = datasetId;
            this.id = id;
            this.searchType = SearchType.CLOUD_SEARCHTYPE_ID;
        }

        /**
         * 返回数据集的唯一标识。
         * @return 数据集的唯一标识。
         */
        public long getDatasetId() {
            return datasetId;
        }

        /**
         * 设置数据集的唯一标识。
         * @param datasetId
         *                 数据集的唯一标识。
         */
        public void setDatasetId(long datasetId) {
            this.datasetId = datasetId;
        }

        /**
         * 返回过滤条件。
         * @return 过滤条件。
         */
        public String getFilter() {
            return filter;
        }

        /**
         * 设置过滤条件。
         * @param filter
         *              过滤条件。
         */
        public void setFilter(String filter) {
            this.filter = filter;
        }

        /**
         * 返回bbox检索的坐标串。
         * @return bbox检索的坐标串。
         */
        public List<LatLonPoint> getbBox() {
            return bBox;
        }

        /**
         * 设置bbox检索的坐标串。
         * @param bBox
         *            bbox检索的坐标串。
         */
        public void setbBox(List<LatLonPoint> bBox) {
            this.bBox = bBox;
        }

        /**
         * 返回云图数据的检索类型。
         * @return 云图数据的检索类型。
         */
        public SearchType getSearchType() {
            return searchType;
        }

        /**
         * 设置云图数据的检索类型。
         * @param searchType
         *                  云图数据的检索类型。
         */
        void setSearchType(SearchType searchType) {
            this.searchType = searchType;
        }

        /**
         * 返回当前页码。
         * @return 当前页码。
         */
        public int getPageNumber() {
            return pageNumber;
        }

        /**
         * 设置当前页码。
         * @param pageNumber
         *                  当前页码。
         */
        public void setPageNumber(int pageNumber) {
            this.pageNumber = pageNumber;
        }

        /**
         * 返回每页数据的大小。
         * @return 每页数据的大小。
         */
        public int getPageSize() {
            return pageSize;
        }

        /**
         * 设置每页数据的大小。
         * @param pageSize
         *                每页数据的大小。
         */
        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        /**
         * 返回数据的唯一标识。
         * @return 数据的唯一标识。
         */
        public long getId() {
            return id;
        }

        /**
         * 设置数据的唯一标识。
         * @param id
         *          数据的唯一标识。
         */
        public void setId(long id) {
            this.id = id;
        }

        /**
         * 是否是云图数据的bbox检索。
         * @return true是bbox检索，false不是。
         */
        public boolean isBBoxSearch(){
            int searchType = this.getSearchType().getTypeInt();
            return SearchType.CLOUD_SEARCHTYPE_BBOX.ordinal() == searchType;
        }

        /**
         * 是否是云图数据的条件检索。
         * @return true是条件检索，false不是。
         */
        public boolean isConditionSearch(){
            int searchType = this.getSearchType().getTypeInt();
            return SearchType.CLOUD_SEARCHTYPE_CONDITION.ordinal() ==
                    searchType;
        }

        /**
         * 是否是云图数据的id检索。
         * @return true是id检索，false不是。
         */
        public boolean isIdSearch(){
            int searchType = this.getSearchType().getTypeInt();
            return SearchType.CLOUD_SEARCHTYPE_ID.ordinal() == searchType;
        }
    }

    /**
     * 云图数据检索的异步处理回调接口。
     */
    public static interface OnCloudSearchListener{
        /**
         * 返回云图数据bbox检索的异步处理结果。
         * @param result
         *              云图数据的搜索结果。
         * @param status
         *              返回结果成功或者失败的响应码，0表示成功，其他为失败。
         */
        public void onBBoxSearched(CloudSearchResult result, int status);

        /**
         * 返回云图数据条件检索的异步处理结果。
         * @param result
         *              云图数据的搜索结果。
         * @param status
         *              返回结果成功或者失败的响应码，0表示成功，其他为失败。
         */
        public void onConditionSearched(CloudSearchResult result, int status);

        /**
         * 返回云图数据id检索的异步处理结果。
         * @param result
         *               云图数据的搜索结果。
         * @param status
         *               返回结果成功或者失败的响应码，0表示成功，其他为失败。
         */
        public void onIDSearched(CloudSearchResult result, int status);
    }

    /**
     * 定义了云图数据的枚举搜索类型。
     */
    public static enum SearchType {
        /**
         * bbox检索。
         */
        CLOUD_SEARCHTYPE_BBOX(0),
        /**
         * 条件检索。
         */
        CLOUD_SEARCHTYPE_CONDITION(1),
        /**
         * id检索。
         */
        CLOUD_SEARCHTYPE_ID(2);

        private int searchType = 0;

        private SearchType(int type){
            searchType = type;
        }

        /**
         * 返回对应的枚举int值。
         * @return 对应的枚举int值，0是bbox检索，1是条件检索，2是id检索。
         */
        public int getTypeInt() {
            return searchType;
        }
    }
}
