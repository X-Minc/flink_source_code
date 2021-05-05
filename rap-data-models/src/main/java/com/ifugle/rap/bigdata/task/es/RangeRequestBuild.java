/**
 * Copyright(C) 2019 Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.bigdata.task.es;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ifugle.rap.exception.DsbServiceException;
import com.ifugle.rap.bigdata.task.util.EsUtil;
import com.ifugle.util.NullUtil;

import lombok.Data;

/**
 * @author XingZhe
 * @version $Id$
 * @since 2019年07月31日 11:28
 */
@Data
public class RangeRequestBuild implements Serializable {

    private Map<String, List> termsParam;
    private Map<String, Object> termParam;
    private Map<String, RangeBean> rangeParam;

    public RangeRequestBuild(Map<String, List> termsParam, Map<String, Object> termParam, Map<String, RangeBean> rangeParam) {
        this.termsParam = termsParam;
        this.termParam = termParam;
        this.rangeParam = rangeParam;
    }

    public RangeRequestBuild() {
    }

    public void setRangeParam(String param, String gte, String lte, String format) {
        if (NullUtil.isNull(rangeParam)) {
            rangeParam = new HashMap<>();
        }

        RangeBean rangeBean = new RangeBean();
        rangeBean.setGte(gte);
        rangeBean.setLte(lte);
        rangeBean.setFormat(format);
        rangeParam.put(param, rangeBean);
    }

    public void setTermsParam(String param, List value) {
        if (NullUtil.isNull(termsParam)) {
            termsParam = new HashMap<>();
        }
        termsParam.put(param, value);
    }

    public void setTermParam(String param, Object value) {
        if (NullUtil.isNull(termParam)) {
            termParam = new HashMap<>();
        }
        termParam.put(param, value);
    }

    public String buid() {
        if (!(NullUtil.isNotNull(termsParam) || NullUtil.isNotNull(termParam) || NullUtil.isNotNull(rangeParam))) {
            throw new DsbServiceException("es请求参数异常");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"query\": {");
        sb.append("\"bool\": {");
        sb.append("\"must\": [");
        if (NullUtil.isNotNull(this.termParam)) {
            String s = EsUtil.termToJson(termParam);
            sb.append(s);
            sb.append(",");
        }

        if (NullUtil.isNotNull(this.termsParam)) {
            String s = EsUtil.termsToJson(termsParam);
            sb.append(s);
            sb.append(",");
        }

        if (NullUtil.isNotNull(this.rangeParam)) {
            String s = EsUtil.rangeToJson(rangeParam);
            sb.append(s);
            sb.append(",");
        }
        sb = EsUtil.removeLastCommas(sb);
        sb.append("]}}}");
        return sb.toString();
    }
}
