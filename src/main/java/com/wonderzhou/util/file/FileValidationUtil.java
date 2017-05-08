package com.wonderzhou.util.file;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

public class FileValidationUtil {
    public static RandomAccessFile validateRandomAccessFile(RandomAccessFile s) {
        return s;
    }

    public static FileInputStream validateFileInputStream(FileInputStream input) {
        return input;
    }
    
    public static FileOutputStream validateFileOutputStream(FileOutputStream output) {
        return output;
    }
    
    public static FileChannel validateFileOutputStream(FileChannel channel) {
        return channel;
    }
    
    
}
