package com.ifugle.rap.utils;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ifugle.util.DateUtil;

/**
 * @author WenYuan
 * @version $
 * @since 5月 05, 2021 19:54
 */
public class GsonUtil {

    private static final Gson GSON = new GsonBuilder().setDateFormat(DateUtil.ISO8601_DATEITME_MEDIUM).create();

    public static String toJson(Object bean) {
        return GSON.toJson(bean);
    }

    public static <T> T fromJson(String json, Class<T> typeOfT) {
        return GSON.fromJson(json, typeOfT);
    }

    public static <T> T fromJson(String json, Type type) {
        return GSON.fromJson(json, type);
    }
}

