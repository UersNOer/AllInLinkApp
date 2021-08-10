package com.unistrong.api.mapcore.util;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class ClientInfo//bo
{
    public static String initXInfo(Context context, SDKInfo sdkInfo, Map<String, String> paramMap, boolean paramBoolean) {
        try {
            ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
            String deviceID = DeviceInfo.getDeviceID(context);

            writeField(localByteArrayOutputStream, deviceID);

            String deviceMac = DeviceInfo.getDeviceMac(context);
            writeField(localByteArrayOutputStream, deviceMac);

            String utdid = DeviceInfo.getUTDID(context);
            if (utdid == null) {
                utdid = "";
            }
            writeField(localByteArrayOutputStream, utdid);

            String packageName = AppInfo.getPackageName(context);

            writeField(localByteArrayOutputStream, packageName);

            String model = Build.MODEL;

            writeField(localByteArrayOutputStream, model);

            String manufacturer = Build.MANUFACTURER;//厂商

            writeField(localByteArrayOutputStream, manufacturer);

            String device = Build.DEVICE;

            writeField(localByteArrayOutputStream, device);

            String appName = AppInfo.getApplicationName(context);

            writeField(localByteArrayOutputStream, appName);

            String appVersion = AppInfo.getApplicationVersion(context);

            writeField(localByteArrayOutputStream, appVersion);

            String sdkversion = String.valueOf(Build.VERSION.SDK_INT);

            writeField(localByteArrayOutputStream, sdkversion);

            String imsi = DeviceInfo.getSubscriberId1(context);

            writeField(localByteArrayOutputStream, imsi);

            String width_height = DeviceInfo.getReslution(context);//屏幕分辨率=x*y

            writeField(localByteArrayOutputStream, width_height);

            String netWorkType = DeviceInfo.getActiveNetWorkType(context) + "";//当前网络类型WIFI OR 3G 4G

            writeField(localByteArrayOutputStream, netWorkType);

            String str14 = DeviceInfo.getNetWorkType(context) + "";//当前使用的网络类型

            writeField(localByteArrayOutputStream, str14);

            String operatorName = DeviceInfo.getNetworkOperatorName(context);////运营商名称

            writeField(localByteArrayOutputStream, operatorName);

            String mnc = DeviceInfo.getMNC(context); //
            writeField(localByteArrayOutputStream, mnc);
            if (paramBoolean) {
                writeField(localByteArrayOutputStream, "");
            } else {
                writeField(localByteArrayOutputStream, DeviceInfo.getWifiMacs(context));
            }
            if (paramBoolean) {
                writeField(localByteArrayOutputStream, "");
            } else {
                writeField(localByteArrayOutputStream, DeviceInfo.g(context));
            }
            if (paramBoolean) {
                writeField(localByteArrayOutputStream, "");

                writeField(localByteArrayOutputStream, "");
            } else {
                String[] localObject1 = DeviceInfo.cellInfo(context);

                writeField(localByteArrayOutputStream, localObject1[0]);

                writeField(localByteArrayOutputStream, localObject1[1]);
            }

            String uuid=DeviceInfo.getMyUUID(context);//基于时间生成一种id
            writeField(localByteArrayOutputStream, uuid);

            String android_id=DeviceInfo.getAndroidID(context);//android_id
            writeField(localByteArrayOutputStream, android_id);

            String deviceVersion=Build.ID;//设备版本
            writeField(localByteArrayOutputStream, deviceVersion);

            String serial=Build.SERIAL;//序列号
            writeField(localByteArrayOutputStream, serial);

            String cpuFreq=DeviceInfo.getCpuAndRom();//cpu频率，ram、rom大小
            writeField(localByteArrayOutputStream, cpuFreq);

            String fingerprint=Build.FINGERPRINT;//多个信息拼成的唯一标识
            writeField(localByteArrayOutputStream, fingerprint);

            byte[] clientBytes1 = localByteArrayOutputStream.toByteArray();
            String cinfo = new String(clientBytes1);
            cinfo = URLEncoder.encode(cinfo);
            clientBytes1 = cinfo.getBytes();
            byte[] arrayOfByte1 = Utils.gZip((byte[]) clientBytes1);

            PublicKey publicKey = RSAUtils.getPublicKey();
            byte[] arrayOfByte2 = null;
            Object localObject2;
            if (arrayOfByte1.length > 117) {
                localObject2 = new byte[117];

                System.arraycopy(arrayOfByte1, 0, localObject2, 0, 117);

                byte[] arrayOfByte3 = RSAUtils.encryptByPublicKey((byte[]) localObject2, (RSAPublicKey) publicKey);

                arrayOfByte2 = new byte[128 + arrayOfByte1.length - 117];

                System.arraycopy(arrayOfByte3, 0, arrayOfByte2, 0, 128);

                System.arraycopy(arrayOfByte1, 117, arrayOfByte2, 128, arrayOfByte1.length - 117);
            } else {
                arrayOfByte2 = RSAUtils.encryptByPublicKey(arrayOfByte1, (RSAPublicKey) publicKey);
            }
            return Encrypt.base64(arrayOfByte2);
        } catch (Throwable throwable) {
            BasicLogHandler.a(throwable, "CInfo", "InitXInfo");
        }
        return null;
    }

//    static String paramEncrypt(Context context, byte[] data)
//            throws InvalidKeyException, IOException, InvalidKeySpecException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, CertificateException {
//        KeyGenerator localKeyGenerator = null;
//        localKeyGenerator = KeyGenerator.getInstance("AES");
//        if (localKeyGenerator == null) {
//            return null;
//        }
//        localKeyGenerator.init(256);
//        SecretKey localSecretKey = localKeyGenerator.generateKey();
//        byte[] arrayOfByte1 = localSecretKey.getEncoded();
//
//        PublicKey localPublicKey = null;
//
//        byte[] arrayOfByte2 = null;
//        byte[] arrayOfByte3 = null;
//        byte[] arrayOfByte4 = null;
//
//        localPublicKey = Utils.getPublicKey(context);
//        if (localPublicKey == null) {
//            return null;
//        }
//        arrayOfByte2 = Encrypt.rsaEncrypt(arrayOfByte1, localPublicKey);
//
//        arrayOfByte3 = Encrypt.aesEncrypt(arrayOfByte1, data);
//
//        arrayOfByte4 = new byte[arrayOfByte2.length + arrayOfByte3.length];
//
//        System.arraycopy(arrayOfByte2, 0, arrayOfByte4, 0, arrayOfByte2.length);
//
//        System.arraycopy(arrayOfByte3, 0, arrayOfByte4, arrayOfByte2.length, arrayOfByte3.length);
//
//        byte[] arrayOfByte5 = Utils.gZip(arrayOfByte4);
//        if (arrayOfByte5 != null) {
//            return Encrypt.base64(arrayOfByte5);
//        }
//        return "";
//    }



    public static String encrypt(byte byteContent[], String password) throws InvalidKeyException, IOException, InvalidKeySpecException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, CertificateException, NoSuchProviderException {
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom random = null;
        if (android.os.Build.VERSION.SDK_INT >= 17) {
            random = SecureRandom.getInstance("SHA1PRNG", "Crypto");
        } else {
            random = SecureRandom.getInstance("SHA1PRNG");
        }
        random.setSeed(password.getBytes());
        kgen.init(128, random);
//                    kgen.init(128, new SecureRandom(password.getBytes()));
        SecretKey secretKey = kgen.generateKey();
        byte[] enCodeFormat = secretKey.getEncoded();
        SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
        Cipher cipher = Cipher.getInstance("AES");// 创建密码器
//      byte[] byteContent = content.getBytes("utf-8");
        cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
        byte[] result = cipher.doFinal(byteContent);
        if (result != null) {
            return DES.parseByte2HexStr(result);
        }
        return ""; // 加密

    }

    /**
     * 解密
     *
     * @param content  待解密内容
     * @param password 解密密钥
     * @return
     */
    public static byte[] decrypt(byte[] content, String password) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom random = null;
            if (android.os.Build.VERSION.SDK_INT >= 17) {
                random = SecureRandom.getInstance("SHA1PRNG", "Crypto");
            } else {
                random = SecureRandom.getInstance("SHA1PRNG");
            }
            random.setSeed(password.getBytes());
            kgen.init(128, random);

            //  kgen.init(128, new SecureRandom(password.getBytes()));
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");// 创建密码器
            cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
            byte[] result = cipher.doFinal(content);
            return result; // 加密
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e){
            e.printStackTrace();
        }
        return null;
    }

    public static final String k = "quhedgetbtomikjfh912874765565609128745";

    public static String AES(byte[] data) {
        try {
//      return paramEncrypt(context, data);
            return encrypt(data, k);
        } catch (InvalidKeyException localInvalidKeyException) {
            BasicLogHandler.a(localInvalidKeyException, "CInfo", "AESData");
        } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
            BasicLogHandler.a(localNoSuchAlgorithmException, "CInfo", "AESData");
        } catch (NoSuchPaddingException localNoSuchPaddingException) {
            BasicLogHandler.a(localNoSuchPaddingException, "CInfo", "AESData");
        } catch (IllegalBlockSizeException localIllegalBlockSizeException) {
            BasicLogHandler.a(localIllegalBlockSizeException, "CInfo", "AESData");
        } catch (BadPaddingException localBadPaddingException) {
            BasicLogHandler.a(localBadPaddingException, "CInfo", "AESData");
        } catch (InvalidKeySpecException localInvalidKeySpecException) {
            BasicLogHandler.a(localInvalidKeySpecException, "CInfo", "AESData");
        } catch (CertificateException localCertificateException) {
            BasicLogHandler.a(localCertificateException, "CInfo", "AESData");
        } catch (IOException localIOException) {
            BasicLogHandler.a(localIOException, "CInfo", "AESData");
        } catch (Throwable localThrowable) {
            BasicLogHandler.a(localThrowable, "CInfo", "AESData");
        }
        return "";
    }

    public static String getClientInfo(Context context, SDKInfo sdkInfo) {
        StringBuilder sb = new StringBuilder();
        try {
            String str = DeviceInfo.e(context);
            sb.append("\"sim\":\"").append(str).append("\",\"username\":\"").append(sdkInfo.username).append("\",\"sdkver\":\"").append(sdkInfo.version).append("\",\"product\":\"").append(sdkInfo.product).append("\",\"child\":\"").append(sdkInfo.d()).append("\",\"wnt\":\"").append(DeviceInfo.getNetworkType(context)).append("\",\"netype\":\"").append(DeviceInfo.getNetworkOperatorName(context)).append("\",\"mnc\":\"").append(DeviceInfo.b(context)).append("\",\"nct\":\"").append(DeviceInfo.getNetType(context)).append("\"");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return sb.toString();
    }

    private static void writeField(ByteArrayOutputStream outputStream, String value) {
        if (!TextUtils.isEmpty(value)) {
            int i = value.getBytes().length;
            byte b = 0;
            if (i > 255) {
                b = -1;
            } else {
                b = (byte) value.getBytes().length;
            }
            try {
                writeField(outputStream, b, value.getBytes("UTF-8"));
            } catch (UnsupportedEncodingException localUnsupportedEncodingException) {
                writeField(outputStream, b, value.getBytes());
            }
        } else {
            writeField(outputStream, (byte) 0, new byte[0]);
        }
    }

    private static void writeField(ByteArrayOutputStream outputStream, byte paramByte, byte[] paramArrayOfByte) {
        try {
            outputStream.write(new byte[]{paramByte});
            if (((paramByte > 0 ? 1 : 0) & ((paramByte & 0xFF) < 255 ? 1 : 0)) != 0) {
                outputStream.write(paramArrayOfByte);
            } else if ((paramByte & 0xFF) == 255) {
                outputStream.write(paramArrayOfByte, 0, 255);
            }
        } catch (IOException localIOException) {
            BasicLogHandler.a(localIOException, "CInfo", "writeField");
        }
    }

    public static String getTS() {
        String time = null;
        try {
            time = String.valueOf(System.currentTimeMillis());
            String str2 = "1";
            int i = time.length();

            time = time.substring(0, i - 2) + str2 + time.substring(i - 1);
        } catch (Throwable localThrowable) {
            BasicLogHandler.a(localThrowable, "CInfo", "getTS");
        }
        return time;
    }

    public static String Scode(Context paramContext, String ts, String paramString2) {
        String str1 = null;
        try {
            String sha1AndPackage = AppInfo.getSHA1AndPackage(paramContext);
            str1 = MD5.encryptString(sha1AndPackage + ":" + ts);
        } catch (Throwable localThrowable) {
            BasicLogHandler.a(localThrowable, "CInfo", "Scode");
        }
        return str1;
    }

}
