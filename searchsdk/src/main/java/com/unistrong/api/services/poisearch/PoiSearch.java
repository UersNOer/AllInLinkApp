package com.unistrong.api.services.poisearch;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.unistrong.api.services.core.CoreUtil;
import com.unistrong.api.services.core.LatLonPoint;
import com.unistrong.api.services.core.UnistrongException;
import com.unistrong.api.services.core.MessageManager;

import java.util.List;

/**
 * 本类为POI（Point Of Interest，兴趣点）搜索的“入口”类，定义此类，开始搜索。在类PoiSearch 中，还定义了两个内部类，Query
 * 与SearchBound，请用这两个内部类设定搜索参数。
 */
public class PoiSearch {
    private SearchBound bound;
    private Query query;
    private Context context;
    private OnPoiSearchListener OnPoiSearchListener;
    private Handler handler = null;

    public PoiSearch(android.content.Context context) {
        this.context = context.getApplicationContext();
        this.handler = MessageManager.getInstance();
    }

    /**
     * 根据给定的参数构造一个PoiSearch 的新对象。
     *
     * @param context 对应的Context。
     * @param query   查询条件。
     */
    public PoiSearch(android.content.Context context, PoiSearch.Query query) {
        this.context = context.getApplicationContext();
        this.handler = MessageManager.getInstance();
        setQuery(query);
    }

    /**
     * 查询POI。
     *
     * @return 查询POI 的结果，结果是分页的，每页最多50 个。
     * @throws UnistrongException 此接口在网络连接出现问题的情况下，会抛出UnistrongException。
     * @brief 如果此时矩形查询（SearchBound）已定义，则搜索范围为该矩形内的符合条件的POI，否则如果此时查询条件（Query）
     * 中的行政区划代码已定义。
     * ，则搜索该城市（地区）内的所有符合条件的POI,如上述两个条件均未定义，则搜索范围为全国。为了性能及结果的清晰，
     * 强烈建议定义一个范围：矩形或行政区划代码均可。整个查询的语义为查找符合查询关键字，符合查询类型编码组合及在指定范围内的所有POI。
     */
    public XGPoiResult searchPOI() throws UnistrongException {

        return XGPoiSearchServerHandler.GetPoiData(query.query);

    }

    public interface returnPOIData {
        void setPOIData(PoiResult re);
    }


//    private boolean isEmpty(){
//        if(CoreUtil.IsEmptyOrNullString(getQuery().getLocation().toString())||CoreUtil.IsEmptyOrNullString(getQuery().getQueryString())
//                && CoreUtil.IsEmptyOrNullString(getQuery())){
//
//        }
//
//    }

