package com.bluetron.nb.common.util.codec;

import cn.hutool.crypto.SecureUtil;

import java.io.UnsupportedEncodingException;


public class AESUtils {
    
    /**
     * AES CBC 7padding 加密
     * @param password 原文
     * @param privatekey 密钥 String
     * @return
     */
    public static String aesCBCEncrypt(String password , String privatekey) {
        
        try {
            return SecureUtil.aes(privatekey.getBytes("UTF-8")).encryptBase64(password);
        } catch (UnsupportedEncodingException e) {
            return password;
        }
    }
    
    
    /**
     * AES CBC CBC 7padding 解密
     * @param encryPassword 密文
     * @param privatekey 密钥 String
     * @return
     */
    public static String aesCBCDecrypt(String encryPassword , String privatekey) {
        try {
            return SecureUtil.aes(privatekey.getBytes("UTF-8")).decryptStr(encryPassword);
        } catch (UnsupportedEncodingException e) {
            return encryPassword;
        }
    }
    
}
