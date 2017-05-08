package com.wonderzhou.util.file.handler;

import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.wonderzhou.util.file.FileUtil;


/**
 * 源文件处理策略
 */
public class DefaultSourceFileHandler implements SourceFileHandler {

    private static final long serialVersionUID = 4718813576277605249L;
    
    //移动到成功目录
    public static final int MOVE_TO_SUCCESS_DIRECTORY     = 1;
    //删除
    public static final int DELETE_WHEN_SUCCESS           = 2;
    //不执行操作
    public static final int RETAIN_WHEN_SUCCESS           = 3;
    //移动到失败目录
    public static final int MOVE_TO_FAIL_DIRECTORY        = 4;
    //不执行操作
    public static final int RETAIN_WHEN_FAIL              = 5;
    
    //文件夹备份名称
    private static final String FORMAT_YEAR = "YYYY";
    private static final String FORMAT_MONTH = "MM";
    private static final String FORMAT_DAY = "DD";
    private static final String FORMAT_HOUR = "HH";
    private static final String FORMAT_MINUTE = "mm";
    private static final String FORMAT_SECOND = "ss";
    
    //成功后移动根目录
    private String successDirectory;
    //失败后移动根目录
    private String failedDirectory;
    //文件发送成功处理策略
    private int successStrategy = RETAIN_WHEN_SUCCESS;
    //文件发送失败处理策略
    private int failStrategy = RETAIN_WHEN_FAIL;
    //是否删除源目录根路径下的空子文件夹
    private boolean isDeleteEmptyDirectory;
    //是否需要保持文件结构(树形结构)
    private boolean retainHiearachy = true;
    //发送文件的源目录(需要备份文件时必须设置)
    private String rootSourceDirectory;
    //子文件夹日期格式
    private String subDirectoryFormat;
    
    public DefaultSourceFileHandler() {
        this.successStrategy = RETAIN_WHEN_SUCCESS;
        this.failStrategy = RETAIN_WHEN_FAIL;
    }
    
    public DefaultSourceFileHandler(String rootSourceDirectory, int successStrategy, int failStrategy) {
        if(successStrategy != MOVE_TO_SUCCESS_DIRECTORY 
            && successStrategy != DELETE_WHEN_SUCCESS 
            && successStrategy != RETAIN_WHEN_SUCCESS) {
            throw new IllegalArgumentException("无效的成功处理策略:" + successStrategy);
        }
        if(failStrategy != MOVE_TO_FAIL_DIRECTORY 
            && failStrategy != RETAIN_WHEN_FAIL) {
            throw new IllegalArgumentException("无效的失败处理策略:" + failStrategy);
        }
        this.successStrategy = successStrategy;
        this.failStrategy = failStrategy;
        this.rootSourceDirectory = rootSourceDirectory;
    }
    
    public void setSuccessMoveToDirectory(String directory) {
        this.successDirectory = directory;
    }

    public void setFailedMoveToDirectory(String directory) {
        this.failedDirectory = directory;
    }

    public String getSuccessMoveToDirectory() {
        return successDirectory;
    }

    public String getFailedMoveToDirectory() {
        return failedDirectory;
    }

    @Override
    public void sendFileFailed(File srcFile, String fileName) throws Exception {
        switch (failStrategy) {
            case MOVE_TO_FAIL_DIRECTORY:
                File parent = createSubFormatDirectory(failedDirectory);
                String name = fileName;
                if(!retainHiearachy) {
                    name = new File(parent, fileName).getName();
                }
                String srcParentPath = srcFile.getParentFile().getAbsolutePath();
                String newPath = srcParentPath.replace(new File(rootSourceDirectory).getAbsolutePath(), "");
                File moveToDir = new File(parent, newPath);
                doMoveFile(moveToDir, srcFile, name);
                break;
            default:
                break;
        }
    }

    @Override
    public void sendFileSuccess(File srcFile, String fileName) throws Exception {
        switch (successStrategy) {
            case MOVE_TO_SUCCESS_DIRECTORY:
                File parent = createSubFormatDirectory(successDirectory);
                String name = fileName;
                if(!retainHiearachy) {
                    name = new File(parent, fileName).getName();
                }
                String srcParentPath = srcFile.getParentFile().getAbsolutePath();
                String newPath = srcParentPath.replace(new File(rootSourceDirectory).getAbsolutePath(), "");
                File moveToDir = new File(parent, newPath);
                doMoveFile(moveToDir, srcFile, name);
                break;
            case DELETE_WHEN_SUCCESS:
                doDeleteFile(srcFile);
                break;
            default:
                break;
        }
    }
    
