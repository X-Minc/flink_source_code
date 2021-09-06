package com.ifugle.rap.data.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.ifugle.rap.bigdata.task.BiDmSwjg;
import com.ifugle.rap.bigdata.task.DepartOds;
import com.ifugle.rap.bigdata.task.service.BiDmSwjgService;
import com.ifugle.rap.bigdata.task.service.EsDepartAggDwService;
import com.ifugle.rap.bigdata.task.service.EsDepartOdsService;
import com.ifugle.rap.service.impl.DepartAggBmIdQueueRedisService;
import com.ifugle.rap.utils.ListUtil;
import com.ifugle.util.DateUtil;
import com.ifugle.util.NullUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;
import com.ifugle.rap.bigdata.task.service.RealTimeUpdateTaskService;

/**
 * @author WenYuan
 * @version $
 * @since 4月 25, 2021 10:11
 */
@RestController
@RequestMapping(value = "/")
public class ApplicationController {

    @Autowired
    private RealTimeUpdateTaskService realTimeUpdateTaskService;


    @Autowired
    private EsDepartOdsService esDepartOdsService;

    @Autowired
    private BiDmSwjgService biDmSwjgService;

    @Autowired
    private EsDepartAggDwService esDepartAggDwService;

    @Autowired
    private DepartAggBmIdQueueRedisService departAggBmIdQueueRedisService;


    @GetMapping("/hc")
    public String healthy(){
        return "OK";
    }

    /**
     * 抽取增量数据到ES并做增量量标签更新和数据汇总
     * @param startTime
     * @return
     */
    @RequestMapping(value = "/getUpdateDataToEs", method = RequestMethod.GET)
    public Map<String, Object> getUpdateDataToEs(String startTime) {
        Map<String, Object> result = Maps.newHashMapWithExpectedSize(1);
        /***
         * 异步进行执行
         */
        Thread t = new Thread(new Runnable() {
            public void run() {
                realTimeUpdateTaskService.getUpdateDataToEs(startTime);
            }
        });
        t.start();
        result.put("success", true);
        return result;
    }

    /**
     * 按部门汇总数据
     * @return
     */
    @RequestMapping(value = "/aggregateStatisticsByBm", method = RequestMethod.GET)
    public Map<String, Object> aggregateStatisticsByBm() {
        Map<String, Object> result = Maps.newHashMapWithExpectedSize(1);
        List<BiDmSwjg> xnzzList = biDmSwjgService.listXnzzForUpdate();
        esDepartAggDwService.aggregateStatisticsByXnzz(xnzzList);
        result.put("success", true);
        return result;
    }

    /**
     * 将虚拟组织下的所有部门加入汇总队列，定时任务在开启状态时，则会加入汇总
     * @param xnzzId
     * @return
     */
    @RequestMapping(value = "/aggregateStatisticsByXnzz", method = RequestMethod.GET)
    public Map<String, Object> aggregateStatisticsByXnzz(Long xnzzId) {
        Map<String, Object> result = Maps.newHashMapWithExpectedSize(1);

        List<DepartOds> departList = esDepartOdsService.listDepart(xnzzId, null);
        List<Long> bmIdList = departList.stream().map(DepartOds::getId).collect(Collectors.toList());
        List<List<Long>> splitList = ListUtil.split(bmIdList, 10000);
        for (List<Long> bmIds : splitList) {
            departAggBmIdQueueRedisService.addBmIdToQueue(bmIds);
        }

        result.put("success", true);
        return result;
    }

    /**
     * 抽取单个组织全量数据到ES并做增量量标签更新和数据汇总
     * @param xnzzId
     * @return
     */
    @RequestMapping(value = "/getUpdateDataByXnzzToEs", method = RequestMethod.GET)
    public Map<String, Object> getUpdateDataToEs(Long xnzzId) {
        Map<String, Object> result = Maps.newHashMapWithExpectedSize(1);
        realTimeUpdateTaskService.getUpdateDataToEs(xnzzId);
        result.put("success", true);
        return result;
    }


    /**
     * 抽取多个组织全量数据到ES并做增量量标签更新和数据汇总
     * @param xnzzIdStr
     * @return
     */
    @RequestMapping(value = "/getUpdateDataByXnzzStrToEs", method = RequestMethod.GET)
    public Map<String, Object> getUpdateDataByXnzzStrToEs(String xnzzIdStr) {
        Map<String, Object> result = Maps.newHashMapWithExpectedSize(1);
        String[] xnzzIds = null;
        if (NullUtil.isNotNull(xnzzIdStr)) {
            xnzzIds = xnzzIdStr.split(",");
        }
        if (xnzzIds != null && xnzzIds.length>0) {
            for (String tempXnzzId : xnzzIds) {
                Long xnzzId = Long.parseLong(tempXnzzId.trim());
                realTimeUpdateTaskService.getUpdateDataToEs(xnzzId);
            }
        }
        result.put("success", true);
        return result;
    }
}
