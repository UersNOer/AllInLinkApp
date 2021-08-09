package com.unistrong.api.maps.model;



import java.util.UUID;

/**
 * 单个点的实体类，表示原始poi。
 */
public class QuadTreeNodeData {
    protected String SEQUENCE;
    private MarkerOptions markerOptions;

    /**
     * 返回MarkerOptions对象
     * @return   MarkerOptions对象
     */
    public MarkerOptions getMarkerOptions() {
        return markerOptions;
    }

    /**
     * 设置mark参数，仅仅对于mark有效，聚合点无效。
     * @param markerOptions  MarkerOptions对象。
     */
    public void setMarkerOptions(MarkerOptions markerOptions) {
        this.markerOptions = markerOptions;
        SEQUENCE = UUID.randomUUID().toString();
    }

}
