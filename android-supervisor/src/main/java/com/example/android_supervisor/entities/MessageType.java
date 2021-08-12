package com.example.android_supervisor.entities;

import java.io.Serializable;
import java.util.List;

public class MessageType implements Serializable {
    private String messageType;
    private List<Message> messageList;

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }
}
