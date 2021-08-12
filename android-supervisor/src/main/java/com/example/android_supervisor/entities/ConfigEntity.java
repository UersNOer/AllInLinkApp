package com.example.android_supervisor.entities;

import java.io.Serializable;

/**
 * Created by dw on 2019/7/17.
 */
public class ConfigEntity implements Serializable {


    /**
     * id : 1133607293196185602
     * createId : 1070143724081586177
     * createTime : 2019-05-29 13:33:35
     * updateId : 1070143724081586177
     * updateTime : 2019-07-16 19:21:00
     * ownerName : 遂平县城管局
     * ownerUrl : https://www.baidu.com/
     * ownerImageUrl : http://192.168.20.72:5004/files/5cf61a7cfdd3c346889df36c
     * devName : 湖南图元云科技有限公司
     * devUrl : http://www.topevery.com/
     * devImageUrl : http://192.168.20.72:5004/files/5cf61a43fdd3c346889df368
     * administrativeCode : 411728
     * appDesc :
     * dbStatus : 1
     * orderNum : 0
     * fileServer : http://192.168.20.72:5100/files/
     */

    private String id;
    private String createId;
    private String createTime;
    private String updateId;
    private String updateTime;
    private String ownerName;
    private String ownerUrl;
    private String ownerImageUrl;
    private String devName;
    private String devUrl;
    private String devImageUrl;
    private int administrativeCode;
    private String appDesc;
    private String dbStatus;
    private int orderNum;
    private String fileServer;

//    @ApiModelProperty("文件服务器内网地址file-intranet-server")
   private String fileIntranectServer;

//    @ApiModelProperty("文件服务器http协议地址file-server-http")
   private String fileServerHttp;


    public String getFileIntranectServer() {
        return fileIntranectServer;
    }

    public void setFileIntranectServer(String fileIntranectServer) {
        this.fileIntranectServer = fileIntranectServer;
    }

    public String getFileServerHttp() {
        return fileServerHttp;
    }

    public void setFileServerHttp(String fileServerHttp) {
        this.fileServerHttp = fileServerHttp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateId() {
        return updateId;
    }

    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerUrl() {
        return ownerUrl;
    }

    public void setOwnerUrl(String ownerUrl) {
        this.ownerUrl = ownerUrl;
    }

    public String getOwnerImageUrl() {
        return ownerImageUrl;
    }

    public void setOwnerImageUrl(String ownerImageUrl) {
        this.ownerImageUrl = ownerImageUrl;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public String getDevUrl() {
        return devUrl;
    }

    public void setDevUrl(String devUrl) {
        this.devUrl = devUrl;
    }

    public String getDevImageUrl() {
        return devImageUrl;
    }

    public void setDevImageUrl(String devImageUrl) {
        this.devImageUrl = devImageUrl;
    }

    public int getAdministrativeCode() {
        return administrativeCode;
    }

    public void setAdministrativeCode(int administrativeCode) {
        this.administrativeCode = administrativeCode;
    }

    public String getAppDesc() {
        return appDesc;
    }

    public void setAppDesc(String appDesc) {
        this.appDesc = appDesc;
    }

    public String getDbStatus() {
        return dbStatus;
    }

    public void setDbStatus(String dbStatus) {
        this.dbStatus = dbStatus;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public String getFileServer() {
        return fileServer;
    }

    public void setFileServer(String fileServer) {
        this.fileServer = fileServer;
    }

    @Override
    public String toString() {
        return "ConfigEntity{" +
                "id='" + id + '\'' +
                ", createId='" + createId + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updateId='" + updateId + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", ownerName='" + ownerName + '\'' +
                ", ownerUrl='" + ownerUrl + '\'' +
                ", ownerImageUrl='" + ownerImageUrl + '\'' +
                ", devName='" + devName + '\'' +
                ", devUrl='" + devUrl + '\'' +
                ", devImageUrl='" + devImageUrl + '\'' +
                ", administrativeCode=" + administrativeCode +
                ", appDesc='" + appDesc + '\'' +
                ", dbStatus='" + dbStatus + '\'' +
                ", orderNum=" + orderNum +
                ", fileServer='" + fileServer + '\'' +
                ", fileIntranectServer='" + fileIntranectServer + '\'' +
                ", fileServerHttp='" + fileServerHttp + '\'' +
                '}';
    }
}
