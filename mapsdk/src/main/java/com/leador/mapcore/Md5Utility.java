package com.leador.mapcore;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;

public class Md5Utility
{
  public static String hexdigest(String paramString)
  {
    String str = null;
    char[] arrayOfChar1 = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    try
    {
      MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
      localMessageDigest.update(paramString.getBytes());
      byte[] arrayOfByte = localMessageDigest.digest();
      char[] arrayOfChar2 = new char[32];
      int i = 0;
      for (int j = 0; j < 16; j++)
      {
        int k = arrayOfByte[j];
        arrayOfChar2[(i++)] = arrayOfChar1[(k >>> 4 & 0xF)];
        arrayOfChar2[(i++)] = arrayOfChar1[(k & 0xF)];
      }
      str = new String(arrayOfChar2);
    }
    catch (Exception localException) {}
    return str;
  }
  
  public static String getFileMD5(String paramString)
  {
    try
    {
      FileInputStream localFileInputStream = new FileInputStream(paramString);
      MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
      byte[] arrayOfByte1 = new byte['Ѐ'];
      int i;
      while ((i = localFileInputStream.read(arrayOfByte1)) != -1) {
        localMessageDigest.update(arrayOfByte1, 0, i);
      }
      localFileInputStream.close();
      byte[] arrayOfByte2 = localMessageDigest.digest();
      StringBuffer localStringBuffer = new StringBuffer();
      for (int j = 0; j < arrayOfByte2.length; j++)
      {
        int k = arrayOfByte2[j] & 0xFF;
        if (k < 16) {
          localStringBuffer.append("0");
        }
        localStringBuffer.append(Integer.toHexString(k));
      }
      return localStringBuffer.toString();
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    return null;
  }
  
  public static String getFileMD5(File paramFile)
  {
    try
    {
      FileInputStream localFileInputStream = new FileInputStream(paramFile);
      MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
      byte[] arrayOfByte1 = new byte['Ѐ'];
      int i;
      while ((i = localFileInputStream.read(arrayOfByte1)) != -1) {
        localMessageDigest.update(arrayOfByte1, 0, i);
      }
      localFileInputStream.close();
      byte[] arrayOfByte2 = localMessageDigest.digest();
      StringBuffer localStringBuffer = new StringBuffer();
      for (int j = 0; j < arrayOfByte2.length; j++)
      {
        int k = arrayOfByte2[j] & 0xFF;
        if (k < 16) {
          localStringBuffer.append("0");
        }
        localStringBuffer.append(Integer.toHexString(k));
      }
      return localStringBuffer.toString();
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    return null;
  }
  
  public static String getStringMD5(String paramString)
  {
    char[] arrayOfChar = paramString.toCharArray();
    
    return getCharArrayMD5(arrayOfChar);
  }
  
  public static String getCharArrayMD5(char[] paramArrayOfChar)
  {
    byte[] arrayOfByte = new byte[paramArrayOfChar.length];
    for (int i = 0; i < paramArrayOfChar.length; i++) {
      arrayOfByte[i] = ((byte)paramArrayOfChar[i]);
    }
    return getByteArrayMD5(arrayOfByte);
  }
  
  public static String getByteArrayMD5(byte[] paramArrayOfByte)
  {
    try
    {
      MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
      localMessageDigest.update(paramArrayOfByte);
      byte[] arrayOfByte = localMessageDigest.digest();
      StringBuffer localStringBuffer = new StringBuffer();
      for (int i = 0; i < arrayOfByte.length; i++)
      {
        int j = arrayOfByte[i] & 0xFF;
        if (j < 16) {
          localStringBuffer.append("0");
        }
        localStringBuffer.append(Integer.toHexString(j));
      }
      return localStringBuffer.toString();
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    return null;
  }
}
