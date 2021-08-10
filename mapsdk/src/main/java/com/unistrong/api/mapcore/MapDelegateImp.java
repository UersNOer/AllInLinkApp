package com.unistrong.api.mapcore;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.unistrong.api.mapcore.util.AppInfo;
import com.unistrong.api.mapcore.util.AuthManager;
import com.unistrong.api.mapcore.util.IndoorBuilding;
import com.unistrong.api.mapcore.util.LMapThrowException;
import com.unistrong.api.mapcore.util.LogManager;
import com.unistrong.api.mapcore.util.MultiTouchSupportDecode;
import com.unistrong.api.mapcore.util.RegionUtil;
import com.unistrong.api.mapcore.util.RotateGestureDetectorDecode;
import com.unistrong.api.mapcore.util.SDKInfo;
import com.unistrong.api.mapcore.util.SDKLogHandler;
import com.unistrong.api.mapcore.util.Util;
import com.unistrong.api.maps.CustomRenderer;
import com.unistrong.api.maps.UnistrongException;
import com.unistrong.api.maps.LocationSource;
import com.unistrong.api.maps.MapController;
import com.unistrong.api.maps.MapOptions;
import com.unistrong.api.maps.MapsInitializer;
import com.unistrong.api.maps.model.ArcOptions;
import com.unistrong.api.maps.model.BitmapDescriptor;
import com.unistrong.api.maps.model.BitmapDescriptorFactory;
import com.unistrong.api.maps.model.CameraPosition;
import com.unistrong.api.maps.model.CircleOptions;
import com.unistrong.api.maps.model.ClusterOptions;
import com.unistrong.api.maps.model.GeoJsonLayerOptions;
import com.unistrong.api.maps.model.GroundOverlayOptions;
import com.unistrong.api.maps.model.ClusterBuild;
import com.unistrong.api.maps.model.QuadTreeNodeData;
import com.unistrong.api.maps.model.LatLng;
import com.unistrong.api.maps.model.LatLngBounds;
import com.unistrong.api.maps.model.Marker;
import com.unistrong.api.maps.model.MarkerOptions;
import com.unistrong.api.maps.model.MyLocationStyle;
import com.unistrong.api.maps.model.NavigateArrowOptions;
import com.unistrong.api.maps.model.Poi;
import com.unistrong.api.maps.model.PolygonOptions;
import com.unistrong.api.maps.model.Polyline;
import com.unistrong.api.maps.model.PolylineOptions;
import com.unistrong.api.maps.model.Text;
import com.unistrong.api.maps.model.TextOptions;
import com.unistrong.api.maps.model.TileOverlay;
import com.unistrong.api.maps.model.TileOverlayOptions;
import com.leador.mapcore.DPoint;
import com.leador.mapcore.FPoint;
import com.leador.mapcore.GLMapResManager;
import com.leador.mapcore.IPoint;
import com.leador.mapcore.MapCore;
import com.leador.mapcore.MapPoi;
import com.leador.mapcore.MapProjection;
import com.leador.mapcore.Tile;
import com.leador.mapcore.VMapDataCache;

import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * 地图显示View 及 内部可以操作的Map
 */
