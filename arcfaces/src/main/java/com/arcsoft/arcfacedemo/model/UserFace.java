package com.arcsoft.arcfacedemo.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "t_face_info")
public class UserFace implements Serializable {

    @DatabaseField(id = true)
    public String userName;

    @DatabaseField
    public String pwd;

    @DatabaseField(dataType = DataType.BYTE_ARRAY)
    public byte[] faceBytes;


    public UserFace(String userName, String pwd, byte[] faceBytes) {
        this.userName = userName;
        this.pwd = pwd;
        this.faceBytes = faceBytes;
    }


    public UserFace() {

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public byte[] getFaceBytes() {
        return faceBytes;
    }

    public void setFaceBytes(byte[] faceBytes) {
        this.faceBytes = faceBytes;
    }
}
