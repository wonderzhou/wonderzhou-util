package com.wonderzhou.util.file.sort;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * 文件排序接口（jackson序列化接口默认不带类信息,导致反序列化出错,需要加上@JsonTypeInfo注解,并且要有默认的构造函数,相关的属性需有gettter和setter方法）
 */
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.WRAPPER_OBJECT, property="@class")
public interface FileComparator extends Comparator<File>, Serializable {
    
}
