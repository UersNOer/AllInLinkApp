package com.unistrong.api.maps.model;


import java.util.ArrayList;
import java.util.List;

public class QuadTreeNode {
    //聚合范围
    public BoundingBox boundingBox;
    // 四个节点
    public QuadTreeNode northEast;
    public QuadTreeNode northWest;
    public QuadTreeNode southEast;
    public QuadTreeNode southWest;
    //存放点的集合
    public List<QuadTreeNodeData> quadTreeNodeDataList = new ArrayList<QuadTreeNodeData>();
    //父节点
    public QuadTreeNode parentNode;
}
