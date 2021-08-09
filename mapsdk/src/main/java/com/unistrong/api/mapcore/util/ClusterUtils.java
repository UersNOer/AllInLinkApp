package com.unistrong.api.mapcore.util;

import com.unistrong.api.maps.model.BoundingBox;
import com.unistrong.api.maps.model.QuadTreeNodeData;
import com.unistrong.api.maps.model.LatLng;
import com.unistrong.api.maps.model.LatLngBounds;

import java.util.List;

/**
 * 聚合工具类
 */
public class ClusterUtils {
    /**
     * 获取最大的子节点BoundingBox范围
     * @param dataArray 子节点集合
     * @return          最大BoundingBox范围
     */
    public static BoundingBox quadTreeNodeDataArrayForPOIs(List<QuadTreeNodeData> dataArray) {
        if (dataArray == null)return null;
        LatLng firstNode = dataArray.get(0).getMarkerOptions().getPosition();
        double minX = firstNode.latitude;
        double maxX = firstNode.latitude;

        double minY = firstNode.longitude;
        double maxY = firstNode.longitude;

        int size = dataArray.size();
        if(size == 1){
            return new BoundingBox(minX, minY, maxX+10, maxY+10);
        }
        for (int i = 0; i < size; i++){
//            dataArray[i] = QuadTreeNodeDataForLDPOI(pois[i]);
            LatLng nodeI =  dataArray.get(i).getMarkerOptions().getPosition();
            if (nodeI.latitude < minX) {
                minX = nodeI.latitude;
            }

            if (nodeI.latitude > maxX) {
                maxX = nodeI.latitude;
            }

            if (nodeI.longitude < minY) {
                minY = nodeI.longitude;
            }

            if (nodeI.longitude > maxY) {
                maxY = nodeI.longitude;
            }
        }
        return new BoundingBox(minX, minY, maxX, maxY);
    }

    /**
     * 判断点是否落举行区间内。
     * @param box
     * @param data
     * @return
     */
    public static boolean boundingBoxContainsData(BoundingBox box, QuadTreeNodeData data) {
        if (box != null && data != null){
            boolean containsX = (box.x0 <= data.getMarkerOptions().getPosition().latitude) && (data.getMarkerOptions().getPosition().latitude <= box.xm);
            boolean containsY = (box.y0 <= data.getMarkerOptions().getPosition().longitude) && (data.getMarkerOptions().getPosition().longitude <= box.ym);

            return containsX && containsY;
        }
        return false;
    }

    /**
     * 判断两个bound是否包含
     * @param b1
     * @param b2
     * @return
     */
    public static boolean boundingBoxIntersectsBoundingBox(BoundingBox b1, BoundingBox b2) {
        if (b1 != null && b2 != null){
            return ((b1.x0 <= b2.xm) && (b1.xm >= b2.x0) && (b1.y0 <= b2.ym) && (b1.ym >= b2.y0));
        }
        return false;
    }

    /**
     * 将举行范围转换为boundingbox对象
     * @param mapRect
     * @return
     */
    public static BoundingBox BoundingBoxForMapRect(LatLngBounds mapRect) {
        double minLat = mapRect.southwest.latitude;
        double maxLat = mapRect.northeast.latitude;
        double minLon = mapRect.southwest.longitude;
        double maxLon = mapRect.northeast.longitude;
        return new BoundingBox(minLat, minLon, maxLat, maxLon);
    }
}
