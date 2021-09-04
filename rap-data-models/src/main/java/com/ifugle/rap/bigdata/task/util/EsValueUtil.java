package com.ifugle.rap.bigdata.task.util;

import java.util.Map;

import com.ifugle.util.NullUtil;

/**
 * @author XuWeigang
 * @since 2020年01月16日 11:28
 */
public class EsValueUtil {
    /**
     * 将返回结果中的字段值转换为String类型
     * @param item
     * @param field
     * @return
     */
    public static String getStringValue(Map item, String field) {
        Object value = item.get(field);
        if (NullUtil.isNull(value)) {
            return null;
        }
        String stringValue = value.toString();
        if (stringValue.startsWith("\"")) {
            // 去除两边引号
            stringValue = stringValue.substring(1, stringValue.length() - 1);
        }
        return stringValue;
    }


    /**
     * 将返回结果中的字段值转换为Long类型
     * @param item
     * @param field
     * @return
     */
    public static Long getLongValue(Map item, String field) {
        Object value = item.get(field);
        if (NullUtil.isNull(value)) {
            return 0L;
        }
        return Double.valueOf(value.toString()).longValue();
    }

    /**
     * 将返回结果中的字段值转换为Integer类型
     * @param item
     * @param field
     * @return
     */
    public static Integer getIntValue(Map item, String field) {
        Object value = item.get(field);
        if (NullUtil.isNull(value)) {
            return 0;
        }
        return Double.valueOf(value.toString()).intValue();
    }

    /**
     * 将返回结果中的字段值转换为Byte类型
     * @param item
     * @param field
     * @return
     */
    public static Byte getByteValue(Map item, String field) {
        Object value = item.get(field);
        if (NullUtil.isNull(value)) {
            return 0;
        }
        return Double.valueOf(value.toString()).byteValue();
    }
}
