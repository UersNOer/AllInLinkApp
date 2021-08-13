package com.example.android_supervisor.entities;

import java.io.Serializable;

public class LeaveRecordModel implements Serializable {


    /**
     * id : 1275008223732043777
     * createId : 0
     * createTime : 2020-06-22 18:10:24
     * updateId : null
     * updateTime : null
     * leavePeopleId : 1000000000000000001
     * leaveStartTime : 1592870400000
     * leaveEndTime : 1592870400000
     * leaveType : 0
     * leaveDuration : 1
     * leaveReason : 11111111111
     * leaveState : 0
     * auditRemark : null
     * auditPeopleId : null
     * auditTime : null
     * dbStatus : 1
     */

    private String id;
    private String createId;
    private String createTime;
    private Object updateId;
    private Object updateTime;
    private String leavePeopleId;
    private long leaveStartTime;
    private long leaveEndTime;
    private String leaveType;
    private long leaveDuration;
    private String leaveReason;
    private String leaveState;
    private Object auditRemark;
    private Object auditPeopleId;
    private Object auditTime;
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

    public void setUpdateId(Object updateId) {
        this.updateId = updateId;
    }

    public Object getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Object updateTime) {
        this.updateTime = updateTime;
    }

    public String getLeavePeopleId() {
        return leavePeopleId;
    }

    public void setLeavePeopleId(String leavePeopleId) {
        this.leavePeopleId = leavePeopleId;
    }

    public long getLeaveStartTime() {
        return leaveStartTime;
    }

    public void setLeaveStartTime(long leaveStartTime) {
        this.leaveStartTime = leaveStartTime;
    }

    public long getLeaveEndTime() {
        return leaveEndTime;
    }

    public void setLeaveEndTime(long leaveEndTime) {
        this.leaveEndTime = leaveEndTime;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public long getLeaveDuration() {
        return leaveDuration;
    }

    public void setLeaveDuration(int leaveDuration) {
        this.leaveDuration = leaveDuration;
    }

    public String getLeaveReason() {
        return leaveReason;
    }

    public void setLeaveReason(String leaveReason) {
        this.leaveReason = leaveReason;
    }

    public String getLeaveState() {
        return leaveState;
    }

    public void setLeaveState(String leaveState) {
        this.leaveState = leaveState;
    }

    public Object getAuditRemark() {
        return auditRemark;
    }

    public void setAuditRemark(Object auditRemark) {
        this.auditRemark = auditRemark;
    }

    public Object getAuditPeopleId() {
        return auditPeopleId;
    }

    public void setAuditPeopleId(Object auditPeopleId) {
        this.auditPeopleId = auditPeopleId;
    }

    public Object getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Object auditTime) {
        this.auditTime = auditTime;
    }

    public String getDbStatus() {
        return dbStatus;
    }

    public void setDbStatus(String dbStatus) {
        this.dbStatus = dbStatus;
    }
}
