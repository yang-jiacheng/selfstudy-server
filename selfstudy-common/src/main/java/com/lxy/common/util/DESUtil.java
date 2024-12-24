package com.lxy.common.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.security.Key;

/**
 * @Description: 3DES对称加密工具
 * @author: jiacheng yang.
 * @Date: 2021/5/16 14:08
 * @Version: 1.0
 */
public class DESUtil {
    //算法
    private static final String ALGORITHM = "DESede";
    //参数：第一段是加密算法的名称，第二段是分组加密的模式，第三段是指最后一个分组的填充方式
    private static final String PARAM = "DESede/CBC/PKCS5Padding";
    // 密钥 长度不得小于24
    private final static String SECRET_KEY = "lanshan-edu-ycyk-2022-java" ;
    // 向量 可有可无 终端后台也要约定
    private final static String IV = "20220802";
    // 加解密统一使用的编码方式
    private final static String ENCODING = "utf-8";
    //密钥种子
    private static final Key KEY;

    static {
        try {
            //DES算法策略
            DESedeKeySpec spec = new DESedeKeySpec(SECRET_KEY.getBytes());
            //密钥工厂
            SecretKeyFactory keyfactory = SecretKeyFactory.getInstance(ALGORITHM);
            //获取密钥种子
            KEY = keyfactory.generateSecret(spec);
        }catch (Exception e){
            throw new RuntimeException("生成密钥种子失败",e);
        }
    }

    /**
     * 加密
     * @param plainText 普通文本
     */
    public static String encode(String plainText) {
        try {
            Cipher cipher = Cipher.getInstance(PARAM);
            IvParameterSpec ips = new IvParameterSpec( IV.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, KEY, ips);
            byte[] encryptData = cipher.doFinal( plainText.getBytes(ENCODING));
            return Base64.encodeBase64String(encryptData);
        }catch (Exception e){
            throw new RuntimeException("加密失败",e);
        }
    }

    /**
     * 解密
     * @param encryptText 加密文本
     */
    public static String decode(String encryptText) {
        try {
            Cipher cipher = Cipher.getInstance(PARAM);
            IvParameterSpec ips = new IvParameterSpec(IV.getBytes());
            cipher. init(Cipher.DECRYPT_MODE, KEY, ips);
            byte[] decryptData = cipher.doFinal(Base64.decodeBase64(encryptText));
            return new String( decryptData, ENCODING);
        }catch (Exception e){
            throw new RuntimeException("解密失败", e);
        }
    }

    public static void main(String[] args) {
        String plainText = "hello world";
        String encrypted = encode(plainText);
        System.out.println("加密后的文本："+encrypted);
        String decrypted = decode(encrypted);
        System.out.println("解密后的文本："+decrypted);
    }

}
