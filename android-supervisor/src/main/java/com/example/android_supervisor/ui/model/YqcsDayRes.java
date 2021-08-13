package com.example.android_supervisor.ui.model;


import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

public class YqcsDayRes implements Serializable {


    /**
     * whDetailsList : [{"geometry":{"type":"Point","coordinates":[113.87022211968959,22.580418607051744]},"type":"WH_0599","objCode":"4403090599000004","bgcode":null,"objname":"小店铺","objState":null,"dataSource":null,"objpos":"广东省深圳市宝安区西乡街道廖春霞口腔诊所乐群社区","picture":".png","areaName":null,"areaCode":null,"dirName":"赵先生：修改联系人","dirPhone":"18475542838","remake":"店铺出现一例疫情","yqFlag":"YQ","typeName":null,"imgPathList":null,"id":"1227805543419084800","ordate":"2020-02-12"},{"geometry":{"type":"Point","coordinates":[113.94437188888632,22.757585266181607]},"type":"WH_0599","objCode":"44030599000005","bgcode":"440309001009003","objname":"小店铺","objState":null,"dataSource":null,"objpos":"广东省深圳市光明区光明街道侨新花园侨新花园(河心南路)","picture":null,"areaName":"光明","areaCode":"440309001009","dirName":"刘红华","dirPhone":"17321543652","remake":"测试测试测试的","yqFlag":"YQ","typeName":"小店铺","imgPathList":[{"filePath":"5e455940f0e81e3d84ff669d.jpg"}],"id":"1227958742578888704","ordate":"2020-02-13 22:11:49"},{"geometry":{"type":"Point","coordinates":[113.9431380121931,22.766488662228756]},"type":"WH_0599","objCode":"4403090599000008","bgcode":"440309001007006","objname":"小店铺","objState":null,"dataSource":null,"objpos":"广东省深圳市光明区光明街道竹华路","picture":null,"areaName":"翠湖","areaCode":"440309001007","dirName":"15","dirPhone":"18573181058","remake":"546","yqFlag":"YQ","typeName":"小店铺","imgPathList":[{"filePath":"5e465d6bf0e81e38fc730c48.jpg"}],"id":"1228238066188025856","ordate":null},{"geometry":{"type":"Point","coordinates":[113.9431380121931,22.766488662228756]},"type":"WH_0599","objCode":"4403090599000010","bgcode":"440309001007006","objname":"1121212","objState":null,"dataSource":null,"objpos":"广东省深圳市光明区光明街道竹华路","picture":null,"areaName":"翠湖","areaCode":"440309001007","dirName":"85","dirPhone":"18573181058","remake":"56464","yqFlag":"YQ","typeName":"小店铺","imgPathList":[{"filePath":"5e466105f0e81e38fc730c5c.jpg"}],"id":"1228242204300935168","ordate":null},{"geometry":{"type":"Point","coordinates":[113.93859689836111,22.768323728401853]},"type":"WH_0599","objCode":"4403090599000011","bgcode":"440309001007002","objname":"芙蓉兴盛","objState":null,"dataSource":null,"objpos":"广东省深圳市光明区光明街道光明社区健康服务中心光明医院社康管理中心","picture":null,"areaName":"翠湖","areaCode":"440309001007","dirName":"","dirPhone":"","remake":"场所","yqFlag":"YQ","typeName":"小店铺","imgPathList":[{"filePath":"5e4685a4f0e81e38fc730c62.jpg"}],"id":"1228281231834087424","ordate":"2020-02-14 19:31:41"},{"geometry":{"type":"Point","coordinates":[113.94326218046173,22.763010490453926]},"type":"WH_0599","objCode":"4403090599000012","bgcode":"440309001007007","objname":"快乐惠","objState":null,"dataSource":null,"objpos":"广东省深圳市光明区光明街道清怡小区","picture":null,"areaName":"翠湖","areaCode":"440309001007","dirName":"张军","dirPhone":"13423122321","remake":null,"yqFlag":"YQ","typeName":"小店铺","imgPathList":[{"filePath":"5e468923f0e81e38fc730c7c.png"}],"id":"1228284978282889216","ordate":"2020-02-14 19:48:14"},{"geometry":{"type":"Point","coordinates":[113.92930461110136,22.7662098837189]},"type":"WH_0599","objCode":"4403090599000013","bgcode":"440309001004007","objname":"小店铺","objState":null,"dataSource":null,"objpos":"广东省深圳市光明区光明街道光明大道428号","picture":null,"areaName":"东周","areaCode":"440309001004","dirName":"刘红华007","dirPhone":"17354214512","remake":null,"yqFlag":"YQ","typeName":"小店铺","imgPathList":[{"filePath":"5e46a468f0e81e38fc730c82.jpg"}],"id":"1228314257934778368","ordate":"2020-02-14 21:44:36"}]
     * page : {"current":1,"limit":10,"page":true,"count":7}
     */

