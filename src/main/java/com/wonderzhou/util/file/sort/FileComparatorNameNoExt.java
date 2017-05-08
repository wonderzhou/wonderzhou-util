package com.wonderzhou.util.file.sort;

import java.io.File;

/**
 * 按文件名称,排除后缀
 */
public class FileComparatorNameNoExt implements FileComparator {

    private static final long serialVersionUID = -8961499217460871791L;

    @Override
    public int compare(File o1, File o2) {
        return getFileNameNoExt(o1.getName()).compareTo(getFileNameNoExt(o2.getName()));
    }
    
    public static String getFileNameNoExt(String fileName) {   
        return fileName.replaceFirst("^(.*)\\.[^\\.]*$", "$1");   
    } 

}
