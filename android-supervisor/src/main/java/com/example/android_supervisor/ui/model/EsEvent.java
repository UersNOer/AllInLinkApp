package com.example.android_supervisor.ui.model;

import com.example.android_supervisor.utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;

public class EsEvent {

    private String esCode;

    private String esTitle;

    private String reportTime  = DateUtils.format(new Date(),0);

    private String reporter;

    private String phone;

    private Long typeId;

    private String typeName;


    private Long bigClassId;


    private String bigClassName;


    private Long smallClassId;


    private String smallClassName;

    private Long placeId;

    private String placeName;

    private String areaCode;

    private String areaName;


    private String esPosition;

    private String esDesc;

    private double geoX;

    private double geoY;

    private String dbStatus;

    private String createName;

    private String  createId;
    private String updateName;

    private int source = 82; // 案件来源 案件上报是4，快速上报是5


    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
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

    public String getReportTime() {
        return reportTime;
    }

    public void setReportTime(String reportTime) {
        this.reportTime = reportTime;
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getTypeId() {
        return typeId;
    }

    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Long getBigClassId() {
        return bigClassId;
    }

    public void setBigClassId(Long bigClassId) {
        this.bigClassId = bigClassId;
    }

    public String getBigClassName() {
        return bigClassName;
    }

    public void setBigClassName(String bigClassName) {
        this.bigClassName = bigClassName;
    }

    public Long getSmallClassId() {
        return smallClassId;
    }

    public void setSmallClassId(Long smallClassId) {
        this.smallClassId = smallClassId;
    }

    public String getSmallClassName() {
        return smallClassName;
    }

    public void setSmallClassName(String smallClassName) {
        this.smallClassName = smallClassName;
    }

    public Long getPlaceId() {
        return placeId;
    }

    public void setPlaceId(Long placeId) {
        this.placeId = placeId;
    }

    public String getPlaceName() {
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

    public String getUpdateName() {
        return updateName;
    }

    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }



    private String _id;

    private Geometry geometry;

    private WhProperties whProperties;


    private String id;
    private String ownerAreaCode;
    private String type;

    public static class Geometry{

        public double[] coordinates;

        public String type  = "Point";
    }

    public static class WhProperties {

        public String createId;
        public String areaCode;
        public String areaName;
        public String bgcode;
        public String datasource;
        public String dirName;
        public String dirPhone;
        public String dlmc;
        public ArrayList<ImgPath> imgPathList;

        public String note;
        public String objcode;
        public String objname;
        public String objpos;
        public String objstate;
        public String ordate = DateUtils.format(new Date(),0);

        public String picture;
        public String remake;
        public String type;
        public String typeName;
        public String x;
        public String y;
        public String yqFlag = "YQ";


        public static class ImgPath {

            public String filePath;


        }
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public WhProperties getWhProperties() {
        return whProperties;
    }

    public void setWhProperties(WhProperties whProperties) {
        this.whProperties = whProperties;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnerAreaCode() {
        return ownerAreaCode;
    }

    public void setOwnerAreaCode(String ownerAreaCode) {
        this.ownerAreaCode = ownerAreaCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
