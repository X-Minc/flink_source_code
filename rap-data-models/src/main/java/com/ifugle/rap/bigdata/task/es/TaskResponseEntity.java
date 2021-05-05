package com.ifugle.rap.bigdata.task.es;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * @author GuanTao
 * @version $Id$
 * @since 2019年12月30日 17:30
 */
@Data
public class TaskResponseEntity {

    /**
     * 异步处理，返回的任务ID
     */
    @SerializedName("task")
    private String task;
}
