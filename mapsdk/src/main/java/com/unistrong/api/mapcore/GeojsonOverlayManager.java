package com.unistrong.api.mapcore;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Handler;
import android.os.RemoteException;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.unistrong.api.mapcore.util.AppInfo;
import com.unistrong.api.mapcore.util.AsyncTaskDecode;
import com.unistrong.api.mapcore.util.ImageWorkerDecode;
import com.unistrong.api.mapcore.util.SDKLogHandler;
import com.unistrong.api.mapcore.util.Util;
import com.unistrong.api.maps.model.BitmapDescriptor;
import com.unistrong.api.maps.model.GeoJsonLayerOptions;
import com.unistrong.api.maps.model.GeojsonMarkerOptions;
import com.unistrong.api.maps.model.GeojsonPolygonOptions;
import com.unistrong.api.maps.model.GeojsonPolylineOptions;
import com.unistrong.api.maps.model.LatLng;
import com.unistrong.api.maps.model.Marker;
import com.unistrong.api.maps.model.MarkerOptions;
import com.unistrong.api.maps.model.PolygonOptions;
import com.unistrong.api.maps.model.PolylineOptions;
import com.leador.mapcore.DPoint;
import com.leador.mapcore.FPoint;
import com.leador.mapcore.IPoint;
import com.leador.mapcore.MapProjection;
import com.leador.mapcore.Tile;
import com.leador.mapcore.geojson.CacheManager;
import com.leador.mapcore.geojson.GeoJsonCallback;
import com.leador.mapcore.geojson.GeoJsonDownTile;
import com.leador.mapcore.geojson.GeoJsonItem;
import com.leador.mapcore.geojson.GeoJsonTaskManager;

import java.io.Serializable;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.microedition.khronos.opengles.GL10;


class GeojsonOverlayManager implements IMapOverlayImageView, IGLOverlayLayer, GeoJsonCallback {
    IMapDelegate map;
    // 当前屏幕中的图块集合,每次变化后要更新
    private CopyOnWriteArrayList<TileCoordinate> screenTileList = new CopyOnWriteArrayList<>();
    // 地图事件回调时重新计算的即将要绘制的图块集合
//    private CopyOnWriteArrayList<TileCoordinate> drawTileList = new CopyOnWriteArrayList<>();

    private GeoJsonLayerOptions options;
    private GeoJsonTaskManager taskManager;
    private CacheManager cacheManager;
    // tile
    private int tileWidth = 256;
    private int tileHeight = 256;
    private int cacheSize;
    private int iLayerIndex = -1; //j
//    private boolean isFlingState = false; //m
    private TileServer tileServer = null; //n

    private boolean e = false; //e
    // marker manager
    private CopyOnWriteArrayList<IMarkerDelegate> markers = new CopyOnWriteArrayList<IMarkerDelegate>(new ArrayList(500));
    private CopyOnWriteArrayList<OverlayTextureItem> overlayitem = new CopyOnWriteArrayList<OverlayTextureItem>();
    private CopyOnWriteArrayList<Integer> recycleTextureIds = new CopyOnWriteArrayList<Integer>();
    Comparetor comparetor = new Comparetor();
    private Handler handler = new Handler();

    private Runnable i = new Runnable(){
        public synchronized void run()
        {
            //av.a(this.a);
            changeOverlayIndex();
        }
    };
    // overlay manager
    private CopyOnWriteArrayList<IOverlayDelegateDecode> overlayList = new CopyOnWriteArrayList(new ArrayList(500)); // d
    private CopyOnWriteArrayList<Integer> TexsureIdList = new CopyOnWriteArrayList<Integer>(); //e
    private Handler f = new Handler(); //f

    GLOverlayLayerDecode.a comparator = new GLOverlayLayerDecode.a(); //b
    private Runnable runnable = new Runnable(){ //g
        public synchronized void run()
        {
            try
            {
                synchronized (GeojsonOverlayManager.this)
                {
                    ArrayList<IOverlayDelegateDecode> localArrayList = new ArrayList<IOverlayDelegateDecode>(overlayList);
                    Collections.sort(localArrayList, comparator);
                    overlayList = new CopyOnWriteArrayList<IOverlayDelegateDecode>(localArrayList);
                }
            }
            catch (Throwable localThrowable)
            {
                SDKLogHandler.exception(localThrowable, "MapOverlayImageView", "changeOverlayIndex");
            }
        }
    };

    public GeojsonOverlayManager(Context context) {
    }

    public GeojsonOverlayManager(Context context, AttributeSet attrs, IMapDelegate map) {
        this.map = map;
    }

    public GeojsonOverlayManager(IMapDelegate map) {
        this.map = map;
        initTileCacheSize();
        // 71edea67409ec56f00c47dca66f32976
        String ak = AppInfo.getKey(((MapDelegateImp)this.map).getContext());
//        ak = "396b4b6316d3e97116a2de36175cf07e";
        this.taskManager = new GeoJsonTaskManager(this, ak,cacheManager);
    }

    // 初始化设置当前屏幕的瓦片容量
    public void initTileCacheSize(){
        // 这里获取到的map宽高可能为0
        int width = this.map.getMapWidth();
        int height = this.map.getMapHeight();
        if (width == 0 || height == 0){
            WindowManager wm = (WindowManager) ((MapDelegateImp)this.map).getContext()
                    .getSystemService(Context.WINDOW_SERVICE);
            width = wm.getDefaultDisplay().getWidth();
            height = wm.getDefaultDisplay().getHeight();
        }
        int cacheSize = (int)(width * height / (getTileWidth() * getTileHeight()));
        setCacheSize(cacheSize * 3);
//        CacheManager.getInstance().setCACHE_MAX_SIZE(cacheSize * 3);
        cacheManager = new CacheManager(cacheSize*3);
    }

    public GeoJsonLayerOptions getOptions() {
        return options;
    }

    public void setOptions(GeoJsonLayerOptions options) {
        this.options = options;
        if (this.taskManager != null){
            this.taskManager.setServerListener(this.map.getGeoJsonServerListener());
        }
    }

    public void refresh(boolean needDownload) {
//        if (this.isFlingState) {
//            return;
//        }
        if ((this.tileServer != null) &&
                (this.tileServer.getStatus() == AsyncTaskDecode.Status.RUNNING)) {
            this.tileServer.cancel(true);
        }
        this.tileServer = new TileServer(needDownload);
        this.tileServer.execute(map);
    }

    public void clearGeojsonOverlay(){
        long start = System.currentTimeMillis();
        // clear
        clear(null);
        clearOverlay(null);
//        CacheManager.release();
        if (cacheManager != null){
            cacheManager.clearCache();
        }
        if (!isListNull(screenTileList)){
            screenTileList.clear();
        }
//        if (!isListNull(drawTileList)){
//            drawTileList.clear();
//        }
    }

