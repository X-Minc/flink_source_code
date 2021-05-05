package com.ifugle.rap.bigdata.task.es;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;
import com.ifugle.rap.elasticsearch.entity.ShareEntity;

import lombok.Data;

/**
 * @author GuanTao
 * @version $Id$
 * @since 2019年07月25日 21:15
 */
@Data
public class SearchResponseEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("_scroll_id")
    private String scrollId;
    @SerializedName("took")
    private int took;
    @SerializedName("timed_out")
    private boolean timedOut;
    @SerializedName("_shards")
    private ShareEntity shards;
    @SerializedName("hits")
    private HitsResponseEntity<T> hits;
}
