package com.example.android_supervisor.entities;

import com.google.gson.annotations.SerializedName;

/**
 * @author wujie
 */
public class DeptRes {
    private String id;
    private String parentId;

    @SerializedName("nodeName")
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
