package com.example.android_supervisor.entities;

import java.io.Serializable;
import java.util.List;

public class CheckStatusRes implements Serializable {


    /**
     * id : 1187192190321184769
     * createId : 1000000000000000001
     * createTime : 2019-10-24 10:20:50
     * updateId : null
     * updateTime : 2019-10-24 14:14:41
     * userId : 1174142738896048129
     * chkId : 1187192188555382785
     * areaCode : 440309
     * replyStatus : 1
     * replyContent : vbbbjj
     * isNotify : 1
     * auditType : 1
     * auditOpinion : 合格事件的看法技术开发技术开发
     * dbStatus : 1
     * replyTime : 2019-10-24
     * replyLocation : 湖南省长沙市岳麓区旺龙路588号靠近高新区创业基地1期
     * absX : 112.8750599501
     * absY : 28.2295128038
     * userName : yj
     * chkName : sdsfdsdf
     * files : []
     */

    private String id;
    private String createId;
    private String createTime;
    private Object updateId;
    private String updateTime;
    private String userId;
    private String chkId;
    private String areaCode;
    private String replyStatus;
    private String replyContent;
    private String isNotify;
    private String auditType;
    private String auditOpinion;
    private String dbStatus;
    private String replyTime;
    private String replyLocation;
    private double absX;
    private double absY;
    private String userName;
    private String chkName;
    private List<?> files;

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

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getChkId() {
        return chkId;
    }

    public void setChkId(String chkId) {
        this.chkId = chkId;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getReplyStatus() {
        return replyStatus;
    }

    public void setReplyStatus(String replyStatus) {
        this.replyStatus = replyStatus;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public String getIsNotify() {
        return isNotify;
    }

    public void setIsNotify(String isNotify) {
        this.isNotify = isNotify;
    }

    public String getAuditType() {
        return auditType;
    }

    public void setAuditType(String auditType) {
        this.auditType = auditType;
    }

    public String getAuditOpinion() {
        return auditOpinion;
    }

    public void setAuditOpinion(String auditOpinion) {
        this.auditOpinion = auditOpinion;
    }

    public String getDbStatus() {
        return dbStatus;
    }

    public void setDbStatus(String dbStatus) {
        this.dbStatus = dbStatus;
    }

    public String getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(String replyTime) {
        this.replyTime = replyTime;
    }

    public String getReplyLocation() {
        return replyLocation;
    }

    public void setReplyLocation(String replyLocation) {
        this.replyLocation = replyLocation;
    }

    public double getAbsX() {
        return absX;
    }

    public void setAbsX(double absX) {
        this.absX = absX;
    }

    public double getAbsY() {
        return absY;
    }

    public void setAbsY(double absY) {
        this.absY = absY;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getChkName() {
        return chkName;
    }

    public void setChkName(String chkName) {
        this.chkName = chkName;
    }

    public List<?> getFiles() {
        return files;
    }

    public void setFiles(List<?> files) {
        this.files = files;
    }
}
