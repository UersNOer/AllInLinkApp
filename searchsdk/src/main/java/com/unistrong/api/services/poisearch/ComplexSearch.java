package com.unistrong.api.services.poisearch;


import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.unistrong.api.services.core.CoreUtil;
import com.unistrong.api.services.core.UnistrongException;
import com.unistrong.api.services.core.MessageManager;

/**
 * 此类定义了综合搜索的入口。
 */
public class ComplexSearch {

    /**
     * 综合搜索的查询条件。
     */
    private Query query;
    /**
     * 对应的Context。
     */
    private Context context;
    /**
     * 综合搜索的回调接口。
     */
    private OnComplexSearchListener searchListener;
    /**
     * 异步通信工具。
     */
    private Handler handler;

    /**
     * 构造一个综合搜索对象。
     * @param context
     *               对应的上下文。
     */
    public ComplexSearch(Context context) {
        this.context = context;
        handler = MessageManager.getInstance();
    }

    /**
     * 构造一个综合搜索对象。
     * @param context
     *               对应的上下文。
     * @param query
     *             查询条件。
     */
    public ComplexSearch(Context context, Query query) {
        this.context = context;
        handler = MessageManager.getInstance();
        setQuery(query);
    }

    /**
     * 返回当前的查询条件。
     * @return 当前的查询条件。
     */
    public Query getQuery() {
        return query;
    }

    /**
     * 设置查询条件。
     * @param query
     *             包含查询条件的Query对象。
     */
    public void setQuery(Query query) {
        this.query = query;
    }

    /**
     * 设置综合搜索的回调监听。
     * @param searchListener
     *                      综合搜索的回调监听。
     */
    public void setSearchListener(OnComplexSearchListener searchListener) {
        this.searchListener = searchListener;
    }

    /**
     * 综合搜索的异步接口。
     */
    public void searchDataAsyn(){
        new Thread(new Runnable() {
            public void run() {
                Message localMessage = MessageManager.getInstance()
                        .obtainMessage();
                MessageManager.ComplexSearchWrapper locale = new MessageManager.ComplexSearchWrapper();
                int status = 0;
                try {
                    localMessage.arg1 = MessageManager.MESSAGE_TYPE_COMPLEXSEARCH;
                    localMessage.arg2 = 0;
                    localMessage.obj = locale;
                    locale.listener = ComplexSearch.this.searchListener;
                    locale.result = ComplexSearch.this.searchData();
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
                    if (null != ComplexSearch.this.handler)
                        ComplexSearch.this.handler.sendMessage(localMessage);
                }
            }
        }).start();
    }

    /**
     * 综合搜索的同步接口。
     * @return ComplexSearchResult
     *                          包含综合搜索数据的结果封装。
     * @throws UnistrongException
     *                        异常信息。
     */
    public ComplexSearchResult searchData() throws UnistrongException {
        ComplexSearchServerHanler handler = new ComplexSearchServerHanler(
                context, getQuery(), CoreUtil.getProxy(context), null);
        return handler.GetData();
    }

    /**
     * 此类定义了综合搜索的请求参数对象。
     */
    public static class Query {

        /**
         * 所有类型的数据源名称。
         */
        public static final String DATASOURCE_TYPE_ALL = "bus,busline,poi,district";
        /**
         * poi的数据源名称。
         */
        public static final String DATASOURCE_TYPE_POI = "poi";
        /**
         * 公交站/地铁站的数据源名称。
         */
        public static final String DATASOURCE_TYPE_BUS = "bus";
        /**
         * 公交线/地铁线的数据源名称。
         */
        public static final String DATASOURCE_TYPE_BUSLINE = "busline";
        /**
         * 行政区域的数据源名称。
         */
        public static final String DATASOURCE_TYPE_DISTRICT = "district";

        /**
         * 关键字，或关键字的首字母、拼音。格式如，公园/gy/gongyuan。
         */
        private String query;
        /**
         * POI类型，多个POI类型之间用“|”分隔，格式如，医院|酒店……。
         */
        private String type;
        /**
         * 数据源类型，可选值如下：
         * district：行政区划,poi：兴趣点,bus：公交站点,busline：公交线路
         * 可选择多个，不同数据源之间用“,”分隔，格式，如：poi,bus,busline,distirct。
         * 默认值：poi。
         */
        private String datasource;
        /**
         * 检索的区域名称，可为城市、省份的名称或者“全国”。
         */
        private String region;
        /**
         * 周边检索的经纬度坐标,格式为：longitude,latitude。
         */
        private String location;
        /**
         * 搜索半径。取值范围0~50000，超过50000时，按默认值1000进行搜搜。
         * 单位，米。默认值为1000。
         */
        private int radius = 1000;
        /**
         * 每页记录数。 默认值为10。
         */
        private int pageSize = 10;
        /**
         * 分页页码，1为第一页。 默认值为1。
         */
        private int pageNum = 1;

        /**
         * 构造一个无参构造的Query对象。
         */
        public Query() {
        }

