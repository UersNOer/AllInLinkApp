package com.example.android_supervisor.entities;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author wujie
 */
public class CensusEventPara implements Cloneable {

    private double geoX;
    private double geoY;

    @SerializedName("position")
    private String address;

    @SerializedName("censusTaskId")
    private String taskId;

    private String areaCode;

    @SerializedName("censusClassId")
    private String typeId; // 案件类别Id

    @SerializedName("censusClassName")
    private String typeName; // 案件类别名称

    @SerializedName("standardId")
    private String standardId; // 立案标准ID

    @SerializedName("regCaseStandard")
    private String standardName; // 立案标准

    @SerializedName("censusLevel")
    private int level; // 案件等级(0：一般；1：重要；2：重大)

    @SerializedName("censusSource")
    private String source = "8"; // 案件来源

    @SerializedName("procDefId")
    private String procId; // 案件流程id

    @SerializedName("questionDesc") // 问题描述
    private String desc;

    private String remark;

    @SerializedName("reporter")
    private String userName; // 举报人

    @SerializedName("reportTime")
    private Date reportTime = new Date(); // 举报人

    @SerializedName("telNum")
    private String mobile; // 举报人

    private int workGridId; // 所在工作网格

    @SerializedName("umEvtAttchList")
    private List<Attach> attaches;

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

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
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

    public int getWorkGridId() {
        return workGridId;
    }

    public void setWorkGridId(int workGridId) {
        this.workGridId = workGridId;
    }

    public List<Attach> getAttaches() {
        return attaches;
    }

    public void setAttaches(List<Attach> attaches) {
        this.attaches = attaches;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CensusEventPara) {
            CensusEventPara other = (CensusEventPara) obj;
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
    public CensusEventPara clone() {
        try {
            CensusEventPara obj = (CensusEventPara) super.clone();
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
