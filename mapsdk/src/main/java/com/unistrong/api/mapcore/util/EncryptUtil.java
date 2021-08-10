package com.unistrong.api.mapcore.util;

import java.io.UnsupportedEncodingException;

class EncryptUtil
{
  static String a(String paramString)
  {
    if (paramString == null) {
      return null;
    }
    byte[] bytes = null;
    try
    {
      bytes = paramString.getBytes("UTF-8");
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      bytes = paramString.getBytes();
    }
    String str1 = Encrypt.base64(bytes);
    String str2 = (char)(str1.length() % 26 + 65) + str1;
    
    return str2;
  }
  
  static String b(String paramString)
  {
    if (paramString.length() < 2) {
      return "";
    }
    return Encrypt.base64Dec(paramString.substring(1));
  }
}
