package com.unistrong.api.mapcore;

import android.content.Context;
import android.graphics.Point;
import android.os.Message;
import android.os.RemoteException;

import com.unistrong.api.mapcore.util.Util;
import com.unistrong.api.mapcore.util.LogManager;
import com.unistrong.api.mapcore.util.IndoorBuilding;
import com.unistrong.api.maps.CustomRenderer;
import com.unistrong.api.maps.MapController;
import com.unistrong.api.maps.model.CameraPosition;
import com.unistrong.api.maps.model.LatLng;
import com.unistrong.api.maps.model.LatLngBounds;
import com.leador.mapcore.BaseMapCallImplement;
import com.leador.mapcore.Convert;
import com.leador.mapcore.DPoint;
import com.leador.mapcore.FPoint;
import com.leador.mapcore.IPoint;
import com.leador.mapcore.MapCore;
import com.leador.mapcore.MapProjection;
import com.leador.mapcore.MapSourceGridData;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

class MapCallback//a
        extends BaseMapCallImplement {
    private MapDelegateImp mapDelegate;

    @Override
    public String getUserGridURL(String mesh) {
        if (mapDelegate != null) {
            String url = mapDelegate.getUserGridURL(mesh);
            return url;
        }
        return null;
    }

    @Override
    public String getMapSvrAddress() {
        return "";
    }

    public MapCallback(MapDelegateImp mapView) {
        this.mapDelegate = mapView;

    }

    @Override
    public void OnMapSurfaceCreate(GL10 gl10, MapCore paramMapCore) {
        super.OnMapSurfaceCreate(paramMapCore);
    }

    @Override
    public void OnMapSurfaceRenderer(GL10 gl10, MapCore mapCore, int paramInt) {
        super.OnMapSurfaceRenderer(gl10, mapCore, paramInt);
        if (paramInt == mapDelegate.drawCustomRenderTime) {
            try {
                this.mapDelegate.baseOverlayLayer.onDrawGL(gl10, true, this.mapDelegate
                        .getMapTextZIndex());
                if (this.mapDelegate.geojsonOverlayManager != null) {
                    this.mapDelegate.geojsonOverlayManager.onDrawGL(gl10, true, this.mapDelegate
                            .getMapTextZIndex());
                }
                if (this.mapDelegate.customRenderer != null) {
                    this.mapDelegate.customRenderer.onDrawFrame(gl10);
                }
                for (CustomRenderer reader : this.mapDelegate.rendererList) {
                    reader.onDrawFrame(gl10);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private float presslastZoom = -1.0F; //g

    @Override
    public void OnMapSufaceChanged(GL10 paramGL10, MapCore mapCore, int paramInt1, int paramInt2) {
    }

    @Override
    public void OnMapProcessEvent(MapCore paramMapCore) {
        float cz = 0.0F;
        if ((this.mapDelegate != null) && (this.mapDelegate.O())) {
            this.mapDelegate.P();
        }
        if (this.mapDelegate != null) {
            cz = this.mapDelegate.getZoomLevel();
            runMessage(paramMapCore);
            MapMessageDecode mapMessage;
            while ((mapMessage = this.mapDelegate.mMapMessges.getMapMessage()) != null) {
                if (mapMessage.id == MapMessageDecode.TRAFFICSET) { //?????
                    if (mapMessage.getEnabled()) {
                        paramMapCore.setParameter(2010, MapCore.ME_DATA_VEC_TMC, 0, 0, 0);
                    } else {
                        paramMapCore.setParameter(2010, 0, 0, 0, 0);
                    }
                }
            }
            /** 设置最终的mapstate */
            paramMapCore.setMapstate(this.mapDelegate.getMapProjection());
            if ((this.lastZoom >= this.mapDelegate.getMinZoomLevel()) && (this.presslastZoom != cz)) {
                if (this.mapDelegate.handler != null) {
                    this.mapDelegate.handler.obtainMessage(MapDelegateImp.UPDATE_ZOOM_BITMAP).sendToTarget();
                }
            }
        }
        this.presslastZoom = cz;
    }

    IPoint lastGeo = new IPoint(); // a
    float lastZoom; //b
    float lastTilt; //c
    float lastBearing; //
    IPoint currGeoPoint = new IPoint(); //e
    private int x; //h //猜测:目标x
    private int y; //i //猜测:目标y

    void runMessage(MapCore paramMapCore) {
        MapProjection prj = this.mapDelegate.getMapProjection();
        float zoom = prj.getMapZoomer();
        float tilt = prj.getCameraHeaderAngle();
        float bearing = prj.getMapAngle();
        prj.getGeoCenter(this.currGeoPoint);

        CameraUpdateFactoryDelegate mapMessage = null;
        boolean isCameraChange = false;
        boolean isCameraChangeFinish = false;
        /** 实现手势控制逻辑*/
        while ((mapMessage = this.mapDelegate.mMapMessges.getMessage()) != null) {
            try {
                runCameraUpdate(mapMessage);
                isCameraChangeFinish |= mapMessage.isChangeFinished;
            } catch (RemoteException localRemoteException1) {
                localRemoteException1.printStackTrace();
            }
        }
        /** 获取地图状态设置后的各值 zoom tilt angle center. */
        this.lastZoom = prj.getMapZoomer();
        this.lastTilt = prj.getCameraHeaderAngle();
        this.lastBearing = prj.getMapAngle();
        prj.getGeoCenter(this.lastGeo);
        if ((zoom != this.lastZoom) || (this.lastTilt != tilt) || (this.lastBearing != bearing) || (this.lastGeo.x != this.currGeoPoint.x) || (this.lastGeo.y != this.currGeoPoint.y)) {
            isCameraChange = true;
        }
        try {
            MapController.OnCameraChangeListener localObject;
            if (isCameraChange) {
                this.mapDelegate.setRunLowFrame(false);

                localObject = this.mapDelegate.getOnCameraChangeListener();
                if (localObject != null) {
                    DPoint localDPoint = new DPoint();
                    MapProjection.geo2LonLat(this.lastGeo.x, this.lastGeo.y, localDPoint);
                    CameraPosition localCameraPosition = new CameraPosition(new LatLng(localDPoint.y, localDPoint.x, false), this.lastZoom, this.lastTilt, this.lastBearing);

                    this.mapDelegate.runOnCameraChangeListener(localCameraPosition);
                }
                this.mapDelegate.setMapBounds();
            } else {
                this.mapDelegate.setRunLowFrame(true);
            }
            if (isCameraChangeFinish) {
                if (isCameraChangeFinish) {
                    this.mapDelegate.refreshTileOverlay(true);
                } else {
                    this.mapDelegate.refreshTileOverlay(false);
                }
                Message message = new Message();
                message.what = MapDelegateImp.CAMERA_UPDATE_FINISH;
                this.mapDelegate.handler.sendMessage(message);
            }
            if (((this.lastTilt != tilt) || (this.lastBearing != bearing)) &&
                    (this.mapDelegate.getUiSettings().isCompassEnabled())) {
                this.mapDelegate.invalidateCompassView();
            }
            if (zoom != this.lastZoom) {
                if (this.mapDelegate.getUiSettings().isScaleControlsEnabled()) {
                    this.mapDelegate.invalidateScaleState();
                }
                MapController.OnMapLevelChangeListener mapLevelChangeListener = this.mapDelegate.getOnMapLevelChangeListener();
                if (mapLevelChangeListener != null)
                    mapLevelChangeListener.onMapLevelChanged(this.lastZoom);
            }
        } catch (RemoteException localRemoteException2) {

            localRemoteException2.printStackTrace();
        }
    }

    private void newLatLngBoundsWithSize(CameraUpdateFactoryDelegate paramo) {
        MapCore mapCore = this.mapDelegate.getMapCore();
        MapProjection proj = this.mapDelegate.getMapProjection();
        LatLngBounds localLatLngBounds = paramo.bounds;
        int j = paramo.width;
        int k = paramo.height;
        int m = paramo.padding;

        IPoint localIPoint1 = new IPoint();
        IPoint localIPoint2 = new IPoint();
        MapProjection.lonlat2Geo(localLatLngBounds.northeast.longitude, localLatLngBounds.northeast.latitude, localIPoint1);

        MapProjection.lonlat2Geo(localLatLngBounds.southwest.longitude, localLatLngBounds.southwest.latitude, localIPoint2);

        int n = localIPoint1.x - localIPoint2.x;
        int i1 = localIPoint2.y - localIPoint1.y;
        int i2 = j - m * 2;
        int i3 = k - m * 2;
        if ((n < 0) && (i1 < 0)) {
            return;
        }
        i2 = i2 <= 0 ? 1 : i2;
        i3 = i3 <= 0 ? 1 : i3;
        float zoom = a(localLatLngBounds.northeast, localLatLngBounds.southwest, i2, i3);

        int centerx = (localIPoint1.x + localIPoint2.x) / 2;
        int centery = (localIPoint1.y + localIPoint2.y) / 2;
        proj.setMapZoomer(zoom);
        proj.setGeoCenter(centerx, centery);
        proj.setCameraHeaderAngle(0.0F);
        proj.setMapAngle(0.0F);
        mapCore.setMapstate(proj);
    }

    private float a(LatLng paramLatLng1, LatLng paramLatLng2, int paramInt1, int paramInt2) // a ???????
    {
        MapProjection localMapProjection = this.mapDelegate.getMapProjection();
        localMapProjection.setMapAngle(0.0F);
        localMapProjection.setCameraHeaderAngle(0.0F);
        localMapProjection.recalculate();
        IPoint localIPoint1 = new IPoint();
        IPoint localIPoint2 = new IPoint();
        this.mapDelegate.getLatLng2Pixel(paramLatLng1.latitude, paramLatLng1.longitude, localIPoint1);

        this.mapDelegate.getLatLng2Pixel(paramLatLng2.latitude, paramLatLng2.longitude, localIPoint2);

        double d1 = localIPoint1.x - localIPoint2.x;
        double d2 = localIPoint2.y - localIPoint1.y;
        d1 = d1 <= 0.0D ? 1.0D : d1;
        d2 = d2 <= 0.0D ? 1.0D : d2;
        double d3 = Math.log(paramInt1 / d1) / Math.log(2.0D);
        double d4 = Math.log(paramInt2 / d2) / Math.log(2.0D);
        double d5 = Math.min(d3, d4);
        int j = Math.abs(d5 - d3) < 1.0E-7D ? 1 : 0;
        float f1 = Util.checkZoomLevel(
                (float) (localMapProjection.getMapZoomer() + Math.floor(d5)));
        double d6 = 0.1D;
        do {
            f1 = (float) (f1 + d6);
            f1 = Util.checkZoomLevel(f1);
            localMapProjection.setMapZoomer(f1);
            localMapProjection.recalculate();
            this.mapDelegate.getLatLng2Pixel(paramLatLng1.latitude, paramLatLng1.longitude, localIPoint1);

            this.mapDelegate.getLatLng2Pixel(paramLatLng2.latitude, paramLatLng2.longitude, localIPoint2);

            d1 = localIPoint1.x - localIPoint2.x;
            d2 = localIPoint2.y - localIPoint1.y;
            if (j != 0 ? d1 >= paramInt1 : d2 >= paramInt2) {
                f1 = (float) (f1 - d6);
                break;
            }
        } while (f1 < 19.0F);
        return f1;

        //return f1;
    }

    void runCameraUpdate(CameraUpdateFactoryDelegate cameraUpdateFactoryDelegate)// a
            throws RemoteException {
        MapCore mapCore = this.mapDelegate.getMapCore();
        MapProjection proj = this.mapDelegate.getMapProjection();
        MapController.OnMapLevelChangeListener levelChangeListener = this.mapDelegate.getOnMapLevelChangeListener();
        cameraUpdateFactoryDelegate.zoom = this.mapDelegate.checkZoomLevel(cameraUpdateFactoryDelegate.zoom);
        Object localObject;
        float f1;
        switch (cameraUpdateFactoryDelegate.nowType) {
            case changeCenter:
                if (cameraUpdateFactoryDelegate.n) {
                    a(proj, cameraUpdateFactoryDelegate.geoPoint);
                } else {
                    proj.setGeoCenter(cameraUpdateFactoryDelegate.geoPoint.x, cameraUpdateFactoryDelegate.geoPoint.y);
                }
                mapCore.setMapstate(proj);
                break;
            case changeBearing:
                if (cameraUpdateFactoryDelegate.n) {
                    d(proj, cameraUpdateFactoryDelegate);
                } else {
                    proj.setMapAngle(cameraUpdateFactoryDelegate.bearing);
                }
                mapCore.setMapstate(proj);
                break;
            case changeBearingGeoCenter:
                if (cameraUpdateFactoryDelegate.n) {
                    a(proj, cameraUpdateFactoryDelegate);
                } else {
                    proj.setMapAngle(cameraUpdateFactoryDelegate.bearing);
                    proj.setGeoCenter(cameraUpdateFactoryDelegate.geoPoint.x, cameraUpdateFactoryDelegate.geoPoint.y);
                }
                mapCore.setMapstate(proj);
                break;
            case changeTilt:
                cameraUpdateFactoryDelegate.tilt = Util.checkTilt(cameraUpdateFactoryDelegate.tilt, proj
                        .getMapZoomer());
                if (cameraUpdateFactoryDelegate.n) {
                    c(proj, cameraUpdateFactoryDelegate);
                } else {
                    proj.setCameraHeaderAngle(cameraUpdateFactoryDelegate.tilt);
                }
                mapCore.setMapstate(proj);
                break;
            case changeGeoCenterZoom:
                if (cameraUpdateFactoryDelegate.n) {
                    b(proj, cameraUpdateFactoryDelegate);
                } else {
                    proj.setGeoCenter(cameraUpdateFactoryDelegate.geoPoint.x, cameraUpdateFactoryDelegate.geoPoint.y);

                    proj.setMapZoomer(cameraUpdateFactoryDelegate.zoom);
                }
                mapCore.setMapstate(proj);
                break;
            case newCameraPosition:
                LatLng xy = cameraUpdateFactoryDelegate.h.target;
                IPoint geoCenterPnt = new IPoint();
                MapProjection.lonlat2Geo(xy.longitude, xy.latitude, (IPoint) geoCenterPnt);

                float zoom = Util.checkZoomLevel(cameraUpdateFactoryDelegate.h.zoom);
                float bearing = cameraUpdateFactoryDelegate.h.bearing;
                float newtilt = Util.checkTilt(cameraUpdateFactoryDelegate.h.tilt, zoom);
                if (cameraUpdateFactoryDelegate.n) {
                    a(proj, geoCenterPnt, zoom, bearing, newtilt);
                } else {
                    proj.setGeoCenter(geoCenterPnt.x, geoCenterPnt.y);
                    proj.setMapZoomer(zoom);
                    proj.setMapAngle(bearing);
                    proj.setCameraHeaderAngle(newtilt);
                }
                mapCore.setMapstate(proj);
                break;
            case zoomIn:
                f1 = proj.getMapZoomer() + 1.0F;
                f1 = this.mapDelegate.checkZoomLevel(f1);
                if (cameraUpdateFactoryDelegate.n) {
                    a(proj, f1);
                } else {
                    proj.setMapZoomer(f1);
                }
                mapCore.setMapstate(proj);
                break;
            case zoomOut:
                f1 = proj.getMapZoomer() - 1.0F;
                f1 = this.mapDelegate.checkZoomLevel(f1);
                if (cameraUpdateFactoryDelegate.n) {
                    a(proj, f1);
                } else {
                    proj.setMapZoomer(f1);
                }
                proj.setMapZoomer(f1);
                mapCore.setMapstate(proj);
                break;
            case zoomTo:
                f1 = cameraUpdateFactoryDelegate.zoom;
                if (cameraUpdateFactoryDelegate.n) {
                    a(proj, f1);
                } else {
                    proj.setMapZoomer(f1);
                }
                mapCore.setMapstate(proj);
                break;
            case zoomBy:
                f1 = this.mapDelegate.checkZoomLevel(proj.getMapZoomer() + cameraUpdateFactoryDelegate.amount);

                localObject = cameraUpdateFactoryDelegate.focus;
                if (localObject != null) {
                    a(proj, f1, ((Point) localObject).x, ((Point) localObject).y);
                } else if (cameraUpdateFactoryDelegate.n) {
                    a(proj, f1);
                } else {
                    proj.setMapZoomer(f1);
                }
                mapCore.setMapstate(proj);
                break;
            case scrollBy:
                f1 = cameraUpdateFactoryDelegate.xPixel;
                float f2 = cameraUpdateFactoryDelegate.yPixel;
                f1 = this.mapDelegate.getWidth() / 2.0F + f1;
                f2 = this.mapDelegate.getHeight() / 2.0F + f2;
                IPoint localIPoint = new IPoint();
                this.mapDelegate.getPixel2Geo((int) f1, (int) f2, localIPoint);
                proj.setGeoCenter(localIPoint.x, localIPoint.y);
                mapCore.setMapstate(proj);
                break;
            case newLatLngBounds:
                cameraUpdateFactoryDelegate.nowType = CameraUpdateFactoryDelegate.Type.newLatLngBoundsWithSize; //?
                cameraUpdateFactoryDelegate.width = this.mapDelegate.getWidth();
                cameraUpdateFactoryDelegate.height = this.mapDelegate.getHeight();
                newLatLngBoundsWithSize(cameraUpdateFactoryDelegate);
                break;
            case newLatLngBoundsWithSize:
                newLatLngBoundsWithSize(cameraUpdateFactoryDelegate);
                break;
            case changeGeoCenterZoomTiltBearing:
                cameraUpdateFactoryDelegate.tilt = Util.checkTilt(cameraUpdateFactoryDelegate.tilt, cameraUpdateFactoryDelegate.zoom);
                if (cameraUpdateFactoryDelegate.n) {
                    a(proj, cameraUpdateFactoryDelegate.geoPoint, cameraUpdateFactoryDelegate.zoom, cameraUpdateFactoryDelegate.bearing, cameraUpdateFactoryDelegate.tilt);
                } else {
                    proj.setGeoCenter(cameraUpdateFactoryDelegate.geoPoint.x, cameraUpdateFactoryDelegate.geoPoint.y);

                    proj.setMapZoomer(cameraUpdateFactoryDelegate.zoom);
                    proj.setMapAngle(cameraUpdateFactoryDelegate.bearing);
                    proj.setCameraHeaderAngle(cameraUpdateFactoryDelegate.tilt);
                }
                mapCore.setMapstate(proj);
                break;
            default:
                mapCore.setMapstate(proj);
        }
    }

    private void a(MapProjection projection, CameraUpdateFactoryDelegate cameraUpdateFactoryDelegate) {
        projection.setMapAngle(cameraUpdateFactoryDelegate.bearing);
        a(projection, cameraUpdateFactoryDelegate.geoPoint);
    }

    private void a(MapProjection paramMapProjection, float paramFloat) {
        a(paramMapProjection, paramFloat, this.x, this.y);
    }

    private void a(MapProjection paramMapProjection, float paramFloat, int paramInt1, int paramInt2) {
        IPoint localIPoint = a(paramMapProjection, paramInt1, paramInt2);
        paramMapProjection.setMapZoomer(paramFloat);
        a(paramMapProjection, localIPoint, paramInt1, paramInt2);
    }

    private void a(MapProjection paramMapProjection, IPoint paramIPoint, float paramFloat1, float paramFloat2, float paramFloat3) {
        paramMapProjection.setMapZoomer(paramFloat1);
        paramMapProjection.setMapAngle(paramFloat2);
        paramMapProjection.setCameraHeaderAngle(paramFloat3);
        a(paramMapProjection, paramIPoint);
    }

    private void b(MapProjection paramMapProjection, CameraUpdateFactoryDelegate paramo) {
        paramMapProjection.setMapZoomer(paramo.zoom);
        a(paramMapProjection, paramo.geoPoint);
    }

    private void c(MapProjection paramMapProjection, CameraUpdateFactoryDelegate paramo) {
        IPoint localIPoint = a(paramMapProjection);
        paramMapProjection.setCameraHeaderAngle(paramo.tilt);

        a(paramMapProjection, localIPoint);
    }

    private void d(MapProjection proj
            , CameraUpdateFactoryDelegate paramo) {
        IPoint localIPoint = a(proj);
        proj.setMapAngle(paramo.bearing);
        a(proj, localIPoint);
    }

    private void a(MapProjection paramMapProjection, IPoint paramIPoint) {
        a(paramMapProjection, paramIPoint, this.x, this.y);
    }

    private void a(MapProjection paramMapProjection, IPoint paramIPoint, int paramInt1, int paramInt2) {
        paramMapProjection.recalculate();
        IPoint localIPoint1 = a(paramMapProjection, paramInt1, paramInt2);
        IPoint localIPoint2 = new IPoint();
        paramMapProjection.getGeoCenter(localIPoint2);
        paramMapProjection.setGeoCenter(localIPoint2.x + paramIPoint.x - localIPoint1.x, localIPoint2.y + paramIPoint.y - localIPoint1.y);
    }

    private IPoint a(MapProjection paramMapProjection) {
        return a(paramMapProjection, this.x, this.y);
    }

    private IPoint a(MapProjection paramMapProjection, int paramInt1, int paramInt2) {
        FPoint localFPoint = new FPoint();
        paramMapProjection.win2Map(paramInt1, paramInt2, localFPoint);
        IPoint localIPoint = new IPoint();
        paramMapProjection.map2Geo(localFPoint.x, localFPoint.y, localIPoint);
        return localIPoint;
    }

    public void OnMapDestory(GL10 gl10, MapCore mapCore) {
        super.OnMapDestory(mapCore);
    }

    public void OnMapReferencechanged(MapCore mapCore, String paramString1, String paramString2) {
        try {
            if (this.mapDelegate.getUiSettings().isCompassEnabled()) {
                this.mapDelegate.invalidateCompassView();
            }
            if (this.mapDelegate.getUiSettings().isScaleControlsEnabled()) {
                this.mapDelegate.invalidateScaleState();
            }
            this.mapDelegate.refreshTileOverlay(true);
        } catch (RemoteException localRemoteException) {

            localRemoteException.printStackTrace();
        }
        this.mapDelegate.U();
    }

    public Context getContext() {
        return this.mapDelegate.getContext();
    }

    public boolean isMapEngineValid() {
        if (this.mapDelegate.getMapCore() != null) {
            return this.mapDelegate.getMapCore().isMapEngineValid();
        }
        return false;
    }

    public void OnMapLoaderError(int paramInt) {
        LogManager.writeLog("MapCore", "OnMapLoaderError=" + paramInt, 112);
    }

    public void a(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void requestRender() {
        this.mapDelegate.setRunLowFrame(false);
    }

    public void onIndoorBuildingActivity(MapCore mapCore, byte[] paramArrayOfByte) {
        try {


            IndoorBuilding localf = null;
            if (null != paramArrayOfByte) {
                localf = new IndoorBuilding();
                int j = 0;
                int k = 0;
                k = paramArrayOfByte[(j++)];
                localf.a = new String(paramArrayOfByte, j, k);
                j += k;
                k = paramArrayOfByte[(j++)];
                localf.b = new String(paramArrayOfByte, j, k);
                j += k;
                k = paramArrayOfByte[(j++)];
                localf.c = new String(paramArrayOfByte, j, k);

                j += k;
                localf.d = Convert.getInt(paramArrayOfByte, j);
                j += 4;
                k = paramArrayOfByte[(j++)];
                localf.e = new String(paramArrayOfByte, j, k);
                j += k;
                localf.f = Convert.getInt(paramArrayOfByte, j);
                j += 4;
                localf.g = new int[localf.f];
                localf.h = new String[localf.f];
                localf.i = new String[localf.f];
                for (int m = 0; m < localf.f; m++) {
                    localf.g[m] = Convert.getInt(paramArrayOfByte, j);

                    j += 4;
                    k = paramArrayOfByte[(j++)];
                    if (k > 0) {
                        localf.h[m] = new String(paramArrayOfByte, j, k);

                        j += k;
                    }
                    k = paramArrayOfByte[(j++)];
                    if (k > 0) {
                        localf.i[m] = new String(paramArrayOfByte, j, k);

                        j += k;
                    }
                }
                localf.j = Convert.getInt(paramArrayOfByte, j);
                j += 4;
                if (localf.j > 0) {
                    localf.k = new int[localf.j];
                    for (int m = 0; m < localf.j; m++) {
                        localf.k[m] = Convert.getInt(paramArrayOfByte, j);

                        j += 4;
                    }
                }
            }
//            this.mapView.a(localf);
        } catch (Throwable localThrowable) {
            localThrowable.printStackTrace();
        }
    }

    public void onIndoorDataRequired(MapCore mapCore, int paramInt, String[] paramArrayOfString, int[] paramArrayOfInt1, int[] paramArrayOfInt2) {
        if ((paramArrayOfString == null) || (paramArrayOfString.length == 0)) {
            return;
        }
        ArrayList<MapSourceGridData> localArrayList = null;
        localArrayList = getReqGridList(paramInt);
        if (null != localArrayList) {
            localArrayList.clear();
            for (int j = 0; j < paramArrayOfString.length; j++) {
                localArrayList.add(new MapSourceGridData(paramArrayOfString[j], paramInt, paramArrayOfInt1[j], paramArrayOfInt2[j]));
            }
            if (paramInt != 5) {
                proccessRequiredData(mapCore, localArrayList, paramInt);
            }
        }
    }
}
