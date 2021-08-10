package com.allinlink.platformapp.video_project.utils;

import android.util.Log;

public class ButtonUtils {
    private static long lastClickTime_Button = 0;
    private static long DIFF = 1000;
    private static int lastButtonId = -1;
    private static long lastClickTime = 0;

    private static int lastKeyCode = -1;

    /**
     * 判断两次点击的间隔，如果小于1000，则认为是多次无效点击
     *
     * @return
     */
    public static boolean isFastDoubleClick_Button() {
        return isFastDoubleClick_Button(lastButtonId, DIFF);
    }

    public static boolean isFastDoublePress_Key() {
        return isFastDoubleClick_Key(lastKeyCode, DIFF);
    }

    /**
     * 判断两次点击的间隔，如果小于1000，则认为是多次无效点击
     *
     * @return
     */
    public static boolean isFastDoubleClick_Button(int buttonId) {
        return isFastDoubleClick_Button(buttonId, DIFF);
    }

    public static boolean isFastDoublePress_Key(int keycode) {
        return isFastDoubleClick_Key(keycode, DIFF);
    }

    /**
     * 判断两次点击的间隔，如果小于diff，则认为是多次无效点击
     *
     * @param diff
     * @return
     */
    public static boolean isFastDoubleClick_Button(int buttonId, long diff) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (lastButtonId == buttonId && lastClickTime > 0 && timeD < diff) {
            Log.v("isFastDoubleClick", "短时间内按钮多次触发");
            return true;
        }
        lastClickTime = time;
        lastButtonId = buttonId;
        return false;
    }

    public static boolean isFastDoubleClick_Key(int keycode, long diff) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (lastButtonId == keycode && lastClickTime > 0 && timeD < diff) {
            Log.v("isFastDoubleClick", "短时间内按钮多次触发");
            return true;
        }
        lastClickTime = time;
        lastButtonId = keycode;
        return false;
    }
}
