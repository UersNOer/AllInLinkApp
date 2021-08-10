package com.allinlink.platformapp.video_project.bean;

import com.google.gson.annotations.SerializedName;

public class TabInfoItemBean {

    @SerializedName("class_name")
    public String className;

    @SerializedName("tab_name")
    public String tabName;

    public String tab_drawable_normal;
    public String tab_drawable_select;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public String getTab_drawable_normal() {
        return tab_drawable_normal;
    }

    public void setTab_drawable_normal(String tab_drawable_normal) {
        this.tab_drawable_normal = tab_drawable_normal;
    }

    public String getTab_drawable_select() {
        return tab_drawable_select;
    }

    public void setTab_drawable_select(String tab_drawable_select) {
        this.tab_drawable_select = tab_drawable_select;
    }

    @Override
    public String toString() {
        return "TabInfo{" +
                "className='" + className + '\'' +
                ", tabName='" + tabName + '\'' +
                ", tab_drawable_normal='" + tab_drawable_normal + '\'' +
                ", tab_drawable_select='" + tab_drawable_select + '\'' +
                '}';
    }


}
