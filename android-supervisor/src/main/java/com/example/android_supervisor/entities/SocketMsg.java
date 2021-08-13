package com.example.android_supervisor.entities;

import java.io.Serializable;

import io.socket.client.Ack;

/**
 * Created by dw on 2019/6/30.
 */
public class SocketMsg implements Serializable{
    private Message msg;
    private Ack ack;

    public Message getMsg() {
        return msg;
    }

    public void setMsg(Message msg) {
        this.msg = msg;
    }

    public Ack getAck() {
        return ack;
    }

    public void setAck(Ack ack) {
        this.ack = ack;
    }
}
