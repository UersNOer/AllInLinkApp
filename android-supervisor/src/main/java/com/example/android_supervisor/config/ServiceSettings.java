package com.example.android_supervisor.config;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author wujie
 */
public class ServiceSettings {
    private static final String PREF_NAME = "service_settings";

    private static final String DEFAULT_SCHEME = "https";
    private static final String DEFAULT_DOMAIN = "zhcg.cszhx.top";

    public static String getScheme(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("scheme", DEFAULT_SCHEME);
    }

    public static void setScheme(Context context, String scheme) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.putString("scheme", scheme);
        editor.apply();
    }

    public static String getDomain(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString("domain", DEFAULT_DOMAIN);
    }

    public static void setDomain(Context context, String domain) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.putString("domain", domain);
        editor.apply();
    }
}
