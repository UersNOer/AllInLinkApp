package com.example.android_supervisor.entities;

/**
 * Created by Administrator on 2019/9/18.
 */
public class UpdatePdaRes {


    /**
     * success : true
     * code : 200
     * message : 操作成功
     * data : true
     */

    private boolean success;
    private int code;
    private String message;
    private boolean data;

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

    public boolean isData() {
        return data;
    }

    public void setData(boolean data) {
        this.data = data;
    }
}
