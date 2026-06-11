package com.lxy.common.util;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 加密工具类(不可逆)
 *
 * @author jiacheng yang.
 * @version 1.0
 * @since 2021/5/19 0:24
 */

@Slf4j
public class EncryptUtil {
    /**
     * 16位数组
     */
    private static final char[] SALT = {'1', '3', '5', '7', '9', 'a', 'c', 'e', 'g', 'i', 'k', 'm', 'o', 'q', 's', 'u'};

    /**
     * 自定义字符映射的 MD5 加密。
     */
    public static String encryptMd5Salt(String origin) {
        try {
            byte[] originBytes = origin.getBytes(StandardCharsets.UTF_8);
            // 获取md5加密对象
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(originBytes);
            byte[] digest = md5.digest();
            char[] chars = new char[digest.length * 2];
            for (int i = 0, k = 0; i < digest.length; i++) {
                byte byte0 = digest[i];
                // 加盐
                chars[k++] = SALT[byte0 >>> 4 & 0xf];
                chars[k++] = SALT[byte0 & 0xf];
            }
            return new String(chars);
        } catch (Exception e) {
            log.error("MD5加密失败", e);
            return null;
        }
    }

    /**
     * 标准 MD5 加密，返回 32 位小写十六进制字符串。
     *
     * @param origin 待加密内容
     * @return 32 位小写 MD5 字符串
     */
    public static String encryptMd5(String origin) {
        try {
            byte[] originBytes = origin.getBytes(StandardCharsets.UTF_8);
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(originBytes);
            byte[] digest = md5.digest();
            StringBuilder stringBuilder = new StringBuilder(digest.length * 2);
            for (byte digestByte : digest) {
                String hex = Integer.toHexString(digestByte & 0xFF);
                if (hex.length() == 1) {
                    // 标准 32 位 MD5 字符串要求每个字节不足两位时补 0。
                    stringBuilder.append("0");
                }
                stringBuilder.append(hex);
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            log.error("MD5加密失败", e);
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
            log.error("SHA-256加密失败", e);
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
            log.error("SHA-256加密失败", e);
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        String password = "ec55acfb64ef12add1a4caeaa319e8bc" + "1300" + System.currentTimeMillis();
        System.out.println(encryptMd5(password));
        System.out.println(encryptMd5Salt(password));
    }

}
