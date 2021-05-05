package com.ifugle.rap.bigdata.task.es;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * @author XuWeigang
 * @since 2021年02月20日 13:53
 */
@Data
public class TotalEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @SerializedName("value")
    private int value;

    @SerializedName("relation")
    private String relation;
}
