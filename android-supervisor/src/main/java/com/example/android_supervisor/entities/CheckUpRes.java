package com.example.android_supervisor.entities;

import com.google.gson.annotations.SerializedName;

/**
 * @author wujie
 */
public class CheckUpRes {
    private String id;

    @SerializedName("spotName")
    private String name;

    @SerializedName("beginDate")
    private String beginDate;

    @SerializedName("overDate")
    private String endDate;

    @SerializedName("areaCode")
    private String areaCode;

    @SerializedName("summary")
    private String content;

    @SerializedName("chkType")
    private int type; // 抽查类型（0：手动抽查；1：自动抽查）

    private String stateOfProgress;

    public String getStateOfProgress() {
        return stateOfProgress;
    }

    public void setStateOfProgress(String stateOfProgress) {
        this.stateOfProgress = stateOfProgress;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


}
