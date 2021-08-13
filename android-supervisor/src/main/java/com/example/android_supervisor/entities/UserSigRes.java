package com.example.android_supervisor.entities;

import java.io.Serializable;

/**
 * Created by Administrator on 2019/9/24.
 */
public class UserSigRes implements Serializable {


    /**
     * success : true
     * code : 200
     * message : 操作成功
     * data : eJw1jt0KgkAQhd9lryNmxt11V*gmJIgiJCXyMnCNURTdpITo3ROty-PzHc5bZMd07caOvRMRKqUIAFaz*3ReRILWIBb9KOpb13Ex9SQAaUCjloQL1w5c8gwghhIlhYExVoM0SPY-wPcpT097eRiDxJu4eQ11i73aXWPVhzKrMC-z8ZL6LdlzlcjNDxy4md9pSxaNps8XHKUyvg__
     */

    private boolean success;
    private int code;
    private String message;
    private String data;

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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
