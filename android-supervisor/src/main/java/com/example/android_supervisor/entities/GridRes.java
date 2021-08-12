package com.example.android_supervisor.entities;

import java.io.Serializable;

public class GridRes implements Serializable {

    String gridId;

    private String workGridId;
    private String manageGridId;

    private String areaCode;

    private String grName;

    public String getGrName() {
        return grName;
    }

    public void setGrName(String grName) {
        this.grName = grName;
    }

    public String getWorkGridId() {
        return workGridId;
    }

    public void setWorkGridId(String workGridId) {
        this.workGridId = workGridId;
    }

    public String getManageGridId() {
        return manageGridId;
    }

    public void setManageGridId(String manageGridId) {
        this.manageGridId = manageGridId;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getGridId() {
        return gridId;
    }

    public void setGridId(String gridId) {
        this.gridId = gridId;
    }



}
