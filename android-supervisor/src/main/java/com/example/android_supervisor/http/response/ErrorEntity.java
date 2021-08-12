package com.example.android_supervisor.http.response;

import java.io.Serializable;

/**
 * Created by dw on 2019/8/8.
 */
public class ErrorEntity implements Serializable {
    private int code;
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
