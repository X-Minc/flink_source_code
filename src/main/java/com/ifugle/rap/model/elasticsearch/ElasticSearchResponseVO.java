/**
 * Copyright(C) 2018 Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.model.elasticsearch;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author LiuZhengyang
 * @version $Id: ElasticSearchResponseVO.java 84708 2018-11-09 08:12:37Z HuangLei $
 * @since 2018年10月16日 13:29
 */
public class ElasticSearchResponseVO {

    @JsonProperty("took")
    private Integer took;

    @JsonProperty("errors")
    private boolean errors;

    @JsonProperty
    private List<Map<String, InstanceDTO>> items;

    public Integer getTook() {
        return took;
    }

    public void setTook(Integer took) {
        this.took = took;
    }

    public boolean isErrors() {
        return errors;
    }

    public void setErrors(boolean errors) {
        this.errors = errors;
    }

    public List<Map<String, InstanceDTO>> getItems() {
        return items;
    }

    public void setItems(List<Map<String, InstanceDTO>> items) {
        this.items = items;
    }
}
