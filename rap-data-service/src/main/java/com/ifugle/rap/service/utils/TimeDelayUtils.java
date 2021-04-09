/**
 * Copyright(C) 2019 Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.service.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author HuangLei(wenyuan)
 * @version $Id TimeDelayUtils.java v 0.1 2019/1/22 HuangLei(wenyuan) Exp $
 */
public class TimeDelayUtils {


    private final static Logger logger  = LoggerFactory.getLogger(TimeDelayUtils.class);
    /***
     * 获取某一时间的下一秒钟
     * @param time
     * @return
     */
    public final static String getNextMilli(String time){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long millionSeconds = sdf.parse(time).getTime();//毫秒
            //加一秒
            long newTime = millionSeconds + 1000;

            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(newTime);
            Date date = c.getTime();
            return sdf.format(date);
        } catch (Exception e) {
            logger.error("Format datatime error ",e);
        }
        return null;
    }

    /***
     * 获取某一时间的下一秒钟
     * @param time
     * @return
     */
    public final static Date getNextMilliDate(Date time){
        try {
            long millionSeconds = time.getTime();//毫秒
            //加一秒
            long newTime = millionSeconds + 1000;

            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(newTime);
            Date date = c.getTime();
            return date;
        } catch (Exception e) {
            logger.error("Format datatime error ",e);
        }
        return null;
    }

    public static boolean isSameDate(Date date1, Date date2) {
        try {
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(date1);

            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(date2);

            boolean isSameYear = cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
            boolean isSameMonth = isSameYear && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
            boolean isSameDate = isSameMonth && cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);

            return isSameDate;
        } catch (Exception e) {
            logger.error("[RatingEngine] error occurred: ERROR ", e);
        }
        return false;
    }

    public static void main(String[] args) {
        Date date =new Date();
        System.out.println(date);
        System.out.println(isSameDate(date,new Date()));
    }

}
