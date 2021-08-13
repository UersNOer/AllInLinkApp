package com.example.android_supervisor.entities;

/**
 * @author wujie
 */
public class EventRollbackPara {
    private String id;
    private String acceptId;
    private String actInstId;
    private String procInstId;
    private String currentLink;
    private String applyType;
    private String comment;

    private String caseTitle;

    public String getCaseTitle() {
        return caseTitle;
    }

    public void setCaseTitle(String caseTitle) {
        this.caseTitle = caseTitle;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAcceptId() {
        return acceptId;
    }

    public void setAcceptId(String acceptId) {
        this.acceptId = acceptId;
    }

    public String getActInstId() {
        return actInstId;
    }

    public void setActInstId(String actInstId) {
        this.actInstId = actInstId;
    }

    public String getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }

    public String getCurrentLink() {
        return currentLink;
    }

    public void setCurrentLink(String currentLink) {
        this.currentLink = currentLink;
    }

    public String getApplyType() {
        return applyType;
    }

    public void setApplyType(String applyType) {
        this.applyType = applyType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
