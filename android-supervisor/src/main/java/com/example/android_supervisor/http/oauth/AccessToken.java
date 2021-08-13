package com.example.android_supervisor.http.oauth;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.lang.ref.WeakReference;

/**
 * @author wujie
 */
public class AccessToken {
    private WeakReference<Context> mContext;
    private SharedPreferences sharedPrefs;

    public static AccessToken getInstance(Context context) {
        return new AccessToken(context);
    }

    AccessToken(Context context) {
        mContext = new WeakReference<>(context);
        sharedPrefs = context.getApplicationContext()
                .getSharedPreferences("user_session", Context.MODE_PRIVATE);
    }

    public boolean isSet() {
        return sharedPrefs.contains("access_token");
    }

    public boolean isExpired() {
        int expires = sharedPrefs.getInt("token_expires_in", 0);
        long createTime = sharedPrefs.getLong("token_create_time", 0L);
        return System.currentTimeMillis() - createTime > expires * 1000;
    }

    public String get() {
        return sharedPrefs.getString("access_token", null);
    }

    public String getSafety() {
//        if (isExpired()) {
//            Context context = mContext.get();
//            if (context != null) {
//                Intent intent = new Intent("com.example.android_supervisor.action.FORCE_REBOOT");
//                intent.setPackage(context.getPackageName());
//                context.sendBroadcast(intent);
//            }
//            throw new IllegalStateException();
//        } else {
            return get();
//        }
    }

    public String getRefreshToken() {
        return sharedPrefs.getString("refresh_token", null);
    }

    public long getToken_create_time() {
        return sharedPrefs.getLong("token_create_time", 0);
    }

    public void set(String accessToken ,String refresh_token) {
        Log.d("accessToken",accessToken);
        sharedPrefs.edit()
                .putString("access_token", accessToken)
                .putString("refresh_token", refresh_token)
//                .putInt("token_expires_in", expires)
                .putLong("token_create_time", System.currentTimeMillis())
                .apply();
    }

    public void delete() {
        sharedPrefs.edit()
                .remove("access_token")
                .remove("refresh_token")
//                .remove("token_expires_in")
                .remove("token_create_time")
                .apply();
    }
}
