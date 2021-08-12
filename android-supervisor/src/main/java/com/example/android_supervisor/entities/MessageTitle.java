package com.example.android_supervisor.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Date;

/**
 * @author yj
 */
@DatabaseTable(tableName = "t_message_title")
public class MessageTitle implements Serializable {

    /**
     * 用来区分主消息页面是否显示图标
     */
    @DatabaseField
    private boolean isNew;

    @DatabaseField
    private Date msgTime;

    @DatabaseField(id = true)
    private String groupCode;

    @DatabaseField
    private String title;

    @DatabaseField
    private String content;

    @DatabaseField
    private String extraInfo;


    @DatabaseField
    private int msgCount;

    public MessageTitle() {
    }



    public int getMsgCount() {
        return msgCount;
    }

    public void setMsgCount(int msgCount) {
        this.msgCount = msgCount;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }

    public Date getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(Date msgTime) {
        this.msgTime = msgTime;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExtraInfo() {
        return extraInfo;
    }

    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }
}
