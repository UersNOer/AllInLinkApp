package com.example.android_supervisor.utils;

import android.util.Base64;

import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author wujie
 */
public class AESUtils {

    public static String encode(String text, String key, String iv) {
        byte[] encodedBytes = encode(getUtf8(text), getLatin(key), getLatin(iv));
        return encodedBytes == null ? null : Base64.encodeToString(encodedBytes, Base64.NO_WRAP);
    }

    private static byte[] encode(byte[] source, byte[] key, byte[] iv) {
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/ZeroBytePadding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            return cipher.doFinal(source);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decode(String text, String key, String iv) {
        byte[] encodedBytes = Base64.decode(text, Base64.NO_WRAP);
        byte[] bytes = decode(encodedBytes, getLatin(key), getLatin(iv));
        return bytes == null ? null : getUtf8(bytes);
    }

    private static byte[] decode(byte[] encodedBytes, byte[] key, byte[] iv) {
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/ZeroBytePadding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            return cipher.doFinal(encodedBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] getLatin(String text) {
        byte[] bytes;
        try {
            bytes = text.getBytes("latin1");
        } catch (UnsupportedEncodingException e) {
            bytes = text.getBytes();
        }
        return bytes;
    }

    private static byte[] getUtf8(String text) {
        byte[] bytes;
        try {
            bytes = text.getBytes("latin1");
        } catch (UnsupportedEncodingException e) {
            bytes = text.getBytes();
        }
        return bytes;
    }

    private static String getUtf8(byte[] bytes) {
        String text;
        try {
            text = new String(bytes, "latin1");
        } catch (UnsupportedEncodingException e) {
            text = new String();
        }
        return text;
    }
}
