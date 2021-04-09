/**
 * Copyright(C) 2018 Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.model.redis;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * @author LiuZhengyang
 * @version $Id: RedisDTO.java 84708 2018-11-09 08:12:37Z HuangLei $
 * @since 2018年10月18日 09:06
 */
public class RedisDTO {

    @NotNull
    private List<Long> ids;

    @NotNull
    private Map<String, Object> attrs;

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    public Map<String, Object> getAttrs() {
        return attrs;
    }

    public void setAttrs(Map<String, Object> attrs) {
        this.attrs = attrs;
    }

    public RedisDTO(List<Long> ids, Map<String, Object> attrs) {
        this.ids = ids;
        this.attrs = attrs;
    }

    public RedisDTO() {
    }

    @Override
    public String toString() {
        return "RedisDTO{" + "ids=" + ids + ", attrs=" + attrs + '}';
    }
}
