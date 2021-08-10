package com.allinlink.platformapp.video_project.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TabInfoBean {

    @SerializedName("default_tab")
    public int defaultTab;

    @SerializedName("tab_info")
    public ArrayList<TabInfoItemBean> tabInfos;

    public int getDefaultTab() {
        return defaultTab;
    }

    public void setDefaultTab(int defaultTab) {
        this.defaultTab = defaultTab;
    }

    public ArrayList<TabInfoItemBean> getTabInfos() {
        return tabInfos;
    }

    public void setTabInfos(ArrayList<TabInfoItemBean> tabInfos) {
        this.tabInfos = tabInfos;
    }
}
