package com.example.android_supervisor.ui.model;

import java.io.Serializable;

public class CaseClassTreeVO implements Serializable {

    private String id;

    private String nodeName;

    private String sortId;

    private String nodeLevel;

    private String dbStatus;

    private String nodeObjCode;

    private String nodeCode;

    private String industryType;

    private String questionClassId;

    private String parentName;

    private String whType;
    
    private String iconUrl;

    private int countWhSys;

    private boolean expandable = false;

    public boolean isExpandable() {
        return expandable;
    }

    public void setExpandable(boolean expandable) {
        this.expandable = expandable;
    }

    public int getCountWhSys() {
        return countWhSys;
    }

    public void setCountWhSys(int countWhSys) {
        this.countWhSys = countWhSys;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getSortId() {
        return sortId;
    }

    public void setSortId(String sortId) {
        this.sortId = sortId;
    }

    public String getNodeLevel() {
        return nodeLevel;
    }

    public void setNodeLevel(String nodeLevel) {
        this.nodeLevel = nodeLevel;
    }

    public String getDbStatus() {
        return dbStatus;
    }

    public void setDbStatus(String dbStatus) {
        this.dbStatus = dbStatus;
    }

    public String getNodeObjCode() {
        return nodeObjCode;
    }

    public void setNodeObjCode(String nodeObjCode) {
        this.nodeObjCode = nodeObjCode;
    }

    public String getNodeCode() {
        return nodeCode;
    }

    public void setNodeCode(String nodeCode) {
        this.nodeCode = nodeCode;
    }

    public String getIndustryType() {
        return industryType;
    }

    public void setIndustryType(String industryType) {
        this.industryType = industryType;
    }

    public String getQuestionClassId() {
        return questionClassId;
    }

    public void setQuestionClassId(String questionClassId) {
        this.questionClassId = questionClassId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getWhType() {
        return whType;
    }

    public void setWhType(String whType) {
        this.whType = whType;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
