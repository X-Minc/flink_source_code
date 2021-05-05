/**
 * Copyright(C) 2019 Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.bigdata.task.es;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.ifugle.rap.bigdata.task.util.EsAggsUtil;
import com.ifugle.util.NullUtil;

import lombok.Data;

/**
 * @author XingZhe
 * @version $Id$
 * @since 2019年07月27日 11:37
 */
@Data
public class AggsRequest implements Serializable {
    private static final long serialVersionUID = -990460610375444417L;
    private Long size;
    private QueryParam queryParam;
    private List<AggsParam> aggsParams;

    public AggsRequest() {
    }

    public AggsRequest(Long size, QueryParam queryParam, List<AggsParam> aggsParams) {
        this.size = size;
        this.queryParam = queryParam;
        this.aggsParams = aggsParams;
    }

    public String buid() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        if (NullUtil.isNotNull(this.size)) {
            sb.append("\"size\":" + this.size + ",");
        }
        if (NullUtil.isNotNull(this.queryParam)) {
            sb.append("\"query\": {");
            if (NullUtil.isNotNull(this.queryParam.getMatch())) {
                sb.append("\"match\": {");
                sb = (EsAggsUtil.mapParamsPlicing(sb, this.queryParam.getMatch()));
            }
            sb.append("}}");
            if (NullUtil.isNotNull(this.aggsParams)) {
                sb.append(",");
            }
        }
        if (NullUtil.isNotNull(this.aggsParams)) {
            sb.append(EsAggsUtil.aggsRecursion(this.aggsParams));
        }
        sb.append("}");
        return sb.toString();
    }

    @Data
    public static class QueryParam {
        private Map<String, Object> match;

    }

}