    public void destroyGeojsonOverlay(){
        // destroy
        destroy();
        destroyOverlay();
        // threadpool shutdown
        if (this.taskManager != null){
            this.taskManager.destory();
        }
        if (!isListNull(screenTileList)){
            screenTileList.clear();
        }
//        if (!isListNull(drawTileList)){
//            drawTileList.clear();
//        }
    }

    // marker----------------------------------------------------------------
    @Override
    public IMapDelegate getMapDelegate() {
        return this.map;
    }

    public void onDrawGL(GL10 gl) {
        for (Integer id : recycleTextureIds) {
            gl.glDeleteTextures(1, new int[]{id}, 0);
            this.map.deleteTexsureId(id.intValue());
        }

        this.recycleTextureIds.clear();
//        if ((this.mHitMarker != null) && (!this.mHitMarker.F())) {
//            postDraw();
//        }

        for (IMarkerDelegate marker : markers) {
            if ((marker.isInScreen()) || (marker.isInfoWindowShow())) {
                marker.drawMarker(gl, this.map);
            }
        }
    }

    public synchronized void addMarker(IMarkerDelegate paramag) {
        this.markers.add(paramag);
        changeIndexs();
    }

    public synchronized boolean removeMarker(IMarkerDelegate marker) {
//        hideInfoWindow(marker);
        return this.markers.remove(marker);
    }

    public synchronized int getMarkersSize() {
        return this.markers.size();
    }

    // 如果id不为空,则保留此id的marker,清除其余marker
    public synchronized void clear(String paramString) {
        try {
            int m = (paramString == null) || (paramString.trim().length() == 0) ? 1 : 0;
//            this.mHitMarker = null;
//            this.mHitMarkPoint = null;
            if (m != 0) {
                for (Iterator<IMarkerDelegate> localIterator = this.markers.iterator(); localIterator.hasNext();)
                {
                    IMarkerDelegate localag = (IMarkerDelegate)localIterator.next();
                    localag.remove();
                }
                this.markers.clear();
            } else {
                for (Iterator<IMarkerDelegate> localIterator = this.markers.iterator(); localIterator.hasNext();) {
                    IMarkerDelegate localag = (IMarkerDelegate)localIterator.next();
                    if (!paramString.equals(localag.getId())) {
                        localag.remove();
                    }
                }
            }
        }
        catch (RemoteException localRemoteException) {
            //Iterator localIterator;
            //ag localag;
            SDKLogHandler.exception(localRemoteException, "MapOverlayImageView", "clear");
            localRemoteException.printStackTrace();
        }
    }

    public synchronized void set2Top(IMarkerDelegate marker){
        try
        {
            if (this.markers.remove(marker))
            {
                changeOverlayIndex();
                this.markers.add(marker);
            }
        }
        catch (Throwable throwable)
        {
            SDKLogHandler.exception(throwable, "MapOverlayImageView", "set2Top");
        }
    }

    public synchronized void calFPoint() {
        for (IMarkerDelegate marker : this.markers) {
            try
            {
                if (marker.isVisible()) {
                    marker.calFPoint();
                }
            }
            catch (Throwable throwable)
            {
                SDKLogHandler.exception(throwable, "MapOverlayImageView", "calFPoint");

                throwable.printStackTrace();
            }
        }
    }

    public synchronized boolean isAnimator() {
        for (IMarkerDelegate localag : this.markers) {
            if (!localag.isAnimator()) {
                return false;
            }
        }
        return true;
    }

    public synchronized void addTexture(OverlayTextureItem overlayTextureItem) {
        if (overlayTextureItem == null) {
            return;
        }
        if (overlayTextureItem.getTextureId() == 0) {
            return;
        }
        this.overlayitem.add(overlayTextureItem);
    }

    public synchronized void removeMarker(int paramInt) {
        for (OverlayTextureItem localbd : this.overlayitem) {
            if (localbd.getTextureId() == paramInt) {
                this.overlayitem.remove(localbd);
            }
        }
    }

    public void recycleId(Integer paramInteger) {
        if (paramInteger.intValue() != 0) {
            this.recycleTextureIds.add(paramInteger);
        }
    }

    public synchronized int getTextureID(BitmapDescriptor paramBitmapDescriptor) {
        if ((paramBitmapDescriptor == null) || (paramBitmapDescriptor.getBitmap() == null) ||
                (paramBitmapDescriptor.getBitmap().isRecycled())) {
            return 0;
        }
        OverlayTextureItem overlayTextureItem = null;
        for (int m = 0; m < this.overlayitem.size(); m++)
        {
            overlayTextureItem = (OverlayTextureItem)this.overlayitem.get(m);
            if (overlayTextureItem.getBitmapDes().equals(paramBitmapDescriptor)) {
                return overlayTextureItem.getTextureId();
            }
        }
        return 0;
    }

    public synchronized void destroy() {
        try
        {
            for (IMarkerDelegate mk : markers) {
                if (mk != null)
                {
                    mk.destroy();
                    mk = null;
                }
            }
            // a(null);
            clear(null); //上一句翻译
            for (Iterator<OverlayTextureItem> localIterator = this.overlayitem.iterator(); localIterator.hasNext();)
            {
                OverlayTextureItem localObject = (OverlayTextureItem)localIterator.next();
                ((OverlayTextureItem)localObject).getBitmapDes().recycle();
            }
            this.overlayitem.clear();
        }
        catch (Throwable localThrowable)
        {
            SDKLogHandler.exception(localThrowable, "MapOverlayImageView", "destroy");
            localThrowable.printStackTrace();
            Log.d("leadorApi", "MapOverlayImageView clear erro" + localThrowable.getMessage());
        }
    }

    public boolean hitTest(Rect rect, int x, int y) {
        return rect.contains(x, y);
    }

    public synchronized List<Marker> getMapScreenMarkers() {
        ArrayList<Marker> localArrayList = new ArrayList<Marker>();
        try
        {
            Rect localRect = new Rect(0, 0, this.map.getMapWidth(), this.map.getMapHeight());
            FPoint fPoint = null;
            IPoint iPoint = new IPoint();
            for (IMarkerDelegate localag : this.markers) {
                if (!(localag instanceof TextDelegateImp))
                {
                    fPoint = localag.anchorUVoff();
                    if (fPoint != null)
                    {
                        this.map.getMapProjection().map2Win(fPoint.x, fPoint.y, iPoint);
                        if (hitTest(localRect, iPoint.x, iPoint.y)) {
                            localArrayList.add(new Marker(localag));
                        }
                    }
                }
            }
        }
        catch (Throwable localThrowable)
        {
//      Rect localRect;
//      FPoint localFPoint;
//      IPoint localIPoint;
            SDKLogHandler.exception(localThrowable, "MapOverlayImageView", "getMapScreenMarkers");

            localThrowable.printStackTrace();
        }
        return localArrayList;
    }

