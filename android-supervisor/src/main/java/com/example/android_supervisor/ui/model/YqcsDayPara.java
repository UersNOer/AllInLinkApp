package com.example.android_supervisor.ui.model;

import java.io.Serializable;
import java.util.List;

public class YqcsDayPara implements Serializable {


    /**
     * page : {"count":0,"current":0,"limit":0,"page":true}
     * queryStr : string
     * type : ["string"]
     */

    private Long createId;
    private PageBean page  = new PageBean();
    private String queryStr;
    private List<String> type ;

    public PageBean getPage() {
        return page;
    }

    public void setPage(PageBean page) {
        this.page = page;
    }

    public String getQueryStr() {
        return queryStr;
    }

    public void setQueryStr(String queryStr) {
        this.queryStr = queryStr;
    }

    public List<String> getType() {
        return type;
    }

    public void setType(List<String> type) {
        this.type = type;
    }

    public Long getCreateId() {
        return createId;
    }

    public void setCreateId(Long createId) {
        this.createId = createId;
    }

    public static class PageBean {
        /**
         * count : 0
         * current : 0
         * limit : 0
         * page : true
         */

        private int count = 50;
        private int current = 1;
        private int limit =50;
        private boolean page = true;

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public int getCurrent() {
            return current;
        }

        public void setCurrent(int current) {
            this.current = current;
        }

        public int getLimit() {
            return limit;
        }

        public void setLimit(int limit) {
            this.limit = limit;
        }

        public boolean isPage() {
            return page;
        }

        public void setPage(boolean page) {
            this.page = page;
        }
    }
}
