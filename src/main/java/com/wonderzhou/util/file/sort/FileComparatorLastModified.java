package com.wonderzhou.util.file.sort;

import java.io.File;

/**
 * 按文件最后修改时间
 */
public class FileComparatorLastModified implements FileComparator {

    private static final long serialVersionUID = -8961499217460871791L;

    @Override
    public int compare(File o1, File o2) {
        return Long.valueOf(o1.lastModified() - o2.lastModified()).intValue();
    }

}
