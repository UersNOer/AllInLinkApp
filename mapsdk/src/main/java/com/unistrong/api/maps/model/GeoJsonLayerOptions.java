package com.unistrong.api.maps.model;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * 定义了GeojsonOvrelay的参数设置对象。
 */
public final class GeoJsonLayerOptions implements Parcelable{

    private long dataSetId;
    private GeojsonMarkerOptions markerOptions;
    private GeojsonPolylineOptions polylineOptions;
    private GeojsonPolygonOptions polygonOptions;
//    private int cacheSize;
//    private int tileWidth = 256;
//    private int tileHeight = 256;

    /**
     * GeoJsonLayerOptions的构造方法
     */
    public GeoJsonLayerOptions() {
    }

    protected GeoJsonLayerOptions(Parcel in) {
        dataSetId = in.readLong();
        markerOptions = in.readParcelable(GeojsonMarkerOptions.class.getClassLoader());
        polylineOptions = in.readParcelable(GeojsonPolylineOptions.class.getClassLoader());
        polygonOptions = in.readParcelable(GeojsonPolygonOptions.class.getClassLoader());
    }

    public static final Creator<GeoJsonLayerOptions> CREATOR = new GeoJsonLayerOptionsCreator();

    /**
     * 返回数据集id。
     * @return 数据集id。
     */
    public long getDataSetId() {
        return dataSetId;
    }

    /**
     * 设置数据集id。
     * @param dataSetId
     *                 数据集id。
     * @return GeoJsonLayerOptions对象。
     */
    public GeoJsonLayerOptions setDataSetId(long dataSetId) {
        this.dataSetId = dataSetId;
        return  this;
    }

    /**
     * 获取marker的自定义样式参数对象。
     * @return marker的自定义样式参数对象。
     */
    public GeojsonMarkerOptions getMarkerOptions() {
        return markerOptions;
    }

    /**
     * 设置marker的自定义样式参数对象。
     * @param markerOptions
     *                     marker的自定义样式参数对象。
     * @return GeoJsonLayerOptions对象。
     */
    public GeoJsonLayerOptions setMarkerOptions(GeojsonMarkerOptions markerOptions) {
        this.markerOptions = markerOptions;
        return this;
    }

    /**
     * 返回polyline的自定义样式参数对象。
     * @return polyline的自定义样式参数对象。
     */
    public GeojsonPolylineOptions getPolylineOptions() {
        return polylineOptions;
    }

    /**
     * 设置polyline的自定义样式参数对象。
     * @param polylineOptions
     *                       polyline的自定义样式参数对象。
     * @return GeoJsonLayerOptions对象。
     */
    public GeoJsonLayerOptions setPolylineOptions(GeojsonPolylineOptions polylineOptions) {
        this.polylineOptions = polylineOptions;
        return this;
    }

    /**
     * 设置polygon的自定义样式参数对象。
     * @return polygon的自定义样式参数对象。
     */
    public GeojsonPolygonOptions getPolygonOptions() {
        return polygonOptions;
    }

    /**
     * 设置polygon的自定义样式参数对象。
     * @param polygonOptions
     *                      polygon的自定义样式参数对象。
     * @return GeoJsonLayerOptions对象。
     */
    public GeoJsonLayerOptions setPolygonOptions(GeojsonPolygonOptions polygonOptions) {
        this.polygonOptions = polygonOptions;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(dataSetId);
        dest.writeParcelable(markerOptions, flags);
        dest.writeParcelable(polylineOptions, flags);
        dest.writeParcelable(polygonOptions, flags);
    }

//    int getCacheSize() {
//        return cacheSize;
//    }
//
//    void setCacheSize(int cacheSize) {
//        this.cacheSize = cacheSize;
//    }
//
//    int getTileWidth() {
//        return tileWidth;
//    }
//
//    void setTileWidth(int tileWidth) {
//        this.tileWidth = tileWidth;
//    }
//
//    int getTileHeight() {
//        return tileHeight;
//    }
//
//    void setTileHeight(int tileHeight) {
//        this.tileHeight = tileHeight;
//    }
}
