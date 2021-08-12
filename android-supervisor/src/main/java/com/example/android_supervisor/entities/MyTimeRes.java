package com.example.android_supervisor.entities;


import com.example.android_supervisor.utils.SyncDateTime;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2019/9/5.
 */

public class MyTimeRes implements Serializable {


    /**
     * success : true
     * code : 200
     * message : 操作成功
     * data : 1567685385638
     */

    private boolean success;
    private int code;
    private String message;
    private long data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getData() {
        return data;
    }

    public void setData(long data) {
        this.data = data;
    }

    public Date toData() {
        return SyncDateTime.getInstance().toDate(data);
    }
}
