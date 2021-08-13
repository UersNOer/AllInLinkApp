package com.example.android_supervisor.notify;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

/**
 * @author wujie
 */
public class NotifyManager {

    public static void notify(Context context, int type, int status, int count) {
        Intent intent = getIntent(type, status, count);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public static void notify(Context context, int type, int status, int count,String groupCode ) {
        Intent intent = getIntent(type, status, count);
        intent.putExtra("refreshItem",groupCode);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public static void registerReceiver(Context context, NotifyReceiver receiver) {
        IntentFilter intentFilter = new IntentFilter(Notifies.NOTIFY_ACTION);
        LocalBroadcastManager.getInstance(context).registerReceiver(receiver, intentFilter);
    }

    public static void unregisterReceiver(Context context, NotifyReceiver receiver) {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(receiver);
    }

    private static Intent getIntent(int type, int status, int count) {
        Intent intent = new Intent(Notifies.NOTIFY_ACTION);
        intent.putExtra("type", type);
        intent.putExtra("status", status);
        intent.putExtra("count", count);
        return intent;
    }
}
