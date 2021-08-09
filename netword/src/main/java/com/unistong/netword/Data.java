package com.unistong.netword;


public class Data<T> {
    public T content;
    public String message;
    public String status;

    public Data(){

    }
    public Data(T content, String message, String status) {
        this.content = content;
        this.message = message;
        this.status = status;
    }

    @Override
    public String toString() {
        return "Data{" +
                "content=" + content +
                ", message='" + message + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
