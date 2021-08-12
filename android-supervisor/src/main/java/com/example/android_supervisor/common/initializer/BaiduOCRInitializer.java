package com.example.android_supervisor.common.initializer;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;

/**
 * @author wujie
 */
public class BaiduOCRInitializer implements Initializer {

    @Override
    public void initialize(Context context) {
        String apiKey = "";
        String secretKey = "";
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            apiKey = appInfo.metaData.getString("com.baidu.ocr.sdk.API_KEY");
            secretKey = appInfo.metaData.getString("com.baidu.ocr.sdk.SECRET_KEY");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        OCR.getInstance(context).initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken accessToken) {
                // Todo
            }

            @Override
            public void onError(OCRError ocrError) {
                // Todo
            }
        }, context, apiKey, secretKey);
    }
}
