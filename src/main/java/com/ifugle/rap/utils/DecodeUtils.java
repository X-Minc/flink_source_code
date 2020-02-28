/**
 * Copyright(C) 2019 Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import javax.crypto.SecretKey;

import com.google.common.io.BaseEncoding;
import com.ifugle.rap.security.crypto.CryptBase36;
import com.ifugle.rap.security.crypto.CryptBase62;
import com.ifugle.rap.security.crypto.CryptNumber;
import com.ifugle.rap.security.crypto.CryptSimple;
import com.ifugle.util.CipherUtil;
import com.ifugle.util.PwdUtil;
import com.ifugle.util.SHA256Util;

/**
 *
 * @author HuangLei(wenyuan)
 * @version $Id DecodeUtils.java v 0.1 2019/1/23 HuangLei(wenyuan) Exp $
 */
public class DecodeUtils {

    /***
     * 数据1#CryptSimpleSearch# 加密
     * @param data
     * @return
     */
    public static String decodeCryptSimpleTest(String data,CryptSimple cryptSimple){
        return cryptSimple.decrypt(data);
    }

    /***
     * 索引数据1#CryptBase62Reverse6#
     * @param data
     * @return
     */
    public static String decodeCryptBase62Reverse6Test(String data,CryptBase62 cryptBase62){
        return cryptBase62.decrypt(data);
    }

    /***
     * 统一社会信用代码
     * @param data
     * @param CryptBase36
     * @return
     */
    public static String deodeCryptBase36Test(String data, CryptBase36 CryptBase36){
        return CryptBase36.decrypt(data);
    }


    public static String decodeCryptSimpleProd(String data,CryptSimple cryptSimple)  {
        return cryptSimple.decrypt(data);
    }

    public static void initCryptSimpleProd(CryptSimple cryptSimple) {
        try {
            /**base key*/
            String baseKey = "IzxxW5L7UAdFl1huMCrg2TKs6+B/WeTCFCY+h2M2n5c";
            /**field key*/
            String fieldkey = "9q2ENkGXzQypZa/7SCpvAqHRkzzo9h2KZlVbzUils7A";
            String key = BaseEncoding.base64().omitPadding().encode(SHA256Util.digest("2-Simple-String-4096"));
            String salts = PwdUtil.getSalts(baseKey, fieldkey, key);
            SecretKey aesKey = CipherUtil.getAESKey(salts);
            Class<? extends CryptSimple> aClass = cryptSimple.getClass();
            Field secretKey = aClass.getDeclaredField("secretKey");
            secretKey.setAccessible(true);
            Field modifiers = Field.class.getDeclaredField("modifiers");
            modifiers.setAccessible(true);
            modifiers.setInt(secretKey, secretKey.getModifiers() & ~Modifier.FINAL);//cancel final
            secretKey.set(null, aesKey);


        }catch(NoSuchFieldException e){

        }catch (IllegalAccessException e){

        }
    }

    public static String decodeCryptBase62Reverse6Prod(String data,CryptBase62 cryptBase62)  {
        return cryptBase62.decrypt(data);
    }

    public static void initCryptBase62Reverse6(CryptBase62 cryptBase62) {
        try {
            /**base key*/
            String baseKey = "IzxxW5L7UAdFl1huMCrg2TKs6+B/WeTCFCY+h2M2n5c";
            /**field key*/
            String fieldkey = "9q2ENkGXzQypZa/7SCpvAqHRkzzo9h2KZlVbzUils7A";
            String key = BaseEncoding.base64().omitPadding().encode(SHA256Util.digest("2-Base62-String-32"));
            String salts = PwdUtil.getSalts(baseKey, fieldkey, key);
            SecretKey aesKey = CipherUtil.getAESKey(salts);
            Class<? extends CryptBase62> aClass = cryptBase62.getClass();
            Field secretKey = aClass.getDeclaredField("secretKey");
            secretKey.setAccessible(true);
            Field modifiers = Field.class.getDeclaredField("modifiers");
            modifiers.setAccessible(true);
            modifiers.setInt(secretKey, secretKey.getModifiers() & ~Modifier.FINAL);//cancel final
            secretKey.set(null, aesKey);
        }catch(NoSuchFieldException e){

        }catch (IllegalAccessException e){

        }
    }

    public static String deodeCryptBase36Prod(String data,CryptBase36 cryptBase36)  {
        return cryptBase36.decrypt(data);
    }

