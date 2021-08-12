package com.example.android_supervisor.entities;

import java.io.Serializable;

/**
 * Created by Administrator on 2019/9/9.
 */
public class ChooseData implements Serializable {

    public String id;

    public String name;

    public String whType;

    public ChooseData() {

    }


    public ChooseData(String id, String name) {
        this.id = id;
        this.name = name;

    }


    public ChooseData(String id, String name,String whType) {
        this.id = id;
        this.name = name;

        this.whType = whType;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWhType() {
        return whType;
    }

    public void setWhType(String whType) {
        this.whType = whType;
    }

    @Override
    public String toString() {
        return name;
    }
}
