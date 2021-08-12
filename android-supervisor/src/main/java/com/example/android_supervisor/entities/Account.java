package com.example.android_supervisor.entities;

import java.io.Serializable;
import java.util.Date;

/**
 * @author wujie
 */
public class Account implements Serializable{
    private String userId;

    private int sdkAppId ;

    private String loginName;

    private String password;

    private Date loginTime;

    public int getSdkAppId() {
        return sdkAppId;
    }

    public void setSdkAppId(int sdkAppId) {
        this.sdkAppId = sdkAppId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }


}