public class MapDelegateImp implements GLSurfaceView.Renderer,
        IMapDelegate {
    private static final int RERCOUNT = 40; // 每秒刷新率
    private static final int AUTH_KEY_SUCCESS = 1;
    private static final int AUTH_KEY_FAILURE = 2;
    private static final int ON_CAMERA_CHANGE = 10;
    private static final int ON_MAP_LOADED = 11;
    static final int NEW_LATLNG_BOUNDS_WITH_SIZE = 12;
    private static final int CAMERA_ANIMATION = 13;
    private static final int COMPASS_UPDATE = 14;
    private static final int SCALE_UPDATE = 15;
    private static final int SCREEN_SHOT = 16;
    static final int CAMERA_UPDATE_FINISH = 17;
    private static final int UPDATE_MAP_BOUNDS = 18;
    private static final int CREATEMAP = 19;
    private static final int REFRESH_TILE_OVERLAY = 20;
    static final int UPDATE_ZOOM_BITMAP = 21;//新增
    private static final int REFRASH_MAP = 22;//新增
    private static final int TRANSPARENTIME = 300;// 保持多少毫秒为白色

    private static final int WXM_PARAMETERNAME_TRAFFIC = 2010;
    private static final int WXM_PARAMETERNAME_SATELLITE = 2011;  //卫星图控制参数
    private static final int WXM_PARAMETERNAME_PANORAMA = 2013;  //实景图层控制参数
    private static final int WXM_PARAMETERNAME_TILE_MAP = 2015;  //栅格地图（目前支持：900913 4326）
    private static final int WXM_PARAMETERNAME_HIDE_VECTOR_MAP = 2030;  //隐藏自有矢量图层（只展示栅格）


    /**
     * 默认线的纹理ID
     */
    private int LINE_TEXTURE_ID = -1;
    private int o = -1; // o
    private Bitmap lineTexture = null; // p
    private Bitmap lineDashTexture = null; // q
    private double mapcenterx = 116.346508; // r
    private double mapcentery = 39.969876; // s
    float mapzoomer = 10.0F; // a
    float cameraheaderangle = 0.0F; // b
    float mapangle = 0.0F; // c
    private final int[] scaleArray = {10000000, 5000000, 2000000, 1000000,
            500000, 200000, 100000, 50000, 30000, 20000, 10000, 5000, 2000,
            1000, 500, 200, 100, 50, 25, 10, 5}; // t
    private CopyOnWriteArrayList<Integer> freeTextureList = new CopyOnWriteArrayList<Integer>(); // u
    private CopyOnWriteArrayList<Integer> textureList = new CopyOnWriteArrayList<Integer>(); // v
    public IMapOverlayImageView mOverlaysImageView; // d
    MapMessageQueueDecode mMapMessges = new MapMessageQueueDecode(this); // e
    ZoomControllerViewDecode mZoomView; // f
    TileOverlayViewDecode mTileOverlayView; // g
    GeojsonOverlayManager geojsonOverlayManager;
    private GLMapResManager.MapViewTime mapViewTime = GLMapResManager.MapViewTime.DAY; //w
    private GLMapResManager.MapViewMode mapViewMode = GLMapResManager.MapViewMode.NORAML; //x
    private GLMapResManager.MapViewModeState mapViewModeState = GLMapResManager.MapViewModeState.NORMAL; //y
    /**
     * 底图类型
     */
    private int mapType = MapController.MAP_TYPE_NORMAL; // z
    private int locType = MapController.LOCATION_TYPE_LOCATE;
    private MapCore mapCore; // A
    private Context context; // B
    private MapCallback mapCallback = null; // C
    private MapProjection mapProjection; // D
    private GestureDetector gestureDetector; // E
    private ScaleGestureDetector scaleGestureDetector; // F
    private RotateGestureDetectorDecode mRotateDetector; // G
    private SurfaceHolder surfaceHolder = null; // H
    private MapOverlayViewGroupDecode mOverlaylay; // I
    private WaterMarkerViewDecode mWaterMarkerView;//LOGO // J
    private LocationViewDecode mLocationView; // K
    private CompassViewDecode mCompassView;//指北针 // L
    private ScaleViewDecode mScaleView;//比例尺 // M
    //    private InndoorViewDecode mInndoorView; // N
    private MapController.OnMyLocationChangeListener myLocationChangeListener; // O;
    private MapController.OnMarkerClickListener mMarkerClickListener; // P;
    private MapController.OnPolylineClickListener mPolylineClickListener; // Q;
    private MapController.OnMarkerDragListener mMarkDragListener; // R;
    private MapController.OnMapLoadedListener mMapLoadedListener; // S;
    private MapController.OnCameraChangeListener cameraChangeListener; // T;
    private MapController.OnMapClickListener mapClickListener; // U;
    private MapController.OnMapTouchListener mMapTouchListener; // V;
    private MapController.OnPOIClickListener mPOIClickListener; // W;
    private MapController.OnMapLongClickListener mapLongClickListener; // X;
    private MapController.OnInfoWindowClickListener mInfoWindowClickListener; // Y;
    private MapController.GridUrlListener mGridUrlListener;
    private MapController.InfoWindowAdapter mInfoAdapter; // Z;
    private MapController.InfoWindowAdapter mInfoAdapterSrc; // aa;
    private MapController.GeoJsonServerListener serverListener;
    private View mInfoview; // ab;
    private IMarkerDelegate mInfoWindowMarker; // ac
    private PopupOverlayDecode popupOverlay; // ad
    private IProjectionDelegate projectionDelegate; // ae
    private IUiSettingsDelegate uiSettings; // af
    private LocationSource locationSource; // ag
    private Rect rectMap = new Rect(); // ah
    private MapOnLocationChangedListener locationChangedListener; // ai
    private MultiTouchSupportDecode multiTouchSupport; // aj
    private MyLocationOverlayDecode myLocationOverlay; // ak
    private CameraAnimatorDecode mCameraAnimator; // al
    private int mScrollX = 0; // am -> mScrollX
    private int mScrollY = 0; // an -> mScrollY
    private MapController.CancelableCallback mCallback = null; // ao
    private int ap = 0; // ap
    private Drawable infowindow_bDrawable = null; // aq
    private Location lastLocation; // ar
    //    private MapController.onMapPrintScreenListener mapOnMapPrintScreenListener = null; // as
    private MapController.OnMapScreenShotListener mapOnMapScreenShotListener = null; // at
    private Handler au = new Handler(); // au
    private IndoorBuilding av = null; // av
    private CameraUpdateFactoryDelegate aw = null; // aw
    private Timer mapTimer; // ax
    private TimeChangedReceiver timeChangereceiver = null; // ay
    private static final double MATH_LOG_2 = Math.log(2.0D); // az
    private boolean locationEnabled = true; // aA
    private boolean initProjOver = false; // aB
    private boolean aC = false; // aC
    private boolean aD = false; // aD
    private boolean aF = true;
    private boolean aE = true; // aE
    private boolean isAnimationStep = false; // aF
    private boolean isCallbacking = false; // aG
    private boolean callbackChanged = false; // aH
    private Boolean aI = Boolean.valueOf(false); // aI
    private boolean aJ = false; // aJ
    private boolean aK = true;// aK
    private boolean isShowMapText = true;
    private boolean isTexsureInited = false;// aL
    private boolean isLocationRotateEnabled = true;
    private Handler rmCacheHandler = new Handler();// 缓存清理
    HandlerThread handlerThread = new HandlerThread("check_auth");
    Handler authHandler;
    IGLOverlayLayer baseOverlayLayer = null; // h
    GLMapResManager glMapResManager = null; // i
    IGLSurfaceView glSurfaceView = null; // j
    private MapController.OnMapLevelChangeListener mapLevelChangeListener = null;

    public MapCore getMapCore() // a
    {
        return this.mapCore;
    }

    public int getLineTextureID() // b
    {
        return this.LINE_TEXTURE_ID;
    }

    public MapProjection getMapProjection() {
        if (this.mapProjection == null) {
            this.mapProjection = this.mapCore.getMapstate();
        }
        return this.mapProjection;
    }

    public void initTexsureIdList(GL10 gl) // a
    {
        if (this.isTexsureInited) {
            return;
        }
        int[] ids = new int[500];
        gl.glGenTextures(500, ids, 0);
        for (int i1 = 0; i1 < ids.length; i1++) {
            this.freeTextureList.add(Integer.valueOf(ids[i1]));
        }
        this.isTexsureInited = true;
    }

    public MapDelegateImp(IGLSurfaceView surfaceView, Context context, AttributeSet attributeSet) throws UnistrongException {


        ConfigableConstDecode.packageName = AppInfo.getPackageName(context);
        handlerThread.start();
        authHandler = new Handler(handlerThread.getLooper());
        this.glSurfaceView = surfaceView;
        // Android 24以上"TextureView doesn't support displaying a background drawable"
        if (Build.VERSION.SDK_INT < 24) {
            this.glSurfaceView.setBackgroundColor(Color.argb(255, 235, 235, 235));
        }
        this.context = context;
        this.mapCore = new MapCore(this.context);
        this.mapCallback = new MapCallback(this);
        this.mapCore.setMapCallback(this.mapCallback);
        initProjection();
        //TODO WCB 屏蔽离线信息设置到底层
//        if(Util.getOfflineStatus(this.context)) {//是否离线
//            this.mapCore.setParameter(8002,0,0,0,0);//SDK是否为离线应用
//        }
        this.glMapResManager = new GLMapResManager(this, context);
        this.projectionDelegate = new ProjectionDelegateImpDecode(this);
        this.locationChangedListener = new MapOnLocationChangedListener(this);
        this.uiSettings = new UiSettingsDelegateImpDecode(this);

        /**
         * 平移手势 双指调整仰角手势
         */
        this.gestureDetector = new GestureDetector(context, new MapViewGestureDetectorListener());
        /**
         * 双击手势
         */
        this.gestureDetector.setOnDoubleTapListener(new MapViewDoubleClickListener());

        this.gestureDetector.setIsLongpressEnabled(true);
        /**
         * 拉伸手势 双指zoomout手势
         */
        this.scaleGestureDetector = new ScaleGestureDetector(context, new MyScaleGestureDetector());
        /**
         * 旋转手势
         */
        this.mRotateDetector = new RotateGestureDetectorDecode(context, new MyRotateListener());

        this.multiTouchSupport = new MultiTouchSupportDecode(context, new MapMultiTouchDragListener());

        this.mOverlaylay = new MapOverlayViewGroupDecode(context, this) {
            public void a() {
                super.a();
                au.removeCallbacks(bo);
                au.post(bn);
            }
        };
        this.baseOverlayLayer = new GLOverlayLayerDecode(this);
        this.mWaterMarkerView = new WaterMarkerViewDecode(this.context, this);
        this.mScaleView = new ScaleViewDecode(this.context, this);
//        this.mInndoorView = new InndoorViewDecode(this.context);
        this.mTileOverlayView = new TileOverlayViewDecode(this.context, this);
        this.mZoomView = new ZoomControllerViewDecode(this.context, this);

        this.mLocationView = new LocationViewDecode(this.context, this.mMapMessges, this);
        this.mCompassView = new CompassViewDecode(this.context, this.mMapMessges, this);
        this.mOverlaysImageView = new MapOverlayImageViewDecode(this.context, attributeSet, this);

        this.mWaterMarkerView.setBackgroundColor(Color.argb(255, 235, 235, 235));
        this.mScaleView.setBackgroundColor(Color.argb(255, 235, 235, 235));
        this.mOverlaylay.setBackgroundColor(Color.argb(255, 235, 235, 235));
        this.mTileOverlayView.setBackgroundColor(Color.argb(255, 235, 235, 235));
        this.mZoomView.setBackgroundColor(Color.argb(255, 235, 235, 235));
//        this.mOverlaysImageView.setBackgroundColor(Color.argb(255, 235, 235, 235));
        this.mLocationView.setBackgroundColor(Color.argb(255, 235, 235, 235));

        ViewGroup.LayoutParams localLayoutParams1 = new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        this.mOverlaylay.addView((View) this.glSurfaceView, 0, localLayoutParams1);

        MapOverlayViewGroupDecode.LayoutParamsExt locala1 = new MapOverlayViewGroupDecode.LayoutParamsExt(localLayoutParams1);

//        this.mOverlaylay.addView(this.mOverlaysImageView, locala1);

        this.mOverlaylay.addView(this.mWaterMarkerView, localLayoutParams1);

        this.mOverlaylay.addView(this.mScaleView, localLayoutParams1);

        this.mOverlaylay.addView(this.mTileOverlayView, localLayoutParams1);

        ViewGroup.LayoutParams localLayoutParams2 = new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);


        MapOverlayViewGroupDecode.LayoutParamsExt locala2 = new MapOverlayViewGroupDecode.LayoutParamsExt(-2, -2, new FPoint(0.0F, 0.0F), 0, 0, 83);

        this.mOverlaylay.addView(this.mZoomView, locala2);

        MapOverlayViewGroupDecode.LayoutParamsExt locala3 = new MapOverlayViewGroupDecode.LayoutParamsExt(-2, -2, new FPoint(0.0F, 0.0F), 0, 0, 83);

        this.mOverlaylay.addView(this.mLocationView, locala3);
        try {
            if (!this.uiSettings.isMyLocationButtonEnabled()) {
                this.mLocationView.setVisibility(View.GONE);
            }
        } catch (RemoteException localRemoteException) {
            SDKLogHandler.exception(localRemoteException, "MapDelegateImp", "locationView gone");

            localRemoteException.printStackTrace();
        }
        MapOverlayViewGroupDecode.LayoutParamsExt locala4 = new MapOverlayViewGroupDecode.LayoutParamsExt(-2, -2, new FPoint(0.0F, 0.0F), 0, 0, 51);

        this.mOverlaylay.addView(this.mCompassView, locala4);
//        this.mCompassView.setVisibility(View.GONE);

        this.mCameraAnimator = new CameraAnimatorDecode(context);

        this.myLocationOverlay = new MyLocationOverlayDecode(this, context);
        this.mInfoAdapterSrc = new MapController.InfoWindowAdapter() {
            public View getInfoWindow(Marker paramAnonymousMarker) {
                return null;
            }

            public View getInfoContents(Marker paramAnonymousMarker) {
                return null;
            }
        };
        this.mInfoAdapter = this.mInfoAdapterSrc;
        surfaceView.setRenderer(this);

        IntentFilter localIntentFilter = new IntentFilter();
        localIntentFilter.addAction("android.intent.action.TIME_SET");
        localIntentFilter.addAction("android.intent.action.DATE_CHANGED");
        this.timeChangereceiver = new TimeChangedReceiver();
        this.context.registerReceiver(this.timeChangereceiver, localIntentFilter);

        this.mZoomView.setId(AutoTestConfig.ZoomControllerViewId);
        this.mScaleView.setId(AutoTestConfig.ScaleControlsViewId);
        this.mLocationView.setId(AutoTestConfig.MyLocationViewId);
        this.mCompassView.setId(AutoTestConfig.CompassViewId);
    }

    private void initUserGrid() {
        if (mapCore != null) {
            mapCore.setParameter(WXM_PARAMETERNAME_TILE_MAP, 1, 0, 0, 0);
            mapCore.setParameter(WXM_PARAMETERNAME_HIDE_VECTOR_MAP, 1, 0, 0, 0);
        }
    }

    public class TimeChangedReceiver extends BroadcastReceiver {
        public TimeChangedReceiver() {
        }

        public void onReceive(Context paramContext, Intent paramIntent) {
            String str = paramIntent.getAction();
            if ((!"android.intent.action.DATE_CHANGED".equals(str)) ||

                    ("android.intent.action.TIME_SET".equals(str))) {
                MapDelegateImp.this.handler.sendEmptyMessage(22);
            }
        }
    }

    private boolean onPauseStatus; // aO

    public void setOnMyLocationChangeListener(
            MapController.OnMyLocationChangeListener paramOnMyLocationChangeListener) // a
    // ->setOnMyLocationChangeListener
    {
        this.myLocationChangeListener = paramOnMyLocationChangeListener;
    }

    public void onResume() // d
    {
        LogManager.writeLog(LogManager.productInfo,
                hashCode() + " onResume ", 111);

        runTimer();
        if (this.mapCallback != null) {
            this.mapCallback.onResume(this.mapCore);
            setRunLowFrame(false);
        }
        if (this.mTileOverlayView != null) {
            this.mTileOverlayView.e();
        }
        if (this.myLocationOverlay != null) {
//            this.myLocationOverlay.registerSensorListener();
        }
        this.onPauseStatus = false;
    }

    public void onPause() // e
    {
        LogManager.writeLog(LogManager.productInfo,
                hashCode() + " onPause ", 111);

        this.onPauseStatus = true;
        cancelTimer();
        if (this.mapCallback != null) {
            this.mapCallback.onPause();
        }
        if (this.mTileOverlayView != null) {
            this.mTileOverlayView.d();
        }
        if (this.myLocationOverlay != null) {
            this.myLocationOverlay.unRegisterSensorListener();
        }
        VMapDataCache.getInstance().reset();
    }

    private void initProjection() // ab
    {
        if (!this.initProjOver) {
            this.mapCore.newMap();
            this.mapProjection = this.mapCore.getMapstate();
            IPoint paramDPoint = new IPoint();
            this.latlon2Geo(this.mapcentery, this.mapcenterx, paramDPoint);
            this.mapProjection.setGeoCenter(paramDPoint.x, paramDPoint.y);
            this.mapProjection.setMapAngle(this.mapangle);
            this.mapProjection.setMapZoomer(this.mapzoomer);
            this.mapProjection.setCameraHeaderAngle(this.cameraheaderangle);
            this.initProjOver = true;
        }
    }


    public void setMyLocationStyle(MyLocationStyle paramMyLocationStyle) // a
    {
        if (this.myLocationOverlay != null) {
            this.myLocationOverlay.setMyLocationStyle(paramMyLocationStyle);
        }
    }

    public void setMyLocationType(int paramInt) // a
    {
        if (this.myLocationOverlay != null) {
            this.myLocationOverlay.a(paramInt);
            this.locType = paramInt;
        }
    }

    public int getMyLocationType() {
        return this.locType;
    }

    public void setLocationRotateEnabled(boolean paramBoolean) {
        this.isLocationRotateEnabled = paramBoolean;
        if (this.isLocationRotateEnabled) {
            if (this.myLocationOverlay != null) {
                this.myLocationOverlay.registerSensorListener();
            }
        } else {
            if (this.myLocationOverlay != null) {
                this.myLocationOverlay.unRegister2DSensorListener();
            }
        }
    }

    public boolean isLocationRotateEnabled() {
        return this.isLocationRotateEnabled;
    }

    public void setRotateAngle(float paramFloat) // a
            throws RemoteException {
        if (this.myLocationOverlay != null) {
            this.myLocationOverlay.setRotateAngle(paramFloat);
        }
    }

    public void showMyLocationOverlay(Location paramLocation)
            throws RemoteException {
        if (paramLocation == null) {
            return;
        }
        LatLng localLatLng = new LatLng(paramLocation.getLatitude(),
                paramLocation.getLongitude());
        try {
            if ((!this.locationEnabled) || (this.locationSource == null)) {
                this.myLocationOverlay.remove();
                this.myLocationOverlay = null;
                return;
            }
            if ((this.myLocationOverlay == null) || (this.lastLocation == null)) {
                if (this.myLocationOverlay == null) {
                    this.myLocationOverlay = new MyLocationOverlayDecode(this,
                            this.context);
                }
                moveCamera(CameraUpdateFactoryDelegate.a(
                        localLatLng, this.mapProjection.getMapZoomer()));
            }
        } catch (RemoteException localRemoteException) {
            SDKLogHandler.exception(localRemoteException, "MapDelegateImp",
                    "showMyLocationOverlay");

            localRemoteException.printStackTrace();
        }
        this.myLocationOverlay.setCentAndRadius(paramLocation);
        if ((this.myLocationChangeListener != null)
                && ((this.lastLocation == null)
                || (this.lastLocation.getBearing() != paramLocation
                .getBearing())
                || (this.lastLocation.getAccuracy() != paramLocation
                .getAccuracy())
                || (this.lastLocation.getLatitude() != paramLocation
                .getLatitude()) || (this.lastLocation
                .getLongitude() != paramLocation.getLongitude()))) {
            this.myLocationChangeListener.onMyLocationChange(paramLocation);
        }
        this.lastLocation = new Location(paramLocation);
        setRunLowFrame(false);
    }

    @Override
    public void setGridUrlListener(MapController.GridUrlListener listener) {
        this.mGridUrlListener = listener;
        initUserGrid();
    }

    protected String getUserGridURL(String mesh) {
        if (mapCore != null) {
            Tile tile = new Tile();
            mapCore.quadKey2Tile(mesh, tile);
            if (this.mGridUrlListener != null && tile != null) {
                String url = mGridUrlListener.getGridUrl(tile.x, tile.y, tile.level-6);
                return url;
            }
        }
        return null;
    }

    // 初始化GeojsonOverlay
    public void setGeojsonOptions(GeoJsonLayerOptions options) {
        if (options == null) {
            options = new GeoJsonLayerOptions();
        }
        if (this.geojsonOverlayManager == null) {
            this.geojsonOverlayManager = new GeojsonOverlayManager(this);
        }
        // 由于数据变化，必须在刷新之前执行clear操作
        this.geojsonOverlayManager.clearGeojsonOverlay();
        this.geojsonOverlayManager.setOptions(options);
        this.refreshTileOverlay(true);
    }

    @Override
    public void setGeoJsonServerListener(MapController.GeoJsonServerListener listener) {
        this.serverListener = listener;
    }

    @Override
    public MapController.GeoJsonServerListener getGeoJsonServerListener() {
        return this.serverListener;
    }

    public void clearGeojsonOverlay() {
        if (this.geojsonOverlayManager != null) {
            this.geojsonOverlayManager.clearGeojsonOverlay();
        }
    }

    // 销毁GeojsonOverlay
    public void destroyGeojsonOverlay() {
        if (this.geojsonOverlayManager != null) {
            this.geojsonOverlayManager.destroyGeojsonOverlay();
            this.geojsonOverlayManager = null;
        }
    }

    private ClusterBuild clusterBuild;

    public void setClusterOptions(ClusterOptions options) {
        try {
            if (options == null) {
                throw new UnistrongException("clusteroption is null");
            }
            if (clusterBuild == null) {
                clusterBuild = new ClusterBuild(options, this);
            } else {
                clusterBuild.setOption(options);
            }
        } catch (UnistrongException e) {
            SDKLogHandler.exception(e, "MapDelegateImp", "setClusterOptions");
            e.printStackTrace();
        }
    }

    public void clearQuad() {
        if (clusterBuild == null) {
            return;
        }
        clusterBuild.clearQuad();
    }

    public void removeQuad(List<QuadTreeNodeData> quadTreeNodeDataList) {
        if (clusterBuild == null) {
            return;
        }
        clusterBuild.removeQuad(quadTreeNodeDataList);
    }

    public void addQuad(List<QuadTreeNodeData> quadTreeNodeDataList) {
        if (clusterBuild == null) {
            return;
        }
        clusterBuild.addQuad(quadTreeNodeDataList);
    }


    public void buildCluster() {
        try {
            if (clusterBuild == null) {
                throw new UnistrongException("please setClusterOptions");
            }
            clusterBuild.build();
        } catch (UnistrongException e) {
            SDKLogHandler.exception(e, "MapDelegateImp", "buildCluster");
            e.printStackTrace();
        }
    }

    public void searchCluster() throws RemoteException {
        try {
            if (clusterBuild == null) {
                throw new UnistrongException("please setClusterOptions");
            }
            clusterBuild.search();
        } catch (UnistrongException e) {
            SDKLogHandler.exception(e, "MapDelegateImp", "searchCluster");
            e.printStackTrace();
        }
    }

    public void showZoomControlsEnabled(boolean paramBoolean) {
        if (this.mZoomView == null) {
            return;
        }
        if (paramBoolean) {
            this.mZoomView.setVisibility(View.VISIBLE);
        } else {
            this.mZoomView.setVisibility(View.GONE);
        }
    }

    public void destroy() // f
    {
        LogManager.writeLog(LogManager.productInfo,
                hashCode() + " destroy ", 111);

        this.aI = Boolean.valueOf(true);
        try {
            cancelTimer();
            if (this.lineDashTexture != null) {
                this.lineDashTexture.recycle();
                this.lineDashTexture = null;
            }
            if (this.lineTexture != null) {
                this.lineTexture.recycle();
                this.lineTexture = null;
            }
            if ((this.handler != null) && (this.k != null)) {
                this.handler.removeCallbacks(this.k);
                this.k = null;
            }
            if (this.frameHandler != null) {
                this.frameHandler.removeCallbacks(this.frameRunnable);
            }
            if (this.timeChangereceiver != null) {
                this.context.unregisterReceiver(this.timeChangereceiver);
            }
            if (this.mZoomView != null) {
                this.mZoomView.a(); // ->destroy
            }
            if (this.mScaleView != null) {
                this.mScaleView.a();
            }
            if (this.mWaterMarkerView != null) {
                this.mWaterMarkerView.destroy();
            }
            if (this.mLocationView != null) {
                this.mLocationView.a();
            }
            if (this.mCompassView != null) {
                this.mCompassView.a();
            }
            if (this.mTileOverlayView != null) {
                this.mTileOverlayView.clear();
                this.mTileOverlayView.f();
            }
            if (this.baseOverlayLayer != null) {
                this.baseOverlayLayer.destroyOverlay();
            }
            if (this.mOverlaysImageView != null) {
                this.mOverlaysImageView.destroy();
            }
            // geojson
            if (this.geojsonOverlayManager != null) {
                this.geojsonOverlayManager.destroyGeojsonOverlay();
            }


            if (this.mapCallback != null) {
                this.mapCallback.OnMapDestory(this.mapCore);
                this.mapCore.setMapCallback(null);
                this.mapCallback = null;
            }
            hiddenInfoWindowShown();

            Util.a(this.infowindow_bDrawable);
            if (this.freeTextureList != null) {
                this.freeTextureList.clear();
            }
            if (this.textureList != null) {
                this.textureList.clear();
            }
            if (this.mapCore != null) {
                a(new Runnable() {
                    public void run() {
                        try {
                            //AMapDelegateImp.changeBearing(AMapDelegateImp.this).destroy();
                            //AMapDelegateImp.a(AMapDelegateImp.this, null);
                            mapCore.destroy(); //上两句翻译
                            mapCore = null;
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }
                });
                Thread.sleep(200L);
            }
            if (this.mOverlaylay != null) {
                this.mOverlaylay.removeAllViews();
                this.mOverlaylay = null;
            }
            this.authHandler.removeCallbacks(authThread);
            this.handlerThread.quit();
            handlerThread = null;
            authHandler = null;
            SDKLogHandler.colseLogThread();
            LogManager.writeLog("map", "完全释放", 113);
        } catch (Throwable localThrowable) {
            SDKLogHandler.exception(localThrowable, "MapDelegateImp", "destroy");

            LogManager.writeLog("map",
                    "没有完全释放" + localThrowable.getMessage(), 111);

            localThrowable.printStackTrace();
        }
    }

    public void showMyLocationButtonEnabled(boolean paramBoolean) // b
    {
        if (this.mLocationView == null) {
            return;
        }
        if (paramBoolean) {
            this.mLocationView.setVisibility(View.VISIBLE);
        } else {
            this.mLocationView.setVisibility(View.GONE);
        }
    }

    public void showCompassEnabled(boolean paramBoolean) // c
    {
        if (this.mCompassView == null) {
            return;
        }
        if (paramBoolean) {
            this.mCompassView.setVisibility(View.VISIBLE);
            this.mCompassView.invalidateAngle();
        } else {
            this.mCompassView.setVisibility(View.GONE);
        }
    }

    void invalidateCompassView() // g
    {
        this.handler.obtainMessage(COMPASS_UPDATE).sendToTarget();
    }

    public void showScaleEnabled(boolean paramBoolean) // d
    {
        if (this.mScaleView == null) {
            return;
        }
        if (paramBoolean) {
            this.mScaleView.setVisibility(View.VISIBLE);
            invalidateScaleState();
        } else {
            this.mScaleView.setScaleText("");
            this.mScaleView.setScaleLength(0);
            this.mScaleView.setVisibility(View.GONE);
        }
    }

    void invalidateScaleState() // h
    {
        this.handler.sendEmptyMessage(SCALE_UPDATE);
    }

    void changeScaleState() // i
    {
        if (this.mScaleView == null) {
            return;
        }
        try {
            LatLng localLatLng = getCameraPosition().target;
            float f1 = this.mapzoomer;
            if (this.initProjOver) {
                f1 = this.mapProjection.getMapZoomer();
            }
            float f2 = 1.0F;

            double d1 = (float) (Math
                    .cos(localLatLng.latitude * 3.141592653589793D / 180.0D) * 2.0D * 3.141592653589793D * 6378137.0D / (256.0D * Math
                    .pow(2.0D, f1)));
            int i1 = (int) (this.scaleArray[((int) f1)] / d1 * f2);
            String str = Util
                    .distance2Text(this.scaleArray[((int) f1)]);
            this.mScaleView.setScaleLength(i1);
            this.mScaleView.setScaleText(str);
            this.mScaleView.invalidate();
        } catch (Throwable localThrowable) {
            SDKLogHandler.exception(localThrowable, "MapDelegateImp",
                    "changeScaleState");

            localThrowable.printStackTrace();
        }
    }

    public boolean removeGLOverlay(String id) // a
            throws RemoteException {
        setRunLowFrame(false);
        if (this.baseOverlayLayer != null && this.baseOverlayLayer.removeOverlay(id)) {
            return true;
        } else if (this.geojsonOverlayManager != null
                && this.geojsonOverlayManager.removeOverlay(id)) {
            return true;
        } else {
            return false;
        }

//        return this.baseOverlayLayer.removeOverlay(id);
    }

    public void set2DMapRotateEnable(boolean isEnable) {
        this.aF = isEnable;
    }

    private volatile boolean isRunLowFrame = false; // aP
    private volatile boolean postDelayedRun = false; // aQ
    private Handler frameHandler = new Handler();
    private Runnable frameRunnable = new Runnable() { // aS 此代码精简自 com.leador.api.mapcore.i
        public synchronized void run() {
            if (postDelayedRun) {
                isRunLowFrame = true;
                postDelayedRun = false;
            }
        }
    };

    Runnable k; // k

    public synchronized void setRunLowFrame(boolean isLow) // e
    {
        if (isLow) {
            if ((!this.isRunLowFrame) && (!this.postDelayedRun)) {
                this.postDelayedRun = true;
                this.frameHandler.postDelayed(this.frameRunnable, 6000L);
            }
        } else {
            this.postDelayedRun = false;
            this.frameHandler.removeCallbacks(this.frameRunnable);
            this.isRunLowFrame = false;
        }
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        if ((this.aI.booleanValue()) || (this.onPauseStatus)) {
            return;
        }

        try {
            if (this.bp) {
                this.bp = false;
                c(gl);
            }
            /** 清空屏幕 */
            gl.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
            gl.glClear(16640);
            this.mapCore.setGL(gl);
            this.mapCore.drawFrame(gl);
            /** 一次生成500个纹理id */
            initTexsureIdList(gl);
            this.mTileOverlayView.onDrawGL(gl);
            /** 基本图形,线，面绘制 */
            this.baseOverlayLayer.onDrawGL(gl, false, this.ap);
            /** marker的绘制 */
            this.mOverlaysImageView.onDrawGL(gl);
            /** geojson的绘制 */
            if (this.geojsonOverlayManager != null) {
                /** 基本图形,线，面绘制 */
                this.geojsonOverlayManager.onDrawGL(gl, false, this.ap);
                /** marker的绘制 */
                this.geojsonOverlayManager.onDrawGL(gl);
            }

            if (this.popupOverlay != null) {
                this.popupOverlay.onDrawGL(gl);
            }
            if (this.aJ) {
                int status = this.mapCore.canStopRenderMap() ? 1 : 0;
                Bitmap localBitmap = SavePixels(0, 0, getWidth(), getHeight(),
                        gl);
                Message message = this.handler.obtainMessage(SCREEN_SHOT, localBitmap);
                message.arg1 = status;
                message.sendToTarget();
                this.aJ = false;
            }
            if (!this.mCameraAnimator.isFinished()) {
                this.handler.sendEmptyMessage(CAMERA_ANIMATION);
            }
            if (!this.aC) {
                this.handler.sendEmptyMessage(ON_MAP_LOADED);
                this.aC = true;
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public Rect getRect() {
        return this.rectMap;
    }

    public int getMapWidth() {
        return this.rectMap.width();
    }

    public int getMapHeight() {
        return this.rectMap.height();
    }

    public int getWidth() // m
    {
        return this.glSurfaceView.getWidth();
    }

    public int getHeight() // n
    {
        return this.glSurfaceView.getHeight();
    }

    public void setMyTrafficStyle(int paramInt1, int paramInt2, int paramInt3, int paramInt4) // a
    {
        if (this.initProjOver) {
            this.mapCore.setParameter(2201, 1, 1, 1, 1);

            this.mapCore.setParameter(2202, paramInt1, paramInt2, paramInt3,
                    paramInt4);
        }
    }

    private synchronized void runTimer() // ad
    {
        if (this.mapTimer != null) {
            cancelTimer();
        }
        if (this.mapTimer == null) {
            this.mapTimer = new Timer();
        }
        this.mapTimer.schedule(new MyTask(this), 0L, 1000 / RERCOUNT);
    }

    private synchronized void cancelTimer() // ae
    {
        if (this.mapTimer != null) {
            this.mapTimer.cancel();
            this.mapTimer = null;
        }
    }

    public void setMapStyleIconForPath(final String stylePath, final String iconPath) {
        a(new Runnable() {
            @Override
            public void run() {
                mapCore.setParameter(2501, 0, 0, 0, 0);
                glMapResManager.setStyleDataForPath(stylePath, iconPath);
                mapCore.setParameter(2501, 1, 1, 0, 0);
            }
        });

    }

    private volatile boolean isSetInternaltexture = false; // aT

    private synchronized void setInternaltexture() // af
    {
        try {
            if (!this.isSetInternaltexture) {
                this.glMapResManager.setStyleData();

                this.glMapResManager.setIconsData(true);

                this.glMapResManager.setTrafficTexture(true);
                this.glMapResManager.setOtherMapTexture(true);
                this.glMapResManager.setRoadGuideTexture(true);

                this.glMapResManager.setBkTexture(true);
                this.isSetInternaltexture = true;
            }
        } catch (Throwable localThrowable) {
            SDKLogHandler.exception(localThrowable, "MapDelegateImp",
                    "setTexture");

            LogManager.writeLog("mapv1.0", "mapInitError", 112);
            localThrowable.printStackTrace();
        }
    }

    public int getDottedLineTextureID() // o
    {
        return this.o;
    }

    public void redrawInfoWindow() // p
    {
        try {
            if (!this.aK) {
                return;
            }
            if (mInfoview == null) {
                return;
            }
            if (mInfoWindowMarker == null) {
                return;
            }
            MapOverlayViewGroupDecode.LayoutParamsExt locala = (MapOverlayViewGroupDecode.LayoutParamsExt) this.mInfoview.getLayoutParams();
            if (locala != null) {
                Rect localRect = this.mInfoWindowMarker.getRect();
                if ((localRect == null) || (localRect.height() == 0) || (localRect.width() == 0)) {
                    return;
                }
                int i1 = this.mInfoWindowMarker.D() + this.mInfoWindowMarker.B();

                int i2 = this.mInfoWindowMarker.E() + this.mInfoWindowMarker.C() + 2;
                locala.point = this.mInfoWindowMarker.anchorUVoff();
                locala.mapx = i1;
                locala.mapy = i2;
                if (this.popupOverlay != null) {
                    this.popupOverlay.a(this.mInfoWindowMarker.anchorUVoff());
                    this.popupOverlay.b(i1, i2);
                }
            }

            this.mOverlaylay.onLayout(false, 0, 0, 0, 0);

            setRunLowFrame(false);
        } catch (Throwable throwable) {
            SDKLogHandler.exception(throwable, "MapDelegateImp",
                    "redrawInfoWindow");

            throwable.printStackTrace();
        }
    }

    public void setZOrderOnTop_(boolean paramBoolean) // f
    {
        this.glSurfaceView.setZOrderOnTop(paramBoolean);
    }

    public CameraPosition getCameraPosition() // q
            throws RemoteException {
        return getCameraPositionPrj_(this.bj);
    }

    public void setMaxZoomLevel(float maxZoomLevel) {
        if (maxZoomLevel >= 3 && maxZoomLevel <= 22) {
            if (Util.maxzoomlevel > maxZoomLevel) {
                this.mapProjection.setMapZoomer(maxZoomLevel);
            }
            Util.maxzoomlevel = maxZoomLevel;
        }
    }

    public float getMaxZoomLevel() {
        return Util.maxzoomlevel;
    }

    public float getMinZoomLevel() {
        return 3.0F;
    }

    public void moveCamera(CameraUpdateFactoryDelegate cameraUpdateFactoryDelegate) // a
            throws RemoteException {
        if (this.onPauseStatus) {
            this.mMapMessges.f();
        }
        if (cameraUpdateFactoryDelegate.nowType == CameraUpdateFactoryDelegate.Type.newLatLngBounds) {
            LMapThrowException
                    .ThrowIllegalStateException((getWidth() > 0)
                            && (getHeight() > 0), "the map must have a size");
        }
        stopAnimation();
        cameraUpdateFactoryDelegate.isChangeFinished = true;
        cameraUpdateFactoryDelegate.n = this.bj;
        this.mMapMessges.addMessage(cameraUpdateFactoryDelegate);
    }

    public void animateCamera(CameraUpdateFactoryDelegate cameraUpdateFactoryDelegate) // b
            throws RemoteException {
        animateCamera(cameraUpdateFactoryDelegate, null);
    }

    public void animateCamera(CameraUpdateFactoryDelegate cameraUpdateFactoryDelegate,
                              MapController.CancelableCallback callback) // a
        // -->animateCameraWithCallback
            throws RemoteException {
        animateCameraWithDurationAndCallback(cameraUpdateFactoryDelegate, 250L,
                callback);
    }

    public void animateCameraWithDurationAndCallback(
            CameraUpdateFactoryDelegate cameraUpdateFactoryDelegate, long durationMs,
            MapController.CancelableCallback paramCancelableCallback) // a
            throws RemoteException {
        if (cameraUpdateFactoryDelegate.nowType == CameraUpdateFactoryDelegate.Type.newLatLngBounds) {
            LMapThrowException
                    .ThrowIllegalStateException((getWidth() > 0)
                            && (getHeight() > 0), "the map must have a size");
        }
        /** 停止之前动画状态 */
        if (!this.mCameraAnimator.isFinished()) {
            if (this.mCameraAnimator.isLocModeChanged()) {
                return;
            }
            this.mCameraAnimator.forceFinished(true);
            if (this.mCallback != null) {
                this.mCallback.onCancel();
            }
        }
        // 设置动画是否是定位模式切换
        if (cameraUpdateFactoryDelegate.nowType == CameraUpdateFactoryDelegate.Type.newCameraPosition
                && durationMs == 400L) {
            this.mCameraAnimator.setLocModeChanged(true);
        } else {
            this.mCameraAnimator.setLocModeChanged(false);
        }
        this.mCameraAnimator.b(this.bj);
        this.mCallback = paramCancelableCallback;
        if (this.isCallbacking) {
            this.callbackChanged = true;
        }
        this.isAnimationStep = false;
        /** 平移 */
        if (cameraUpdateFactoryDelegate.nowType == CameraUpdateFactoryDelegate.Type.scrollBy) {
            if ((cameraUpdateFactoryDelegate.xPixel == 0.0F) && (cameraUpdateFactoryDelegate.yPixel == 0.0F)) {
                this.handler.obtainMessage(CAMERA_UPDATE_FINISH)
                        .sendToTarget();
                return;
            }
            this.mCameraAnimator.b(false);
            IPoint geoCenter = new IPoint();
            this.mapProjection.getGeoCenter(geoCenter);
            IPoint newCenter = new IPoint();

            /** 中心点平移距离量 */
            getPixel2Geo(getWidth() / 2 + (int) cameraUpdateFactoryDelegate.xPixel, getHeight() / 2
                    + (int) cameraUpdateFactoryDelegate.yPixel, newCenter);

            this.mCameraAnimator
                    .setInterpolator(new AccelerateDecelerateInterpolator());
            this.mCameraAnimator.startChangeCamera(geoCenter.x,
                    geoCenter.y, this.mapProjection.getMapZoomer(),
                    this.mapProjection.getMapAngle(),
                    this.mapProjection.getCameraHeaderAngle(), newCenter.x
                            - geoCenter.x, newCenter.y - geoCenter.y,
                    0.0F, 0.0F, 0.0F, durationMs);
        }

        /** 放大 **/
        else if (cameraUpdateFactoryDelegate.nowType == CameraUpdateFactoryDelegate.Type.zoomIn) {
            float zoom = this.mapProjection.getMapZoomer();
            float dz = Util.checkZoomLevel(zoom + 1.0F) - zoom;
            if (dz == 0.0F) {
                this.handler.obtainMessage(
                        CAMERA_UPDATE_FINISH).sendToTarget();
                return;
            }
            IPoint geoCenter = new IPoint();
            if (this.bj) {
                getPixel2Geo(this.xPixel, this.yPixel, geoCenter);
            } else {
                this.mapProjection.getGeoCenter(geoCenter);
            }
            this.mCameraAnimator
                    .setInterpolator(new AccelerateInterpolator());
            this.mCameraAnimator.startChangeCamera(geoCenter.x,
                    geoCenter.y, zoom, this.mapProjection.getMapAngle(),
                    this.mapProjection.getCameraHeaderAngle(), 0, 0, dz,
                    0.0F, 0.0F, durationMs);
        }

        /** 缩小 **/
        else if (cameraUpdateFactoryDelegate.nowType == CameraUpdateFactoryDelegate.Type.zoomOut) {
            float zoom = this.mapProjection.getMapZoomer();
            float dz = Util.checkZoomLevel(zoom - 1.0F) - zoom;
            if (dz == 0.0F) {
                this.handler.obtainMessage(
                        CAMERA_UPDATE_FINISH).sendToTarget();
                return;
            }
            IPoint geoCenter = new IPoint();
            if (this.bj) {
                getPixel2Geo(this.xPixel, this.yPixel, geoCenter);
            } else {
                this.mapProjection.getGeoCenter(geoCenter);
            }
            this.mCameraAnimator
                    .setInterpolator(new AccelerateInterpolator());
            this.mCameraAnimator.startChangeCamera(geoCenter.x,
                    geoCenter.y, zoom, this.mapProjection.getMapAngle(),
                    this.mapProjection.getCameraHeaderAngle(), 0, 0, dz,
                    0.0F, 0.0F, durationMs);
        }

        /** 设置地图级别  **/
        else if (cameraUpdateFactoryDelegate.nowType == CameraUpdateFactoryDelegate.Type.zoomTo) {
            float zoom = this.mapProjection.getMapZoomer();
            float dz = Util.checkZoomLevel(cameraUpdateFactoryDelegate.zoom)
                    - zoom;
            if (dz == 0.0F) {
                this.handler.obtainMessage(
                        CAMERA_UPDATE_FINISH).sendToTarget();
                return;
            }
            IPoint geoCenter = new IPoint();
            if (this.bj) {
                getPixel2Geo(this.xPixel, this.yPixel, geoCenter);
            } else {
                this.mapProjection.getGeoCenter(geoCenter);
            }
            this.mCameraAnimator
                    .setInterpolator(new AccelerateInterpolator());
            this.mCameraAnimator.startChangeCamera(geoCenter.x,
                    geoCenter.y, zoom, this.mapProjection.getMapAngle(),
                    this.mapProjection.getCameraHeaderAngle(), 0, 0, dz,
                    0.0F, 0.0F, durationMs);
        }

        /** 地图级别变化量 */
        else if (cameraUpdateFactoryDelegate.nowType == CameraUpdateFactoryDelegate.Type.zoomBy) {
            this.mCameraAnimator.b(false);
            float amount = cameraUpdateFactoryDelegate.amount;
            float zoom = this.mapProjection.getMapZoomer();
            float dz = Util.checkZoomLevel(zoom + amount) - zoom;
            if (dz == 0.0F) {
                this.handler.obtainMessage(
                        CAMERA_UPDATE_FINISH).sendToTarget();
                return;
            }
            Point focus = cameraUpdateFactoryDelegate.focus;
            IPoint geoCenter = new IPoint();
            this.mapProjection.getGeoCenter(geoCenter);
            int dx = 0;
            int dy = 0;
            IPoint geoPt = new IPoint();
            if (focus != null) {
                getPixel2Geo(focus.x, focus.y, geoPt);
                dx = geoCenter.x - geoPt.x;
                dy = geoCenter.y - geoPt.y;
                dx = (int) (dx / Math.pow(2, amount) - dx);
                dy = (int) (dy / Math.pow(2, amount) - dy);
            } else if (this.bj) {
                getPixel2Geo(this.xPixel, this.yPixel, geoPt);
                dx = geoCenter.x - geoPt.x;
                dy = geoCenter.y - geoPt.y;
                dx = (int) (dx / Math.pow(2, amount) - dx);
                dy = (int) (dy / Math.pow(2, amount) - dy);
            }
            this.mCameraAnimator
                    .setInterpolator(new AccelerateInterpolator());
            this.mCameraAnimator.startChangeCamera(geoCenter.x, geoCenter.y, zoom, this.mapProjection.getMapAngle(),
                    this.mapProjection.getCameraHeaderAngle(), dx, dy, dz, 0.0F, 0.0F, durationMs);
        }

        /** 设置地图当前状态 中心点 级别 旋转角度 倾斜角等 */
        else if (cameraUpdateFactoryDelegate.nowType == CameraUpdateFactoryDelegate.Type.newCameraPosition) {
            IPoint geoCenter = new IPoint();
            if (this.bj) {
                getPixel2Geo(this.xPixel, this.yPixel, geoCenter);
            } else {
                this.mapProjection.getGeoCenter(geoCenter);
            }
            IPoint localIPoint5 = new IPoint();
            CameraPosition cp = cameraUpdateFactoryDelegate.h;
            MapProjection.lonlat2Geo(cp.target.longitude, cp.target.latitude, localIPoint5);

            float zoom = this.mapProjection.getMapZoomer();
            int dx = localIPoint5.x - geoCenter.x;
            int dy = localIPoint5.y - geoCenter.y;
            float dz = Util
                    .checkZoomLevel(cp.zoom)
                    - zoom;
            float bearing = this.mapProjection.getMapAngle();
            float dbearing = cp.bearing
                    % 360.0F - bearing % 360.0F;

            dbearing = Math.abs(dbearing) >= 180.0F ? dbearing - Math.signum(dbearing)
                    * 360.0F : dbearing;
            float tilt = this.mapProjection.getCameraHeaderAngle();
            float dtilt = Util.checkTilt((cp).tilt, zoom) - tilt;
            if ((dx == 0) && (dy == 0) && (dz == 0.0F)
                    && (dbearing == 0.0F) && (dtilt == 0.0F)) {
                this.handler.obtainMessage(CAMERA_UPDATE_FINISH)
                        .sendToTarget();
                return;
            }
            this.mCameraAnimator
                    .setInterpolator(new AccelerateInterpolator());
            this.mCameraAnimator.startChangeCamera(geoCenter.x,
                    geoCenter.y, zoom, bearing, tilt, dx, dy, dz, dbearing,
                    dtilt, durationMs);
        }

        /** 设置旋转角度 */
        else if (cameraUpdateFactoryDelegate.nowType == CameraUpdateFactoryDelegate.Type.changeBearing) {
            float bearing = this.mapProjection.getMapAngle();
            float dbearing = cameraUpdateFactoryDelegate.bearing % 360.0F - bearing % 360.0F;

            dbearing = Math.abs(dbearing) >= 180.0F ? dbearing - Math.signum(dbearing)
                    * 360.0F : dbearing;
            if (dbearing == 0.0F) {
                this.handler.obtainMessage(CAMERA_UPDATE_FINISH).sendToTarget();
                return;
            }
            IPoint cp = new IPoint();
            if (this.bj) {
                getPixel2Geo(this.xPixel, this.yPixel, cp);
            } else {
                this.mapProjection.getGeoCenter(cp);
            }
            this.mCameraAnimator.setInterpolator(new AccelerateInterpolator());
            this.mCameraAnimator.startChangeCamera(cp.x, cp.y, this.mapProjection.getMapZoomer(), bearing, this.mapProjection.getCameraHeaderAngle(),
                    0, 0, 0.0F, dbearing, 0.0F, durationMs);

        }

        /** 双指滑下 */
        else if (cameraUpdateFactoryDelegate.nowType == CameraUpdateFactoryDelegate.Type.changeTilt) {
            float tilt = this.mapProjection.getCameraHeaderAngle();
            float dtilt = cameraUpdateFactoryDelegate.tilt - tilt;
            if (dtilt == 0.0F) {
                this.handler.obtainMessage(CAMERA_UPDATE_FINISH)
                        .sendToTarget();
                return;
            }
            IPoint cp = new IPoint();
            if (this.bj) {
                getPixel2Geo(this.xPixel, this.yPixel, cp);
            } else {
                this.mapProjection
                        .getGeoCenter(cp);
            }
            this.mCameraAnimator
                    .setInterpolator(new AccelerateInterpolator());
            this.mCameraAnimator.startChangeCamera(cp.x, cp.y,
                    this.mapProjection.getMapZoomer(),
                    this.mapProjection.getMapAngle(), tilt, 0, 0,
                    0.0F, 0.0F, dtilt, durationMs);
        }

        /** 设置地图中心点 */
        else if (cameraUpdateFactoryDelegate.nowType == CameraUpdateFactoryDelegate.Type.changeCenter) {
            IPoint geoCenter = new IPoint();
            if (this.bj) {
                getPixel2Geo(this.xPixel, this.yPixel, geoCenter);
            } else {
                this.mapProjection
                        .getGeoCenter(geoCenter);
            }
            int dx = cameraUpdateFactoryDelegate.geoPoint.x - geoCenter.x;
            int dy = cameraUpdateFactoryDelegate.geoPoint.y - geoCenter.y;
            if ((dx == 0) && (dy == 0)) {
                this.handler.obtainMessage(CAMERA_UPDATE_FINISH)
                        .sendToTarget();
                return;
            }
            this.mCameraAnimator
                    .setInterpolator(new AccelerateDecelerateInterpolator());
            this.mCameraAnimator.startChangeCamera(
                    geoCenter.x, geoCenter.y,
                    this.mapProjection.getMapZoomer(),
                    this.mapProjection.getMapAngle(),
                    this.mapProjection
                            .getCameraHeaderAngle(), dx,
                    dy, 0.0F, 0.0F, 0.0F, durationMs);
        }

        /** 重置地图显示范围 */
        else if ((cameraUpdateFactoryDelegate.nowType == CameraUpdateFactoryDelegate.Type.newLatLngBounds)
                || (cameraUpdateFactoryDelegate.nowType == CameraUpdateFactoryDelegate.Type.newLatLngBoundsWithSize)) {
            final int width;//i1
            final int height;//i2
            float tilt;
            float zoom;

            this.mCameraAnimator.b(false);
            if (cameraUpdateFactoryDelegate.nowType == CameraUpdateFactoryDelegate.Type.newLatLngBounds) {
                width = getWidth();
                height = getHeight();
            } else {
                width = cameraUpdateFactoryDelegate.width;
                height = cameraUpdateFactoryDelegate.height;
            }
            float bearing = this.mapProjection.getMapAngle() % 360.0F;
            tilt = this.mapProjection.getCameraHeaderAngle();
            float dbearing = -bearing;//f8=-f6
            dbearing = Math.abs(dbearing) >= 180.0F ? dbearing - Math.signum(dbearing) * 360.0F : dbearing;
            float dtilt = -tilt;//f9 = -f7
            final LatLngBounds localLatLngBounds = cameraUpdateFactoryDelegate.bounds;
            final int padding = cameraUpdateFactoryDelegate.padding;//i7
            IPoint geoCenter = new IPoint();
            this.mapProjection.getGeoCenter(geoCenter);
            zoom = this.mapProjection.getMapZoomer();
            this.mCameraAnimator.setInterpolator(new AccelerateInterpolator());
            IPoint winPt1 = new IPoint();
            IPoint winPt2 = new IPoint();
            MapProjection.lonlat2Geo(
                    localLatLngBounds.northeast.longitude,
                    localLatLngBounds.northeast.latitude,
                    winPt1);

            MapProjection.lonlat2Geo(
                    localLatLngBounds.southwest.longitude,
                    localLatLngBounds.southwest.latitude,
                    winPt2);

            int reqwidth = winPt1.x - winPt2.x;
            int reqheight = winPt2.y - winPt1.y;
            if ((reqwidth <= 0) && (reqheight <= 0)) {
                this.handler.obtainMessage(CAMERA_UPDATE_FINISH)
                        .sendToTarget();
                return;
            }
            float dz = 0.0F;
            int i10 = (winPt1.x + winPt2.x) / 2;
            int i11 = (winPt1.y + winPt2.y) / 2;
            IPoint newGeoCenter = new IPoint();
            getLatLng2Pixel(
                    (localLatLngBounds.northeast.latitude + localLatLngBounds.southwest.latitude) / 2.0D,
                    (localLatLngBounds.northeast.longitude + localLatLngBounds.southwest.longitude) / 2.0D,
                    newGeoCenter);

            int i12 = !this.rectMap.contains(newGeoCenter.x, newGeoCenter.y) ? 1 : 0;
            if (i12 == 0) {
                int i13 = width - padding * 2;
                int i14 = height - padding * 2;
                i13 = i13 <= 0 ? 1 : i13;
                i14 = i14 <= 0 ? 1 : i14;
                double d1 = this.mapProjection.getMapLenWithGeo(reqwidth);
                double d2 = this.mapProjection.getMapLenWithGeo(reqheight);
                double d3 = Math.log(this.mapProjection.getMapLenWithWin(i13) / d1) / Math.log(2.0D);
                double d4 = Math.log(this.mapProjection.getMapLenWithWin(i14) / d2) / Math.log(2.0D);
                double d5 = Math.min(d3, d4);
                dz = Util.checkZoomLevel((int) (d5 + zoom)) - zoom;

                int dx = i10 - geoCenter.x;
                int dy = i11 - geoCenter.y;
                if ((dx == 0) && (dy == 0)
                        && (dz == 0.0F)) {
                    this.handler
                            .obtainMessage(CAMERA_UPDATE_FINISH)
                            .sendToTarget();
                    return;
                }
                this.mCameraAnimator
                        .setInterpolator(new DecelerateInterpolator());
                this.mCameraAnimator.startChangeCamera(
                        geoCenter.x, geoCenter.y,
                        zoom, bearing, tilt, dx, dy, dz, dbearing, dtilt,
                        durationMs);
            } else {// change bearing and tilt first
                final long duration = durationMs;
                final MapController.CancelableCallback localCancelableCallback = this.mCallback;
                this.mCallback = new MapController.CancelableCallback() {
                    public void onFinish() {
                        try {
//												AMapDelegateImp.this.getPixel2LatLng(com.leador.api.mapcore.CameraUpdateFactoryDelegateDecode
//                                                                   .a(localLatLngBounds, i1, i2, i7), l1, this.f);//????
                            animateCameraWithDurationAndCallback(CameraUpdateFactoryDelegate
                                    .newLatLngBoundsWithSize(localLatLngBounds, width, height, padding), duration, localCancelableCallback);
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }

                    public void onCancel() {
                        // if (this.f != null) {
                        // this.f.onCancel();
                        // } //????
                        if (localCancelableCallback != null)
                            localCancelableCallback.onCancel();
                    }
                };
                int i15 = (i10 + geoCenter.x) / 2
                        - geoCenter.x;
                int i16 = (i11 + geoCenter.y) / 2
                        - geoCenter.y;
                int i17 = (int) Util.a(getMapWidth() / 2.0F, getMapHeight() / 2.0F, Math.abs(i10 - geoCenter.x), Math.abs(i11 - geoCenter.y));
                dz = i17 == 0 ? 0.0F : i17 - zoom;
                dz = dz >= 0.0F ? 0.0F : dz;
                this.isAnimationStep = true;
                this.mCameraAnimator.startChangeCamera(geoCenter.x, geoCenter.y, zoom, bearing, tilt, i15, i16, dz, dbearing / 2.0F, dtilt / 2.0F, durationMs / 2L);
            }
        } else {
            cameraUpdateFactoryDelegate.isChangeFinished = true;
            this.mMapMessges.addMessage(cameraUpdateFactoryDelegate);
        }

        setRunLowFrame(false);
    }

    public void stopAnimation() // t
            throws RemoteException {
        if (!this.mCameraAnimator.isFinished()) {
            this.mCameraAnimator.forceFinished(true);
            cameraChangeFinish(true, null);
            if (this.mCallback != null) {
                this.mCallback.onCancel();
            }
            if ((this.mInfoview != null) && (this.popupOverlay != null)) {
                this.mInfoview.setVisibility(View.VISIBLE);
            }
            this.mCallback = null;
        }
        setRunLowFrame(false);
    }

    public IPolylineDelegateDecode addPolyline(
            PolylineOptions paramPolylineOptions) // ak
            throws RemoteException {
        if (paramPolylineOptions == null) {
            return null;
        }
        PolylineDelegateImpDecode polyline = new PolylineDelegateImpDecode(
                this.baseOverlayLayer);
        polyline.setColor(paramPolylineOptions.getColor());
        polyline.geodesic(paramPolylineOptions.isGeodesic());
        polyline.setDottedLine(paramPolylineOptions.isDottedLine());
        polyline.setPoints(paramPolylineOptions.getPoints());
        polyline.setVisible(paramPolylineOptions.isVisible());
        polyline.setWidth(paramPolylineOptions.getWidth());
        polyline.setZIndex(paramPolylineOptions.getZIndex());
        polyline.setUseTexture(paramPolylineOptions.isUseTexture());
        if (paramPolylineOptions.getColorValues() != null) {
            polyline.e(paramPolylineOptions.getColorValues());
            polyline.e(paramPolylineOptions.isUseGradient());
        }
        if (paramPolylineOptions.getCustomTexture() != null) {
            polyline.a(paramPolylineOptions.getCustomTexture());
        }
        if (paramPolylineOptions.getCustomTextureList() != null) {
            polyline.setTextureList(paramPolylineOptions.getCustomTextureList());
            polyline.setTextureIndex(paramPolylineOptions.getCustomTextureIndex());
        }
        this.baseOverlayLayer.add(polyline);
        setRunLowFrame(false);
        return polyline;
    }

    public IPolylineDelegateDecode addGeojsonPolyline(PolylineOptions paramPolylineOptions) // ak
            throws RemoteException {
        if (paramPolylineOptions == null) {
            return null;
        }

        PolylineDelegateImpDecode polyline = new PolylineDelegateImpDecode(
                this.geojsonOverlayManager);
        polyline.setColor(paramPolylineOptions.getColor());
        polyline.geodesic(paramPolylineOptions.isGeodesic());
        polyline.setDottedLine(paramPolylineOptions.isDottedLine());
        polyline.setPoints(paramPolylineOptions.getPoints());
        polyline.setVisible(paramPolylineOptions.isVisible());
        polyline.setWidth(paramPolylineOptions.getWidth());
        polyline.setZIndex(paramPolylineOptions.getZIndex());
        polyline.setUseTexture(paramPolylineOptions.isUseTexture());
        if (paramPolylineOptions.getColorValues() != null) {
            polyline.e(paramPolylineOptions.getColorValues());
            polyline.e(paramPolylineOptions.isUseGradient());
        }
        if (paramPolylineOptions.getCustomTexture() != null) {
            polyline.a(paramPolylineOptions.getCustomTexture());
        }
        if (paramPolylineOptions.getCustomTextureList() != null) {
            polyline.setTextureList(paramPolylineOptions.getCustomTextureList());
            polyline.setTextureIndex(paramPolylineOptions.getCustomTextureIndex());
        }
        this.geojsonOverlayManager.add(polyline);
        setRunLowFrame(false);
        return polyline;
    }

    public INavigateArrowDelegateDecode addNavigateArrow(
            NavigateArrowOptions paramNavigateArrowOptions) // a
            throws RemoteException {
        if (paramNavigateArrowOptions == null) {
            return null;
        }
        NavigateArrowDelegateImpDecode arrowDelegate = new NavigateArrowDelegateImpDecode(this);

        arrowDelegate.setTopColor(paramNavigateArrowOptions.getTopColor());
        arrowDelegate.setSideColor(paramNavigateArrowOptions.getSideColor());
        arrowDelegate.setPoints(paramNavigateArrowOptions.getPoints());
        arrowDelegate.setVisible(paramNavigateArrowOptions.isVisible());
        arrowDelegate.setWidth(paramNavigateArrowOptions.getWidth());
        arrowDelegate.setZIndex(paramNavigateArrowOptions.getZIndex());
        this.baseOverlayLayer.add(arrowDelegate);
        setRunLowFrame(false);
        return arrowDelegate;
    }

    public IPolygonDelegate addPolygon(PolygonOptions paramPolygonOptions) // a
            throws RemoteException {
        if (paramPolygonOptions == null) {
            return null;
        }
        PolygonDelegateImpDecode localbe = new PolygonDelegateImpDecode(this);
        localbe.setFillColor(paramPolygonOptions.getFillColor());
        localbe.a(paramPolygonOptions.getPoints());
        localbe.setVisible(paramPolygonOptions.isVisible());
        localbe.setStrokeWidth(paramPolygonOptions.getStrokeWidth());
        localbe.setZIndex(paramPolygonOptions.getZIndex());
        localbe.setStrokeColor(paramPolygonOptions.getStrokeColor());
        this.baseOverlayLayer.add(localbe);
        setRunLowFrame(false);
        return localbe;
    }

    public IPolygonDelegate addGeojsonPolygon(PolygonOptions paramPolygonOptions) // a
            throws RemoteException {
        if (paramPolygonOptions == null) {
            return null;
        }

        PolygonDelegateImpDecode localbe = new PolygonDelegateImpDecode(this);
        localbe.setFillColor(paramPolygonOptions.getFillColor());
        localbe.a(paramPolygonOptions.getPoints());
        localbe.setVisible(paramPolygonOptions.isVisible());
        localbe.setStrokeWidth(paramPolygonOptions.getStrokeWidth());
        localbe.setZIndex(paramPolygonOptions.getZIndex());
        localbe.setStrokeColor(paramPolygonOptions.getStrokeColor());
        this.geojsonOverlayManager.add(localbe);
        setRunLowFrame(false);
        return localbe;
    }

    public ICircleDelegate addCircle(CircleOptions paramCircleOptions) // a
            throws RemoteException {
        if (paramCircleOptions == null) {
            return null;
        }
        CircleDelegateImp localp = new CircleDelegateImp(this);
        localp.setFillColor(paramCircleOptions.getFillColor());
        localp.setCenter(paramCircleOptions.getCenter());
        localp.setVisible(paramCircleOptions.isVisible());
        localp.setStrokeWidth(paramCircleOptions.getStrokeWidth());
        localp.setZIndex(paramCircleOptions.getZIndex());
        localp.setStrokeColor(paramCircleOptions.getStrokeColor());
        localp.setRadius(paramCircleOptions.getRadius());
        this.baseOverlayLayer.add(localp);
        setRunLowFrame(false);
        return localp;
    }

    public IArcDelegateDecode addArc(ArcOptions paramArcOptions) // a
            throws RemoteException {
        if (paramArcOptions == null) {
            return null;
        }
        ArcDelegateImp localm = new ArcDelegateImp(this);
        localm.setStrokeColor(paramArcOptions.getStrokeColor());
        localm.setStart(paramArcOptions.getStart());
        localm.setPassed(paramArcOptions.getPassed());
        localm.setEnd(paramArcOptions.getEnd());
        localm.setVisible(paramArcOptions.isVisible());
        localm.setStrokeWidth(paramArcOptions.getStrokeWidth());
        localm.setZIndex(paramArcOptions.getZIndex());
        this.baseOverlayLayer.add(localm);
        setRunLowFrame(false);
        return localm;
    }

    public IGroundOverlayDelegateDecode addGroundOverlay(
            GroundOverlayOptions options) // a
            throws RemoteException {
        if (options == null) {
            return null;
        }
        GroundOverlayDelegateImpDecode overlay = new GroundOverlayDelegateImpDecode(
                this);

        overlay.setAnchor(options.getAnchorU(),
                options.getAnchorV());
        overlay.setDimensions(options.getWidth(),
                options.getHeight());
        overlay.setImage(options.getImage());
        overlay.setPosition(options.getLocation());
        overlay.setPositionFromBounds(options.getBounds());
        overlay.setBearing(options.getBearing());
        overlay.setTransparency(options.getTransparency());
        overlay.setVisible(options.isVisible());
        overlay.setZIndex(options.getZIndex());
        this.baseOverlayLayer.add(overlay);
        setRunLowFrame(false);
        return overlay;
    }

    public Marker addMarker(final MarkerOptions options) // a
            throws RemoteException {
        if (options == null) {
            return null;
        }

        if (options.isAddByAnimation() && options.getPosition() != null) {
            final Handler handler = new Handler();
            final long start = SystemClock.uptimeMillis();
            final LatLng latLng = options.getPosition();

            IPoint localIPoint = new IPoint();
            getLatLng2Pixel(latLng.latitude, latLng.longitude, localIPoint);
            Point startPoint = new Point(localIPoint.x, localIPoint.y);
            startPoint.offset(0, -localIPoint.y);

            DPoint localDPoint = new DPoint();
            getPixel2LatLng(startPoint.x, startPoint.y, localDPoint);
            final LatLng startLatLng = new LatLng(localDPoint.y, localDPoint.x);

            options.position(startLatLng);
            MarkerDelegateImp localaz = new MarkerDelegateImp(
                    options, this.mOverlaysImageView);
            this.mOverlaysImageView.addMarker(localaz);
            setRunLowFrame(false);
            final Marker marker = new Marker(localaz);

            // 持续时间
            final long duration = 500;
            final Interpolator interpolator = new LinearInterpolator();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    long elapsed = SystemClock.uptimeMillis() - start;
                    float t = interpolator.getInterpolation((float) elapsed
                            / duration);
                    double lng = t * latLng.longitude + (1 - t)
                            * startLatLng.longitude;
                    double lat = t * latLng.latitude + (1 - t)
                            * startLatLng.latitude;
                    if (t < 1.0) {
                        marker.setPosition(new LatLng(lat, lng));
                        handler.postDelayed(this, 16);
                    } else {
                        // 保证位置是传入的值
                        marker.setPosition(latLng);
                    }
                }
            });
            return marker;
        } else {
            MarkerDelegateImp localaz = new MarkerDelegateImp(
                    options, this.mOverlaysImageView);
            this.mOverlaysImageView.addMarker(localaz);
            setRunLowFrame(false);
            final Marker marker = new Marker(localaz);
            return marker;
        }
    }

    public IMarkerDelegate addGeojsonMarker(final MarkerOptions options) // a
            throws RemoteException {
        if (options == null) {
            return null;
        }

        final MarkerDelegateImp localaz;
        if (options.isAddByAnimation() && options.getPosition() != null) {
            final Handler handler = new Handler();
            final long start = SystemClock.uptimeMillis();
            final LatLng latLng = options.getPosition();

            IPoint localIPoint = new IPoint();
            getLatLng2Pixel(latLng.latitude, latLng.longitude, localIPoint);
            Point startPoint = new Point(localIPoint.x, localIPoint.y);
            startPoint.offset(0, -localIPoint.y);

            DPoint localDPoint = new DPoint();
            getPixel2LatLng(startPoint.x, startPoint.y, localDPoint);
            final LatLng startLatLng = new LatLng(localDPoint.y, localDPoint.x);

            options.position(startLatLng);
            localaz = new MarkerDelegateImp(options, this.geojsonOverlayManager);
            this.geojsonOverlayManager.addMarker(localaz);
            setRunLowFrame(false);

            // 持续时间
            final long duration = 500;
            final Interpolator interpolator = new LinearInterpolator();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    long elapsed = SystemClock.uptimeMillis() - start;
                    float t = interpolator.getInterpolation((float) elapsed
                            / duration);
                    double lng = t * latLng.longitude + (1 - t)
                            * startLatLng.longitude;
                    double lat = t * latLng.latitude + (1 - t)
                            * startLatLng.latitude;
                    if (t < 1.0) {
                        localaz.setPosition(new LatLng(lat, lng));
                        handler.postDelayed(this, 16);
                    } else {
                        // 保证位置是传入的值
                        localaz.setPosition(latLng);
                    }
                }
            });
            return localaz;
        } else {
            localaz = new MarkerDelegateImp(options, this.geojsonOverlayManager);
            this.geojsonOverlayManager.addMarker(localaz);
            setRunLowFrame(false);
//            final Marker marker = new Marker(localaz);
            return localaz;
        }
    }

    public Text addText(TextOptions paramTextOptions) // a
            throws RemoteException {
        if (paramTextOptions == null) {
            return null;
        }
        TextDelegateImp localbk = new TextDelegateImp(
                paramTextOptions, this.mOverlaysImageView);

        this.mOverlaysImageView.addMarker(localbk);
        setRunLowFrame(false);
        return new Text(localbk);
    }

    public ArrayList<Marker> addMarkers(
            ArrayList<MarkerOptions> optionses, boolean moveToCenter) // a
            throws RemoteException {
        if ((optionses == null) || (optionses.size() == 0)) {
            return null;
        }
        ArrayList<Marker> markerList = new ArrayList<Marker>();
        try {
            MarkerOptions options = null;
            if ((optionses.size() == 1)
                    && ((options = (MarkerOptions) optionses
                    .get(0)) != null)) {
                markerList.add(addMarker(options));
                if ((moveToCenter)
                        && (options.getPosition() != null)) {
                    moveCamera(CameraUpdateFactoryDelegate.a(options.getPosition(), 18.0F));
                }
            } else {
                final LatLngBounds.Builder builder = LatLngBounds.builder();
                int i1 = 0;
                for (int i2 = 0; i2 < optionses.size(); i2++) {
                    options = (MarkerOptions) optionses.get(i2);
                    if (optionses.get(i2) != null) {
                        markerList.add(addMarker(options));
                        if (options.getPosition() != null) {
                            builder.include(options
                                    .getPosition());
                            i1++;
                        }
                    }
                }
                if ((moveToCenter) && (i1 > 0)) {
                    if (this.aC) {
                        this.handler.postDelayed(new Runnable() {
                            public void run() {
                                try {
                                    MapDelegateImp.this
                                            .moveCamera(CameraUpdateFactoryDelegate
                                                    .newLatLngBounds(builder.build(), 50));
                                } catch (Throwable localThrowable) {
                                }
                            }
                        }, 50L);
                    } else {
                        this.aw = CameraUpdateFactoryDelegate
                                .newLatLngBounds(builder.build(), 50);
                    }
                }
            }
            return markerList;
        } catch (Throwable localThrowable) {
            SDKLogHandler.exception(localThrowable, "MapDelegateImp", "addMarkers");

            localThrowable.printStackTrace();
        }
        return markerList;
    }

    public TileOverlay addTileOverlay(TileOverlayOptions tileOverlayOptions)
            throws RemoteException {
        if ((tileOverlayOptions == null)
                || (tileOverlayOptions.getTileProvider() == null)) {
            return null;
        }
        TileOverlayDelegateImp localbm = new TileOverlayDelegateImp(
                tileOverlayOptions, this.mTileOverlayView);

        this.mTileOverlayView.addTileOverlay(localbm);
        setRunLowFrame(false);
        return new TileOverlay(localbm);
    }

    public Handler getAu() {
        return au;
    }

    Runnable clearRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                clear(false);
            } catch (Throwable localThrowable) {
                SDKLogHandler.exception(localThrowable, "MapDelegateImp", "clear");

                Log.d("leadorApi", "MapDelegateImp clear erro"
                        + localThrowable.getMessage());
                localThrowable.printStackTrace();
            }
        }
    };

    Runnable hiddenInfoWindowRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                hiddenInfoWindowShown();
            } catch (Throwable localThrowable) {
                SDKLogHandler.exception(localThrowable, "MapDelegateImp", "hiddenInfoWindowShown");

                Log.d("leadorApi", "MapDelegateImp hiddenInfoWindowShown erro"
                        + localThrowable.getMessage());
                localThrowable.printStackTrace();
            }
        }
    };

    public void clear() throws RemoteException {
//        this.au.removeCallbacks(this.clearRunnable);
//        this.au.post(this.clearRunnable);
        try {
            clear(false);
        } catch (Throwable localThrowable) {
            SDKLogHandler.exception(localThrowable, "MapDelegateImp", "clear");

            Log.d("leadorApi", "MapDelegateImp clear erro"
                    + localThrowable.getMessage());
            localThrowable.printStackTrace();
        }
    }

    public void clear(boolean paramBoolean) // g
            throws RemoteException {
        try {
//            hiddenInfoWindowShown();
            this.au.removeCallbacks(this.hiddenInfoWindowRunnable);
            this.au.post(this.hiddenInfoWindowRunnable);
            String str1 = null;
            String str2 = null;
            if (this.myLocationOverlay != null) {
                if (paramBoolean) {
                    str1 = this.myLocationOverlay.getId();
                    str2 = this.myLocationOverlay.e();
                } else {
                    this.myLocationOverlay.f();
                }
            }
            this.baseOverlayLayer.clearOverlay(str2);
            this.mTileOverlayView.clear();
            this.mOverlaysImageView.clear(str1);
            if (this.geojsonOverlayManager != null) {
                this.geojsonOverlayManager.clear(null);
                this.geojsonOverlayManager.clearOverlay(null);
            }
            setRunLowFrame(false);
        } catch (Throwable localThrowable) {
            SDKLogHandler.exception(localThrowable, "MapDelegateImp", "clear");

            Log.d("leadorApi", "MapDelegateImp clear erro"
                    + localThrowable.getMessage());
            localThrowable.printStackTrace();
        }
    }

    public int getMapType() // v
            throws RemoteException {
        return this.mapType;
    }

    public void setMapType(int mapType) // b
            throws RemoteException {
        try {
            this.mapType = mapType;
            if (mapType == MapController.MAP_TYPE_NORMAL) {
                setMapType(GLMapResManager.MapViewMode.NORAML,
                        GLMapResManager.MapViewTime.DAY);
            } else if (mapType == MapController.MAP_TYPE_SATELLITE) {
                if (mapCore.getAuthStatus()) {
                    setMapType(GLMapResManager.MapViewMode.SATELLITE,
                            GLMapResManager.MapViewTime.DAY);
                } else {
                    throw new UnistrongException(UnistrongException.ERROR_LOCAL_AUTH_FAILURE);
                }
            } else if (mapType == MapController.MAP_TYPE_NIGHT) {
                setMapType(GLMapResManager.MapViewMode.NORAML,
                        GLMapResManager.MapViewTime.NIGHT,
                        GLMapResManager.MapViewModeState.NORMAL);
            } else if (mapType == MapController.MAP_TYPE_NAVI) {
                setMapType(GLMapResManager.MapViewMode.NORAML,
                        GLMapResManager.MapViewTime.DAY,
                        GLMapResManager.MapViewModeState.NAVI_CAR);
            } else if (mapType == MapController.MAP_TYPE_STREETVIEW) {
                setMapType(GLMapResManager.MapViewMode.STREETVIEW,
                        GLMapResManager.MapViewTime.DAY);
            } else if (mapType == MapController.MAP_TYPE_EAGLE_EYE) {
                setMapType(GLMapResManager.MapViewMode.EAGLE_EYE,
                        GLMapResManager.MapViewTime.DAY,
                        GLMapResManager.MapViewModeState.NORMAL);
                this.getUiSettings().setZoomControlsEnabled(false);
                this.getUiSettings().setZoomGesturesEnabled(false);
                this.getUiSettings().setTiltGesturesEnabled(false);
                if (mOverlaylay != null) {
                    mOverlaylay.removeView(mWaterMarkerView);
                }
                showMapText(false);
            } else {
                this.mapType = MapController.MAP_TYPE_NORMAL;
            }
            setRunLowFrame(false);
        } catch (Throwable throwable) {
            SDKLogHandler.exception(throwable, "MapDelegateImp", "setMaptype");
            throwable.printStackTrace();
        }
    }

    public void setMapType(GLMapResManager.MapViewMode mapViewMode,
                           GLMapResManager.MapViewTime mapViewTime) // a
    {
        setMapType(mapViewMode, mapViewTime,
                GLMapResManager.MapViewModeState.NORMAL);
    }

    public void setMapType(GLMapResManager.MapViewMode mapViewMode,
                           GLMapResManager.MapViewTime mapViewTime,
                           GLMapResManager.MapViewModeState mapViewModeState) // a
    {
        if ((this.mapViewTime == mapViewTime)
                && (this.mapViewMode == mapViewMode)
                && (this.mapViewModeState == mapViewModeState)) {
            return;
        }
        if (!this.aD) {
            this.mapViewTime = mapViewTime;
            this.mapViewMode = mapViewMode;
            this.mapViewModeState = mapViewModeState;
        } else {
            final GLMapResManager.MapViewTime mapViewTime1 = mapViewTime;
            final GLMapResManager.MapViewMode mapViewMode1 = mapViewMode;
            final GLMapResManager.MapViewModeState localMapViewModeState1 = mapViewModeState;
            final GLMapResManager.MapViewTime mapViewTime2 = this.mapViewTime;
            final GLMapResManager.MapViewMode mapViewMode2 = this.mapViewMode;
            final GLMapResManager.MapViewModeState localMapViewModeState2 = this.mapViewModeState;
            if ((this.isSetInternaltexture) && (this.initProjOver)) //上一句翻译
            {
                a(new Runnable() {
                    public void run() {
                        String str1 = glMapResManager.getStyleName();
                        String str2 = glMapResManager.getIconName();
                        MapDelegateImp.this.mapViewTime = mapViewTime1;
                        MapDelegateImp.this.mapViewMode = mapViewMode1;
                        MapDelegateImp.this.mapViewModeState = localMapViewModeState1;
                        String str3 = glMapResManager.getStyleName();
                        String str4 = glMapResManager.getIconName();
                        if ((MapDelegateImp.this.mapViewMode == GLMapResManager.MapViewMode.SATELLITE) ||
                                (MapDelegateImp.this.mapViewTime == GLMapResManager.MapViewTime.NIGHT) ||
                                (mapViewTime2 == GLMapResManager.MapViewTime.NIGHT) || (mapViewMode2 == GLMapResManager.MapViewMode.SATELLITE)) {
                            handler.post(new Runnable() {
                                public void run() {
                                    ai();
                                }
                            });
                        }
                        mapCore.setParameter(2501, 0, 0, 0, 0);
                        if (!str1.equals(str3)) {
                            glMapResManager.setStyleData();
                        }
                        if ((MapDelegateImp.this.mapViewMode == GLMapResManager.MapViewMode.SATELLITE) ||
                                (mapViewMode2 == GLMapResManager.MapViewMode.SATELLITE)) {
                            mapCore.setParameter(WXM_PARAMETERNAME_SATELLITE,
                                    MapDelegateImp.this.mapViewMode == GLMapResManager.MapViewMode.SATELLITE ? 1 : 0, 0, 0, 0);
                        }
                        if ((MapDelegateImp.this.mapViewMode == GLMapResManager.MapViewMode.STREETVIEW) ||
                                (mapViewMode2 == GLMapResManager.MapViewMode.STREETVIEW)) {
                            Log.e("mapDelegateimp", "____STREETVIEW______");
                            mapCore.setParameter(WXM_PARAMETERNAME_PANORAMA,
                                    MapDelegateImp.this.mapViewMode == GLMapResManager.MapViewMode.STREETVIEW ? 1 : 0, 0, 0, 0);
                        }
                        if ((MapDelegateImp.this.mapViewTime == GLMapResManager.MapViewTime.NIGHT) ||
                                (mapViewTime2 == GLMapResManager.MapViewTime.NIGHT)) {
                            mapCore.setParameter(2401,
                                    MapDelegateImp.this.mapViewTime == GLMapResManager.MapViewTime.NIGHT ? 1 : 0, 0, 0, 0);

                            glMapResManager.setRoadGuideTexture(true);
                            glMapResManager.setBkTexture(true);
                        }
                        if (!str2.equals(str4)) {
                            glMapResManager.setIconsData(true);
                        }
                        glMapResManager.setTrafficTexture(true);
//                        if (null != MapDelegateImp.this.mapViewModeState) {
//                            mapCore.setParameter(2013, MapDelegateImp.this.mapViewMode.ordinal(),
//                                    MapDelegateImp.this.mapViewTime.ordinal(),
//                                    MapDelegateImp.this.mapViewModeState.ordinal(), 0);
//                        }
                        if (localMapViewModeState1 == GLMapResManager.MapViewModeState.NORMAL &&
                                localMapViewModeState2 == GLMapResManager.MapViewModeState.NORMAL) {//底图未发生变化，不刷新
                            mapCore.setParameter(2501, 1, 0, 0, 0);
                        } else {
                            mapCore.setParameter(2501, 1, 1, 0, 0);
                        }
                    }
                });
            } else {
                this.bq.d = mapViewMode;
                this.bq.e = mapViewTime;
                this.bq.b = true;
            }
        }
    }

    public boolean isTrafficEnabled() // w
            throws RemoteException {
        return this.trafficEnabled;
    }

    private boolean trafficEnabled = false; // aU

    public void setTrafficEnabled(boolean paramBoolean) // h
            throws RemoteException {
        this.trafficEnabled = paramBoolean;
        this.mMapMessges.addMapMessage(new MapMessageDecode(
                MapMessageDecode.TRAFFICSET).setEnabled(paramBoolean));
    }

    public void showMapText(final boolean paramBoolean) throws RemoteException {
        this.isShowMapText = paramBoolean;
        a(new Runnable() {
            public void run() {
                if (mapCore != null) {
                    mapCore.setParameter(1024, paramBoolean ? 1 : 0, 0, 0, 0);
                }
            }
        });
    }

    public boolean isMyLocationEnabled() throws RemoteException {
        return this.locationEnabled;
    }


    public Location getMyLocation() // y
            throws RemoteException {
        if (this.locationSource != null) {
            return this.locationChangedListener.location;
        }
        return null;
    }

    public void setLocationSource(LocationSource locationSource) // public void
        // a(LocationSource
        // paramLocationSource)
            throws RemoteException {
        try {
            this.locationSource = locationSource;
            if (locationSource != null) {
                this.mLocationView.showSelect(true);
            } else {
                this.mLocationView.showSelect(false);
            }
        } catch (Throwable localThrowable) {
            SDKLogHandler.exception(localThrowable, "MapDelegateImp",
                    "setLocationSource");

            localThrowable.printStackTrace();
        }
    }

    public IUiSettingsDelegate getUiSettings() // z
            throws RemoteException {
        return this.uiSettings;
    }

    public IProjectionDelegate getProjection() // A
            throws RemoteException {
        return this.projectionDelegate;
    }

    public void setOnCameraChangeListener(MapController.OnCameraChangeListener listener) // a
            throws RemoteException {
        this.cameraChangeListener = listener;
    }

    void runOnCameraChangeListener(CameraPosition cp) // a
    {
        Message localMessage = new Message();
        localMessage.what = ON_CAMERA_CHANGE;
        localMessage.obj = cp;
        this.handler.sendMessage(localMessage);
    }

    public MapController.OnCameraChangeListener getOnCameraChangeListener() // B
            throws RemoteException {
        return this.cameraChangeListener;
    }

    public void setOnMapLevelChangeListener(MapController.OnMapLevelChangeListener listener) {
        this.mapLevelChangeListener = listener;
    }

    public MapController.OnMapLevelChangeListener getOnMapLevelChangeListener() throws RemoteException {
        return this.mapLevelChangeListener;
    }

    public void setOnMapClickListener(MapController.OnMapClickListener paramOnMapClickListener) // a -->
        // setOnMapClickListener
            throws RemoteException {
        this.mapClickListener = paramOnMapClickListener;
    }

    public void setOnMapTouchListener(MapController.OnMapTouchListener paramOnMapTouchListener) // a -->
        // setOnMapTouchListener
            throws RemoteException {
        this.mMapTouchListener = paramOnMapTouchListener;
    }

    public void setOnPOIClickListener(MapController.OnPOIClickListener poiClickListener)// a
            throws RemoteException {
        this.mPOIClickListener = poiClickListener;
    }

    public void setOnMapLongClickListener(MapController.OnMapLongClickListener mapLongClickListener) // a
            throws RemoteException {
        this.mapLongClickListener = mapLongClickListener;
    }

    public void setOnMarkerClickListener(MapController.OnMarkerClickListener markerClickListener) // a
            throws RemoteException {
        this.mMarkerClickListener = markerClickListener;
    }

    public void setOnPolylineClickListener(MapController.OnPolylineClickListener polylineClickListener)// a
            throws RemoteException {
        this.mPolylineClickListener = polylineClickListener;
    }

    public void setOnMarkerDragListener(MapController.OnMarkerDragListener paramOnMarkerDragListener) // a
            throws RemoteException {
        this.mMarkDragListener = paramOnMarkerDragListener;
    }

    public void setOnMapLoadedListener(MapController.OnMapLoadedListener paramOnMapLoadedListener) // a
            throws RemoteException {
        this.mMapLoadedListener = paramOnMapLoadedListener;
    }

    public void setOnInfoWindowClickListener(MapController.OnInfoWindowClickListener infoWindowClickListener) // a
            throws RemoteException {
        this.mInfoWindowClickListener = infoWindowClickListener;
    }

    public void setInfoWindowAdapter(MapController.InfoWindowAdapter paramInfoWindowAdapter) // a
            throws RemoteException {
        if (paramInfoWindowAdapter == null) {
            this.mInfoAdapter = this.mInfoAdapterSrc;
        } else {
            this.mInfoAdapter = paramInfoWindowAdapter;
        }
    }

    public View getView() throws RemoteException {
        return this.mOverlaylay;
    }

    public float checkZoomLevel(float paramFloat) // b
            throws RemoteException {
        return Util.checkZoomLevel(paramFloat);
    }

    public float getMapLenWithWin(int paramInt) // c
    {
        if (this.initProjOver) {
            return this.mapProjection.getMapLenWithWin(paramInt);
        }
        return 0.0F;
    }

    public void getPixel2LatLng(int xPixel, int yPixel, DPoint paramDPoint) // a
    {
        getPixel2LatLng(this.mapProjection, xPixel, yPixel, paramDPoint);
    }

    private void getPixel2LatLng(MapProjection paramMapProjection, int xPixel,
                                 int yPixel, DPoint paramDPoint) // a
    {
        if (this.initProjOver) {
            FPoint localFPoint = new FPoint();
            paramMapProjection.win2Map(xPixel, yPixel, localFPoint);
            IPoint localIPoint = new IPoint();
            paramMapProjection.map2Geo(localFPoint.x, localFPoint.y,
                    localIPoint);
            MapProjection.geo2LonLat(localIPoint.x, localIPoint.y, paramDPoint);
        }
    }

    public void getPixel2Geo(int xPixel, int yPixel, IPoint result) // a
    {
        if (this.initProjOver) {
            FPoint fPoint = new FPoint();
            this.mapProjection.win2Map(xPixel, yPixel, fPoint);
            this.mapProjection.map2Geo(fPoint.x, fPoint.y, result);
        }
    }

    public void b(int paramInt1, int paramInt2, IPoint paramIPoint) // b
    {
        if (this.initProjOver) {
            FPoint localFPoint = new FPoint();
            this.mapProjection.geo2Map(paramInt1, paramInt2, localFPoint);
            this.mapProjection.map2Win(localFPoint.x, localFPoint.y,
                    paramIPoint);
        }
    }

    public void getLatLng2Map(double paramDouble1, double paramDouble2,
                              FPoint paramFPoint) // a
    {
        if (this.initProjOver) {
            IPoint localIPoint = new IPoint();
            MapProjection.lonlat2Geo(paramDouble2, paramDouble1, localIPoint);
            this.mapProjection.geo2Map(localIPoint.x, localIPoint.y,
                    paramFPoint);
        }
    }

    public void latlon2Geo(double latitude, double longitude,
                           IPoint paramIPoint)// a
    {
        MapProjection.lonlat2Geo(longitude, latitude, paramIPoint);
    }

    public void win2Map(int paramInt1, int paramInt2, FPoint fPoint)// a
    {
        if (this.initProjOver) {
            this.mapProjection.win2Map(paramInt1, paramInt2, fPoint);
        }
    }

    public void geo2Map(int paramInt1, int paramInt2, FPoint paramFPoint)// b
    {
        if (this.initProjOver) {
            this.mapProjection.geo2Map(paramInt2, paramInt1, paramFPoint);
        }
    }

    public void a(float paramFloat1, float paramFloat2, IPoint paramIPoint) // a
    {
        if (this.initProjOver) {
            this.mapProjection.map2Geo(paramFloat1, paramFloat2, paramIPoint);
        }
    }

    public void geo2Latlng(int paramInt1, int paramInt2, DPoint paramDPoint) // b
    {
        MapProjection.geo2LonLat(paramInt1, paramInt2, paramDPoint);
    }

    public void getLatLng2Pixel(double latitude, double longitude,
                                IPoint paramIPoint) // b
    {
        if (this.initProjOver) {
            IPoint localIPoint = new IPoint();
            FPoint localFPoint = new FPoint();
            MapProjection.lonlat2Geo(longitude, latitude, localIPoint);
            this.mapProjection.geo2Map(localIPoint.x, localIPoint.y,
                    localFPoint);
            this.mapProjection.map2Win(localFPoint.x, localFPoint.y,
                    paramIPoint);
        }
    }

    private LatLng getLatLngCenter() // ag
    {
        if (this.initProjOver) {
            DPoint localDPoint = new DPoint();
            IPoint localIPoint = new IPoint();
            this.mapProjection.getGeoCenter(localIPoint);
            MapProjection.geo2LonLat(localIPoint.x, localIPoint.y, localDPoint);
            LatLng localLatLng = new LatLng(localDPoint.y, localDPoint.x, false);
            return localLatLng;
        }
        return null;
    }

    public CameraPosition getCameraPositionPrj_(boolean paramBoolean) // l
    {
        if (this.initProjOver) {
            LatLng localLatLng = null;
            if (paramBoolean) {
                DPoint localDPoint = new DPoint();
                getPixel2LatLng(this.xPixel, this.yPixel, localDPoint);
                localLatLng = new LatLng(localDPoint.y, localDPoint.x, false);
            } else {
                localLatLng = getLatLngCenter();
            }
            return CameraPosition.builder().target(localLatLng)
                    .bearing(this.mapProjection.getMapAngle())
                    .tilt(this.mapProjection.getCameraHeaderAngle())
                    .zoom(this.mapProjection.getMapZoomer()).build();
        }
        return null;
    }

    //是否在双指上下滑 调整仰角
    private boolean isDoubleScolling = false;//aV
    //是否正在缩放
    private boolean isScaleTouching = false; //aW
    private boolean isMarkerDraging = false;//aX
    private Marker hitMarker = null; //aY
    private IMarkerDelegate markerDelegate = null; //aZ
    private boolean mIsScrolling = false; //ba
    private boolean mIsRotating = false;//bb
    private boolean mIsDoubleTapping = false;//bc

    private void EndTouchEvent()// ah
    {
        if (this.isLongPress) {
            this.isLongPress = false;
        }
        CameraUpdateFactoryDelegate localo;
        if (this.mIsScrolling) {
            this.mIsScrolling = false;

            localo = CameraUpdateFactoryDelegate
                    .newInstance();
            localo.isChangeFinished = true;
            this.mMapMessges.addMessage(localo);
        }
        if (this.isDoubleScolling) {
            this.isDoubleScolling = false;

            localo = CameraUpdateFactoryDelegate
                    .newInstance();
            localo.isChangeFinished = true;
            this.mMapMessges.addMessage(localo);
        }
        this.isScaleTouching = false;
        this.isMarkerDraging = false;
        if ((this.mMarkDragListener != null) && (this.hitMarker != null)) {
            this.mMarkDragListener.onMarkerDragEnd(this.hitMarker);
            this.hitMarker = null;
        }
    }

    private void onDragMarker(MotionEvent paramMotionEvent) // b
            throws RemoteException {
        if ((this.isMarkerDraging) && (this.hitMarker != null)) {
            int i1 = (int) paramMotionEvent.getX();
            int i2 = (int) (paramMotionEvent.getY() - 60.0F);
            LatLng localLatLng1 = this.markerDelegate.g();
            LatLng localLatLng2 = this.markerDelegate.getPosition();
            DPoint localDPoint = new DPoint();
            getPixel2LatLng(i1, i2, localDPoint);
            LatLng localLatLng3 = new LatLng(localLatLng2.latitude
                    + localDPoint.y - localLatLng1.latitude,
                    localLatLng2.longitude + localDPoint.x
                            - localLatLng1.longitude);

            this.hitMarker.setPosition(localLatLng3);
            this.mMarkDragListener.onMarkerDrag(this.hitMarker);
        }
    }

    //该参数用来防止 双指双击触发 单指双击放大
    private int pointerCounter = 0; //bd
    //是否长按
    private boolean isLongPress = false; //be

    public boolean onTouchEvent(MotionEvent paramMotionEvent)  // a
    {
        if (!this.isTRANSPARENT) {
            return false;
        }
        setRunLowFrame(false);
        if (paramMotionEvent.getAction() == 261) {
            this.pointerCounter = paramMotionEvent.getPointerCount();
        }
        this.gestureDetector.onTouchEvent(paramMotionEvent);
        this.multiTouchSupport.onTouchEvent(paramMotionEvent);
        this.scaleGestureDetector.onTouchEvent(paramMotionEvent);
        boolean isEnable = false;
        try {
            isEnable = !this.aF && (getCameraPosition().tilt == 0);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if (!isEnable) this.mRotateDetector.onTouchEvent(paramMotionEvent);
        if (paramMotionEvent.getAction() == MotionEvent.ACTION_MOVE) {
            try {
                onDragMarker(paramMotionEvent);
            } catch (RemoteException localRemoteException) {
                SDKLogHandler.exception(localRemoteException, "MapDelegateImp",
                        "onDragMarker");

                localRemoteException.printStackTrace();
            }
        }
        if (paramMotionEvent.getAction() == MotionEvent.ACTION_UP) {
            EndTouchEvent();
        }
        setRunLowFrame(false);
        if (this.mMapTouchListener != null) {
            this.bm.removeMessages(1);
            Message localMessage = this.bm.obtainMessage();
            localMessage.what = 1;
            localMessage.obj = MotionEvent.obtain(paramMotionEvent);
            localMessage.sendToTarget();
        }
        return true;
    }

    private class MyScaleGestureDetector // g  check - 1
            implements ScaleGestureDetector.OnScaleGestureListener {
        //开始旋转时候的级别
        private float scaleBeginZoom = 0; // c
        private IPoint d = new IPoint(); // d


        CameraUpdateFactoryDelegate comeraUpdateFactoryDelegate = CameraUpdateFactoryDelegate.newInstance();

        public boolean onScale(ScaleGestureDetector detector) {
            if (isDoubleScolling) {
                return false;
            }
            float scale = detector.getScaleFactor();
            if ((isScaleTouching) || (scale > 1.08D) || (scale < 0.92D)) {
                isScaleTouching = true;
                float dz = (float) (Math.log(scale) / MATH_LOG_2);
                this.comeraUpdateFactoryDelegate.zoom = Util.checkZoomLevel(this.scaleBeginZoom + dz);
                MapDelegateImp.this.mMapMessges.addMessage(this.comeraUpdateFactoryDelegate);
            }
            return false;
        }

        public boolean onScaleBegin(ScaleGestureDetector paramScaleGestureDetector) {
            try {
                if ((!uiSettings.isZoomGesturesEnabled()) || (pointerCounter < 2)) {
                    return false;
                }
            } catch (RemoteException localRemoteException) {
                SDKLogHandler.exception(localRemoteException, "MapDelegateImp", "onScaleBegin");
                localRemoteException.printStackTrace();
            }
            pointerCounter = 2;
            if (isDoubleScolling) {// 如果在仰角则不缩放
                return false;
            }

            if (bj) {
                comeraUpdateFactoryDelegate.n = bj;
                comeraUpdateFactoryDelegate.nowType = CameraUpdateFactoryDelegate.Type.changeGeoCenterZoom;
                getPixel2Geo(xPixel, yPixel, this.d);
                comeraUpdateFactoryDelegate.geoPoint = this.d;
            } else {
                comeraUpdateFactoryDelegate.nowType = CameraUpdateFactoryDelegate.Type.zoomTo;
            }
            isScaleTouching = false;
            this.scaleBeginZoom = mapProjection.getMapZoomer();

            return true;
        }

        public void onScaleEnd(ScaleGestureDetector paramScaleGestureDetector) {
            this.scaleBeginZoom = 0.0F;
            if (isScaleTouching) {
                isScaleTouching = false;

                CameraUpdateFactoryDelegate localo = CameraUpdateFactoryDelegate.newInstance();
                localo.isChangeFinished = true;
                MapDelegateImp.this.mMapMessges.addMessage(localo);
            }
            EndTouchEvent();
        }
    }

    private class MapViewGestureDetectorListener // d
            implements GestureDetector.OnGestureListener {
        FPoint downPnt; // a
        IPoint downIPnt; // b
        IPoint iPnt; // c

        @Override
        public boolean onDown(MotionEvent paramMotionEvent) {
            mIsScrolling = false;
            if (!mIsDoubleTapping) {
                try {
                    MapDelegateImp.this.stopAnimation();
                } catch (RemoteException localRemoteException) {
                    SDKLogHandler.exception(localRemoteException, "MapDelegateImp", "onDown");
                    localRemoteException.printStackTrace();
                }
            }
            mIsDoubleTapping = false;
            pointerCounter = 0;
            this.downPnt.x = paramMotionEvent.getX();
            this.downPnt.y = paramMotionEvent.getY();
            MapDelegateImp.this.getPixel2Geo((int) this.downPnt.x, (int) this.downPnt.y, this.downIPnt);
            return true;
        }

        @Override
        public boolean onFling(MotionEvent paramMotionEvent1,
                               MotionEvent paramMotionEvent2, float velocityX,
                               float velocityY) {
            //velocityX:每秒x轴方向移动的像素,大于0表示向右
            mIsScrolling = false;
            try {
                if (!uiSettings.isScrollGesturesEnabled()) {
                    return true;
                }
            } catch (RemoteException remoteException) {
                SDKLogHandler.exception(remoteException, "MapDelegateImp",
                        "onFling");
                remoteException.printStackTrace();
            }
            if (multiTouchSupport.isDragMode()
                    || (paramMotionEvent1.getEventTime() - multiTouchSupport.getUpdateTime() < 30L)) {
                return true;
            }
            int width = MapDelegateImp.this.getMapWidth();
            int height = MapDelegateImp.this.getMapHeight();
            int mXFling = width * 2;
            int mYFling = height * 2;
            mScrollX = width / 2;
            mScrollY = height / 2;
            mCallback = null;

            if ((mInfoview != null) && (mInfoWindowMarker != null) && (!mInfoWindowMarker.F())) {
                aK = false;
                if (popupOverlay != null) {
                    popupOverlay.setVisible(true);
                }
            }
            mCameraAnimator.fling(mScrollX, mScrollY, (int) -velocityX * 4 / 5, (int) -velocityY * 4 / 5, -mXFling, mXFling, -mYFling, mYFling);

            if (MapDelegateImp.this.mTileOverlayView != null) {
                MapDelegateImp.this.mTileOverlayView.b(true);
            }
            return true;
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {

            mIsScrolling = false;

            markerDelegate = mOverlaysImageView.getLongPressHitMarker(motionEvent);

            Object localObject;
            if ((mMarkDragListener != null) && (markerDelegate != null) && (markerDelegate.isDraggable())) {
                hitMarker = new Marker(markerDelegate);
                localObject = hitMarker.getPosition();
                LatLng localLatLng1 = markerDelegate.g();
                IPoint localIPoint = new IPoint();
                getLatLng2Pixel(localLatLng1.latitude, localLatLng1.longitude, localIPoint);

                localIPoint.y -= 60;
                DPoint localDPoint = new DPoint();
                getPixel2LatLng(localIPoint.x, localIPoint.y, localDPoint);
                LatLng localLatLng2 = new LatLng(((LatLng) localObject).latitude + localDPoint.y - localLatLng1.latitude, ((LatLng) localObject).longitude + localDPoint.x - localLatLng1.longitude);

                hitMarker.setPosition(localLatLng2);
                mOverlaysImageView.set2Top(markerDelegate);
                mMarkDragListener.onMarkerDragStart(hitMarker);
                isMarkerDraging = true;
            } else if (mapLongClickListener != null) {
                localObject = new DPoint();
                getPixel2LatLng((int) motionEvent.getX(), (int) motionEvent.getY(), (DPoint) localObject);
                mapLongClickListener.onMapLongClick(new LatLng(((DPoint) localObject).y, ((DPoint) localObject).x));
                isLongPress = true;
            }
        }

        @Override
        public boolean onScroll(MotionEvent e1,
                                MotionEvent e2, float distanceX,
                                float distanceY) {
            mIsScrolling = true;
            if (((!mCameraAnimator.isFinished()) && (mCameraAnimator.getMode() == 1)) ||
                    (multiTouchSupport.isDragMode()) || (e2.getEventTime() - multiTouchSupport.getUpdateTime() < 30L)) {
                mIsScrolling = false;
                return true;
            }
            if (e2.getPointerCount() >= 2) {
                mIsScrolling = false;
            } else {//平移
                try {
                    if (!uiSettings.isScrollGesturesEnabled()) {
                        mIsScrolling = false;
                        return true;
                    }
                    if (pointerCounter > 1) {
                        mIsScrolling = false;
                        return true;
                    }
                    if ((mInfoview != null) && (mInfoWindowMarker != null) && (!mInfoWindowMarker.F()) && (popupOverlay != null)) {
                        popupOverlay.setVisible(true);
                    }
                    IPoint scrollPoint = new IPoint();
                    getPixel2Geo((int) e2.getX(), (int) e2.getY(), scrollPoint);//坐标转化
                    int dx = this.downIPnt.x - scrollPoint.x;
                    int dy = this.downIPnt.y - scrollPoint.y;

                    IPoint geoCenter = new IPoint();
                    mapProjection.getGeoCenter(geoCenter);
                    this.iPnt.x = (geoCenter.x + dx);
                    this.iPnt.y = (geoCenter.y + dy);
                    this.changeGeoCenter.geoPoint = this.iPnt;
                    mMapMessges.addMessage(this.changeGeoCenter);
                } catch (Throwable throwable) {
                    SDKLogHandler.exception(throwable, "MapDelegateImp", "onScroll");

                    throwable.printStackTrace();
                }
            }
            return true;
        }

        private MapViewGestureDetectorListener() {
            this.downPnt = new FPoint();
            this.downIPnt = new IPoint();

            this.iPnt = new IPoint();
        }

        CameraUpdateFactoryDelegate changeGeoCenter = CameraUpdateFactoryDelegate
                .changeGeoCenter(this.iPnt);// d

        @Override
        public boolean onSingleTapUp(MotionEvent paramMotionEvent) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent paramMotionEvent) {
        }
    }

    public void showInfoWindow(IMarkerDelegate marker) // a
            throws RemoteException {
        ViewGroup.LayoutParams localLayoutParams = null;
        if (marker == null) {
            return;
        }
        if ((marker.getTitle() == null) && (marker.getSnippet() == null)) {
            return;
        }
        if (this.mInfoAdapter != null) {
            hiddenInfoWindowShown();
            this.mInfoWindowMarker = marker;
            if (!this.aC) {
                this.au.postDelayed(new Runnable() {
                    public void run() {
                        try {
                            //this.a.a(AMapDelegateImp.w(this.a));
                            showInfoWindow(mInfoWindowMarker); //上一句翻译, 有待验证

                        } catch (Throwable localThrowable) {
                            SDKLogHandler.exception(localThrowable, "MapDelegateImp", "showInfoWindow postDelayed");
                            localThrowable.printStackTrace();
                        }
                    }
                }, 100L);

                return;
            }
            Marker localMarker = new Marker(marker);
            this.mInfoview = this.mInfoAdapter.getInfoWindow(localMarker);
            try {
                if (this.infowindow_bDrawable == null) {
                    this.infowindow_bDrawable = NinePatchToolDecode.getDrawable(
                            this.context, "marker_info_bg.9.png");
                }
            } catch (Throwable throwable) {
                SDKLogHandler.exception(throwable, "MapDelegateImp",
                        "showInfoWindow decodeDrawableFromAsset");

                throwable.printStackTrace();
            }
            if (this.mInfoview == null) {
                this.mInfoview = this.mInfoAdapter.getInfoContents(localMarker);
            }
            LinearLayout localLinearLayout = new LinearLayout(this.context);
            if (this.mInfoview != null) {
                if (this.mInfoview.getBackground() == null) {
                    this.mInfoview.setBackgroundDrawable(this.infowindow_bDrawable);
                }
                localLinearLayout.addView(this.mInfoview);
            } else {
                localLinearLayout.setBackgroundDrawable(this.infowindow_bDrawable);
                TextView localTextView1 = new TextView(this.context);
                localTextView1.setText(marker.getTitle());
                localTextView1.setTextColor(-16777216);
                TextView localTextView2 = new TextView(this.context);
                localTextView2.setTextColor(-16777216);
                localTextView2.setText(marker.getSnippet());
                localLinearLayout.setOrientation(LinearLayout.VERTICAL);
                localLinearLayout.addView(localTextView1);
                localLinearLayout.addView(localTextView2);
            }
            this.mInfoview = localLinearLayout;
            localLayoutParams = this.mInfoview.getLayoutParams();
            this.mInfoview.setDrawingCacheEnabled(true);
            this.mInfoview.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);
            marker.getRect();

            int i1 = marker.D() + marker.B();

            int i2 = marker.E() + marker.C() + 2;
            int i3 = -2;
            int i4 = -2;
            if (localLayoutParams != null) {
                i3 = localLayoutParams.width;
                i4 = localLayoutParams.height;
            }
            MapOverlayViewGroupDecode.LayoutParamsExt locala = new MapOverlayViewGroupDecode.LayoutParamsExt(
                    i3, i4, marker.anchorUVoff(), i1, i2, 81);
            Bitmap localBitmap;
            BitmapDescriptor localBitmapDescriptor;
            if (this.popupOverlay == null) {
                localBitmap = Util.getBitmapFromView(this.mInfoview);

                localBitmapDescriptor = BitmapDescriptorFactory.fromBitmap(localBitmap);
                localBitmap.recycle();

                this.popupOverlay = new PopupOverlayDecode(new MarkerOptions().icon(localBitmapDescriptor), this) {
                    public void a() {
                        //AMapDelegateImp.b(this.a).removeCallbacks(AMapDelegateImp.c(this.a));
                        au.removeCallbacks(bn); //上一句翻译
                        //AMapDelegateImp.b(this.a).post(AMapDelegateImp.a(this.a));
                        au.post(bo); //上一句翻译
                    }
                };
                this.popupOverlay.a(marker.anchorUVoff());
                this.popupOverlay.b(i1, i2);
            } else {
                this.popupOverlay.a(marker.anchorUVoff());
                this.popupOverlay.b(i1, i2);
                localBitmap = Util.getBitmapFromView(this.mInfoview);

                localBitmapDescriptor = BitmapDescriptorFactory.fromBitmap(localBitmap);
                localBitmap.recycle();
                this.popupOverlay.icon(localBitmapDescriptor);
            }
            this.mOverlaylay.addView(this.mInfoview, locala);
            marker.windowShowing(true);
        }
    }

    public void hiddenInfoWindowShown() // D
    {
        if (this.mInfoview != null) {
            this.mInfoview.clearFocus();
            this.mOverlaylay.removeView(this.mInfoview);
            Drawable localDrawable = this.mInfoview.getBackground();
            Util.a(localDrawable);
            Util.a(this.infowindow_bDrawable);
            if (this.popupOverlay != null) {
                this.popupOverlay.setVisible(false);
            }
            this.mInfoview = null;
        }
        this.mInfoWindowMarker = null;
    }

    private class MapViewDoubleClickListener // c check-1
            implements GestureDetector.OnDoubleTapListener {
        private MapViewDoubleClickListener() {
        }

        public boolean onDoubleTap(MotionEvent paramMotionEvent) {
            try {
                // if
                // (!AMapDelegateImp.m(AMapDelegateImp.this).isZoomGesturesEnabled())
                // {
                if (!uiSettings.isZoomGesturesEnabled()) { // 如上语句的翻译
                    return true;
                }
            } catch (RemoteException localRemoteException1) {
                localRemoteException1.printStackTrace();
            }
            // if (AMapDelegateImp.n(AMapDelegateImp.this) > 1) {
            if (pointerCounter > 1) { // 如上语句的翻译
                return true;
            }

            // AMapDelegateImp.e(AMapDelegateImp.this, true);
            mIsDoubleTapping = true; // 如上语句的翻译
            // float f1 =
            // AMapDelegateImp.r(AMapDelegateImp.this).getMapZoomer();
            float f1 = mapProjection.getMapZoomer(); // 如上语句的翻译
            if (f1 == MapDelegateImp.this.getMaxZoomLevel()) {
                return true;
            }
            float xPixel = paramMotionEvent.getX();
            float yPixel = paramMotionEvent.getY();
            int x = (int) xPixel;
            int y = (int) yPixel;
            CameraUpdateFactoryDelegate localo = CameraUpdateFactoryDelegate
                    .zoomBy(1.0F, new Point(x, y));
            try {
                MapDelegateImp.this.animateCamera(localo);
            } catch (RemoteException localRemoteException2) {
                SDKLogHandler.exception(localRemoteException2, "MapDelegateImp",
                        "onDoubleTap");

                localRemoteException2.printStackTrace();
            }
            return true;
        }

        public boolean onDoubleTapEvent(MotionEvent paramMotionEvent) {
            return false;
        }

        public boolean onSingleTapConfirmed(final MotionEvent paramMotionEvent) {
            // AMapDelegateImp.changeBearing(AMapDelegateImp.this, false);
            mIsScrolling = false; // 如上语句的翻译
            // if (AMapDelegateImp.F(AMapDelegateImp.this))
            if (isLongPress) // 如上语句翻译
            {
                // AMapDelegateImp.h(AMapDelegateImp.this, false);
                isLongPress = false; // 如上语句的翻译
                return true;
            }
            try {
                final Object hitMarker;
                Object marker;
                // if (AMapDelegateImp.v(AMapDelegateImp.this) != null)
                if (mInfoview != null) // 如上语句的翻译
                {
                    // Rect localRect = new
                    // Rect(AMapDelegateImp.v(AMapDelegateImp.this).getLeft(),
                    // AMapDelegateImp.v(AMapDelegateImp.this).getTop(),
                    // AMapDelegateImp.v(AMapDelegateImp.this).getRight(),
                    // AMapDelegateImp.v(AMapDelegateImp.this).getBottom());
                    Rect localRect = new Rect(mInfoview.getLeft(),
                            mInfoview.getTop(), mInfoview.getRight(),
                            mInfoview.getBottom()); // 如上语句翻译
                    if (MapDelegateImp.this.mOverlaysImageView.hitTest(
                            localRect, (int) paramMotionEvent.getX(),
                            (int) paramMotionEvent.getY())) {
                        // if (AMapDelegateImp.G(AMapDelegateImp.this) != null)
                        if (mInfoWindowClickListener != null) // 如上语句的翻译
                        {
                            hitMarker = MapDelegateImp.this.mOverlaysImageView.getHitMarker();
                            if (!((IMarkerDelegate) hitMarker).isVisible()) {
                                return true;
                            }
                            marker = new Marker((IMarkerDelegate) hitMarker);
                            // AMapDelegateImp.G(AMapDelegateImp.this).onInfoWindowClick((Marker)localObject2);
                            mInfoWindowClickListener.onInfoWindowClick((Marker) marker); //上一句翻译
                        }
                        return true;
                    }
                }
                boolean bool1 = MapDelegateImp.this.mOverlaysImageView.onSingleTap(paramMotionEvent);
                if (bool1) {
                    hitMarker = MapDelegateImp.this.mOverlaysImageView.getHitMarker();
                    if ((hitMarker == null) || (!((IMarkerDelegate) hitMarker).isVisible())) {
                        return true;
                    }
                    marker = new Marker(
                            (IMarkerDelegate) hitMarker);
                    // if (AMapDelegateImp.H(AMapDelegateImp.this) != null)
                    if (mMarkerClickListener != null) // 如上语句的翻译
                    {
                        // boolean bool2 =
                        // AMapDelegateImp.H(AMapDelegateImp.this).onMarkerClick((Marker)localObject2);
                        boolean bool2 = mMarkerClickListener.onMarkerClick((Marker) marker); //上一句翻译
                        if ((bool2) || (MapDelegateImp.this.mOverlaysImageView.getMarkersSize() <= 0)) {
                            MapDelegateImp.this.mOverlaysImageView.set2Top((IMarkerDelegate) hitMarker);
                            return true;
                        }
                        if (MapDelegateImp.this.mOverlaysImageView.getHitMarker() != null) {
                            // AMapDelegateImp.b(AMapDelegateImp.this).postDelayed(new Runnable()
                            au.postDelayed(new Runnable() {// 如上语句翻译
                                public void run() {
                                    try {
                                        //AMapDelegateImp.this.a(localObject1);
                                        showInfoWindow((IMarkerDelegate) hitMarker); //上一语句翻译
                                    } catch (Throwable localThrowable) {
                                        SDKLogHandler.exception(localThrowable, "MapDelegateImp", "onSingleTapUp showInfoWindow");
                                        localThrowable.printStackTrace();
                                    }
                                }
                            }, 20L);
                            if (!((IMarkerDelegate) hitMarker).F()) {
                                LatLng localLatLng = ((IMarkerDelegate) hitMarker).g();
                                if (localLatLng != null) {
                                    IPoint localIPoint = new IPoint();
                                    MapDelegateImp.this.latlon2Geo(
                                            localLatLng.latitude,
                                            localLatLng.longitude,
                                            localIPoint);

                                    // AMapDelegateImp.this.a(o.a(localIPoint));
//                                        MapDelegateImp.this.moveCamera(CameraUpdateFactoryDelegate.changeGeoCenter(localIPoint)); // 如上语句的翻译
                                }
                            }
                        }
                    }
                    MapDelegateImp.this.mOverlaysImageView.set2Top((IMarkerDelegate) hitMarker);
                    return true;
                }
                // if (AMapDelegateImp.I(AMapDelegateImp.this) != null)
                if (mapClickListener != null) // 如上语句翻译
                {
                    hitMarker = new DPoint();
                    MapDelegateImp.this.getPixel2LatLng(
                            (int) paramMotionEvent.getX(),
                            (int) paramMotionEvent.getY(),
                            (DPoint) hitMarker);
                    // AMapDelegateImp.I(AMapDelegateImp.this).onMapClick(new
                    // LatLng(((DPoint)localObject1).y,
                    // ((DPoint)localObject1).x));
                    mapClickListener.onMapClick(new LatLng(
                            ((DPoint) hitMarker).y,
                            ((DPoint) hitMarker).x)); // 如上语句翻译
                }
                // if (AMapDelegateImp.J(AMapDelegateImp.this) != null)
                if (mPolylineClickListener != null) // 如上语句翻译
                {
                    DPoint localObject12 = new DPoint();
                    MapDelegateImp.this.getPixel2LatLng(
                            (int) paramMotionEvent.getX(),
                            (int) paramMotionEvent.getY(),
                            (DPoint) localObject12);
                    marker = new LatLng(((DPoint) localObject12).y,
                            ((DPoint) localObject12).x);
                    if (marker != null) {
                        IOverlayDelegateDecode localai = MapDelegateImp.this.baseOverlayLayer
                                .polylineClick((LatLng) marker);
                        if (localai != null) {
                            // AMapDelegateImp.J(AMapDelegateImp.this).onPolylineClick(new
                            // Polyline((IPolylineDelegateDecode)localai));
                            mPolylineClickListener.onPolylineClick(new Polyline((IPolylineDelegateDecode) localai)); // 如上语句翻译

                            return true;
                        }
                    }
                }
                MapDelegateImp.this.a(new Runnable() {
                    public void run() {
                        // final Poi localPoi =
                        // AMapDelegateImp.a(AMapDelegateImp.this,
                        // (int)paramMotionEvent.getX(),
                        // (int)paramMotionEvent.getY(), 25);
                        final Poi localPoi = MapDelegateImp.this.getSelectedMapPoi(
                                (int) paramMotionEvent.getX(),
                                (int) paramMotionEvent.getY(), 25); // 如上语句翻译
                        // if ((AMapDelegateImp.K(AMapDelegateImp.this) != null)
                        // && (localPoi != null)) {
                        if ((mPOIClickListener != null) && (localPoi != null)) { // 如上语句翻译
                            MapDelegateImp.this.handler
                                    .post(new Runnable() {
                                        public void run() {
                                            // AMapDelegateImp.K(AMapDelegateImp.this).onPOIClick(localPoi);
                                            mPOIClickListener.onPOIClick(localPoi); // 如上语句翻译
                                        }
                                    });
                        }
                    }
                });
            } catch (Throwable localThrowable) {
                SDKLogHandler.exception(localThrowable, "MapDelegateImp",
                        "onSingleTapUp");

                localThrowable.printStackTrace();
                return true;
            }
            return true;
        }
    }

    private class MyRotateListener // f check - 1
            implements RotateGestureDetectorDecode.OnRotateGestureListener {
        float a; // a
        float b; // b
        IPoint c; // c

        private MyRotateListener() // f
        {
            this.a = 0.0F;
            this.b = 0.0F;
            this.c = new IPoint();
        }

        CameraUpdateFactoryDelegate d = CameraUpdateFactoryDelegate
                .newInstance(); // d

        public boolean onRotate(
                RotateGestureDetectorDecode paramd) // a
        {
            // if (AMapDelegateImp.k(AMapDelegateImp.this)) {
            if (isDoubleScolling) { // 如上语句翻译 －－ 如果在仰角则不缩放
                return false;
            }
            float f = paramd.getRotationDegreesDelta();
            this.a += f;
            // if ((AMapDelegateImp.L(AMapDelegateImp.this)) ||
            // (Math.abs(this.a) > 30.0F) || (Math.abs(this.a) > 350.0F))
            if (mIsRotating || (Math.abs(this.a) > 30.0F) || (Math.abs(this.a) > 350.0F)) { //上一语句翻译
                // AMapDelegateImp.i(AMapDelegateImp.this, true);
                mIsRotating = true; // 如上语句的翻译
                // this.b = (AMapDelegateImp.r(AMapDelegateImp.this).getMapAngle() + f);
                this.b = mapProjection.getMapAngle() + f; // 如上语句的翻译
                this.d.bearing = this.b;
                MapDelegateImp.this.mMapMessges.addMessage(this.d);
                this.a = 0.0F;
            }
            return true;
        }

        public boolean onRotateBegin(
                RotateGestureDetectorDecode paramd)// b
        {
            try {
                // if (!AMapDelegateImp.m(AMapDelegateImp.this).isRotateGesturesEnabled()){
                if (!uiSettings.isRotateGesturesEnabled()) { // 如上语句的翻译
                    return false;
                }
            } catch (RemoteException localRemoteException) {
                localRemoteException.printStackTrace();
            }

            if (!bj) {
                this.d.nowType = CameraUpdateFactoryDelegate.Type.changeBearing;
            } else {
                this.d.n = bj;
                this.d.nowType = CameraUpdateFactoryDelegate.Type.changeBearingGeoCenter;
                MapDelegateImp.this.getPixel2Geo(xPixel, yPixel, this.c);
                this.d.geoPoint = this.c;
            }

            // AMapDelegateImp.i(AMapDelegateImp.this, false);
            mIsRotating = false; // 如上语句的翻译
            this.a = 0.0F;
            // AMapDelegateImp.a(AMapDelegateImp.this, 2);
            pointerCounter = 2; // 如上语句的翻译
            // if (AMapDelegateImp.k(AMapDelegateImp.this)) {
            if (isDoubleScolling) { // 如上语句的翻译
                return false;
            }
            if (MapDelegateImp.this.getWidth() / 8.0F < paramd.c()) {
                return true;
            }
            return false;
        }

        public void onRotateEnd(
                RotateGestureDetectorDecode paramd)// c
        {
            this.a = 0.0F;
            // if (AMapDelegateImp.L(AMapDelegateImp.this))
            if (mIsRotating) // 如上语句的翻译
            {
                // AMapDelegateImp.i(AMapDelegateImp.this, false);
                mIsRotating = false;

                CameraUpdateFactoryDelegate localo = CameraUpdateFactoryDelegate.newInstance();
                localo.isChangeFinished = true;
                MapDelegateImp.this.mMapMessges.addMessage(localo);
            }
            // AMapDelegateImp.s(AMapDelegateImp.this);
            EndTouchEvent(); // 上一句翻译
        }
    }

    //双指上下滑
    private class MapMultiTouchDragListener // b --- check1
            implements MultiTouchSupportDecode.MultiTouchDragListener {
        private float startX1;//g
        private float startX2;//h
        private float startY1;//i
        private float startY2;//j
        private float startK;//k
        Float a;// a
        Float b;//b
        IPoint c;//c
        float d;// d

        public void onMultiTouchGestureInit(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5) // a
        {
            this.startX1 = paramFloat2;
            this.startY1 = paramFloat3;
            this.startX2 = paramFloat4;
            this.startY2 = paramFloat5;
            this.startK = ((this.startY2 - this.startY1) / (this.startX2 - this.startX1));
            this.a = null;
            this.b = null;
            if (bj) {
                this.e.nowType = CameraUpdateFactoryDelegate.Type.changeGeoCenterZoomTiltBearing;
                MapDelegateImp.this.getPixel2Geo(xPixel, yPixel, this.c);
                this.e.geoPoint = this.c;
                this.e.n = bj;
            } else {
                // this.e.nowType = o.a.changeBearing;
                this.e.nowType = CameraUpdateFactoryDelegate.Type.changeTilt; // 如上一句的翻译结果
            }
            // this.e.zoom =
            // AMapDelegateImp.r(AMapDelegateImp.this).getMapZoomer();
            this.e.zoom = mapProjection.getMapZoomer(); // 如上语句的翻译结果
            // this.e.g = AMapDelegateImp.r(AMapDelegateImp.this).getMapAngle();
            this.e.bearing = mapProjection.getMapAngle(); // 如上语句的翻译结果
        }

        private MapMultiTouchDragListener() // b
        {
            this.a = null;
            this.b = null;

            this.c = new IPoint();

            this.d = 0.0F;
        }

        CameraUpdateFactoryDelegate e = CameraUpdateFactoryDelegate
                .newInstance(); // e

        public boolean onMultiTouchDraging(MotionEvent paramMotionEvent,
                                           float paramFloat1, float paramFloat2, float paramFloat3,
                                           float paramFloat4) // a
        {
            try {
                // if
                // (!AMapDelegateImp.m(AMapDelegateImp.this).isTiltGesturesEnabled())
                // {
                if (!MapDelegateImp.this.uiSettings.isTiltGesturesEnabled()) { // 如上语句的翻译
                    return true;
                }
            } catch (RemoteException localRemoteException) {
                localRemoteException.printStackTrace();
                return true;
            }
            // if ((AMapDelegateImp.l(AMapDelegateImp.this)) ||
            // (AMapDelegateImp.L(AMapDelegateImp.this))) {
            if (isScaleTouching || mIsRotating) { // 如上语句的翻译
                return true;
            }

            if (this.b == null) {
                this.b = Float.valueOf(paramFloat4);
            }
            if (this.a == null) {
                this.a = Float.valueOf(paramFloat2);
            }
            float f1 = this.startY1 - paramFloat2;
            float f2 = this.startY2 - paramFloat4;
            float f3 = this.startX1 - paramFloat1;
            float f4 = this.startX2 - paramFloat3;
            float f5 = (paramFloat4 - paramFloat2)
                    / (paramFloat3 - paramFloat1);
            if ((Math.abs(this.startK - f5) < 0.2D)
                    && (((f1 > 0.0F) && (f2 > 0.0F)) || ((f1 < 0.0F)
                    && (f2 < 0.0F) && (((f3 >= 0.0F) && (f4 >= 0.0F)) || ((f3 <= 0.0F) && (f4 <= 0.0F)))))) {
                float f6 = (this.a.floatValue() - paramFloat2) / 4.0F;
                // AMapDelegateImp.j(AMapDelegateImp.this, true);
                isDoubleScolling = true; // 如上语句的翻译
                // float f7 =
                // AMapDelegateImp.r(AMapDelegateImp.this).getCameraHeaderAngle();
                float f7 = MapDelegateImp.this.mapProjection
                        .getCameraHeaderAngle(); // 如上语句的翻译
                this.d = ((f7 > 45.0F ? 45.0F : f7) - f6);
                this.e.tilt = this.d;
                MapDelegateImp.this.mMapMessges.addMessage(this.e);
                this.a = Float.valueOf(paramFloat2);
                this.b = Float.valueOf(paramFloat4);
                return true;
            }
            return false;
        }

        public void onMultiTouchSingleTap() {
            // if (AMapDelegateImp.l(AMapDelegateImp.this)) {
            if (isScaleTouching) { // 如上语句的翻译
                return;
            }
            try {
                // if
                // (!AMapDelegateImp.m(AMapDelegateImp.this).isZoomGesturesEnabled())
                // {
                if (!MapDelegateImp.this.uiSettings.isZoomGesturesEnabled()) { // 如上语句的翻译
                    return;
                }
            } catch (RemoteException localRemoteException1) {
                localRemoteException1.printStackTrace();
            }
            CameraUpdateFactoryDelegate localo = CameraUpdateFactoryDelegate
                    .zoomOut();
            try {
                MapDelegateImp.this.animateCamera(localo);
            } catch (RemoteException localRemoteException2) {
                SDKLogHandler.exception(localRemoteException2, "MapDelegateImp",
                        "onMultiTouchSingleTap");

                localRemoteException2.printStackTrace();
            }
        }
    }

    public float getZoomLevel() {
        return this.mapProjection.getMapZoomer();
    }

    private Runnable authThread = new mVerfy(this); //验证key

    private LatLngBounds bounds = null; // bg

    void setMapBounds() // F
    {
        this.handler.obtainMessage(UPDATE_MAP_BOUNDS).sendToTarget();
    }

    public LatLngBounds getMapBounds() {
        return this.bounds;
    }

    public LatLngBounds a(LatLng paramLatLng, float paramFloat) // a
    {
        int i1 = getWidth();
        int i2 = getHeight();
        if ((i1 <= 0) || (i2 <= 0)) {
            return null;
        }
        IPoint localIPoint = new IPoint();
        MapProjection.lonlat2Geo(paramLatLng.longitude, paramLatLng.latitude,
                localIPoint);
        MapProjection localMapProjection = new MapProjection(this.mapCore);
        localMapProjection.setCameraHeaderAngle(0.0F);
        localMapProjection.setMapAngle(0.0F);
        localMapProjection.setGeoCenter(localIPoint.x, localIPoint.y);
        localMapProjection.setMapZoomer(paramFloat);
        localMapProjection.recalculate();
        DPoint localDPoint = new DPoint();
        getPixel2LatLng(localMapProjection, 0, 0, localDPoint);
        LatLng localLatLng1 = new LatLng(localDPoint.y, localDPoint.x, false);
        getPixel2LatLng(localMapProjection, i1, i2, localDPoint);
        LatLng localLatLng2 = new LatLng(localDPoint.y, localDPoint.x, false);
        localMapProjection.recycle();

        return LatLngBounds.builder().include(localLatLng2)
                .include(localLatLng1).build();
    }

    final Handler handler = new MapDelegateHandlerDecode(this); // l
    private boolean isTRANSPARENT = false;//bh
    private boolean bi = false;//bi
    private boolean bj = false;//bj
    private int xPixel;//bk  屏幕坐标x
    private int yPixel;//bl  屏幕坐标y


    void H() // H
    {
        if (!this.isTRANSPARENT) {
            if (Build.VERSION.SDK_INT < 24) {
                this.glSurfaceView.setBackgroundColor(0);
            }
            this.mWaterMarkerView.setBackgroundColor(0);
            this.mScaleView.setBackgroundColor(0);
            this.mOverlaylay.setBackgroundColor(0);
            this.mTileOverlayView.setBackgroundColor(0);
            if (this.mZoomView != null) {
                this.mZoomView.setBackgroundColor(0);
            }
//            this.mOverlaysImageView.setBackgroundColor(0);
            this.mLocationView.setBackgroundColor(0);
            this.isTRANSPARENT = true;
        }
    }

    public Point I() // I
    {
        if (this.mWaterMarkerView == null) {
            return null;
        }
        return this.mWaterMarkerView.getPosition();
    }

    public static Bitmap SavePixels(int x, int y, int w, int h, GL10 gl) {
        Bitmap localBitmap = null;
        try {
            int[] arrayOfInt1 = new int[w * h];
            int[] arrayOfInt2 = new int[w * h];
            IntBuffer localIntBuffer = IntBuffer.wrap(arrayOfInt1);
            localIntBuffer.position(0);
            gl.glReadPixels(x, y, w, h, 6408, 5121, localIntBuffer);
            for (int i1 = 0; i1 < h; i1++) {
                for (int i2 = 0; i2 < w; i2++) {
                    int i3 = arrayOfInt1[(i1 * w + i2)];
                    int i4 = i3 >> 16 & 0xFF;
                    int i5 = i3 << 16 & 0xFF0000;
                    int i6 = i3 & 0xFF00FF00 | i5 | i4;
                    arrayOfInt2[((h - i1 - 1) * w + i2)] = i6;
                }
            }
            localBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            localBitmap.setPixels(arrayOfInt2, 0, w, 0, 0, w, h);
            return localBitmap;
        } catch (Throwable localThrowable) {
            SDKLogHandler.exception(localThrowable, "MapDelegateImp", "SavePixels");

            localThrowable.printStackTrace();
        }
        return null;
    }
