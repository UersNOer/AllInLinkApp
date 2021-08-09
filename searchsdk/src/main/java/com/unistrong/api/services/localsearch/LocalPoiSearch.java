package com.unistrong.api.services.localsearch;

import android.content.Context;

import com.unistrong.api.services.core.UnistrongException;
import com.unistrong.api.services.localsearch.model.SearchResultInfo;
import java.util.ArrayList;
import java.util.List;

/**
 * POI本地搜索类。
 */
public class LocalPoiSearch {
    /**
     * 本地搜索构造器。
     * @param context 对应的Context。
     * @param snPath 本地鉴权文件根路径。
     * @param dataPath 本地搜索数据路径，POI文件夹的上一级目录。
     */
    public LocalPoiSearch(Context context, String snPath, String dataPath)throws UnistrongException{
        if(context != null){
            if(snPath!=null&&snPath.length()>0&&dataPath!=null&&dataPath.length()>0){
                LocalSearchAPI.getInstance().init(context,dataPath,snPath);
            }else{
                throw new UnistrongException(UnistrongException.ERROR_LOCAL_PATH);
            }
        }
    }

    /**
     * 释放本地搜索
     */
    public void destroy(){
        LocalSearchAPI.getInstance().exit();
    }

    /**
     * 根据坐标查询ADCODE。
     * @param longitude 坐标经度,如116.123
     * @param latitude 坐标经度,39.456
     * @return 可能获得的参考ADCODE,0表示查询失败,-1表示鉴权失败。
     */
    public int getAdcode(double longitude,double latitude){
       return LocalSearchAPI.getInstance().getAdcode(longitude,latitude);
    }
    /**
     * 搜索POI。
     * @param query 搜索条件
     * @return 搜索结果
     * @throws UnistrongException 异常
     */
    public LocalPoiResult searchPOI(LocalPoiSearch.Query query) throws UnistrongException{
        if (query==null){
            throw new UnistrongException(UnistrongException.ERROR_NULL_PARAMETER);
        }

        if(query.getSearchType() == SearchType.SEARCH_TYPE_POINAME || query.getSearchType() == SearchType.SEARCH_TYPE_ADDRNAME){
            return searchName(query);
        }
//        else if(query.getSearchType() == SearchType.SEARCH_TYPE_POIFIRSTLETTER || query.getSearchType() == SearchType.SEARCH_TYPE_ADDRFIRSTLETTER){
//            return searchFristLetter(query);
//        }
        return null;
    }
    private LocalPoiResult searchName(LocalPoiSearch.Query query) throws UnistrongException{
        int resetRes = 0;
        int count = 0;
        List<SearchResultInfo> resList = new ArrayList<SearchResultInfo>();
        LocalPoiResult poiResult = new LocalPoiResult(query,resList);
        LocalSearchAPI search = LocalSearchAPI.getInstance();

            resetRes = search.resetNameSearch(query.typeCode,query.adCode+"",query.adLevel,query.searchType);
            if(resetRes == -1){
                poiResult.setMessage(UnistrongException.ERROR_LOCAL_AUTH_FAILURE);
                throw new UnistrongException(UnistrongException.ERROR_LOCAL_AUTH_FAILURE);
            }
            if(resetRes == 1){
                count = search.inputName(query.query);
                if(count>0){
                    count = search.startNameSearch();

                    poiResult.setTotal(count);
                    int pageCount = poiResult.getPageCount();
                    if(count > 0 && query.pageNum<=pageCount){
                        int start = ( query.pageNum - 1 ) * query.pageSize;
                        int end = ( count < ( query.pageNum * query.pageSize ) ) ? count : ( query.pageNum * query.pageSize );
                        for( int i = start ; i < end ; i++ ){
                            resList.add(search.getNameRecordByIndex(i));
                        }
                    }
                }
            }
        return poiResult;
    }
    private LocalPoiResult searchFristLetter(LocalPoiSearch.Query query) throws UnistrongException{
        int resetRes = 0;
        int count = 0;
        List<SearchResultInfo> resList = new ArrayList<SearchResultInfo>();
        LocalPoiResult poiResult = new LocalPoiResult(query,resList);
        LocalSearchAPI search = LocalSearchAPI.getInstance();
        resetRes = search.resetFirstLetterSearch(query.typeCode,query.adCode+"",query.adLevel,query.searchType);
        if(resetRes == -1){
            poiResult.setMessage(UnistrongException.ERROR_LOCAL_AUTH_FAILURE);
            throw new UnistrongException(UnistrongException.ERROR_LOCAL_AUTH_FAILURE);
        }
        if(resetRes == 1){
            count = search.inputFirstLetters(query.query);
            if(count>0){
                count = search.startFirstLetterSearch();
                poiResult.setTotal(count);
                int pageCount = poiResult.getPageCount();
                if(count > 0 && query.pageNum<=pageCount){
                    int start = ( query.pageNum - 1 ) * query.pageSize;
                    int end = ( count < ( query.pageNum * query.pageSize ) ) ? count : ( query.pageNum * query.pageSize );
                    for( int i = start ; i < end ; i++ ){
                        resList.add(search.getFirstLetterRecordByIndex(i));
                    }
                }
            }
        }
        return poiResult;
    }
    /**
     * 此类定义了本地搜索的关键字，类别及城市编码和城市级别。
     */
    public static class Query {
        private String query;
        private String [] typeCode;
        private int adCode;
        private int adLevel;
        private int searchType;
        private int pageNum = 1;
        private int pageSize = 20;
        /**
         * 构造搜索器。
         * @param query 关键字
         * @param typeCodes POI类型
         * @param adCode 城市编码
         * @param adLevel 城市级别
         * @param searchType 搜索类型
         */
        public Query(String query,String [] typeCodes, int adCode, int adLevel, int searchType){
            this.query = query;
            this.typeCode = typeCodes;
            this.adCode = adCode;
            this.adLevel = adLevel;
            this.searchType = searchType;
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
         * @param pageNum
         *            查询第几页的结果，从1开始。
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
         * @param pageSize
         *            新的查询条件， 默认值是20 条,取值范围在1-50 条。
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
         * 获取搜索类型。
         * @return 返回搜索类型
         */
        public int getSearchType() {
            return searchType;
        }

        /**
         * 设置搜索类型。
         * @param searchType 搜索类型
         */
        public void setSearchType(int searchType) {
            this.searchType = searchType;
        }

        /**
         * 获取城市级别。
         * @return 返回城市级别
         */
        public int getAdLevel() {
            return adLevel;
        }

        /**
         * 设置城市级别。
         * @param adLevel 城市级别
         */
        public void setAdLevel(int adLevel) {
            this.adLevel = adLevel;
        }

        /**
         * 获取城市编码。
         * @return 返回城市编码
         */
        public int getAdCode() {
            return adCode;
        }

        /**
         * 设置城市编码。
         * @param adCode 城市编码
         */
        public void setAdCode(int adCode) {
            this.adCode = adCode;
        }

        /**
         * 获取搜索的POI类型范围。
         * @return POI类型
         */
        public String[] getTypeCode() {
            return typeCode;
        }

        /**
         * 设置搜索的POI类型范围。
         * @param typeCode POI类型
         */
        public void setTypeCode(String[] typeCode) {
            this.typeCode = typeCode;
        }
    }
}
