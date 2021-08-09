package com.unistrong.uni_utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class MyUtils {
    public static final ThreadLocal<SimpleDateFormat> simpleDateFormat1 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        }
    };
    public static final ThreadLocal<SimpleDateFormat> simpleDateFormat3 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy/MM/dd", Locale.CHINA);
        }
    };

    public static final ThreadLocal<SimpleDateFormat> simpleDateFormat2 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("HH:00", Locale.CHINA);
        }
    };

    public static final ThreadLocal<SimpleDateFormat> simpleDateFormat4 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy/MM", Locale.CHINA);
        }
    };

    public static final ThreadLocal<SimpleDateFormat> simpleDateFormat9 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy", Locale.CHINA);
        }
    };
    public static final ThreadLocal<SimpleDateFormat> simpleDateFormat5 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM", Locale.CHINA);
        }
    };
    public static final ThreadLocal<SimpleDateFormat> simpleDateFormat6 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("MM/dd", Locale.CHINA);
        }
    };
    public static final ThreadLocal<SimpleDateFormat> simpleDateFormat7 = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        }
    };


    public static String simpleDateString7(Date date) {
        if (null == date)
            return "";
        return simpleDateFormat7.get().format(date);
    }

    public static String simpleDateStringYM4(Date date) {
        if (null == date)
            return "";
        return simpleDateFormat4.get().format(date);
    }

    public static String simpleDateStringYM6(Date date) {
        if (null == date)
            return "";
        return simpleDateFormat6.get().format(date);
    }

    public static String simpleDateStringYM5(Date date) {
        if (null == date)
            return "";
        return simpleDateFormat5.get().format(date);
    }


    public static String simpleDateStringYMD9(Date date) {
        if (null == date)
            return "";
        return simpleDateFormat9.get().format(date);
    }


    public static String simpleDateStringYMD2(Date date) {
        if (null == date)
            return "";
        return simpleDateFormat1.get().format(date);
    }

    public static String simpleDateStringYMD(Date date) {
        if (null == date)
            return "";
        return simpleDateFormat3.get().format(date);
    }

    public static String simpleDateStringHM(Date date) {
        if (null == date)
            return "";
        return simpleDateFormat2.get().format(date);
    }


    public static boolean txtCheckEmpty(TextView txNav) {
        String txt = txNav.getText().toString().trim();
        if (txt == null) {
            return true;
        }
        if (TextUtils.isEmpty(txt)) {
            return true;
        }
        return false;
    }

//    public static boolean txtCheckEmpty(EditLinearLayout txNav) {
//        String txt = txNav.getText();
//        if (txt == null) {
//            return true;
//        }
//        if (TextUtils.isEmpty(txt)) {
//            return true;
//        }
//        return false;
//    }

    public static boolean txtCheckEmpty(String txt) {
        if (txt == null) {
            return true;
        }
        if (TextUtils.isEmpty(txt)) {
            return true;
        }
        return false;
    }

    //TODO 版本判断，只判断versionCode
    public static void checkVersionCode(Activity context, int newCode, CheckVersionCallBack checkVersionCallBack) {
        int locationCode;
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        locationCode = packageInfo.versionCode;
        if (newCode > locationCode) {
            checkVersionCallBack.showNewAppDialog();
        }
    }


    public interface CheckVersionCallBack {
        void showNewAppDialog();
    }


    //    public static void writeResponseBodyToDisk(ResponseBody body, String deviceName, DownloadFileListance listance) {
//        try {
//            // todo change the file location/name according to your needs
//            Log.i("TAG", "开始下载");
//            File futureStudioIconFile = new File(UserCache.FILEPATH + "/" + deviceName + "_" + System.currentTimeMillis() + ".xls");
//
//            if (!futureStudioIconFile.exists()) {
//                futureStudioIconFile.createNewFile();
//            }
//
//            if (futureStudioIconFile.exists()) {
//                Log.i("TAG", "创建成功————" + futureStudioIconFile.getAbsolutePath());
//            }
//
//            InputStream inputStream = null;
//            OutputStream outputStream = null;
//
//            try {
//                byte[] fileReader = new byte[4096];
//
//                long fileSize = body.contentLength();
//                long fileSizeDownloaded = 0;
//
//                inputStream = body.byteStream();
//                outputStream = new FileOutputStream(futureStudioIconFile);
//
//                while (true) {
//                    int read = inputStream.read(fileReader);
//
//                    if (read == -1) {
//                        break;
//                    }
//
//                    outputStream.write(fileReader, 0, read);
//                    fileSizeDownloaded += read;
//                }
//
//                outputStream.flush();
//                listance.downloadSuccess("下载成功，文件路径: WarningXls/" + futureStudioIconFile.getName());
//            } catch (IOException e) {
//                listance.downloadSuccess("下载失败1");
//
//            } finally {
//                if (inputStream != null) {
//                    inputStream.close();
//                }
//
//                if (outputStream != null) {
//                    outputStream.close();
//                }
//            }
//        } catch (IOException e) {
//            listance.downloadSuccess("下载失败2");
//
//        }
//    }
    public static int getAppVersionCode(Context context) {
        int versioncode = 0;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            // versionName = pi.versionName;
            versioncode = pi.versionCode;
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versioncode;
    }
    public static String getAppVersionName(Context context) {
        String versioncode = "1.0.0";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            // versionName = pi.versionName;
            versioncode = pi.versionName;
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versioncode;
    }

    public static String Bitmap2StrByBase64(String filePath) {

        Bitmap bit = BitmapFactory.decodeFile(filePath);
//        if (filePath.contains("GalleryFinal")) {
//            bit= rotaingImageView(filePath);
//        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, 50, baos);//参数100表示不压缩
        byte[] bytes = baos.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    /**
     * 读取照片旋转角度
     *
     * @param path 照片路径
     * @return 角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            Log.e("TAG", "原图被旋转角度： ========== " + orientation);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public static Bitmap rotaingImageView(String filePath) {
//        int angle = readPictureDegree(filePath);
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        Bitmap returnBm = null;
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bitmap;
        }
        if (bitmap != returnBm) {
            bitmap.recycle();
        }
        return returnBm;
    }

    public static Bitmap stringtoBitmap(String string) {
        //将字符串转换成Bitmap类型
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("TAG", e.toString());
        }

        return bitmap;
    }

    public static String addMonth(Date date, int month) {
        String nowDate = null;
        SimpleDateFormat format = MyUtils.simpleDateFormat1.get();
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MONTH, month);
            nowDate = format.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nowDate;
    }

}