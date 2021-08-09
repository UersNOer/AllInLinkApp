package com.unistrong.api.mapcore.util;

import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
    public static String getFileMD5(String filename) {
        FileInputStream localFileInputStream = null;
        try {
            if (TextUtils.isEmpty(filename)) {
                return null;
            }
            Object localObject1 = new File(filename);
            if ((!((File) localObject1).isFile()) || (!((File) localObject1).exists())) {
                return null;
            }
            MessageDigest localObject2 = null;

            byte[] arrayOfByte1 = new byte['ࠀ'];

            localObject2 = MessageDigest.getInstance("MD5");
            localFileInputStream = new FileInputStream((File) localObject1);
            int i;
            while ((i = localFileInputStream.read(arrayOfByte1)) != -1) {
                ((MessageDigest) localObject2).update(arrayOfByte1, 0, i);
            }
            byte[] arrayOfByte2 = ((MessageDigest) localObject2).digest();
            return Utils.byte2HexString(arrayOfByte2);
        } catch (Throwable localThrowable) {
            Object localObject2;
            BasicLogHandler.a(localThrowable, "MD5", "getMd5FromFile");
            return null;
        } finally {
            try {
                if (localFileInputStream != null) {
                    localFileInputStream.close();
                }
            } catch (IOException localIOException) {
                BasicLogHandler.a(localIOException, "MD5", "getMd5FromFile");
            }
        }
    }

    public static String MD5AndHex(String paramString) {
        if (paramString == null) {
            return null;
        }
        byte[] arrayOfByte = md5(paramString);
        return Utils.byte2HexString(arrayOfByte);
    }

    public static String a(byte[] paramArrayOfByte) {
        byte[] arrayOfByte = b(paramArrayOfByte);
        return Utils.byte2HexString(arrayOfByte);
    }

    public static String encryptString(String paramString) {
        byte[] arrayOfByte = stringToMD5(paramString);
        return Utils.byte2HexString(arrayOfByte);
    }

    public static byte[] a(byte[] paramArrayOfByte, String paramString) {
        MessageDigest messageDigest = null;
        byte[] arrayOfByte = null;
        try {
            messageDigest = MessageDigest.getInstance(paramString);
            messageDigest.update(paramArrayOfByte);
            arrayOfByte = messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            BasicLogHandler.a(e, "MD5", "getMd5Bytes");
        } catch (Throwable localThrowable) {
            BasicLogHandler.a(localThrowable, "MD5", "getMd5Bytes1");
        }
        return arrayOfByte;
    }

    private static byte[] b(byte[] paramArrayOfByte) {
        return a(paramArrayOfByte, "MD5");
    }

    public static byte[] md5(String paramString) {
        try {
            return toMD5(paramString);
        } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
            BasicLogHandler.a(localNoSuchAlgorithmException, "MD5", "getMd5Bytes");
        } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
            BasicLogHandler.a(localUnsupportedEncodingException, "MD5", "getMd5Bytes");
        } catch (Throwable localThrowable) {
            BasicLogHandler.a(localThrowable, "MD5", "getMd5Bytes");
        }
        return new byte[0];
    }

    private static byte[] stringToMD5(String paramString) {
        try {
            return toMD5(paramString);
        } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
            localNoSuchAlgorithmException.printStackTrace();
        } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
            localUnsupportedEncodingException.printStackTrace();
        } catch (Throwable localThrowable) {
            localThrowable.printStackTrace();
        }
        return new byte[0];
    }

    private static byte[] toMD5(String paramString) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest localMessageDigest = null;
        byte[] arrayOfByte = null;
        if (paramString == null) {
            return null;
        }
        localMessageDigest = MessageDigest.getInstance("MD5");
        localMessageDigest.update(paramString.getBytes("UTF-8"));
        arrayOfByte = localMessageDigest.digest();
        return arrayOfByte;
    }
}
