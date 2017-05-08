package com.wonderzhou.util.file.sort;

import java.io.File;

/**
 * 按文件名称
 */
public class FileComparatorPath implements FileComparator {

    private static final long serialVersionUID = -8961499217460871791L;

    @Override
    public int compare(File o1, File o2) {
        return o1.getAbsolutePath().compareTo(o2.getAbsolutePath());
    }
    
}
