package com.example.android_supervisor.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;

/**
 * @author wujie
 */
public class JsonUtils {
    public static <T> T fromJson(String json, Class<T> classOfT) {
        try {
            return getGson().fromJson(json, classOfT);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T fromJsonBuffer(byte[] bytes, Class<T> classOfT) {
        String json;
        try {
            json = new String(bytes, "utf-8");
        } catch (UnsupportedEncodingException e) {
            json = new String(bytes);
        }
        return fromJson(json, classOfT);
    }

    public static String toJson(Object object) {
        return getGson().toJson(object);
    }

    public static byte[] toJsonBuffer(Object object) {
        String json = toJson(object);
        byte[] bytes;
        try {
            bytes = json.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            bytes = json.getBytes();
        }
        return bytes;
    }

    private static Gson getGson() {
        return new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
    }
}

