/**
 * Copyright(C) 2019 Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.test;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.alibaba.druid.util.StringUtils;

/**
 *
 * @author HuangLei(wenyuan)
 * @version $Id ListTest.java v 0.1 2019/1/22 HuangLei(wenyuan) Exp $
 */
public class ListTest {

    public static void main(String[] args) {
        List<String> list = new ArrayList<String>();
        list.add("2018-09-23 00:03:03");
        list.add("2018-09-23 00:03:03");
        list.add("2018-09-23 00:05:03");
        list.add("2018-09-23 00:03:03");
        list.add("2018-09-23 00:03:03");

        System.out.println(checkTimeEquals(list));

        try {
        }catch (Exception e){

        }

        //System.out.println(TimeDelayUtils.isSameDate(date,new Date()));

    }

    public static boolean checkTimeEquals(List<String> strList){
        if(CollectionUtils.isNotEmpty(strList)){
            String time = strList.get(0);
            for (String dateTime: strList) {
                if(!StringUtils.equalsIgnoreCase(dateTime,time)){
                    return false;
                }
            }
        }
        return true;
    }
}
