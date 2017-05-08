package com.wonderzhou.util.size;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SizeConverter {
    public static final String[] UNITS = {"B", "KB", "MB", "GB", "TB"};
    public static final BigDecimal KILO = new BigDecimal(1024);

    public static String convert(long fileSize) {
        BigDecimal size = BigDecimal.valueOf(fileSize);
        int unitIndex = 0;
        while (size.compareTo(KILO) >= 0) {
            unitIndex++;
            size = size.divide(KILO, 2, RoundingMode.HALF_UP);
        }
        return size.toString() + UNITS[unitIndex];
    }

    public static void main(String[] args) {
        System.out.println(convert(1024 * 1024 * 98));
    }
}
