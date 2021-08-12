package com.example.android_supervisor.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author wujie
 */
public class CheckUpPara {

    @SerializedName("chkId")
    private String checkId;

    @SerializedName("userId")
    private String userId;

    @SerializedName("replyContent")
    private String content;

    @SerializedName("annexDTOList")
    private List<Attach> attaches;

    private double absX;
    private double absY;
    private String replyLocation;

    public double getAbsX() {
        return absX;
    }

    public void setAbsX(double absX) {
        this.absX = absX;
    }

    public String getReplyLocation() {
        return replyLocation;
    }

    public void setReplyLocation(String replyLocation) {
        this.replyLocation = replyLocation;
    }

    public double getAbsY() {
        return absY;
    }

    public void setAbsY(double absY) {
        this.absY = absY;
    }

    public String getCheckId() {
        return checkId;
    }

    public void setCheckId(String checkId) {
        this.checkId = checkId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Attach> getAttaches() {
        return attaches;
    }

    public void setAttaches(List<Attach> attaches) {
        this.attaches = attaches;
    }
}
