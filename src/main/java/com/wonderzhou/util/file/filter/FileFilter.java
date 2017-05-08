package com.wonderzhou.util.file.filter;

import java.io.File;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * 文件过滤接口（jackson序列化接口默认不带类信息,导致反序列化出错,需要加上@JsonTypeInfo注解,并且要有默认的构造函数,相关的属性需有gettter和setter方法）
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.WRAPPER_OBJECT, property="@class")
public interface FileFilter extends Serializable {
    
    /**
     * 文件过滤
     * @param rootFile 根目录
     * @param file 需要判断的文件
     * @return
     */
    public boolean accept(File rootFile, File file);

}
