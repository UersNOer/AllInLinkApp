package com.example.android_supervisor.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;

import com.tangxiaolv.annotations.RouterPath;
import com.tencent.bugly.beta.Beta;
import com.example.android_supervisor.R;
import com.example.android_supervisor.common.UserSession;
import com.example.android_supervisor.http.ServiceGenerator;
import com.example.android_supervisor.utils.Environments;
import com.example.android_supervisor.utils.OauthHostManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import butterknife.ButterKnife;

;

/**
 * 闪屏界面
 *
 */
public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        Beta.checkUpgrade();
        checkConfig();
//        sHA1(this);//发布版

    }
    public static String sHA1(Context context){
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            String result = hexString.toString();
            return result.substring(0, result.length()-1);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 检查配置
     */
    private void checkConfig() {
        if (Environments.isDeBug()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(LoginActivity.class);
                }
            }, 0);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!UserSession.getGuideStatus(SplashActivity.this)){
                        startActivity(GuideActivity.class);
                    }else {
                        checkIsOauth();
                    }
                }
            }, 0);

        }
    }

    /**
     * 检查是否已经获得授权
     */
    private void checkIsOauth() {
        if (OauthHostManager.getInstance(this).getConfigStatus()) {
            // 授权成功->登录页面
            startActivity(LoginActivity.class);
        } else {
            // 授权失败->授权页面
            startActivity(OauthCodeActivity.class);
        }
    }

    /**
     * 清空別的code緩存
     */
//    private void clearOtherCodeCache() {
//        Map<Integer, Boolean> chooseList = new HashMap<>();
//        chooseList.put(DataCacheCleaner.DATA_COMMON, true);
//        chooseList.put(DataCacheCleaner.DATA_USER, true);
//        chooseList.put(DataCacheCleaner.DATA_MEDIA, true);
//        chooseList.put(DataCacheCleaner.DATA_DOWNLOAD, true);
//        clearCache(SplashActivity.this, chooseList);
//    }

//    private void clearCache(final Context context, final Map<Integer, Boolean> chooseList) {
//        Observable.create(new ObservableOnSubscribe<Long>() {
//            @Override
//            public void subscribe(ObservableEmitter<Long> emitter) throws Exception {
//                DataCacheCleaner.clear(context, chooseList);
//                long cacheSize = DataCacheCleaner.getCacheSize(context);
//                emitter.onNext(cacheSize);
//                emitter.onComplete();
//            }
//        }).compose(this.<Long>bindToLifecycle())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .compose(new ProgressTransformer<Long>(context, ProgressText.load))
//                .subscribe(new Consumer<Long>() {
//                    @Override
//                    public void accept(Long cacheSize) throws Exception {
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//
//                    }
//                });
//    }
    @Override
    protected boolean isEnableAppLock() {
        return false;
    }

    private void startActivity(Class activity) {
        //配置Url，登录
        ServiceGenerator.initialize(this);
        Intent intent = new Intent(this, activity);
        startActivity(intent);
        finish();
    }
}
