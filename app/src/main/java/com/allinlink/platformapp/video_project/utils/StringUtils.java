package com.allinlink.platformapp.video_project.utils;

import android.text.TextUtils;
import android.widget.TextView;

public class StringUtils {

    public static boolean txtCheckEmpty(TextView txNav) {
        String txt = txNav.getText().toString().trim();
        if (txt == null) {
            return true;
        }
        if (TextUtils.isEmpty(txt)) {
            return true;
        }
        return false;
    }

    public static boolean txtCheckEmpty(String txt) {
        if (txt == null) {
            return true;
        }
        if (TextUtils.isEmpty(txt)) {
            return true;
        }
        return false;
    }

}
