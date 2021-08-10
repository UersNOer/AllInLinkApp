package com.unistrong.api.mapcore.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Map;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Utils//bw
{
  public static boolean hasKey(JSONObject jsonObject, String key)
  {
    return (jsonObject != null) && (jsonObject.has(key));
  }
  
//  public static byte[] getIV()
//  {
//    try
//    {
//      String str1 = "16,16,18,77,15,911,121,77,121,911,38,77,911,99,86,67,611,96,48,77,84,911,38,67,021,301,86,67,611,98,48,77,511,77,48,97,511,58,48,97,511,84,501,87,511,96,48,77,221,911,38,77,121,37,86,67,25,301,86,67,021,96,86,67,021,701,86,67,35,56,86,67,611,37,221,87";
//
//      StringBuffer localStringBuffer = new StringBuffer(str1);
//      String str2 = localStringBuffer.reverse().toString();
//      String[] arrayOfString1 = str2.split(",");
//      byte[] arrayOfByte1 = new byte[arrayOfString1.length];
//      for (int i = 0; i < arrayOfString1.length; i++) {
//        arrayOfByte1[i] = Byte.parseByte(arrayOfString1[i]);
//      }
//      byte[] arrayOfByte2 = Encrypt.base64Decode(new String(arrayOfByte1));
//      String str3 = new String(arrayOfByte2);
//      localStringBuffer = new StringBuffer(str3);
//      String str4 = localStringBuffer.reverse().toString();
//
//      String[] arrayOfString2 = str4.split(",");
//      byte[] arrayOfByte3 = new byte[arrayOfString2.length];
//      for (int j = 0; j < arrayOfString2.length; j++) {
//        arrayOfByte3[j] = Byte.parseByte(arrayOfString2[j]);
//      }
//      return arrayOfByte3;
//    }
//    catch (Throwable localThrowable)
//    {
//      BasicLogHandler.a(localThrowable, "Utils", "getIV");
//    }
//    return new byte[16];
//  }
  
  static String mapToString(Map<String, String> map)
  {
    if (map != null)
    {
      StringBuilder localStringBuilder = new StringBuilder();
      for (Map.Entry localEntry : map.entrySet())
      {
        if (localStringBuilder.length() > 0) {
          localStringBuilder.append("&");
        }
        localStringBuilder.append((String)localEntry.getKey());
        localStringBuilder.append("=");
        localStringBuilder.append((String)localEntry.getValue());
      }
      return localStringBuilder.toString();
    }
    return null;
  }
  
  public static String a(Throwable throwable)
  {
    StringWriter stringWriter = null;
    PrintWriter printWriter = null;
    try
    {
      stringWriter = new StringWriter();
      printWriter = new PrintWriter(stringWriter);
      throwable.printStackTrace(printWriter);
      Throwable localThrowable1 = throwable.getCause();
      while (localThrowable1 != null)
      {
        localThrowable1.printStackTrace(printWriter);
        localThrowable1 = localThrowable1.getCause();
      }
      String str1 = stringWriter.toString();
      return str1;
    }
    catch (Throwable throwable1)
    {
      throwable1.printStackTrace();
    }
    finally
    {
      if (stringWriter != null) {
        try
        {
          stringWriter.close();
        }
        catch (Throwable localThrowable3)
        {
          localThrowable3.printStackTrace();
        }
      }
      if (printWriter != null) {
        try
        {
          printWriter.close();
        }
        catch (Throwable localThrowable4)
        {
          localThrowable4.printStackTrace();
        }
      }
    }
    return null;
  }
  
  public static String appendParams(Map<String, String> paramMap)
  {
    String str = mapToString(paramMap);
    return sortParams(str);
  }
  
  public static String sortParams(String paramString)
  {
    try
    {
      if (TextUtils.isEmpty(paramString)) {
        return "";
      }
      String[] arrayOfString = paramString.split("&");
      Arrays.sort(arrayOfString);
      StringBuffer localStringBuffer = new StringBuffer();
      for (String str : arrayOfString)
      {
        localStringBuffer.append(str);
        localStringBuffer.append("&");
      }
      String strValue = localStringBuffer.toString();
      if (strValue.length() > 1) {
        return (String)strValue.subSequence(0, (strValue.length() - 1));
      }
    }
    catch (Throwable localThrowable)
    {
      BasicLogHandler.a(localThrowable, "Utils", "sortParams");
    }
    return paramString;
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
  
  public static byte[] zipLog(byte[] data)
  {
    if ((data == null) || (data.length == 0)) {
      return null;
    }
    byte[] arrayOfByte = null;
    ZipOutputStream zipOutputStream = null;
    ByteArrayOutputStream outputStream = null;
    try
    {
      outputStream = new ByteArrayOutputStream();
      zipOutputStream = new ZipOutputStream(outputStream);
      zipOutputStream.putNextEntry(new ZipEntry("log"));
      zipOutputStream.write(data);
      zipOutputStream.closeEntry();
      zipOutputStream.finish();
      arrayOfByte = outputStream.toByteArray();
    }
    catch (Throwable throwable)
    {
      BasicLogHandler.a(throwable, "Utils", "zip");
    }
    finally
    {
      if (zipOutputStream != null) {
        try
        {
          zipOutputStream.close();
        }
        catch (Throwable throwable)
        {
          BasicLogHandler.a(throwable, "Utils", "zip1");
          throwable.printStackTrace();
        }
      }
      if (outputStream != null) {
        try
        {
          outputStream.close();
        }
        catch (Throwable throwable)
        {
          BasicLogHandler.a(throwable, "Utils", "zip2");
          throwable.printStackTrace();
        }
      }
    }
    return arrayOfByte;
  }

  /*
   * 获取public key.
   * @param context
   * @return
   * @throws CertificateException
   * @throws InvalidKeySpecException
   * @throws NoSuchAlgorithmException
   * @throws NullPointerException
   * @throws IOException
   */
  static PublicKey getPublicKey(Context context)
    throws CertificateException, InvalidKeySpecException, NoSuchAlgorithmException, NullPointerException, IOException
  {
    String str="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDFoAkykHtfxJJpD9UGIHM6/Z41L9pHlDlFCH10CcVDDRMAP3ilNQBMUFHBGM7qxNsTPGlcpYOOyQ5JaWcpL+EI+dUDmL+rdM6vpvIGIxKIgZv12qntHytheT9xs/ouvooZ6JHu2874Wa981sUqwlr5L8o7ZFc8Dix2CQn4L5Eq8wIDAQAB";
    KeyFactory localKeyFactory = null;
    try
    {
      localKeyFactory = KeyFactory.getInstance("RSA");
      Object localObject1 = new X509EncodedKeySpec(Base64.decode(str,0));
      
      return localKeyFactory.generatePublic((KeySpec)localObject1);
    }
    catch (Throwable throwable)
    {
      throwable.printStackTrace();
    }
    return null;
  }
  
  static String c(byte[] paramArrayOfByte)
  {
    try
    {
      return byte2HexString(paramArrayOfByte);
    }
    catch (Throwable localThrowable)
    {
      BasicLogHandler.a(localThrowable, "Utils", "HexString");
    }
    return null;
  }
  
  static String d(byte[] paramArrayOfByte)
  {
    try
    {
      return byte2HexString(paramArrayOfByte);
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
    return null;
  }
  
  public static String byte2HexString(byte[] paramArrayOfByte)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    if (paramArrayOfByte == null) {
      return null;
    }
    for (int i = 0; i < paramArrayOfByte.length; i++)
    {
      String str = Integer.toHexString(paramArrayOfByte[i] & 0xFF);
      if (str.length() == 1) {
        str = '0' + str;
      }
      localStringBuilder.append(str);
    }
    return localStringBuilder.toString();
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
