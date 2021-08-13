package com.example.android_supervisor.entities;

import com.google.gson.annotations.SerializedName;

/**
 * @author wujie
 */
public class CheckInPara {
    private String planId; // 排班id
    private String scheduleId; // 班次id
    private int operationType; // 打卡类型： 0-手动打卡 1-自动打卡
    private String areaCode;

    private double absX;
    private double absY;

    @SerializedName("punchAddress")
    private String address;

    private String userId;
    private String userName;

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public String getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    public int getOperationType() {
        return operationType;
    }

    public void setOperationType(int operationType) {
        this.operationType = operationType;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
