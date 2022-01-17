package com.ifugle.rap.data;

import com.ifugle.rap.utils.TimeUtil;

import java.util.Calendar;

/**
 * @author Minc
 * @date 2022/1/15 10:01
 */
public class Test {
    public static void main(String[] args) throws Exception {
        Integer date = Integer.valueOf(TimeUtil.getBeforeTime(new int[]{Calendar.MONTH, Calendar.DAY_OF_MONTH}, new int[]{-1, -1}, "yyyMMdd"));
        System.out.println(date);
    }
}
