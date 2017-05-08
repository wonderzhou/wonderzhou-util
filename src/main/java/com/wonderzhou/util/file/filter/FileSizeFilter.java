package com.wonderzhou.util.file.filter;

import java.io.File;

/**
 * 文件大小过滤条件
 */
public class FileSizeFilter implements FileFilter {
    
    private static final long serialVersionUID = 2949511127816528234L;
    
    private long minSize = -1;
    private long maxSize = Long.MAX_VALUE;
    //排除or包含min~max大小的文件
    private boolean isExclude = false;
    
    public FileSizeFilter() {
        
    }
    
    public FileSizeFilter(long minSize, long maxSize, boolean isExclude) {
        this.minSize = minSize;
        this.maxSize = maxSize;
        this.isExclude = isExclude;
    }
    
    public FileSizeFilter(long minSize, long maxSize) {
        this.minSize = minSize;
        this.maxSize = maxSize;
    }
    
    @Override
    public boolean accept(File rootFile, File file) {
        if(file.isDirectory()) {
            return true;
        }
        boolean isIn = file.length()>=minSize && file.length()<=maxSize;
        if(isExclude) {
            return !isIn;
        } else {
            return isIn;
        }
    }

    public long getMinSize() {
        return minSize;
    }


    public void setMinSize(long minSize) {
        this.minSize = minSize;
    }


    public long getMaxSize() {
        return maxSize;
    }


    public void setMaxSize(long maxSize) {
        this.maxSize = maxSize;
    }


    public boolean isExclude() {
        return isExclude;
    }


    public void setExclude(boolean isExclude) {
        this.isExclude = isExclude;
    }

}
