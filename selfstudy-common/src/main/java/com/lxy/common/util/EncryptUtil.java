package com.lxy.common.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 加密工具类(不可逆)
 * @author jiacheng yang.
 * @since 2021/5/19 0:24
 * @version 1.0
 */
public class EncryptUtil {
    /**
     * 16位数组
     */
    private static final char[] SALT = {
            '1', '3', '5', '7', '9', 'a', 'c', 'e', 'g', 'i', 'k', 'm', 'o', 'q', 's', 'u'
    };

    /**
     * MD5加密
     */
    public static String encryptMd5(String origin) {
        try {
            byte[] originBytes = origin.getBytes(StandardCharsets.UTF_8);
            //获取md5加密对象
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(originBytes);
            byte[] digest = md5.digest();
            char[] chars = new char[digest.length * 2];
            for (int i = 0, k = 0; i < digest.length; i++) {
                byte byte0 = digest[i];
                //加盐
                chars[k++] = SALT[byte0 >>> 4 & 0xf];
                chars[k++] = SALT[byte0 & 0xf];
            }
            return new String(chars);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * SHA-256加密
     */
    public static String encryptSha256(String str) {
        try {
            byte[] originBytes = str.getBytes(StandardCharsets.UTF_8);
            MessageDigest sha_256 = MessageDigest.getInstance("SHA-256");
            sha_256.update(originBytes);
            byte[] bytes = sha_256.digest();
            StringBuilder stringBuilder = new StringBuilder();
            String temp = null;
            for (byte aByte : bytes) {
                temp = Integer.toHexString(aByte & 0xFF);
                if (temp.length() == 1) {
                    // 1得到一位的进行补0操作
                    stringBuilder.append("0");
                }
                stringBuilder.append(temp);
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String encryptSha256Salt(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(str.getBytes(StandardCharsets.UTF_8));
            md.update(new String(SALT).getBytes(StandardCharsets.UTF_8));
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static void main(String[] args) {
        String password = "123456";
        System.out.println(encryptMd5(password));
        System.out.println(encryptSha256(password));
        System.out.println(encryptSha256Salt(password));
    }

}
