package com.ifugle.rap.bigdata.task.es;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * @author GuanTao
 * @version $Id$
 * @since 2019年12月30日 18:46
 */
@Data
public class TaskResultResponseEntity {

    /**
     * 是否结束，true：处理结束，false：处理中
     */
    @SerializedName("completed")
    private Boolean completed;

    /**
     * 任务信息
     */
    @SerializedName("task")
    private TaskEntity task;
}
