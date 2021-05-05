package com.ifugle.rap.bigdata.task.es;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * @author GuanTao
 * @version $Id$
 * @since 2020年02月21日 10:49
 */
@Data
public class TaskStatusEntity {

    /**
     * 总数
     */
    @SerializedName("total")
    private Long total;

    /**
     * 更新数
     */
    @SerializedName("updated")
    private Long updated;

    /**
     * 创建数
     */
    @SerializedName("created")
    private Long created;

    /**
     * 删除数
     */
    @SerializedName("deleted")
    private Long deleted;

    /**
     * 批量处理次数
     */
    @SerializedName("batches")
    private Long batches;
}
