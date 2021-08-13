package com.example.android_supervisor.ui.model;

import java.util.List;

/**
 * Created by Administrator on 2020/2/17.
 */
public class SjNoteRes {


    /**
     * id : 1229063367843495938
     * createId : 1174142738896048129
     * createTime : 2020-02-16 23:21:56
     * updateId : null
     * updateTime : null
     * esCode : 202002160001
     * esTitle : 疫2020字第02160001
     * reportTime : null
     * reporter : null
     * phone : null
     * typeId : null
     * typeName : null
     * bigClassId : null
     * bigClassName : null
     * smallClassId : 83
     * smallClassName : 人群聚集事件（聚餐、聚会等）
     * placeId : null
     * placeName : null
     * areaCode : 440309
     * areaName : 光明区
     * esPosition : 广东省深圳市光明区光明街道光安路
     * esDesc : 广场舞
     * geoX : 113.93820462515669
     * geoY : 22.763537303417305
     * dbStatus : 1
     * createName : yj
     * updateName : null
     * beforeImgUrls : [{"id":"1229063367868661762","createId":"1174142738896048129","createTime":"2020-02-16 23:21:56","updateId":null,"updateTime":null,"esEventId":"1229063367843495938","filePath":null,"fileUseType":"0","fileType":null,"dbStatus":"1","createName":"yj","updateName":null}]
     * afterImgUrls : [{"id":"1229063367885438978","createId":"1174142738896048129","createTime":"2020-02-16 23:21:56","updateId":null,"updateTime":null,"esEventId":"1229063367843495938","filePath":null,"fileUseType":"1","fileType":null,"dbStatus":"1","createName":"yj","updateName":null}]
     */

    private String id;
    private String createId;
    private String createTime;
    private Object updateId;
    private Object updateTime;
    private String esCode;
    private String esTitle;
    private String reportTime;
    private String reporter;
    private String phone;
    private Object typeId;
    private String typeName;
    private Object bigClassId;
    private Object bigClassName;
    private String smallClassId;
    private String smallClassName;
    private Object placeId;
    private String placeName;
    private String areaCode;
    private String areaName;
    private String esPosition;
    private String esDesc;
    private double geoX;
    private double geoY;
    private String dbStatus;
    private String createName;
    private String updateName;
    /**
     * id : 1229063367868661762
     * createId : 1174142738896048129
     * createTime : 2020-02-16 23:21:56
     * updateId : null
     * updateTime : null
     * esEventId : 1229063367843495938
     * filePath : null
     * fileUseType : 0
     * fileType : null
     * dbStatus : 1
     * createName : yj
     * updateName : null
     */

    private List<BeforeImgUrlsBean> beforeImgUrls;
    /**
     * id : 1229063367885438978
     * createId : 1174142738896048129
     * createTime : 2020-02-16 23:21:56
     * updateId : null
     * updateTime : null
     * esEventId : 1229063367843495938
     * filePath : null
     * fileUseType : 1
     * fileType : null
     * dbStatus : 1
     * createName : yj
     * updateName : null
     */

    private List<AfterImgUrlsBean> afterImgUrls;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Object getUpdateId() {
        return updateId;
    }

    public void setUpdateId(Object updateId) {
        this.updateId = updateId;
    }

