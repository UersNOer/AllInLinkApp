package com.unistrong.api.maps.model;


import android.content.Context;
import android.os.RemoteException;
import android.util.LruCache;

import com.unistrong.api.mapcore.ClusterListener;
import com.unistrong.api.mapcore.IMapDelegate;
import com.unistrong.api.mapcore.MapDelegateImp;
import com.unistrong.api.mapcore.util.ClusterUtils;
import com.unistrong.api.maps.UnistrongException;
import com.leador.mapcore.DPoint;
import com.leador.mapcore.IPoint;
import com.leador.mapcore.MapProjection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 四叉树的构建,包含(增删改核心代码)
 */
public class ClusterBuild {
    /**
     * 四叉树的root节点
     */
    private QuadTreeNode quadTreeNode = null;
    //root Boundingbox
    private BoundingBox maxBoundingBox;
    //ClusterOptions 聚合选择
    private ClusterOptions clusterOptions;
    //代理
    private IMapDelegate iMapDelegate;
    //容量，一个聚合点的容量
    private int capacity;
    //聚合样式图片缓存
    private LruCache<Integer, BitmapDescriptor> mLruCache;
    //最大聚合树
    private double maxCludster, minClusterNumber;
    //上下文
    private Context context;
    //记录屏幕中聚合的范围
    private Map<String,QuadTreeNodeModel> currentMap = new HashMap<String,QuadTreeNodeModel>();
    //添加
    private Map<String,QuadTreeNodeModel> addMap = new HashMap<String,QuadTreeNodeModel>();
    //删除
    private Map<String,QuadTreeNodeModel> removeMap = new HashMap<String,QuadTreeNodeModel>();
    //记录数据对应的节点
    private Map<QuadTreeNodeData,QuadTreeNode> nodeDataList = new HashMap<QuadTreeNodeData,QuadTreeNode>();
    /**
     * 构造函数
     * @param clusterOptions
     * @param iMapDelegate
     */
    public ClusterBuild(ClusterOptions clusterOptions, IMapDelegate iMapDelegate){
        this.clusterOptions = clusterOptions;
        this.iMapDelegate = iMapDelegate;
        if(clusterOptions == null){
            return;
        }
        capacity = clusterOptions.maxCapacity;
        maxCludster = clusterOptions.getMaxClusterLevel();
        //默认最多会缓存80张图片作为聚合显示元素图片,根据自己显示需求和app使用内存情况,可以修改数量
        mLruCache = new LruCache<Integer, BitmapDescriptor>(80) {
            protected void entryRemoved(boolean evicted, Integer key, BitmapDescriptor oldValue,
                                        BitmapDescriptor newValue) {
                if(oldValue.getBitmap() != null && !oldValue.getBitmap().isRecycled()){
                    oldValue.getBitmap().recycle();
                }
            }
        };
        minClusterNumber = clusterOptions.getMinClusterNum();
        this.context = ((MapDelegateImp)this.iMapDelegate).getContext();
    }

    /**
     * 设置option
     * @param clusterOptions
     */
    public void setOption(ClusterOptions clusterOptions){
        this.clusterOptions = clusterOptions;
        capacity = clusterOptions.maxCapacity;
        minClusterNumber = clusterOptions.getMinClusterNum();
    }

    public List<Future> futures = new ArrayList<Future>();
    public ExecutorService service = Executors.newSingleThreadExecutor();

    /**
     * 1。构建四叉树
     */
    public void build(){
        //clear
        clear();
        Task createTask = new Task(CLUSTER_INIT);
        service.submit(createTask);
    }

    /**
     * 2。搜索四叉树
     */
    public void search(){
        //clear
        clear();
        //先停止后启动
        Future future = service.submit(new Task(SEARCH_INIT));
        futures.add(future);
    }

