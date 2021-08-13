package com.example.android_supervisor.map;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.example.android_supervisor.R;
import com.example.android_supervisor.config.AppConfig;

import java.util.ArrayList;

/***/
public class MapLocationAPI {

    private static AMapLocationClient mLocationClient;
    /**
     * 表示获取不到设备gps或者网络gps
     */
    private static final double value_default = 4.9E-324;
    // private static double value_change = 0;

    private static ArrayList<MapLocationListener> listeners = new ArrayList<>();

    private static Context mContext;


    /**
     * 初始化
     */
    public static void initialize(Context context) {
        mContext = context;

        mLocationClient = new AMapLocationClient(context);

        AMapLocationClientOption locationOption = new AMapLocationClientOption();
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        locationOption.setNeedAddress(true);
        locationOption.setLocationCacheEnable(true);

        locationOption.setOnceLocationLatest(true);//修正AP源数据

        int interval = AppConfig.getGpsRequestInterval(context) * 1000;
        locationOption.setInterval(interval);

        mLocationClient.setLocationOption(locationOption);
        mLocationClient.setLocationListener(mLocationListener);
    }

    /**
     * 启动定位服务
     */
    public static void start() {
        mLocationClient.startLocation();
        mLocationClient.enableBackgroundLocation(2001, buildNotification());
    }

    /**
     * 停止定位服务
     */
    public static void stop() {
        mLocationClient.stopLocation();
    }

    public static boolean isStarted() {
        return mLocationClient.isStarted();
    }

    /**
     * 获取当前location
     */
    public static AMapLocation getLocation() {
        AMapLocation location = null;

//        location = mLocationClient.getLastKnownLocation();

        if (validate()) {
            location = mLocationClient.getLastKnownLocation();
        }
        return location;

        // return null;
    }

    private static int locType = 0;

    //	/** 判断是否gps是否有效 */
    private static boolean validate() {
        // 判断getLocType BDLocation.TypeGpsLocation
        // BDLocation.TypeNetWorkLocation
        // 或者判断 getLongitude getLatitude，当获取不到设备gps或者网络gps时返回4.9E-324

        // return (value_change != 0 && value_change != value_default);
        //return true;
        return locType != 0;
    }

    private static AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                locType = aMapLocation.getLocationType();
//                if (validate()) {
//                    onMapReceiveLocation(aMapLocation);
//                }
                onMapReceiveLocation(aMapLocation);
            } else {
                locType = 0;
            }
        }


    };

    private static void onMapReceiveLocation(AMapLocation location) {
        for (MapLocationListener listener : listeners) {
            if (listener != null) {
                listener.onMapReceiveLocation(location);
            }
        }
    }

    //
	public static void addMapLocationListener(MapLocationListener listener)
	{
		if(listener != null && !listeners.contains(listener))
		{
			listeners.add(listener);
		}
	}
//
    public static void removeMapLocationListener(MapLocationListener listener) {
        if (listener != null && listeners.contains(listener)) {
            listeners.remove(listener);
        }
    }

    public static interface MapLocationListener {
        public void onMapReceiveLocation(AMapLocation location);
    }
//
//	public static void logMsg(A location)
//	{
//		StringBuffer sb = new StringBuffer(256);
//		sb.append("time : ");
//		sb.append(location.getTime());
//		sb.append("\nerror code : ");
//		sb.append(location.getLocType());
//		sb.append("\nlatitude : ");
//		sb.append(location.getLatitude());
//		sb.append("\nlontitude : ");
//		sb.append(location.getLongitude());
//		sb.append("\nradius : ");
//		sb.append(location.getRadius());
//
//		if(location.getLocType() == BDLocation.TypeGpsLocation)
//		{
//			sb.append("\nspeed : ");
//			sb.append(location.getSpeed());
//			sb.append("\nsatellite : ");
//			sb.append(location.getSatelliteNumber());
//			sb.append("\ndirection : ");
//			sb.append("\naddr : ");
//			sb.append(location.getAddrStr());
//			sb.append(location.getDirection());
//		}
//		else if(location.getLocType() == BDLocation.TypeNetWorkLocation)
//		{
//			sb.append("\naddr : ");
//			sb.append(location.getAddrStr());
//			// 运营商信息
//			sb.append("\noperationers : ");
//			sb.append(location.getOperators());
//		}
//		Log.i("BaiduLocationApiDem", sb.toString());
//	}

    private static NotificationManager notificationManager = null;
    private static boolean isCreateChannel = false;

    @SuppressLint("NewApi")
    private static Notification buildNotification() {

        Notification.Builder builder = null;
        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= 26) {
            //Android O上对Notification进行了修改，如果设置的targetSDKVersion>=26建议使用此种方式创建通知栏
            if (null == notificationManager) {
                notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
            }
            String channelId = mContext.getPackageName();
            if (!isCreateChannel) {
                NotificationChannel notificationChannel = new NotificationChannel(channelId,
                        "BackgroundLocation", NotificationManager.IMPORTANCE_DEFAULT);
                notificationChannel.enableLights(true);//是否在桌面icon右上角展示小圆点
                notificationChannel.setLightColor(Color.BLUE); //小圆点颜色
                notificationChannel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
                notificationManager.createNotificationChannel(notificationChannel);
                isCreateChannel = true;
            }
            builder = new Notification.Builder(mContext, channelId);
        } else {
            builder = new Notification.Builder(mContext);
        }
        builder.setSmallIcon(R.drawable.app_icon_mask)
                .setContentTitle(mContext.getString(R.string.app_label))
                .setContentText("正在后台运行")
                .setWhen(System.currentTimeMillis());

        if (android.os.Build.VERSION.SDK_INT >= 16) {
            notification = builder.build();
        } else {
            return builder.getNotification();
        }
        return notification;
    }

}
