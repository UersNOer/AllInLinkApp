package com.example.android_supervisor.entities;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by dw yj
 */
@DatabaseTable(tableName = "t_WorkPlan")
public class WorkPlan1 implements Serializable{

    @DatabaseField(id = true)
    //人员ID
    private Long userId;


    //行政区域编码
    private String areaCode;

    //工作网格编码
    private String workGridCode;

    //角色分类
    private String roleClass;


    //上班日期（yyyy
    private Date workDate;

    //班次ID
    private Long schId;

    //下班时间
    private Date beginTime;

    //上班时间
    private Date endTime;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getWorkGridCode() {
        return workGridCode;
    }

    public void setWorkGridCode(String workGridCode) {
        this.workGridCode = workGridCode;
    }

    public String getRoleClass() {
        return roleClass;
    }

    public void setRoleClass(String roleClass) {
        this.roleClass = roleClass;
    }

    public Date getWorkDate() {
        return workDate;
    }

    public void setWorkDate(Date workDate) {
        this.workDate = workDate;
    }

    public Long getSchId() {
        return schId;
    }

    public void setSchId(Long schId) {
        this.schId = schId;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
