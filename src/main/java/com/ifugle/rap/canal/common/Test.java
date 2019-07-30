/**
 * Copyright(C) 2018 Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.canal.common;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

/**
 *
 * @author HuangLei(wenyuan)
 * @version $Id Test.java v 0.1 2018/12/3 HuangLei(wenyuan) Exp $
 */
public class Test {

    public static void main(String[] args) {
//        try {
//            while (true) {
//                Thread.sleep(1000);
//                System.out.println("start ...................");
//            }
//        }catch (Exception e){
//
//        }
//        System.out.println("complete ....................");


        Map<String,Object> map = new HashMap<>();
        map.put("XXWLQY_BJ",1);
        System.out.println(JSON.toJSONString(map));
    }
}
