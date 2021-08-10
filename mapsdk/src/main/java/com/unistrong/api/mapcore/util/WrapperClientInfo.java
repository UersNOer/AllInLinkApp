package com.unistrong.api.mapcore.util;

import android.content.Context;
import android.os.Build;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class WrapperClientInfo//by
{
  public static String getPublicInfo(Context paramContext)
  {
    StringBuilder sb = new StringBuilder();
    try
    {
      sb.append("\"key\":\"").append(AppInfo.getKey(paramContext)).append("\",\"platform\":\"0\",\"imei\":\"").append(DeviceInfo.getDeviceID(paramContext)).append("\",\"pkg\":\"").append(AppInfo.getPackageName(paramContext)).append("\",\"model\":\"").append(Build.MODEL).append("\",\"appname\":\"").append(AppInfo.getApplicationName(paramContext)).append("\",\"appver\":\"").append(AppInfo.getApplicationVersion(paramContext)).append("\",\"sysver\":\"").append(Build.VERSION.RELEASE).append("\"");
    }
    catch (Throwable localThrowable)
    {
      BasicLogHandler.a(localThrowable, "CInfo", "getPublicJSONInfo");
    }
    return sb.toString();
  }
  
  public static String aesEncrypt(Context paramContext, byte[] paramArrayOfByte)
  {
    try
    {
      return ClientInfo.encrypt(paramArrayOfByte,ClientInfo.k);
    }
    catch (InvalidKeyException localInvalidKeyException)
    {
      localInvalidKeyException.printStackTrace();
    }
    catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
    {
      localNoSuchAlgorithmException.printStackTrace();
    }
    catch (NoSuchPaddingException localNoSuchPaddingException)
    {
      localNoSuchPaddingException.printStackTrace();
    }
    catch (IllegalBlockSizeException localIllegalBlockSizeException)
    {
      localIllegalBlockSizeException.printStackTrace();
    }
    catch (BadPaddingException localBadPaddingException)
    {
      localBadPaddingException.printStackTrace();
    }
    catch (InvalidKeySpecException localInvalidKeySpecException)
    {
      localInvalidKeySpecException.printStackTrace();
    }
    catch (CertificateException localCertificateException)
    {
      localCertificateException.printStackTrace();
    }
    catch (IOException localIOException)
    {
      localIOException.printStackTrace();
    }
    catch (Throwable localThrowable)
    {
      localThrowable.printStackTrace();
    }
    return "";
  }
}
