/*
 *--------------------------------------
 * Apusic (Kingdee Middleware)
 *---------------------------------------
 * Copyright By Apusic ,All right Reserved
 * chenpengliang   2015-3-26   comment
 * chenpengliang  2015-3-26  Created
 */
package com.wonderzhou.util.file;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.channels.FileChannel;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);

    public static FileInputStream validateFileInputStream(FileInputStream input) {
        return input;
    }

    public static FileOutputStream validateFileOutputStream(FileOutputStream input) {
        return input;
    }

    public static FileChannel validateChannel(FileChannel input) {
        return input;
    }

    /**
     * 传输文件，将一个目录下的文件转移到另一个目录
     * 
     * @param sourceFile
     * @param targetFile
     * @param delete 完成是否删除源文件
     * @throws Exception
     */
    public static void transferFile(File sourceFile, File targetFile, boolean delete) throws Exception {

        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;

        try {
            if (!sourceFile.exists()) {
                throw new Exception("来源文件[" + sourceFile.getAbsolutePath() + "]不存在");
            }
            String targetParentDir = FilenameUtils.getFullPathNoEndSeparator(targetFile.getAbsolutePath());
            File targetParentFile = new File(targetParentDir);
            if (!targetParentFile.exists()) {
                targetParentFile.mkdirs();
            }
            inputStream = FileValidationUtil.validateFileInputStream(new FileInputStream(sourceFile));
            inputChannel = inputStream.getChannel();
            if (!targetFile.exists()) {
                targetFile.createNewFile();
            }
            outputStream = new FileOutputStream(targetFile);
            outputChannel = outputStream.getChannel();

            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } catch (Exception e) {
            throw new Exception(e);
        } finally {
            if (inputChannel != null) {
                try {
                    inputChannel.close();
                } catch (Exception e) {
                    LOGGER.error("流关闭失败", e);
                }
            }

            if (outputChannel != null) {
                try {
                    outputChannel.close();
                } catch (Exception e) {
                    LOGGER.error("流关闭失败", e);
                }
            }

            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    LOGGER.error("流关闭失败", e);
                }
            }

            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (Exception e) {
                    LOGGER.error("流关闭失败", e);
                }
            }

        }

        if (delete) {
            sourceFile.delete();
        }

    }

    public static void deleteDir(File dir) {
        File[] listFiles = dir.listFiles();
        for (File file : listFiles) {
            if (file.isDirectory()) {
                deleteDir(file);
            } else {
                file.delete();
            }
        }
        dir.delete();
    }

    public static void deleteDirIfEmpty(File dir) throws IOException {
        deleteDirIfEmpty(dir, dir.getAbsolutePath());
    }

    public static void deleteDirIfEmpty(File dir, String source) throws IOException {
        if (source == null)
            source = dir.getAbsolutePath();
        if (dir.list().length == 0) {
            FileUtils.deleteDirectory(dir);
        } else {
            File[] files = dir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.isDirectory();
                }
            });

            for (File file : files) {
                deleteDirIfEmpty(file, source);
            }
            if (dir.list().length == 0 && !dir.getAbsolutePath().equals(source)) {
                FileUtils.deleteDirectory(dir);
            }
        }
    }

    public static void mkdirIfNotExist(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static void createFileIfNotExist(String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            file.createNewFile();
        }
    }

    public static void deleteFileIfExist(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }

    }

    public static void saveFile(String content,
                                String charsetName,
                                String filePath) throws UnsupportedEncodingException, IOException {
        if (content == null) {
            return;
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filePath);
            IOUtils.write(content.getBytes(charsetName), fos);
        } finally {
            IOUtils.closeQuietly(fos);
        }
    }
    
    public static void copyFile(File from, File to, boolean override) throws IOException {
        checkFile(from, to, override);
        if (to.exists()) {
            if (override) {
                //先将旧文件rename到临时文件,再拷贝,最后delete临时文件
                int idx = 0;
                File tmpFile = new File(to.getParentFile(), to.getName() + "_tmp_" + idx++);
                while (tmpFile.exists()) {
                    tmpFile = new File(to.getParentFile(), to.getName() + "_tmp_" + idx++);
                }
                if (to.renameTo(tmpFile)) {
                    try {
                        copyToANewFile(from, to);
                        tmpFile.delete();
                    } catch (IOException e) {
                        tmpFile.renameTo(to);
                        throw e;
                    }
                } else {
                    //如果不能改名，尝试删除后拷贝
                    if (to.delete()) {
                        copyToANewFile(from, to);
                    } else {
                        //如果删也删不掉,就直接覆盖
                        overrideAExistFile(from, to);
                    }
                }
            } else {
                //不可能走到这里，这种情况已经在checkFile方法里面进行了处理
            }
        } else {
            copyToANewFile(from, to);
        }
    }

    private static void checkFile(File from, File to, boolean override) throws IOException {
        if (to.exists()) {
            if (override) {
                if (to.isDirectory()) {
                    throw new IOException("目的文件是目录,不能被覆盖:" + to.getAbsolutePath());
                }
            } else {
                throw new IOException("目的文件已存在:" + to.getAbsolutePath());
            }
        } else {
            File parent = to.getParentFile();
            if (parent != null && !parent.exists()) {
                if (!parent.mkdirs()) {
                    throw new IOException("创建父目录失败:" + parent.getAbsolutePath());
                }
            }
        }
        if (!from.exists()) {
            throw new FileNotFoundException("源文件不存在:" + from.getAbsolutePath());
        }

        if (from.isDirectory()) {
            throw new FileNotFoundException("源文件是目录:" + from.getAbsolutePath());
        }
    }

    private static void overrideAExistFile(File from, File to) throws IOException {
        long maxBlockSize = 100 * 1024 * 1024L; //100m
        FileInputStream fromInput = null;
        FileOutputStream toOutput = null;
        try {
            fromInput = new FileInputStream(from);
            toOutput = new FileOutputStream(to);
            FileChannel fromFc = fromInput.getChannel();
            FileChannel toFc = toOutput.getChannel();
            long length = fromFc.size();
            RandomAccessFile raf = new RandomAccessFile(to, "rw");
            raf.setLength(length);
            raf.close();
            long transfered = 0;
            while (transfered < length) {
                transfered += fromFc.transferTo(transfered, Math.min(maxBlockSize, length - transfered), toFc);
            }
        } finally {
            try {
                if (fromInput != null) {
                    fromInput.close();
                }
            } catch (IOException e) {
            }
            try {
                if (toOutput != null) {
                    toOutput.close();
                }
            } catch (IOException e) {
            }
        }
    }

    private static void copyToANewFile(File from, File to) throws IOException {
        if (!to.createNewFile()) {
            throw new IOException("创建文件失败：" + to.getAbsolutePath());
        }
        try {
            overrideAExistFile(from, to);
        } catch (IOException e) {
            to.delete();
            throw e;
        }
    }

    public static void copyFile(File from, File to) throws IOException {
        copyFile(from, to, false);
    }

    public static void moveFile(File from, File to, boolean override) throws Exception {
        checkFile(from, to, override);
        if (to.exists()) {
            if (override) {
                //先将已存在的文件移动到临时文件，再移动文件，最后删除临时文件
                int idx = 0;
                File tmpFile = new File(to.getParentFile(), to.getName() + "_tmp_" + idx++);
                while (tmpFile.exists()) {
                    tmpFile = new File(to.getParentFile(), to.getName() + "_tmp_" + idx++);
                }
                if (to.renameTo(tmpFile)) {
                    if (!from.renameTo(to)) {
                        try {
                            copyToANewFile(from, to);
                            from.delete();
                            tmpFile.delete();
                        } catch (IOException e) {
                            tmpFile.renameTo(to);
                            throw e;
                        }
                    } else {
                        tmpFile.delete();
                    }
                } else {
                    //如果不能将已存在的文件备份到临时文件，则直接删除
                    if (to.delete()) {
                        if (!from.renameTo(to)) {
                            copyToANewFile(from, to);
                            from.delete();
                        }
                    } else {
                        //如果删也删不掉，则直接覆盖
                        overrideAExistFile(from, to);
                        from.delete();
                    }
                }
            } else {
                //不可能走到这里，这种情况已经在checkFile方法里面进行了处理
            }
        } else { //不存在
            if (!from.renameTo(to)) {
                copyToANewFile(from, to);
                from.delete();
            }
        }
    }

    public static void moveFile(File from, File to) throws Exception {
        moveFile(from, to, false);
    }

}
