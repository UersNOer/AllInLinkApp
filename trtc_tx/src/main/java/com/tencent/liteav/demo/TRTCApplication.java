package com.tencent.liteav.demo;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.multidex.MultiDexApplication;

import com.tencent.liteav.demo.trtc.debug.GenerateTestUserSig;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class TRTCApplication extends MultiDexApplication {

    @Override
    public void onCreate() {

        super.onCreate();

        closeAndroidPDialog();
//
        GenerateTestUserSig.SDKAPPID = getInt();
        GenerateTestUserSig.SECRETKEY = getString();
    }


	private void closeAndroidPDialog(){
        try {
            Class aClass = Class.forName("android.content.pm.PackageParser$Package");
            Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
            declaredConstructor.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
            declaredMethod.setAccessible(true);
            Object activityThread = declaredMethod.invoke(null);
            Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getString(){
        SharedPreferences sharedPreferences=getSharedPreferences("config,txt", Context.MODE_PRIVATE);
        return  sharedPreferences.getString("SECRETKEY", GenerateTestUserSig.SECRETKEY);
      //  return  sharedPreferences.getString("SECRETKEY", "e74a9843253420281614de5df35da018d3b912603be2b3df154c8bc8c4dfcb7d");
    }


    public int getInt(){
        SharedPreferences sharedPreferences=getSharedPreferences("config,txt", Context.MODE_PRIVATE);

        return  sharedPreferences.getInt("SDKAPPID", GenerateTestUserSig.SDKAPPID);

    }

}
