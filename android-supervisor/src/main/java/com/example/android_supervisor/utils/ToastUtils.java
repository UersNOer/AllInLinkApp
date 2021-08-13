package com.example.android_supervisor.utils;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import android.widget.Toast;

/**
 * @author wujie
 */
public class ToastUtils {

    private ToastUtils() {
        throw new AssertionError();
    }

    public static void show(@NonNull Context context, @StringRes int resId) {
        Toast.makeText(context.getApplicationContext(), resId, Toast.LENGTH_SHORT).show();
    }

    public static void show(@NonNull Context context, String text) {
        Toast.makeText(context.getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    public static void show(@NonNull Context context, @StringRes int resId, int duration) {
        Toast.makeText(context.getApplicationContext(), resId, duration).show();
    }

    public static void show(@NonNull Context context, String text, int duration) {
        Toast.makeText(context.getApplicationContext(), text, duration).show();
    }
}