    // OnMapProcessEvent
    public synchronized void realdestroy() {
        for (IMarkerDelegate localag : this.markers) {
            if (localag.x()) {
                localag.realdestroy();
            }
        }
    }

    public synchronized void changeIndexs() {
        this.handler.removeCallbacks(this.i);
        this.handler.postDelayed(this.i, 10L);
    }

    private void changeOverlayIndex() {
        try {
            ArrayList<IMarkerDelegate> localArrayList = new ArrayList<IMarkerDelegate>(this.markers);

            Collections.sort(localArrayList, this.comparetor);
            this.markers = new CopyOnWriteArrayList<IMarkerDelegate>(localArrayList);
        }
        catch (Throwable throwable)
        {
            SDKLogHandler.exception(throwable, "MapOverlayImageView", "changeOverlayIndex");
        }
    }
    static class Comparetor implements Comparator<Object>, Serializable {
        public int compare(Object paramObject1, Object paramObject2)
        {
            IMarkerDelegate localag1 = (IMarkerDelegate)paramObject1;
            IMarkerDelegate localag2 = (IMarkerDelegate)paramObject2;
            try {
                if ((localag1 != null) && (localag2 != null))
                {
                    if (localag1.getZIndex() > localag2.getZIndex()) {
                        return 1;
                    }
                    if (localag1.getZIndex() < localag2.getZIndex()) {
                        return -1;
                    }
                }
            } catch (Throwable localThrowable) {
                SDKLogHandler.exception(localThrowable, "MapOverlayImageView", "compare");

                localThrowable.printStackTrace();
            }
            return 0;
        }

    }


    //    private final Handler j = new Handler();
//    private final Runnable reDrawRunnable = new Runnable(){
//        public void run()
//        {
//            try
//            {
//                //this.a.a.p();
//                map.redrawInfoWindow();
//            }
//            catch (Throwable throwable)
//            {
//                SDKLogHandler.exception(throwable, "MapOverlayImageView", "redrawInfoWindow post");
//
//                throwable.printStackTrace();
//            }
//        }
//    };

    public void postDraw() {
//        this.j.post(this.reDrawRunnable);
    }

    @Override
    public void hideInfoWindow(IMarkerDelegate marker) {

    }

    @Override
    public void showInfoWindow(IMarkerDelegate marker) {

    }

    @Override
    public IMarkerDelegate getLongPressHitMarker(MotionEvent motionEvent) {
        return null;
    }

    @Override
    public IMarkerDelegate getHitMarker() {
        return null;
    }

    @Override
    public boolean onSingleTap(MotionEvent motionEvent) throws RemoteException {
        return false;
    }

    // overlay----------------------------------------------------------------

    @Override
    public IMapDelegate getOverlayMapDelegate() {
        return this.map;
    }

    @Override
    public void onDrawGL(GL10 paramGL10, boolean paramBoolean, int paramInt) {
        for (Iterator<Integer> localIterator = this.TexsureIdList.iterator(); localIterator.hasNext();)
        {
            Integer localObject = (Integer)localIterator.next();
            paramGL10.glDeleteTextures(1, new int[] { ((Integer)localObject).intValue() }, 0);
            this.map.deleteTexsureId(((Integer) localObject).intValue());
        }
        this.TexsureIdList.clear();
        int overlaySize = this.overlayList.size();
        for (IOverlayDelegateDecode localai:overlayList)
        {
            try
            {
                if (localai.isVisible()) {
                    if (overlaySize > 20)
                    {
                        if (localai.a()) {
                            if (paramBoolean)
                            {
                                if (localai.getZIndex() <= paramInt) {
                                    localai.draw(paramGL10);
                                }
                            }
                            else if (localai.getZIndex() > paramInt) {
                                localai.draw(paramGL10);
                            }
                        }
                    }
                    else if (paramBoolean)
                    {
                        if (localai.getZIndex() <= paramInt) {
                            localai.draw(paramGL10);
                        }
                    }
                    else if (localai.getZIndex() > paramInt) {
                        localai.draw(paramGL10);
                    }
                }
            }
            catch (RemoteException localRemoteException)
            {
                SDKLogHandler.exception(localRemoteException, "GLOverlayLayer", "draw");
                localRemoteException.printStackTrace();
            }
        }
    }

    @Override
    public void clearOverlay(String id) {
        try
        {
            if ((id == null) || (id.trim().length() == 0))
            {
                this.overlayList.clear();
//                index = 0;
            }
            else
            {
                for (IOverlayDelegateDecode localai : this.overlayList) {
                    if (!id.equals(localai.getId())) {
                        this.overlayList.remove(localai);
                    }
                }
            }
        }
        catch (Throwable localThrowable)
        {
            SDKLogHandler.exception(localThrowable, "GLOverlayLayer", "clear");
            localThrowable.printStackTrace();
            Log.d("leadorApi", "GLOverlayLayer clear erro" + localThrowable.getMessage());
        }
    }

    @Override
    public void destroyOverlay() {
        try
        {
            for (IOverlayDelegateDecode localai : this.overlayList)
            {
                localai.destroy();
                localai = null;
            }
            clearOverlay(null);
        }
        catch (Throwable localThrowable)
        {
            SDKLogHandler.exception(localThrowable, "GLOverlayLayer", "destroy");
            localThrowable.printStackTrace();
            Log.d("leadorApi", "GLOverlayLayer destroy erro" + localThrowable.getMessage());
        }
    }

    @Override
    public void add(IOverlayDelegateDecode overlay) throws RemoteException {
        synchronized (GeojsonOverlayManager.this) {
            this.overlayList.add(overlay);
        }
        changeOverlayIndexs();
    }

    @Override
    public boolean removeOverlay(String id) throws RemoteException {
        IOverlayDelegateDecode localai = getIOverlay(id);
        if (localai != null) {
            return this.overlayList.remove(localai);
        }
        return false;
    }

    private synchronized IOverlayDelegateDecode getIOverlay(String paramString) // d
            throws RemoteException
    {
        for (IOverlayDelegateDecode localai : this.overlayList) {
            if ((localai != null) && (localai.getId().equals(paramString))) {
                return localai;
            }
        }
        return null;
    }

    @Override
    public void changeOverlayIndexs() {
        this.f.removeCallbacks(this.runnable);
        this.f.postDelayed(this.runnable, 10L);
    }

    @Override
    public void recycleOverlayId(Integer texsureId) {
        if (texsureId.intValue() != 0) {
            this.TexsureIdList.add(texsureId);
        }
    }