    /**
     * 查询POI异步接口。
     *
     * @brief 查询POI异步接口。
     * 如果此时矩形查询（SearchBound）已定义，则搜索范围为该矩形内的符合条件的POI，否则如果此时查询条件（
     * Query）中的行政区划代码已定义
     * ，则搜索该城市（地区）内的所有符合条件的POI,如上述两个条件均未定义，则搜索范围为全国。建议定义一个范围
     * ：矩形或行政区划代码均可。整个查询的语义为查找符合查询关键字，符合查询类型编码组合及在指定范围内的指定POI。查询POI
     * 的结果，结果是分页的，每页最多50 个。 此方法为异步处理。
     */
    public void searchPOIAsyn() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Message localMessage = MessageManager.getInstance()
                            .obtainMessage();
                    try {
                        localMessage.arg1 = MessageManager.MESSAGE_TYPE_POISEARCH;
                        localMessage.arg2 = 0;
                        MessageManager.POIWrapper locale = new MessageManager.POIWrapper();
                        locale.listener = PoiSearch.this.OnPoiSearchListener;
                        locale.result = PoiSearch.this.searchPOI();
                        localMessage.obj = locale;
                    } finally {
                        if (null != PoiSearch.this.handler)
                            PoiSearch.this.handler.sendMessage(localMessage);
                    }
                } catch (UnistrongException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }


    /**
     * 已知poiid信息（点击地图底图），搜索POI的详细信息，同步查询接口。
     *
     * @param poiID poi的ID。
     * @return poi的信息，如POI名称、类型、城市编码等。
     * @throws UnistrongException 此接口在出现问题的情况下，会抛出UnistrongException。
     */
    public List<PoiItem> searchPOIId(java.lang.String poiID) throws UnistrongException {
        PoiIdSearchServerHandler handler = new PoiIdSearchServerHandler(context, poiID, CoreUtil.getProxy(context), null);
        return handler.GetData();
    }

    /**
     * 已知poiid信息（点击地图底图），搜索POI的详细信息，异步查询接口。
     *
     * @param poiID poi的ID。
     */
    public void searchPOIIdAsyn(final java.lang.String poiID) {
        new Thread(new Runnable() {
            public void run() {
                Message localMessage = MessageManager.getInstance().obtainMessage();
                try {
                    localMessage.arg1 = MessageManager.MESSAGE_TYPE_POIIDSEARCH;
                    localMessage.arg2 = 0;
                    MessageManager.POIIDWrapper locale = new MessageManager.POIIDWrapper();
                    localMessage.obj = locale;
                    locale.listener = PoiSearch.this.OnPoiSearchListener;
                    locale.result = PoiSearch.this.searchPOIId(poiID);
                } catch (UnistrongException unistrongException) {
                    localMessage.arg2 = unistrongException.getErrorCode();
                } finally {
                    if (null != PoiSearch.this.handler)
                        PoiSearch.this.handler.sendMessage(localMessage);
                }
            }
        }).start();

    }

    /**
     * 设置查询条件。
     *
     * @param query 新的查询条件。
     */
    public void setQuery(Query query) {
        this.query = query;
    }

    /**
     * 设置查询矩形。
     *
     * @param bnd 新的查询矩形。
     */
    public void setBound(SearchBound bnd) {
        this.bound = bnd;
    }

    /**
     * 返回查询条件。
     *
     * @return 查询条件。
     */

    public Query getQuery() {
        return this.query;
    }

    /**
     * 返回查询矩形。
     *
     * @return 查询矩形。
     */
    public SearchBound getBound() {
        return this.bound;
    }

    /**
     * 设置查询监听接口。
     *
     * @param listener 查询监听接口。
     */
    public void setOnPoiSearchListener(OnPoiSearchListener listener) {
        this.OnPoiSearchListener = listener;
    }

    /**
     * 此类定义了搜索的关键字，类别及城市。
     */
    public static class Query {
        private String query;
        // 详细程度，默认基本信息
        private int scope = 1;
        // 区域名称
        private String region;
        // 查询半径
        private int radius;
        // 周边中心坐标
        private LatLonPoint location;
        private int pageNum = 1;
        private int pageSize = 10;
        // poi类型。
        private String category;
        /**
         * 数据源，默认取值poi，可填用户自定义的数据源，多个数据源之间用“,”分隔，如：poi,mypoi。
         */
        private String datasource;

        /**
         * Query构造函数。
         *
         * @param query 查询字符串。
         * @brief 参数 city 必须定义，不能为空。
         */
        public Query(String query) {
            this(query, null, "");
        }

        /**
         * 根据给定的参数来构造一个 PoiSearch.Query 的新对象。
         *
         * @param query 查询字符串。
         * @param ctgr  POI 类型的组合。注:货车类型不可组合
         * @param city  待查询城市（地区）的城市名称（中文）。必设参数。
         * @brief 参数 city 必须定义，不能为空。
         */
        public Query(String query, String ctgr, String city) {
            this.query = query;
            this.region = city;
            this.category = ctgr;
        }

        private boolean isValidQuery() {
            return !CoreUtil.IsEmptyOrNullString(query) && !CoreUtil.IsEmptyOrNullString(region);
        }

        /**
         * 返回待查分类组合。
         *
         * @return 待查分类组合。
         */
        public String getCategory() {
            return category;
        }

        /**
         * 设置poi类型。
         *
         * @param category POI 类型的组合，比如定义如下组合：餐馆|电影院|景点
         */
        public void setCategory(String category) {
            this.category = category;
        }

        /**
         * 返回待查字符串。
         *
         * @return 待查字符串。
         */
        public String getQueryString() {
            return this.query;
        }

        /**
         * 返回结果的详细程度。
         *
         * @return 结果的详细程度。
         * @brief 1为基本信息，2为详细信息。
         */
        public int getScope() {
            return this.scope;
        }

        /**
         * 设置结果的详细程度。
         *
         * @param mScope 结果的详细程度。
         * @brief 1为基本信息，2为详细信息。
         */
        public void setScope(int mScope) {
            this.scope = mScope;
        }

        /**
         * 返回查询区域名称。
         *
         * @return 查询区域名称。
         */
        public String getRegion() {
            return this.region;
        }

        /**
         * 设置查询区域名称。
         *
         * @param region 查询区域名称。
         */
        public void setRegion(String region) {
            this.region = region;
        }

        /**
         * 返回查询半径，单位，米。
         *
         * @return 查询半径。
         */
        public int getRadius() {
            return this.radius;
        }

        /**
         * 设置查询半径，单位，米。
         *
         * @param radius 查询半径。
         */
        public void setRadius(int radius) {
            this.radius = radius;
        }

        /**
         * 返回周边查询的中心坐标。
         *
         * @return 中心坐标。
         */
        public LatLonPoint getLocation() {
            return this.location;
        }

        /**
         * 设置周边查询的中心坐标。
         *
         * @param location 中心坐标。
         */
        public void setLocation(LatLonPoint location) {
            this.location = location;
        }

        /**
         * 获取设置查询的是第几页，从1开始。
         *
         * @return 查询的是第几页。
         */
        public int getPageNum() {
            return this.pageNum;
        }

        /**
         * 设置查询第几页的结果数目。
         *
         * @param pageNum 查询第几页的结果，从1开始。
         */
        public void setPageNum(int pageNum) {
            this.pageNum = pageNum;
        }

        /**
         * 获取设置的查询页面的结果数目。
         *
         * @return 查询页面的结果数目。
         */
        public int getPageSize() {
            return this.pageSize;
        }

        /**
         * 设置查询每页的结果数目。
         *
         * @param pageSize 新的查询条件， 默认值是10 条,取值范围在1-50 条。
         */
        public void setPageSize(int pageSize) {
            if (pageSize < 0)
                this.pageSize = 1;
            else if (pageSize > 50)
                this.pageSize = 50;
            else
                this.pageSize = pageSize;
        }

        /**
         * <p><em>从V3.6.14增加此接口。</em></p>
         * 返回当前查询的数据源。
         *
         * @return 数据源。
         * @since 1.10.0
         */
        public String getDatasource() {
            return datasource;
        }

        /**
         * <p><em>从V3.6.14增加此接口。</em></p>
         * 设置当前查询的数据源。
         *
         * @param datasource 数据源，可以取默认值poi，也可填用户自定义的数据源，多个数据源之间用“,”分隔，
         *                   如：poi,mypoi。
         * @since 1.10.0
         */
        public void setDatasource(String datasource) {
            this.datasource = datasource;
        }

        public int hashCode() {
            int i = 31;
            int j = 1;
            j = i * j + (this.query == null ? 0 : this.query.hashCode());
            j = i * j + (this.region == null ? 0 : this.region.hashCode());
            j = i * j + (this.location == null ? 0 : this.location.hashCode());
            j = i * j + (this.datasource == null ? 0 : this.datasource.hashCode());
            j = i * j + this.pageNum;
            j = i * j + this.pageSize;
            j = i * j + this.radius;
            j = i * j + this.scope;
            return j;
        }

        /**
         * 比较两个查询条件是否相同（包括查询第几页）。
         *
         * @param obj - 查询条件。
         * @return 查询条件是否相同。
         */
        public boolean equals(java.lang.Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Query localQuery = (Query) obj;
            if (this.query == null) {
                if (localQuery.query != null)
                    return false;
            } else if (!this.query.equals(localQuery.query))
                return false;
            if (this.region == null) {
                if (localQuery.region != null)
                    return false;
            } else if (!this.region.equals(localQuery.region)) {
                return false;
            }

            if (this.location == null) {
                if (localQuery.location != null)
                    return false;
            } else if (!this.location.equals(localQuery.location))
                return false;

            if (this.datasource == null) {
                if (localQuery.datasource != null)
                    return false;
            } else if (!this.datasource.equals(localQuery.datasource))
                return false;

            if (this.scope != localQuery.scope)
                return false;

            if (this.radius != localQuery.radius) {
                return false;
            }
            if (this.pageNum != localQuery.pageNum) {
                return false;
            }
            if (this.pageSize != localQuery.pageSize) {
                return false;
            }
            return true;
        }

    }

    /**
     * 此类定义了查询圆形和查询矩形，查询返回的POI的位置在此圆形或矩形内。
     */
    public static class SearchBound {
        private LatLonPoint lowerLeft;
        private LatLonPoint upperRight;
        private int range; // 查询范围的半径
        private LatLonPoint centerPoint; // 中心点坐标
        private String shape; // 查询区域
        private List<LatLonPoint> polyGonList;//多边形的坐标点
        public static final String BOUND_SHAPE = "circle"; // 圆形区域
        public static final String POLYGON_SHAPE = "polygon"; // 多边形区域
        public static final String RECTANGLE_SHAPE = "rectangle"; // 矩形区域
//		public static final String ELLIPSE_SHAPE = "ellipse"; // 椭圆区域

        /**
         * 根据给定的参数来构造PoiSearch.SearchBound 的新对象。
         *
         * @param center         该范围的中心点。
         * @param radiusInMeters 半径，单位：米。
         */
        public SearchBound(LatLonPoint center, int radiusInMeters) {
            this.shape = BOUND_SHAPE;
            this.range = radiusInMeters;
            this.centerPoint = center;
//			centerToRect(center, CoreUtil.MeterToE6(radiusInMeters),
//					CoreUtil.MeterToE6(radiusInMeters));
            centerToRect(center, a(radiusInMeters),
                    a(radiusInMeters));

        }


        private static double a(int paramInt) {
            return paramInt / 111700.0D;
        }

        /**
         * 根据给定的参数来构造PoiSearch.SearchBound 的新对象。
         *
         * @param lowerLeft  矩形的左下角。
         * @param upperRight 矩形的右上角。
         * @brief 如果超出中国边界或者lowerLeft>=upperRight，则抛出IllegalArgumentException
         * 异常。
         */
        public SearchBound(LatLonPoint lowerLeft, LatLonPoint upperRight) {
            this.shape = RECTANGLE_SHAPE;
            doInit(lowerLeft, upperRight);
        }


//        /**
//         * 根据给定的参数来构造PoiSearch.SearchBound 的新对象。
//         *
//         * @brief 如果超出中国边界或者lowerLeft>=upperRight，则抛出IllegalArgumentException
//         *        异常。
//         * @param lowerLeft
//         *            矩形的左下角。
//         * @param upperRight
//         *            矩形的右上角。
//         */
//        public SearchBound(LatLonPoint lowerLeft, LatLonPoint upperRight,String shape) {
//            this.shape = ELLIPSE_SHAPE;
//            doInit(lowerLeft, upperRight);
//        }

        /**
         * 根据给定的参数来构造PoiSearch.SearchBound
         * 的新对象，默认由近到远排序。如果超出中国边界或者radiusInMeters<=0，则抛出IllegalArgumentException
         * 异常。
         *
         * @param center         该范围的中心点。
         * @param radiusInMeters 半径，单位：米。
         * @param isDistanceSort 是否按照距离排序。
         */
        private SearchBound(LatLonPoint center, int radiusInMeters,
                            boolean isDistanceSort) {

        }

        /**
         * 根据给定的参数来构造PoiSearch.SearchBound
         * 的新对象。如果超出中国边界或者不为多边形，则抛出IllegalArgumentException 异常。
         *
         * @param list 首尾相接的几何点，可以组成多边形。
         */
        public SearchBound(java.util.List<LatLonPoint> list) {
            this.shape = POLYGON_SHAPE;
            this.polyGonList = list;

        }


        private void doInit(LatLonPoint lowerLeft, LatLonPoint upperRight) {
            this.lowerLeft = lowerLeft;
            this.upperRight = upperRight;

            if (lowerLeft.getLatitude() >= upperRight.getLatitude()
                    || lowerLeft.getLongitude() >= upperRight.getLongitude()) {
//				throw new IllegalArgumentException("invalid rect ");
            }
            this.centerPoint = new LatLonPoint(
                    (lowerLeft.getLatitude() + upperRight.getLatitude()) / 2.0d,
                    (lowerLeft.getLongitude() + upperRight.getLongitude()) / 2.0d);
        }

        // 当半径查询时， 初始化左下角，右上角坐标，及跨度。
        private void centerToRect(LatLonPoint center, double latSpan, double lonSpan) {
            double halfLatSpan = latSpan / 2;
            double halfLonSpan = lonSpan / 2;
            double clat = center.getLatitude();
            double clon = center.getLongitude();
            doInit(new LatLonPoint(clat - halfLatSpan, clon - halfLonSpan),
                    new LatLonPoint(clat + halfLatSpan, clon + halfLonSpan));
        }

        /**
         * 返回首尾相接的几何点，可以组成多边形。
         *
         * @return 首尾相接的几何点，可以组成多边形。
         */
        public List<LatLonPoint> getPolyGonList() {
            return polyGonList;
        }

        /**
         * 设置首尾相接的几何点，可以组成多边形。
         *
         * @param polyGonList 首尾相接的几何点。
         */
        public void setPolyGonList(List<LatLonPoint> polyGonList) {
            this.polyGonList = polyGonList;
        }

        /**
         * 返回查询范围形状。
         *
         * @return 查询范围形状。
         */
        public String getShape() {
            return shape;
        }

        /**
         * 设置查询范围形状。
         *
         * @param shape 查询范围形状。
         */
        private void setShape(String shape) {
            this.shape = shape;
        }

        /**
         * 返回矩形左下角坐标。
         *
         * @return 矩形左下角坐标。
         */
        public LatLonPoint getLowerLeft() {
            return this.lowerLeft;
        }

        /**
         * 返回矩形右上角坐标。
         *
         * @return 矩形右上角坐标。
         */
        public LatLonPoint getUpperRight() {
            return this.upperRight;
        }

        /**
         * 返回矩形中心点坐标。
         *
         * @return 矩形中心点坐标。
         */
        public LatLonPoint getCenter() {
            return this.centerPoint;
        }

//		/**
//		 * 返回左下角和右下角坐标的字符串坐标。
//		 *
//		 * @return 字符串坐标。
//		 */
//		private String toString() {
//			return this.lowerLeft.toString() + ";" + this.upperRight.toString();
//		}

        /**
         * 返回矩形水平方向的间距，单位为米。
         *
         * @return 矩形水平方向的间距，单位为米。
         */
        public double getLonSpanInMeter() {

            if (!"rectangle".equals(getShape())) {
                return 0.0D;
            }
            return this.upperRight.getLongitude() - this.lowerLeft.getLongitude();
        }

        /**
         * 返回矩形竖直方向的间距，单位为米。
         *
         * @return 矩形竖直方向的间距，单位为米。
         */
        public double getLatSpanInMeter() {

            if (!"rectangle".equals(getShape())) {
                return 0.0D;
            }
            return this.upperRight.getLatitude() - this.lowerLeft.getLatitude();

        }

        /**
         * 返回查询半径 单位，米。
         *
         * @return 查询半径。
         */
        public int getRange() {
            return this.range;
        }

        public int hashCode() {
            int j = 1;
            int i = 31;
            j = i * j + (this.shape == null ? 0 : this.shape.hashCode());
            j = i * j
                    + (this.lowerLeft == null ? 0 : this.lowerLeft.hashCode());
            j = i
                    * j
                    + (this.upperRight == null ? 0 : this.upperRight.hashCode());
            j = i
                    * j
                    + (this.centerPoint == null ? 0 : this.centerPoint
                    .hashCode());
            j = i * j + this.range;
            return j;
        }

        // /**
        // * 返回是否按照距离排序。
        // * @return true 按照距离排序，false不按照距离排序。
        // */
        // public boolean isDistanceSort() {
        // return false;
        // }

        /**
         * 比较两个查询范围是否相同
         *
         * @param obj - 查询条件。
         * @return 查询范围是否相同。
         */
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            SearchBound localSearchBound = (SearchBound) obj;
            if (this.lowerLeft == null) {
                if (localSearchBound.lowerLeft != null)
                    return false;
            } else if (!this.lowerLeft.equals(localSearchBound.lowerLeft))
                return false;
            if (this.range != localSearchBound.range)
                return false;
            if (this.upperRight == null) {
                if (localSearchBound.upperRight != null)
                    return false;
            } else if (!this.upperRight.equals(localSearchBound.upperRight))
                return false;
            if (this.centerPoint == null) {
                if (localSearchBound.centerPoint != null)
                    return false;
            } else if (!this.centerPoint.equals(localSearchBound.centerPoint))
                return false;
            if (this.shape == null) {
                if (localSearchBound.shape != null)
                    return false;
            } else if (!this.shape.equals(localSearchBound.shape))
                return false;
            return true;
        }
    }

    /**
     * 本类为POI（Point Of Interest，兴趣点）搜索结果的异步处理回调接口。
     */
    public static interface OnPoiSearchListener {
        /**
         * poi id搜索的结果回调
         *
         * @param result    poi信息。
         * @param errorCode 返回结果成功或者失败的响应码。0为成功，其他为失败（详细信息参见网站开发指南-错误码对照表）。
         */
        public void onPoiItemSearched(List<XGPoiResult> result, int errorCode);

        /**
         * 返回POI搜索异步处理的结果。
         *
         * @param pageResult POI的搜索结果。
         * @param errorCode  返回结果成功或者失败的响应码。0为成功，其他为失败。
         */
        public void onPoiSearched(XGPoiResult pageResult, int errorCode);


    }
}
