const SALT = ['1', '3', '5', '7', '9', 'a', 'c', 'e', 'g', 'i', 'k', 'm', 'o', 'q', 's', 'u'];

// MD5加密
function encryptMd5(origin) {
    try {
        const originBytes = CryptoJS.enc.Utf8.parse(origin);
        const digest = CryptoJS.MD5(originBytes);
        const chars = [];
        for (let i = 0; i < digest.sigBytes; i++) {
            const byte0 = digest.words[Math.floor(i / 4)] >>> (24 - (i % 4) * 8);
            chars.push(SALT[byte0 >>> 4 & 0xf]);
            chars.push(SALT[byte0 & 0xf]);
        }
        return chars.join('');
    } catch (error) {
        console.error(error);
        return null;
    }
}

async function encryptSha256(str) {
    try {
        const encoder = new TextEncoder();
        const originBytes = encoder.encode(str);
        const sha256 = await crypto.subtle.digest('SHA-256', originBytes);
        return Array.from(new Uint8Array(sha256), b => b.toString(16).padStart(2, '0')).join('');
    } catch (e) {
        console.error(e);
        return null;
    }
}

async function encryptSha256Salt(str) {
    try {
        const encoder = new TextEncoder();
        const md = await crypto.subtle.digest('SHA-256', encoder.encode(str + SALT.join('')));
        return Array.from(new Uint8Array(md), b => b.toString(16).padStart(2, '0')).join('');
    } catch (e) {
        console.error(e);
        return null;
    }
}


// 测试
const password = '123456';
console.log( encryptMd5(password));
encryptSha256(password).then((result) => {
    console.log(result);
});
encryptSha256Salt(password).then((result) => {
    console.log(result);
});