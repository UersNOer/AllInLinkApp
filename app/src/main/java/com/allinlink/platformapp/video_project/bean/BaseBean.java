package com.allinlink.platformapp.video_project.bean;

public class BaseBean<T> {
    private T result;
    private boolean suf;
    private int code;
    private String token;


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public boolean isSuf() {
        return suf;
    }

    public void setSuf(boolean suf) {
        this.suf = suf;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public boolean isSuccess() {
        return suf;
    }
}
