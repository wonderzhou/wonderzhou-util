package com.wonderzhou.util.date;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    /**
     * 转换日期时间
     * 
     * @param dateString
     *            日期时间字符串
     * @param dateFormat
     *            日期时间格式
     * @param defaultDate
     *            默认日期时间
     * @return 返回dateString按指定dateFormat格式转换的结果，若转换失败则返回defaultDate
     */
    public static Date parse(String dateString, DateFormat dateFormat, Date defaultDate) {
        Date ret = null;
        try {
            ret = dateFormat.parse(dateString);
        } catch (Exception e) {
            // doing nothing
        }
        return ret == null ? defaultDate : ret;
    }

    /**
     * 返回昨天的日期，不带时分秒
     * 
     * @return
     */
    public static Date yesterday() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 一天的开始时间，精确到毫秒，即当天0点0分0秒0毫秒
     * 
     * @param day
     * @return
     */
    public static Date beginOfDay(Date day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(day);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();

    }

    /**
     * 一天的结束时间，精确到毫秒，即当天23点59分59秒999毫秒
     * 
     * @param day
     * @return
     */
    public static Date endOfDay(Date day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(day);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();

    }

    public static String format(Date date, String format){
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        String dateString = formatter.format(date);
        return dateString;
    }
}
