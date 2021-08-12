package com.example.android_supervisor.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by dw on 2019/8/23.
 */

@DatabaseTable(tableName = "t_attendance")
public class Attendance implements Serializable {

    /**
     * id : 1136089169122476033
     * createId : 1070143724081586177
     * createTime : 2019-06-05 09:55:40
     * updateId : null
     * updateTime : null
     * beforeWork : 3
     * beLate : 3
     * completion : 30
     * afterWork : 3
     * dbStatus : 1
     * leaveEarly : 30
     */

    @DatabaseField(id = true)
    private String id;
    @DatabaseField
    private String createId;
    @DatabaseField
    private String createTime;
    @DatabaseField
    private int beforeWork;
    @DatabaseField
    private int beLate;
    @DatabaseField
    private int completion;
    @DatabaseField
    private int afterWork;
    private String dbStatus;
    @DatabaseField
    private int leaveEarly;

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

    public int getBeforeWork() {
        return beforeWork;
    }

    public void setBeforeWork(int beforeWork) {
        this.beforeWork = beforeWork;
    }

    public int getBeLate() {
        return beLate;
    }

    public void setBeLate(int beLate) {
        this.beLate = beLate;
    }

    public int getCompletion() {
        return completion;
    }

    public void setCompletion(int completion) {
        this.completion = completion;
    }

    public int getAfterWork() {
        return afterWork;
    }

    public void setAfterWork(int afterWork) {
        this.afterWork = afterWork;
    }

    public String getDbStatus() {
        return dbStatus;
    }

    public void setDbStatus(String dbStatus) {
        this.dbStatus = dbStatus;
    }

    public int getLeaveEarly() {
        return leaveEarly;
    }

    public void setLeaveEarly(int leaveEarly) {
        this.leaveEarly = leaveEarly;
    }
}
