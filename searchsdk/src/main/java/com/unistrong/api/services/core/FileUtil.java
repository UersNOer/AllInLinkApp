package com.unistrong.api.services.core;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.zip.GZIPOutputStream;

class FileUtil {

    @SuppressLint("NewApi")
    public static String getExternalStroragePath(Context context)
    {
        int i = Build.VERSION.SDK_INT;
        if (i >= 12) {
            try
            {
                StorageManager localStorageManager = (StorageManager)context.getSystemService(Context.STORAGE_SERVICE);

                Method localMethod1 = StorageManager.class.getMethod("getVolumeList", new Class[0]);
                Method localMethod2 = StorageManager.class.getMethod("getVolumeState", new Class[] { String.class });

                Object[] arrayOfObject1 = (Object[])localMethod1.invoke(localStorageManager, new Object[0]);
                String str1 = null;
                String str2 = null;
                Boolean localBoolean = Boolean.valueOf(false);
                String str3 = "";
                String str4 = "";
                String str5 = "";
                String str6 = "";
                String str7 = null;
                for (Object localObject : arrayOfObject1)
                {
                    Method localMethod3 = localObject.getClass().getMethod("getPath", new Class[0]);
                    Method localMethod4 = localObject.getClass().getMethod("isRemovable", new Class[0]);

                    str2 = (String)localMethod3.invoke(localObject, new Object[0]);

                    str1 = (String)localMethod2.invoke(localStorageManager, new Object[] {localMethod3
                            .invoke(localObject, new Object[0]) });
                    localBoolean = (Boolean)localMethod4.invoke(localObject, new Object[0]);
                    if (!str2.toLowerCase().contains("private")) {
                        if (localBoolean.booleanValue())
                        {
                            str3 = str2;
                            str5 = str1;
                            if ((null != str3) && (null != str5) &&
                                    (str5.equals("mounted")))
                            {
                                if (i <= 18)
                                {
                                    str7 = str3; break;
                                }
                                try
                                {
                                    File[] arrayOfFile = context.getExternalFilesDirs(null);
                                    if (arrayOfFile != null) {
                                        if (arrayOfFile.length > 1) {
                                            str7 = arrayOfFile[1].getAbsolutePath();
                                        } else {
                                            str7 = str2;
                                        }
                                    }
                                }
                                catch (Exception localException2)
                                {
                                    str7 = str3;
                                }
                            }
                        }
                        else
                        {
                            str4 = str2;
                            str6 = str1;
                        }
                    }
                }
                if (i <= 18)
                {
                    if ((null == str7) && (null != str4) &&
                            (null != str6) &&
                            (str6.equals("mounted"))) {}
                    return str4;
                }
                if ((null != str4) &&
                        (null != str6) &&
                        (str6.equals("mounted"))) {}
                return str4;
            }
            catch (Exception localException1) {}
        }
        File localFile = null;
        boolean bool = Environment.getExternalStorageState().equals("mounted");
        if (bool)
        {
            localFile = Environment.getExternalStorageDirectory();
            return localFile.toString();
        }
        return null;
    }

    /**
     * 将字符串保存到指定的文件
     *
     * @param file
     * @param data
     */
    public static void writeTextFile(File file, String data) {
        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        ReentrantReadWriteLock.WriteLock writelock = lock.writeLock();
        writelock.lock();
        try {
            if (!file.exists()) {
                File parent = file.getParentFile();
                parent.mkdirs();
            }
            if (file.exists()) { // 判断当前文件是否存在
                file.delete(); // 存在就删除
            }
            file.createNewFile();
            byte[] bytes = data.getBytes();
            OutputStream os = new FileOutputStream(file);
            os.write(bytes);
            os.flush();
            os.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            writelock.unlock();
        }
    }

    /**
     * 检测文件是否存在
     *
     * @param fileName
     * @return boolean
     */
    public static boolean isFileExists(String fileName) {
        if (!TextUtils.isEmpty(fileName)) {
            return new File(fileName).exists();
        } else {
            return false;
        }
    }

    /**
     * 获取文本文件内容
     *
     * @param fileName
     * @return string
     * @throws java.io.IOException
     */

    public static String getFileContents(String fileName) throws IOException {
        byte[] bytes = new byte[1024];
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        File fHandler = new File(fileName);
        FileInputStream inputStream = new FileInputStream(fHandler);
        while ((inputStream.read(bytes)) != -1) {
            arrayOutputStream.write(bytes, 0, bytes.length);
        }
        String content = new String(arrayOutputStream.toByteArray());
        inputStream.close();
        arrayOutputStream.close();
        return content.trim();
    }

    public static byte[] gZip(byte[] paramArrayOfByte)
    {
        try
        {
            return f(paramArrayOfByte);
        }
        catch (IOException localIOException)
        {
            BasicLogHandler.a(localIOException, "Utils", "gZip");
        }
        catch (Throwable localThrowable)
        {
            BasicLogHandler.a(localThrowable, "Utils", "gZip");
        }
        return new byte[0];
    }

    private static byte[] f(byte[] paramArrayOfByte)
            throws IOException, Throwable
    {
        byte[] arrayOfByte = null;
        ByteArrayOutputStream localByteArrayOutputStream = null;
        GZIPOutputStream localGZIPOutputStream = null;
        if (paramArrayOfByte == null) {
            return null;
        }
        try
        {
            localByteArrayOutputStream = new ByteArrayOutputStream();
            localGZIPOutputStream = new GZIPOutputStream(localByteArrayOutputStream);
            localGZIPOutputStream.write(paramArrayOfByte);
            localGZIPOutputStream.finish();
            arrayOfByte = localByteArrayOutputStream.toByteArray();
        }
        catch (IOException localIOException)
        {
            throw localIOException;
        }
        catch (Throwable localThrowable1)
        {
            throw localThrowable1;
        }
        finally
        {
            if (localGZIPOutputStream != null) {
                try
                {
                    localGZIPOutputStream.close();
                }
                catch (Throwable localThrowable2)
                {
                    throw localThrowable2;
                }
            }
            if (localByteArrayOutputStream != null) {
                try
                {
                    localByteArrayOutputStream.close();
                }
                catch (Throwable localThrowable3)
                {
                    throw localThrowable3;
                }
            }
        }
        return arrayOfByte;
    }
}
