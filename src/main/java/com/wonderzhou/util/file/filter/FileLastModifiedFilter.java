package com.wonderzhou.util.file.filter;

import java.io.File;
import java.util.Date;

/**
 * 文件最后修改时间过滤
 */
public class FileLastModifiedFilter implements FileFilter {
    
    private static final long serialVersionUID = -6231567670995422990L;
    
    private Date startDate;
    private Date endDate;
    //排除or包含最后修改时间为startDate~endDate的文件
    private boolean isExclude = false;
    

    public FileLastModifiedFilter() {
        
    }
    
    public FileLastModifiedFilter(Date startDate, Date endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }
    
    public FileLastModifiedFilter(Date startDate, Date endDate, boolean isExclude) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.isExclude = isExclude;
    }
    
    @Override
    public boolean accept(File rootFile, File file) {
        if(file.isDirectory()) {
            return true;
        }
        boolean isDown = null != startDate && file.lastModified() <= startDate.getTime();
        boolean isUp = null != endDate && file.lastModified() >= endDate.getTime();
        if(isExclude) {
            if(isDown || isUp) {
                return true;
            }
            return false;
        } else {
            if(isDown || isUp) {
                return false;
            }
            return true;
        }
    }
    
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean isExclude() {
        return isExclude;
    }

    public void setExclude(boolean isExclude) {
        this.isExclude = isExclude;
    }

}
