package com.example.android_supervisor.utils;

import android.content.Context;


public class LoginMethodConfig {

    private static final String LOGIN_PHONE = "login_phone";
    public static final String LOGIN_FACE="login_face";

    public static final String LOGIN_FINGER="login_finger";

    public static void setLoginFace(Context context, boolean face) {
        LocalSaveUtils.saveBoolean(context, LOGIN_FACE, face);
    }

    public static boolean getLoginFace(Context context) {
        return LocalSaveUtils.getBoolean(context, LOGIN_FACE, false);
    }


    public static void setLoginPhone(Context context, boolean phone) {
        LocalSaveUtils.saveBoolean(context, LOGIN_PHONE, phone);
    }

    public static boolean getLoginPhone(Context context) {
        return  LocalSaveUtils.getBoolean(context, LOGIN_PHONE, false);
    }

    public static void setLoginFinger(Context context, boolean finger) {
        LocalSaveUtils.saveBoolean(context, LOGIN_FINGER, finger);
    }

    public static boolean getLoginFigger(Context context) {
        return  LocalSaveUtils.getBoolean(context, LOGIN_FINGER, true);
    }


}
