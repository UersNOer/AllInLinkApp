package com.example.android_supervisor.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author wujie
 */
@DatabaseTable(tableName = "t_gesture")
public class GestureLock {
    @DatabaseField(id = true)
    private String userId;

    @DatabaseField
    private String gesture;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGesture() {
        return gesture;
    }

    public void setGesture(String gesture) {
        this.gesture = gesture;
    }
}
