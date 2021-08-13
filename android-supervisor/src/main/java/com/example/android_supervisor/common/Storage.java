package com.example.android_supervisor.common;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * @author wujie
 */
public class Storage {

    /**
     * SD卡是否可用
     */
    public static boolean checkSDCardAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取工作目录
     */
    public static String getRootDir(Context context) {
        String rootDir;
        if (checkSDCardAvailable()) {
            File parent = Environment.getExternalStoragePublicDirectory("Topevery");
            File root = new File(parent, context.getPackageName());
            createDirectory(root);

            rootDir = root.getAbsolutePath();
        } else {
            rootDir = context.getFilesDir().getAbsolutePath();
        }
        return rootDir;
    }

    public static String getDataDir(Context context) {
        String parent = getRootDir(context);
        File data = new File(parent, "data");
        createDirectory(data);

        return data.getAbsolutePath();
    }

    public static String getMapDir(Context context) {
        String parent = getRootDir(context);
        File map = new File(parent, "map");
        createDirectory(map);

        return map.getAbsolutePath();
    }

    public static String getDownloadsDir(Context context) {
        String parent = getRootDir(context);
        File downloads = new File(parent, "downloads");
        createDirectory(downloads);

        return downloads.getAbsolutePath();
    }

    public static String getMediaDir(Context context) {
        String parent = getRootDir(context);
        File media = new File(parent, "media");
        createDirectory(media);

        return media.getAbsolutePath();
    }

    public static String getImageDir(Context context) {
        String parent = getMediaDir(context);
        File image = new File(parent, "image");
        createDirectory(image);

        return image.getAbsolutePath();
    }

    public static String getAudioDir(Context context) {
        String parent = getMediaDir(context);
        File audio = new File(parent, "audio");
        createDirectory(audio);

        return audio.getAbsolutePath();
    }

    public static String getVideoDir(Context context) {
        String parent = getMediaDir(context);
        File video = new File(parent, "video");
        createDirectory(video);

        return video.getAbsolutePath();
    }

    public static String getLogsDir(Context context) {
        String parent = getRootDir(context);
        File logs = new File(parent, "logs");
        createDirectory(logs);

        return logs.getAbsolutePath();
    }

    public static String getTempDir(Context context) {
        String parent = getRootDir(context);
        File temp = new File(parent, "temp");
        createDirectory(temp);

        return temp.getAbsolutePath();
    }

    public static String getUsersDir(Context context) {
        String parent = getRootDir(context);
        File downloads = new File(parent, "users");
        createDirectory(downloads);

        return downloads.getAbsolutePath();
    }

    public static String getUserDir(Context context) {
        String parent = getUsersDir(context);

        String userId = UserSession.getUserId(context);
        String dirName = userId;

        File user = new File(parent, dirName);
        createDirectory(user);

        return user.getAbsolutePath();
    }

    public static String getUserDataDir(Context context) {
        String parent = getUserDir(context);
        File data = new File(parent, "data");
        createDirectory(data);

        return data.getAbsolutePath();
    }

    private static boolean createDirectory(File directory) {
        boolean result = false;
        if (directory.exists()) {
            if (!directory.isDirectory()) {
                result = directory.delete();
                result = directory.mkdirs();
            }
        } else {
            result = directory.mkdirs();
        }
        return result;
    }
}
