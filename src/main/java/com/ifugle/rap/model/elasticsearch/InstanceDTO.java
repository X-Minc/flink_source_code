/**
 * Copyright(C) 2018 Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.model.elasticsearch;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author LiuZhengyang
 * @version $Id: InstanceDTO.java 84708 2018-11-09 08:12:37Z HuangLei $
 * @since 2018年10月16日 13:49
 */
public class InstanceDTO {

    @JsonProperty("_index")
    private String              index;

    @JsonProperty("_type")
    private String              type;

    @JsonProperty("_id")
    private String              id;

    @JsonProperty
    private InstanceErrorDTO error;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public InstanceErrorDTO getError() {
        return error;
    }

    public void setError(InstanceErrorDTO error) {
        this.error = error;
    }
}
