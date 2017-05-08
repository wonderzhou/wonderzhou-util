package com.wonderzhou.util.bytes;

public final class ByteUtil {

    /**
     * @param v
     * @param dst
     * @param offset
     */
    public static void writeInt(int v, byte[] dst, int offset) {
        dst[offset] = (byte)((v >>> 24) & 0xFF);
        dst[offset + 1] = (byte)((v >>> 16) & 0xFF);
        dst[offset + 2] = (byte)((v >>> 8) & 0xFF);
        dst[offset + 3] = (byte)((v >>> 0) & 0xFF);
    }
    
    /**
     * @param source
     * @param offset
     * @return
     */
    public static int readInt(byte[] source, int offset) {
        return (((source[offset + 0] << 24) & 0xFF000000) + ((source[offset + 1]  << 16) & 0x00FF0000) + ((source[offset + 2]  << 8) & 0x0000FF00) + ((source[offset + 3]  << 0) & 0x000000FF));
    }
    
    /**
     * @param v
     * @param dst
     * @param offset
     */
    public static void writeLong(long v, byte[] dst, int offset) {
        dst[offset + 0] = (byte)(v >>> 56);
        dst[offset + 1] = (byte)(v >>> 48);
        dst[offset + 2] = (byte)(v >>> 40);
        dst[offset + 3] = (byte)(v >>> 32);
        dst[offset + 4] = (byte)(v >>> 24);
        dst[offset + 5] = (byte)(v >>> 16);
        dst[offset + 6] = (byte)(v >>>  8);
        dst[offset + 7] = (byte)(v >>>  0);
    }
    
    /**
     * @param source
     * @param offset
     * @return
     */
    public static long readLong(byte[] source, int offset) {
        return (((long)source[offset + 0] << 56) +
                ((long)(source[offset + 1] & 255) << 48) +
                ((long)(source[offset + 2] & 255) << 40) +
                ((long)(source[offset + 3] & 255) << 32) +
                ((long)(source[offset + 4] & 255) << 24) +
                ((source[offset + 5] & 255) << 16) +
                ((source[offset + 6] & 255) <<  8) +
                ((source[offset + 7] & 255) <<  0));
    }
    
}
