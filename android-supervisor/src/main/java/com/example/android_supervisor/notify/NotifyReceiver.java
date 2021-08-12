package com.example.android_supervisor.notify;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

/**
 * @author yangjie
 */
public abstract class NotifyReceiver extends BroadcastReceiver {

    @Override
    public final void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null && action.equals(Notifies.NOTIFY_ACTION)) {
            int type = intent.getIntExtra("type", Notifies.NOTIFY_TYPE_NONE);
            int status = intent.getIntExtra("status", Notifies.NOTIFY_STATUS_NONE);
            int count = intent.getIntExtra("count", 0);
            String refreshItem = intent.getStringExtra("refreshItem");
            if (!TextUtils.isEmpty(refreshItem)){
                onReceive(context, type, status, count,refreshItem);
            }else {
                onReceive(context, type, status, count);
            }

        }
    }

    public abstract void onReceive(Context context, int type, int status, int count);

    public void onReceive(Context context, int type, int status, int count,String refreshItem){

    }

}
