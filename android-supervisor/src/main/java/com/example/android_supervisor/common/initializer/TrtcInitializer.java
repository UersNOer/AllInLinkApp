package com.example.android_supervisor.common.initializer;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.example.android_supervisor.common.UserSession;
import com.example.android_supervisor.entities.Account;
import com.example.android_supervisor.entities.UserSigRes;
import com.example.android_supervisor.http.ServiceGenerator;
import com.example.android_supervisor.http.service.PublicService;
import com.tencent.liteav.demo.trtc.debug.GenerateTestUserSig;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;

/**
 * @author yj
 */
public class TrtcInitializer implements Initializer {

    private Context context;
    private Object secretkey;

    @Override
    public void initialize(Context context) {

        this.context = context;
        closeAndroidPDialog();//反射屏蔽谷歌限制

//        GenerateTestUserSig.SDKAPPID = getInt();
//        GenerateTestUserSig.SECRETKEY = getString();
        getSecretkey();
    }

    private void closeAndroidPDialog() {
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

    public String getString() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("config,txt", Context.MODE_PRIVATE);
        return sharedPreferences.getString("SECRETKEY", GenerateTestUserSig.SECRETKEY);

    }


    public int getInt() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("config,txt", Context.MODE_PRIVATE);

        return sharedPreferences.getInt("SDKAPPID", GenerateTestUserSig.SDKAPPID);

    }

    public void getSecretkey() {

        PublicService publicService = ServiceGenerator.create(PublicService.class);
        Observable<UserSigRes> observer1 = publicService.getUserAppId();
        observer1.observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<UserSigRes, Observable<UserSigRes>>() {
                    @Override
                    public Observable<UserSigRes> apply(UserSigRes userSigRes) throws Exception {

                        if (userSigRes != null) {
                            GenerateTestUserSig.SDKAPPID = Integer.valueOf(userSigRes.getData());
                            Log.d("yj", GenerateTestUserSig.SDKAPPID + "");
                        }
                        Account userSigPara = new Account();
                        userSigPara.setUserId(UserSession.getUserId(context));
                        userSigPara.setSdkAppId(GenerateTestUserSig.SDKAPPID);
                        return publicService.getUserSig(userSigPara);
                    }
                })
                .subscribe(new Observer<UserSigRes>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(UserSigRes userSigRes) {
                        if (userSigRes != null) {
                            GenerateTestUserSig.UserSig = userSigRes.getData();
                            Log.d("yj", TextUtils.isEmpty(GenerateTestUserSig.UserSig) ? "ss" : GenerateTestUserSig.UserSig);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


//        Account userSigPara1 = new Account();
//        userSigPara1.setUserId(UserSession.getUserId(context));
//        userSigPara1.setSdkAppId(GenerateTestUserSig.SDKAPPID);
//        PublicService publicService= ServiceGenerator.create(PublicService.class);
//        Call call = publicService.getUserSig(userSigPara1);
//        call.enqueue(new Callback<UserSigRes>() {
//            @Override
//            public void onResponse(Call<UserSigRes> call, Response<UserSigRes> response) {
//                if (response.isSuccessful()) {
//                    UserSigRes userSigRes = response.body();
//                    if (userSigRes != null) {
//                        GenerateTestUserSig.UserSig = userSigRes.getData();
//                        Log.d("yj", TextUtils.isEmpty(GenerateTestUserSig.UserSig)?"ss":GenerateTestUserSig.UserSig);
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<UserSigRes> call, Throwable t) {
//                Log.d("yj",t.getMessage());
//            }
//        });
    }
}
