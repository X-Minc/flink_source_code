/**
 * Copyright(C) 2019 Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.service.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

/**
 *
 * @author HuangLei(wenyuan)
 * @version $Id GsonUtil.java v 0.1 2019/7/30 HuangLei(wenyuan) Exp $
 */
public final class GsonUtil {

    private final static Gson GSON = new GsonBuilder().disableHtmlEscaping().create();

    private GsonUtil() {

    }

    public static Gson getGson() {
        return GSON;
    }

    /**
     * 转换
     *
     * @param json
     * @param bean
     * @return
     */
    public static <T> T getBean(String json, Class<T> bean) {
        return GSON.fromJson(json, bean);
    }

    /**
     * 转换
     *
     * @param json
     * @param type
     * @return
     */
    public static <T> T getBean(String json, Type type) {
        return GSON.fromJson(json, type);
    }

    /**
     * 对象转JSON
     *
     * @param bean
     * @return
     */
    public static String toJson(Object bean) {
        return GSON.toJson(bean);
    }

}
