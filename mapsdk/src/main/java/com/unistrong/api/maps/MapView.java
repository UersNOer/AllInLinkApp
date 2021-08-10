package com.unistrong.api.maps;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.unistrong.api.mapcore.IMapDelegate;
import com.unistrong.api.mapcore.IMapFragmentDelegate;
import com.unistrong.api.mapcore.MapFragmentDelegateImp;
import com.unistrong.api.maps.model.CameraPosition;
import com.unistrong.api.maps.model.LatLng;
import com.unistrong.api.maps.model.RuntimeRemoteException;
import com.unistrong.api.maps.model.TileOverlay;
import com.leador.map.R;

/**
 * 一个显示地图的视图（View）。它负责从服务端获取地图数据。当屏幕焦点在这个视图上时，它将会捕捉键盘事件（如果手机配有实体键盘）及屏幕触控手势事件。 使用这个类必须按照它的生命周期进行操控，你必须参照以下方法onCreate(Bundle)、 onResume()、onPause()、onDestroy()、onSaveInstanceState(Bundle)、onLowMemory() 当MapView初始化完成后，用户可以通过getMap()方法获得一个LMap 对象。如果MapView 没有初始成功，则执行getMap()将返回null。 如果要求程序在比较低版本的Android 上运行，使用这个类将比MapFragment 或SupportMapFragment 类更加合适。
 */
public class MapView extends FrameLayout {
    private IMapFragmentDelegate mapFragmentDelegate;
    private MapController lmap;
    private int visible = View.VISIBLE;

    private Bundle savedInstanceState;

    public void setSavedInstanceState(Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
    }

    /**
     * 根据给定的参数构造一个MapView 的新对象。
     *
     * @param paramContext - 上下文，不能为空
     */
    public MapView(Context paramContext) {
        super(paramContext);
        getMapFragmentDelegate().setContext(paramContext);
    }

    /**
     * 根据给定的参数构造一个MapView 的新对象。
     *
     * @param context      - 上下文，不能为空。
     * @param attributeSet
     */
    public MapView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.visible = attributeSet.getAttributeIntValue(16842972, View.VISIBLE);


        TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.MapView);
        loadWTMS = a.getBoolean(R.styleable.MapView_loadwtms, false);

        if (loadWTMS) {
            MapsInitializer.setCoodSystem(MapsInitializer.COOD_SYSTEM_900913);
        }

        getMapFragmentDelegate().setContext(context);
        getMapFragmentDelegate().setVisible(this.visible);
    }

    /**
     * 根据给定的参数构造一个MapView 的新对象。
     *
     * @param paramContext      - 上下文，不能为空。
     * @param paramAttributeSet
     * @param paramInt
     */
    public MapView(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
        this.visible = paramAttributeSet.getAttributeIntValue(16842972, View.VISIBLE);
        getMapFragmentDelegate().setContext(paramContext);
        getMapFragmentDelegate().setVisible(this.visible);
    }

    /**
     * 根据给定的参数构造一个MapView 的新对象。
     *
     * @param context    - 上下文，不能为空。
     * @param mapOptions - Map 的选项类。
     */
    public MapView(Context context, MapOptions mapOptions) {
        super(context);
        getMapFragmentDelegate().setContext(context);
        getMapFragmentDelegate().setMapOptions(mapOptions);
    }


    protected IMapFragmentDelegate getMapFragmentDelegate() {
        if (this.mapFragmentDelegate == null) {
            this.mapFragmentDelegate = new MapFragmentDelegateImp(MapFragmentDelegateImp.SURFACE_VIEW);
        }
        return this.mapFragmentDelegate;
    }

    /**
     * 返回一个与这个视察（View）相关联的LMap 对象。
     *
     * @return 如果这个MapView 没有成功被初始化，则调用此方法会返回Null。
     */
    public MapController getMap() throws UnistrongException {
        IMapFragmentDelegate localaf = getMapFragmentDelegate();
        IMapDelegate mapDelegate;
        try {
            mapDelegate = localaf.getMapDelegate();
        } catch (RemoteException remoteException) {
            throw new RuntimeRemoteException(remoteException);
        }
        if (mapDelegate == null) {
            return null;
        }
        if (this.lmap == null) {
            this.lmap = new MapController(mapDelegate);
        }
        return this.lmap;
    }

    /**
     * 用户重载这个方法时必须调用父类的这个方法。
     */

    private boolean loadWTMS;


    public final void onCreate() {
        try {

            MapController map = null;
            map = getMap();
//            if (!loadWTMS) {
//                final String url = "http://map.xg.ha.cn:8082";
//                TileOverlay tileOverlay = map.addTileOverlayOptions(url, null);
//            }
            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(
                    new LatLng(34.48375819, 113.81705838), 10, 30, 0));
            map.animateCamera(cameraUpdate, 10, null);


            View localView = getMapFragmentDelegate().onCreateView((LayoutInflater) null, (ViewGroup) null, savedInstanceState);

            ViewGroup.LayoutParams localLayoutParams = new ViewGroup.LayoutParams(-1, -1);

            addView(localView, localLayoutParams);
        } catch (RemoteException localRemoteException) {
            localRemoteException.printStackTrace();
        } catch (UnistrongException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用户重载这个方法时必须调用父类的这个方法。
     */

    public final void onResume() {
        try {
            getMapFragmentDelegate().onResume();
        } catch (RemoteException localRemoteException) {
            localRemoteException.printStackTrace();
        }
    }

    /**
     * 用户重载这个方法时必须调用父类的这个方法。
     */
    public final void onPause() {
        try {
            getMapFragmentDelegate().onPause();
        } catch (RemoteException localRemoteException) {
            localRemoteException.printStackTrace();
        }
    }

    /**
     * 用户重载这个方法时必须调用父类的这个方法。
     */
    public final void onDestroy() {
        try {
            MapsInitializer.setCoodSystem(MapsInitializer.COOD_SYSTEM_4326);
            IMapFragmentDelegate localaf = getMapFragmentDelegate();
            localaf.onDestroy();
        } catch (RemoteException localRemoteException) {
            localRemoteException.printStackTrace();
        }
    }

    /**
     * 用户重载这个方法时必须调用父类的这个方法。
     */
    public final void onLowMemory() {
        try {
            getMapFragmentDelegate().onLowMemory();
        } catch (RemoteException localRemoteException) {
            localRemoteException.printStackTrace();
        }
    }

    /**
     * 用户重载这个方法时必须调用父类的这个方法。
     */
    public final void onSaveInstanceState(Bundle paramBundle) {
        try {
            getMapFragmentDelegate().onSaveInstanceState(paramBundle);
        } catch (RemoteException localRemoteException) {
            localRemoteException.printStackTrace();
        }
    }

//  public void setLayerType(int paramInt, Paint paramPaint) {}

    /**
     * 是在是否显示，在fragment切换的时候可以使用，或者想隐藏MapView的时候可以使用。
     *
     * @param paramInt true：显示； false：不显示；
     */
    public void setVisibility(int paramInt) {
        super.setVisibility(paramInt);
        getMapFragmentDelegate().setVisible(paramInt);
    }

    public void addWTMStoMap() {
        final String url = "http://map.xg.ha.cn:8082";
        final MapController lMap;
        try {
            lMap = this.lmap;
            TileOverlay tileOverlay = lMap.addTileOverlayOptions(url, null);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
