package com.wonderzhou.util.file.filter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 默认的文件过滤规则： 先匹配符合条件的，再从符合条件中剔除不符合的条件的
 */
public class DefaultFileFilter implements FileFilter {

    private static final long serialVersionUID = -7723036539208352252L;

    // 匹配
    private List<FileFilter> yesList = new ArrayList<FileFilter>();
    // 非匹配的
    private List<FileFilter> notList = new ArrayList<FileFilter>();

    @Override
    public boolean accept(File rootFile, File file) {

        // 不符合的直接扔掉
        if (notList.size() > 0) {
            for (FileFilter filter : notList) {
                if (filter.accept(rootFile, file)) {
                    return false;
                }
            }
        }

        // 没有过滤器直接返回
        if (yesList.size() == 0) {
            return true;
        }

        // 有一个不匹配，就是不匹配了
        for (FileFilter filter : yesList) {
            if (!filter.accept(rootFile, file)) {
                return false;
            }
        }

        return true;
    }

    public void addYesFilter(FileFilter filter) {
        checkNull(filter);
        yesList.add(filter);
    }

    public void addNotFilter(FileFilter filter) {
        checkNull(filter);
        notList.add(filter);
    }

    private void checkNull(FileFilter filter) {
        if (null == filter) {
            throw new RuntimeException("filter can't be null");
        }
    }

    public List<FileFilter> getYesList() {
        return yesList;
    }

    public void setYesList(List<FileFilter> yesList) {
        this.yesList = yesList;
    }

    public List<FileFilter> getNotList() {
        return notList;
    }

    public void setNotList(List<FileFilter> notList) {
        this.notList = notList;
    }

}
