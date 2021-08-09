package com.unistrong.api.maps.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;

import com.unistrong.api.maps.UnistrongException;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * 存储经纬度坐标值的类，单位角度。
 */
public class LatLng
        implements Parcelable, Cloneable {
    public static final LatLngCreator CREATOR = new LatLngCreator();
    public double latitude;
    public double longitude;
    private String TAG;
    private String updstatus;
    private static DecimalFormat a = new DecimalFormat("0.000000", new DecimalFormatSymbols(Locale.US));

    public String getTAG() {
        return TextUtils.isEmpty(TAG) ? "" : TAG;
    }

    public void setTAG(String TAG) {
        this.TAG = TAG;
    }

    /**
     * 构造LatLng 对象
     *
     * @param latitude  地点的纬度，在-90 与90 之间的double 型数值。
     * @param longitude 地点的经度，在-180 与180 之间的double 型数值。
     */
    public LatLng(double latitude, double longitude) {
        this(latitude, longitude, true);
    }

    public String getUpdstatus() {
        return updstatus;
    }

    public void setUpdstatus(String updstatus) {
        this.updstatus = updstatus;
    }

    /**
     * 使用传入的经纬度构造LatLng 对象，一对经纬度值代表地球上一个地点。
     *
     * @param latitude  - 地点的纬度，在-90 与90 之间的double 型数值。
     * @param longitude - 地点的经度，在-180 与180 之间的double 型数值。
     * @param isCheck   - 是否需要检查经纬度的合理性，建议填写true
     */
    public LatLng(double latitude, double longitude, boolean isCheck) {
        if (isCheck) {
            if ((-180.0D <= longitude) && (longitude < 180.0D)) {
                this.longitude = a(longitude);
            } else {
                this.longitude = a(((longitude - 180.0D) % 360.0D + 360.0D) % 360.0D - 180.0D);
            }
            if ((latitude < -90.0D) || (latitude > 90.0D)) {
                try {
                    throw new UnistrongException("非法坐标值");
                } catch (UnistrongException ex) {
                    ex.printStackTrace();
                    Log.d("leador", ex.getErrorMessage());
                }
            }
            this.latitude = a(Math.max(-90.0D, Math.min(90.0D, latitude)));
        } else {
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }

    private static double a(double paramDouble) {
        return Double.parseDouble(a.format(paramDouble));
    }

    /**
     * 克隆一个新坐标对象
     *
     * @return
     */
    public LatLng clone() {
        return new LatLng(this.latitude, this.longitude);
    }

    /**
     * 获取坐标的哈希值
     *
     * @return 坐标的哈希值
     */
    public int hashCode() {
        int i = 31;
        int j = 1;
        long l = Double.doubleToLongBits(this.latitude);
        j = i * j + (int) (l ^ l >>> 32);
        l = Double.doubleToLongBits(this.longitude);
        j = i * j + (int) (l ^ l >>> 32);
        return j;
    }

    /**
     * 判断是否与另一个LatLng对象相等的方法,如果两个地点的经纬度全部相同,则返回true,否则返回false
     *
     * @param latlng 用于比较的经纬度坐标
     * @return
     */
    public boolean equals(Object latlng) {
        if (this == latlng) {
            return true;
        }
        if (!(latlng instanceof LatLng)) {
            return false;
        }
        LatLng localLatLng = (LatLng) latlng;
        if (Double.doubleToLongBits(this.latitude) == Double.doubleToLongBits(localLatLng.latitude) &&
                Double.doubleToLongBits(this.longitude) == Double.doubleToLongBits(localLatLng.longitude)) {
            return true;
        }
        return false;
    }

    public String toString() {
        return "lat/lng: (" + this.latitude + "," + this.longitude + ")";
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel paramParcel, int paramInt) {
        paramParcel.writeDouble(this.longitude);
        paramParcel.writeDouble(this.latitude);
    }
}
