package com.example.android_supervisor.utils;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.example.android_supervisor.common.UserSession;

/**
 * Created by yj on 2019/8/6. 疯子
 */
public class FileServerUtils {

    public static Uri getUri(Context context,String attachUrl, Uri coverUri) {
        if (!attachUrl.contains("http")) {
            if (!TextUtils.isEmpty(UserSession.getFileServer(context))){
                String path = new StringBuilder(UserSession.getFileServer(context)).append(attachUrl).toString();
                coverUri = Uri.parse(path);
            }
        }
        return coverUri;
    }

    public static String getPath(Context context,String url) {
        String fileServer = UserSession.getFileServer(context);
        if (!TextUtils.isEmpty(fileServer)){
            String path=new StringBuilder(fileServer).append(url).toString();
            return path;
        }
        return "";
    }


    public static String getUrl(Context context,String attachUrl) {
        String path =attachUrl;
        if (!attachUrl.contains("http")) {
            String fileServer = UserSession.getFileServer(context);
            if (!TextUtils.isEmpty(fileServer)){
                path = new StringBuilder(fileServer).append(attachUrl).toString();
            }
        }
        return path;
    }
}
