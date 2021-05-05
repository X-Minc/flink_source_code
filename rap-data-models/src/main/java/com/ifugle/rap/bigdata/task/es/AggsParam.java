package com.ifugle.rap.bigdata.task.es;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import lombok.Data;

/**
 * @author GuanTao
 * @version $Id$
 * @since 2019年08月02日 22:40
 */
@Data
public class AggsParam implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 功能
     */
    private String function;

    /**
     * 条件
     */
    private Map<String, Object> params;

    public void setParam(String key, Object value) {
        if (this.params == null) {
            this.params = new HashMap<>();
        }
        this.params.put(key, value);
    }
}