    /**
     * 3.新增mark
     * @param quadTreeNodeData
     */
    public void addQuad(List<QuadTreeNodeData> quadTreeNodeData){
        if(quadTreeNodeData == null || clusterOptions == null){
            return;
        }
        boolean needCreate = true;
        // 先执行插入操作,如果插入成功则执行聚合,否则由于某些插入数据不在根节点的范围内，需要重新构建四叉树
        for(QuadTreeNodeData quadTreeNod : quadTreeNodeData){
            try {
//                if(ClusterUtils.boundingBoxContainsData(maxBoundingBox, quadTreeNod)){
//                    addQuadTreeNode(quadTreeNode, quadTreeNod);
//                    needCreate = false;
//                }
                // 优化逻辑
                needCreate = !addQuadTreeNode(quadTreeNode, quadTreeNod);
            } catch (UnistrongException e) {
                e.printStackTrace();
            }
        }
        clusterOptions.addQuadTreeNodeDataList(quadTreeNodeData);

        // 要插入的数据不在根节点区域范围,根节点需要重新构建，即四叉树需要重新构建
        if(needCreate){
            createQuad(false);
        }

        // 先停止后启动
        clear();
        // 执行聚合过程
        Future future = service.submit(new Task(SEARCH_INIT));
        futures.add(future);
    }


    /**
     * 4.移除一个或多个点(先从节点中删除,再执行聚合)
     * @param list
     */
    public void removeQuad(List<QuadTreeNodeData> list){
        if(list == null || clusterOptions == null){
            return;
        }
        // 遍历要删除的点的集合
        for(QuadTreeNodeData quadTreeNodeData :list) {
            QuadTreeNode iNode = ClusterBuild.this.nodeDataList.get(quadTreeNodeData);
            if(iNode == null){
                continue;
            }
            // 从点对应的节点中删除该点的数据
            iNode.quadTreeNodeDataList.remove(quadTreeNodeData);
            // 从缓存中删除该点对应的键值对
            ClusterBuild.this.nodeDataList.remove(quadTreeNodeData);
        }

        clusterOptions.removeQuadTreeNodeDataList(list);

        //clear
        clear();
        //先停止后启动
        Future future = service.submit(new Task(SEARCH_INIT));
        futures.add(future);
    }

    /**
     * 5.清除mark
     */
    public void clearQuad(){
        for (String key : currentMap.keySet()) {
            QuadTreeNodeModel iq = currentMap.get(key);
            iq.status = 3;
            iq.addMarkerToMap();
        }
        currentMap.clear();
        mLruCache.evictAll();
        quadTreeNode = null;
        clusterOptions = null;
    }

    /**
     * 清除所有任务
     */
    public void clear(){
        for(Future f:futures){
            f.cancel(true);
        }
        futures.clear();
    }

    public class Task implements Runnable{
        private int arg;
        public Task(int arg){
            this.arg = arg;
        }
        @Override
        public void run() {
            synchronized (ClusterBuild.class){
                try {
                    switch (arg){
                        case CLUSTER_INIT:
                            createQuad(true);
                            break;
                        case SEARCH_INIT:
                            searchQuad();
                            break;
                    }
                    Thread.sleep(1);
                } catch (InterruptedException e) {

                }
            }
        }

    }
    private final static  int CLUSTER_INIT = 0;//初始化
    private final static  int SEARCH_INIT = 1;//搜索渲染

    /**
     * 获取cell大小
     * @param zoomLevel
     * @return
     */
    float CellSizeForZoomLevel(double zoomLevel) {
        /*zoomLevel越大，cellSize越小. */
        if (zoomLevel < 13.0) {
            return 64*2;
        }
        else if (zoomLevel <15.0) {
            return 32*2;
        }
        else if (zoomLevel <18.0) {
            return 24*2;
        }
        else if (zoomLevel < 20.0) {
            return 24*2;
        }
        return 64*2;
    }

    /**
     * 移除四叉树节点
     */
    private void removeQuadAll(){
        searchQuad();
    }

    /**
     * 添加四叉树节点
     */
    private void addQuadAll()  {
        searchQuad();
    }

