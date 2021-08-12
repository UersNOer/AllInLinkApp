package com.example.android_supervisor.cache;

import android.content.Context;


import com.example.android_supervisor.common.Storage;
import com.example.android_supervisor.sqlite.PrimarySqliteHelper;
import com.example.android_supervisor.sqlite.PublicSqliteHelper;
import com.example.android_supervisor.ui.media.MediaUtils;

import java.io.File;
import java.util.Map;

public class DataCacheCleaner {

    public static final int DATA_COMMON = 1;//公共数据
    public static final int DATA_USER = 2;//用户数据（消息、暂存案件）
    public static final int DATA_MEDIA = 3;//媒体文件（图片、视频等）
    public static final int DATA_DOWNLOAD = 4;//下载目录

//    public enum DataCacheType {
//        DATA_COMMON(1, "公共数据"), DATA_USER(2, "用户数据"), DATA_MEDIA(3, "媒体文件"), DATA_DOWNLOAD(4, "下载目录");
//
//        private int index;
//        private String desc;
//
//        private DataCacheType(int index, String desc) {
//            this.index = index;
//            this.desc = desc;
//        }
//    }

    /**
     * 清除数据
     * <p>
     * 1、公共数据
     * 2、用户数据（消息、暂存案件）
     * 3、媒体文件（图片、视频等）
     * 4、下载目录
     */
    public static void clear(Context context, Map<Integer, Boolean> chooseList) {
        DataCacheManager.releaseMemoryCache();

        String logsDir = Storage.getLogsDir(context);
        clearFolder(logsDir, false);

        if (chooseList.get(DataCacheCleaner.DATA_COMMON)) {
            String dataDir = Storage.getDataDir(context);
            clearFolder(dataDir, false);
            PublicSqliteHelper.getInstance(context).close();
        }
        if (chooseList.get(DataCacheCleaner.DATA_USER)) {
            String userDir = Storage.getUserDir(context);
            clearFolder(userDir, false);
            PrimarySqliteHelper.getInstance(context).close();
        }
        if (chooseList.get(DataCacheCleaner.DATA_MEDIA)) {
            String mediaDir = Storage.getMediaDir(context);
            clearFolder(mediaDir, false);

            MediaUtils.clearMediaListWithImage(context, mediaDir);
            MediaUtils.clearMediaListWithAudio(context, mediaDir);
            MediaUtils.clearMediaListWithVideo(context, mediaDir);
        }
        if (chooseList.get(DataCacheCleaner.DATA_DOWNLOAD)) {
            String downloadsDir = Storage.getDownloadsDir(context);
            clearFolder(downloadsDir, false);
        }
    }

    /**
     * 清除文件夹
     *
     * @param path    文件夹路径
     * @param delSelf 是否删除文件夹本身
     */
    public static void clearFolder(String path, boolean delSelf) {
        File root = new File(path);
        if (root.exists()) {
            if (root.isDirectory()) {
                File[] files = root.listFiles();
                for (File file : files) {
                    if (file.isDirectory()) {
                        clearFolder(file.getAbsolutePath(), true);
                    } else {
                        file.delete();
                    }
                }
            }
            if (delSelf) {
                root.delete();
            }
        }
    }

    public static long getCacheSize(Context context) {
        long blockSize = 0;

        String dataDir = Storage.getDataDir(context);
        blockSize += getFolderSize(dataDir);

        String userDir = Storage.getUserDir(context);
        blockSize += getFolderSize(userDir);

        String mediaDir = Storage.getMediaDir(context);
        blockSize += getFolderSize(mediaDir);

        String downloadsDir = Storage.getDownloadsDir(context);
        blockSize += getFolderSize(downloadsDir);

        return blockSize;
    }

    private static long getFolderSize(String path) {
        long size = 0;
        File root = new File(path);
        if (root.exists()) {
            if (root.isDirectory()) {
                File[] files = root.listFiles();
                for (File file : files) {
                    if (file.isDirectory()) {
                        size += getFolderSize(file.getAbsolutePath());
                    } else {
                        size += file.length();
                    }
                }
            }
        }
        return size;
    }
}
