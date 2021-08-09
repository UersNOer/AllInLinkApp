package com.allinlink.platformapp.video_project.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.unistrong.network.FileUtil;
import com.allinlink.platformapp.video_project.config.Configs;
import com.allinlink.platformapp.video_project.service.VideoApi;

import org.android.agoo.common.CallBack;
import org.android.agoo.common.Config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.HashMap;

import okhttp3.ResponseBody;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 缓存处理工具类
 *
 * @author ltd
 */

public class CacheUtil {
    /**
     * 获取缓存大小
     *
     * @param context
     * @return
     * @throws Exception
     */
    public static String getTotalCacheSize(Context context) throws Exception {
        long cacheSize = getFolderSize(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cacheSize += getFolderSize(context.getExternalCacheDir());
        }
        return getFormatSize(cacheSize);
    }

    /***
     * 清理所有缓存
     * @param context
     */
    public static void clearAllCache(Context context) {
        deleteDir(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteDir(context.getExternalCacheDir());
        }
    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    // 获取文件
    //Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
    //Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    public static File checkFile(String gid, String name, Context context) throws IOException {
        File externalFilesDir = Environment.getExternalStorageDirectory();
        File groupFile = new File(externalFilesDir, "video_app/" + gid);
        if (groupFile.exists()) {
            File chirlFile = new File(groupFile, name);
            if (chirlFile.exists()) {
                chirlFile.delete();
            }
            chirlFile.createNewFile();
            return chirlFile;
        } else {
            groupFile.mkdirs();
            File chirlFile = new File(groupFile, name);
            chirlFile.createNewFile();
            return chirlFile;
        }
    }

    public static void downLoadFile(String gid, String name, String realName, Context context) throws IOException {
        File file = checkFile(gid, name, context);
        if (null == file) {
            return;
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("gid", gid);
        map.put("fileName", realName);
        VideoApi.getVideoService().downLoadFile(realName, gid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {


                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        try {
                            InputStream inputStream = responseBody.byteStream();
                            OutputStream os = new FileOutputStream(file);
                            byte[] buf = new byte[4];
                            int hasRead = 0;
                            while ((hasRead = inputStream.read(buf)) > 0) {
                                os.write(buf, 0, hasRead);
                            }
                            inputStream.close();
                            os.close();
                            Log.i("TAG", "下载完成" + file.getAbsolutePath());
                            Toast.makeText(context,"下载完成" + file.getAbsolutePath(),Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Log.i("TAG", e.toString());
                        }

                    }
                });


    }


    /**
     * 格式化单位
     *
     * @param size
     * @return
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
//            return size + "Byte";
            return "0K";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }

}