    public static void initCryptBase36(CryptBase36 cryptBase36) {
        try {
            /**base key*/
            String baseKey = "IzxxW5L7UAdFl1huMCrg2TKs6+B/WeTCFCY+h2M2n5c";
            /**field key*/
            String fieldkey = "9q2ENkGXzQypZa/7SCpvAqHRkzzo9h2KZlVbzUils7A";
            String key = BaseEncoding.base64().omitPadding().encode(SHA256Util.digest("2-Base36-String-36"));
            String salts = PwdUtil.getSalts(baseKey, fieldkey, key);
            SecretKey aesKey = CipherUtil.getAESKey(salts);
            //CryptBase62 cryptSimple = new CryptBase62(6);
            Class<? extends CryptBase36> aClass = cryptBase36.getClass();
            Field secretKey = aClass.getDeclaredField("secretKey");
            secretKey.setAccessible(true);
            Field modifiers = Field.class.getDeclaredField("modifiers");
            modifiers.setAccessible(true);
            modifiers.setInt(secretKey, secretKey.getModifiers() & ~Modifier.FINAL);//cancel final
            secretKey.set(null, aesKey);

        }catch(NoSuchFieldException e){

        }catch (IllegalAccessException e){

        }
    }


    /***
     * 解密手机号码
     * @param data
     * @param cryptNumber
     * @return
     */
    public static String  deodeCryptNumberProd(String data, CryptNumber cryptNumber){
        return cryptNumber.decrypt(data);
    }

    /***
     * 加密手机号码
     * @param data
     * @param cryptNumber
     * @return
     */
    public static String enCodeCryptNumberProd(String data,CryptNumber cryptNumber)  {
        return cryptNumber.encrypt(data);
    }

    /***
     * 初始化CryptNumber
     * @param cryptNumber
     */
    public static void initCryptNumber(CryptNumber cryptNumber) {
        try {
            /**base key*/
            String baseKey = "IzxxW5L7UAdFl1huMCrg2TKs6+B/WeTCFCY+h2M2n5c";
            /**field key*/
            String fieldkey = "9q2ENkGXzQypZa/7SCpvAqHRkzzo9h2KZlVbzUils7A";
            String key = BaseEncoding.base64().omitPadding().encode(SHA256Util.digest("2-Number-String-56"));
            String salts = PwdUtil.getSalts(baseKey, fieldkey, key);
            SecretKey aesKey = CipherUtil.getAESKey(salts);
            //CryptBase62 cryptSimple = new CryptBase62(6);
            Class<? extends CryptNumber> aClass = cryptNumber.getClass();
            Field secretKey = aClass.getDeclaredField("secretKey");
            secretKey.setAccessible(true);
            Field modifiers = Field.class.getDeclaredField("modifiers");
            modifiers.setAccessible(true);
            modifiers.setInt(secretKey, secretKey.getModifiers() & ~Modifier.FINAL);//cancel final
            secretKey.set(null, aesKey);

        }catch(NoSuchFieldException e){

        }catch (IllegalAccessException e){

        }
    }

    public static String decodeCryptNumberTest(String data, CryptNumber cryptNumber){
        return cryptNumber.decrypt(data);
    }

    public static String decodeCryptSimpleTest(String data, CryptBase36 cryptNumber){
        return cryptNumber.decrypt(data);
    }

    public static void main(String[] args) {
        //System.out.println(decodeCryptSimpleTest("蛿儛摔窋毖烉梔懷由苸",new CryptSimple()));
        //System.out.println(deodeCryptBase36Test("HV5XA5VXAZMDYX4",new CryptBase36()));
        //System.out.println(decodeCryptBase62Reverse6Prod("94GN4Bs3ztkM6OL",new CryptBase62(6)));
//        CryptBase36 cryptBase36 = new CryptBase36();
        CryptSimple cryptSimple =new CryptSimple();
        initCryptSimpleProd(cryptSimple);
        System.out.println(decodeCryptSimpleProd("伨珆藦鹞鉦鬨魗鎍車 ",cryptSimple));


//        CryptBase62 cryptBase62 = new CryptBase62(CryptBase62.MAX_SEARCH_SIZE_6);
//        initCryptBase62Reverse6(cryptBase62);
//        System.out.println(decodeCryptBase62Reverse6Prod("zkUKu9jfmSFo94P",cryptBase62));

    }
}