    public Object getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Object updateTime) {
        this.updateTime = updateTime;
    }

    public String getEsCode() {
        return esCode;
    }

    public void setEsCode(String esCode) {
        this.esCode = esCode;
    }

    public String getEsTitle() {
        return esTitle;
    }

    public void setEsTitle(String esTitle) {
        this.esTitle = esTitle;
    }

    public Object getReportTime() {
        return reportTime;
    }

    public void setReportTime(String reportTime) {
        this.reportTime = reportTime;
    }

    public Object getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public Object getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Object getTypeId() {
        return typeId;
    }

    public void setTypeId(Object typeId) {
        this.typeId = typeId;
    }

    public Object getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Object getBigClassId() {
        return bigClassId;
    }

    public void setBigClassId(Object bigClassId) {
        this.bigClassId = bigClassId;
    }

    public Object getBigClassName() {
        return bigClassName;
    }

    public void setBigClassName(Object bigClassName) {
        this.bigClassName = bigClassName;
    }

    public String getSmallClassId() {
        return smallClassId;
    }

    public void setSmallClassId(String smallClassId) {
        this.smallClassId = smallClassId;
    }

    public String getSmallClassName() {
        return smallClassName;
    }

    public void setSmallClassName(String smallClassName) {
        this.smallClassName = smallClassName;
    }

    public Object getPlaceId() {
        return placeId;
    }

    public void setPlaceId(Object placeId) {
        this.placeId = placeId;
    }

    public Object getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getEsPosition() {
        return esPosition;
    }

    public void setEsPosition(String esPosition) {
        this.esPosition = esPosition;
    }

    public String getEsDesc() {
        return esDesc;
    }

    public void setEsDesc(String esDesc) {
        this.esDesc = esDesc;
    }

    public double getGeoX() {
        return geoX;
    }

    public void setGeoX(double geoX) {
        this.geoX = geoX;
    }

    public double getGeoY() {
        return geoY;
    }

    public void setGeoY(double geoY) {
        this.geoY = geoY;
    }

    public String getDbStatus() {
        return dbStatus;
    }

    public void setDbStatus(String dbStatus) {
        this.dbStatus = dbStatus;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public Object getUpdateName() {
        return updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }

    public List<BeforeImgUrlsBean> getBeforeImgUrls() {
        return beforeImgUrls;
    }

    public void setBeforeImgUrls(List<BeforeImgUrlsBean> beforeImgUrls) {
        this.beforeImgUrls = beforeImgUrls;
    }

    public List<AfterImgUrlsBean> getAfterImgUrls() {
        return afterImgUrls;
    }

    public void setAfterImgUrls(List<AfterImgUrlsBean> afterImgUrls) {
        this.afterImgUrls = afterImgUrls;
    }

    public static class BeforeImgUrlsBean {
        private String id;
        private String createId;
        private String createTime;
        private Object updateId;
        private Object updateTime;
        private String esEventId;
        private String filePath;
        private String fileUseType;
        private String fileType;
        private String dbStatus;
        private String createName;
        private Object updateName;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCreateId() {
            return createId;
        }

        public void setCreateId(String createId) {
            this.createId = createId;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public Object getUpdateId() {
            return updateId;
        }

        public void setUpdateId(Object updateId) {
            this.updateId = updateId;
        }

        public Object getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(Object updateTime) {
            this.updateTime = updateTime;
        }

        public String getEsEventId() {
            return esEventId;
        }

        public void setEsEventId(String esEventId) {
            this.esEventId = esEventId;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public String getFileUseType() {
            return fileUseType;
        }

        public void setFileUseType(String fileUseType) {
            this.fileUseType = fileUseType;
        }

        public String getFileType() {
            return fileType;
        }

        public void setFileType(String fileType) {
            this.fileType = fileType;
        }

        public String getDbStatus() {
            return dbStatus;
        }

        public void setDbStatus(String dbStatus) {
            this.dbStatus = dbStatus;
        }

        public String getCreateName() {
            return createName;
        }

        public void setCreateName(String createName) {
            this.createName = createName;
        }

        public Object getUpdateName() {
            return updateName;
        }

        public void setUpdateName(Object updateName) {
            this.updateName = updateName;
        }
    }

    public static class AfterImgUrlsBean {
        private String id;
        private String createId;
        private String createTime;
        private Object updateId;
        private String updateTime;
        private String esEventId;
        private String filePath;
        private String fileUseType;
        private String fileType;
        private String dbStatus;
        private String createName;
        private String updateName;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCreateId() {
            return createId;
        }

        public void setCreateId(String createId) {
            this.createId = createId;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public Object getUpdateId() {
            return updateId;
        }

        public void setUpdateId(Object updateId) {
            this.updateId = updateId;
        }

        public Object getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getEsEventId() {
            return esEventId;
        }

        public void setEsEventId(String esEventId) {
            this.esEventId = esEventId;
        }

        public Object getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public String getFileUseType() {
            return fileUseType;
        }

        public void setFileUseType(String fileUseType) {
            this.fileUseType = fileUseType;
        }

        public Object getFileType() {
            return fileType;
        }

        public void setFileType(String fileType) {
            this.fileType = fileType;
        }

        public String getDbStatus() {
            return dbStatus;
        }

        public void setDbStatus(String dbStatus) {
            this.dbStatus = dbStatus;
        }

        public String getCreateName() {
            return createName;
        }

        public void setCreateName(String createName) {
            this.createName = createName;
        }

        public Object getUpdateName() {
            return updateName;
        }

        public void setUpdateName(String updateName) {
            this.updateName = updateName;
        }
    }
}
