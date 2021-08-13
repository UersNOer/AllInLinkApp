package com.example.android_supervisor.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "t_Layer_config")
public class LayerConfig implements Serializable {

    /**
     * id : 1146599645938462721
     * createId : 1070143724081586177
     * createTime : 2019-07-04 10:00:33
     * updateId : null
     * updateTime : 2019-07-17 17:33:55
     * layerCode : normal_case_icon
     * layerName : 正常案件
     * layerType : 1
     * layerUrl : 5d22dea8f7e5ae0bc8f569b9
     * remark : null
     * orderNum : null
     * dbStatus : 1
     */

    @DatabaseField(id = true)
    private String id;

    private String createId;

    private String createTime;
    private String updateId;
    private String updateTime;

    @DatabaseField()
    private String layerCode;

    @DatabaseField()
    private String layerName;

    private String layerType;

    @DatabaseField()
    private String layerUrl;

    private String remark;
    private String orderNum;
    private String dbStatus;

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


    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getLayerCode() {
        return layerCode;
    }

    public void setLayerCode(String layerCode) {
        this.layerCode = layerCode;
    }

    public String getLayerName() {
        return layerName;
    }

    public void setLayerName(String layerName) {
        this.layerName = layerName;
    }

    public String getLayerType() {
        return layerType;
    }

    public void setLayerType(String layerType) {
        this.layerType = layerType;
    }

    public String getLayerUrl() {
        return layerUrl;
    }

    public void setLayerUrl(String layerUrl) {
        this.layerUrl = layerUrl;
    }

    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getDbStatus() {
        return dbStatus;
    }

    public void setDbStatus(String dbStatus) {
        this.dbStatus = dbStatus;
    }
}
