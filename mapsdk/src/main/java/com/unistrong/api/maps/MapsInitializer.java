package com.unistrong.api.maps;

import android.content.Context;
import android.os.RemoteException;

import com.unistrong.api.mapcore.MapFragmentDelegateImp;
import com.unistrong.api.mapcore.util.AuthManager;

/**
 * 在获取地图之前某些功能想要使用，可以使用这个类初始化地图Android API。 由于一些类比如BitmapDescriptorFactory、CameraUpdateFactory需要被初始化，就必须调用类MapsInitializer。 如果您正在使用MapFragment或MapView，并且调用getMap()获得非空的地图，您不需要关注这个类。
 */
public final class MapsInitializer {
    /**
     * 墨卡托投影坐标系，系统默认坐标系。
     */
    public static final int COOD_SYSTEM_900913 = 0;
    /**
     * 4326坐标系。
     */
    public static final int COOD_SYSTEM_4326 = 1;

    private static final String MAPSDK_VERSION = "1.12.0";
    private static int coodSystem = COOD_SYSTEM_4326;

    /**
     * 初始化地图Android API,为使用它包含的类做准备。 在这些类中，您如果正在使用MapFragment或MapView，并且调用getMap()获得非空的地图，您可以不需要调用这个方法。
     *
     * @param paramContext - 获取必须的API资源和代码。不能为空。
     * @throws RemoteException - 如果服务器不可使用。
     */
    public static void initialize(Context paramContext)
            throws RemoteException {
        MapFragmentDelegateImp.context = paramContext.getApplicationContext();
    }

    /**
     * 获取坐标系类型。
     *
     * @return 坐标系类型。
     */
    public static int getCoodSystem() {
        return coodSystem;
    }

    /**
     * 设置坐标系类型，调用需写在onCreate()函数内的第一行。
     *
     * @param coodSystem 坐标系类型，900913坐标系：COOD_SYSTEM_900913，4326坐标系：COOD_SYSTEM_4326。
     */
    public static void setCoodSystem(int coodSystem) {
        if (coodSystem == COOD_SYSTEM_900913) {
            MapsInitializer.coodSystem = COOD_SYSTEM_900913;
        } else {
            MapsInitializer.coodSystem = COOD_SYSTEM_4326;
        }
    }

    /**
     * 地图存储目录。
     */
    public static String sdcardDir = "";
    private static boolean netWorkEnable = true;

    /**
     * 设置是否可以联网获取地图数据。
     *
     * @param enable - 是否可以联网获取地图数据。默认为true，可以联网。
     */
    public static void setNetWorkEnable(boolean enable) {
        netWorkEnable = enable;
    }

    /**
     * 是否可以联网获取地图数据。
     *
     * @return 是否可以联网获取地图数据。默认为true，可以联网。
     */
    public static boolean getNetWorkEnable() {
        return netWorkEnable;
    }

    /**
     * 设置访问使用的协议类别。
     *
     * @param apiKey
     */
    public static void setApiKey(String apiKey) {
        if ((apiKey != null) && (apiKey.trim().length() > 0)) {
            AuthManager.a(apiKey);
        }
    }

    /**
     * 返回地图的版本号。
     *
     * @return 地图的版本号。
     */
    public static String getVersion() {
        return MAPSDK_VERSION;
    }
}
