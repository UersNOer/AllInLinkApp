package com.example.android_supervisor.ui.upgrade;

import android.os.Parcel;
import android.os.Parcelable;

import com.tencent.bugly.beta.download.DownloadTask;


/**
 * @author wujie
 */
public class DownloadPecket implements Parcelable {
    private long costTime;
    private int downloadType;
    private String downloadUrl;
    private String md5;
    private long savedLength;
    private String saveFile;
    private int status;

    public DownloadPecket() {
    }

    public DownloadPecket(DownloadTask download) {
        costTime = download.getCostTime();
        downloadType = download.getDownloadType();
        downloadUrl = download.getDownloadUrl();
        md5 = download.getMD5();
        savedLength = download.getSavedLength();
        saveFile = download.getSaveFile().getAbsolutePath();
        status = download.getStatus();
    }

    protected DownloadPecket(Parcel in) {
        costTime = in.readLong();
        downloadType = in.readInt();
        downloadUrl = in.readString();
        md5 = in.readString();
        savedLength = in.readLong();
        saveFile = in.readString();
        status = in.readInt();
    }

    public static final Creator<DownloadPecket> CREATOR = new Creator<DownloadPecket>() {
        @Override
        public DownloadPecket createFromParcel(Parcel in) {
            return new DownloadPecket(in);
        }

        @Override
        public DownloadPecket[] newArray(int size) {
            return new DownloadPecket[size];
        }
    };

    public long getCostTime() {
        return costTime;
    }

    public void setCostTime(long costTime) {
        this.costTime = costTime;
    }

    public int getDownloadType() {
        return downloadType;
    }

    public void setDownloadType(int downloadType) {
        this.downloadType = downloadType;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getMD5() {
        return md5;
    }

    public void setMD5(String md5) {
        this.md5 = md5;
    }

    public long getSavedLength() {
        return savedLength;
    }

    public void setSavedLength(long savedLength) {
        this.savedLength = savedLength;
    }

    public String getSaveFile() {
        return saveFile;
    }

    public void setSaveFile(String saveFile) {
        this.saveFile = saveFile;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(costTime);
        dest.writeInt(downloadType);
        dest.writeString(downloadUrl);
        dest.writeString(md5);
        dest.writeLong(savedLength);
        dest.writeString(saveFile);
        dest.writeInt(status);
    }
}
