package com.wonderzhou.util.file.handler;

import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.wonderzhou.util.file.FileUtil;

/**
 * 默认目标文件处理策略
 */
public class DefaultDestFileHandler implements DestFileHandler {

    private static final long serialVersionUID = 7195148248496091884L;

    // 有重名文件直接覆盖
    public static final int OVERRIDE_WHEN_FILE_CONFLICK = 1;
    // 有重名文件直接通知错误
    public static final int NOTIFY_ERROR_WHEN_FILE_CONFLICK = 2;
    // 备份旧文件
    public static final int BACK_OLD_WHEN_FILE_CONFLICK = 3;
    // 备份新文件
    public static final int BACK_NEW_WHEN_FILE_CONFLICK = 4;

    // 文件备份名称
    private static final String FORMAT_YEAR = "YYYY";
    private static final String FORMAT_MONTH = "MM";
    private static final String FORMAT_DAY = "DD";
    private static final String FORMAT_HOUR = "HH";
    private static final String FORMAT_MINUTE = "mm";
    private static final String FORMAT_SECOND = "ss";
    private static final String FORMAT_FILENAME = "filename";

    // 是否需要保持文件结构(树形结构)
    private boolean retainHiearachy = true;
    // 重命名格式
    private String fileRenameFormat;
    // 子文件夹格式
    private String subDirectoryFormat;
    // 文件重名处理策略
    private int fileConflickStrategy = OVERRIDE_WHEN_FILE_CONFLICK;

    public DefaultDestFileHandler() {
        this.retainHiearachy = true;
        this.fileConflickStrategy = OVERRIDE_WHEN_FILE_CONFLICK;
    }

    public DefaultDestFileHandler(String subDirectoryFormat, int conflickStrategy, boolean retainHiearachy,
                                  String renameFormat) {
        if (conflickStrategy < OVERRIDE_WHEN_FILE_CONFLICK || conflickStrategy > BACK_NEW_WHEN_FILE_CONFLICK) {
            throw new IllegalArgumentException("无法识别的文件冲突处理策略:" + conflickStrategy);
        }
        this.fileConflickStrategy = conflickStrategy;
        this.retainHiearachy = retainHiearachy;
        this.fileRenameFormat = renameFormat;
        // 如果不为空则此次任务接收的文件都放在该文件夹下
        this.subDirectoryFormat = subDirectoryFormat;
    }

    @Override
    public void handler(String srcPath, String destDirectory, String fileName) throws Exception {
        File srcFile = new File(srcPath);
        if (!srcFile.exists()) {
            throw new Exception("文件不存在:" + srcFile.getAbsolutePath());
        }
        File parent = new File(destDirectory);
        // 日期格式目录
        if (null != subDirectoryFormat && !"".endsWith(subDirectoryFormat.trim())) {
            String format = parseFormat(null, subDirectoryFormat);
            parent = new File(parent, format);
        }
        if (!parent.exists()) {
            boolean success = parent.mkdirs();
            if (!success) {
                throw new Exception("创建目录失败:" + parent.getAbsolutePath());
            }
        }
        String name = fileName;
        // 不需要保持原来的目录结构
        if (!retainHiearachy) {
            name = new File(destDirectory, fileName).getName();
        }
        File destFile = new File(parent, name);
        if (srcFile.isFile()) {
            if (destFile.exists()) {
                switch (fileConflickStrategy) {
                    case OVERRIDE_WHEN_FILE_CONFLICK:
                        FileUtil.moveFile(srcFile, destFile, true);
                        break;
                    case NOTIFY_ERROR_WHEN_FILE_CONFLICK:
                        throw new Exception("目标文件已存在");
                    case BACK_OLD_WHEN_FILE_CONFLICK:
                        backupOldExistsFile(destFile);
                        FileUtil.moveFile(srcFile, destFile, true);
                        break;
                    case BACK_NEW_WHEN_FILE_CONFLICK:
                        backupNewExistsFile(destFile, srcFile);
                        break;
                    default:
                        throw new Exception("无法识别的文件冲突处理策略:" + fileConflickStrategy);
                }
            } else {
                FileUtil.moveFile(srcFile, destFile, false);
            }
        } else {
            boolean result = destFile.mkdirs();
            if (!result && !destFile.exists()) {
                throw new Exception("创建目录失败:" + destFile.getAbsolutePath());
            }
        }
    }

