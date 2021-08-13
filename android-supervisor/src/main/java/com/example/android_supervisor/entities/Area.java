package com.example.android_supervisor.entities;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author wujie
 */
@DatabaseTable(tableName = "t_area")
public class Area {

    @DatabaseField
    @SerializedName("areaCode")
    private String code;

    @DatabaseField
    @SerializedName("areaName")
    private String name;

    @DatabaseField
    @SerializedName("parentCode")
    private String pcode;

    @DatabaseField
    @SerializedName("areaLevel")
    private int level;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPcode() {
        return pcode;
    }

    public void setPcode(String pcode) {
        this.pcode = pcode;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
