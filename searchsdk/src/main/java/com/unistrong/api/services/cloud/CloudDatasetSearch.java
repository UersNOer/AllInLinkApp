package com.unistrong.api.services.cloud;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.unistrong.api.services.core.CoreUtil;
import com.unistrong.api.services.core.UnistrongException;
import com.unistrong.api.services.core.MessageManager;

/**
 * 此类定义了云图数据集搜索的入口
 */
public class CloudDatasetSearch {
    /**
     * 云图数据集检索的查询条件。
     */
    private Query query;
    /**
     * 对应的Context。
     */
    private Context context;
    /**
     * 云图数据集检索的回调接口。
     */
    private OnCloudDatasetSearchListener searchListener;
    /**
     * 异步通信工具。
     */
    private Handler handler;

    /**
     * 构造一个新的CloudDatasetSearch对象。
     * @param context
     *                对应的Context。
     */
    public CloudDatasetSearch(Context context) {
        this.context = context;
        handler = MessageManager.getInstance();
    }

    /**
     *  构造一个新的CloudDatasetSearch对象。
     * @param context
     *                对应的Context。
     * @param query
     *             查询条件。
     */
    public CloudDatasetSearch(Context context, Query query) {
        this.context = context;
        handler = MessageManager.getInstance();
        this.query = query;
    }

    /**
     * 返回查询条件。
     * @return 查询条件的Query对象。
     */
    public Query getQuery() {
        return query;
    }

    /**
     * 设置查询条件。
     * @param query
     *             查询条件的Query对象。
     */
    public void setQuery(Query query) {
        this.query = query;
    }

    /**
     * 设置云图数据集检索的异步回调监听。
     * @param searchListener
     *                      云图数据集检索的异步回调监听。
     */
    public void setSearchListener(OnCloudDatasetSearchListener searchListener) {
        this.searchListener = searchListener;
    }

    /**
     *  云图数据集id检索、按条件分页检索、按条件全部检索的异步接口。
     */
    public void searchDatasetAsyn(){
        new Thread(new Runnable() {
            public void run() {
                Message localMessage = MessageManager.getInstance()
                        .obtainMessage();
                MessageManager.CloudDatasetSearchWrapper locale = new MessageManager.CloudDatasetSearchWrapper();
                int status = 0;
                try {
                    if (query.isIdSearch()){
                        localMessage.arg1 = MessageManager.MESSAGE_TYPE_CLOUDDATASET_SEARCHID;
                    } else if (query.isPageSearch()){
                        localMessage.arg1 = MessageManager.MESSAGE_TYPE_CLOUDDATASET_SEARCHPAGE;
                    } else if (query.isAllSearch()){
                        localMessage.arg1 = MessageManager.MESSAGE_TYPE_CLOUDDATASET_SEARCHALL;
                    }
                    localMessage.arg2 = 0;
                    localMessage.obj = locale;
                    locale.listener = CloudDatasetSearch.this.searchListener;
                    locale.result = CloudDatasetSearch.this.searchDataset();
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
                    if (null != CloudDatasetSearch.this.handler)
                        CloudDatasetSearch.this.handler.sendMessage(localMessage);
                }
            }
        }).start();
    }

    /**
     * 云图数据集id检索、按条件分页检索、按条件全部检索的同步接口。
     * @return 云图数据集的结果封装。
     * @throws UnistrongException
     *                        异常信息。
     */
    public CloudDatasetSearchResult searchDataset() throws UnistrongException {
        CloudDatasetSearchServerHandler handler = new CloudDatasetSearchServerHandler(
                context, getQuery(), CoreUtil.getProxy(context), null);
        return handler.GetData();
    }

    /**
     * 定义了云图数据的枚举搜索类型。
     */
    public static enum SearchType {
        /**
         * id检索。
         */
        CLOUD_DATASET_SEARCHTYPE_ID(0),
        /**
         * 按条件分页检索。
         */
        CLOUD_DATASET_SEARCHTYPE_PAGE(1),
        /**
         * 按条件查询全部。
         */
        CLOUD_DATASET_SEARCHTYPE_ALL(2);

        private int searchType = 0;

        private SearchType(int type){
            searchType = type;
        }

        /**
         * 返回对应的枚举int值。
         * @return 对应的枚举int值，0是id检索，1是按条件分页检索，2是按条件查询全部。
         */
        public int getTypeInt() {
            return searchType;
        }
    }

    /**
     * 此类定义了云图数据集检索的参数。
     */
    public static class Query {
        /**
         * 数据集的唯一标识。
         */
        private long id;
        /**
         * 用户id。
         */
        private long userId;
        /**
         * 数据集名称。
         */
        private String name;
        /**
         * 数据集类型：1--点，2--线，3--面。
         */
        private int geoType;
        /**
         * 起始页码。
         */
        private int pageNumber;
        /**
         * 每页显示条数。
         */
        private int pageSize;
        /**
         * 云图数据的检索类型：0是id检索，1是按条件分页检索，2是按条件查询全部。
         */
        private SearchType searchType = SearchType.CLOUD_DATASET_SEARCHTYPE_ID;

        /**
         * 构造一个云图数据集id检索的Query对象,包含所有的参数。
         * @param id
         *          数据集的唯一标识，必填。
         */
        public Query(long id) {
            this.id = id;
            this.searchType = SearchType.CLOUD_DATASET_SEARCHTYPE_ID;
        }

