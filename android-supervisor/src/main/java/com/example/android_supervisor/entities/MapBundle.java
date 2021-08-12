package com.example.android_supervisor.entities;

import java.io.Serializable;

/**
 * Created by Administrator on 2019/9/15.
 */
public class MapBundle implements Serializable {

    public double latitude ;
    public double longitude;

    public boolean isPart;

    public boolean isShowGird;
    public boolean isShowCase;

    public String taskId;

    public boolean isCurrenTask;

    public EventRes eventRes;


}
