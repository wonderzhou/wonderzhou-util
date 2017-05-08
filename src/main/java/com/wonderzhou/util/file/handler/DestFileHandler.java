package com.wonderzhou.util.file.handler;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * 目标文件处理（jackson序列化接口默认不带类信息,导致反序列化出错,需要加上@JsonTypeInfo注解,并且要有默认的构造函数,相关的属性需有gettter和setter方法）
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.WRAPPER_OBJECT, property="@class")
public interface DestFileHandler extends Serializable {

    /**
     * 目的端文件处理
     * 
     * @param srcPath 文件路径
     * @param destDirectory 目标目录（比如：e:/A）
     * @param fileName 目标文件名 ，比如：a.txt或者A/a.txt
     */
    public void handler(String srcPath, String destDirectory, String fileName) throws Exception;

}