        /**
         *  构造一个云图数据集按条件分页检索的Query对象,包含所有的参数（所有的参数选填，
         *  无参数时默认返回第一页的10条数据集）。
         * @param id
         *          数据集的唯一标识，选填(传0即可请求时忽略此参数)。
         * @param userId
         *              用户id，选填(传0即可请求时忽略此参数)。
         * @param name
         *            数据集名称，选填(传""或者null即可请求时忽略此参数)。
         * @param geoType
         *               数据集类型：1--点，2--线，3--面，选填(传0即可请求时忽略此参数)。
         * @param pageNumber
         *                  要查询的起始页码，选填(传0即可请求时忽略此参数)。
         * @param pageSize
         *                每页显示条数，选填(传0即可请求时忽略此参数)。
         */
        public Query(long id, long userId, String name, int geoType, int pageNumber, int pageSize) {
            this.id = id;
            this.userId = userId;
            this.name = name;
            this.geoType = geoType;
            this.pageNumber = pageNumber;
            this.pageSize = pageSize;
            this.searchType = SearchType.CLOUD_DATASET_SEARCHTYPE_PAGE;
        }

        /**
         * 构造一个云图数据集按条件查询所有的Query对象，包含了所有的参数（所有的参数选填，
         * 默认返回该ak下所有创建的数据集）。
         * @param id
         *          数据集的唯一标识，选填(传0即可请求时忽略此参数)。
         * @param userId
         *              用户id，选填(传0即可请求时忽略此参数)。
         * @param name
         *            数据集名称，选填(传""或者null即可请求时忽略此参数)。
         * @param geoType
         *               数据集类型：1--点，2--线，3--面，选填(传0即可请求时忽略此参数)。
         */
        public Query(long id, long userId, String name, int geoType) {
            this.id = id;
            this.userId = userId;
            this.name = name;
            this.geoType = geoType;
            this.searchType = SearchType.CLOUD_DATASET_SEARCHTYPE_ALL;
        }

        /**
         * 返回数据集的唯一标识。
         * @return 数据集的唯一标识。
         */
        public long getId() {
            return id;
        }

        /**
         * 设置数据集的唯一标识。
         * @param id
         *          数据集的唯一标识。
         */
        public void setId(long id) {
            this.id = id;
        }

        /**
         * 返回用户id。
         * @return 用户id。
         */
        public long getUserId() {
            return userId;
        }

        /**
         * 设置用户id。
         * @param userId
         *              用户id。
         */
        public void setUserId(long userId) {
            this.userId = userId;
        }

        /**
         * 返回数据集名称。
         * @return 数据集名称。
         */
        public String getName() {
            return name;
        }

        /**
         * 设置数据集名称。
         * @param name
         *            数据集名称。
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * 返回数据集类型：1--点，2--线，3--面。
         * @return 数据集类型：1--点，2--线，3--面。
         */
        public int getGeoType() {
            return geoType;
        }

        /**
         * 设置数据集类型：1--点，2--线，3--面。
         * @param geoType
         *               数据集类型：1--点，2--线，3--面。
         */
        public void setGeoType(int geoType) {
            this.geoType = geoType;
        }

        /**
         * 返回要查询的起始页码。
         * @return 要查询的起始页码。
         */
        public int getPageNumber() {
            return pageNumber;
        }

        /**
         * 设置要查询的起始页码。
         * @param pageNumber
         *                  要查询的起始页码。
         */
        public void setPageNumber(int pageNumber) {
            this.pageNumber = pageNumber;
        }

        /**
         * 返回每页数据条数。
         * @return 每页数据条数。
         */
        public int getPageSize() {
            return pageSize;
        }

        /**
         * 设置每页数据条数。
         * @param pageSize
         *                每页数据条数。
         */
        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        /**
         * 返回云图数据集检索的类型枚举。
         * @return 云图数据集检索的类型枚举。
         */
        public SearchType getSearchType() {
            return searchType;
        }

        /**
         * 设置云图数据集检索的类型枚举。
         * @param searchType
         *                  云图数据集检索的类型枚举。
         */
        void setSearchType(SearchType searchType) {
            this.searchType = searchType;
        }

        /**
         * 是否是云图数据集的id检索。
         * @return true是id检索，false不是。
         */
        public boolean isIdSearch(){
            int searchType = this.getSearchType().getTypeInt();
            return searchType == SearchType.CLOUD_DATASET_SEARCHTYPE_ID.ordinal();
        }

        /**
         * 是否是云图数据集的按条件分页检索。
         * @return true是按条件分页检索，false不是。
         */
        public boolean isPageSearch(){
            int searchType = this.getSearchType().getTypeInt();
            return searchType == SearchType.CLOUD_DATASET_SEARCHTYPE_PAGE.ordinal();
        }

        /**
         * 是否是云图数据集的按条件查询全部。
         * @return true是按条件查询全部，false不是。
         */
        public boolean isAllSearch(){
            int searchType = this.getSearchType().getTypeInt();
            return searchType == SearchType.CLOUD_DATASET_SEARCHTYPE_ALL.ordinal();
        }
    }

    /**
     *  云图数据集检索的异步处理回调接口。
     */
    public static interface OnCloudDatasetSearchListener{
        /**
         *  返回云图数据集id检索的异步处理结果。
         * @param result
         *              云图数据集的搜索结果。
         * @param status
         *               返回结果成功或者失败的响应码，0表示成功，其他为失败。
         */
        public void onIDSearched(CloudDatasetSearchResult result, int status);

        /**
         * 返回云图数据集按条件分页检索的异步处理结果。
         * @param result
         *              云图数据集的搜索结果。
         * @param status
         *              返回结果成功或者失败的响应码，0表示成功，其他为失败。
         */
        public void onPageSearched(CloudDatasetSearchResult result, int status);

        /**
         * 返回云图数据集按条件检索全部的异步处理结果。
         * @param result
         *              云图数据集的搜索结果。
         * @param status
         *              返回结果成功或者失败的响应码，0表示成功，其他为失败。
         */
        public void onAllSearched(CloudDatasetSearchResult result, int status);
    }
}
