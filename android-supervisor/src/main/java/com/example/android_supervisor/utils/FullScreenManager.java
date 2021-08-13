package com.example.android_supervisor.utils;

import android.content.Context;
import android.content.res.Resources;

/**
 * Created by Administrator on 2019/9/30.
 */
public class FullScreenManager {

    public static int getNavigationBarHeight(Context context) {
        Resources resources =  context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height","dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }
}
