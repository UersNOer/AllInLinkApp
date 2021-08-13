package com.example.android_supervisor.ui.media;

import android.content.Context;
import android.content.Intent;

import com.example.android_supervisor.ui.CameraActivity;

/**
 * Created by yj on 2019/9/5.
 */
public class CameraUtils {
    static CameraCompleted cameraCompleted = null;

    public static void camera(Context context, CameraCompleted cameraCompleted, int handType)
    {
        camera(context, cameraCompleted);
    }

    public static void camera(Context context, CameraCompleted cameraCompleted)
    {
        if(context != null)
        {

            CameraUtils.cameraCompleted = cameraCompleted;
            Intent intent = new Intent();
            intent.setClass(context, CameraActivity.class);
            // intent.setClass(context, Camera2.class);
            context.startActivity(intent);

        }
    }

    public static void cameraCompleted(MediaInfo mediaInfo)
    {
        if(cameraCompleted != null)
        {
            cameraCompleted.completed(mediaInfo);
            cameraCompleted = null;
        }
    }


    public interface CameraCompleted
    {
        void completed(MediaInfo mediaInfo);
    }
}
