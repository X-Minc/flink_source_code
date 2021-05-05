package com.ifugle.rap.bigdata.task.es;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

/**
 * @author GuanTao
 * @version $Id$
 * @since 2020年02月21日 10:38
 */
@Data
public class TaskEntity {

    /**
     * node:id = 任务ID
     */
    @SerializedName("node")
    private String node;

    @SerializedName("id")
    private Long id;

    @SerializedName("type")
    private String type;

    @SerializedName("action")
    private String action;

    /**
     * 任务状态，处理条数信息
     */
    @SerializedName("status")
    private TaskStatusEntity status;

    @SerializedName("description")
    private String description;

    @SerializedName("start_time_in_millis")
    private Long startTimeInMillis;

    @SerializedName("running_time_in_nanos")
    private Long runningTimeInNanos;

    @SerializedName("cancellable")
    private Boolean cancellable;
}
