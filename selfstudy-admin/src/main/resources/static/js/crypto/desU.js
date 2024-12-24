const KEY = CryptoJS.enc.Utf8.parse('lanshan-edu-ycyk-2022-java');
const iv = CryptoJS.enc.Utf8.parse('20220802');
function desEncode(plainText){
    var encrypted = CryptoJS.TripleDES.encrypt(plainText, KEY, {
        iv: iv,
        mode: CryptoJS.mode.CBC,
        padding: CryptoJS.pad.Pkcs7
    }).ciphertext
    return CryptoJS.enc.Base64.stringify(encrypted);
}
function desDecode(encryptText){
    var decrypted = CryptoJS.TripleDES.decrypt({
        ciphertext: CryptoJS.enc.Base64.parse(encryptText)
    }, KEY, {
        iv: iv,
        mode: CryptoJS.mode.CBC,
        padding: CryptoJS.pad.Pkcs7
    });
    return decrypted.toString(CryptoJS.enc.Utf8)
}