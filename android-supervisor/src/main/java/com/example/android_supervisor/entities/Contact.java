package com.example.android_supervisor.entities;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName = "t_contact")
public class Contact {

//    "id": "1176736041906757634",
//            "createId": "0",
//            "createTime": "2019-09-25 13:51:50",
//            "updateId": "0",
//            "updateTime": "2019-09-25 13:51:50",
//            "dirName": "消息测试帐号",
//            "dirEmail": null,
//            "dirMobileNumber": "13912341234",
//            "dirPhomeNumber": null,
//            "dirAddress": null,
//            "dirCompany": null,
//            "dirDept": "光明街道办事处",
//            "orderNum": null,
//            "remark": null,
//            "userId": "1174137292273573889",
//            "areaCode": null


    @DatabaseField
    private String id;

    @DatabaseField
    @SerializedName("dirNames")
    private String userName;

    @DatabaseField
    private int sex;

    @DatabaseField
    @SerializedName("imageUrl")
    private String avatar;

    @DatabaseField
    @SerializedName("dirName")
    private String nickName;

    @DatabaseField
    @SerializedName("userEmail")
    private String email;

    @DatabaseField
    @SerializedName("dirDept")
    private String department;

    @DatabaseField
    @SerializedName("dirMobileNumber")
    private String mobile;

    @DatabaseField
    @SerializedName("dirPhomeNumber")
    private String telephone;

    @DatabaseField
    private String remark;

    @DatabaseField(id = true)
    @SerializedName("userId")
    private String chatUserId;

    private String initialName;

    public String getChatUserId() {
        return chatUserId;
    }


    public String getInitialName() {
        return initialName;
    }

    public void setChatUserId(String chatUserId) {
        this.chatUserId = chatUserId;
    }

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

    public void setInitialName(String initialName) {
        this.initialName = initialName;
    }
}
