package com.lxy.common.util;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SecureRandom;

/**
 * 3DES对称加密工具
 * @author jiacheng yang.
 * @since 2021/5/16 14:08
 * @version 1.0
 */

@Slf4j
@Component
public class DESUtil {

    private static final String ALGORITHM = "DESede";
    private static final String PARAM = "DESede/CBC/PKCS5Padding";
    private static final String ENCODING = "utf-8";

    @Value("${crypto.des.secret-key}")
    private String secretKeyConfig;

    private Key key;

    @PostConstruct
    public void init() {
        this.key = generateKey(secretKeyConfig);
    }

    /**
     * 生成 Key
     */
    private Key generateKey(String secret) {
        try {
            if (secret == null || secret.length() < 24) {
                throw new IllegalArgumentException("3DES 密钥长度不得小于 24 位");
            }
            DESedeKeySpec spec = new DESedeKeySpec(secret.getBytes(StandardCharsets.UTF_8));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
            return keyFactory.generateSecret(spec);
        } catch (Exception e) {
            throw new RuntimeException("生成密钥失败", e);
        }
    }

    /**
     * 加密
     * @param plainText 明文
     * @return Base64(IV + 密文)
     */
    public String encode(String plainText) {
        try {
            // 随机生成IV
            byte[] ivBytes = new byte[8];
            new SecureRandom().nextBytes(ivBytes);
            IvParameterSpec iv = new IvParameterSpec(ivBytes);

            Cipher cipher = Cipher.getInstance(PARAM);
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);

            byte[] encryptData = cipher.doFinal(plainText.getBytes(ENCODING));

            // 拼接 IV + 密文
            byte[] ivAndCipher = new byte[ivBytes.length + encryptData.length];
            System.arraycopy(ivBytes, 0, ivAndCipher, 0, ivBytes.length);
            System.arraycopy(encryptData, 0, ivAndCipher, ivBytes.length, encryptData.length);

            return Base64.encodeBase64String(ivAndCipher);
        } catch (Exception e) {
            log.error("加密失败", e);
            return null;
        }
    }

    /**
     * 解密
     * @param encryptText Base64(IV + 密文)
     */
    public String decode(String encryptText) {
        try {
            byte[] ivAndCipher = Base64.decodeBase64(encryptText);

            // 提取 IV
            byte[] ivBytes = new byte[8];
            System.arraycopy(ivAndCipher, 0, ivBytes, 0, ivBytes.length);
            IvParameterSpec iv = new IvParameterSpec(ivBytes);

            // 提取密文
            byte[] cipherBytes = new byte[ivAndCipher.length - ivBytes.length];
            System.arraycopy(ivAndCipher, ivBytes.length, cipherBytes, 0, cipherBytes.length);

            Cipher cipher = Cipher.getInstance(PARAM);
            cipher.init(Cipher.DECRYPT_MODE, key, iv);

            byte[] decryptData = cipher.doFinal(cipherBytes);
            return new String(decryptData, ENCODING);
        } catch (Exception e) {
            log.error("解密失败", e);
            return null;
        }
    }
}