    private void doMoveFile(File rootDir, File srcFile, String fileName) throws Exception {
        File destFile = new File(rootDir, fileName);
        if(srcFile.isFile()) {
            FileUtil.moveFile(srcFile, destFile, true);
            if(isDeleteEmptyDirectory) {
                doDeleteEmptyDirectory(srcFile.getParentFile());
            }
        } else if(srcFile.isDirectory()) {
            File[] files = srcFile.listFiles();
            if (files == null || files.length == 0) {
                boolean success = srcFile.renameTo(destFile);
                if (!success) {
                    success = destFile.mkdirs();
                    if (success) {
                        success = srcFile.delete();
                    }
                }
                if (!success) {
                    throw new Exception("移动目录到" + destFile.getAbsolutePath() + "失败:" + srcFile.getAbsolutePath());
                }
            }
        } else {
            //既不是文件也不是目录，有可能是不存在的文件，或文件名有乱码，导致java无法读取
            throw new Exception("文件不存在:" + srcFile.getAbsolutePath());
        }
    }
    
    private void doDeleteFile(File srcFile) throws Exception {
        boolean success = srcFile.delete();
        if (!success && srcFile.isFile()) {
            throw new Exception("无法删除文件:" + srcFile.getAbsolutePath());
        }
        if(isDeleteEmptyDirectory) {
            doDeleteEmptyDirectory(srcFile.getParentFile());
        }
    }
    
    /**
     * 删除根目录下的空目录
     * @param rootSourceDirectory
     * @param directory
     * @throws Exception
     */
    private void doDeleteEmptyDirectory(File directory) throws Exception {
        if(null == rootSourceDirectory) {
            return;
        }
        if(null != directory && !(new File(rootSourceDirectory)).equals(directory) && directory.list().length == 0) {
            boolean success = directory.delete();
            if (!success) {
                throw new Exception("无法删除空目录:" + directory.getAbsolutePath());
            }
        }
    }
   
    /**
     * 创建日期格式目录
     * @throws Exception 
     */
    private File createSubFormatDirectory(String parentDirectory) throws Exception {
        File parent = null;
        if(null == parentDirectory) {
            throw new IllegalArgumentException("参数不正确,要创建的目录的上级目录为空");
        }
        if (null != subDirectoryFormat && !"".endsWith(subDirectoryFormat.trim())) {
            String format = parseFormat(subDirectoryFormat);
            parent = new File(parentDirectory, format);
        } else {
            parent = new File(parentDirectory);
        }
        if (!parent.exists()) {
            boolean success = parent.mkdirs();
            if (!success) {
                throw new Exception("创建目录失败:" + parent.getAbsolutePath());
            }
        }
        return parent;
    }
    
    private String parseFormat(String formatName) {
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
        return formatName;
    }
    
    private String getWithPrefix(int value) {
        if (value < 10) {
            return "0" + value;
        }
        return "" + value;
    }

    public String getSuccessDirectory() {
        return successDirectory;
    }

    public void setSuccessDirectory(String successDirectory) {
        this.successDirectory = successDirectory;
    }

    public String getFailedDirectory() {
        return failedDirectory;
    }

    public void setFailedDirectory(String failedDirectory) {
        this.failedDirectory = failedDirectory;
    }

    public int getSuccessStrategy() {
        return successStrategy;
    }

    public void setSuccessStrategy(int successStrategy) {
        this.successStrategy = successStrategy;
    }

    public int getFailStrategy() {
        return failStrategy;
    }

    public void setFailStrategy(int failStrategy) {
        this.failStrategy = failStrategy;
    }

    public boolean isDeleteEmptyDirectory() {
        return isDeleteEmptyDirectory;
    }

    public void setDeleteEmptyDirectory(boolean isDeleteEmptyDirectory) {
        this.isDeleteEmptyDirectory = isDeleteEmptyDirectory;
    }

    public boolean isRetainHiearachy() {
        return retainHiearachy;
    }

    public void setRetainHiearachy(boolean retainHiearachy) {
        this.retainHiearachy = retainHiearachy;
    }

    public String getRootSourceDirectory() {
        return rootSourceDirectory;
    }

    public void setRootSourceDirectory(String rootSourceDirectory) {
        this.rootSourceDirectory = rootSourceDirectory;
    }

    public String getSubDirectoryFormat() {
        return subDirectoryFormat;
    }

    public void setSubDirectoryFormat(String subDirectoryFormat) {
        this.subDirectoryFormat = subDirectoryFormat;
    }

}
