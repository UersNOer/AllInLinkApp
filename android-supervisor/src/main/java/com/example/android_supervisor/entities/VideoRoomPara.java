package com.example.android_supervisor.entities;

import java.io.Serializable;

public class VideoRoomPara implements Serializable {


    /**
     * roomId : string
     * userId : 0
     */

    private String roomId;
    private int userId;

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;

    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
