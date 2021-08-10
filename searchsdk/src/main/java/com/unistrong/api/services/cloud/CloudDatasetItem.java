package com.unistrong.api.services.cloud;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 此类定义了一个云图数据集对象。
 */
public class CloudDatasetItem implements Parcelable{

    /**
     * 数据集的唯一标识。
     */
    private long id;
    /**
     * 用户id。
     */
    private long userId;
    /**
     * 数据集名称。
     */
    private String name;
    /**
     * 数据集类型：1--点，2--线，3--面。
     */
    private int geoType;

    private List<DBFieldInfo> fieldInfos;
    public static final Creator<CloudDatasetItem> CREATOR = new CloudDatasetItemCreator();

    public CloudDatasetItem() {
    }

//    /**
//     * 构造方法
//     * @param id 数据集id。
//     * @param userId 用户id。
//     * @param name 数据集名称。
//     * @param geoType 数据集类型 。1--点，2--线，3--面。
//     */
//     CloudDatasetItem(long id, long userId, String name, int geoType) {
//        this.id = id;
//        this.userId = userId;
//        this.name = name;
//        this.geoType = geoType;
//    }

    /**
     * 构造方法
     * @param name 数据集名称。
     * @param geoType 数据集类型 。1--点，2--线，3--面。
     * @param fieldInfos 数据集字段属性集合。
     */
    public CloudDatasetItem(String name, int geoType,List<DBFieldInfo> fieldInfos){
        this.name=name;
        this.geoType=geoType;
        this.fieldInfos=fieldInfos;
    }

    protected CloudDatasetItem(Parcel in) {
        id = in.readLong();
        userId = in.readLong();
        name = in.readString();
        geoType = in.readInt();
    }

    /**
     * 获取数据集id.
     * @return 数据集id.
     */
    public long getId() {
        return id;
    }

    /**
     * 设置数据集id.
     * @param id 数据集id.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * 获取用户id.
     * @return 用户id.
     */
    public long getUserId() {
        return userId;
    }

    /**
     * 设置用户id.
     * @param userId 用户id.
     */
    public void setUserId(long userId) {
        this.userId = userId;
    }

    /**
     * 获取数据集名称
     * @return 数据集名称。
     */
    public String getName() {
        return name;
    }

    /**
     * 设置数据集名称。
     * @param name 数据集名称。
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取数据集类型。
     * @return 数据集类型。1,点;2,面;3,面。
     */
    public int getGeoType() {
        return geoType;
    }

    /**
     * 设置数据集类型
     * @param geoType 数据集类型
     */
    public void setGeoType(int geoType) {
        this.geoType = geoType;
    }

    /**
     * 获取数据集存储的字段属性信息集合。
     * @return 数据集存储的字段属性信息集合。
     */
    public List<DBFieldInfo> getFieldInfos() {
        return fieldInfos;
    }
    /**
     * 设置数据集存储的字段属性信息集合。
     * @param fieldInfos 数据集存储的字段属性信息集合。
     */
    public void setFieldInfos(List<DBFieldInfo> fieldInfos) {
        this.fieldInfos = fieldInfos;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CloudDatasetItem localCloudItem = (CloudDatasetItem) obj;
        if (this.id != localCloudItem.id) {
            return false;
        } else if (this.geoType != localCloudItem.geoType)
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        int i1 = 31;
        int i2 = 1;
        i2 = i1 * i2 + (String.valueOf(this.id) == null ? 0 : String.valueOf(this.id).hashCode());
        return i2;
    }

    @Override
    public String toString() {
        String toStr = "CloudDatasetItem [id = "+id+", userId = "+userId+", name = "+name+", geoType = "
                +geoType;
        return toStr;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeLong(userId);
        dest.writeString(name);
        dest.writeInt(geoType);
    }
}
