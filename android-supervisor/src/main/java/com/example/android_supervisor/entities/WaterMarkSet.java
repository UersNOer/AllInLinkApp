package com.example.android_supervisor.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "t_water_set")
public class WaterMarkSet {

    /**
     * id : 100000000000001
     * createId : 100000000000590
     * createTime : 2019-07-17 16:48:34
     * updateId : null
     * updateTime : 2020-07-20 03:45:56
     * wmWord : 1
     * size : 30
     * color : rgb(247, 7, 7)
     * imgPosition : 左上
     * devLevel : 0
     * devVertical : 0
     * dbStatus : 1
     * type : 1
     */

    @DatabaseField(id = true)
    private String id;
    @DatabaseField
    private String createId;
    private String createTime;
    private Object updateId;
    private String updateTime;
    @DatabaseField
    private String wmWord;
    @DatabaseField
    private int size;
    @DatabaseField
    private String color;
    @DatabaseField
    private String imgPosition;
    private int devLevel;
    private int devVertical;
    @DatabaseField
    private String dbStatus;
    @DatabaseField
    private String type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Object getUpdateId() {
        return updateId;
    }

    public void setUpdateId(Object updateId) {
        this.updateId = updateId;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getWmWord() {
        return wmWord;
    }

    public void setWmWord(String wmWord) {
        this.wmWord = wmWord;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getImgPosition() {
        return imgPosition;
    }

    public void setImgPosition(String imgPosition) {
        this.imgPosition = imgPosition;
    }

    public int getDevLevel() {
        return devLevel;
    }

    public void setDevLevel(int devLevel) {
        this.devLevel = devLevel;
    }

    public int getDevVertical() {
        return devVertical;
    }

    public void setDevVertical(int devVertical) {
        this.devVertical = devVertical;
    }

    public String getDbStatus() {
        return dbStatus;
    }

    public void setDbStatus(String dbStatus) {
        this.dbStatus = dbStatus;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
