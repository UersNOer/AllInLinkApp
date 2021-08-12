package com.example.android_supervisor.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

/**
 * @author wujie
 */
public class EventHandlePara {
    private String eventId;
    private String acceptId;
    private String attchType;
    private String businessId;
    private String businessTable;

    private String evtId;

    @SerializedName("prodeptStatus")
    private int status; // (0:核实属实 1:核实不属实 4:核查完成/未完成)

    private String comment;
    private String linkName;
    private String procDefId;
    private String procInsId;
    private String actInstId;
    private Map<String, Object> params;
    private List<Attach> umEvtAttchList;

    private String caseTitle;

    public String getEvtId() {
        return evtId;
    }

    public void setEvtId(String evtId) {
        this.evtId = evtId;
    }

    public String getCaseTitle() {
        return caseTitle;
    }

    public void setCaseTitle(String caseTitle) {
        this.caseTitle = caseTitle;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getAcceptId() {
        return acceptId;
    }

    public void setAcceptId(String acceptId) {
        this.acceptId = acceptId;
    }

    public String getAttchType() {
        return attchType;
    }

    public void setAttchType(String attchType) {
        this.attchType = attchType;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getBusinessTable() {
        return businessTable;
    }

    public void setBusinessTable(String businessTable) {
        this.businessTable = businessTable;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public String getProcDefId() {
        return procDefId;
    }

    public void setProcDefId(String procDefId) {
        this.procDefId = procDefId;
    }

    public String getProcInsId() {
        return procInsId;
    }

    public void setProcInsId(String procInsId) {
        this.procInsId = procInsId;
    }

    public String getActInstId() {
        return actInstId;
    }

    public void setActInstId(String actInstId) {
        this.actInstId = actInstId;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public List<Attach> getUmEvtAttchList() {
        return umEvtAttchList;
    }

    public void setUmEvtAttchList(List<Attach> umEvtAttchList) {
        this.umEvtAttchList = umEvtAttchList;
    }

    public String getEvtid() {
        return evtId;
    }

    public void setEvtid(String evtid) {
        this.evtId = evtid;
    }
}
