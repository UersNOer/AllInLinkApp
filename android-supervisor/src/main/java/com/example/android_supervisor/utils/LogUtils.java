package com.example.android_supervisor.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.orhanobut.logger.Logger;

/**
 * 对 Logger 进行了简单的封装，如果想替换的话，直接将此文件中的 Logger 替换掉即可
 * <p>
 * 另外，在 AppContext 类中对 LogInitializer 类进行了初始化操作
 */
public class LogUtils {

    public static void log(int priority, @Nullable String tag, @Nullable String message, @Nullable Throwable throwable) {
        Logger.log(priority, tag, message, throwable);
    }

    public static void d(@NonNull String message, @Nullable Object... args) {
        Logger.d(message, args);
    }

    public static void d(@Nullable Object object) {
        Logger.d(object);
    }

    public static void e(@NonNull String message, @Nullable Object... args) {
        Logger.e(null, message, args);
    }

    public static void e(@Nullable Throwable throwable, @NonNull String message, @Nullable Object... args) {
        Logger.e(throwable, message, args);
    }

    public static void i(@NonNull String message, @Nullable Object... args) {
        Logger.i(message, args);
    }

    public static void v(@NonNull String message, @Nullable Object... args) {
        Logger.v(message, args);
    }

    public static void w(@NonNull String message, @Nullable Object... args) {
        Logger.w(message, args);
    }

    public static void wtf(@NonNull String message, @Nullable Object... args) {
        Logger.wtf(message, args);
    }

    public static void json(@Nullable String json) {
        Logger.json(json);
    }

    public static void xml(@Nullable String xml) {
        Logger.xml(xml);
    }
}
