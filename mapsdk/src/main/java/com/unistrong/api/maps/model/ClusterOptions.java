package com.unistrong.api.maps.model;




import com.unistrong.api.mapcore.ClusterListener;
import java.util.ArrayList;
import java.util.List;

/**
 * 点聚合的参数构造类。
 */
public class ClusterOptions {
    // 最大聚合级别
    protected int maxClusterLevel = 17;
    // 最小聚合个数,至少是2
    protected int minClusterNum = 2;
    //聚合监听
    protected ClusterListener clusterListener;
    //聚合集合
    private List<QuadTreeNodeData> quadTreeNodeDataList = new ArrayList<QuadTreeNodeData>();
    //最大聚合容量
    protected int maxCapacity = 4;
    //地图上的区域
    protected LatLngBounds latLngBounds;
    /**
     * 设置最大聚合级别。
     * @param maxClusterLevel 最大聚合级别(取值范围是地图的缩放级别,默认17)。
     */
    public void setMaxClusterLevel(int maxClusterLevel) {
        this.maxClusterLevel = maxClusterLevel;
    }

    /**
     * 设置最小聚合个数。
     * @param minClusterNum 最小聚合个数(大于等于2,默认是2)。
     */
    public void setMinClusterNum(int minClusterNum) {
        if(minClusterNum <= 2 ){
            this.minClusterNum = 2;
        }
        this.minClusterNum = minClusterNum;
    }

    /**
     * 返回最大聚合级别。
     * @return 最大聚合级别。
     */
    public int getMaxClusterLevel() {
        return maxClusterLevel;
    }

    /**
     * 返回最小聚合个数。
     * @return 最小聚合个数。
     */
    public int getMinClusterNum() {
        return minClusterNum;
    }

    /**
     * 设置聚合监听
     * @param clusterListener 监听对象
     */
    public void setClusterListener(ClusterListener clusterListener) {
        this.clusterListener = clusterListener;
    }

    /**
     * 返回要聚合的点集合。
     * @return 要聚合的点集合。
     */
    public List<QuadTreeNodeData> getQuadTreeNodeDataList() {
        return quadTreeNodeDataList;
    }

    /**
     * 设置聚合的点集合
     * @param quadTreeNodeDataList 聚合集合
     */
    public void setQuadTreeNodeDataList(List<QuadTreeNodeData> quadTreeNodeDataList) {
        if(quadTreeNodeDataList == null){
            return;
        }
        this.quadTreeNodeDataList.clear();
        this.quadTreeNodeDataList.addAll(quadTreeNodeDataList);
    }

    /**
     * 添加集合
     * @param quadTreeNodeDataList
     */
    protected void addQuadTreeNodeDataList(List<QuadTreeNodeData> quadTreeNodeDataList){
        if(quadTreeNodeDataList == null){
            return;
        }
        this.quadTreeNodeDataList.addAll(quadTreeNodeDataList);
    }

    /**
     * 移除集合
     * @param quadTreeNodeDataList
     */
    protected void removeQuadTreeNodeDataList(List<QuadTreeNodeData> quadTreeNodeDataList){
        if(quadTreeNodeDataList == null){
            return;
        }
        this.quadTreeNodeDataList.removeAll(quadTreeNodeDataList);
    }

    /**
     * 设置区域
     * @param boundingBox
     */
    protected void setLatLngBounds(BoundingBox boundingBox){
        LatLng southwest = new LatLng(boundingBox.x0,boundingBox.y0);
        LatLng notheast = new LatLng(boundingBox.xm,boundingBox.ym);
        this.latLngBounds = new LatLngBounds(southwest,notheast);
    }

    /**
     * 返回一个矩形区域，在调用{@setClusterOptions(ClusterOptions options)}成功之后生效。
     * @return  矩形区域
     */
    public LatLngBounds getLatLngBounds() {
        return latLngBounds;
    }
}
