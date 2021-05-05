/**
 * Copyright(C) 2019 Fugle Technology Co., Ltd. All rights reserved.
 */
package com.ifugle.rap.bigdata.task.es;

import java.util.Map;

import org.apache.poi.ss.formula.functions.T;

import com.google.gson.annotations.SerializedName;
import com.ifugle.rap.elasticsearch.entity.HitsEntity;
import com.ifugle.rap.elasticsearch.entity.ShareEntity;

import lombok.Data;

/**
 * @author XingZhe
 * @version $Id$
 * @since 2019年07月29日 14:03
 */
@Data
public class AggsResponseEntity {
    @SerializedName("_scroll_id")
    private String scrollId;
    @SerializedName("took")
    private int took;
    @SerializedName("timed_out")
    private boolean timedOut;
    @SerializedName("_shards")
    private ShareEntity shards;
    @SerializedName("hits")
    private HitsEntity<T> hits;
    @SerializedName("aggregations")
    private Map<String, Object> aggregations;

}