    /**
     * 搜索四叉树,生成当前可视区域的所有聚合点。
     */
    private void searchQuad(){
        long time = System.currentTimeMillis();
        try {
            //获得  zoomLevel , zoomScale , visibleRect
            LatLngBounds visibleRect = iMapDelegate.getProjection().getVisibleRegion().latLngBounds;
            double zoomLevel = iMapDelegate.getZoomLevel();
            LatLng northeastL = visibleRect.northeast;
            LatLng southwestL = visibleRect.southwest;
            IPoint northeastI = new IPoint();
            IPoint southwestI = new IPoint();
            MapProjection.lonlat2Geo(northeastL.longitude, northeastL.latitude, northeastI);
            MapProjection.lonlat2Geo(southwestL.longitude, southwestL.latitude, southwestI);
            int minX = southwestI.x;
            int maxX = northeastI.x;
            int rectWidth = maxX - minX;
            int mapWidth = iMapDelegate.getMapWidth();
            double zoomScale = mapWidth * 1.0D / rectWidth;
            //获得 cellSize
            double cellSize = CellSizeForZoomLevel(zoomLevel);
            double scaleFactor = zoomScale / cellSize;

            // 要转化成地理坐标
            LatLng pNortheastL = visibleRect.northeast;
            LatLng pSouthwestL = visibleRect.southwest;
            IPoint pNortheastI = new IPoint();
            IPoint pSouthwestI = new IPoint();
            MapProjection.lonlat2Geo(pNortheastL.longitude, pNortheastL.latitude, pNortheastI);
            MapProjection.lonlat2Geo(pSouthwestL.longitude, pSouthwestL.latitude, pSouthwestI);
            int pMinX = (int)(pSouthwestI.x * scaleFactor);
            // int向下取整会影响精度
//            int pMaxX = (int)(pNortheastI.x * scaleFactor);
            int pMaxX = (int)(pNortheastI.x * scaleFactor)+1;
            int pMinY = (int)(pNortheastI.y * scaleFactor);
//            int pMaxY = (int)(pSouthwestI.y * scaleFactor);
            int pMaxY = (int)(pSouthwestI.y * scaleFactor)+1;
            //清除原先的聚合
            addMap.clear();
            removeMap.clear();
            removeMap.putAll(currentMap);
            //按原先划分的网格查找数据
            for(int x = pMinX;x<pMaxX;x++){
                for(int y=pMinY;y<pMaxY;y++){
                    searchQuad(x,y,zoomLevel,scaleFactor);
                }
            }
            //添加mark至图层中
            addClusterToMap();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将聚合后的点加入到平面中
     * @throws RemoteException
     */
    private void addClusterToMap() throws RemoteException {
        for (String key : removeMap.keySet()) {
            QuadTreeNodeModel iq = removeMap.get(key);
            iq.status = 3;
            iq.addMarkerToMap();
            currentMap.remove(iq.getSeqId());
        }
        for (String key : addMap.keySet()) {
            QuadTreeNodeModel iq = addMap.get(key);
            iq.status = 1;
            iq.addMarkerToMap();
            currentMap.put(iq.getSeqId(),iq);
        }
    }

    double totalX = 0;
    double totalY = 0;
    int count;
    /**
     * 搜索
     * @param x
     * @param y
     */
    public void searchQuad(int x,int y,double zoomLevel,double scaleFactor){
        IPoint northeI = new IPoint();
        IPoint southwI = new IPoint();
        northeI.x = (int) (x / scaleFactor + 1.0 / scaleFactor);
        northeI.y = (int) (y / scaleFactor);
        southwI.x = (int) (x / scaleFactor);
        southwI.y = (int) (y / scaleFactor + 1.0 / scaleFactor);
        DPoint southWestD = new DPoint();
        DPoint northEastD = new DPoint();
        MapProjection.geo2LonLat(southwI.x, southwI.y, southWestD);
        MapProjection.geo2LonLat(northeI.x, northeI.y, northEastD);
        LatLng northL = new LatLng(northEastD.y, northEastD.x);
        LatLng southL = new LatLng(southWestD.y, southWestD.x);
        LatLngBounds mapRect = new LatLngBounds(southL, northL);

        totalX = 0;
        totalY = 0;
        count = 0;

        List<QuadTreeNodeData> quadTreeNodeDataList = new ArrayList<QuadTreeNodeData>();
        quadTreeGatherDataInRange(quadTreeNode, ClusterUtils.BoundingBoxForMapRect(mapRect), quadTreeNodeDataList, new ClusterBuildNistener() {

            @Override
            public void searchNode(QuadTreeNodeData quadTreeNodeData, double x, double y) {
                totalX += x;
                totalY += y;
                count++;
            }
        });

        if(quadTreeNodeDataList.size() == 0){
            return;
        }

        //大于最大聚合数
        if(zoomLevel>maxCludster){
            dealQuadNode(quadTreeNodeDataList,x,y);
            return;
        }

        //超过最小聚合数，不进行聚合。
        if (minClusterNumber <= 1) minClusterNumber = 2;
        if(quadTreeNodeDataList.size() < minClusterNumber){
            dealQuadNode(quadTreeNodeDataList,x,y);
            return;
        }

        QuadTreeNodeModel quadTreeNodeModel = new QuadTreeNodeModel(iMapDelegate,mLruCache);

        //单个聚合点
        if(quadTreeNodeDataList.size() == 1){
            LatLng latLng = quadTreeNodeDataList.get(0).getMarkerOptions().getPosition();
            quadTreeNodeModel.lng = latLng;
        }

        //聚合点
        if(quadTreeNodeDataList.size() > 1){
            LatLng  latLng = new LatLng(totalX / count, totalY / count);
            quadTreeNodeModel.lng = latLng;
        }

        quadTreeNodeModel.quadTreeNodeDataList = quadTreeNodeDataList;
        quadTreeNodeModel.x = x;
        quadTreeNodeModel.y = y;
        addOrRemove(quadTreeNodeModel);
    }

    /**
     * 将点全部转为mark模型
     * @param quadTreeNodeDataList
     */
    public void dealQuadNode(List<QuadTreeNodeData> quadTreeNodeDataList,int x,int y){
        for(QuadTreeNodeData quadTreeNodeData : quadTreeNodeDataList){
            LatLng latLng = quadTreeNodeData.getMarkerOptions().getPosition();
            List<QuadTreeNodeData> data = new ArrayList<QuadTreeNodeData>();
            data.add(quadTreeNodeData);
            QuadTreeNodeModel quadTreeNodeModelOne = new QuadTreeNodeModel(iMapDelegate,mLruCache);
            quadTreeNodeModelOne.lng = latLng;
            quadTreeNodeModelOne.quadTreeNodeDataList = data;
            quadTreeNodeModelOne.x = x;
            quadTreeNodeModelOne.y = y;
            addOrRemove(quadTreeNodeModelOne);
        }
    }

    /**
     * 控制视图内聚合数量
     * <p>当聚合点所在范围的点增加或减少时,QuadTreeNodeModel.getSeqId()会动态变化</p>
     * @param quadTreeNodeModel
     */
    public void addOrRemove(QuadTreeNodeModel quadTreeNodeModel){
        if(currentMap.get(quadTreeNodeModel.getSeqId()) == null){
            addMap.put(quadTreeNodeModel.getSeqId(),quadTreeNodeModel);
        } else {
            QuadTreeNodeModel q = currentMap.get(quadTreeNodeModel.getSeqId());
            Marker marker = q.mMarker;
            if(!marker.isVisible()){
                addMap.put(quadTreeNodeModel.getSeqId(), quadTreeNodeModel);
            }else{
                removeMap.remove(quadTreeNodeModel.getSeqId());
            }
        }
    }

    /**
     * 先序遍历 查找符合范围的节点
     * @param node
     * @param range
     * @param dataList
     */
    private void quadTreeGatherDataInRange(QuadTreeNode node, BoundingBox range, List<QuadTreeNodeData> dataList, ClusterBuildNistener clusterBuildNistener){
        if(node == null || range == null || dataList== null){
            return;
        }

        /* 若节点的覆盖范围与range不相交，则返回. */
        if (!ClusterUtils.boundingBoxIntersectsBoundingBox(node.boundingBox, range)) {
            return;
        }

        List<QuadTreeNodeData> quadTreeNodeDataList = node.quadTreeNodeDataList;
        if(quadTreeNodeDataList == null){
            return;
        }

        int size = quadTreeNodeDataList.size();
        for (int i = 0; i < size; i++) {
                 /* 若节点数据在range内，则调用block记录. */
            if (ClusterUtils.boundingBoxContainsData(range, quadTreeNodeDataList.get(i))) {
                dataList.add(quadTreeNodeDataList.get(i));

                if(clusterBuildNistener == null){
                    continue;
                }
                if(quadTreeNodeDataList.get(i).getMarkerOptions() == null){
                    continue;
                }

                LatLng lng = quadTreeNodeDataList.get(i).getMarkerOptions().getPosition();
                clusterBuildNistener.searchNode(quadTreeNodeDataList.get(i),lng.latitude,lng.longitude);
            }
        }

            /* 若已是叶子节点，返回. */
        if (node.northWest == null) {
            return ;
        }

            /* 不是叶子节点，继续向下查找. */
        quadTreeGatherDataInRange(node.northWest, range, dataList, clusterBuildNistener);
        quadTreeGatherDataInRange(node.northEast, range, dataList, clusterBuildNistener);
        quadTreeGatherDataInRange(node.southWest, range, dataList, clusterBuildNistener);
        quadTreeGatherDataInRange(node.southEast, range, dataList, clusterBuildNistener);
    }

    /**
     * 创建四叉树（初始化）
     * @param init 是否回调四叉树构建成功
     */
    private void createQuad(boolean init){
        if(clusterOptions == null){
            return;
        }
        if(clusterOptions.getQuadTreeNodeDataList() == null
                || clusterOptions.getQuadTreeNodeDataList().size() == 0){
            return;
        }

        List<QuadTreeNodeData> dataList = clusterOptions.getQuadTreeNodeDataList();
        maxBoundingBox = ClusterUtils.quadTreeNodeDataArrayForPOIs(dataList);
        clusterOptions.setLatLngBounds(maxBoundingBox);
        try {
            quadTreeBuildWithData(clusterOptions.getQuadTreeNodeDataList());
        } catch (UnistrongException e) {
            e.printStackTrace();
        }

        ClusterListener listener = clusterOptions.clusterListener;
        if(listener == null){
            return;
        }
        if(init){
            listener.builderFinish();
        }
    }

    /**
     * 将list转为四叉树
     * @param datas
     */
    public void quadTreeBuildWithData(List<QuadTreeNodeData> datas) throws UnistrongException {
        if(maxBoundingBox == null){
            return;
        }
        //clear
        quadTreeNode = null;
        ClusterBuild.this.nodeDataList.clear();
        //reset
        int size = datas.size();
        for (int i = 0; i < size; i++) {
            QuadTreeNodeData quadTreeNodeData = datas.get(i);
            if(i == 0 && quadTreeNode == null){
                quadTreeNode = new QuadTreeNode();
                quadTreeNode.boundingBox = maxBoundingBox;
                quadTreeNode.quadTreeNodeDataList.add(quadTreeNodeData);
                ClusterBuild.this.nodeDataList.put(quadTreeNodeData, quadTreeNode);
            }else{
                addQuadTreeNode(quadTreeNode, quadTreeNodeData);
            }
        }
    }

    /**
     * 添加一个点的数据到四叉树的节点中(如果已有的节点未满，则放入对应的节点;否则向下叶子节点,再插入)。
     * @param data 单个点的数据
     * @throws UnistrongException
     */
    public boolean addQuadTreeNode(QuadTreeNode node, QuadTreeNodeData data) throws UnistrongException {
        if(data == null){
            throw  new UnistrongException("QuadTreeNodeData is null");
        }

        //判断节点是否在象限内
        if (!ClusterUtils.boundingBoxContainsData(node.boundingBox, data)) {
            return false;
        }

        //象限内的容量是否填满，满则不插入
        int size = node.quadTreeNodeDataList.size();
        if (size < capacity) {
            node.quadTreeNodeDataList.add(data);
            ClusterBuild.this.nodeDataList.put(data,node);
            return true;
        }

            /* 若节点容量已满，且该节点为叶子节点，则向下扩展. */
        if (node.northWest == null) {
            quadTreeNodeSubdivide(node);
        }

        if (addQuadTreeNode(node.northWest, data)) return true;
        if (addQuadTreeNode(node.northEast, data)) return true;
        if (addQuadTreeNode(node.southWest, data)) return true;
        if (addQuadTreeNode(node.southEast, data)) return true;
        return false;
    }

    /**
     * 向下扩展
     * @param node
     */
    private void quadTreeNodeSubdivide(QuadTreeNode node) {
        if (node == null)return;
        BoundingBox box = node.boundingBox;

        double xMid = (box.xm + box.x0) / 2.0;
        double yMid = (box.ym + box.y0) / 2.0;

        BoundingBox northWest = new BoundingBox(box.x0, box.y0, xMid, yMid);
        node.northWest = new QuadTreeNode();
        node.northWest.parentNode = node;
        node.northWest.boundingBox = northWest;

        BoundingBox northEast = new BoundingBox(xMid, box.y0, box.xm, yMid);
        node.northEast = new QuadTreeNode();
        node.northEast.parentNode = node;
        node.northEast.boundingBox = northEast;

        BoundingBox southWest = new BoundingBox(box.x0, yMid, xMid, box.ym);
        node.southWest = new QuadTreeNode();
        node.southWest.parentNode = node;
        node.southWest.boundingBox = southWest;

        BoundingBox southEast = new BoundingBox(xMid, yMid, box.xm, box.ym);
        node.southEast = new QuadTreeNode();
        node.southEast.parentNode = node;
        node.southEast.boundingBox = southEast;
    }
}
