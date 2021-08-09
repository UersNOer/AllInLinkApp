package com.unistrong.api.maps.model;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * 节点范围，两个点表示
 */
public class BoundingBox implements Parcelable{
    public double x0;
    public double y0;
    public double xm;
    public double ym;

    public BoundingBox(double x0, double y0, double xm, double ym) {
        this.x0 = x0;
        this.y0 = y0;
        this.xm = xm;
        this.ym = ym;
    }

    protected BoundingBox(Parcel in) {
        x0 = in.readDouble();
        y0 = in.readDouble();
        xm = in.readDouble();
        ym = in.readDouble();
    }

    public static final Creator<BoundingBox> CREATOR = new BoundingBoxCreator();

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(x0);
        dest.writeDouble(y0);
        dest.writeDouble(xm);
        dest.writeDouble(ym);
    }

    @Override
    public String toString() {
        return "BoundingBox{" +
                "x0=" + x0 +
                ", y0=" + y0 +
                ", xm=" + xm +
                ", ym=" + ym +
                '}';
    }
}
