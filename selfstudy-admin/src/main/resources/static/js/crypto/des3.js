// 密钥 长度不得小于24
const SECRET_KEY = "lanshan-edu-ycyk-2022-java";
// 向量 可有可无 终端后台也要约定
const IV = "20220802";
// 加解密统一使用的编码方式
const ENCODING = "utf-8";

// 将密钥和向量转为 WordArray 类型
const key = CryptoJS.enc.Utf8.parse(SECRET_KEY);
const iv = CryptoJS.enc.Utf8.parse(IV);

/**
 * 加密
 * @param {string} plainText 普通文本
 * @return {string} 加密后的文本
 */
function encode(plainText) {
    const cipher = CryptoJS.algo.TripleDES.createEncryptor(key, {
        iv: iv,
    });
    const ciphertext = cipher.finalize(CryptoJS.enc.Utf8.parse(plainText));
    return CryptoJS.enc.Base64.stringify(ciphertext);
}

/**
 * 解密
 * @param {string} encryptText 加密文本
 * @return {string} 解密后的文本
 */
function decode(encryptText) {
    const cipher = CryptoJS.algo.TripleDES.createDecryptor(key, {
        iv: iv,
    });
    const ciphertext = CryptoJS.enc.Base64.parse(encryptText);
    const decrypted = cipher.finalize(ciphertext);
    return decrypted.toString(CryptoJS.enc.Utf8);
}


const plainText = "hello world";
const encrypted = encode(plainText);
console.log("encrypted:", encrypted); // 加密后的文本
const decrypted = decode(encrypted);
console.log("decrypted:", decrypted); // 解密后的文本