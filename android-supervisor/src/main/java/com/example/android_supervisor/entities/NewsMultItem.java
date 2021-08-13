package com.example.android_supervisor.entities;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by dw on 2019/6/28.
 */
public class NewsMultItem implements MultiItemEntity {

    public static final int IMG = 1;
    public static final int VIDEO = 2;
    public static final int AUDIO = 3;

    private String url;
    private int itemType;

    public NewsMultItem(String url, int itemType) {
        this.url = url;
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public String getUrl() {
        return url;
    }
}
