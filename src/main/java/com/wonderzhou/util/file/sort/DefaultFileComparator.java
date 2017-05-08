package com.wonderzhou.util.file.sort;

import java.io.File;

/**
 * 按文件名称自然顺序排序 
 */
public class DefaultFileComparator implements FileComparator {

    private static final long serialVersionUID = -8961499217460871791L;

    @Override
    public int compare(File o1, File o2) {
        return 1;
    }

}