    private String parseFormat(String fileName, String formatName) {
        if (null == formatName) {
            return fileName;
        } else {
            Calendar now = new GregorianCalendar();
            int year = now.get(Calendar.YEAR);
            int month = now.get(Calendar.MONTH) + 1;
            int day = now.get(Calendar.DAY_OF_MONTH);
            int hour = now.get(Calendar.HOUR_OF_DAY);
            int minute = now.get(Calendar.MINUTE);
            int second = now.get(Calendar.SECOND);
            formatName = formatName.replace(FORMAT_YEAR, "" + year);
            formatName = formatName.replace(FORMAT_MONTH, getWithPrefix(month));
            formatName = formatName.replace(FORMAT_DAY, getWithPrefix(day));
            formatName = formatName.replace(FORMAT_HOUR, getWithPrefix(hour));
            formatName = formatName.replace(FORMAT_MINUTE, getWithPrefix(minute));
            formatName = formatName.replace(FORMAT_SECOND, getWithPrefix(second));
            if (null != fileName) {
                String suffix = fileName.replaceFirst("^(.*)(\\.[^\\.]+$)", "$2");
                if(suffix.equals(fileName)) {
                    formatName = formatName.replace(FORMAT_FILENAME, fileName);
                } else {
                    String fileNameNoExt = fileName.replaceFirst("^(.*)(\\.[^\\.]+)$", "$1");
                    formatName = formatName.replace(FORMAT_FILENAME, fileNameNoExt) + suffix;
                }
            }
            return formatName;
        }
    }

    /**
     * 备份旧文件
     * 
     * @param file 文件接收端已经存在的文件
     * @throws Exception
     */
    private void backupOldExistsFile(File file) throws Exception {
        if (null == fileRenameFormat || "".equals(fileRenameFormat.trim())) {
            throw new Exception("没有指定文件备份格式");
        } else {
            String newName = parseFormat(file.getName(), fileRenameFormat);
            File to = new File(file.getParentFile(), newName);
            int i = 0;
            while(to.exists()) {
                String suffix = to.getName().replaceFirst("^(.*)(\\.[^\\.]+)$", "$2");
                //没有匹配到
                if(suffix.equals(to.getName())) {
                    newName = suffix + i;
                } else {
                    newName = to.getName().replaceFirst("^(.*)(\\.[^\\.]+)$", "$1") + i + suffix;
                }
                to = new File(to.getParentFile(), newName);
                i++;
            }
            FileUtil.moveFile(file, to);
        }
    }

    /**
     * 备份新的文件
     * 
     * @param oldFile 文件接收端已经存在的文件
     * @param newFile 需要重命名的文件
     * @throws Exception
     */
    private void backupNewExistsFile(File oldFile, File newFile) throws Exception {
        if (null == fileRenameFormat || "".equals(fileRenameFormat.trim())) {
            throw new Exception("没有指定文件备份格式");
        } else {
            String newName = parseFormat(oldFile.getName(), fileRenameFormat);
            File to = new File(oldFile.getParentFile(), newName);
            int i = 0;
            while(to.exists()) {
                String suffix = to.getName().replaceFirst("^(.*)(\\.[^\\.]+)$", "$2");
                //没有匹配到
                if(suffix.equals(to.getName())) {
                    newName = suffix + i;
                } else {
                    newName = to.getName().replaceFirst("^(.*)(\\.[^\\.]+)$", "$1") + i + suffix;
                }
                to = new File(to.getParentFile(), newName);
                i++;
            }
            FileUtil.moveFile(newFile, to);
        }
    }

    private String getWithPrefix(int value) {
        if (value < 10) {
            return "0" + value;
        }
        return "" + value;
    }

    public boolean isRetainHiearachy() {
        return retainHiearachy;
    }

    public void setRetainHiearachy(boolean retainHiearachy) {
        this.retainHiearachy = retainHiearachy;
    }

    public String getFileRenameFormat() {
        return fileRenameFormat;
    }

    public void setFileRenameFormat(String fileRenameFormat) {
        this.fileRenameFormat = fileRenameFormat;
    }

    public String getSubDirectoryFormat() {
        return subDirectoryFormat;
    }

    public void setSubDirectoryFormat(String subDirectoryFormat) {
        this.subDirectoryFormat = subDirectoryFormat;
    }

    public int getFileConflickStrategy() {
        return fileConflickStrategy;
    }

    public void setFileConflickStrategy(int fileConflickStrategy) {
        if (fileConflickStrategy < OVERRIDE_WHEN_FILE_CONFLICK || fileConflickStrategy > BACK_NEW_WHEN_FILE_CONFLICK) {
            throw new IllegalArgumentException("无法识别的文件冲突处理策略:" + fileConflickStrategy);
        }
        this.fileConflickStrategy = fileConflickStrategy;
    }

}
