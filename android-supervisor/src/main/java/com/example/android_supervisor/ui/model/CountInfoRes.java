package com.example.android_supervisor.ui.model;

import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;

import java.io.Serializable;

@SmartTable(name="健康信息列表")
public class CountInfoRes implements Serializable {


    /**
     * id : 1228530290244636673
     * createId : 10001
     * createTime : 2020-02-15 12:03:40
     * updateId : null
     * updateTime : null
     * parentId : 1228530289468690433
     * areaCode : 440309
     * areaName : 光明区
     * placeId : 1228281231834087400
     * placeName : 芙蓉新盛
     * personName : 李赵
     * phone : 18273832519
     * idCard : 432524199112134980
     * personCensus : 湖南长沙
     * currentAddress : 湖南长沙岳麓区
     * temperature : 37.3
     * discomfort : 0
     * whAddress : 武汉大学
     * leftWhDate : 2019-12-12 10:00:00
     * vehicleType : 车次
     * vehicleInfo : G226
     * stopAddress : null
     * isTouch : 1
     * colleagueNames : null
     * personTypeName : 武汉入湘
     * endTime : 2019-12-26 10:00:00
     * remark : null
     * dbStatus : 1
     * createName : 康胜
     * updateName : null
     */

    private String id;
    private String createId;
    private String createTime;
    private Object updateId;
    private Object updateTime;
    private String parentId;
    private String areaCode;
    private String areaName;
    private long placeId;


    private String placeName;

    @SmartColumn(id =1,name = "姓名")
    private String personName;
    @SmartColumn(id =2,name = "号码")
    private String phone;
    @SmartColumn(id =3,name = "身份证号码")
    private String idCard;
    @SmartColumn(id =4,name = "户籍")
    private String personCensus;
    @SmartColumn(id =5,name = "当前居住地址")
    private String currentAddress;
    @SmartColumn(id =6,name = "当前居住地址")
    private double temperature;
    @SmartColumn(id =7,name = "有无咳嗽等不良症状")
    private String discomfort;
    @SmartColumn(id =8,name = "武汉当前居住地址")
    private String whAddress;
    @SmartColumn(id =9,name = "离开武汉的时间")
    private String leftWhDate;

    private String vehicleType;
    @SmartColumn(id =10,name = "交通工具信息")
    private String vehicleInfo;
    private Object stopAddress;

    @SmartColumn(id =11,name = "  有无和武汉人员进行接触")
    private String isTouch;
    private Object colleagueNames;
    private String personTypeName;
    private String endTime;
    private Object remark;
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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
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

    public long getPlaceId() {
        return placeId;
    }

    public void setPlaceId(long placeId) {
        this.placeId = placeId;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public String getPersonCensus() {
        return personCensus;
    }

    public void setPersonCensus(String personCensus) {
        this.personCensus = personCensus;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public String getDiscomfort() {
        return discomfort;
    }

    public void setDiscomfort(String discomfort) {
        this.discomfort = discomfort;
    }

    public String getWhAddress() {
        return whAddress;
    }

    public void setWhAddress(String whAddress) {
        this.whAddress = whAddress;
    }

    public String getLeftWhDate() {
        return leftWhDate;
    }

    public void setLeftWhDate(String leftWhDate) {
        this.leftWhDate = leftWhDate;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleInfo() {
        return vehicleInfo;
    }

    public void setVehicleInfo(String vehicleInfo) {
        this.vehicleInfo = vehicleInfo;
    }

    public Object getStopAddress() {
        return stopAddress;
    }

    public void setStopAddress(Object stopAddress) {
        this.stopAddress = stopAddress;
    }

    public String getIsTouch() {
        return isTouch;
    }

    public void setIsTouch(String isTouch) {
        this.isTouch = isTouch;
    }

    public Object getColleagueNames() {
        return colleagueNames;
    }

    public void setColleagueNames(Object colleagueNames) {
        this.colleagueNames = colleagueNames;
    }

    public String getPersonTypeName() {
        return personTypeName;
    }

    public void setPersonTypeName(String personTypeName) {
        this.personTypeName = personTypeName;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Object getRemark() {
        return remark;
    }

    public void setRemark(Object remark) {
        this.remark = remark;
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
