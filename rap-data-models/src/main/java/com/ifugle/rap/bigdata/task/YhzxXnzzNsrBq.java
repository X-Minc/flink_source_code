package com.ifugle.rap.bigdata.task;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * @author XuWeigang
 * @since 2019年11月04日 11:11
 */
@Data
public class YhzxXnzzNsrBq implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 虚拟组织ID
     */
    @SerializedName("xnzzId")
    private Long xnzzId;

    /**
     * 纳税人ID
     */
    @SerializedName("nsrId")
    private Long nsrId;

    /**
     * 标签ID，按nsrIds分组，以逗号分隔
     */
    @SerializedName("bqIds")
    private String bqIds;
}
