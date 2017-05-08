/*
 *--------------------------------------
 * Apusic (Kingdee Middleware)
 *---------------------------------------
 * Copyright By Apusic ,All right Reserved
 * chenpengliang   2015-1-5   comment
 * chenpengliang  2015-1-5  Created
 */
package com.wonderzhou.util.security;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

import org.apache.commons.codec.binary.Base64;

/**
 * 安全工具
 * @author chenpengliang
 *
 */
public abstract class SecurityUtil {

    
    /**
     * 计算流的md5
     * @param inputStream
     * @return
     * @throws Exception
     */
    public static String calculateMd5(InputStream inputStream) throws Exception{
        byte[] b = createChecksum(inputStream);
        String result = "";

        for (int i=0; i < b.length; i++) {
            result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
        }
        return result;
    }
    
    /**
     * 计算文件MD5
     * @param file
     * @return
     */
    public static String calculateMd5(File file) throws Exception {
        return calculateMd5(new FileInputStream(file));
    }

    
    /**
     * 计算md5
     * @param inputStream
     * @return
     * @throws Exception
     */
    private static byte[] createChecksum(InputStream inputStream) throws Exception {
        try {
            byte[] buffer = new byte[1024];
            StringBuffer sb = new StringBuffer();
            sb.append("M").append("D").append("5");
            MessageDigest complete = MessageDigest.getInstance(sb.toString());
            int numRead;
            
            do {
                numRead = inputStream.read(buffer);
                if (numRead > 0) {
                    complete.update(buffer, 0, numRead);
                }
            } while (numRead != -1);
            return complete.digest();
        } catch (Exception e) {
            throw new Exception(e);
        }finally {
            inputStream.close();
        }
    }
    
    /**
     * 加密
     * @param inputStr
     * @return
     */
    public static String encryptSha256(String inputStr) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte digest[] = md.digest(inputStr.getBytes("UTF-8"));
            return new String(Base64.encodeBase64(digest));
        } catch (Exception e) {
            return null;
        }
    }
    
    public static String decodeBase64(String inputStr) throws UnsupportedEncodingException{
        return new String(Base64.decodeBase64(inputStr.getBytes("utf8")), "utf8");
    }
    
}
