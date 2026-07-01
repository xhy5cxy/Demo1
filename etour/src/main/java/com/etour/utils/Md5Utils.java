package com.etour.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密工具类
 */
public class Md5Utils {
    
    private static final String SALT = "etour2025";
    
    /**
     * 生成MD5加密字符串
     * @param input 输入字符串
     * @return MD5加密后的字符串
     */
    public static String encode(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 加盐加密
            String saltedInput = input + SALT;
            byte[] bytes = md.digest(saltedInput.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    sb.append('0');
                }
                sb.append(hex);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5加密失败", e);
        }
    }
    
    /**
     * 验证密码是否匹配
     * @param rawPassword 原始密码
     * @param encodedPassword 加密后的密码
     * @return 是否匹配
     */
    public static boolean matches(String rawPassword, String encodedPassword) {
        return encode(rawPassword).equals(encodedPassword);
    }
}