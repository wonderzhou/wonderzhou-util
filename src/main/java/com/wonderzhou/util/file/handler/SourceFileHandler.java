package com.wonderzhou.util.file.handler;

import java.io.File;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * 源文件处理接口（jackson序列化接口默认不带类信息,导致反序列化出错,需要加上@JsonTypeInfo注解,并且要有默认的构造函数,相关的属性需有gettter和setter方法）
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.WRAPPER_OBJECT, property="@class")
public interface SourceFileHandler extends Serializable {
    
    /**
     * 文件发送失败
     * @param srcFile 源文件
     * @param fileName 文件名(最终备份的文件名,也可带上相对目录比如：a.txt或者backup/a.txt，如果不需要重命名直接获取源文件的文件名即可)
     * @throws Exception
     */
    public void sendFileFailed(File srcFile, String fileName) throws Exception;
    
    /**
     * 文件发送成功
     * @param srcFile 源文件
     * @param fileName 文件名(最终备份的文件名,也可带上相对目录比如：a.txt或者backup/a.txt，如果不需要重命名直接获取源文件的文件名即可)
     * @throws Exception
     */
    public void sendFileSuccess(File srcFile, String fileName) throws Exception;
    
}
