package com.example.android_supervisor.common.initializer;

import android.content.Context;


import com.example.android_supervisor.http.OkHttpClientFactory;

import io.socket.client.IO;
import okhttp3.OkHttpClient;

/**
 * @author wujie
 */
public class SocketIOInitializer implements Initializer {

    @Override
    public void initialize(Context context) {
        OkHttpClient okHttpClient = OkHttpClientFactory.createSSLHttpClient();
        IO.setDefaultOkHttpCallFactory(okHttpClient);
        IO.setDefaultOkHttpWebSocketFactory(okHttpClient);
    }
}
