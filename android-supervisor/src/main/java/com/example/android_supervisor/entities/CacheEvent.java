package com.example.android_supervisor.entities;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;


@DatabaseTable(tableName = "t_event")
public class CacheEvent {

    @DatabaseField(id = true)
    private String id;

    @DatabaseField
    private String typeName;

    @DatabaseField
    private String address;

    @DatabaseField
    private String description;

    @DatabaseField
    private Date createTime;

    @DatabaseField
    private boolean isUpdata = false;//是否上传成功

    @DatabaseField
    private int caseTag = 0;//4 是暂存上报   5 快速上报暂存  3等待后台上传调用(注：暂定）


    @DatabaseField
    private int saveType = 0;// 0 普通暂存案件 1 自动上报暂存案件


    @DatabaseField(dataType = DataType.SERIALIZABLE)
    private EventPara data;



    public CacheEvent() {
    }

    public CacheEvent(String id, Date createTime, EventPara data,int caseTag,int saveType) {
        this.id = id;
        this.createTime = createTime;
        this.data = data;
        this.typeName = data.getTypeName();
        this.address = data.getAddress();
        this.description = data.getDesc();
        this.caseTag = caseTag;
        this.saveType = saveType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public EventPara getData() {
        return data;
    }

    public void setData(EventPara data) {
        this.data = data;
    }

    public int getCaseTag() {
        return caseTag;
    }

    public void setCaseTag(int caseTag) {
        this.caseTag = caseTag;
    }

    public boolean isUpdata() {
        return isUpdata;
    }

    public void setIsUpdata(boolean isUpdata) {
        this.isUpdata = isUpdata;
    }
}
