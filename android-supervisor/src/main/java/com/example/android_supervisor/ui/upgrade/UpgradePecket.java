package com.example.android_supervisor.ui.upgrade;

import android.os.Parcel;
import android.os.Parcelable;

import com.tencent.bugly.beta.UpgradeInfo;

/**
 * @author wujie
 */
public class UpgradePecket implements Parcelable {
    private String id = "";
    private String title = "";
    private String newFeature = "";
    private long publishTime = 0L;
    private int publishType = 0; // 发布类型（0:测试 1:正式）
    private int upgradeType = 1; // 弹窗类型（1:建议 2:强制 3:手工）
    private int popTimes = 0;
    private long popInterval = 0L;
    private int versionCode;
    private String versionName = "";
    private String apkMd5;
    private String apkUrl;
    private long fileSize;
    private String imageUrl;
    private int updateType;

    public UpgradePecket() {
    }

    public UpgradePecket(UpgradeInfo ui) {
        id = ui.id;
        title = ui.title;
        newFeature = ui.newFeature;
        publishTime = ui.publishTime;
        publishType = ui.publishType;
        upgradeType = ui.upgradeType;
        popTimes = ui.popTimes;
        popInterval = ui.popInterval;
        versionCode = ui.versionCode;
        versionName = ui.versionName;
        apkMd5 = ui.apkMd5;
        apkUrl = ui.apkUrl;
        fileSize = ui.fileSize;
        imageUrl = ui.imageUrl;
        updateType = ui.updateType;
    }

    private UpgradePecket(Parcel in) {
        id = in.readString();
        title = in.readString();
        newFeature = in.readString();
        publishTime = in.readLong();
        publishType = in.readInt();
        upgradeType = in.readInt();
        popTimes = in.readInt();
        popInterval = in.readLong();
        versionCode = in.readInt();
        versionName = in.readString();
        apkMd5 = in.readString();
        apkUrl = in.readString();
        fileSize = in.readLong();
        imageUrl = in.readString();
        updateType = in.readInt();
    }

    public static final Creator<UpgradePecket> CREATOR = new Creator<UpgradePecket>() {
        @Override
        public UpgradePecket createFromParcel(Parcel in) {
            return new UpgradePecket(in);
        }

        @Override
        public UpgradePecket[] newArray(int size) {
            return new UpgradePecket[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNewFeature() {
        return newFeature;
    }

    public void setNewFeature(String newFeature) {
        this.newFeature = newFeature;
    }

    public long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(long publishTime) {
        this.publishTime = publishTime;
    }

    public int getPublishType() {
        return publishType;
    }

    public void setPublishType(int publishType) {
        this.publishType = publishType;
    }

    public int getUpgradeType() {
        return upgradeType;
    }

    public void setUpgradeType(int upgradeType) {
        this.upgradeType = upgradeType;
    }

    public int getPopTimes() {
        return popTimes;
    }

    public void setPopTimes(int popTimes) {
        this.popTimes = popTimes;
    }

    public long getPopInterval() {
        return popInterval;
    }

    public void setPopInterval(long popInterval) {
        this.popInterval = popInterval;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getApkMd5() {
        return apkMd5;
    }

    public void setApkMd5(String apkMd5) {
        this.apkMd5 = apkMd5;
    }

    public String getApkUrl() {
        return apkUrl;
    }

    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getUpdateType() {
        return updateType;
    }

    public void setUpdateType(int updateType) {
        this.updateType = updateType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(newFeature);
        dest.writeLong(publishTime);
        dest.writeInt(publishType);
        dest.writeInt(upgradeType);
        dest.writeInt(popTimes);
        dest.writeLong(popInterval);
        dest.writeInt(versionCode);
        dest.writeString(versionName);
        dest.writeString(apkMd5);
        dest.writeString(apkUrl);
        dest.writeLong(fileSize);
        dest.writeString(imageUrl);
        dest.writeInt(updateType);
    }
}