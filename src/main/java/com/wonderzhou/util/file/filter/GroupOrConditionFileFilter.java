package com.wonderzhou.util.file.filter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GroupOrConditionFileFilter implements FileFilter {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    // 匹配
    private List<FileFilter> filterList = new ArrayList<FileFilter>();

    @Override
    public boolean accept(File rootFile, File file) {
        if (filterList.size() == 0) {
            return true;
        }

        // 有一个不匹配，就是不匹配了
        for (FileFilter filter : filterList) {
            if (filter.accept(rootFile, file)) {
                return true;
            }
        }

        return false;
    }

    public void addFileFilter(FileFilter fileFilter) {
        filterList.add(fileFilter);
    }

    public List<FileFilter> getFilterList() {
        return filterList;
    }

    public void setFilterList(List<FileFilter> filterList) {
        this.filterList = filterList;
    }
    
}