//
//    public void getMapPrintScreen(MapController.onMapPrintScreenListener paramonMapPrintScreenListener) // a
//    {
//        this.mapOnMapPrintScreenListener = paramonMapPrintScreenListener;
//        this.aJ = true;
//        setRunLowFrame(false);
//    }

    public void a(MapController.OnMapScreenShotListener paramOnMapScreenShotListener)// a
    {
        this.mapOnMapScreenShotListener = paramOnMapScreenShotListener;
        this.aJ = true;
        setRunLowFrame(false);
    }

    public void setLogoPosition(int paramInt) {
        if (this.mWaterMarkerView != null) {
            this.mWaterMarkerView.setLogoPosition(paramInt);
            this.mWaterMarkerView.invalidate();
            if (this.mScaleView.getVisibility() == View.VISIBLE) {
                this.mScaleView.invalidate();
            }
        }
    }

    public void setZoomPosition(int paramInt) {
        if (this.mZoomView != null) {
            MapOverlayViewGroupDecode.LayoutParamsExt layoutParams = (MapOverlayViewGroupDecode.LayoutParamsExt) this.mZoomView
                    .getLayoutParams();
            if (paramInt == MapOptions.ZOOM_POSITION_RIGHT_CENTER) {
                layoutParams.alignment = 16;
            } else if (paramInt == MapOptions.ZOOM_POSITION_RIGHT_BUTTOM) {
                layoutParams.alignment = 80;
            }
            this.mOverlaylay.updateViewLayout(this.mZoomView, layoutParams);
        }
    }

    public float getScalePerPixel() // J
    {
        try {
            LatLng localLatLng = getCameraPosition().target;
            float f1 = this.mapzoomer;
            if (this.initProjOver) {
                f1 = this.mapProjection.getMapZoomer();
            }
            return (float) (Math
                    .cos(localLatLng.latitude * 3.141592653589793D / 180.0D) * 2.0D * 3.141592653589793D * 6378137.0D / (256.0D * Math
                    .pow(2.0D, f1)));
        } catch (Throwable localThrowable) {
            SDKLogHandler.exception(localThrowable, "MapDelegateImp",
                    "getScalePerPixel");

            localThrowable.printStackTrace();
        }
        return 0.0F;
    }

    void refreshTileOverlay(boolean paramBoolean)// m
    {
        this.handler.obtainMessage(REFRESH_TILE_OVERLAY, paramBoolean ? 1 : 0, 0)
                .sendToTarget();
    }

    protected void cameraChangeFinish(boolean paramBoolean,
                                      CameraPosition paramCameraPosition) // a
    {
        if (this.cameraChangeListener == null) {
            return;
        }
        if (!this.mCameraAnimator.isFinished()) {
            return;
        }
        if (!this.glSurfaceView.isEnabled()) {
            return;
        }
        if (paramCameraPosition == null) {
            try {
                paramCameraPosition = getCameraPosition();
            } catch (RemoteException localRemoteException) {
                SDKLogHandler.exception(localRemoteException, "MapDelegateImp",
                        "cameraChangeFinish");

                localRemoteException.printStackTrace();
            }
        }
        this.cameraChangeListener.onCameraChangeFinish(paramCameraPosition);
    }

    public void deleteTexsureId(int paramInt) // f
    {
        if (this.textureList.contains(Integer.valueOf(paramInt))) {
            this.freeTextureList.add(Integer.valueOf(paramInt));
            this.textureList.remove(this.textureList.indexOf(Integer
                    .valueOf(paramInt)));
        }
    }

    public int getTexsureId() // K
    {
        Integer localInteger = Integer.valueOf(0);
        if (this.freeTextureList.size() > 0) {
            localInteger = (Integer) this.freeTextureList.get(0);
            this.freeTextureList.remove(0);
            this.textureList.add(localInteger);
        }
        return localInteger.intValue();
    }

    public List<Marker> getMapScreenMarkers() // L
    {
        LMapThrowException
                .ThrowIllegalStateException((getWidth() > 0)
                        && (getHeight() > 0), "地图未初始化完成！");
        return this.mOverlaysImageView.getMapScreenMarkers();
    }

    public void M() {
        this.baseOverlayLayer.changeOverlayIndexs();
        if (this.geojsonOverlayManager != null) {
            this.geojsonOverlayManager.changeOverlayIndexs();
        }
    }

    public void N() {
        this.bi = true;
    }

    public boolean O() {
        return this.bi;
    }

    public void P() {
        if (this.mOverlaysImageView != null) {
            this.mOverlaysImageView.realdestroy();
        }
        if (this.geojsonOverlayManager != null) {
            this.geojsonOverlayManager.realdestroy();
        }
        this.bi = false;
    }

    /**
     * 设置屏幕上的某个点为地图中心点
     *
     * @param x - 屏幕上设置为地图中心点的 x 像素坐标，x 的范围为 0<= x <= 手机屏幕的像素宽度。
     * @param y - 屏幕上设置为地图中心点的 y 像素坐标，y 的范围为 0<= y <= 手机屏幕的像素高度。
     */
    public void setPointToCenter(int x, int y) {
        if (this.mapCallback != null) {
            this.bj = true;
            this.mapCallback.a(x, y);
            this.xPixel = x;
            this.yPixel = y;
        }
    }

    public void setCompassViewPosition(final int xPix, final int yPix) {
        if (this.mCompassView != null && mOverlaylay != null) {
            if (xPix >= 0 && yPix >= 0) {
                // 如果不加此延时方法,下面的view获取宽高都是0
                this.mOverlaylay.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int x = xPix;
                        int y = yPix;
                        int xMax = mOverlaylay.getWidth() - MapDelegateImp.this.mCompassView.getWidth();
                        int yMax = mOverlaylay.getHeight() - MapDelegateImp.this.mCompassView.getHeight();
                        if (x > xMax) x = xMax;
                        if (y > yMax) y = yMax;
                        MapOverlayViewGroupDecode.LayoutParamsExt param = (MapOverlayViewGroupDecode.LayoutParamsExt) mCompassView.getLayoutParams();
                        param.mapx = x;
                        param.mapy = y;
                        MapDelegateImp.this.mOverlaylay.updateViewLayout(mCompassView, param);
                    }
                }, 10);
            }
        }
    }

    public void setScaleViewPosition(int xPix, int yPix) {
        if (this.mScaleView != null && mOverlaylay != null) {
            if (xPix >= 0 && yPix >= 0) {
                this.mScaleView.setScaleViewPosition(xPix, yPix);
                this.mScaleView.invalidate();
            }
        }
    }

    public void setLogoViewPosition(int xPix, int yPix) {
        if (this.mWaterMarkerView != null && mOverlaylay != null) {
            if (xPix >= 0 && yPix >= 0) {
                this.mWaterMarkerView.setLogoPositionByPix(xPix, yPix);
                this.mWaterMarkerView.invalidate();
            }
        }
    }

    public void setCompassViewBitmap(Bitmap bitmap) {
        if (this.mCompassView != null)
            this.mCompassView.setmCompassViewBitmap(bitmap);
    }

    public void setLogoBitmap(Bitmap bitmap) {
        if (this.mWaterMarkerView != null)
            this.mWaterMarkerView.setBitMap(bitmap);
    }

    public void setLocationViewBitmap(Bitmap[] bitmaps) {
        if (bitmaps.length >= 2) {
            if (this.mLocationView != null)
                this.mLocationView.setLocstionViewBitmap(bitmaps[0], bitmaps[1]);
        }
    }

    public void setMapTextZIndex(int paramInt) {
        this.ap = paramInt;
    }

    public int getMapTextZIndex() {
        return this.ap;
    }

    public boolean R() {
        return this.aC;
    }

    public CameraAnimatorDecode S() {
        return this.mCameraAnimator;
    }

    public void setLoadOfflineData(final boolean paramBoolean)
            throws RemoteException {
        a(new Runnable() {
            public void run() {
                MapDelegateImp.this.mapCore.setParameter(2601,
                        paramBoolean ? 1 : 0, 0, 0, 0);
            }
        });
    }

    public void removecache()

    {
        removecache((MapController.OnCacheRemoveListener) null);
    }

    public void removecache(MapController.OnCacheRemoveListener listener) {
        if (this.rmCacheHandler != null) {
            try {
                this.mapCore.setParameter(2601, 0, 0, 0, 0);
                RemoveCacheRunnable rmRunnable = new RemoveCacheRunnable(this.context, listener);

                this.rmCacheHandler.removeCallbacks(rmRunnable);
                this.rmCacheHandler.post(rmRunnable);
            } catch (Throwable localThrowable) {
                SDKLogHandler.exception(localThrowable, "MapDelegateImp",
                        "removecache");

                localThrowable.printStackTrace();
            }
        }
    }

    private boolean a(File paramFile) // a
            throws IOException, Exception {
        if ((paramFile == null) || (!paramFile.exists())) {
            return false;
        }
        File[] arrayOfFile = paramFile.listFiles();
        if (arrayOfFile != null) {
            for (int i1 = 0; i1 < arrayOfFile.length; i1++) {
                if (arrayOfFile[i1].isFile()) {
                    if (!arrayOfFile[i1].delete()) {
                        return false;
                    }
                } else {
                    if (!a(arrayOfFile[i1])) {
                        return false;
                    }
                    arrayOfFile[i1].delete();
                }
            }
        }
        return true;
    }

    private class RemoveCacheRunnable // i  check - 1
            implements Runnable {
        private Context context; // b
        private MapController.OnCacheRemoveListener listener; // c

        public RemoveCacheRunnable(Context context, MapController.OnCacheRemoveListener listener) // i
        {
            this.context = context;
            this.listener = listener;
        }

        public void run() {
            boolean bool = true;
            try {
                Context context = this.context.getApplicationContext();
                String str1 = Util.getMapDataPath(context);
                String str2 = Util.getMapRoot(context);
                // bool = (bool) && (AMapDelegateImp.a(AMapDelegateImp.this, new File(str1)));
                // bool = (bool) && (AMapDelegateImp.a(AMapDelegateImp.this, new File(str2)));
                bool = (bool) && MapDelegateImp.this.a(new File(str1)); // 原始语句见上面，
                bool = (bool) && MapDelegateImp.this.a(new File(str2));
                return;
            } catch (Throwable localThrowable2) {
                SDKLogHandler.exception(localThrowable2, "MapDelegateImp", "RemoveCacheRunnable");

                bool = false;
            } finally {
                try {
                    MapDelegateImp.this.mapCore.setParameter(2601, 1, 0, 0, 0);
                    if (this.listener != null) {
                        this.listener.onRemoveCacheFinish(bool);
                    }
                } catch (Throwable localThrowable4) {
                    localThrowable4.printStackTrace();
                }
            }
        }

        public boolean equals(Object paramObject) {
            return paramObject instanceof RemoveCacheRunnable;
        }
    }

    public void U() // U
    {
        if (this.baseOverlayLayer != null) {
            this.baseOverlayLayer.calMapFPoint();
        }
        if (this.mOverlaysImageView != null) {
            this.mOverlaysImageView.calFPoint();
        }
        if (this.geojsonOverlayManager != null) {
            this.geojsonOverlayManager.calFPoint();
            this.geojsonOverlayManager.calMapFPoint();
        }
        if (this.customRenderer != null) {
            this.customRenderer.onMapReferencechanged();
        }
        for (CustomRenderer reader : rendererList) {
            reader.onMapReferencechanged();
        }
    }

    public void setVisibility(int visibility) // h
    {
        this.glSurfaceView.setVisibility(visibility);
    }


    private Poi getSelectedMapPoi(int xPix, int yPix, int bounds) // a
    {
        if (!this.aC) {
            return null;
        }
        try {
            MapPoi localSelectedMapPoi = this.mapCore
                    .GetSelectedMapPoi(xPix, yPix, bounds);
            if (localSelectedMapPoi != null) {
                DPoint localDPoint = new DPoint();
                MapProjection.geo2LonLat(localSelectedMapPoi.mapx,
                        localSelectedMapPoi.mapy, localDPoint);
                return new Poi(localSelectedMapPoi.name, new LatLng(
                        localDPoint.y, localDPoint.x, false),
                        localSelectedMapPoi.poiid);
            }
            return null;
        } catch (Throwable localThrowable) {
        }
        return null;
    }

    private Handler bm = new MapTouchEventHandler(this);
    private Runnable bn = new ffff(this);
    private Runnable bo = new gggg(this);
    CustomRenderer customRenderer;
    CopyOnWriteArrayList<CustomRenderer> rendererList = new CopyOnWriteArrayList<CustomRenderer>();
    int drawCustomRenderTime = 3;

    class MapTouchEventHandler extends Handler {

        MapTouchEventHandler(MapDelegateImp mapDelegateImp) {
        }

        public void handleMessage(Message message) {
            super.handleMessage(message);
            try {
                if (mMapTouchListener != null) {
                    mMapTouchListener.onTouch((MotionEvent) message.obj);
                }
            } catch (Throwable localThrowable) {
                SDKLogHandler.exception(localThrowable, "MapDelegateImp", "onTouchHandler");
                localThrowable.printStackTrace();
            }
        }
    }

    class ffff implements Runnable {

        ffff(MapDelegateImp mapDelegateImp) {
        }

        public void run() {
            if (mInfoview != null) {
                aK = true;
                if (popupOverlay != null) {
                    popupOverlay.setVisible(false);
                }
            }
        }
    }

    class gggg implements Runnable {

        gggg(MapDelegateImp paramMapDelegateImp) {
        }

        public void run() {
            if (mInfoview != null) {
                aK = false;
                mInfoview.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void drawCustomRenderTime(int drawTime) {
        if (drawTime == 3 || drawTime == 4) {
            this.drawCustomRenderTime = drawTime;
        }
    }

    @Override
    public void setCustomRenderer(CustomRenderer customRenderer) // a
    {
        this.customRenderer = customRenderer;
    }

    @Override
    public void addCustomRenderer(CustomRenderer customRenderer) {
        if (!this.rendererList.contains(customRenderer)) {
            this.rendererList.add(customRenderer);
        }
    }

    @Override
    public void removeCustomRenderer(CustomRenderer customRenderer) {
        if (this.rendererList.contains(customRenderer)) {
            this.rendererList.remove(customRenderer);
        }
    }

    @Override
    public void clearCustomRenderer() {
        if (this.rendererList.size() > 0) {
            this.rendererList.clear();
        }
    }

    //自定义控制刷新率
    private class MyTask
            extends TimerTask {
        MapDelegateImp map;

        public MyTask(MapDelegateImp map) {
            this.map = map;
        }

        public void run() {
            if ((!isRunLowFrame) || postDelayedRun || (!baseOverlayLayer.checkInBounds())) {
                glSurfaceView.requestRender();
            } else if (!mOverlaysImageView.isAnimator()) {
                glSurfaceView.requestRender();
            }
        }
    }

//    private class e // e  check - 1
//            implements InndoorViewDecode.InndoorViewInterface {
//        private e() {
//        }
//
//        public void a(int paramInt) // a
//        {
//            //if (AMapDelegateImp.Z(AMapDelegateImp.this) != null)
//            if (av != null) //上一句翻译
//            {
//                //AMapDelegateImp.Z(AMapDelegateImp.this).changeBearing = AMapDelegateImp.Z(AMapDelegateImp.this).g[paramInt];
//                av.d = av.g[paramInt];//上一句翻译
//                //AMapDelegateImp.Z(AMapDelegateImp.this).c = AMapDelegateImp.Z(AMapDelegateImp.this).h[paramInt];
//                av.c = av.h[paramInt];//上一句翻译
//                //AMapDelegateImp.this.e(false);
//                setRunLowFrame(false);//上一句翻译
//                MapDelegateImp.this.a(new Runnable() {
//                    public void run() {
//                        //AMapDelegateImp.changeBearing(AMapDelegateImp.this).setIndoorBuildingToBeActive(
//                        //  AMapDelegateImp.Z(AMapDelegateImp.this).c,
//                        //  AMapDelegateImp.Z(AMapDelegateImp.this).changeBearing,
//                        //  AMapDelegateImp.Z(AMapDelegateImp.this).e);
//                        mapCore.setIndoorBuildingToBeActive(av.c, av.d, av.e);//上一句翻译
//                    }
//                });
//            }
//        }
//    }

    public Context getContext() // V
    {
        return this.context;
    }

    /**
     * 非动画设置中心点
     *
     * @param latLng
     */
    public void setMapCenter(LatLng latLng) {
        if (mapProjection == null) {
            return;
        }
        if (latLng.latitude == 0 || latLng.longitude == 0) {
            return;
        }
        IPoint geoPoint = new IPoint();
        MapProjection.lonlat2Geo(latLng.longitude, latLng.latitude, geoPoint);
        mapProjection.setGeoCenter(geoPoint.x, geoPoint.y);
    }

    public void a(Runnable runnable) // a
    {
        if (this.glSurfaceView != null) {
            this.glSurfaceView.queueEvent(runnable);
        }
    }

    private void b(GL10 paramGL10) // b
    {
        runTimer();
        this.mapCore.setGL(paramGL10);

        setInternaltexture();
        this.mapCore.surfaceCreate(paramGL10);
        if ((this.lineTexture == null) || (this.lineTexture.isRecycled())) {
            this.lineTexture = Util.fromAsset(
                    this.context, "default_route_Texture.png");
        }
        if ((this.lineDashTexture == null)
                || (this.lineDashTexture.isRecycled())) {
            this.lineDashTexture = Util.fromAsset(
                    this.context, "dotted_Line_Texture.png");
        }
        this.LINE_TEXTURE_ID = Util.loadTexture(
                paramGL10, this.lineTexture);
        this.o = Util.a(paramGL10,
                this.lineDashTexture, true);

        this.lineTexture = null;

        setRunLowFrame(false);
        if (!this.aD) {
            try {
                authHandler.post(authThread);
                this.aD = true;
            } catch (Throwable localThrowable) {
                localThrowable.printStackTrace();
            }
        }
//        OfflineCityInfoManager.checkVersionAndDownJson(context,null);


    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        this.glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);//反应式模式
        try {
            b(gl);
        } catch (Throwable localThrowable) {
            localThrowable.printStackTrace();
        }
        if (this.customRenderer != null) {
            this.customRenderer.onSurfaceCreated(gl, config);
        }
        for (CustomRenderer reader : rendererList) {
            reader.onSurfaceCreated(gl, config);
        }
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
        this.rectMap = new Rect(0, 0, width, height);
        try {
            a(gl, width, height);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    private void a(GL10 gl10, int width, int height) // a
    {
        this.mapCore.setGL(gl10);
        this.mapCore.surfaceChange(gl10, width, height);
        int densityDpi = this.context.getResources().getDisplayMetrics().densityDpi;
        float density = this.context.getResources().getDisplayMetrics().density;
        setInternaltexture();
        int zoomScaleInt = 100;
        int textScaleInt = 50;
        int dip = 1;
        int mallocCacheType = 1;// 1 low 2 mid 3 hight 4 vhight
        if (densityDpi > 120) {
            if (densityDpi <= 160) {
                if (Math.max(width, height) <= 480) {// 320*480 phone
                    zoomScaleInt = 120;
                    textScaleInt = 60;//120
                    dip = 1;
                } else { // 1280*800 pad
                    zoomScaleInt = 100;
                    textScaleInt = 80; //160
                    dip = 1;
                }
                mallocCacheType = 1;
            } else if (densityDpi <= 240) {
                if (Math.min(width, height) >= 1000) {// 1200*1772// 华硕pad
                    zoomScaleInt = 60;
                    textScaleInt = 100;//200
                    dip = 2;
                    mallocCacheType = 2;
                } else {
                    zoomScaleInt = 70;
                    textScaleInt = 75;//
                    dip = 2;
                    mallocCacheType = 2;
                }
            } else if (densityDpi <= 320) {
                zoomScaleInt = 50;
                textScaleInt = 95; //180
                dip = 2;
                mallocCacheType = 3;
            } else if (densityDpi <= 480) {// 1080p
                zoomScaleInt = 50;
                textScaleInt = 150; //300
                dip = 3;
                mallocCacheType = 3;
            } else {
                zoomScaleInt = 40;
                textScaleInt = 180; //360
                dip = 3;
                mallocCacheType = 4;
            }
        }
        //this.mapCore.setParameter(2051, zoomScaleInt, textScaleInt, (int) (density * 100.0F), mallocCacheType);
        this.mapCore.setParameter(2051, zoomScaleInt, textScaleInt, dip, mallocCacheType); //按照V4DEMO改写

        this.mapCore.setParameter(1001, 0, 0, 0, 0);
        this.mapCore.setParameter(1021, 1, 0, 0, 0);
        this.mapCore.setParameter(1022, 1, 0, 0, 0);
        this.mapCore.setParameter(1023, 1, 0, 0, 0);
        this.mapCore.setParameter(1024, this.isShowMapText ? 1 : 0, 0, 0, 0);

        setRunLowFrame(false);
        if (this.k == null) {
            this.k = new Runnable() {
                public void run() {
                    MapDelegateImp.this.handler.obtainMessage(CREATEMAP).sendToTarget();
                }
            };
        }
        this.handler.postDelayed(this.k, 300L);
        if (this.customRenderer != null) {
            this.customRenderer.onSurfaceChanged(gl10, width, height);
        }
        for (CustomRenderer reader : rendererList) {
            reader.onSurfaceChanged(gl10, width, height);
        }
    }

    private void c(GL10 paramGL10) // c
    {
        this.initProjOver = false;

        this.aC = false;
        if (this.mapProjection != null) {
            IPoint localIPoint = new IPoint();
            DPoint dCenter = new DPoint();
            this.mapProjection.getGeoCenter(localIPoint);
            mapProjection.geo2LonLat(localIPoint.x, localIPoint.y, dCenter);
            this.mapzoomer = this.mapProjection.getMapZoomer();
            this.mapangle = this.mapProjection.getMapAngle();
            this.cameraheaderangle = this.mapProjection.getCameraHeaderAngle();
            this.mapcenterx = dCenter.x;
            this.mapcentery = dCenter.y;
        }
        cancelTimer();
        this.aI = Boolean.valueOf(true);
        if (this.mapCore != null) {
            try {
                this.mapCore.destroy();
            } catch (Throwable localThrowable) {
                localThrowable.printStackTrace();
            }
            this.mapCore.setMapCallback(null);
            this.mapCore = null;
        }
        if (this.mapProjection != null) {
            this.mapProjection.recycle();
            this.mapProjection = null;
        }
        VMapDataCache.getInstance().reset();

        this.mMapMessges.destory();

        this.mMapMessges = new MapMessageQueueDecode(this);

        this.mapCore = new MapCore(this.context);

        this.mapCore.setMapCallback(this.mapCallback);

        this.glMapResManager = new GLMapResManager(this, this.context);

        this.projectionDelegate = new ProjectionDelegateImpDecode(this);
        if (!this.initProjOver) {
            initProjection();
        }
        this.mapCallback.onResume(this.mapCore);

        this.isSetInternaltexture = false;

        b(paramGL10);
        a(paramGL10, this.rectMap.width(), this.rectMap.height());

        this.aI = Boolean.valueOf(false);
    }

    public void reloadMap() // W
    {
        this.bp = true;
    }

    public GLMapResManager.MapViewTime getMapViewTime() // X
    {
        return this.mapViewTime;
    }

    public GLMapResManager.MapViewMode getMapViewMode()// Y
    {
        return this.mapViewMode;
    }

    public GLMapResManager.MapViewModeState getMapViewModeState() // Z
    {
        return this.mapViewModeState;
    }

    private void ai() // ai
    {
        if ((this.mapViewMode != GLMapResManager.MapViewMode.SATELLITE)
                && (this.mapViewTime != GLMapResManager.MapViewTime.NIGHT)) {
            this.mWaterMarkerView.changeBitmap(false);
        } else {
            this.mWaterMarkerView.changeBitmap(true);
        }
    }

    private boolean bp = false; // bp

    private static abstract class a // a
            implements Runnable {
        boolean b = false; //b
        boolean c = false; //c
        GLMapResManager.MapViewMode d; // d
        GLMapResManager.MapViewTime e; //e
        GLMapResManager.MapViewModeState f; //f

        public void run() {
            this.b = false;
        }
    }

    private a bq = new aaa(this); // bq

    // ----- 如下是 com.leador.api.mapcore.h 类的反编译代码 －－－－－－－－－－－－－－－－
    class aaa extends MapDelegateImp.a {// 之前是com.amp.api.mapcore.h类，因为无法使用直接派生私有的基类，所以挪进来了。

        MapDelegateImp map = null;

        aaa(MapDelegateImp mapDelegateImp) {
            super();
            map = mapDelegateImp;
        }

        public void run() {
            super.run();
            // this.a.a(this.changeBearing, this.e, this.f);
            map.setMapType(this.d, this.e, this.f); // 之前是上一句。 但感觉逻辑不太对
        }
    }

    // ----- 如下是 com.leador.api.mapcore.changeBearing 类的反编译代码 －－－－－－－－－－－－－－－－

    class MapDelegateHandlerDecode extends Handler {
        MapDelegateImp mapDelegateImp;//
        boolean isShowToast = true;//防止多次提示

        MapDelegateHandlerDecode(MapDelegateImp mapDelegateImp) {
            this.mapDelegateImp = mapDelegateImp;
        }

        public void handleMessage(Message paramMessage) {
            // if ((paramMessage == null) || (AMapDelegateImp.N(this.a).booleanValue()) {
            if ((paramMessage == null) || aI.booleanValue()) { //上一句翻译
                return;
            }

            this.mapDelegateImp.setRunLowFrame(false);
            CameraUpdateFactoryDelegate localo;
            switch (paramMessage.what) {
                case AUTH_KEY_FAILURE:
                    String message = "";
                    Log.w("authFail", "Key验证失败：[" + AuthManager.b + "]");
                    if (AuthManager.authResult == 2 && isShowToast) {
                        message = "经检测key值为空，请配置！";
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        isShowToast = false;
                    } else {
                        message = "授权验证无响应";
                    }
                    if (mapCore != null) {
                        mapCore.setParameter(5001, 0, 0, 0, 0);
                    }
                    break;


                /** camera改变 */
                case ON_CAMERA_CHANGE:
                    CameraPosition localCameraPosition = (CameraPosition) paramMessage.obj;
                    // if ((localCameraPosition != null) &&
                    // (AMapDelegateImp.O(this.a) != null)) {
                    if ((localCameraPosition != null)
                            && (cameraChangeListener != null)) { // 如上语句翻译
                        // AMapDelegateImp.O(this.a).onCameraChange(localCameraPosition);
                        cameraChangeListener.onCameraChange(localCameraPosition); // 如上语句翻译
                    }

                    break;
                /** 地图初始化成功 */
                case ON_MAP_LOADED:
                    // if (AMapDelegateImp.P(this.a) != null) {
                    if (aw != null) { // 如上语句翻译
                        try {
                            // this.a.doInBackground(AMapDelegateImp.P(this.a));
                            this.mapDelegateImp.moveCamera(aw); // 如上语句翻译
                        } catch (Throwable localThrowable1) {
                            SDKLogHandler.exception(localThrowable1, "MapDelegateImp",
                                    "onMapLoaded");

                            localThrowable1.printStackTrace();
                        }
                    }
                    // if (AMapDelegateImp.Q(this.a) != null) {
                    if (mMapLoadedListener != null) { // 如上语句翻译
                        // AMapDelegateImp.Q(this.a).onMapLoaded();
                        mMapLoadedListener.onMapLoaded(); // 如上语句翻译
                    }
                    break;
                case NEW_LATLNG_BOUNDS_WITH_SIZE:
                    localo = (CameraUpdateFactoryDelegate) paramMessage.obj;
                    if (localo != null) {
                        // this.a.mMapMessges.a(localo);
                        this.mapDelegateImp.mMapMessges.addMessage(localo); // 如上语句翻译
                    }
                    break;

                /** 地图事件动画实现 */
                case CAMERA_ANIMATION:
                    // if ((AMapDelegateImp.A(this.a) != null) &&
                    // (AMapDelegateImp.A(this.a).h())) {
                    if ((mCameraAnimator != null) && (mCameraAnimator.computeScrollOffset())) { // 如上语句的翻译

                        // switch (AMapDelegateImp.A(this.a).getRect())
                        switch (mCameraAnimator.getMode()) // 如上语句翻译
                        {
                            case CameraAnimatorDecode.CHANGE_CAMERA_MODE:
                                // localo = CameraUpdateFactoryDelegateDecode.a(new
                                // IPoint( AMapDelegateImp.A(this.a).b(),
                                // AMapDelegateImp.A(this.a).c()),
                                // AMapDelegateImp.A(this.a).changeBearing(),
                                // AMapDelegateImp.A(this.a).e(),
                                // AMapDelegateImp.A(this.a).f());

                                localo = CameraUpdateFactoryDelegate.changeGeoCenterZoomTiltBearing(
                                        new IPoint(mCameraAnimator.getCurrX(), mCameraAnimator
                                                .getCurrY()), mCameraAnimator.getCurrZ(),
                                        mCameraAnimator.getCurrBearing(), mCameraAnimator.getCurrTilt()); // 如上语句翻译

                                // if (AMapDelegateImp.A(this.a).a()) {
                                /** 标识动画是否结束 */
                                if (mCameraAnimator.isFinished()) {// 如上语句翻译

                                    localo.isChangeFinished = true;
                                }
                                // localo.n = AMapDelegateImp.A(this.a).k();
                                localo.n = mCameraAnimator.k();// 如上语句翻译
                                this.mapDelegateImp.mMapMessges.addMessage(localo);
                                break;
                            default:// Fling
                                // int i = AMapDelegateImp.A(this.a).b() - AMapDelegateImp.y(this.a);
                                // int k = AMapDelegateImp.A(this.a).c() - AMapDelegateImp.z(this.a);
                                // AMapDelegateImp.b(this.a,AMapDelegateImp.A(this.a).b());
                                // AMapDelegateImp.c(this.a,AMapDelegateImp.A(this.a).c());

                                int x1 = mCameraAnimator.getCurrX() - mScrollX; //  如上四句的翻译
                                int y1 = mCameraAnimator.getCurrY() - mScrollY;
                                mScrollX = mCameraAnimator.getCurrX();
                                mScrollY = mCameraAnimator.getCurrY();

                                // localObject2 = new FPoint(this.a.m() / 2 + i,this.a.n() / 2 + k);
                                FPoint fp = new FPoint(
                                        this.mapDelegateImp.glSurfaceView.getWidth() / 2 + x1,
                                        this.mapDelegateImp.glSurfaceView.getHeight() / 2 + y1); // 如上语句翻译
                                FPoint localFPoint = new FPoint();
                                // AMapDelegateImp.r(this.a).win2Map((int)((FPoint)localObject2).x,(int)((FPoint)localObject2).y, localFPoint);
                                mapProjection.win2Map((int) fp.x, (int) fp.y, localFPoint);// 如上语句翻译
                                IPoint localIPoint2 = new IPoint();
                                // AMapDelegateImp.r(this.a).map2Geo(localFPoint.x,
                                // localFPoint.y, localIPoint2);
                                mapProjection.map2Geo(localFPoint.x, localFPoint.y,
                                        localIPoint2);// 如上语句翻译

                                CameraUpdateFactoryDelegate localObject3 = CameraUpdateFactoryDelegate
                                        .changeGeoCenter(localIPoint2);
                                // if (AMapDelegateImp.A(this.a).a()) {
                                if (mCameraAnimator.isFinished()) {// 如上语句翻译
                                    // ((CameraUpdateFactoryDelegateDecode)localObject3).p = true;
                                    ((CameraUpdateFactoryDelegate) localObject3).isChangeFinished = true; // 如上语句翻译
                                }

                                this.mapDelegateImp.mMapMessges
                                        .addMessage((CameraUpdateFactoryDelegate) localObject3);
                        }
                    }
                    break;
                case COMPASS_UPDATE:
                    // if (AMapDelegateImp.R(this.a) == null) {
                    if (mCompassView == null) {// 如上语句翻译
                        return;
                    }
                    // AMapDelegateImp.R(this.a).b();
                    mCompassView.invalidateAngle(); // 上一句翻译
                    break;
                case SCALE_UPDATE:
                    this.mapDelegateImp.changeScaleState();
                    break;
                /** 地图截图ui线程实现 */
                case SCREEN_SHOT:
                    Bitmap bitmap = (Bitmap) paramMessage.obj;
                    int status = paramMessage.arg1;
                    if (bitmap != null) {
                        Canvas localCanvas = new Canvas(bitmap);
                        if (mWaterMarkerView != null) {
                            mWaterMarkerView.draw(localCanvas);
                        }
                        if ((mInfoview != null) && (mInfoWindowMarker != null)) {
                            Bitmap localObject2 = mInfoview.getDrawingCache(true);
                            if (localObject2 != null) {
                                int n = mInfoview.getLeft();
                                int i1 = mInfoview.getTop();
                                localCanvas.drawBitmap(localObject2, n,
                                        i1, new Paint());
                            }
                        }
//                        if (mapOnMapPrintScreenListener != null) {
//                            mapOnMapPrintScreenListener
//                                    .onMapPrint(new BitmapDrawable(context
//                                            .getResources(), (Bitmap) bitmap));
//                        }
                        if (mapOnMapScreenShotListener != null) {
                            mapOnMapScreenShotListener.onMapScreenShot((Bitmap) bitmap);
                            mapOnMapScreenShotListener.onMapScreenShot((Bitmap) bitmap, status);
                        }
                    } else {
//                        if (mapOnMapPrintScreenListener != null) {
//                            mapOnMapPrintScreenListener.onMapPrint(null);
//                        }
                        if (mapOnMapScreenShotListener != null) {
                            mapOnMapScreenShotListener.onMapScreenShot(null);
                        }
                    }
//                    mapOnMapPrintScreenListener = null;
                    mapOnMapScreenShotListener = null;
                    break;
                case CAMERA_UPDATE_FINISH:
                    if ((mInfoview != null) && (popupOverlay != null)) {
                        mInfoview.setVisibility(View.VISIBLE);
                    }
                    try {
                        CameraPosition cameraPosition = this.mapDelegateImp.getCameraPosition();
                        if (cameraPosition != null) {
                            if ((((CameraPosition) cameraPosition).zoom >= 10.0F)
                                    && (!RegionUtil.a(
                                    ((CameraPosition) cameraPosition).target.latitude,
                                    ((CameraPosition) cameraPosition).target.longitude))) {
                                mWaterMarkerView.setVisibility(View.GONE);
                            } else {
                                mWaterMarkerView.setVisibility(View.VISIBLE);
                            }
                        }

                        if ((mCallback == null) || (!isAnimationStep)) { // 如上三句翻译
                            cameraChangeFinish(true, (CameraPosition) cameraPosition);
                        }

                        if (mCallback != null) {
                            isCallbacking = true;
                            mCallback.onFinish();
                            isCallbacking = false;
                        }
                        if (!callbackChanged) {
                            mCallback = null;
                        } else {
                            callbackChanged = false;
                        }
                    } catch (Throwable localThrowable2) {
                        SDKLogHandler.exception(localThrowable2, "MapDelegateImp",
                                "CameraUpdateFinish");
                    }
                    break;
                case UPDATE_MAP_BOUNDS:/** 设置当前地图显示范围 */

                    int width = this.mapDelegateImp.getWidth();//j
                    int height = this.mapDelegateImp.getHeight();//m
                    if ((width <= 0) || (height <= 0)) {
                        bounds = null;
                    } else {
                        try {
                            CameraPosition localObject2 = this.mapDelegateImp.getCameraPosition();
                            IPoint localIPoint1 = new IPoint();
                            MapProjection
                                    .lonlat2Geo(
                                            ((CameraPosition) localObject2).target.longitude,
                                            ((CameraPosition) localObject2).target.latitude,
                                            localIPoint1);

                            MapProjection localMapProjection = new MapProjection(
                                    mapCore);
                            localMapProjection
                                    .setCameraHeaderAngle(((CameraPosition) localObject2).tilt);
                            localMapProjection
                                    .setMapAngle(((CameraPosition) localObject2).bearing);
                            localMapProjection
                                    .setMapZoomer(((CameraPosition) localObject2).zoom);
                            localMapProjection.recalculate();
                            DPoint dp = new DPoint();
                            getPixel2LatLng(localMapProjection, 0, 0, (DPoint) dp);
                            LatLng localLatLng1 = new LatLng(
                                    ((DPoint) dp).y,
                                    ((DPoint) dp).x, false);
                            getPixel2LatLng(localMapProjection, width, 0, (DPoint) dp);
                            LatLng localLatLng2 = new LatLng(
                                    ((DPoint) dp).y,
                                    ((DPoint) dp).x, false);
                            getPixel2LatLng(localMapProjection, 0, height, (DPoint) dp);
                            LatLng localLatLng3 = new LatLng(
                                    ((DPoint) dp).y,
                                    ((DPoint) dp).x, false);
                            getPixel2LatLng(localMapProjection, width, height, (DPoint) dp);
                            LatLng localLatLng4 = new LatLng(
                                    ((DPoint) dp).y,
                                    ((DPoint) dp).x, false);
                            bounds = LatLngBounds.builder().include(localLatLng3)
                                    .include(localLatLng4).include(localLatLng1)
                                    .include(localLatLng2).build();// 上一句翻译
                            localMapProjection.recycle();
                        } catch (Throwable localThrowable4) {
                        }
                    }
                    break;
                case CREATEMAP:/** 地图第一次启动时 */
                    try {
                        /** 设置地图view为透明状态,避免启动时黑屏显示 */
                        this.mapDelegateImp.H();
                        this.mapDelegateImp.refreshTileOverlay(true);
                    } catch (Throwable localThrowable3) {
                        SDKLogHandler.exception(localThrowable3, "MapDelegateImp",
                                "CREATEMAP");

                        localThrowable3.printStackTrace();
                    }
                    break;
                case REFRESH_TILE_OVERLAY:
                    if ((mCameraAnimator.isFinished()) || (
                            (mCameraAnimator.getMode() != 1) && (mTileOverlayView != null))) {
                        mTileOverlayView.b(false);
                    }
                    if (mTileOverlayView != null) {
                        mTileOverlayView.refresh(paramMessage.arg1 != 0);
                    }
                    if (MapDelegateImp.this.geojsonOverlayManager != null) {
                        MapDelegateImp.this.geojsonOverlayManager.refresh(paramMessage.arg1 != 0);
                    }
                    break;
                case UPDATE_ZOOM_BITMAP:
                    if (this.mapDelegateImp.mZoomView != null) {
                        this.mapDelegateImp.mZoomView.setZoomBitmap(this.mapDelegateImp.getZoomLevel());
                    }
                    break;
                case REFRASH_MAP://刷新
                    runTimer();
                    break;
            }
            this.mapDelegateImp.setRunLowFrame(false);
        }
    }

    class mVerfy implements Runnable {//c
        int count = 0;//鉴权次数

        mVerfy(MapDelegateImp paramMapDelegateImp) {
        }

        public void run() {
            try {
                // 解决离线鉴权和在线鉴权之间冲突问题。
                if (Util.getOfflineStatus(context) && (null != mapCore && mapCore.getAuthStatus())) {//离线不鉴权，不启动上报日志
                    AuthManager.authResult = 0;
                    return;
                }
                boolean bool = Util.checkNet(context);
                if (!bool) {
                    count++;
                    if (count < 3) {
                        authHandler.postDelayed(authThread, 5000);
                    } else {
                        AuthManager.authResult = 0;
                    }
                    return;
                }
                SDKInfo sdkInfo = new SDKInfo.createSDKInfo(ConfigableConstDecode.product, MapsInitializer.getVersion(), ConfigableConstDecode.userAgent, "test").setPackageName(new String[]{"com.leador.api.maps", "com.leador.api.mapcore", "com.leador.mapcore"}).a();

                if (count == 0)
//                    if (Util.getMapUrl(context).contains("ishowchina")) {
//                        SDKLogHandler.a(context, sdkInfo);
//                    }

                AuthManager.getKeyAuth(context, sdkInfo);
                if (AuthManager.authResult != 0) {
                    if (AuthManager.authResult == -1) {
                        count++;
                        if (count < 3) {
                            authHandler.postDelayed(authThread, 5000);
                        } else {
                            AuthManager.authResult = 0;
                        }
                    } else {
                        handler.sendEmptyMessage(AUTH_KEY_FAILURE);
                    }
                }
                ConfigableConstDecode.sdkInfo = sdkInfo;
                setRunLowFrame(false);
            } catch (Throwable localThrowable) {
                SDKLogHandler.exception(localThrowable, "MapDelegateImpGLSurfaceView", "mVerfy");
                localThrowable.printStackTrace();
            }
        }
    }
}
