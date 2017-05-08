package com.wonderzhou.util.code;

import java.util.Random;

public class CodeGenerator {
    private static final char[] SECRET_CODE = "1QAZ2WSX3EDC4RFV5TGB6YHN7UJM8IK9OL0P".toCharArray();
    private static Random RANDOM = new Random();
    private static int MAX_TYPE_LENGTH = 6;

    /**
     * 为指定的类型生成编码
     * 
     * @param type 类型字符串，作为编码的前缀
     * @param length 编码长度
     * @return
     */
    public static String getCode(String type, int length) {
        if (type.length() > MAX_TYPE_LENGTH) {
            type = type.substring(0, MAX_TYPE_LENGTH);
        }
        StringBuilder codeBuilder = new StringBuilder(length);
        codeBuilder.append(type).append('_');
        for (int i = type.length(); i < length - 1; i++) {
            int index = RANDOM.nextInt(SECRET_CODE.length);
            codeBuilder.append(SECRET_CODE[index]);
        }
        return codeBuilder.toString();
    }
    
    public static void main(String[] args) {
        String code = CodeGenerator.getCode("FILE", 16);
        System.out.println(code);
    }

}
