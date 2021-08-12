package com.example.android_supervisor.entities;

public class ClockPara {


    /**
     * id : 0
     * pointId : 0
     * punchStatus : string
     * punchTime : 2019-11-06T07:26:00.990Z
     * punchX : string
     * punchY : string
     * userId : 0
     * workGridCode : string
     * workPlanId : 0
     */

    private Long id;
    private Long pointId;
    private String punchStatus;
    private String punchTime;
    private String punchX;
    private String punchY;
    private Long userId;
    private String workGridCode;
    private Long workPlanId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPointId() {
        return pointId;
    }

    public void setPointId(Long pointId) {
        this.pointId = pointId;
    }

    public String getPunchStatus() {
        return punchStatus;
    }

    public void setPunchStatus(String punchStatus) {
        this.punchStatus = punchStatus;
    }

    public String getPunchTime() {
        return punchTime;
    }

    public void setPunchTime(String punchTime) {
        this.punchTime = punchTime;
    }

    public String getPunchX() {
        return punchX;
    }

    public void setPunchX(String punchX) {
        this.punchX = punchX;
    }

    public String getPunchY() {
        return punchY;
    }

    public void setPunchY(String punchY) {
        this.punchY = punchY;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getWorkGridCode() {
        return workGridCode;
    }

    public void setWorkGridCode(String workGridCode) {
        this.workGridCode = workGridCode;
    }

    public Long getWorkPlanId() {
        return workPlanId;
    }

    public void setWorkPlanId(Long workPlanId) {
        this.workPlanId = workPlanId;
    }
}