    @Override
    public void calMapFPoint() {
        for (IOverlayDelegateDecode localai : this.overlayList) {
            try
            {
                if (localai != null) {
                    localai.calMapFPoint();
                }
            }
            catch (RemoteException localRemoteException)
            {
                SDKLogHandler.exception(localRemoteException, "GLOverlayLayer", "calMapFPoint");
                localRemoteException.printStackTrace();
            }
        }
    }

    @Override
    public boolean checkInBounds() {
        for (IOverlayDelegateDecode localai : this.overlayList) {
            if ((localai != null) && (!localai.checkInBounds())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public IOverlayDelegateDecode polylineClick(LatLng paramLatLng) {
        return null;
    }


    // tile----------------------------------------------------------------


    public int getTileWidth() {
        return tileWidth;
    }

//    public void setTileWidth(int tileWidth) {
//        this.tileWidth = tileWidth;
//    }

    public int getTileHeight() {
        return tileHeight;
    }

//    public void setTileHeight(int tileHeight) {
//        this.tileHeight = tileHeight;
//    }

    public int getCacheSize() {
        return cacheSize;
    }

    public void setCacheSize(int cacheSize) {
        this.cacheSize = cacheSize;
    }

    private ArrayList<TileCoordinate> getTilesInDomain(int iZoolLevel, int iMapWidth, int iMapHeight){
        // level controll
        if (iZoolLevel < 10){
            // zoom小于10，不显示geojsonvOverlay
            clearGeojsonOverlay();
            return null;
        } else if (iZoolLevel >= 10 && iZoolLevel <= 11){
            iZoolLevel = 10;
        } else if (iZoolLevel >= 12 && iZoolLevel <= 13){
            iZoolLevel = 12;
        } else if (iZoolLevel >= 14){
            iZoolLevel = 14;
        }

        MapProjection prj = this.map.getMapProjection();
        int iCurZoomLevel = iZoolLevel;
        int iCenterTileX = 0;
        int iCenterTileY = 0;
        IPoint pointGeo = null;
        // geo bounds
        int minX = 0x7FFFFFFF;
        int maxX = 0;
        int minY = 0x7FFFFFFF;
        int maxY = 0;

        FPoint fpoint = new FPoint();
        IPoint ipoint = new IPoint();
        IPoint geoCenter = new IPoint();
        // top left
        prj.win2Map(0, 0, fpoint);
        prj.map2Geo(fpoint.x, fpoint.y, ipoint);
        minX = Math.min(minX, ipoint.x);
        maxX = Math.max(maxX, ipoint.x);
        minY = Math.min(minY, ipoint.y);
        maxY = Math.max(maxY, ipoint.y);
        // top right
        prj.win2Map(iMapWidth, 0, fpoint);
        prj.map2Geo(fpoint.x, fpoint.y, ipoint);
        minX = Math.min(minX, ipoint.x);
        maxX = Math.max(maxX, ipoint.x);
        minY = Math.min(minY, ipoint.y);
        maxY = Math.max(maxY, ipoint.y);
        // bottom left
        prj.win2Map(0, iMapHeight, fpoint);
        prj.map2Geo(fpoint.x, fpoint.y, ipoint);
        minX = Math.min(minX, ipoint.x);
        maxX = Math.max(maxX, ipoint.x);
        minY = Math.min(minY, ipoint.y);
        maxY = Math.max(maxY, ipoint.y);
        // bottom right
        prj.win2Map(iMapWidth, iMapHeight, fpoint);
        prj.map2Geo(fpoint.x, fpoint.y, ipoint);
        minX = Math.min(minX, ipoint.x);
        maxX = Math.max(maxX, ipoint.x);
        minY = Math.min(minY, ipoint.y);
        maxY = Math.max(maxY, ipoint.y);

        minX = minX - (1 << (20 - iCurZoomLevel)) * this.tileWidth;
        // maxX = maxX + (1 << (20 - iCurZoomLevel)) * this.tileWidth;
        minY = minY - (1 << (20 - iCurZoomLevel)) * this.tileHeight;
        // maxY = maxY + (1 << (20 - iCurZoomLevel)) * this.tileHeight;

        prj.getGeoCenter(geoCenter);
        int xIndex = (geoCenter.x >> (20 - iCurZoomLevel)) / this.tileWidth;
        int yIndex = (geoCenter.y >> (20 - iCurZoomLevel)) / this.tileHeight;
        int x2 = (xIndex << (20 - iCurZoomLevel)) * this.tileWidth;
        int y2 = (yIndex << (20 - iCurZoomLevel)) * this.tileHeight;
        TileCoordinate tileCenter = new TileCoordinate(xIndex, yIndex,
                iCurZoomLevel, this.iLayerIndex);
        tileCenter.pointGeo = new IPoint(x2, y2);
        calFPoint(tileCenter);

        ArrayList<TileCoordinate> arrListTileCoord = new ArrayList<TileCoordinate>();
        arrListTileCoord.add(tileCenter);

        // 已经计算出了中心图块，然后再循环添加
        iCenterTileX = xIndex;
        iCenterTileY = yIndex;
        boolean boInsideScreen = false;
        int iAroundTileX = 0;
        int iAroundTileY = 0;
        TileCoordinate tileCoordFind = null;
        for (int i = 1; ; i++) {
            boInsideScreen = false;
            for (int iX = iCenterTileX - i; iX <= iCenterTileX + i; iX++) {
                iAroundTileX = iX;
                iAroundTileY = iCenterTileY + i;

                pointGeo = new IPoint((iAroundTileX << (20 - iCurZoomLevel))
                        * this.tileWidth,
                        (iAroundTileY << (20 - iCurZoomLevel))
                                * this.tileHeight);
                if (pointGeo.x < maxX && pointGeo.x > minX && pointGeo.y < maxY
                        && pointGeo.y > minY) {
                    if (boInsideScreen == false) {
                        boInsideScreen = true;
                    }
                    tileCoordFind = new TileCoordinate(iAroundTileX,
                            iAroundTileY, iCurZoomLevel, this.iLayerIndex);
                    tileCoordFind.pointGeo = pointGeo;
                    calFPoint(tileCoordFind);
                    arrListTileCoord.add(tileCoordFind);
                }

                iAroundTileY = iCenterTileY - i;

                pointGeo = new IPoint((iAroundTileX << (20 - iCurZoomLevel))
                        * this.tileWidth,
                        (iAroundTileY << (20 - iCurZoomLevel))
                                * this.tileHeight);
                if (pointGeo.x < maxX && pointGeo.x > minX && pointGeo.y < maxY
                        && pointGeo.y > minY) {
                    if (boInsideScreen == false) {
                        boInsideScreen = true;
                    }
                    tileCoordFind = new TileCoordinate(iAroundTileX,
                            iAroundTileY, iCurZoomLevel, this.iLayerIndex);
                    tileCoordFind.pointGeo = pointGeo;
                    calFPoint(tileCoordFind);
                    arrListTileCoord.add(tileCoordFind);
                }
            }

            for (int iY = iCenterTileY + i - 1; iY > iCenterTileY - i; iY--) {
                iAroundTileX = iCenterTileX + i;
                iAroundTileY = iY;

                pointGeo = new IPoint((iAroundTileX << (20 - iCurZoomLevel))
                        * this.tileWidth,
                        (iAroundTileY << (20 - iCurZoomLevel))
                                * this.tileHeight);
                if (pointGeo.x < maxX && pointGeo.x > minX && pointGeo.y < maxY
                        && pointGeo.y > minY) {
                    if (boInsideScreen == false) {
                        boInsideScreen = true;
                    }
                    tileCoordFind = new TileCoordinate(iAroundTileX,
                            iAroundTileY, iCurZoomLevel, this.iLayerIndex);
                    tileCoordFind.pointGeo = pointGeo;
                    calFPoint(tileCoordFind);
                    arrListTileCoord.add(tileCoordFind);
                }

                iAroundTileX = iCenterTileX - i;

                pointGeo = new IPoint((iAroundTileX << (20 - iCurZoomLevel))
                        * this.tileWidth,
                        (iAroundTileY << (20 - iCurZoomLevel))
                                * this.tileHeight);
                if (pointGeo.x < maxX && pointGeo.x > minX && pointGeo.y < maxY
                        && pointGeo.y > minY) {
                    if (boInsideScreen == false) {
                        boInsideScreen = true;
                    }
                    tileCoordFind = new TileCoordinate(iAroundTileX,
                            iAroundTileY, iCurZoomLevel, this.iLayerIndex);
                    tileCoordFind.pointGeo = pointGeo;
                    calFPoint(tileCoordFind);
                    arrListTileCoord.add(tileCoordFind);
                }
            }

            if (boInsideScreen == false) {
                break;
            }
        }

        return arrListTileCoord;
    }

    private boolean calFPoint(TileCoordinate tile) {
        MapProjection localMapProjection = this.map.getMapProjection();
        float zoom = tile.Zoom;
        int width = this.tileWidth;
        int height = this.tileHeight;

        int geox = tile.pointGeo.x;
        int geoy = tile.pointGeo.y + (1 << 20 - (int) zoom) * height;

        float[] vertices = new float[12];

        FPoint localFPoint1 = new FPoint();
        localMapProjection.geo2Map(geox, geoy, localFPoint1);

        FPoint localFPoint2 = new FPoint();
        localMapProjection.geo2Map(geox + (1 << 20 - (int) zoom) * width, geoy, localFPoint2);

        FPoint localFPoint3 = new FPoint();
        localMapProjection.geo2Map(geox + (1 << 20 - (int) zoom) * width, geoy - (1 << 20 - (int) zoom) * height, localFPoint3);

        FPoint localFPoint4 = new FPoint();
        localMapProjection.geo2Map(geox, geoy - (1 << 20 - (int) zoom) * height, localFPoint4);

        vertices[0] = localFPoint1.x;
        vertices[1] = localFPoint1.y;
        vertices[2] = 0.0F;

        vertices[3] = localFPoint2.x;
        vertices[4] = localFPoint2.y;
        vertices[5] = 0.0F;

        vertices[6] = localFPoint3.x;
        vertices[7] = localFPoint3.y;
        vertices[8] = 0.0F;

        vertices[9] = localFPoint4.x;
        vertices[10] = localFPoint4.y;
        vertices[11] = 0.0F;
        if (tile.verticesBuffer == null) {
            tile.verticesBuffer = Util.makeFloatBuffer(vertices);
        } else {
            tile.verticesBuffer = Util.makeFloatBuffer(vertices, tile.verticesBuffer);
        }
        vertices = null;
        return true;
    }

    @Override
    public void onCallback(long tileId, List<GeoJsonItem> itemList) {
        // 将数据存储到对应的tileId
        if (tileId <= 0 || isListNull(itemList))return;
        for (TileCoordinate tile : screenTileList){
            if (tileId == tile.tileId){
                tile.itemList = new ArrayList<>();
                for (GeoJsonItem item : itemList){
                    tile.itemList.add(item.clone());
                }
                // draw
                drawTile(tile);
                break;
            }
        }
    }

    @Override
    public void onError(int errorCode, String message) {

    }

//    @Override
//    public String getGeoJsonSerUrl() {
//        return getOptions().getUrl();
//    }
//
//    @Override
//    public String getGeoJsonSerPath() {
//        return getOptions().getPath();
//    }
//
//    @Override
//    public String getRequestParmas() {
//        return null;
//    }

    private class TileServer
            extends AsyncTaskDecode<IMapDelegate, Void, List<TileCoordinate>> {
        private int zoom; //e
        private boolean needDownload; //f

        public TileServer(boolean needDownload) {
            this.needDownload = needDownload;
        }

        //protected List<bm.a> a(aa... paramVarArgs)
        protected List<GeojsonOverlayManager.TileCoordinate> doInBackground(IMapDelegate... map) {
            int width;
            int height;
            try {
                width = map[0].getMapWidth();
                height = map[0].getMapHeight();
                this.zoom = ((int) map[0].getZoomLevel());
            } catch (Throwable localThrowable) {
                width = 0;
                height = 0;
            }
            if ((width <= 0) || (height <= 0)) {
                return null;
            }
            //return bm.a(bm.this, this.e, i, j);
            List<GeojsonOverlayManager.TileCoordinate> calTileList =
                    GeojsonOverlayManager.this.getTilesInDomain(zoom, width, height);
            if (calTileList == null || calTileList.isEmpty()){
                return null;
            }

//            long start = System.currentTimeMillis();
            // 用map找出俩list的不同元素--------------------------------------------------------
            int maxSize = Math.max(screenTileList.size(), calTileList.size());
            Map<TileCoordinate,Integer> tileMap = new HashMap<>(maxSize);
            // 新增的tile
//            CopyOnWriteArrayList<TileCoordinate> drawTileList = new CopyOnWriteArrayList<>();
            List<GeojsonOverlayManager.TileCoordinate> cacheExistList = new ArrayList<>();
            List<GeojsonOverlayManager.TileCoordinate> needDownloadList = new ArrayList<>();
            // 需要删除的tile list
            CopyOnWriteArrayList<TileCoordinate> delTileList = new CopyOnWriteArrayList<>();

            for (TileCoordinate tile : screenTileList){
                tileMap.put(tile, 1);
            }
            // 给屏幕中原有的tile集合赋值数据
            for (TileCoordinate tile : calTileList) {
                long tileId = tile.tileId;
                List<GeoJsonItem> geoJsonItemList =
                        cacheManager.getGeojsonDataForTile(tileId);
                if (!isListNull(geoJsonItemList)){ // cache中存在
                    tile.itemList = new ArrayList<>();
                    for (GeoJsonItem item : geoJsonItemList){
                        tile.itemList.add(item.clone());
                    }
                }
                if (tileMap.get(tile) == null){ // 新增的tile
//                    drawTileList.add(tile);
                    cacheExistList.add(tile);
                    needDownloadList.add(tile);
                } else { // 重复元素
                    tileMap.put(tile, 2);
                }
            }
            for (Map.Entry<TileCoordinate,Integer> entry : tileMap.entrySet()){
                if (entry.getValue() == 1){ // 需要删除的tile
                    delTileList.add(entry.getKey());
                }
            }

            // 用list的removeAll()来求差集------------------------------------------------------------------------------------

//            CopyOnWriteArrayList<TileCoordinate> drawTileList = new CopyOnWriteArrayList<>();
////            if (drawTileList != null && !drawTileList.isEmpty()){
////                drawTileList.clear();
////            }
//
//            // 求新增:drawTileList
//            copyList(drawTileList, calTileList);
//            drawTileList.removeAll(screenTileList);
//            // 给屏幕中原有的tile集合赋值数据
//            for (TileCoordinate tile : calTileList) {
//                long tileId = tile.tileId;
//                List<GeoJsonItem> geoJsonItemList =
//                        cacheManager.getGeojsonDataForTile(tileId);
//                if (!isListNull(geoJsonItemList)){ // cache中存在
//                    tile.itemList = new ArrayList<>();
//                    for (GeoJsonItem item : geoJsonItemList){
//                        tile.itemList.add(item.clone());
//                    }
//                }
//            }

//            // 缓存中是否存在新增的tile信息
//            List<GeojsonOverlayManager.TileCoordinate> cacheExistList = new ArrayList<>();
//            List<GeojsonOverlayManager.TileCoordinate> needDownloadList = new ArrayList<>();
//            if (!drawTileList.isEmpty()){
//                for (TileCoordinate tile : drawTileList) {
//                    long tileId = tile.tileId;
//                    List<GeoJsonItem> geoJsonItemList =
//                            cacheManager.getGeojsonDataForTile(tileId);
//                    if (!isListNull(geoJsonItemList)){ // cache中存在
//                        tile.itemList = new ArrayList<>();
//                        for (GeoJsonItem item : geoJsonItemList){
//                            tile.itemList.add(item.clone());
//                        }
//                        cacheExistList.add(tile);
//                    } else { // needDownload
//                        needDownloadList.add(tile);
//                    }
//                }
//            }
//            // 需要删除的tile list
//            CopyOnWriteArrayList<TileCoordinate> delTileList = new CopyOnWriteArrayList<>();
//            copyList(delTileList, screenTileList);
//            delTileList.removeAll(calTileList);
            // 计算完毕--------------------------------------------------------------------------

//            long calEnd = System.currentTimeMillis();
//            Log.e("@@@", "geojson calculate time is "+(calEnd-start)+"ms");
            // 更新screenTileList
            if (screenTileList != null && !screenTileList.isEmpty()){
                screenTileList.clear();
            }
            copyList(screenTileList, calTileList);

            // 清理要删除tile的点线面
            if (!isListNull(delTileList)){
                delTileList(delTileList);
            }
            // draw cache exist
            if (!isListNull(cacheExistList)){
                for (TileCoordinate tile : cacheExistList) {
                    drawTile(tile);
                }
            }

            // download
            if (!isListNull(needDownloadList)){
                taskManager.updataConntionList(geoJsonDownTiles(needDownloadList));
            }
            return null;
//            return GeojsonOverlayManager.this.getTilesInDomain(zoom, width, height);
        }

        protected void onPostExecute(List<TileCoordinate> result) {
            if (result == null) {
                return;
            }
            int i = result.size();
            if (i <= 0) {
                return;
            }
            result.clear();
            result = null;
        }
    }

    private void copyList(CopyOnWriteArrayList<TileCoordinate> des, List<TileCoordinate> src){
//        des = new CopyOnWriteArrayList<>();
        Iterator iterator = src.iterator();
        while (iterator.hasNext()){
            TileCoordinate tile = (TileCoordinate) iterator.next();
            des.add(tile.copy());
        }
    }

    // 移除tile绘制的点线面
    private void delTileList(List<GeojsonOverlayManager.TileCoordinate> delTileList){
        try {
            for (TileCoordinate tile : delTileList){
                // del markers
                for (IMarkerDelegate marker : markers){
                    if (tile.tileId == marker.getTileId()){
//                        marker.remove();
                        GeojsonOverlayManager.this.removeMarker(marker);
                    }
                }

                // del overlay
                // 排重:如果当前屏幕中其他tile已经绘制过这条线或者面,则不需要删除
                List<GeoJsonItem> geoJsonItemList = tile.itemList;
                if (isListNull(geoJsonItemList)){
                    geoJsonItemList =
                            cacheManager.getGeojsonDataForTile(tile.tileId);
                }
                if (!isListNull(geoJsonItemList)){
                    for (GeoJsonItem item : geoJsonItemList){
                        int itemId = item.getId(); // id是唯一的
                        if (item.getType() == 1){ // 点

                        } else if (item.getType() == 2){ // 线
                            delOverlay(itemId, tile.tileId);
                        } else if (item.getType() == 3){ // 面
                            delOverlay(itemId, tile.tileId);
                        }
                    }
                }
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
        }
    }

    // remove overlay
    private void delOverlay(int itemId, long tileId) throws RemoteException {
        for (IOverlayDelegateDecode overlay : overlayList){
            // 数据id和tileId都能对上
            List tileIds = overlay.getTileIds();
            if (itemId == overlay.getDataId() && tileIds.contains(tileId)){
                boolean needDel = true;
                for (TileCoordinate tileS : screenTileList){
                    if (tileIds.contains(tileS.tileId)){
                        needDel = false;
                        break;
                    }
                }
                Iterator iterator = tileIds.iterator();
                while (iterator.hasNext()){
                    long tileId_ = (long) iterator.next();
                    if (tileId_ == tileId){
                        iterator.remove();
                        break;
                    }
                }
                if (needDel){
//                    overlay.remove();
                    GeojsonOverlayManager.this.removeOverlay(overlay.getId());
                }
                break;
            }
        }
    }

    // 根据瓦片类型绘制瓦片数据
    private void drawTile(TileCoordinate tile){
        try {
            List<GeoJsonItem> geoJsonItemList = tile.itemList;
            if (isListNull(geoJsonItemList)){
                geoJsonItemList =
                        cacheManager.getGeojsonDataForTile(tile.tileId);
            }
            if (!isListNull(geoJsonItemList)){
                for (GeoJsonItem item : geoJsonItemList){
                    LatLng[] pois = item.getCoords();
                    if (pois == null || pois.length ==0)continue;
                    List<LatLng> latLngs = new ArrayList<>();
                    Collections.addAll(latLngs, pois);
                    // id是唯一的
                    int itemId = item.getId();
                    if (item.getType() == 1){ // 点
                        GeojsonMarkerOptions gMoptions = GeojsonOverlayManager.this.getOptions()
                                .getMarkerOptions();

                        MarkerOptions options = new MarkerOptions();
                        options.position(latLngs.get(0));
                        if (gMoptions != null){
                            options.anchor(gMoptions.getAnchorU(), gMoptions.getAnchorV());
//                            options.draggable(gMoptions.isDraggable());
                            options.icon(gMoptions.getIcon());
//                            options.icons(gMoptions.getIcons());
//                            options.period(gMoptions.getPeriod());
//                            options.perspective(gMoptions.isPerspective());
//                            options.position(gMoptions.getPosition());
//                            options.setAddByAnimation(gMoptions.isAddByAnimation());
                            options.setFlat(gMoptions.isFlat());
//                            options.setGps(gMoptions.isGps());
//                            options.setInfoWindowOffset(gMoptions.getInfoWindowOffsetX(),
//                                    gMoptions.getInfoWindowOffsetY());
//                            options.snippet(gMoptions.getSnippet());
//                            options.title(gMoptions.getTitle());
//                            options.visible(gMoptions.isVisible());
                            options.zIndex(gMoptions.getZIndex());
                        }


                        IMarkerDelegate marker = ((MapDelegateImp)GeojsonOverlayManager.this.map)
                                .addGeojsonMarker(options);
                        marker.setTileId(tile.tileId);
                    } else if (item.getType() == 2){ // 线
                        // 排重:防止其他tile已经绘制过这条线了
                        // 先通过lineId找到其所在的tile集合,然后再去overlayList
                        boolean isExist = false;
                        for (IOverlayDelegateDecode overlay : overlayList){
                            if (itemId == overlay.getDataId()){
                                isExist = true;
                                List<Long> tileIds = overlay.getTileIds();
                                if (!tileIds.contains(tile.tileId)){
                                    tileIds.add(tile.tileId);
                                }
                                break;
                            }
                        }
                        if (isExist)continue;

                        GeojsonPolylineOptions gPolylineOptions = GeojsonOverlayManager.this.getOptions()
                                .getPolylineOptions();

                        PolylineOptions paramPolylineOptions = new PolylineOptions();
                        paramPolylineOptions.addAll(latLngs);
                        if (gPolylineOptions != null){
//                            paramPolylineOptions.addAll(gPolylineOptions.getPoints());
                            paramPolylineOptions.color(gPolylineOptions.getColor());
//                            paramPolylineOptions.colorValues(gPolylineOptions.getColorValues());
                            paramPolylineOptions.geodesic(gPolylineOptions.isGeodesic());
                            paramPolylineOptions.setCustomTexture(gPolylineOptions.getCustomTexture());
//                            paramPolylineOptions.setCustomTextureIndex(gPolylineOptions.getCustomTextureIndex());
//                            paramPolylineOptions.setCustomTextureList(gPolylineOptions.getCustomTextureList());
//                            paramPolylineOptions.setDottedLine(gPolylineOptions.isDottedLine());
                            paramPolylineOptions.setUseTexture(gPolylineOptions.isUseTexture());
//                            paramPolylineOptions.useGradient(gPolylineOptions.isUseGradient());
//                            paramPolylineOptions.visible(gPolylineOptions.isVisible());
                            paramPolylineOptions.width(gPolylineOptions.getWidth());
                            paramPolylineOptions.zIndex(gPolylineOptions.getZIndex());
                        }

                        IPolylineDelegateDecode polyline = ((MapDelegateImp)GeojsonOverlayManager.this.map)
                                .addGeojsonPolyline(paramPolylineOptions);
                        polyline.setDataId(itemId);
                        List<Long> tileIds = polyline.getTileIds();
                        tileIds.add(tile.tileId);
                        Set set = new HashSet(tileIds);
                        tileIds.clear();
                        tileIds.addAll(set);
                        polyline.setTileIds(tileIds);
                    } else if (item.getType() == 3){ // 面
                        // 排重:防止其他tile已经绘制过这个面了
                        boolean isExist = false;
                        for (IOverlayDelegateDecode overlay : overlayList){
                            if (itemId == overlay.getDataId()){
                                isExist = true;
                                List<Long> tileIds = overlay.getTileIds();
                                if (!tileIds.contains(tile.tileId)){
                                    tileIds.add(tile.tileId);
                                }
                                break;
                            }
                        }
                        if (isExist)continue;

                        GeojsonPolygonOptions gPolygonOptions = GeojsonOverlayManager.this.getOptions()
                                .getPolygonOptions();

                        PolygonOptions paramPolygonOptions = new PolygonOptions();
                        paramPolygonOptions.addAll(latLngs);
                        if (gPolygonOptions != null){
                            paramPolygonOptions.fillColor(gPolygonOptions.getFillColor());
                            paramPolygonOptions.strokeColor(gPolygonOptions.getStrokeColor());
                            paramPolygonOptions.strokeWidth(gPolygonOptions.getStrokeWidth());
//                            paramPolygonOptions.visible(gPolygonOptions.isVisible());
                            paramPolygonOptions.zIndex(gPolygonOptions.getZIndex());
                        }

                        IPolygonDelegate polygon = ((MapDelegateImp)GeojsonOverlayManager.this.map)
                                .addGeojsonPolygon(paramPolygonOptions);
                        polygon.setDataId(itemId);
                        List<Long> tileIds = polygon.getTileIds();
                        tileIds.add(tile.tileId);
                        Set set = new HashSet(tileIds);
                        tileIds.clear();
                        tileIds.addAll(set);
                        polygon.setTileIds(tileIds);
                    }
                }
            }
        } catch (RemoteException e1) {
            e1.printStackTrace();
        } finally {
        }
    }

    // TileCoordinate转成GeoJsonDownTile去请求下载
    private List<GeoJsonDownTile> geoJsonDownTiles(List<GeojsonOverlayManager.TileCoordinate> needDownloadList){
        List<GeoJsonDownTile> geoJsonDownTiles = new ArrayList<>();
        for (GeojsonOverlayManager.TileCoordinate tile : screenTileList){
//            String id = tile.X+","+tile.Y+","+tile.Zoom;
            long tileId = tile.tileId;
            int minX = tile.pointGeo.x;
            int minY = tile.pointGeo.y;
            int maxX = tile.pointGeo.x + (1 << (20 - (int) tile.Zoom)) * this.tileWidth;
            int maxY = tile.pointGeo.y + (1 << (20 - (int) tile.Zoom)) * this.tileHeight;
            IPoint southWestP = new IPoint(minX, maxY);
            IPoint northEastP = new IPoint(maxX, minY);
            DPoint southWestD = new DPoint();
            DPoint northEastD = new DPoint();
            MapProjection.geo2LonLat(southWestP.x, southWestP.y, southWestD);
            MapProjection.geo2LonLat(northEastP.x, northEastP.y, northEastD);
            LatLng[] latLngs = new LatLng[2];
            latLngs[0] = new LatLng(southWestD.y, southWestD.x);
            latLngs[1] = new LatLng(northEastD.y, northEastD.x);
            long datasetId = 0;
            if (GeojsonOverlayManager.this.getOptions() != null){
                datasetId = GeojsonOverlayManager.this.getOptions().getDataSetId();
            }
            GeoJsonDownTile geoJsonDownTile = new GeoJsonDownTile(tileId, latLngs, datasetId);
            if (needDownloadList.contains(tile)){
                geoJsonDownTile.setNeedDown(true);
            } else {
                geoJsonDownTile.setNeedDown(false);
            }
            geoJsonDownTiles.add(geoJsonDownTile);
        }
        return geoJsonDownTiles;
    }

    private long getTileId(GeojsonOverlayManager.TileCoordinate tile){
        long tileId = 0;
        try {
            if (tile != null){
                Tile tile_ = new Tile();
                tile_.x = tile.X;
                tile_.y = tile.Y;
                tile_.level = tile.Zoom;
                String tileKey = ((MapDelegateImp)GeojsonOverlayManager.this.getMapDelegate())
                        .getMapCore().tile2QuadKey(tile_);
                tileId = Long.valueOf(tileKey);
            }
        } catch (NumberFormatException e1) {
            e1.printStackTrace();
        } finally {
        }
        return tileId;
    }

    private boolean isListNull(List list){
        boolean isNull = false;
        if (list == null || list.isEmpty()){
            isNull = true;
        }
        return isNull;
    }

    public class TileCoordinate implements Cloneable {
        public int X; // a
        public int Y; //b
        public int Zoom; //c
        public int d; // d
        public IPoint pointGeo; //e
        public int textureId = 0; //f
        public boolean isLoadTexture = false; //g
        public FloatBuffer verticesBuffer = null; //h
        public Bitmap bitmap = null; //i
        public ImageWorkerDecode.BitmapWorkerTask task = null; //j
        public int k = 0; //k
        // storge data
        public List<GeoJsonItem> itemList;
        public long tileId = 0;

        public TileCoordinate(int tile_x, int tile_y, int tile_z, int paramInt4) {
            this.X = tile_x;
            this.Y = tile_y;
            this.Zoom = tile_z;
            this.d = paramInt4;
            this.tileId = GeojsonOverlayManager.this.getTileId(this);
        }

        public TileCoordinate(TileCoordinate parama) {
            this.X = parama.X;
            this.Y = parama.Y;
            this.Zoom = parama.Zoom;
            this.d = parama.d;
            this.pointGeo = parama.pointGeo;
            this.verticesBuffer = parama.verticesBuffer;
        }

        public TileCoordinate copy() // a
        {
            TileCoordinate locala = null;
            try {
                locala = (TileCoordinate) super.clone();
                locala.X = this.X;
                locala.Y = this.Y;
                locala.Zoom = this.Zoom;
                locala.d = this.d;
                locala.tileId = this.tileId;
                locala.itemList = new ArrayList<>();
                if (this.itemList != null){
                    for (GeoJsonItem item : this.itemList){
                        locala.itemList.add(item.clone());
                    }
                }

                locala.pointGeo = (this.pointGeo == null) ? null : ((IPoint) this.pointGeo.clone());
                locala.verticesBuffer = (this.verticesBuffer == null) ? null :
                        this.verticesBuffer.asReadOnlyBuffer();
            } catch (CloneNotSupportedException localCloneNotSupportedException) {
                localCloneNotSupportedException.printStackTrace();
            }
            //return new a(bm.this, this); //??????????
            return locala;
        }

        public boolean equals(Object paramObject) {
            if (this == paramObject) {
                return true;
            }
            if (!(paramObject instanceof TileCoordinate)) {
                return false;
            }
            TileCoordinate locala = (TileCoordinate) paramObject;
            return (this.X == locala.X) && (this.Y == locala.Y) && (this.Zoom == locala.Zoom)
                    && (this.d == locala.d);
        }

        public int hashCode() {
            return this.X * 7 + this.Y * 11 + this.Zoom * 13 + this.d;
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.X);
            stringBuilder.append("-");
            stringBuilder.append(this.Y);
            stringBuilder.append("-");
            stringBuilder.append(this.Zoom);
            stringBuilder.append("-");
            stringBuilder.append(this.d);
            return stringBuilder.toString();
        }

        public void getBitmapFromMemCache(Bitmap paramBitmap) {
//            if ((paramBitmap != null) && (!paramBitmap.isRecycled())) {
//                try {
//                    this.task = null;
//                    int m = paramBitmap.getWidth();
//                    int n = paramBitmap.getHeight();
//                    this.bitmap = Util.a(paramBitmap, Util.pow2(m),
//                            Util.pow2(n));
//                    mMap.setRunLowFrame(false);
//                } catch (Throwable localThrowable) {
//                    SDKLogHandler.exception(localThrowable, "TileOverlayDelegateImp", "setBitmap");
//
//                    localThrowable.printStackTrace();
//                    if (this.k < 3) {
//                        //bm.b(bm.this).a(true, this);
//                        TileOverlayDelegateImp.this.mImageFetcher.a(true, this);
//                        this.k += 1;
//                    }
//                }
//            } else if (this.k < 3) {
//                //bm.b(bm.this).a(true, this);
//                TileOverlayDelegateImp.this.mImageFetcher.a(true, this);
//                this.k += 1;
//            }
//            if ((paramBitmap != null) && (!paramBitmap.isRecycled())) {
//                paramBitmap.recycle();
//                paramBitmap = null;
//            }
        }

        public void destroy() {
//            ImageWorkerDecode.cancelWork(this);
//            if (this.isLoadTexture) {
//                //bm.c(bm.this).c.add(Integer.valueOf(this.f));
//                mTileOverlayView.recycleTextureIds.add(Integer.valueOf(this.textureId));
//            }
            this.isLoadTexture = false;
            this.textureId = 0;
            if ((this.bitmap != null) && (!this.bitmap.isRecycled())) {
                this.bitmap.recycle();
            }
            this.bitmap = null;
            if (this.verticesBuffer != null) {
                this.verticesBuffer.clear();
            }
            this.verticesBuffer = null;
            this.task = null;
            if (this.itemList != null){
                this.itemList.clear();
            }
            this.itemList = null;
        }


    }
}