        /**
         * 构造一个综合搜索的Query对象。
         * @param query
         *             待查询的关键字，或关键字的首字母、拼音。格式如，公园/gy/gongyuan。
         *             query和type不可同时存在,query不为空时,type必须置空;
         *             当datasource为bus时,type和query可以同时为空,可以进行公交地铁站的分类查询。
         *             此参数选填,传入""或者null则请求时忽略此参数。
         * @param type
         *            POI类型，多个POI类型之间用“|”分隔，格式如，医院|酒店……。
         *            query和type不可同时存在,type不为空时,query必须置空;
         *            当datasource为bus时,type和query可以同时为空,可以进行公交地铁站的分类查询。
         *            此参数选填,传入""或者null则请求时忽略此参数。
         * @param datasource
         *                  数据源名称，必填不能为空，可查看常量定义：DATASOURCE_TYPE_ALL为所有类型,
         *                  DATASOURCE_TYPE_POI为poi,DATASOURCE_TYPE_BUS是公交地铁站的,
         *                  DATASOURCE_TYPE_BUSLINE为公交地铁线的,DATASOURCE_TYPE_DISTRICT是行政区划；
         *                  查询时各个数据源可随意组合,以","分隔,如"poi,bus"。
         * @param region
         *              检索的区域名称，可为城市、省份的名称或者“全国”;region和location不可同时存在,
         *              一般先以location发起周边查询,如果搜索不到结果再进行城市最后到全国的区域搜索。
         *              此参数选填,传入""或者null则请求时忽略此参数。
         * @param location
         *                周边检索的经纬度坐标,格式为："longitude,latitude";region和location不可同时存在,
         *              一般先以location发起周边查询,如果搜索不到结果再进行城市最后到全国的区域搜索。
         *              此参数选填,传入""或者null则请求时忽略此参数。
         * @param radius
         *              搜索半径。取值范围0~50000，超过50000时，按默认值1000进行搜搜。
         *              单位，米。默认值为1000。
         * @param page_size
         *                 每页记录数。 默认值为10。
         * @param page_num
         *                分页页码，1为第一页。 默认值为1。
         */
        public Query(String query, String type, String datasource, String region, String location,
                     int radius, int page_size, int page_num) {
            this.query = query;
            this.type = type;
            this.datasource = datasource;
            this.region = region;
            this.location = location;
            this.radius = radius;
            this.pageSize = page_size;
            this.pageNum = page_num;
        }

        /**
         * 返回关键字，或关键字的首字母、拼音。格式如，公园/gy/gongyuan。
         * @return  关键字，或关键字的首字母、拼音。格式如，公园/gy/gongyuan。
         */
        public String getQuery() {
            return query;
        }

        /**
         * 设置待查询的关键字。
         * @param query
         *             关键字，或关键字的首字母、拼音。格式如，公园/gy/gongyuan。
         */
        public void setQuery(String query) {
            this.query = query;
        }

        /**
         * 返回POI类型，主要用于分类查询,多个POI类型之间用“|”分隔，格式如，医院|酒店……。
         * @return  POI类型，主要用于分类查询,多个POI类型之间用“|”分隔，格式如，医院|酒店……。
         */
        public String getType() {
            return type;
        }

        /**
         * 设置POI类型，主要用于分类查询,多个POI类型之间用“|”分隔，格式如，医院|酒店……。
         * @param type
         *            POI类型，主要用于分类查询,多个POI类型之间用“|”分隔，格式如，医院|酒店……。
         */
        public void setType(String type) {
            this.type = type;
        }

        /**
         * 返回数据源名称。
         * @return 数据源名称。
         */
        public String getDatasource() {
            return datasource;
        }

        /**
         * 设置数据源名称。
         * @param datasource
         *                  数据源名称,不能为空。
         */
        public void setDatasource(String datasource) {
            this.datasource = datasource;
        }

        /**
         * 返回检索的区域名称，可为城市、省份的名称,如果是全国范围查询则返回“全国”。
         * @return  检索的区域名称，可为城市、省份的名称或者“全国”。
         */
        public String getRegion() {
            return region;
        }

        /**
         * 设置检索的区域名称，可为城市、省份的名称，如果是全国范围内查询则传入“全国”即可。
         * @param region
         *              检索的区域名称，可为城市、省份的名称,如果是全国范围内查询则传入“全国”即可。
         */
        public void setRegion(String region) {
            this.region = region;
        }

        /**
         * 返回周边检索的经纬度坐标,格式为：longitude,latitude。
         * @return 周边检索的经纬度坐标,格式为：longitude,latitude。
         */
        public String getLocation() {
            return location;
        }

        /**
         * 设置周边检索的经纬度坐标,格式为：longitude,latitude。
         * @param location
         *                周边检索的经纬度坐标,格式为：longitude,latitude。
         */
        public void setLocation(String location) {
            this.location = location;
        }

        /**
         * 返回搜索半径。取值范围0~50000，超过50000时，按默认值1000进行搜搜。
         * @return 搜索半径。取值范围0~50000，超过50000时，按默认值1000进行搜搜。
         *         单位，米。默认值为1000。
         */
        public int getRadius() {
            return radius;
        }

        /**
         * 设置搜索半径。
         * @param radius
         *              搜索半径。取值范围0~50000，超过50000时，按默认值1000进行搜搜。
         *              单位，米。默认值为1000。
         */
        public void setRadius(int radius) {
            this.radius = radius;
        }

        /**
         * 返回每页记录数。 默认值为10。
         * @return 每页记录数。 默认值为10。
         */
        public int getPageSize() {
            return pageSize;
        }

        /**
         * 设置每页记录数。 默认值为10。
         * @param pageSize
         *                 每页记录数。 默认值为10。
         */
        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        /**
         * 返回分页页码，1为第一页。 默认值为1。
         * @return  分页页码，1为第一页。 默认值为1。
         */
        public int getPageNum() {
            return pageNum;
        }

        /**
         * 设置分页页码，1为第一页。 默认值为1。
         * @param pageNum
         *                分页页码，1为第一页。 默认值为1。
         */
        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }
    }

    /**
     *  综合搜索的异步处理回调接口。
     */
    public static interface OnComplexSearchListener{
        /**
         * 返回综合搜索的异步处理结果。
         * @param result
         *              综合搜索的搜索结果。
         * @param status
         *              返回结果成功或者失败的响应码，0表示成功，其他为失败。
         */
        public void onComplexSearched(ComplexSearchResult result, int status);
    }
}
