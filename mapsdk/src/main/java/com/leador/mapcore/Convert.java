package com.leador.mapcore;

import java.io.ByteArrayOutputStream;

public class Convert
{
  public int a;
  public String b;
  public static int getInt(byte[] data, int index)
  {
    int i = data[(3 + index)] & 0xFF;
    int j = data[(2 + index)] & 0xFF;
    int k = data[(1 + index)] & 0xFF;
    int m = data[(0 + index)] & 0xFF;
    int n = (i << 24) + (j << 16) + (k << 8) + (m << 0);
    return n;
  }
  
  public static short getShort(byte[] paramArrayOfByte, int paramInt)
  {
    int i = paramArrayOfByte[(paramInt + 1)] & 0xFF;
    int j = paramArrayOfByte[(paramInt + 0)] & 0xFF;
    short s = (short)((i << 8) + (j << 0));
    return s;
  }
  
  public static int getUShort(byte[] paramArrayOfByte, int paramInt)
  {
    int i = paramArrayOfByte[(paramInt + 1)] & 0xFF;
    int j = paramArrayOfByte[(paramInt + 0)] & 0xFF;
    int k = (i << 8) + (j << 0);
    return k;
  }
  
  public static boolean getBit(byte paramByte, int paramInt)
  {
    int i = paramByte;
    i <<= 32 - paramInt;
    i >>>= 32 - paramInt;
    i >>>= paramInt - 1;
    if (i > 0) {
      return true;
    }
    return false;
  }
  
  public static byte[] copyString(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    byte[] arrayOfByte = new byte[paramInt2];
    System.arraycopy(paramArrayOfByte, paramInt1, arrayOfByte, 0, paramInt2);
    return arrayOfByte;
  }
  
  public static byte[] covertBytes(byte paramByte)
  {
    byte[] arrayOfByte = new byte[1];
    arrayOfByte[0] = paramByte;
    return arrayOfByte;
  }
  
  public static byte[] convertInt(int paramInt)
  {
    byte[] arrayOfByte = new byte[4];
    arrayOfByte[0] = ((byte)(paramInt & 0xFF));
    arrayOfByte[1] = ((byte)(paramInt >> 8 & 0xFF));
    arrayOfByte[2] = ((byte)(paramInt >> 16 & 0xFF));
    arrayOfByte[3] = ((byte)(paramInt >> 24 & 0xFF));
    return arrayOfByte;
  }
  
  public static int getNum(byte paramByte, int paramInt1, int paramInt2)
  {
    int i = paramByte;
    
    i <<= 32 - paramInt2 - 1;
    i >>>= 32 - paramInt2 - 1;
    i >>>= paramInt1;
    return i;
  }
  
  public static int getNum(short paramShort, int paramInt1, int paramInt2)
  {
    int i = paramShort;
    i <<= 32 - paramInt2;
    i >>>= 32 - paramInt2;
    i >>>= paramInt1 - 1;
    return i;
  }
  
  public static byte[] convertShort(int paramInt)
  {
    byte[] arrayOfByte = new byte[2];
    arrayOfByte[0] = ((byte)(paramInt & 0xFF));
    arrayOfByte[1] = ((byte)(paramInt >> 8 & 0xFF));
    return arrayOfByte;
  }
  
  public static void writeInt(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    byte[] arrayOfByte = convertInt(paramInt2);
    System.arraycopy(arrayOfByte, 0, paramArrayOfByte, paramInt1, 4);
  }
  
  public static void writeShort(byte[] paramArrayOfByte, int paramInt, short paramShort)
  {
    byte[] arrayOfByte = convertShort(paramShort);
    System.arraycopy(arrayOfByte, 0, paramArrayOfByte, paramInt, 2);
  }
  
  public static void moveArray(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2, int paramInt3)
  {
    byte[] arrayOfByte = new byte[paramInt3];
    System.arraycopy(paramArrayOfByte1, paramInt1, arrayOfByte, 0, paramInt3);
    System.arraycopy(arrayOfByte, 0, paramArrayOfByte2, paramInt2, paramInt3);
  }
  
  public static String getString(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    try
    {
      return new String(paramArrayOfByte, paramInt1, paramInt2, "UTF-8");
    }
    catch (Exception localException) {}
    return "";
  }
  
  public static byte[] getSubBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    byte[] arrayOfByte = new byte[paramInt2];
    System.arraycopy(paramArrayOfByte, paramInt1, arrayOfByte, 0, paramInt2);
    return arrayOfByte;
  }
  
  public static byte[] get1BString(String paramString)
  {
    try
    {
      ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
      
      byte[] arrayOfByte1 = paramString.getBytes("UTF-8");
      byte[] arrayOfByte2 = new byte[1];
      arrayOfByte2[0] = ((byte)arrayOfByte1.length);
      localByteArrayOutputStream.write(arrayOfByte2);
      localByteArrayOutputStream.write(arrayOfByte1);
      return localByteArrayOutputStream.toByteArray();
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    return new byte[1];
  }
  
  public static byte[] get2BString(String paramString)
  {
    try
    {
      ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
      
      byte[] arrayOfByte = paramString.getBytes("UTF-8");
      localByteArrayOutputStream.write(convertShort(arrayOfByte.length));
      localByteArrayOutputStream.write(arrayOfByte);
      return localByteArrayOutputStream.toByteArray();
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    return new byte[1];
  }
  
  public static double convertDouble(byte[] paramArrayOfByte, int paramInt)
  {
    long l = 0L;
    for (int i = 0; i < 8; i++) {
      l += ((paramArrayOfByte[(i + paramInt)] & 0xFF) << 8 * i);
    }
    return Double.longBitsToDouble(l);
  }
  
  public static byte[] getDouble(double paramDouble)
  {
    byte[] arrayOfByte = new byte[8];
    long l = Double.doubleToLongBits(paramDouble);
    String str1 = Long.toHexString(l);
    for (int i = 0; i < 8; i++)
    {
      String str2 = str1.substring(2 * i, 2 * i + 2);
      arrayOfByte[(7 - i)] = ((byte)Integer.parseInt(str2, 16));
    }
    return arrayOfByte;
  }
  
  public static void convert1bString(byte[] paramArrayOfByte, int paramInt, Convert paramb)
  {
    try
    {
      paramb.a = paramArrayOfByte[paramInt];
      paramb.b = new String(paramArrayOfByte, paramInt + 1, paramb.a, "UTF-8");
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
      paramb.a = 0;
      paramb.b = "";
    }
  }
  
  public static void convert2bString(byte[] paramArrayOfByte, int paramInt, Convert paramb)
  {
    try
    {
      paramb.a = getShort(paramArrayOfByte, paramInt);
      paramb.b = new String(paramArrayOfByte, paramInt + 2, paramb.a, "UTF-8");
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
      paramb.a = 0;
      paramb.b = "";
    }
  }
  
  public static final String bytesToHexString(byte[] paramArrayOfByte)
  {
    StringBuffer localStringBuffer = new StringBuffer(paramArrayOfByte.length);
    for (int i = 0; i < paramArrayOfByte.length; i++)
    {
      String str = Integer.toHexString(0xFF & paramArrayOfByte[i]);
      if (str.length() < 2) {
        localStringBuffer.append(0);
      }
      localStringBuffer.append(str.toUpperCase());
    }
    return localStringBuffer.toString();
  }
}
