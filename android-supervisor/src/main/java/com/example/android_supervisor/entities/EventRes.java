package com.example.android_supervisor.entities;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * @author wujie
 */
public class EventRes implements Serializable {
    private String id;

    private double geoX;
    private double geoY;

    @SerializedName("position")
    private String address;

    private String type;

    private String statusLamp;

    private String areaCode; // 所属区域码
    private String areaName; // 所属区域名称

    @SerializedName("caseClassId")
    private String typeId; // 案件类别Id

    @SerializedName("caseClassName")
    private String typeName; // 案件类别名称

    @SerializedName("standardId")
    private String standardId; // 立案标准ID

    @SerializedName("regCaseStandard")
    private String standardName; // 立案标准

    @SerializedName("caseLevel")
    private String level; // 案件等级(0：一般；1：重要；2：重大)

    @SerializedName("caseSource")
    private int source; // 案件来源

    @SerializedName("questionDesc") // 问题描述
    private String desc;

    private String remark;

    private String evtId;

    @SerializedName("reporter")
    private String userName; // 举报人

    private String workGridId; // 所在工作网格

    private String procInstId; // 流程实例id

    private String procDefId; // 工作流标识

    private String actDefId; // 环节定义id

    private String actInstId; // 案件环节实例Id

    private String currentLink; // 当前环节

    @SerializedName(value = "caseCode")
    private String eventCode; // 案件号

    private String acceptCode; // 受理号

    @SerializedName(value = "caseTitle")
    private String title; // 案件标题

    private String reportTime; // 案件上报时间
    private String arrivalTime; // 来件时间（只有在待办列表里面才有赋值）



    @SerializedName(value = "expirationTime")
    private String expireTime; // 到期时间

    @SerializedName("timeLimit")
    private int taskTimeLimit; // 任务办理时限（分钟）

    @SerializedName("hasSuper")
    private boolean isDB; // 是否为督办案件

    @SerializedName("hasTodo")
    private boolean isCB; // 是否为催办案件

    @SerializedName(value = "umEvtAttchList", alternate = {"evAttachtList", "evtAttchList"})
    private List<Attach> attaches;

    @SerializedName("removeUmEvtAttchList")
    private List<Attach> removeAttchList;

    @SerializedName("newUmEvtAttchList")
    private List<Attach> newAttchList;
    /**
     * checkSurplusTime : true
     * surplusTime : 0秒
     *     * surplusType : 0
     */

    private boolean checkSurplusTime;
    private String surplusTime;
    private String surplusType;
    private Bitmap layerUrl;
    private String lastProdeptOpinion;


    public boolean isShowCaseTypeUi = false;


    public String getStatusLamp() {
        return statusLamp;
    }

    public void setStatusLamp(String statusLamp) {
        this.statusLamp = statusLamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEvtId() {
        return evtId;
    }

    public void setEvtId(String evtId) {
        this.evtId = evtId;
    }

    public String getLastProdeptOpinion() {
        return lastProdeptOpinion;
    }

    public void setLastProdeptOpinion(String lastProdeptOpinion) {
        this.lastProdeptOpinion = lastProdeptOpinion;
    }

    public List<Attach> getRemoveAttchList() {
        return removeAttchList;
    }

    public void setRemoveAttchList(List<Attach> removeAttchList) {
        this.removeAttchList = removeAttchList;
    }

    public List<Attach> getNewAttchList() {
        return newAttchList;
    }

    public void setNewAttchList(List<Attach> newAttchList) {
        this.newAttchList = newAttchList;
    }

    public Bitmap getLayerUrl() {
        return layerUrl;
    }

    public void setLayerUrl(Bitmap layerUrl) {
        this.layerUrl = layerUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
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

    public String getActDefId() {
        return actDefId;
    }

    public void setActDefId(String actDefId) {
        this.actDefId = actDefId;
    }

    public String getCurrentLink() {
        return currentLink;
    }

    public void setCurrentLink(String currentLink) {
        this.currentLink = currentLink;
    }

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    public String getAcceptCode() {
        return acceptCode;
    }

    public void setAcceptCode(String acceptCode) {
        this.acceptCode = acceptCode;
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

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public void setIsCB(boolean isCB) {
        this.isCB = isCB;
    }

    public void setIsDB(boolean isDB) {
        this.isDB = isDB;
    }

    public String getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(String expireTime) {
        this.expireTime = expireTime;
    }

    public int getTaskTimeLimit() {
        return taskTimeLimit;
    }

    public void setTaskTimeLimit(int taskTimeLimit) {
        this.taskTimeLimit = taskTimeLimit;
    }

    public List<Attach> getAttaches() {
        return attaches;
    }

    public boolean isDB() {
        return isDB;
    }

    public void setDB(boolean DB) {
        isDB = DB;
    }

    public boolean isCB() {
        return isCB;
    }

    public void setCB(boolean CB) {
        isCB = CB;
    }

    public void setAttaches(List<Attach> attaches) {
        this.attaches = attaches;
    }

    public String getSourceName() {
        String sourceName = String.valueOf(this.source);
//        FaceSqliteHelper sqliteHelper = FaceSqliteHelper.getInstance(null);
//        try {
//            Dictionary dictionary = sqliteHelper.getDictionaryDao()
//                    .queryBuilder()
//                    .where()
//                    .eq("pcode", "case_source")
//                    .and()
//                    .eq("code", sourceName)
//                    .queryForFirst();
//            if (dictionary != null) {
//                sourceName = dictionary.getName();
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        return sourceName;
    }

    public boolean isCheckSurplusTime() {
        return checkSurplusTime;
    }

    public void setCheckSurplusTime(boolean checkSurplusTime) {
        this.checkSurplusTime = checkSurplusTime;
    }

    public String getSurplusTime() {
        return surplusTime;
    }

    public void setSurplusTime(String surplusTime) {
        this.surplusTime = surplusTime;
    }

    public String getSurplusType() {
        return surplusType;
    }

    public void setSurplusType(String surplusType) {
        this.surplusType = surplusType;
    }
}
