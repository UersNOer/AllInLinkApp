package com.unistrong.api.maps.model;

/**
 * 点聚合内部接口
 */
public interface ClusterBuildNistener {
    /**
     * 搜索点
     * @param x 经度
     * @param y 纬度
     */
    void searchNode(QuadTreeNodeData quadTreeNodeData, double x, double y);

}
