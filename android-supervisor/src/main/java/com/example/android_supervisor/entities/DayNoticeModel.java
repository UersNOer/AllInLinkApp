package com.example.android_supervisor.entities;

import android.text.TextUtils;

import java.io.Serializable;

public class DayNoticeModel implements Serializable {


    /**
     * id : 1283647411561959426
     * createId : null
     * createTime : 2020-07-16 14:19:27
     * updateId : null
     * updateTime : null
     * userId : 1214430769989029890
     * fillTime : null
     * fillContent : ggftt
     * fillState : 0
     * dbStatus : 1
     * userName : 朱向前
     */

    private String id;
    private Object createId;
    private String createTime;
    private Object updateId;
    private Object updateTime;
    private String userId;
    private Object fillTime;
    private String fillContent;
    private String fillState;
    private String dbStatus;
    private String userName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getCreateId() {
        return createId;
    }

    public void setCreateId(Object createId) {
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Object getFillTime() {
        return fillTime;
    }

    public void setFillTime(Object fillTime) {
        this.fillTime = fillTime;
    }

    public String getFillContent() {
        return fillContent;
    }

    public void setFillContent(String fillContent) {
        this.fillContent = fillContent;
    }

    public int getFillState() {
        if (TextUtils.isEmpty(fillState)){
            return 0;
        }
        return Integer.valueOf(fillState);
    }

    public void setFillState(String fillState) {
        this.fillState = fillState;
    }

    public String getDbStatus() {
        return dbStatus;
    }

    public void setDbStatus(String dbStatus) {
        this.dbStatus = dbStatus;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
