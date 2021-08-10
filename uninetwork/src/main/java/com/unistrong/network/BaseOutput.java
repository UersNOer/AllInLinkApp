package com.unistrong.network;


import android.text.TextUtils;

public final class BaseOutput<T> implements IModel {
    private int code;
    public String status;
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    private T data;
    private T content;

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    /**
     * 接口暂时一直都返回0，此方法先别用
     *
     * @return
     */
    @Override
    public boolean isError() {
        if (code != 200) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isSuccess() {
        if (TextUtils.equals(status, "success")) {
            return true;
        }
        return false;
    }

    @Override
    public String getErrorMsg() {
        return message;
    }

}
