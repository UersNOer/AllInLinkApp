package com.example.android_supervisor.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;

import com.example.android_supervisor.config.AppConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 * 开发环境下本地服务器读写配置类
 */
public class DevLocalHostManager {
    private static final String DEV_LOCAL_HOST_MANAGER_CONFIG_FILE_NAME = "DevLocalHostManagerConfig";

    public static final String KEY_DEV_LOCALHOST_SERVER_CONFIG = "key_dev_localhost_server_config";

    public static final String KEY_OAUTH = "oauth";
    public static final String KEY_PLATFORM = "platform";
    public static final String KEY_DIGITAL_BASIC = "digital-basic";
    public static final String KEY_DIGITAL_QUESTION = "digital-question";
    public static final String KEY_DIGITAL_PUBLICS = "digital-publics";
    public static final String KEY_FILES = "files";
    public static final String KEY_MSG = "msg";
    public static final String KEY_GPS = "gps";
    public static final String KEY_LOGIN = "login";

    /**
     * 保存集合
     *
     * @param context
     * @param key
     * @param map
     */
    public static void putHashMapData(Context context, String key, Map<String, String> map) {
        JSONArray mJsonArray = new JSONArray();
        Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();

        JSONObject object = new JSONObject();

        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            try {
                object.put(entry.getKey(), entry.getValue());
            } catch (JSONException e) {
                LogUtils.e(e.getMessage());
            }
        }
        mJsonArray.put(object);
        SharedPreferences sp = context.getSharedPreferences(DEV_LOCAL_HOST_MANAGER_CONFIG_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, mJsonArray.toString());
        editor.commit();
    }

    public static Map<String, String> getHashMapData(Context context, String key) {
        Map<String, String> map = new HashMap<>();
        SharedPreferences sp = context.getSharedPreferences(DEV_LOCAL_HOST_MANAGER_CONFIG_FILE_NAME, Context.MODE_PRIVATE);
        String result = sp.getString(key, "");
        try {
            JSONArray array = new JSONArray(result);
            for (int i = 0; i < array.length(); i++) {
                JSONObject itemObject = array.getJSONObject(i);
                JSONArray names = itemObject.names();
                if (names != null) {
                    for (int j = 0; j < names.length(); j++) {
                        String name = names.getString(j);
                        String value = itemObject.getString(name);
                        map.put(name, value);
                    }
                }
            }
        } catch (JSONException e) {
            LogUtils.e(e.getMessage());
        }
        return map;
    }

    public static Map<String, String> loadDevLocalHostServerConfig(Context context) throws IOException {
        Map<String, String> hashMap = new HashMap<>();
        AssetManager assetManager = context.getAssets();
        InputStream stream = assetManager.open(AppConfig.CONFIG_NAME);
        Properties properties = new Properties();
        properties.load(stream);

        String oauth_name = properties.getProperty("service.oauth.name");
        String platform_name = properties.getProperty("service.platform.name");
        String basic_name = properties.getProperty("service.digital.basic.name");
        String question_name = properties.getProperty("service.digital.question.name");
        String publics_name = properties.getProperty("service.digital.publics.name");
        String files_name = properties.getProperty("service.files.name");
        String gps_name = properties.getProperty("service.gps.name");
        String msg_name = properties.getProperty("service.msg.name");

        String oauth_domain = properties.getProperty("service.oauth.domain");
        String platform_domain = properties.getProperty("service.platform.domain");
        String basic_domain = properties.getProperty("service.digital.basic.domain");
        String question_domain = properties.getProperty("service.digital.question.domain");
        String publics_domain = properties.getProperty("service.digital.publics.domain");
        String files_domain = properties.getProperty("service.files.domain");
        String gps_domain = properties.getProperty("service.gps.domain");
        String msg_domain = properties.getProperty("service.msg.domain");

        hashMap.put(oauth_name, oauth_domain);
        hashMap.put(platform_name, platform_domain);
        hashMap.put(basic_name, basic_domain);
        hashMap.put(question_name, question_domain);
        hashMap.put(publics_name, publics_domain);
        hashMap.put(files_name, files_domain);
        hashMap.put(gps_name, gps_domain);
        hashMap.put(msg_name, msg_domain);

        return hashMap;
    }
}
