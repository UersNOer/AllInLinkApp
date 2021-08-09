package com.unistrong.api.mapcore.util;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Encrypt//br
{
  public static String base64Dec(String decodeData)
  {
    try
    {
      return new String(base64Decode(decodeData), "UTF-8");
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException) {}
    return new String(base64Decode(decodeData));
  }
//  与base64方法相同，保留一个
//  public static String a(byte[] data)
//  {
//    try
//    {
//      return encodeBase64(data);
//    }
//    catch (Throwable throwable)
//    {
//      throwable.printStackTrace();
//    }
//    return null;
//  }
  
  private static final char[] base64EncodeChars = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/' };
  private static final byte[] base64DecodeChars = new byte[''];
  
  static
  {
    for (int i = 0; i < 128; i++) {
      base64DecodeChars[i] = -1;
    }
    for (int i = 65; i <= 90; i++) {
      base64DecodeChars[i] = ((byte)(i - 65));
    }
    for (int i = 97; i <= 122; i++) {
      base64DecodeChars[i] = ((byte)(i - 97 + 26));
    }
    for (int i = 48; i <= 57; i++) {
      base64DecodeChars[i] = ((byte)(i - 48 + 52));
    }
    base64DecodeChars[43] = 62;
    base64DecodeChars[47] = 63;
  }
  
//  static byte[] aesEncrypt(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
//  {
//    try
//    {
//      return AES(paramArrayOfByte1, paramArrayOfByte2);
//    }
//    catch (InvalidKeyException localInvalidKeyException)
//    {
//      BasicLogHandler.a(localInvalidKeyException, "Encrypt", "aesEncrypt");
//    }
//    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
//    {
//      BasicLogHandler.a(localNoSuchAlgorithmException, "Encrypt", "aesEncrypt");
//    }
//    catch (NoSuchPaddingException localNoSuchPaddingException)
//    {
//      BasicLogHandler.a(localNoSuchPaddingException, "Encrypt", "aesEncrypt");
//    }
//    catch (IllegalBlockSizeException localIllegalBlockSizeException)
//    {
//      BasicLogHandler.a(localIllegalBlockSizeException, "Encrypt", "aesEncrypt");
//    }
//    catch (BadPaddingException localBadPaddingException)
//    {
//      BasicLogHandler.a(localBadPaddingException, "Encrypt", "aesEncrypt");
//    }
//    catch (Throwable localThrowable)
//    {
//      BasicLogHandler.a(localThrowable, "Encrypt", "aesEncrypt");
//    }
//    return null;
//  }
  
//  private static byte[] AES(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
//    throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException
//  {
//    byte[] arrayOfByte = null;
//
//    IvParameterSpec localIvParameterSpec = new IvParameterSpec(Utils.getIV());
//    SecretKeySpec localSecretKeySpec = new SecretKeySpec(paramArrayOfByte1, "AES");
//    Cipher localCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//    try
//    {
//      localCipher.init(Cipher.ENCRYPT_MODE, localSecretKeySpec, localIvParameterSpec);
//    }
//    catch (InvalidAlgorithmParameterException localInvalidAlgorithmParameterException)
//    {
//      localInvalidAlgorithmParameterException.printStackTrace();
//    }
//    arrayOfByte = localCipher.doFinal(paramArrayOfByte2);
//
//    return arrayOfByte;
//  }
  
  static byte[] rsaEncrypt(byte[] paramArrayOfByte, Key publicKey)   throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException
  {
    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    cipher.init(Cipher.ENCRYPT_MODE, publicKey);
    // 模长
    int key_len = ((RSAPublicKey)publicKey).getModulus().bitLength() / 8;
    // 加密数据长度 <= 模长-11
    String[] datas = splitString(new String(paramArrayOfByte), key_len - 11);
    String mi = "";
    //如果明文长度大于模长-11则要分组加密
    for (String s : datas) {
      mi += bcd2Str(cipher.doFinal(s.getBytes()));
    }
    return mi.getBytes();

  }
  /**
   * BCD转字符串
   */
  public static String bcd2Str(byte[] bytes) {
    char temp[] = new char[bytes.length * 2], val;

    for (int i = 0; i < bytes.length; i++) {
      val = (char) (((bytes[i] & 0xf0) >> 4) & 0x0f);
      temp[i * 2] = (char) (val > 9 ? val + 'A' - 10 : val + '0');

      val = (char) (bytes[i] & 0x0f);
      temp[i * 2 + 1] = (char) (val > 9 ? val + 'A' - 10 : val + '0');
    }
    return new String(temp);
  }
  /**
   * 拆分字符串
   */
  public static String[] splitString(String string, int len) {
    int x = string.length() / len;
    int y = string.length() % len;
    int z = 0;
    if (y != 0) {
      z = 1;
    }
    String[] strings = new String[x + z];
    String str = "";
    for (int i=0; i<x+z; i++) {
      if (i==x+z-1 && y!=0) {
        str = string.substring(i*len, i*len+y);
      }else{
        str = string.substring(i*len, i*len+len);
      }
      strings[i] = str;
    }
    return strings;
  }
  public static byte[] base64Decode(String datastring)
  {
    if (datastring == null) {
      return new byte[0];
    }
    byte[] dataBytes;
    try
    {
      dataBytes = datastring.getBytes("UTF-8");
    }
    catch (UnsupportedEncodingException ex)
    {
      dataBytes = datastring.getBytes();
    }
    int length = dataBytes.length;
    ByteArrayOutputStream buf = new ByteArrayOutputStream(length);
    int i = 0;
    while (i < length)
    {
      int k;
      do
      {
        k = base64DecodeChars[dataBytes[(i++)]];
      } while ((i < length) && (k == -1));
      if (k == -1) {
        break;
      }
      int m;
      do
      {
        m = base64DecodeChars[dataBytes[(i++)]];
      } while ((i < length) && (m == -1));
      if (m == -1) {
        break;
      }
      buf.write(k << 2 | (m & 0x30) >>> 4);
      int n;
      do
      {
        n = dataBytes[(i++)];
        if (n == 61) {
          return buf.toByteArray();
        }
        n = base64DecodeChars[n];
      } while ((i < length) && (n == -1));
      if (n == -1) {
        break;
      }
      buf.write((m & 0xF) << 4 | (n & 0x3C) >>> 2);
      int i1;
      do
      {
        i1 = dataBytes[(i++)];
        if (i1 == 61) {
          return buf.toByteArray();
        }
        i1 = base64DecodeChars[i1];
      } while ((i < length) && (i1 == -1));
      if (i1 == -1) {
        break;
      }
      buf.write((n & 0x3) << 6 | i1);
    }
    return buf.toByteArray();
  }
  
  private static String encodeBase64(byte[] paramArrayOfByte)
  {
    StringBuffer buf = new StringBuffer();
    int i = paramArrayOfByte.length;
    int j = 0;
    while (j < i)
    {
      int k = paramArrayOfByte[(j++)] & 0xFF;
      if (j == i)
      {
        buf.append(base64EncodeChars[(k >>> 2)]);
        buf.append(base64EncodeChars[((k & 0x3) << 4)]);
        buf.append("==");
        break;
      }
      int m = paramArrayOfByte[(j++)] & 0xFF;
      if (j == i)
      {
        buf.append(base64EncodeChars[(k >>> 2)]);
        buf.append(base64EncodeChars[((k & 0x3) << 4 | (m & 0xF0) >>> 4)]);
        
        buf.append(base64EncodeChars[((m & 0xF) << 2)]);
        buf.append("=");
        break;
      }
      int n = paramArrayOfByte[(j++)] & 0xFF;
      buf.append(base64EncodeChars[(k >>> 2)]);
      buf.append(base64EncodeChars[((k & 0x3) << 4 | (m & 0xF0) >>> 4)]);
      
      buf.append(base64EncodeChars[((m & 0xF) << 2 | (n & 0xC0) >>> 6)]);
      
      buf.append(base64EncodeChars[(n & 0x3F)]);
    }
    return buf.toString();
  }

  public static String base64(byte[] data)
  {
    try
    {
      return encodeBase64(data);
    }
    catch (Throwable localThrowable)
    {
      BasicLogHandler.a(localThrowable, "Encrypt", "encodeBase64");
    }
    return null;
  }
  public static String parseByte2HexStr(byte buf[]) {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < buf.length; i++) {
      String hex = Integer.toHexString(buf[i] & 0xFF);
      if (hex.length() == 1) {
        hex = '0' + hex;
      }
      sb.append(hex.toUpperCase());
    }
    return sb.toString();
  }

}
