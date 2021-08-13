package com.example.android_supervisor.service;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.orhanobut.logger.Logger;
import com.example.android_supervisor.attendance.AttendanceManager;
import com.example.android_supervisor.config.AppConfig;
import com.example.android_supervisor.entities.GpsPoint;
import com.example.android_supervisor.jt808.GpsEchoClient;
import com.example.android_supervisor.map.MapLocationAPI;
import com.example.android_supervisor.notify.Notifies;
import com.example.android_supervisor.notify.NotifyManager;
import com.example.android_supervisor.utils.CoordinateUtils;
import com.example.android_supervisor.utils.DevLocalHostManager;
import com.example.android_supervisor.utils.Environments;
import com.example.android_supervisor.utils.LogUtils;
import com.example.android_supervisor.utils.OauthHostManager;
import com.example.android_supervisor.utils.ToastUtils;

import java.util.ArrayDeque;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SendGpsThread implements ScheduledRunnable, AMapLocationListener, MapLocationAPI.MapLocationListener {
    private Context mContext;
    private GpsEchoClient gpsClient;
    private  ArrayDeque<GpsPoint> mGpsPointDeque;
//    private List<MapGrid> mMapGridList;

    public SendGpsThread(Context context) {

        try{
            mContext = context;
            mGpsPointDeque = new ArrayDeque<GpsPoint>();

            // 消息服务器的地址格式：https://zhcg.cszhx.top:30113
            String hostAddress = "";
            String domain = "";
            int port = 443;

            if (!Environments.isDeBug()) {// 测试环境或正式环境
                hostAddress = OauthHostManager.getInstance(context).getGpsConfig();
            } else {// 本地开发环境
                Map<String, String> map = DevLocalHostManager.getHashMapData(mContext, DevLocalHostManager.KEY_DEV_LOCALHOST_SERVER_CONFIG);
                hostAddress = map.get(DevLocalHostManager.KEY_GPS);
//            hostAddress = "https://tykj.cszhx.top";
            }

            String[] hostArray = cutHostAdress(hostAddress);
            if (hostArray == null || hostArray.length != 2) {
                // throw new RuntimeException("消息服务器的地址协议错误");
            }

            domain = hostArray[0];
            port = Integer.valueOf(hostArray[1]);
            LogUtils.e("domain = " + domain + ", port = " + port);
            gpsClient = new GpsEchoClient(mContext, domain, port);
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    /**
     * 根据完整的服务器地址 拆分 ip(域名)  端口
     */
    private String[] cutHostAdress(String hostAddress) {
        String domain = "";
        String port = "443";

        if (TextUtils.isEmpty(hostAddress) || ((!hostAddress.startsWith("http://")) && (!hostAddress.startsWith("https://")))) {

            ToastUtils.show(mContext,"gps服务器的地址协议错误");
        }

        int portHost = hostAddress.startsWith("https://") ? 443 : hostAddress.startsWith("http://") ? 80 : -1;

        if (portHost == -1) {
            ToastUtils.show(mContext,"gps服务器的地址协议错误");
        }

        hostAddress = hostAddress.replace("http://", "").replace("https://", "");

        LogUtils.e(hostAddress);

        if (!TextUtils.isEmpty(hostAddress)) {
            //zhcg.cszhx.top:30113
            //zhcg.cszhx.top
            String[] split = hostAddress.split(":");
            if (split != null && split.length == 2) {
                domain = split[0];
                port = String.valueOf(split[1]);
            } else {
                domain = hostAddress;
                port = String.valueOf(portHost);
            }
        } else {
            ToastUtils.show(mContext,"gps服务器的地址不正确！");
        }

        String[] ipCongif = {domain, port};
        return ipCongif;
    }

    public void putGpsPointToDeque(GpsPoint gpsPoint) {
        synchronized (mGpsPointDeque) {
            mGpsPointDeque.offer(gpsPoint);
        }
    }

    public GpsPoint getGpsPointFromDeque() {
        synchronized (mGpsPointDeque) {
            return mGpsPointDeque.poll();
        }
    }

    @Override
    public void run() {
        GpsPoint gpsPoint;

        while ((gpsPoint = getGpsPointFromDeque()) != null) {
            LogUtils.d("gps正在发送..."+gpsPoint.getLongitude() +"  "+gpsPoint.getLatitude());
            gpsClient.sendGpsPoint(gpsPoint.getLatitude(), gpsPoint.getLongitude(),
                    gpsPoint.getElevation(), gpsPoint.getSpeed(),
                    gpsPoint.getDirection(), gpsPoint.getCoordType(),
                    gpsPoint.getProvider(), true,
                    gpsPoint.getTime());
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //触发心跳
                gpsClient.heartBeat();
            }
        },1000 * 60);
    }

    @Override
    public void start(ScheduledExecutorService service) {

        int interval = Math.max(AppConfig.getGpsSendInterval(mContext), 5);

        Log.d("socket-io", "Start gps thread" +interval);

        service.scheduleWithFixedDelay(this,
                5, interval, TimeUnit.SECONDS);

        MapLocationAPI.start();
        MapLocationAPI.addMapLocationListener(this);

        gpsClient.connect();
    }

    @Override
    public void stop(ScheduledExecutorService service) {
        Log.d("socket-io", "Stop gps thread");
        MapLocationAPI.stop();
        gpsClient.disconnect();
    }


    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {


        if (aMapLocation.getErrorCode() != AMapLocation.LOCATION_SUCCESS || aMapLocation.getLocationType() == 6 || aMapLocation.getAccuracy()>300) {
            Logger.e("Get location Failed: code=%d, reason=%s", aMapLocation.getErrorCode(), aMapLocation.getErrorInfo());
            NotifyManager.notify(mContext, Notifies.NOTIFY_TYPE_GPS_COLLECT, Notifies.NOTIFY_STATUS_OFFLINE, 0);
            return;
        }
        if(gpsClient.isDisconnected()){
            gpsClient.connect();
        }

        double[] latLng84  =  CoordinateUtils.gcj02ToWGS84( aMapLocation.getLongitude(), aMapLocation.getLatitude());
//        double latitude84,longitude84;

        GpsPoint gpsPoint = new GpsPoint();
        gpsPoint.setLatitude((float) latLng84[1]);
        gpsPoint.setLongitude((float)latLng84[0]);
        gpsPoint.setElevation((int) aMapLocation.getAltitude());
        gpsPoint.setSpeed(aMapLocation.getSpeed());
        gpsPoint.setDirection((int) aMapLocation.getBearing());
        gpsPoint.setAccuracy(aMapLocation.getAccuracy());
        if (aMapLocation.getCoordType().equals(AMapLocation.COORD_TYPE_WGS84))
            gpsPoint.setCoordType(0);
        else
            gpsPoint.setCoordType(1);
//        gpsPoint.setTime(new Date(aMapLocation.getTime()));
        gpsPoint.setTime(new Date());
        if (aMapLocation.getProvider() != null) {
            if (aMapLocation.getProvider().equals("lbs")) {
                gpsPoint.setProvider(2);
            }
        }
        NotifyManager.notify(mContext, Notifies.NOTIFY_TYPE_GPS_COLLECT, Notifies.NOTIFY_STATUS_ONLINE, 0);
        putGpsPointToDeque(gpsPoint);
        LogUtils.d("gps正在收集..."+gpsPoint);
        AttendanceManager.getInstance(mContext).autuClock(gpsPoint);

    }

    @Override
    public void onMapReceiveLocation(AMapLocation location) {
        onLocationChanged(location);
    }



    //纠差计算
    public boolean correctLocation(GpsPoint lastGpsPoint,GpsPoint gpsPoint){

        float distance = AMapUtils.calculateLineDistance(new LatLng(lastGpsPoint.getLatitude(),lastGpsPoint.getLongitude()),
                new LatLng(gpsPoint.getLatitude(),gpsPoint.getLongitude()));


        if (Math.abs(gpsPoint.getSpeed()-lastGpsPoint.getSpeed()) < 3 && distance> 50){
            //此点无效
            return false;
        }
        return true;
    }


}
