package com.example.android_supervisor.ui.model;

import java.util.List;

public class CsDescRess {


    /**
     * geometry : {"type":"Point","coordinates":[113.87022211968959,22.580418607051744]}
     * type : WH_0599
     * objCode : 4403090599000004
     * bgcode : 440309001007002
     * objname : 常德牛肉粉
     * objState : null
     * dataSource : null
     * objpos : 广东省深圳市宝安区西乡街道廖春霞口腔诊所乐群社区
     * picture : .png
     * areaName : 翠湖
     * areaCode : 440309001007
     * dirName : 赵先生：修改联系人
     * dirPhone : 18475542838
     * remake : 店铺出现一例疫情
     * yqFlag : YQ
     * typeName : 小店铺
     * imgPathList : [{"filePath":"5e454c93f0e81e3d84ff668d.jpg"},{"filePath":"5e454c96f0e81e3d84ff668f.jpg"}]
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
    private String id;
    private String ordate;
    private List<ImgPathListBean> imgPathList;

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

    public String getBgcode() {
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

    public String getAreaName() {
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

    public List<ImgPathListBean> getImgPathList() {
        return imgPathList;
    }

    public void setImgPathList(List<ImgPathListBean> imgPathList) {
        this.imgPathList = imgPathList;
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

    public static class ImgPathListBean {
        /**
         * filePath : 5e454c93f0e81e3d84ff668d.jpg
         */

        private String filePath;

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }
    }
}
