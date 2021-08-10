package com.allinlink.platformapp.video_project.bean;

import com.allinlink.platformapp.video_project.utils.MediaUrlUtils;

import java.io.Serializable;

public class ChannelBean implements Serializable {

    /**
     * gid : 2020102216433100012
     * channelCode : SC001
     * channelName : 测试1
     * channelSn : 1
     * ipAddress : 0.0.0.0
     * bitMax : 1080
     * bitType : type1
     * bitNum : 3
     * videoEncoded : 1
     * videoFrame : 1
     * frameSpace : 1
     * resRatio : 1
     * cameraType : 1
     * standardType : 1
     * mediaAddress : 1
     * cameraInfo : 1
     * cameraGroup : 2020102216433100001
     * jd : 0
     * wd : 0
     */

    private String gid;
    private String channelCode;
    private String channelName;
    private String channelSn;
    private String ipAddress;
    private String bitMax;
    private String bitType;
    private int bitNum;
    private String videoEncoded;
    private String videoFrame;
    private int frameSpace;
    private String resRatio;
    private String cameraType;
    private String standardType;
    private String mediaAddress;
    private int favoriteFlag;
    private String cameraInfo;
    private String cameraGroup;
    private String jd;
    private String wd;


    private MapMarkerBean.FeaturesBean latlng;

    public MapMarkerBean.FeaturesBean getLatlng() {
        return latlng;
    }

    public int getFavoriteFlag() {
        return favoriteFlag;
    }

    public void setFavoriteFlag(int favoriteFlag) {
        this.favoriteFlag = favoriteFlag;
    }

    public void setLatlng(MapMarkerBean.FeaturesBean latlng) {
        this.latlng = latlng;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getChannelSn() {
        return channelSn;
    }

    public void setChannelSn(String channelSn) {
        this.channelSn = channelSn;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getBitMax() {
        return bitMax;
    }

    public void setBitMax(String bitMax) {
        this.bitMax = bitMax;
    }

    public String getBitType() {
        return bitType;
    }

    public void setBitType(String bitType) {
        this.bitType = bitType;
    }

    public int getBitNum() {
        return bitNum;
    }

    public void setBitNum(int bitNum) {
        this.bitNum = bitNum;
    }

    public String getVideoEncoded() {
        return videoEncoded;
    }

    public void setVideoEncoded(String videoEncoded) {
        this.videoEncoded = videoEncoded;
    }

    public String getVideoFrame() {
        return videoFrame;
    }

    public void setVideoFrame(String videoFrame) {
        this.videoFrame = videoFrame;
    }

    public int getFrameSpace() {
        return frameSpace;
    }

    public void setFrameSpace(int frameSpace) {
        this.frameSpace = frameSpace;
    }

    public String getResRatio() {
        return resRatio;
    }

    public void setResRatio(String resRatio) {
        this.resRatio = resRatio;
    }

    public String getCameraType() {
        return cameraType;
    }

    public void setCameraType(String cameraType) {
        this.cameraType = cameraType;
    }

    public String getStandardType() {
        return standardType;
    }

    public void setStandardType(String standardType) {
        this.standardType = standardType;
    }

    public String getMediaAddress() {
        String mediaUrl = MediaUrlUtils.checkVideoUrl(mediaAddress);

        return mediaUrl;
    }

    public void setMediaAddress(String mediaAddress) {
        this.mediaAddress = mediaAddress;
    }

    public String getCameraInfo() {
        return cameraInfo;
    }

    public void setCameraInfo(String cameraInfo) {
        this.cameraInfo = cameraInfo;
    }

    public String getCameraGroup() {
        return cameraGroup;
    }

    public void setCameraGroup(String cameraGroup) {
        this.cameraGroup = cameraGroup;
    }

    public String getJd() {
        return jd;
    }

    public void setJd(String jd) {
        this.jd = jd;
    }

    public String getWd() {
        return wd;
    }

    public void setWd(String wd) {
        this.wd = wd;
    }
}