    private PageBean page;
    private List<WhDetailsListBean> whDetailsList;

    public PageBean getPage() {
        return page;
    }

    public void setPage(PageBean page) {
        this.page = page;
    }

    public List<WhDetailsListBean> getWhDetailsList() {
        return whDetailsList;
    }

    public void setWhDetailsList(List<WhDetailsListBean> whDetailsList) {
        this.whDetailsList = whDetailsList;
    }

    public static class PageBean implements Serializable {
        /**
         * current : 1
         * limit : 10
         * page : true
         * count : 7
         */

        private int current;
        private int limit;
        private boolean page;
        private int count;

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

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }

    public static class WhDetailsListBean implements Serializable {
        /**
         * geometry : {"type":"Point","coordinates":[113.87022211968959,22.580418607051744]}
         * type : WH_0599
         * objCode : 4403090599000004
         * bgcode : null
         * objname : 小店铺
         * objState : null
         * dataSource : null
         * objpos : 广东省深圳市宝安区西乡街道廖春霞口腔诊所乐群社区
         * picture : .png
         * areaName : null
         * areaCode : null
         * dirName : 赵先生：修改联系人
         * dirPhone : 18475542838
         * remake : 店铺出现一例疫情
         * yqFlag : YQ
         * typeName : null
         * imgPathList : null
         * id : 1227805543419084800
         * ordate : 2020-02-12
         */

        private GeometryBean geometry;
        private String type;
        private String objCode;
        private String bgcode;
        private String objname;
        private Object objState;
        private Object dataSource;
        private String objpos;
        private String picture;
        private String areaName;
        private String areaCode;
        private String dirName;
        private String dirPhone;
        private String remake;
        private String yqFlag;
        private String typeName;
        private List<Img> imgPathList;
        private String id;
        private String ordate;

        public static class Img{

            private String filePath;

            public String getFilePath() {
                return filePath;
            }

            public void setFilePath(String filePath) {
                this.filePath = filePath;
            }
        }

        public GeometryBean getGeometry() {
            return geometry;
        }

        public void setGeometry(GeometryBean geometry) {
            this.geometry = geometry;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getObjCode() {
            return objCode;
        }

        public void setObjCode(String objCode) {
            this.objCode = objCode;
        }

        public Object getBgcode() {
            return bgcode;
        }

        public void setBgcode(String bgcode) {
            this.bgcode = bgcode;
        }

        public String getObjname() {
            return objname;
        }

        public void setObjname(String objname) {
            this.objname = objname;
        }

        public Object getObjState() {
            return objState;
        }

        public void setObjState(Object objState) {
            this.objState = objState;
        }

        public Object getDataSource() {
            return dataSource;
        }

        public void setDataSource(Object dataSource) {
            this.dataSource = dataSource;
        }

        public String getObjpos() {
            return objpos;
        }

        public void setObjpos(String objpos) {
            this.objpos = objpos;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }

        public Object getAreaName() {
            return areaName;
        }

        public void setAreaName(String areaName) {
            this.areaName = areaName;
        }

        public String getAreaCode() {
            return areaCode;
        }

        public void setAreaCode(String areaCode) {
            this.areaCode = areaCode;
        }

        public String getDirName() {
            return dirName;
        }

        public void setDirName(String dirName) {
            this.dirName = dirName;
        }

        public String getDirPhone() {
            return dirPhone;
        }

        public void setDirPhone(String dirPhone) {
            this.dirPhone = dirPhone;
        }

        public String getRemake() {
            return remake;
        }

        public void setRemake(String remake) {
            this.remake = remake;
        }

        public String getYqFlag() {
            return yqFlag;
        }

        public void setYqFlag(String yqFlag) {
            this.yqFlag = yqFlag;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        public List getImgPathList() {
            return imgPathList;
        }

        public void setImgPathList(List<Img> imgPathList) {
            this.imgPathList = imgPathList;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOrdate() {
            return ordate;
        }

        public void setOrdate(String ordate) {
            this.ordate = ordate;
        }

        public static class GeometryBean {
            /**
             * type : Point
             * coordinates : [113.87022211968959,22.580418607051744]
             */

            private String type;
            private List<Double> coordinates;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public List<Double> getCoordinates() {
                return coordinates;
            }

            public void setCoordinates(List<Double> coordinates) {
                this.coordinates = coordinates;
            }
        }

        @NonNull
        @Override
        public String toString() {
            return objname;
        }
    }
}
