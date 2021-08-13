package com.example.android_supervisor.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "t_word")
public class WordRes implements Serializable {

    /**
     * id : 1152106164854054913
     * createId : 1085418907000426498
     * createTime : 2019-07-19 14:41:29
     * updateId : null
     * updateTime : 2019-11-19 09:39:50
     * type : 0
     * content : 受理结束
     * umFwNodeId : 1090481476101320750
     * isDefault : 1
     * orderNum : 0
     * dbStatus : 1
     */
    @DatabaseField(generatedId = true)
    private long id;

    private String createId;
    private String createTime;
    private Object updateId;
    private String updateTime;
    private String type;

    @DatabaseField
    private String content;
    private String umFwNodeId;
    private String isDefault;
    private int orderNum;
    private String dbStatus;

    @DatabaseField
    private boolean isCheck;

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUmFwNodeId() {
        return umFwNodeId;
    }

    public void setUmFwNodeId(String umFwNodeId) {
        this.umFwNodeId = umFwNodeId;
    }

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
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
}
