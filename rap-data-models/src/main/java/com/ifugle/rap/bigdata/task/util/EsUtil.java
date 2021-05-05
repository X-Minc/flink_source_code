package com.ifugle.rap.bigdata.task.util;

import java.util.List;
import java.util.Map;

import com.ifugle.rap.bigdata.task.es.RangeBean;
import com.ifugle.rap.utils.GsonUtil;
import com.ifugle.util.NullUtil;

/**
 * @author GuanTao
 * @version $Id$
 * @since 2019年09月01日 12:16
 */
public class EsUtil {

    /**
     * es terms 生成json格式数据
     *
     * @param termsParam
     *
     * @return
     */
    public static String termsToJson(Map<String, List> termsParam) {
        StringBuilder sb = new StringBuilder();
        if (NullUtil.isNotNull(termsParam)) {
            for (Map.Entry<String, List> param : termsParam.entrySet()) {
                sb.append("{\"terms\": {\"");
                sb.append(param.getKey());
                sb.append("\":");
                sb.append(GsonUtil.toJson(param.getValue()));
                sb.append("}},");
            }
            sb = removeLastCommas(sb);
        }
        return sb.toString();
    }

    /**
     * es term 生成json格式数据
     *
     * @param termParam
     *
     * @return
     */
    public static String termToJson(Map<String, Object> termParam) {
        StringBuilder sb = new StringBuilder();
        if (NullUtil.isNotNull(termParam)) {
            for (Map.Entry<String, Object> entry : termParam.entrySet()) {
                sb.append("{\"term\": {\"");
                sb.append(entry.getKey());
                sb.append("\":");
                Object value = entry.getValue();
                if (value instanceof String) {
                    value = "\"" + value + "\"";
                }
                sb.append(value);
                sb.append("}},");

            }
            sb = removeLastCommas(sb);
        }
        return sb.toString();
    }

    /**
     * es range 生成json格式数据
     *
     * @param rangeParam
     *
     * @return
     */
    public static String rangeToJson(Map<String, RangeBean> rangeParam) {
        StringBuilder sb = new StringBuilder();
        if (NullUtil.isNotNull(rangeParam)) {
            for (Map.Entry<String, RangeBean> entry : rangeParam.entrySet()) {
                sb.append("{\"range\": {\"");
                sb.append(entry.getKey());
                sb.append("\":");
                sb.append(GsonUtil.toJson(entry.getValue()));
                sb.append("}},");
            }
            sb = removeLastCommas(sb);
        }
        return sb.toString();
    }

    public static StringBuilder removeLastCommas(StringBuilder sb) {
        if (sb.lastIndexOf(",") == sb.length() - 1) {
            // 去除末尾的逗号
            sb = new StringBuilder(sb.substring(0, sb.length() - 1));
        }
        return sb;
    }
}
