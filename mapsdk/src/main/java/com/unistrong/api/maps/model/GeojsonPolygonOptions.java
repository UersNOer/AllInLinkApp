package com.unistrong.api.maps.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *  定义了Geojson中polygon的相关参数设置对象。
 */
public class GeojsonPolygonOptions implements Parcelable{
    private float strokeWidth = 10.0F;
    private int strokeColor = -16777216;
    private int fillColor = -16777216;
    private float zIndex = 0.0F;
//    private boolean visible = true;
//    String a;

    /**
     * 创建PolygonOptions对象。
     */
    public GeojsonPolygonOptions() {
    }

    public static final Creator<GeojsonPolygonOptions> CREATOR = new GeojsonPolygonOptionsCreator();

    /**
     * 设置多边形边框宽度，单位：像素。默认为10
     * @param paramFloat - 多边形边框宽度。
     * @return 设置新的边框宽度后PolygonOptions 对象
     */
    public GeojsonPolygonOptions strokeWidth(float paramFloat)
    {
        this.strokeWidth = paramFloat;
        return this;
    }

    /**
     * 设置多边形边框颜色，32位 ARGB格式，默认为黑色。
     * @param paramInt - 多边形边框颜色。
     * @return 设置新的边框颜色后PolygonOptions 对象
     */
    public GeojsonPolygonOptions strokeColor(int paramInt)
    {
        this.strokeColor = paramInt;
        return this;
    }

    /**
     *设置多边形的填充颜色，32位ARGB格式。默认黑色。
     * @param paramInt - 多边形的填充颜色。
     * @return 设置新的填充颜色后PolygonOptions对象
     */
    public GeojsonPolygonOptions fillColor(int paramInt)
    {
        this.fillColor = paramInt;
        return this;
    }

    /**
     *设置多边形Z轴数值。
     * @param paramFloat - 多边形Z轴数值。
     * @return 设置新的Z轴数值后PolygonOptions 对象。
     */
    public GeojsonPolygonOptions zIndex(float paramFloat)
    {
        this.zIndex = paramFloat;
        return this;
    }

//    /**
//     *设置多边形的是否可见。默认为可见。
//     * @return 设置新的可见属性后PolygonOptions 对象。
//     */
//    public GeojsonPolygonOptions visible(boolean paramBoolean)
//    {
//        this.visible = paramBoolean;
//        return this;
//    }

    /**
     *返回多边形边框宽度。
     * @return 多边形边框宽度。
     */
    public float getStrokeWidth()
    {
        return this.strokeWidth;
    }
    /**
     *返回多边形边框颜色。
     * @return 多边形边框颜色。
     */
    public int getStrokeColor()
    {
        return this.strokeColor;
    }
    /**
     *返回多边形填充颜色。
     * @return 多边形填充颜色。
     */
    public int getFillColor()
    {
        return this.fillColor;
    }
    /**
     *返回多边形Z轴数值。
     * @return 多边形Z轴数值。
     */
    public float getZIndex()
    {
        return this.zIndex;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(strokeWidth);
        dest.writeInt(strokeColor);
        dest.writeInt(fillColor);
        dest.writeFloat(zIndex);
    }
//    /**
//     *返回多边形是否可见。
//     * @return True为可见；false为不可见。
//     */
//    public boolean isVisible()
//    {
//        return this.visible;
//    }
}
