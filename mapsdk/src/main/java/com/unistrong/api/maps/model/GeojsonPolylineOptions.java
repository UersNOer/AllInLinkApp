package com.unistrong.api.maps.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *  定义了Geojson中polyline的相关参数设置对象。
 */
public class GeojsonPolylineOptions implements Parcelable{
    private float width = 10.0F;
    private int color = -16777216;
    private float zIndex = 0.0F;
//    private boolean isVisible = true;
    private BitmapDescriptor bitmap;
//    private List<BitmapDescriptor> bitmapList;
//    private List<Integer> i;
//    private List<Integer> customTextureList;
    private boolean useTexture = true;
    private boolean geodesic = false;
    private boolean isDottedLine = false;
//    private boolean isUseGradient = false;

    /**
     * 构造函数。
     */
    public GeojsonPolylineOptions() {
    }

    public static final Creator<GeojsonPolylineOptions> CREATOR = new GeojsonPolylineOptionsCreator();

    /**
     * 设置是否使用纹理贴图画线。
     * @param useTexture - true，使用纹理贴图；false，不使用。默认为使用纹理贴图画线。
     * @return PolylineOptions对象。
     */
    public GeojsonPolylineOptions setUseTexture(boolean useTexture)
    {
        this.useTexture = useTexture;
        return this;
    }

    /**
     * 设置线段的纹理图，图片为2的n次方。如果不是，会自动放大至2的n次方。图片最好不大于128*128。
     * @param customTexture  - 用户设置线段的纹理。
     * @return GeojsonPolylineOptions 对象。
     */
    public GeojsonPolylineOptions setCustomTexture(BitmapDescriptor customTexture)
    {
        this.bitmap = customTexture;
        return this;
    }

    /**
     * 返回线段的纹理图。
     * @return 线段的纹理图。
     */
    public BitmapDescriptor getCustomTexture()
    {
        return this.bitmap;
    }

//    /**
//     *设置分段纹理list。
//     * @param customTextureList  - 纹理列表。
//     * @return 分段纹理list。
//     */
//    public GeojsonPolylineOptions setCustomTextureList(List<BitmapDescriptor> customTextureList)
//    {
//        this.bitmapList = customTextureList;
//        return this;
//    }

//    /**
//     * 获取分段纹理列表。
//     * @return 分段纹理列表。
//     */
//    public List<BitmapDescriptor> getCustomTextureList()
//    {
//        return this.bitmapList;
//    }

//    /**
//     * 设置分段纹理index。
//     * @param customTextureList - 分段纹理index集合。
//     * @return  分段纹理index列表。
//     */
//    public GeojsonPolylineOptions setCustomTextureIndex(List<Integer> customTextureList)
//    {
//        this.customTextureList = customTextureList;
//        return this;
//    }

//    /**
//     * 获取分段纹理index列表。
//     * @return 分段纹理index列表。
//     */
//    public List<Integer> getCustomTextureIndex()
//    {
//        return this.customTextureList;
//    }

//    /**
//     * 设置分段颜色。
//     * @param colors - 颜色列表。
//     * @return PolylineOptions对象。
//     */
//    public GeojsonPolylineOptions colorValues(List<Integer> colors)
//    {
//        this.i = colors;
//        return this;
//    }

//    /**
//     * 获取颜色列表。
//     * @return 颜色列表。
//     */
//    public List<Integer> getColorValues()
//    {
//        return this.i;
//    }

//    /**
//     * 设置线段是否使用渐变色。
//     * @param paramBoolean
//     * @return PolylineOptions对象。
//     */
//    public GeojsonPolylineOptions useGradient(boolean paramBoolean)
//    {
//        this.isUseGradient = paramBoolean;
//        return this;
//    }

//    /**
//     * 获取线段是否使用渐变色。
//     * @return true,使用;false,不使用。
//     */
//    public boolean isUseGradient()
//    {
//        return this.isUseGradient;
//    }

    /**
     * 获取线段是否使用纹理贴图。
     * @return true,使用纹理贴图;false,未使用
     */
    public boolean isUseTexture()
    {
        return this.useTexture;
    }

    /**
     * 获取线段是否为大地曲线,默认为false,不画大地曲线
     * @return true,为大地曲线;false 不为大地曲线。
     */
    public boolean isGeodesic()
    {
        return this.geodesic;
    }

    /**
     * 设置线段的宽度
     * @param width 宽度
     */
    public GeojsonPolylineOptions width(float width)
    {
        this.width = width;
        return this;
    }
    /**
     * 设置线段的颜色
     * @param color 颜色
     */
    public GeojsonPolylineOptions color(int color)
    {
        this.color = color;
        return this;
    }
    /**
     * 设置线段的Z轴值
     * @param zIndex Z轴值
     */
    public GeojsonPolylineOptions zIndex(float zIndex)
    {
        this.zIndex = zIndex;
        return this;
    }

//    /**
//     * 设置线段的可见性
//     * @param isVisible  true表示可见,false 表示不可见
//     */
//    public GeojsonPolylineOptions visible(boolean isVisible)
//    {
//        this.isVisible = isVisible;
//        return this;
//    }
    /**
     * 设置线段是否为大地曲线 默认为false
     * @param isGeodesic  true表示是大地曲线,false 表示不是大地曲线
     */
    public GeojsonPolylineOptions geodesic(boolean isGeodesic)
    {
        this.geodesic = isGeodesic;
        return this;
    }

    /**
     * 设置是否画虚线，默认为false，画实线。
     * @param isDottedLine
     */
    public GeojsonPolylineOptions setDottedLine(boolean isDottedLine)
    {
        this.isDottedLine = isDottedLine;
        return this;
    }

    /**
     * 获取线段是否为画虚线,默认false
     * @return true 画虚线;false 画实线
     */
    public boolean isDottedLine()
    {
        return this.isDottedLine;
    }

    /**
     * 获取线段的宽度
     * @return 线段的宽度
     */
    public float getWidth()
    {
        return this.width;
    }
    /**
     * 获取线段的颜色
     * @return 线段的颜色
     */
    public int getColor()
    {
        return this.color;
    }
    /**
     * 获取线段的Z轴值
     * @return 线段的Z轴值
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
    public void writeToParcel(Parcel paramParcel, int paramInt) {
//        paramParcel.writeTypedList(this.points);

        paramParcel.writeFloat(this.width);
        paramParcel.writeInt(this.color);
        paramParcel.writeFloat(this.zIndex);
//        paramParcel.writeString(this.a);
        paramParcel.writeBooleanArray(new boolean[] { this.isDottedLine, this.geodesic});
        if (this.bitmap != null) {
            paramParcel.writeParcelable(this.bitmap, paramInt);
        }
//        if (this.bitmapList != null) {
//            paramParcel.writeList(this.bitmapList);
//        }
//        if (this.customTextureList != null) {
//            paramParcel.writeList(this.customTextureList);
//        }
//        if (this.i != null) {
//            paramParcel.writeList(this.i);
//        }
    }
//    /**
//     * 获取线段是否可见
//     * @return true 可见;false 不可见
//     */
//    public boolean isVisible()
//    {
//        return this.isVisible;
//    }
}
