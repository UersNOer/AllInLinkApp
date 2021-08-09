package com.leador.mapcore;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.text.TextUtils;

import com.unistrong.api.maps.MapsInitializer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class FileUtil
{
  private static final String TAG = "FileUtil";
  public static final String CACHE_DIR_NAME="leador";
  public static void copy(Context paramContext, String paramString, File paramFile)
    throws Exception
  {
    paramFile.delete();
    InputStream localInputStream = paramContext.getAssets().open(paramString);
    byte[] arrayOfByte = new byte[localInputStream.available()];
    localInputStream.read(arrayOfByte);
    localInputStream.close();
    FileOutputStream localFileOutputStream = new FileOutputStream(paramFile);
    localFileOutputStream.write(arrayOfByte);
    localFileOutputStream.close();
  }
  
  public static void deleteFile(File paramFile)
  {
    if (paramFile.isDirectory())
    {
      File[] arrayOfFile = paramFile.listFiles();
      if (arrayOfFile == null) {
        return;
      }
      int i = 0;
      for (int j = arrayOfFile.length; i < j; i++) {
        deleteFile(arrayOfFile[i]);
      }
    }
    paramFile.delete();
  }
  
  public static String getMapBaseStorage(Context paramContext)
  {
    int i = Build.VERSION.SDK_INT;
    String str1 = "map_base_path";
    if (i > 18) {
      str1 = "map_base_path_v44";
    }
    String str2 = "";
    SharedPreferences basePath = paramContext.getSharedPreferences("base_path", 0);
    if ((MapsInitializer.sdcardDir != null) &&   (MapsInitializer.sdcardDir.trim().length() > 0)) {
      str2 = MapsInitializer.sdcardDir;
    } else {
      str2 = basePath.getString(str1, "");
    }
    File localFile;
    if ((str2 != null) && (str2.length() > 2))
    {
      localFile = new File(str2);
      if (!localFile.exists()) {
        localFile.mkdir();
      }
      if (localFile.isDirectory())
      {
        if (localFile.canWrite()) {
          return str2;
        }
        str2 = paramContext.getCacheDir().toString();
        if ((str2 != null) && (str2.length() > 2))
        {
          localFile = new File(str2);
          if (localFile.isDirectory()) {
            return str2;
          }
        }
      }
    }
    str2 = getExternalStroragePath(paramContext);
    if ((str2 != null) && (str2.length() > 2))
    {
      str2 = str2 + File.separator + CACHE_DIR_NAME;
      localFile = new File(str2);
      if (!localFile.exists()) {
        localFile.mkdir();
      }
      if (localFile.isDirectory())
      {
        SharedPreferences.Editor localEditor = basePath.edit();
        localEditor.putString(str1, str2);
        localEditor.commit();
        
        createNoMediaFileIfNotExist(str2);
        return str2;
      }
    }
    str2 = paramContext.getCacheDir().toString();
    if ((str2 != null) && (str2.length() > 2))
    {
      str2 = str2 + File.separator + CACHE_DIR_NAME;
      localFile = new File(str2);
      if (!localFile.exists()) {
        localFile.mkdir();
      }
      if (localFile.isDirectory()) {
        return str2;
      }
    }
    return str2;
  }
  
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
  
  public static void writeDatasToFile(String paramString, byte[] paramArrayOfByte)
  {
    ReentrantReadWriteLock localReentrantReadWriteLock = new ReentrantReadWriteLock();
    ReentrantReadWriteLock.WriteLock localWriteLock = localReentrantReadWriteLock.writeLock();
    localWriteLock.lock();
    try
    {
      if ((paramArrayOfByte == null) || (paramArrayOfByte.length == 0)) {
        return;
      }
      File localFile = new File(paramString);
      if (localFile.exists()) {
        localFile.delete();
      }
      localFile.createNewFile();
      FileOutputStream localFileOutputStream = new FileOutputStream(localFile);
      localFileOutputStream.write(paramArrayOfByte);
      localFileOutputStream.flush();
      localFileOutputStream.close();
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    finally
    {
      localWriteLock.unlock();
    }
  }
  
  public static byte[] readFileContents(String filePath)
  {
    try
    {
      File file = new File(filePath);
      if (!file.exists()) {
        return null;
      }
      FileInputStream fileInputStream = new FileInputStream(file);
      byte[] arrayOfByte = new byte['Ѐ'];
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      int data = 0;
      while ((data = fileInputStream.read(arrayOfByte)) != -1) {
        byteArrayOutputStream.write(arrayOfByte, 0, data);
      }
      byteArrayOutputStream.close();
      fileInputStream.close();
      return byteArrayOutputStream.toByteArray();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return null;
  }
  public static byte[] readFileContents(File file)
  {
    try
    {
      if (!file.exists()) {
        return null;
      }
      FileInputStream fileInputStream = new FileInputStream(file);
      byte[] arrayOfByte = new byte['Ѐ'];
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      int data = 0;
      while ((data = fileInputStream.read(arrayOfByte)) != -1) {
        byteArrayOutputStream.write(arrayOfByte, 0, data);
      }
      byteArrayOutputStream.close();
      fileInputStream.close();
      return byteArrayOutputStream.toByteArray();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return null;
  }
  public static void createNoMediaFileIfNotExist(String paramString) {}

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
}
