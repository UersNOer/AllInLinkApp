package com.unistrong.api.services.cloud;

import android.os.Parcel;
import android.os.Parcelable;

import com.unistrong.api.services.core.LatLonPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 此类定义了一个云图的数据对象。
 */
public class CloudItem implements Parcelable {

    /**
     * 数据的唯一标识。
     */
    private long id;
    /**
     * 数据的经度（点数据的特有属性）。
     */
    private double lon;
    /**
     * 数据的纬度（点数据的特有属性）。
     */
    private double lat;
    /**
     * 数据的坐标串（线面数据的特有属性）。
     */
    private List<LatLonPoint> coordinates = new ArrayList<>();
    /**
     * 数据的城市编码。
     */
    private String adcode;
    /**
     * 数据集类型：1--点，2--线，3--面。
     */
    private int geoType;
    /**
     * 存储用户的自定义字段。
     */
    private HashMap<String, Object> extras = new HashMap<>();

    public static final Creator<CloudItem> CREATOR = new CloudItemCreator();

    /**
     * 云图检索结果的构造方法。
     */
    public CloudItem() {
    }

//    /**
//     * 云图检索结果的构造方法。
//     * @param id
//     *          数据的唯一标识。
//     * @param lon
//     *           数据的经度（点数据的特有属性）。
//     * @param lat
//     *           数据的纬度（点数据的特有属性）。
//     * @param coordinates
//     *                    数据的坐标串（线面数据的特有属性）。
//     * @param adcode
//     *              数据的城市编码。
//     * @param geoType
//     *               数据类型，分别表示点、线、面。
//     * @param extras
//     *              用户的自定义字段数据。
//     */
//     CloudItem(long id, double lon, double lat, List<LatLonPoint> coordinates, String adcode,
//                     int geoType, HashMap<String, Object> extras) {
//        this.id = id;
//        this.lon = lon;
//        this.lat = lat;
//        this.coordinates = coordinates;
//        this.adcode = adcode;
//        this.geoType = geoType;
//        this.extras = extras;
//    }

    /**
     * 云图点数据结构对象的构造方法。
     * @param lon
     *           数据的经度（点数据的特有属性）。
     * @param lat
     *           数据的纬度（点数据的特有属性）。
     * @param extras
     *              用户的自定义字段数据。
     */
    public CloudItem(double lon, double lat,HashMap<String, Object> extras){
        this.lon = lon;
        this.lat = lat;
        this.extras = extras;
    }
    /**
     * 云图线、面数据结构对象的构造方法。
     * @param coordinates
     *                    数据的坐标串（线面数据的特有属性）。
     * @param extras
     *              用户的自定义字段数据。
     */
    public CloudItem(List<LatLonPoint> coordinates,HashMap<String, Object> extras){
        this.extras = extras;
        this.coordinates = coordinates;
    }
    /**
     * 序列化。
     * @param in
     */
    protected CloudItem(Parcel in) {
        id = in.readLong();
        lon = in.readDouble();
        lat = in.readDouble();
        coordinates = in.createTypedArrayList(LatLonPoint.CREATOR);
        adcode = in.readString();
        geoType = in.readInt();
        extras = in.readHashMap(HashMap.class.getClassLoader());
    }

    /**
     * 返回数据的id。
     * @return id
     *           数据的唯一标识。
     */
    public long getId() {
        return id;
    }

    /**
     * 设置数据的id。
     * @param id
     *          数据的唯一标识。
     */
    void setId(long id) {
        this.id = id;
    }

    /**
     * 返回数据的经度（点数据的特有属性）。
     * @return 数据的经度（点数据的特有属性）。
     */
    public double getLon() {
        return lon;
    }

    /**
     * 设置数据的经度（点数据的特有属性）。
     * @param lon
     *           数据的经度（点数据的特有属性）。
     */
    void setLon(double lon) {
        this.lon = lon;
    }

    /**
     * 返回数据的经度（点数据的特有属性）。
     * @return 数据的纬度（点数据的特有属性）。
     */
    public double getLat() {
        return lat;
    }

    /**
     * 设置数据的纬度（点数据的特有属性）。
     * @param lat
     *           数据的纬度（点数据的特有属性）。
     */
    void setLat(double lat) {
        this.lat = lat;
    }

    /**
     * 返回数据的坐标串（线面数据的特有属性）。
     * @return 数据的坐标串（线面数据的特有属性）。
     */
    public List<LatLonPoint> getCoordinates() {
        return coordinates;
    }

    /**
     * 设置数据的坐标串（线面数据的特有属性）。
     * @param coordinates
     *                   数据的坐标串（线面数据的特有属性）。
     */
    void setCoordinates(List<LatLonPoint> coordinates) {
        this.coordinates = coordinates;
    }

    /**
     * 获取数据的城市编码。
     * @return 数据的城市编码。
     */
    public String getAdcode() {
        return adcode;
    }

    /**
     * 设置数据的城市编码。
     * @param adcode
     *              数据的城市编码。
     */
    void setAdcode(String adcode) {
        this.adcode = adcode;
    }


    /**
     * 返回数据类型。
     * @return 数据类型1、2、3分别表示点、线、面。
     */
    public int getGeoType() {
        return geoType;
    }

    /**
     * 设置数据类型。
     * @param geoType
     *               数据类型1、2、3分别表示点、线、面。
     */
    public void setGeoType(int geoType) {
        this.geoType = geoType;
    }

    /**
     * 返回用户的自定义字段数据。
     * @return 用户的自定义字段数据,存储的value必须是基本类型。
     */
    public HashMap<String, Object> getExtras() {
        return extras;
    }

    /**
     * 设置用户的自定义字段数据。
     * @param extras
     *              用户的自定义字段数据,存储的value必须是基本类型。
     */
    public void setExtras(HashMap<String, Object> extras) {
        this.extras = extras;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
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
        String toStr = "CloudItem [id = "+id+", lon = "+lon+", lat = "+lat+", coordinates = "
                +coordinates+", adcode = "+adcode+", geoType = "+geoType+", extras = "+extras;
        return toStr;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CloudItem localCloudItem = (CloudItem) obj;
        if (this.id != localCloudItem.id) {
            return false;
        } else if (this.lon != localCloudItem.lon && this.lat != localCloudItem.lat)
            return false;
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeDouble(lon);
        dest.writeDouble(lat);
        dest.writeTypedList(coordinates);
        dest.writeString(adcode);
        dest.writeInt(geoType);
        dest.writeMap(extras);
    }
}
