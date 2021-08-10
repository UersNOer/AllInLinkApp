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
 * 一个显示地图的视图（View）。它负责从服务端获取地图数据。当屏幕焦点在这个视图上时，它将会捕捉键盘事件（如果手机配有实体键盘）及屏幕触控手势事件。 使用这个类必须按照它的生命周期进行操控，你必须参照以下方法onCreate(Bundle)、 onResume()、onPause()、onDestroy()、onSaveInstanceState(Bundle)、onLowMemory() 当MapView初始化完成后，用户可以通过getMap()方法获得一个LMap 对象。如果MapView 没有初始成功，则执行getMap()将返回null。 如果要求程序在比较低版本的Android 上运行，使用这个类将比TextureMapFragment 或TextureSupportMapFragment 类更加合适。
 * 它和MapView的区别在于，TextureMapView是一个TextureView,而MapView是一个GLSurfaceView。
 * 所以TextureMapView 可以和其他的GlSurfaceView（比如相机）共存不会出现穿透现象，也可以放在ViewPager中不会出现滑动黑边现象。
 */
public class TextureMapView
        extends FrameLayout {
    private IMapFragmentDelegate iMapFragmentDelegate;
    private MapController lmap;
    private int visible = 0;

    /**
     * 根据给定的参数构造一个MapView 的新对象。
     *
     * @param context - 上下文。
     */
    public TextureMapView(Context context) {
        super(context);
        getMapFragmentDelegate().setContext(context);
    }

    /**
     * 根据给定的参数构造一个MapView 的新对象。
     *
     * @param context - 上下文。
     */

    private boolean loadWTMS;

    public TextureMapView(Context context, AttributeSet attributeSet) {
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
     * @param context - 上下文。
     */
    public TextureMapView(Context context, AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
        this.visible = attributeSet.getAttributeIntValue(16842972, View.VISIBLE);
        getMapFragmentDelegate().setContext(context);
        getMapFragmentDelegate().setVisible(this.visible);
    }

    /**
     * 根据给定的参数构造一个MapView 的新对象。
     *
     * @param paramContext - 上下文。
     */
    public TextureMapView(Context paramContext, MapOptions paramMapOptions) {
        super(paramContext);
        getMapFragmentDelegate().setContext(paramContext);
        getMapFragmentDelegate().setMapOptions(paramMapOptions);
    }

    protected IMapFragmentDelegate getMapFragmentDelegate() {
        if (this.iMapFragmentDelegate == null) {
            this.iMapFragmentDelegate = new MapFragmentDelegateImp(MapFragmentDelegateImp.TEXTURE_VIEW);
        }
        return this.iMapFragmentDelegate;
    }

    /**
     * 返回一个与这个视察（View）相关联的LMap 对象。
     *
     * @return Map 如果这个TextureMapView 没有成功被初始化，则调用此方法会返回Null。
     */
    public MapController getMap() throws UnistrongException {
        IMapFragmentDelegate localaf = getMapFragmentDelegate();
        IMapDelegate localaa;
        try {
            localaa = localaf.getMapDelegate();
        } catch (RemoteException localRemoteException) {
            throw new RuntimeRemoteException(localRemoteException);
        }
        if (localaa == null) {
            return null;
        }
        if (this.lmap == null) {
            this.lmap = new MapController(localaa);
        }
        return this.lmap;
    }

    /**
     * 用户重载这个方法时必须调用父类的这个方法。
     */
    public final void onCreate(Bundle paramBundle) {
        try {

            MapController map = null;
            map = getMap();
            if (!loadWTMS) {
                final String url = "http://map.xg.ha.cn:8082";
                TileOverlay tileOverlay = map.addTileOverlayOptions(url, null);
            }
            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(
                    new LatLng(34.48375819, 113.81705838), 14, 30, 0));
            map.animateCamera(cameraUpdate, 10, null);

            //View localView = getMapFragmentDelegate().a(null, null, paramBundle);
            View localView = getMapFragmentDelegate().onCreateView((LayoutInflater) null, (ViewGroup) null, paramBundle);

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
     * 用户重载这个方法时必须调用父类的这个方法，保存地图状态。
     */
    public final void onSaveInstanceState(Bundle bundle) {
        try {
            getMapFragmentDelegate().onSaveInstanceState(bundle);
        } catch (RemoteException localRemoteException) {
            localRemoteException.printStackTrace();
        }
    }

    /**
     * 是在是否显示，在fragment切换的时候可以使用，或者想隐藏MapView的时候可以使用。
     *
     * @param paramInt true：显示； false：不显示；
     */
    public void setVisibility(int paramInt) {
        super.setVisibility(paramInt);
        getMapFragmentDelegate().setVisible(paramInt);
    }
}
