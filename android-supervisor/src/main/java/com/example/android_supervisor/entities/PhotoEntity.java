package com.example.android_supervisor.entities;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "t_photo")
public class PhotoEntity {

    /**
     * 是否需要上传
     */
    @DatabaseField
    private String isUpload;

    /**
     * 上传附件个数
     */
    @DatabaseField
    private int uploadNum;

    /**
     * 是否可以选择相册
     */
    @DatabaseField
    @SerializedName("isAlbum")
    private String isAlbum;

    @DatabaseField
    @SerializedName("type")
    private String type;

    @DatabaseField
    @SerializedName("dbStatus")
    private String dbStatus;

    @DatabaseField
    @SerializedName("effectiveTime")
    private int effectiveTime;

    public int getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(int effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    public String getIsUpload() {
        return isUpload;
    }

    public void setIsUpload(String isUpload) {
        this.isUpload = isUpload;
    }

    public int getUploadNum() {
        return uploadNum;
    }

    public void setUploadNum(int uploadNum) {
        this.uploadNum = uploadNum;
    }

    public String getIsAlbum() {
        return isAlbum;
    }

    public void setIsAlbum(String isAlbum) {
        this.isAlbum = isAlbum;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDbStatus() {
        return dbStatus;
    }

    public void setDbStatus(String dbStatus) {
        this.dbStatus = dbStatus;
    }
}
