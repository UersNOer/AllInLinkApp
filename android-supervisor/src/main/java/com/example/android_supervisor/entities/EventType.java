package com.example.android_supervisor.entities;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * @author wujie
 */
@DatabaseTable(tableName = "t_even_type")
public class EventType implements Serializable{
    @DatabaseField(id = true)
    private String id;

    @DatabaseField
    @SerializedName(value = "parentId", alternate = {"classId"})
    private String pid;

    @DatabaseField
    @SerializedName(value = "name", alternate = {"standardBufferName"})
    private String name;

    @DatabaseField
    private String caseClassLevel;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCaseClassLevel() {
        return caseClassLevel;
    }

    public void setCaseClassLevel(String caseClassLevel) {
        this.caseClassLevel = caseClassLevel;
    }
}
