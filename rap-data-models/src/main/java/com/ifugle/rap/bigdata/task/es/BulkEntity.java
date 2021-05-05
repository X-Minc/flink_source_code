package com.ifugle.rap.bigdata.task.es;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;
import com.ifugle.rap.elasticsearch.entity.ShareEntity;

import lombok.Data;

/**
 * @author GuanTao
 * @version $Id$
 * @since 2019年09月12日 19:16
 */
@Data
public class BulkEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("index")
    private IndexBean index;

    @Data
    public static class IndexBean {

        @SerializedName("_index")
        private String index;
        @SerializedName("_type")
        private String type;
        @SerializedName("_id")
        private String id;
        @SerializedName("_shards")
        private ShareEntity shards;
        @SerializedName("_version")
        private int version;
        @SerializedName("result")
        private String result;
        @SerializedName("created")
        private boolean created;
        @SerializedName("status")
        private int status;
    }
}
