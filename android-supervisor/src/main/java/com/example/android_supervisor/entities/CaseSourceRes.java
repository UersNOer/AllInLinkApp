package com.example.android_supervisor.entities;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "t_CaseSources")
public class CaseSourceRes{


    /**
     * id : 1082814834963226626
     * createId : 0
     * createTime : 2019-01-09 09:42:29
     * updateId : 0
     * updateTime : 2019-03-15 03:19:33
     * parentCode : case_source
     * dictCode : 1
     * dictName : 公众举报
     * dictUrl : null
     * dictColour : null
     * remark : null
     * orderNum : 1
     * dbStatus : 1
     * dictType : 2
     * areaCode : null
     * parentName : 案件来源
     */

    private String id;
    private String createId;
    private String createTime;
    private String updateId;
    private String updateTime;
    private String parentCode;

    @DatabaseField(id = true)
    private String dictCode;

    @DatabaseField
    private String dictName;

    private Object dictUrl;
    private Object dictColour;
    private Object remark;
    private int orderNum;
    private String dbStatus;
    private String dictType;
    private Object areaCode;
    private String parentName;

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

    public String getUpdateId() {
        return updateId;
    }

    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getDictCode() {
        return dictCode;
    }

    public void setDictCode(String dictCode) {
        this.dictCode = dictCode;
    }

    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }

    public Object getDictUrl() {
        return dictUrl;
    }

    public void setDictUrl(Object dictUrl) {
        this.dictUrl = dictUrl;
    }

    public Object getDictColour() {
        return dictColour;
    }

    public void setDictColour(Object dictColour) {
        this.dictColour = dictColour;
    }

    public Object getRemark() {
        return remark;
    }

    public void setRemark(Object remark) {
        this.remark = remark;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public String getDbStatus() {
        return dbStatus;
    }

    public void setDbStatus(String dbStatus) {
        this.dbStatus = dbStatus;
    }

    public String getDictType() {
        return dictType;
    }

    public void setDictType(String dictType) {
        this.dictType = dictType;
    }

    public Object getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(Object areaCode) {
        this.areaCode = areaCode;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }
}
