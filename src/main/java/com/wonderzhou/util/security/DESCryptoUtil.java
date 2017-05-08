/*
 *--------------------------------------
 * Apusic (Kingdee Middleware)
 *---------------------------------------
 * Copyright By Apusic ,All right Reserved
 * linxueqin   2015-6-27   comment
 * linxueqin  2015-6-27  Created
 */
package com.wonderzhou.util.security;


import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;



/**
 * DES 工具类
 * @author linxueqin
 * 
 */
public class DESCryptoUtil {

    /**
     * 
     */
    protected DESCryptoUtil() {
        // TODO Auto-generated constructor stub
    }

    /**
     * 加密
     * @param datasource
     * @param password
     * @return
     * @throws Exception
     */
    public static byte[] encrypt(byte[] datasource, String password) throws Exception {
        Cipher cipher = createEncryptCipher(password);
        // 正式执行加密操作
        return cipher.doFinal(datasource);
    }
   

    /**
     * 解密
     * @param src
     * @param password
     * @return
     * @throws Exception
     */
    public static byte[] decrypt(byte[] src, String password) throws Exception {
        Cipher cipher = createDecryptCipher(password);
        // 真正开始解密操作
        return cipher.doFinal(src);
    }

    /**
     * @param text
     * @param password
     * @return
     * @throws Exception
     */
    public static String encrypt(String text, String password) throws Exception {
        byte[] tmp = encrypt(text.getBytes("utf8"), password);
        return new String(Base64.encodeBase64(tmp), "utf8").replace("+", "-").replace("/", "_").replace("\n", "").replace("\r", "");
    }
    
    /**
     * @param text
     * @param password
     * @return
     * @throws Exception
     */
    public static String decrypt(String text, String password) throws Exception {
        byte[] tmp = Base64.decodeBase64(text.replace("-", "+").replace("_", "/").getBytes("utf8"));
        return new String(decrypt(tmp, password), "utf8");
    }

    
    /**
     * @param password
     * @return
     * @throws Exception
     */
    public static Cipher createEncryptCipher(String password) throws Exception {
        return createCipher(password, Cipher.ENCRYPT_MODE);
    }
    
    /**
     * @param password
     * @return
     * @throws Exception
     */
    public static Cipher createDecryptCipher(String password) throws Exception {
        return createCipher(password, Cipher.DECRYPT_MODE);
    }
    
    /**
     * @param password
     * @param opmode
     * @return
     * @throws Exception
     */
    public static Cipher createCipher(String password, int opmode) throws Exception {
    	StringBuffer s=new StringBuffer();
    	s.append("M").append("D").append("5");
    	MessageDigest digest = MessageDigest.getInstance(s.toString());
        digest.update(password.getBytes("utf8"));
        byte[] raw = digest.digest();
        
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(opmode, skeySpec);
        return cipher;
    }
}
