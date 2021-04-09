/**
 * Copyright (c) 2018 Fugle Technology Co. Ltd. All Rights Reserved.
 */
package com.ifugle.rap.utils;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * 
 * @author HuangLei(wenyuan)
 * @version $Id: FileUtils.java, v 0.1 2018年5月22日 上午8:52:27 HuangLei(wenyuan) Exp $
 */
public class CommonUtils {

    private final static Logger logger = LoggerFactory.getLogger(CommonUtils.class);

    private final static String H5ROOT = "/tmp";


    public static boolean writeLocalTimeFile(String content, String path) {
        File instanceDir = new File(H5ROOT, path);
        try {
            FileUtils.writeStringToFile(instanceDir, content);
        } catch (IOException e) {
            logger.error("", e);
        }
        return true;
    }

    public static String readlocalTimeFile(String path) {
        File instanceDir = new File(H5ROOT, path);
        try {
            if (!instanceDir.exists()) {
                return null;
            }
            return FileUtils.readFileToString(instanceDir);
        } catch (IOException e) {
            logger.error("", e);
        }
        return null;
    }

    public static boolean isExistDir(String path){
        File instanceDir = new File(H5ROOT, path);
        if (instanceDir.exists()) {
            return true;
        }else{
            return false;
        }
    }

    public static void main(String[] args) {
//        writeLocalTimeFile("1", "status");
//        System.out.println(readlocalTimeFile("status"));

        System.out.println(readlocalTimeFile("D:/11.txt"));
    }
}
