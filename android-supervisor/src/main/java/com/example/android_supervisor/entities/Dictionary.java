package com.example.android_supervisor.entities;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author wujie
 */
@DatabaseTable(tableName = "t_dictionary")
public class Dictionary {

    @DatabaseField
    @SerializedName("dictCode")
    private String code;

    @DatabaseField
    @SerializedName("dictName")
    private String name;

    @DatabaseField
    @SerializedName("parentCode")
    private String pcode;

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
}
