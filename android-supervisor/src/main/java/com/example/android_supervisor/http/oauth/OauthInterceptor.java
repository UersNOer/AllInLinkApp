package com.example.android_supervisor.http.oauth;

import android.util.Log;


import androidx.annotation.NonNull;

import com.example.android_supervisor.config.AppConfig;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public final class OauthInterceptor implements Interceptor {
    private final AccessToken accessToken;

    public OauthInterceptor(AccessToken accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        String url = chain.request().url().toString();
        if (accessToken.isSet()) {
            try {
                String token = accessToken.getSafety();
                Log.d("token", "intercept: "+token);
                if (url.contains("oauth/getUserInfo")) {
                    builder.header("Authorization", "Bearer " + token);
                }
//                else if (url.contains("whSys") ){
//                    builder.header("Authorization", "Bearer " + "9aa72a30-215d-4f7d-8292-aec24646f732");//假token
//                }
                 if (!isIgnoreAuthUrl(url)) {
                    builder.header("Authorization", "Bearer " + token);
                }

            } catch (IllegalStateException e) {
            }
        }

        return chain.proceed(builder.build());
    }

    static boolean isIgnoreAuthUrl(String url) {
        List<String> ignoreAuthPatterns = AppConfig.getIgnoreAuthPatterns();
        for (String pattern : ignoreAuthPatterns) {
            if (matchUrlPattern(pattern, url)) {
                return true;
            }
        }
        return false;
    }

    static boolean matchUrlPattern(String pattern, String url) {
        int urlLength = url.length();
        int urlIndex = 0;
        char ch;
        for (int patternIndex = 0, patternLength = pattern.length(); patternIndex < patternLength; patternIndex++) {
            ch = pattern.charAt(patternIndex);
            if (ch == '*') {
                while (urlIndex < urlLength) {
                    if (matchUrlPattern(pattern.substring(patternIndex + 1), url.substring(urlIndex))) {
                        return true;
                    }
                    urlIndex++;
                }
            } else {
                if ((urlIndex >= urlLength) || (ch != url.charAt(urlIndex))) {
                    return false;
                }
                urlIndex++;
            }
        }
        return (urlIndex == urlLength);
    }
}
