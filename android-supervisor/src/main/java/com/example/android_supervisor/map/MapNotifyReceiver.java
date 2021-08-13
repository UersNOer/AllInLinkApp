package com.example.android_supervisor.map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.amap.api.fence.GeoFence;
import com.example.android_supervisor.entities.ConstantEntity;

/**
 * Created by dw on 2019/7/29.
 */
public class MapNotifyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ConstantEntity.GEOFENCE_BROADCAST_ACTION)) {
            //解析广播内容
            //获取Bundle
            Bundle bundle = intent.getExtras();
            //获取围栏行为：
            int status = bundle.getInt(GeoFence.BUNDLE_KEY_FENCESTATUS);
            //获取自定义的围栏标识：
            String customId = bundle.getString(GeoFence.BUNDLE_KEY_CUSTOMID);
            //获取围栏ID:
            String fenceId = bundle.getString(GeoFence.BUNDLE_KEY_FENCEID);
            //获取当前有触发的围栏对象：
            GeoFence fence = bundle.getParcelable(GeoFence.BUNDLE_KEY_FENCE);
        }
    }

}
