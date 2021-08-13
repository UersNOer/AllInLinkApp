package com.example.android_supervisor.entities;

import com.google.gson.annotations.SerializedName;

/**
 * @author wujie
 */
public class CheckInRes {
    @SerializedName("shift")
    private String name;

    @SerializedName("onPunchTime")
    private String onTime;

    @SerializedName("offPunchTime")
    private String offTime;

    @SerializedName("onPunchAddress")
    private String onAddress;

    @SerializedName("offPunchAddress")
    private String offAddress;

    @SerializedName("punchStatus")
    private String status;

    private String onStatus;

    private String offStatus;
    /**
     * userId : 1111471286707625986
     * punchTime : 2019-06-29
     * userName : 吴杰
     * mileage : null
     * operationType : 0
     */

    private long userId;
    private String punchTime;
    private String userName;
    private String mileage;
    private String operationType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOffTime() {
        return offTime;
    }

    public void setOffTime(String offTime) {
        this.offTime = offTime;
    }

    public String getOnTime() {
        return onTime;
    }

    public void setOnTime(String onTime) {
        this.onTime = onTime;
    }

    public String getOnAddress() {
        return onAddress;
    }

    public void setOnAddress(String onAddress) {
        this.onAddress = onAddress;
    }

    public String getOffAddress() {
        return offAddress;
    }

    public void setOffAddress(String offAddress) {
        this.offAddress = offAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOnStatus() {
        return onStatus;
    }

    public void setOnStatus(String onStatus) {
        this.onStatus = onStatus;
    }

    public String getOffStatus() {
        return offStatus;
    }

    public void setOffStatus(String offStatus) {
        this.offStatus = offStatus;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getPunchTime() {
        return punchTime;
    }

    public void setPunchTime(String punchTime) {
        this.punchTime = punchTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }
}
