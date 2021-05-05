/**
 * Copyright(C) 2019 Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.bigdata.task.es;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.ifugle.util.NullUtil;

import lombok.Data;

/**
 * @author XingZhe
 * @version $Id$
 * @since 2019年07月31日 11:28
 */
@Data
public class QueryTermsRequest implements Serializable {
    private Long size;
    private Map<String, List> termsParam;
    private Map<String, Object> termParam;
    private Map<String, Object> sortParam;

    public QueryTermsRequest(Long size, Map<String, Object> termParam, Map<String, List> termsParam, Map<String, Object> sortParam) {
        this.size = size;
        this.termsParam = termsParam;
        this.termParam = termParam;
        this.sortParam = sortParam;
    }

    public String buid() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        if (NullUtil.isNotNull(this.size)) {
            sb.append("\"size\":" + this.size + ",");
        }
        sb.append("\"query\": {");
        sb.append("\"bool\": {");
        sb.append("\"filter\": [");
        if (NullUtil.isNotNull(this.termParam)) {
            for (Map.Entry<String, Object> entry : this.termParam.entrySet()) {
                sb.append("{\"term\": {");
                Object value = entry.getValue();
                if (entry.getValue() instanceof String) {
                    value = "\"" + value + "\"";
                }
                sb.append("\"" + entry.getKey() + "\": " + value + "}},");
            }
        }
        if (NullUtil.isNotNull(this.termsParam)) {
            sb.append("{");
            sb.append("\"terms\": {");
            for (Map.Entry<String, List> param : this.termsParam.entrySet()) {
                sb.append("\"" + param.getKey() + "\": [");
                for (Object o : param.getValue()) {
                    if (o instanceof String) {
                        sb.append("\"" + o + "\",");
                    } else {
                        sb.append(o.toString() + ",");
                    }
                }
                sb = removeLastCommas(sb);
                sb.append("]}}");
            }
        }
        sb = removeLastCommas(sb);
        sb.append("]}}");
        if (NullUtil.isNotNull(sortParam)) {
            sb.append(",\"sort\": [");
            for (Map.Entry<String, Object> param : this.sortParam.entrySet()) {
                sb.append("{\"" + param.getKey() + "\": {");
                sb.append(" \"order\":");
                Object o = param.getValue();
                if (o instanceof String) {
                    sb.append("\"" + o + "\"}},");
                } else {
                    sb.append(o.toString() + "}},");
                }
            }
                sb = removeLastCommas(sb);
            sb.append("],\"from\": 0");
        }
        sb.append("}");
        return sb.toString();
    }

    private StringBuilder removeLastCommas(StringBuilder sb) {
        if (sb.lastIndexOf(",") == sb.length() - 1) {
            // 去除末尾的逗号
            sb = new StringBuilder(sb.substring(0, sb.length() - 1));
        }
        return sb;
    }
}
