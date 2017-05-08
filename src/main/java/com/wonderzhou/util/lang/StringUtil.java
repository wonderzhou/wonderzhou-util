package com.wonderzhou.util.lang;

import org.apache.commons.lang3.StringUtils;

/**
 * 字符串工具类
 * 
 * 
 *
 */
public final class StringUtil {
    /**
     * Convert a String into an integer. If the conversion fails, assign a default value.
     * 
     * @param str The String to convert to an integer
     * @param def The default value
     * @return The converted value or the default.
     */
    public static final int toInt(String str, int def) {
        int retval;
        try {
            retval = Integer.parseInt(str);
        } catch (Exception e) {
            retval = def;
        }
        return retval;
    }

    /**
     * 合并多个String
     * 
     * @param strs
     * @return
     */
    public final static String concat(String... strs) {
        if (strs.length == 1) {
            return strs[0];
        }

        int capacity = 0;

        for (String str : strs) {
            if (str == null) {
                continue;
            }

            capacity += str.length();
        }

        StringBuilder sb = new StringBuilder(capacity);

        for (String str : strs) {
            if (str == null) {
                continue;
            }

            sb.append(str);
        }

        return sb.toString();
    }

    public static void checkNotEmpty(String str) {
        if (StringUtils.isNotEmpty(str)) {
            throw new RuntimeException(str);
        }
    }

}
