package com.example.android_supervisor.common;

import android.content.Context;
import android.os.Looper;
import android.util.ArraySet;

import com.example.android_supervisor.config.AppConfig;
import com.example.android_supervisor.map.TencentLocationAdapter;
import com.orhanobut.logger.Logger;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class TXLocationApi {
    public static final int COORDINATE_TYPE_GCJ02 = TencentLocationManager.COORDINATE_TYPE_GCJ02;
    public static final int COORDINATE_TYPE_WGS84 = TencentLocationManager.COORDINATE_TYPE_WGS84;

    private static TencentLocationListenerHolder locationListenerHolder;

    private static AtomicBoolean started = new AtomicBoolean(false);

    /**
     * 启动定位服务
     */
    public static void start(Context context) {
        if (started.compareAndSet(false, true)) {
            Logger.i("Start location service");
            int interval = AppConfig.getGpsRequestInterval(context) * 1000;

            locationListenerHolder = new TencentLocationListenerHolder();
            TencentLocationRequest request = TencentLocationRequest.create()
                    .setRequestLevel(TencentLocationRequest.REQUEST_LEVEL_GEO)
                    .setAllowCache(false)
                    .setInterval(interval);

            TencentLocationManager lbsManager = TencentLocationManager.getInstance(context);
            lbsManager.setCoordinateType(TencentLocationManager.COORDINATE_TYPE_WGS84);
            lbsManager.requestLocationUpdates(request, locationListenerHolder);
        }
    }

    /**
     * 停止定位服务
     */
    public static void stop(Context context) {
        if (started.compareAndSet(true, false)) {
            Logger.i("Stop location service");
            TencentLocationManager lbsManager = TencentLocationManager.getInstance(context);
            lbsManager.removeUpdates(locationListenerHolder);
        }
    }

    public static boolean isStarted() {
        return started.get();
    }

    public static void requestSingleLocation(Context context, int coordinateType, final ResultCallback<TencentLocation> callback) {
        TencentLocationManager lbsManager = TencentLocationManager.getInstance(context);
        TencentLocationRequest request = TencentLocationRequest.create()
                .setRequestLevel(TencentLocationRequest.REQUEST_LEVEL_GEO)
                .setAllowCache(false);
        lbsManager.requestSingleFreshLocation(request, new TencentLocationAdapter(context, coordinateType) {

            @Override
            public void onLocationChanged(TencentLocation location) {
                if (callback != null) {
                    callback.onResult(location);
                }
            }

            @Override
            public void onError(int error, String reason) {
                if (callback != null) {
                    callback.onError(new IllegalArgumentException(reason));
                }
            }
        }, Looper.myLooper());
    }

    public static void addLocationListener(TencentLocationListener listener) {
        if (locationListenerHolder != null) {
            locationListenerHolder.add(listener);
        }
    }

    public static void removeLocationListener(TencentLocationListener listener) {
        if (locationListenerHolder != null) {
            locationListenerHolder.remove(listener);
        }
    }

    /**
     * 获取当前location
     */
    public static TencentLocation getLastKnownLocation(Context context) {
        TencentLocationManager lbsManager = TencentLocationManager.getInstance(context);
        return lbsManager.getLastKnownLocation();
    }

    private static boolean isValid(TencentLocation location) {
        final double longitude = location.getLongitude();
        final double latitude = location.getLatitude();

        boolean isValidLatLong = longitude > 0 && latitude > 0;
        return isValidLatLong;
    }

    static class TencentLocationListenerHolder implements TencentLocationListener {
        final Set<TencentLocationListener> locationListenerCollection = new ArraySet<>();

        public void add(TencentLocationListener listener) {
            locationListenerCollection.add(listener);
        }

        public void remove(TencentLocationListener listener) {
            locationListenerCollection.remove(listener);
        }

        @Override
        public void onLocationChanged(TencentLocation location, int error, String reason) {
            for (TencentLocationListener listener : locationListenerCollection) {
                listener.onLocationChanged(location, error, reason);
            }
        }

        @Override
        public void onStatusUpdate(String status, int error, String reason) {
            for (TencentLocationListener listener : locationListenerCollection) {
                listener.onStatusUpdate(status, error, reason);
            }
        }
    }
}
