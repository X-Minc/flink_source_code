package com.ifugle.rap.utils;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

/**
 * 针对时间的工具类
 *
 * @author Minc
 */
public class TimeUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(TimeUtil.class);
    private static final Calendar CALENDAR = Calendar.getInstance();

    /**
     * 获取当前时间当天0点时间戳
     *
     * @param time 当前时间
     * @return 当天0点时间戳
     * @throws Exception 错误
     */
    public synchronized static long getZeroTimeOfToday(long time) throws Exception {
        CALENDAR.setTime(new Date(time));
        CALENDAR.set(Calendar.HOUR_OF_DAY, 0);
        CALENDAR.set(Calendar.MINUTE, 0);
        CALENDAR.set(Calendar.SECOND, 0);
        CALENDAR.set(Calendar.MILLISECOND, 0);
        return CALENDAR.getTimeInMillis();
    }


    /**
     * 获取当前时间当天0点时间戳
     *
     * @param time   当前时间
     * @param offSet 偏移量
     * @return 当天0点时间戳
     * @throws Exception 错误
     */
    public synchronized static long getZeroTimeOfToday(long time, Long offSet) throws Exception {
        CALENDAR.setTime(new Date(time));
        CALENDAR.set(Calendar.HOUR_OF_DAY, 0);
        CALENDAR.set(Calendar.MINUTE, 0);
        CALENDAR.set(Calendar.SECOND, 0);
        CALENDAR.set(Calendar.MILLISECOND, 0);
        return CALENDAR.getTimeInMillis() + offSet;
    }

    /**
     * 获取当天剩余可用时间时间戳
     *
     * @param timeStamp 当前时间戳
     * @param offset    右偏移量
     * @return 当天剩余可用时间时间戳
     */
    public synchronized static long getRestTimeOfToday(long timeStamp, Long offset) {
        long millSeconds = 0;
        try {
            Date date = new Date(timeStamp);
            LocalDateTime dateTime = LocalDateTime.of(date.getYear(), date.getMonth() + 1, date.getDate(), date.getHours(), date.getMinutes(), date.getSeconds());
            LocalDateTime midnight = dateTime.plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
            millSeconds = ChronoUnit.MILLIS.between(dateTime, midnight);
        } catch (Exception e) {
            LOGGER.error("发生错误！", e);
        }
        return millSeconds + offset;
    }

    /**
     * @param day      当前日期
     * @param timeType 日期格式
     * @param offset   偏移量
     * @return 偏移后的日期字符串
     * @throws Exception 错误
     */
    public synchronized static String getStringDate(String day, String timeType, Long offset) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat(timeType);
        return dateFormat.format(dateFormat.parse(day).getTime() + offset);
    }

    /**
     * @param timeStamp 时间戳
     * @param format    日期表达式
     * @param offset    偏移量
     * @return 符合日期表达式的字符串日期
     * @throws Exception 异常
     */
    public synchronized static String getStringDate(Long timeStamp, String format, Long offset) throws Exception {
        return new SimpleDateFormat(format).format(timeStamp + offset);
    }

    /**
     * @param timeStamp 时间戳
     * @param format    日期表达式
     * @return 符合表达式的字符串日期
     */
    public synchronized static String getStringDate(Long timeStamp, String format) {
        try {
            return new SimpleDateFormat(format).format(timeStamp);
        } catch (Exception e) {
            LOGGER.error("出现脏数据：" + timeStamp + "reason:\n", e);
            return new SimpleDateFormat(format).format(timeStamp);
        }
    }

    /**
     * @param stringTimeStamp 字符串类型日期
     * @param format          日期表达式
     * @return 长整型时间戳
     * @throws Exception 异常
     */
    public synchronized static Long getLongDate(String stringTimeStamp, String format, Long offset) throws Exception {
        return new SimpleDateFormat(format).parse(stringTimeStamp).getTime() + offset;
    }

    /**
     * @param field  CALENDAR的静态属性
     * @param amount 偏移量
     * @param format string日期的时间格式
     * @return 偏移后日期
     * @throws Exception 异常
     */
    public synchronized static String getBeforeTime(int[] field, int[] amount, String format) throws Exception {
        if (field.length != amount.length) throw new Exception("请保证日期字段和偏移量数组长度相同！");
        CALENDAR.setTime(new Date());
        for (int i = 0; i < field.length; i++) {
            CALENDAR.add(field[i], amount[i]);
        }
        Date d = CALENDAR.getTime();
        return new SimpleDateFormat(format).format(d);
    }
}
