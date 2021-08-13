package com.example.android_supervisor.utils;


/**
 * 密码工具类
 */
//public class PasswordUtils {
//    private static final String KEY_ALGORITHM = "AES";
//    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/CBC/NOPadding";
//
//    @Value("${security.encode.key:1234567887654321}")
//    public static String DEFAULT_DECODE_KEY="1234567887654321";
//
//    /**
//     * 解密 前段密码
//     *
//     * @param decodePass
//     * @param decodeKey
//     * @return
//     * @throws Exception
//     */
//    public static String decodeAES(String decodePass, String decodeKey) throws Exception {
//        if(UnionUtils.isEmpty(decodeKey)) {
//            decodeKey= DEFAULT_DECODE_KEY;
//        }
//        Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
//        SecretKeySpec keyspec = new SecretKeySpec(decodeKey.getBytes(), KEY_ALGORITHM);
//        IvParameterSpec ivspec = new IvParameterSpec(decodeKey.getBytes());
//        cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
//        byte[] result = cipher.doFinal(Base64.decode(decodePass.getBytes(CharsetUtil.UTF_8)));
//        return new String(result, CharsetUtil.UTF_8);
//    }
//
//    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//
//    /**
//     * 加密
//     * @param password 带加密的密码字符串
//     * @return
//     */
//    public static String encode(String password){
//        return passwordEncoder.encode(password);
//    }
//
//    /**
//     * 判断密码是否匹配
//     * @param toMatchPassword 已经加密的字符串
//     * @param hadEncodePassword 待匹配的密码字符串
//     * @return
//     */
//    public static boolean matches(String toMatchPassword,String hadEncodePassword){
//       return passwordEncoder.matches(toMatchPassword,hadEncodePassword);
//    }
//
//}
