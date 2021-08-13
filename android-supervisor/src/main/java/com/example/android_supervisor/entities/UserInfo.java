package com.example.android_supervisor.entities;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * @author wujie
 */
@DatabaseTable(tableName = "t_user_info")
public class UserInfo implements Serializable {
    @DatabaseField(id = true)
    private String id;

    @DatabaseField
    @SerializedName("loginName")
    private String userName;

    @DatabaseField
    private int sex;

    @DatabaseField
    @SerializedName("imageUrl")
    private String avatar;

    @DatabaseField
    @SerializedName("userName")
    private String nickName;

    @DatabaseField
    @SerializedName("userEmail")
    private String email;

    @DatabaseField
    @SerializedName("deptName")
    private String department;

    @DatabaseField
    @SerializedName("userMobile")
    private String mobile;

    @DatabaseField
    @SerializedName("userTelephone")
    private String telephone;

    @DatabaseField
    private String remark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
