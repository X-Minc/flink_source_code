package com.ifugle.rap.data.task;

/**
 * @author WenYuan
 * @version $
 * @since 5月 05, 2021 18:57
 */

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Conditional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ifugle.rap.bigdata.task.BiDmSwjg;
import com.ifugle.rap.bigdata.task.service.BiDmSwjgService;
import com.ifugle.rap.bigdata.task.service.RealTimeUpdateTaskService;

import lombok.extern.slf4j.Slf4j;

/**
 * 全量抽取新组织的实时数据
 */
@Component
@Slf4j
@Conditional(TaskCondition.class)
public class DepartmentOdsTask {

    @Autowired
    private BiDmSwjgService biDmSwjgService;

    @Autowired
    private RealTimeUpdateTaskService realTimeUpdateTaskService;

    @Scheduled(cron = "0 30 2 * * ?")
    public void action() {
        List<BiDmSwjg> xnzzList = biDmSwjgService.listXnzzForAllInsert();
        if (xnzzList.size() > 0) {
            for (BiDmSwjg xnzz : xnzzList) {
                log.info("全量抽取实时数据开始，xnzzId = {}", xnzz.getXnzzId());
                realTimeUpdateTaskService.getUpdateDataToEs(xnzz.getXnzzId());
                log.info("全量抽取实时数据结束，xnzzId = {}", xnzz.getXnzzId());
            }
        }
    }
}
