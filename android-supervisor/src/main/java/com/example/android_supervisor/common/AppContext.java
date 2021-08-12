package com.example.android_supervisor.common;

import android.app.Activity;


import androidx.multidex.MultiDexApplication;

import com.arcsoft.arcfacedemo.common.FaceMangaer;
import com.example.android_supervisor.common.initializer.BaiduOCRInitializer;
import com.example.android_supervisor.common.initializer.BuglyInitializer;
import com.example.android_supervisor.common.initializer.LogInitializer;
import com.example.android_supervisor.common.initializer.PicassoInitializer;
import com.example.android_supervisor.common.initializer.SocketIOInitializer;
import com.example.android_supervisor.config.AppConfig;
import com.example.android_supervisor.http.ServiceGenerator;
import com.xuexiang.xui.XUI;

import java.util.ArrayList;
import java.util.List;

//import com.example.android_supervisor.common.initializer.EaseUIInitializer;

/**
 * @author wujie
 */
public class AppContext extends MultiDexApplication {

    private static AppContext instance ;


    public static AppContext getInstance() {
        return instance;
    }


    private List<Activity> mActivityList = new ArrayList<>();


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        // 下面的初始化顺序不能乱！
        new LogInitializer().initialize(this);
        new BuglyInitializer().initialize(this);
        new SocketIOInitializer().initialize(this);
        new PicassoInitializer().initialize(this);
        new BaiduOCRInitializer().initialize(this);

//        EaseUIInitializer.getInstance().initialize(this);
        AppConfig.initialized(this);
        ServiceGenerator.initialize(this);

        FaceMangaer.getInstance(this).activeEngine();

        XUI.init(this);
        XUI.debug(true);  //开启UI框架调试日志

    }



    public void pushActivity(Activity activity) {
        mActivityList.add(activity);
    }

    public boolean removeActivity(Activity activity) {
        return mActivityList.remove(activity);
    }

    public void exit() {
        for (int i = 0, s = mActivityList.size(); i < s; i++) {
            Activity activity = mActivityList.get(i);
            activity.finish();
        }
        mActivityList.clear();
    }
}
