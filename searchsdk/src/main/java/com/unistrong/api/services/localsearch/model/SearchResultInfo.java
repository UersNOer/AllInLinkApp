package com.unistrong.api.services.localsearch.model;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * 搜索结果记录。
 */
public class SearchResultInfo implements Parcelable{
    public static final Parcelable.Creator<SearchResultInfo> CREATOR = new SearchResultInfoCreator();

    @Override
    public void writeToParcel(Parcel dest, int flags) {
       dest.writeDouble(this.longitude);
        dest.writeDouble(this.latitude);
        dest.writeString(this.name);
        dest.writeString(this.addr);
        dest.writeString(this.tel);
        dest.writeString(typeCode);
        dest.writeInt(this.typeCodeIndex);
        dest.writeInt(this.adcode);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * 经度信息。
     */
    public double longitude;

    /**
     * 纬度信息。
     */
    public double latitude;

    /**
     * 名称。
     */
    public String name;

    /**
     * 地址。
     */
    public String addr;

    /**
     * 电话。
     */
    public String tel;

    /**
     * 类型编码。
     */
    public String typeCode;

    /**
     * 类型编码索引。
     */
    public int typeCodeIndex;

    /**
     * 行政区划编码。
     */
    public int adcode;

    /**
     * SearchResultInfo的构造函数。
     */
    public SearchResultInfo() {
        super();
    }
    /**
     * Tip的构造函数。
     *
     * @param paramParcel
     *            序列化接口的实现实例。
     */
    public SearchResultInfo(Parcel paramParcel) {
        this.longitude = paramParcel.readDouble();
        this.latitude = paramParcel.readDouble();
        this.name = paramParcel.readString();
        this.addr = paramParcel.readString();
        this.tel = paramParcel.readString();
        this.typeCode = paramParcel.readString();
        this.typeCodeIndex = paramParcel.readInt();
        this.adcode = paramParcel.readInt();

    }
//    /**
//     * 获取纬度坐标。
//     * @return 纬度坐标。
//     */
//    public double getLatitude() {
//        return latitude;
//    }
//
//    /**
//     *
//     * @param latitude
//     */
//    public void setLatitude(double latitude) {
//        this.latitude = latitude;
//    }
//
//    /**
//     *
//     * @return
//     */
//    public String getName() {
//        return name;
//    }
//
//    /**
//     *
//     * @param name
//     */
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    /**
//     *
//     * @return
//     */
//    public String getAddr() {
//        return addr;
//    }
//
//    /**
//     *
//     * @param addr
//     */
//    public void setAddr(String addr) {
//        this.addr = addr;
//    }
//
//    /**
//     *
//     * @return
//     */
//    public String getTel() {
//        return tel;
//    }
//
//    /**
//     *
//     * @param tel
//     */
//    public void setTel(String tel) {
//        this.tel = tel;
//    }
//
//    /**
//     *
//     * @return
//     */
//    public String getTypeCode() {
//        return typeCode;
//    }
//
//    /**
//     *
//     * @param typeCode
//     */
//    public void setTypeCode(String typeCode) {
//        this.typeCode = typeCode;
//    }
//
//    /**
//     *
//     * @return
//     */
//    public int getTypeCodeIndex() {
//        return typeCodeIndex;
//    }
//
//    /**
//     *
//     * @param typeCodeIndex
//     */
//    public void setTypeCodeIndex(int typeCodeIndex) {
//        this.typeCodeIndex = typeCodeIndex;
//    }
//
//    /**
//     *
//     * @return
//     */
//    public int getAdcode() {
//        return adcode;
//    }
//
//    /**
//     *
//     * @param adcode
//     */
//    public void setAdcode(int adcode) {
//        this.adcode = adcode;
//    }
//
//    /**
//     *
//     * @return
//     */
//    public double getLongitude() {
//        return longitude;
//    }
//
//    /**
//     *设置经度坐标
//     * @param longitude 经度坐标
//     */
//    public void setLongitude(double longitude) {
//        this.longitude = longitude;
//    }
}
