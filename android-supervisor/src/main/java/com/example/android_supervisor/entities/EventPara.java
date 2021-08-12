package com.example.android_supervisor.entities;

import com.example.android_supervisor.utils.DateUtils;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author wujie
 */
public class EventPara implements Serializable, Cloneable {
   @SerializedName("id")
    private String caseId;

    private double geoX;
    private double geoY;

    @SerializedName("position")
    private String address;

    private String areaCode;

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
    private int source = 4; // 案件来源 案件上报是4，快速上报是5

    @SerializedName("procDefId")
    private String procId; // 案件流程id

    @SerializedName("questionDesc") // 问题描述
    private String desc;

    private String remark;

    @SerializedName("reporter")
    private String userName; // 举报人

    @SerializedName("reportTime")
    private String reportTime = DateUtils.format(new Date()); // 举报人  用string

    @SerializedName("telNum")
    private String mobile; // 举报人

    private String workGridId; // 所在工作网格

    @SerializedName("umEvtAttchList")
    private ArrayList<Attach> attaches;

    @SerializedName("umEvtAttchLaterList")
    private ArrayList<Attach> laterAttaches;

    @SerializedName("gridId")
    private String gridId;


    @SerializedName("manageGridId")
    private String manageGridId;

    private String bidType;


    public String getBidType() {
        return bidType;
    }

    public void setBidType(String bidType) {
        this.bidType = bidType;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }


//    public Date getReportTime() {
//        return reportTime;
//    }
//
//    public void setReportTime(Date reportTime) {
//        this.reportTime = reportTime;
//    }


    public String getManageGridId() {
        return manageGridId;
    }

    public void setManageGridId(String manageGridId) {
        this.manageGridId = manageGridId;
    }

    public String getGridId() {
        return gridId;
    }

    public void setGridId(String gridId) {
        this.gridId = gridId;
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

    public String getProcId() {
        return procId;
    }

    public void setProcId(String procId) {
        this.procId = procId;
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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getWorkGridId() {
        return workGridId;
    }

    public void setWorkGridId(String workGridId) {
        this.workGridId = workGridId;
    }

    public ArrayList<Attach> getAttaches() {
        return attaches;
    }

    public void setAttaches(ArrayList<Attach> attaches) {
        this.attaches = attaches;
    }

    public ArrayList<Attach> getLaterAttaches() {
        return laterAttaches;
    }

    public void setLaterAttaches(ArrayList<Attach> laterAttaches) {
        this.laterAttaches = laterAttaches;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof EventPara) {
            EventPara other = (EventPara) obj;
            if (!equals(this.typeId, other.typeId)) {
                return false;
            }
            if (!equals(this.typeName, other.typeName)) {
                return false;
            }
            if (!equals(this.standardId, other.standardId)) {
                return false;
            }
            if (!equals(this.standardName, other.standardName)) {
                return false;
            }
            if (this.geoX != other.geoX) {
                return false;
            }
            if (this.geoY != other.geoY) {
                return false;
            }
            if (!equals(this.address, other.address)) {
                return false;
            }
            if (!equals(this.desc, other.desc)) {
                return false;
            }
            if (!equals(this.attaches, other.attaches)) {
                return false;
            }
            return true;
        }
        return false;
    }

    private boolean equals(Object obj1, Object obj2) {
        if (obj1 == null && obj2 == null) {
            return true;
        }
        if (obj1 == null || obj2 == null) {
            return false;
        }
        return obj1.equals(obj2);
    }

    @Override
    public EventPara clone() {
        try {
            EventPara obj = (EventPara) super.clone();
            if (this.attaches != null) {
                obj.attaches = new ArrayList<>();
                for (Attach attach : this.attaches) {
                    obj.attaches.add(attach.clone());
                }
            }
            return obj;
        } catch (CloneNotSupportedException e) {
            return this;
        }
    }
}
