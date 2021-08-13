package com.example.android_supervisor.entities;

import com.example.android_supervisor.sqlite.PublicSqliteHelper;
import com.google.gson.annotations.SerializedName;

import java.sql.SQLException;
import java.util.List;

/**
 * @author wujie
 */
public class CensusEventRes {

    public String taskId;

    private String id;

    private String procInstId; // 流程实例id

    private String procDefId; // 工作流标识

    private String actInstId; // 案件环节实例Id

    @SerializedName(value = "censusCode")
    private String eventCode; // 案件号

    @SerializedName(value = "censusTitle")
    private String title; // 案件标题

    private String reportTime; // 案件上报时间

    private double geoX;
    private double geoY;

    @SerializedName("position")
    private String address;

    private String areaCode; // 所属区域码
    private String areaName; // 所属区域名称

    @SerializedName("censusClassId")
    private String typeId; // 案件类别Id

    @SerializedName("censusClassName")
    private String typeName; // 案件类别名称

    @SerializedName("standardId")
    private String standardId; // 立案标准ID

    @SerializedName("standardName")
    private String standardName; // 立案标准

//    @SerializedName("caseLevel")
//    private int level; // 案件等级(0：一般；1：重要；2：重大)

    @SerializedName("censusSource")
    private String source; // 案件来源

    @SerializedName("questionDesc") // 问题描述
    private String desc;

    private String remark;

    @SerializedName("reporter")
    private String userName; // 举报人

    private String workGridId; // 所在工作网格

    @SerializedName(value = "umEvtAttchList", alternate = {"evAttachtList", "evtAttchList"})
    private List<Attach> attaches;

    private String taskName;

    private String censusAreaName;

    private String className;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getCensusAreaName() {
        return censusAreaName;
    }

    public void setCensusAreaName(String censusAreaName) {
        this.censusAreaName = censusAreaName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }

    public String getProcDefId() {
        return procDefId;
    }

    public void setProcDefId(String procDefId) {
        this.procDefId = procDefId;
    }

    public String getActInstId() {
        return actInstId;
    }

    public void setActInstId(String actInstId) {
        this.actInstId = actInstId;
    }

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReportTime() {
        return reportTime;
    }

    public void setReportTime(String reportTime) {
        this.reportTime = reportTime;
    }

    public double getGeoX() {
        return geoX;
    }

    public void setGeoX(double geoX) {
        this.geoX = geoX;
    }

    public double getGeoY() {
        return geoY;
    }

    public void setGeoY(double geoY) {
        this.geoY = geoY;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getStandardId() {
        return standardId;
    }

    public void setStandardId(String standardId) {
        this.standardId = standardId;
    }

    public String getStandardName() {
        return standardName;
    }

    public void setStandardName(String standardName) {
        this.standardName = standardName;
    }

//    public int getLevel() {
//        return level;
//    }
//
//    public void setLevel(int level) {
//        this.level = level;
//    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getWorkGridId() {
        return workGridId;
    }

    public void setWorkGridId(String workGridId) {
        this.workGridId = workGridId;
    }

    public List<Attach> getAttaches() {
        return attaches;
    }

    public void setAttaches(List<Attach> attaches) {
        this.attaches = attaches;
    }

    public String getSourceName() {
        String sourceName = String.valueOf(this.source);
        PublicSqliteHelper sqliteHelper = PublicSqliteHelper.getInstance(null);
        try {
            Dictionary dictionary = sqliteHelper.getDictionaryDao()
                    .queryBuilder()
                    .where()
                    .eq("pcode", "case_source")
                    .and()
                    .eq("code", sourceName)
                    .queryForFirst();
            if (dictionary != null) {
                sourceName = dictionary.getName();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sourceName;
    }
}
