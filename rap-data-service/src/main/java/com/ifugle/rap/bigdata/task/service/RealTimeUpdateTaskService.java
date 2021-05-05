package com.ifugle.rap.bigdata.task.service;

/**
 * @author WenYuan
 * @version $
 * @since 5月 05, 2021 19:13
 */
public interface RealTimeUpdateTaskService {

    /**
     * 抽取增量数据到ES并做增量量标签更新和数据汇总
     * @param startTime
     */
    void getUpdateDataToEs(String startTime);

    /**
     * 抽取单个组织全量数据到ES并做增量量标签更新和数据汇总
     * @param xnzzId
     */
    void getUpdateDataToEs(Long xnzzId);
}
