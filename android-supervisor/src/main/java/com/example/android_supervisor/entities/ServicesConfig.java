package com.example.android_supervisor.entities;

import java.io.Serializable;

/**
 * 服务器访问设置类
 *
 * @author wujie
 */
public class ServicesConfig implements Serializable {

    /**
     * id : 1
     * createId : null
     * createTime : null
     * updateId : null
     * updateTime : 2019-06-24 14:14:56
     * proId : 1139358757725569025
     * configType : 1
     * protocolName : https
     * hostAddress : tykj.cszhx.top
     * portNum : 8000
     */

    private String id;
    private Object createId;
    private Object createTime;
    private Object updateId;
    private String updateTime;
    private String proId;
    /**
     * 0:
     */
    private int configType;
    private String hostAddress;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getCreateId() {
        return createId;
    }

    public void setCreateId(Object createId) {
        this.createId = createId;
    }

    public Object getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Object createTime) {
        this.createTime = createTime;
    }

    public Object getUpdateId() {
        return updateId;
    }

    public void setUpdateId(Object updateId) {
        this.updateId = updateId;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    public int getConfigType() {
        return configType;
    }

    public void setConfigType(int configType) {
        this.configType = configType;
    }

    public String getHostAddress() {
        return hostAddress;
    }

    public void setHostAddress(String hostAddress) {
        this.hostAddress = hostAddress;
    }
}
