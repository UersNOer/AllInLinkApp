package com.example.android_supervisor.common.initializer;

import android.content.Context;

import com.example.android_supervisor.http.OkHttpClientFactory;
import com.squareup.picasso.Downloader;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

/**
 * @author wujie
 */
public class PicassoInitializer implements Initializer {

    public void initialize(Context context) {
        Downloader downloader = new OkHttp3Downloader(
                OkHttpClientFactory.createSSLHttpClient());

        Picasso picasso = new Picasso.Builder(context)
                .downloader(downloader)
                .build();

        Picasso.setSingletonInstance(picasso);
    }
}
