package com.example.android_supervisor.entities;

import java.io.Serializable;

/**
 * Created by dw on 2019/8/23.
 */
public class SyncRequestBody implements Serializable {

    /**
     * roleId : 1039716566813396998
     * type : 1
     * userId : 1164745833250320385
     */

    private long roleId;
    private String type;
    private long userId;

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
