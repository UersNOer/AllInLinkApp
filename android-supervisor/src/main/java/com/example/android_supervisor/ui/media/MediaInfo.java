package com.example.android_supervisor.ui.media;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;
import java.util.Date;

/**
 * @author wujie
 */
public class MediaInfo implements Parcelable {
    private long id;

    private String title;

    //图片、视频源地址
    private String originalPath;

    //图片、视频创建时间
    private long createDate;

    //图片、视频最后修改时间
    private long modifiedDate;

    private Long fileDate;

    //媒体类型
    private String mimeType;

    //宽
    private int width;
    //高
    private int height;

    //纬度
    private double latitude;
    //经度
    private double longitude;

    //图片方向
    private int orientation;

    //文件大小
    private long length;

    //缩略图地址
    private String thumbnailPath;

    public MediaInfo() {
    }

    protected MediaInfo(Parcel in) {
        id = in.readLong();
        title = in.readString();
        originalPath = in.readString();
        createDate = in.readLong();
        modifiedDate = in.readLong();
        mimeType = in.readString();
        width = in.readInt();
        height = in.readInt();
        latitude = in.readDouble();
        longitude = in.readDouble();
        orientation = in.readInt();
        length = in.readLong();
        thumbnailPath = in.readString();
    }

    public static final Creator<MediaInfo> CREATOR = new Creator<MediaInfo>() {
        @Override
        public MediaInfo createFromParcel(Parcel in) {
            return new MediaInfo(in);
        }

        @Override
        public MediaInfo[] newArray(int size) {
            return new MediaInfo[size];
        }
    };

    public Long getFileDate() {
        return fileDate;
    }

    public void setFileDate(Long fileDate) {
        this.fileDate = fileDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalPath() {
        return originalPath;
    }

    public void setOriginalPath(String originalPath) {
        this.originalPath = originalPath;
    }

    public long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public long getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(long modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(originalPath);
        dest.writeLong(createDate);
        dest.writeLong(modifiedDate);
        dest.writeString(mimeType);
        dest.writeInt(width);
        dest.writeInt(height);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeInt(orientation);
        dest.writeLong(length);
        dest.writeString(thumbnailPath);
    }

    public Long getFileTime(){
        File file = new File(originalPath);
        if (file.exists()){
            return file.lastModified();
        }
        return new Date().getTime();
    }
}
